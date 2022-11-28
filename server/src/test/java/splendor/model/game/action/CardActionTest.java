package splendor.model.game.action;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import splendor.model.game.card.DevelopmentCard;
import splendor.model.game.card.SplendorCard;

public class CardActionTest {
  SplendorCard card = DevelopmentCard.get(1);
  CardActionType actionType = CardActionType.BUY;
  CardAction cardAction;

  @Before
  public void setUp() {
    cardAction = getNewCardAction(card, actionType);
  }

  private CardAction getNewCardAction(SplendorCard card, CardActionType actionType) {
    try {
      Constructor<DevelopmentCardAction> constructor = DevelopmentCardAction.class
          .getDeclaredConstructor(CardActionType.class, SplendorCard.class);
      constructor.setAccessible(true);
      return constructor.newInstance(actionType, card);
    } catch (NoSuchMethodException | InstantiationException | InvocationTargetException |
             IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void testGetType() {
    assertEquals(actionType, cardAction.getType());
  }

  @Test
  public void testGetCard() {
    assertEquals(card, cardAction.getCard());
  }

  @Test
  public void testIdsAreUnique() {
    assertNotEquals(getNewCardAction(card, actionType).getId(),
        getNewCardAction(card, actionType).getId());
  }
}
