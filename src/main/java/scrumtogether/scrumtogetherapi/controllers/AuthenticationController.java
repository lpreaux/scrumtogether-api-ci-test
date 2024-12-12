package scrumtogether.scrumtogetherapi.controllers;

import jakarta.validation.Valid;
import scrumtogether.scrumtogetherapi.dtos.SignInResponse;
import scrumtogether.scrumtogetherapi.services.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import scrumtogether.scrumtogetherapi.dtos.RegistrationDto;
import scrumtogether.scrumtogetherapi.dtos.SignInRequest;
import scrumtogether.scrumtogetherapi.entities.User;
import scrumtogether.scrumtogetherapi.services.AuthenticationService;

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

    @GetMapping("test-secured")
    public ResponseEntity<String> testSecured() {
        return new ResponseEntity<>("Secured endpoint", null, HttpStatus.OK);
    }
}
