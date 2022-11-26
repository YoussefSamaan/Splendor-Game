package splendor.controller.game.action;

import java.util.HashMap;
import splendor.model.game.Color;

/**
 * Class to store all relevant data for an action.
 */
public class ActionData {
  private final HashMap<Color, Integer> payment;

  /**
   * Creates a new action data.
   *
   * @param payment the payment
   */
  public ActionData(HashMap<Color, Integer> payment) {
    this.payment = payment;
  }


}
