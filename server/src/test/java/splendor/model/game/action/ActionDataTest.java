package splendor.model.game.action;

import java.util.HashMap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import splendor.model.game.Color;

public class ActionDataTest {

  @Test
  void creatingActionDataObjects() {
    HashMap<Color, Integer> payment = new HashMap<>();
    payment.put(Color.RED,3);
    ActionData actionData1 = new ActionData(payment);
    ActionData actionData2 = new ActionData();

    Assertions.assertEquals(actionData1.getPayment(), payment);
    Assertions.assertEquals(actionData2.getPayment(), new HashMap<>());
    Assertions.assertEquals(actionData1.getPayment().get(Color.RED), 3);
  }

}
