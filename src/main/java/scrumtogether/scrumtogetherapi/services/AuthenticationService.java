package scrumtogether.scrumtogetherapi.services;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final AuthenticationManager authManager;

    /**
     * Registers a new user in the system by converting the provided registration details
     * to a user entity and saving it to the repository.
     *
     * @param registrationDto the details required to register a new user.
     *                        It includes the user's first name, last name, email, username,
     *                        password, and confirmation password. The parameter must be
     *                        valid and not null.
     */
    public void register(@Valid RegistrationDto registrationDto) {
        User user = userMapper.toEntity(registrationDto);
        userRepository.save(user);
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
        try {
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            signInRequest.getUsername(),
                            signInRequest.getPassword()
                    )
            );

            // Cast the principal to your UserDetails implementation
            if (authentication.getPrincipal() instanceof User user) {
                return user;
            }

            // Fallback to database query if needed
            return userRepository.findByUsername(authentication.getName())
                    .orElseThrow(() -> new AuthenticationException("Authentication failed"));

        } catch (BadCredentialsException e) {
            throw new AuthenticationException("Invalid credentials");
        } catch (DisabledException e) {
            throw new AuthenticationException("Account is disabled");
        } catch (LockedException e) {
            throw new AuthenticationException("Account is locked");
        } catch (Exception e) {
            throw new AuthenticationException("Authentication failed");
        }
    }
}
