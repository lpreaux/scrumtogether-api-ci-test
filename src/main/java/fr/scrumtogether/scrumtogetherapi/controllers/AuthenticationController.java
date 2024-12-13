package fr.scrumtogether.scrumtogetherapi.controllers;

import fr.scrumtogether.scrumtogetherapi.dtos.RegistrationDto;
import fr.scrumtogether.scrumtogetherapi.dtos.SignInRequest;
import fr.scrumtogether.scrumtogetherapi.dtos.SignInResponse;
import fr.scrumtogether.scrumtogetherapi.entities.User;
import fr.scrumtogether.scrumtogetherapi.services.AuthenticationService;
import fr.scrumtogether.scrumtogetherapi.services.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final JwtService jwtService; // Ensure JwtService is implemented properly in the services package

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody @Valid RegistrationDto registrationDto) {
        authenticationService.register(registrationDto);
        return new ResponseEntity<>("Inscription réussi", null, HttpStatus.CREATED);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<SignInResponse> signIn(@RequestBody @Valid SignInRequest signInRequest) {
        User authenticated = authenticationService.authenticate(signInRequest);
        String jwt = jwtService.generateToken(authenticated);

        SignInResponse response = SignInResponse.builder()
                .token(jwt)
                .expiresIn(jwtService.getExpirationTime())
                .build();

        return new ResponseEntity<>(response, null, HttpStatus.OK);
    }
}
