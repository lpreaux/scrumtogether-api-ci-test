package fr.scrumtogether.scrumtogetherapi.controllers;

import fr.scrumtogether.scrumtogetherapi.dtos.ErrorResponse;
import fr.scrumtogether.scrumtogetherapi.exceptions.AuthenticationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

/**
 * Global exception handler for the application.
 * Provides centralized handling of exceptions across all controllers.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final String DEFAULT_GENERAL_ERROR_MESSAGE = "An internal error occurred";
    private static final String VALIDATION_ERROR_MESSAGE = "Validation failed";
    private static final String AUTH_ERROR_MESSAGE = "Authentication failed";

    /**
     * Handles validation exceptions thrown during request processing.
     *
     * @param ex the MethodArgumentNotValidException
     * @return ResponseEntity containing validation error details
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        log.warn("Validation error: {}", ex.getMessage());

        List<String> errors = new ArrayList<>();

        // Add field validation errors
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.add(error.getField() + ": " + error.getDefaultMessage())
        );

        // Add global validation errors (like @PasswordMatching)
        ex.getBindingResult().getGlobalErrors().forEach(error ->
                errors.add(error.getDefaultMessage())
        );

        return ResponseEntity
                .badRequest()
                .body(new ErrorResponse(VALIDATION_ERROR_MESSAGE, errors));
    }

    /**
     * Handles authentication related exceptions.
     *
     * @param ex the AuthenticationException
     * @return ResponseEntity containing authentication error details
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException ex) {
        log.warn("Authentication error: {}", ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse(AUTH_ERROR_MESSAGE, ex.getMessage()));
    }

    /**
     * Handles general exceptions not caught by other handlers.
     *
     * @param ex the Exception
     * @return ResponseEntity containing error details
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
        log.error("Internal server error", ex);

        String message = StringUtils.hasText(ex.getMessage())
                ? ex.getMessage()
                : DEFAULT_GENERAL_ERROR_MESSAGE;

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(DEFAULT_GENERAL_ERROR_MESSAGE, message));
    }
}
