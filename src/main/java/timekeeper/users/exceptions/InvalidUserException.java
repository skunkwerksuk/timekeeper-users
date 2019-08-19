package timekeeper.users.exceptions;

public class InvalidUserException extends RuntimeException {
  private static final long serialVersionUID = 6152704507769043665L;

  public InvalidUserException(final String message) {
    super(message);
  }
}
