package fr.scrumtogether.scrumtogetherapi.exceptions;

public class EntityNotFoundException extends ApplicationException {
  public EntityNotFoundException(String message) {
    super(message);
  }

  public EntityNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}
