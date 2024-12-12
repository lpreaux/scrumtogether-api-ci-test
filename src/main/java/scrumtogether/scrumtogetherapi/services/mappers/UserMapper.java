package scrumtogether.scrumtogetherapi.services.mappers;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import scrumtogether.scrumtogetherapi.dtos.RegistrationDto;
import scrumtogether.scrumtogetherapi.entities.User;

@AllArgsConstructor
@Service
public class UserMapper {
    private final PasswordEncoder passwordEncoder;

    public User toEntity(RegistrationDto registrationDto) {
        return User.builder()
                .lastName(registrationDto.getLastName())
                .firstName(registrationDto.getFirstName())
                .email(registrationDto.getEmail())
                .username(registrationDto.getUsername())
                .password(passwordEncoder.encode(registrationDto.getPassword()))
                .build();
    }
}
