package fr.scrumtogether.scrumtogetherapi.exceptions;

public class AccessDeniedException extends ApplicationException {
    public AccessDeniedException(String message) {
        super(message);
    }

    public AccessDeniedException(String message, Throwable cause) {
        super(message, cause);
    }
}
