package fr.scrumtogether.scrumtogetherapi.services;

import com.google.common.util.concurrent.RateLimiter;
import fr.scrumtogether.scrumtogetherapi.config.RateLimitConfig;
import fr.scrumtogether.scrumtogetherapi.dtos.UserDto;
import fr.scrumtogether.scrumtogetherapi.entities.User;
import fr.scrumtogether.scrumtogetherapi.entities.enums.Role;
import fr.scrumtogether.scrumtogetherapi.exceptions.*;
import fr.scrumtogether.scrumtogetherapi.repositories.UserRepository;
import fr.scrumtogether.scrumtogetherapi.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Slf4j
@Service
@Validated
@PreAuthorize("isAuthenticated()")
public class UserService {
    private final UserRepository userRepository;
    private final SecurityUtils securityUtils;
    private final RateLimitConfig rateLimitConfig;
    private final AuditService auditService;

    @Transactional(readOnly = true)
    public Page<User> getAll(Integer pageNumber, Integer pageSize) {
        log.debug("Getting all users - paginated");

        // Validate and set default page number
        int validatedPageNumber = Optional.ofNullable(pageNumber)
                .filter(page -> page > 0)
                .orElse(0);

        // Validate and set default page size with upper bound
        int validatedPageSize = Optional.ofNullable(pageSize)
                .filter(size -> size > 0 && size <= 100)
                .orElse(20);

        log.debug("Using page number: {}, page size: {}", validatedPageNumber, validatedPageSize);

        PageRequest pageRequest = PageRequest.of(validatedPageNumber, validatedPageSize);
        return userRepository.findAll(pageRequest);
    }

    @Transactional(readOnly = true)
    public User getById(Long id) {
        return userRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public User update(Long id, UserDto userDto) {
        log.info("Starting update process for user with ID: {}", id);
        try {
            checkRateLimit();
            log.debug("Rate limit check passed for user update");

            validateUpdateRequest(id, userDto);
            log.debug("Update request validation passed");

            User user = getById(id);
            User userBeforeUpdate = createUserCopy(user);

            log.debug("Validating update permissions and business rules");
            validateUpdatePermissions(user, userDto);
            validateBusinessRules(user, userDto);

            log.debug("Handling sensitive data updates");
            handleSensitiveDataUpdate(user, userDto);

            log.debug("Creating audit log for user update");
            auditService.logUpdate("USER", id, userBeforeUpdate, user);

            log.info("Successfully updated user with ID: {}", id);
            return user;

        } catch (Exception e) {
            log.error("Failed to update user with ID: {}. Error: {}", id, e.getMessage());
            throw e;
        }
    }

    private void handleSensitiveDataUpdate(User user, UserDto userDto) {
        log.debug("Processing sensitive data updates for user: {}", user.getId());
        boolean isAdmin = securityUtils.isCurrentUserAdmin();

        if (userDto.getEmail() != null) {
            log.debug("Processing email update for user: {}", user.getId());
            handleEmailUpdate(user, userDto.getEmail());
        }

        if (userDto.getRole() != null) {
            log.debug("Processing role update for user: {}", user.getId());
            handleRoleUpdate(user, userDto.getRole(), isAdmin);
        }

        log.debug("Processing non-sensitive updates for user: {}", user.getId());
        handleNonSensitiveUpdates(user, userDto);
    }

    private void handleEmailUpdate(User user, String newEmail) {
        if (!user.getEmail().equals(newEmail)) {
            log.info("Email change requested for user: {} from {} to {}",
                    user.getId(),
                    user.getEmail(),
                    newEmail);

            String sanitizedEmail = sanitizeEmail(newEmail);
            validateEmail(sanitizedEmail);

            user.setEmail(sanitizedEmail);
            user.setVerifiedEmail(false);
            log.info("Email updated and verification status reset for user: {}", user.getId());

            // TODO: Send verification email
        }
    }

    private void handleRoleUpdate(User user, Role newRole, boolean isAdmin) {
        if (!isAdmin) {
            throw new AccessDeniedException("Only administrators can modify roles");
        }

        if (user.getRole() != newRole) {
            // Log role change
            logSensitiveFieldChange("role", user.getRole(), newRole);
            user.setRole(newRole);
        }
    }

    private void handleNonSensitiveUpdates(User user, UserDto userDto) {
        // Update non-sensitive fields
        if (userDto.getFirstName() != null) {
            user.setFirstName(userDto.getFirstName().trim());
        }
        if (userDto.getLastName() != null) {
            user.setLastName(userDto.getLastName().trim());
        }
    }

    private String sanitizeEmail(String email) {
        if (email == null) return null;
        return email.toLowerCase().trim();
    }

    private void validateEmail(String email) {
        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new ValidationException("Invalid email format");
        }

        if (userRepository.existsByUsernameIgnoreCaseAndDeletedAtIsNull(email)) {
            throw new ValidationException("Email already in use");
        }
    }

    private void logSensitiveFieldChange(String fieldName, Object oldValue, Object newValue) {
        log.info("Sensitive field '{}' changed by user '{}'. Old value: {}, New value: {}",
                fieldName,
                securityUtils.getCurrentUser().getUsername(),
                oldValue,
                newValue
        );
    }

    private void checkRateLimit() {
        String key = "user-update:" + securityUtils.getCurrentUser().getUsername();
        RateLimiter rateLimiter = rateLimitConfig.getRateLimiter(key);

        if (!rateLimiter.tryAcquire(5, TimeUnit.SECONDS)) {
            throw new RateLimitExceededException(
                    "Too many update requests. Please wait before trying again."
            );
        }
    }

