package scrumtogether.scrumtogetherapi.services;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import scrumtogether.scrumtogetherapi.dtos.RegistrationDto;
import scrumtogether.scrumtogetherapi.dtos.SignInRequest;
import scrumtogether.scrumtogetherapi.entities.User;
import scrumtogether.scrumtogetherapi.exceptions.AuthenticationException;
import scrumtogether.scrumtogetherapi.repositories.UserRepository;
import scrumtogether.scrumtogetherapi.services.mappers.UserMapper;

/**
 * Service responsible for user authentication and registration operations.
 * <p>
 * This class provides methods to register new users by saving their details to the database
 * and authenticate existing users based on their credentials. It utilizes components such as
 * {@code UserRepository}, {@code UserMapper}, and {@code AuthenticationManager} to perform these operations.
 * Validation is enforced via annotations on method parameters to ensure data integrity.
 */
@RequiredArgsConstructor
@Slf4j
@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final AuthenticationManager authManager;

    /**
     * Registers a new user in the system with validation checks.
     *
     * @param registrationDto the registration details
     * @throws AuthenticationException if registration fails due to duplicate username or email
     */
    @Transactional
    public void register(@Valid RegistrationDto registrationDto) {
        log.debug("Processing registration request for user: {}", registrationDto.getUsername());

        // Check for existing username
        if (userRepository.existsByUsernameIgnoreCase(registrationDto.getUsername().trim().toLowerCase())) {
            log.warn("Registration failed - Username already exists: {}", registrationDto.getUsername());
            throw new AuthenticationException("Username already exists");
        }

        // Check for existing email
        if (userRepository.existsByEmailIgnoreCase(registrationDto.getEmail().trim().toLowerCase())) {
            log.warn("Registration failed - Email already exists: {}", registrationDto.getEmail());
            throw new AuthenticationException("Email already exists");
        }

        try {
            User user = userMapper.toEntity(registrationDto);
            userRepository.save(user);
            log.info("User successfully registered: {}", user.getUsername());

            // Here you could also:
            // - Send verification email
            // - Create initial user settings
            // - Send welcome notification

        } catch (Exception e) {
            log.error("Unexpected error during registration for user: {}", registrationDto.getUsername(), e);
            throw new AuthenticationException("Registration failed");
        }
    }

    /**
     * Authenticates a user based on the provided sign-in request details.
     *
     * @param signInRequest the details of the user's sign-in request
     * @return the authenticated User object
     * @throws AuthenticationException if authentication fails
     */
    @Transactional(readOnly = true)
    public User authenticate(@Valid SignInRequest signInRequest) {
        log.debug("Authentication attempt for user: {}", signInRequest.getUsername());

        try {
            Authentication authenticate = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            signInRequest.getUsername(),
                            signInRequest.getPassword()
                    )
            );
            log.info("User successfully authenticated: {}", signInRequest.getUsername());

            return userRepository.findByUsername(authenticate.getName())
                    .orElseThrow(() -> {
                        log.error("User not found after successful authentication: {}", signInRequest.getUsername());
                        return new AuthenticationException("Cannot find user with username " + signInRequest.getUsername());
                    });

        } catch (BadCredentialsException e) {
            log.warn("Failed authentication attempt for user: {} - Invalid credentials", signInRequest.getUsername());
            throw new AuthenticationException("Invalid credentials");
        } catch (DisabledException e) {
            log.warn("Failed authentication attempt for user: {} - Account is disabled", signInRequest.getUsername());
            throw new AuthenticationException("Account is disabled");
        } catch (LockedException e) {
            log.warn("Failed authentication attempt for user: {} - Account is locked", signInRequest.getUsername());
            throw new AuthenticationException("Account is locked");
        } catch (Exception e) {
            log.error("Unexpected error during authentication for user: {}", signInRequest.getUsername(), e);
            throw new AuthenticationException("Authentication failed");
        }
    }
}
