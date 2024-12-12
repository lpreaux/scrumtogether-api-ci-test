package scrumtogether.scrumtogetherapi.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import scrumtogether.scrumtogetherapi.dtos.RegistrationDto;
import scrumtogether.scrumtogetherapi.services.AuthenticationService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody @Valid RegistrationDto registrationDto) {
        authenticationService.register(registrationDto);
        return new ResponseEntity<>("Inscription réussi", null, HttpStatus.CREATED);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<Object> signIn() {
        return new ResponseEntity<>("null", null, HttpStatus.BAD_REQUEST);
    }
}
