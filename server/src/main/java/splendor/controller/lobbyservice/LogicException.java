package splendor.controller.lobbyservice;

/**
 * Custom Exception that is fired whenever the logic is instructed to handle parameters that
 * semantically are not applicable.
 *
 * @Author Maximilian Schiedermeier
 * @Date December 2020
 */
public class LogicException extends Exception {

  public LogicException(String cause) {
    super(cause);
  }
}

