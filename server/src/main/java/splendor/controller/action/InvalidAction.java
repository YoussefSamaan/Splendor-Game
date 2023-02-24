package splendor.controller.action;

/**
 * Exception that is thrown when an action is invalid.
 */
public class InvalidAction extends Exception {
  public InvalidAction(String message) {
    super(message);
  }
}
