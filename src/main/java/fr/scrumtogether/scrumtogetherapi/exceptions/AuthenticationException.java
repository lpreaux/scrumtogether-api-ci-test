package fr.scrumtogether.scrumtogetherapi.exceptions;

/**
 * Exception thrown when an authentication process fails.
 * <p>
 * This exception is typically used to signal issues encountered
 * during user authentication, such as invalid credentials or
 * when a user cannot be found in the system.
 * <p>
 * Extends {@link ApplicationException}
 */
public class AuthenticationException extends ApplicationException {
    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
