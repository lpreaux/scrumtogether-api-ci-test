package scrumtogether.scrumtogetherapi.services;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import scrumtogether.scrumtogetherapi.dtos.RegistrationDto;
import scrumtogether.scrumtogetherapi.dtos.SignInRequest;
import scrumtogether.scrumtogetherapi.entities.User;
import scrumtogether.scrumtogetherapi.exceptions.AuthenticationException;
import scrumtogether.scrumtogetherapi.repositories.UserRepository;
import scrumtogether.scrumtogetherapi.services.mappers.UserMapper;

@RequiredArgsConstructor
@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final AuthenticationManager authManager;

    public void register(@Valid RegistrationDto registrationDto) {
        User user = userMapper.toEntity(registrationDto);
        userRepository.save(user);
    }

    public User authenticate(@Valid SignInRequest signInRequest) {
        Authentication authenticate = authManager.authenticate(new UsernamePasswordAuthenticationToken(signInRequest.getUsername(), signInRequest.getPassword()));
        return userRepository.findByUsername(authenticate.getName())
                .orElseThrow(() -> new AuthenticationException("Cannot find user with username " + signInRequest.getUsername()));
    }
}
