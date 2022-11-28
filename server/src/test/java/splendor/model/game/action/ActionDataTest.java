package splendor.model.game.action;

import java.util.HashMap;
import org.junit.Before;
import org.junit.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import splendor.model.game.Color;

public class ActionDataTest {

  private final HashMap<Color, Integer> payment = new HashMap<>();

  @Before
  public void setUp() {
      payment.put(Color.BLUE, 1);
      payment.put(Color.GREEN, 2);
      payment.put(Color.WHITE, 3);
  }

  @Test
  public void testActionData() {
    new ActionData(payment);
  }

  @Test
  public void testActionDataEmptyConstructor() {
    new ActionData();
  }

  @Test
  public void testGetPayment() {
    assertEquals(payment, new ActionData(payment).getPayment());
  }
}
