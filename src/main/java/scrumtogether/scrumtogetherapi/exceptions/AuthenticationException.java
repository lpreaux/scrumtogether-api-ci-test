package scrumtogether.scrumtogetherapi.exceptions;

/**
 * Exception thrown when an authentication process fails.
 * <p>
 * This exception is typically used to signal issues encountered
 * during user authentication, such as invalid credentials or
 * when a user cannot be found in the system.
 * <p>
 * Extends {@link RuntimeException} to allow unchecked exceptions
 * for flexibility in handling authentication errors.
 */
public class AuthenticationException extends RuntimeException {
    public AuthenticationException(String message) {
        super(message);
    }
}
