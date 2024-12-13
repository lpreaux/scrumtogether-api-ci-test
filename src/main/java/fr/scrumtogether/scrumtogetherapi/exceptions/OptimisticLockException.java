package fr.scrumtogether.scrumtogetherapi.exceptions;

public class OptimisticLockException extends ApplicationException {
    public OptimisticLockException(String message) {
        super(message);
    }

    public OptimisticLockException(String message, Throwable cause) {
        super(message, cause);
    }
}
