package splendor.model.game.card;

import org.json.JSONObject;
import splendor.model.game.Color;

/**
 * Flyweight class for a development card.
 */
public class DevelopmentCard extends AbstractCard implements DevelopmentCardI {
  private final Color color;
  private static final int NUMBER_OF_CARDS = 120;
  private static final DevelopmentCard[] CARDS = new DevelopmentCard[NUMBER_OF_CARDS];

  /**
   * Creates a new card. The Card values are based on the cardId.
   *
   * @param cardId the card id.
   */
  private DevelopmentCard(int cardId) {
    super(cardId);
    color = getColorFromJson(cardId);
  }

  @Override
  public Color getColor() {
    return color;
  }

  /**
   * Returns the development card with the given id.
   *
   * @param cardId the id of the development card.
   * @return the development card with the given id.
   */
  public static DevelopmentCard get(int cardId) {
    if (cardId < 1 || cardId > NUMBER_OF_CARDS) {
      throw new IllegalArgumentException("The card id must be between 1 and " + NUMBER_OF_CARDS);
    }
    int index = cardId - 1; // to get the index of the array
    if (CARDS[index] == null) {
      CARDS[index] = new DevelopmentCard(cardId);
    }
    return CARDS[index];
  }

  private Color getColorFromJson(int cardId) {
    JSONObject map = super.getJson().getJSONObject("color_map");
    for (String key : map.keySet()) {
      if (map.getJSONArray(key).getInt(0) <= cardId
              && map.getJSONArray(key).getInt(1) >= cardId) {
        if (key.contains("red")) {
          key = "red";
        }
        return Color.valueOf(key.toUpperCase());
      }
    }
    throw new IllegalArgumentException("The card id must be between 1 and " + NUMBER_OF_CARDS);
  }
}
