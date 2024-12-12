package scrumtogether.scrumtogetherapi.services.mappers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import scrumtogether.scrumtogetherapi.dtos.RegistrationDto;
import scrumtogether.scrumtogetherapi.entities.User;
import scrumtogether.scrumtogetherapi.entities.enums.Role;

@AllArgsConstructor
@Slf4j
@Service
public class UserMapper {
    private final PasswordEncoder passwordEncoder;

    /**
     * Converts a registration DTO to a User entity with encoded password.
     * Also performs data sanitization and validation.
     *
     * @param registrationDto the registration details
     * @return the mapped User entity
     */
    public User toEntity(RegistrationDto registrationDto) {
        log.debug("Mapping registration request for user: {}", registrationDto.getUsername());

        return User.builder()
                .lastName(registrationDto.getLastName().trim())
                .firstName(registrationDto.getFirstName().trim())
                .email(registrationDto.getEmail().trim().toLowerCase())
                .username(registrationDto.getUsername().trim())
                .password(passwordEncoder.encode(registrationDto.getPassword()))
                .verifiedEmail(false)
                .role(Role.DEFAULT)
                .build();
    }
}
