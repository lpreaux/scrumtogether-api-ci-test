package fr.scrumtogether.scrumtogetherapi.security;

import fr.scrumtogether.scrumtogetherapi.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Service implementation for managing user details used in Spring Security.
 * This class implements the {@code UserDetailsService} interface, which is a core interface
 * used by Spring Security to retrieve user-related data.
 * <p>
 * The primary responsibility of this class is to load a user's details (such as username,
 * password, and granted authorities) from the database or another data source, using a
 * {@code UserRepository}. The loaded user details are then utilized in the authentication process.
 */
@RequiredArgsConstructor
@Service
public class STUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    /**
     * Loads a user's details by their username.
     *
     * @param username the username of the user whose details should be loaded; must not be null or empty.
     * @return the {@code UserDetails} object containing the user's information.
     * @throws UsernameNotFoundException if a user with the given username is not found.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsernameAndDeletedAtIsNull(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }
}
