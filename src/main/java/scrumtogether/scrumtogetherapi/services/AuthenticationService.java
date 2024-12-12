package scrumtogether.scrumtogetherapi.services;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import scrumtogether.scrumtogetherapi.dtos.RegistrationDto;
import scrumtogether.scrumtogetherapi.entities.User;
import scrumtogether.scrumtogetherapi.repositories.UserRepository;
import scrumtogether.scrumtogetherapi.services.mappers.UserMapper;

@RequiredArgsConstructor
@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public void register(@Valid RegistrationDto registrationDto) {
        User user = userMapper.toEntity(registrationDto);
        userRepository.save(user);
    }
}