    private void validateUpdatePermissions(User user, UserDto userDto) {
        User currentUser = securityUtils.getCurrentUser();

        // Self-update checks
        if (user.getId().equals(currentUser.getId())) {
            validateSelfUpdate(user, userDto);
            return;
        }

        // Admin checks
        if (!securityUtils.isCurrentUserAdmin()) {
            throw new AccessDeniedException("Only admins can update other users");
        }
    }

    private void validateSelfUpdate(User user, UserDto userDto) {
        // Users cannot change their own role
        if (userDto.getRole() != null && !userDto.getRole().equals(user.getRole())) {
            throw new AccessDeniedException("Users cannot modify their own role");
        }

        // Users cannot change their own verification status
        if (userDto.getVerifiedEmail() != null &&
                !userDto.getVerifiedEmail().equals(user.getVerifiedEmail())) {
            throw new AccessDeniedException("Users cannot modify their own verification status");
        }
    }

    private void validateUpdateRequest(Long id, UserDto userDto) {
        log.debug("Validating update request for user ID: {}", id);

        if (userDto == null) {
            log.warn("Update request rejected - null UserDto for ID: {}", id);
            throw new ValidationException("User data cannot be null");
        }

        if (!id.equals(userDto.getId())) {
            log.warn("Update request rejected - ID mismatch. Path ID: {}, DTO ID: {}", id, userDto.getId());
            throw new ValidationException("ID in path and body must match");
        }

        User currentUser = getById(id);
        if (!currentUser.getVersion().equals(userDto.getVersion())) {
            log.warn("Update request rejected - Version mismatch for user: {}. Expected: {}, Received: {}",
                    id, currentUser.getVersion(), userDto.getVersion());
            throw new OptimisticLockException("User has been modified by another transaction");
        }

        log.debug("Update request validation passed for user ID: {}", id);
    }

    private void validateBusinessRules(User user, UserDto userDto) {
        log.debug("Validating business rules for user: {}", user.getId());

        validateUniqueConstraints(user, userDto);
        validateUserStatus(user);

        log.debug("Business rules validation passed for user: {}", user.getId());
    }

    private void validateUniqueConstraints(User user, UserDto userDto) {
        log.debug("Checking unique constraints for user: {}", user.getId());

        if (userDto.getEmail() != null &&
                !userDto.getEmail().equalsIgnoreCase(user.getEmail())) {

            if (userRepository.existsByUsernameIgnoreCaseAndDeletedAtIsNull(userDto.getEmail())) {
                log.warn("Email uniqueness validation failed - email already in use: {}", userDto.getEmail());
                throw new ValidationException("Email already in use");
            }
        }

        if (userDto.getUsername() != null &&
                !userDto.getUsername().equalsIgnoreCase(user.getUsername())) {

            if (userRepository.existsByEmailIgnoreCaseAndDeletedAtIsNull(userDto.getUsername())) {
                log.warn("Username uniqueness validation failed - username already in use: {}", userDto.getUsername());
                throw new ValidationException("Username already in use");
            }
        }
    }

    private void validateUserStatus(User user) {
        if (user.getDeletedAt() != null) {
            throw new ValidationException("Cannot update deleted user");
        }
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public void delete(Long id) {
        log.info("Starting deletion process for user with ID: {}", id);

        User user = getById(id);

        validateDeletionPermissions(user);
        validateDeletionBusinessRules(user);

        softDelete(user);

        auditService.logDelete("USER", id, user);

        log.info("Successfully soft-deleted user with ID: {}", id);
    }

    private void validateDeletionPermissions(User user) {
        User currentUser = securityUtils.getCurrentUser();

        // Cannot delete your own account if you're the last admin
        if (user.getId().equals(currentUser.getId()) &&
                user.isAdmin() &&
                isLastAdmin()) {
            throw new ValidationException("Cannot delete the last admin account");
        }

        // Only admins can delete other users
        if (!user.getId().equals(currentUser.getId()) &&
                !securityUtils.isCurrentUserAdmin()) {
            throw new AccessDeniedException("Only admins can delete other users");
        }
    }

    private void validateDeletionBusinessRules(User user) {
        // Check if user is already deleted
        if (user.isDeleted()) {
            throw new ValidationException("User is already deleted");
        }

        // Check if user has active teams/projects
        if (!user.getTeamUsers().isEmpty() || !user.getProjectUsers().isEmpty()) {
            throw new ValidationException(
                    "User cannot be deleted while being a member of teams or projects"
            );
        }
    }

    private void softDelete(User user) {
        user.setDeletedAt(LocalDateTime.now());
        user.setDeletedBy(securityUtils.getCurrentUser().getUsername());

        // TODO invalidate or expire user tokens
        // TODO send notification email
    }

    private boolean isLastAdmin() {
        return userRepository.countByRoleAndDeletedAtIsNull(Role.ADMIN) <= 1;
    }

    // Add method to restore deleted users if needed
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public User restore(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (!user.isDeleted()) {
            throw new ValidationException("User is not deleted");
        }

        user.setDeletedAt(null);
        user.setDeletedBy(null);

        auditService.logRestore("USER", id, user);

        return user;
    }

    private User createUserCopy(User user) {
        return User.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .username(user.getUsername())
                .role(user.getRole())
                .verifiedEmail(user.getVerifiedEmail())
                .version(user.getVersion())
                .build();
    }
}
