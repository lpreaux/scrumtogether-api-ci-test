package fr.scrumtogether.scrumtogetherapi.exceptions;

public class RateLimitExceededException extends ApplicationException {
    public RateLimitExceededException(String message) {
        super(message);
    }
    public RateLimitExceededException(String message, Throwable cause) {
        super(message, cause);
    }
}


