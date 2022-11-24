package splendor.model.game.card;

import java.util.HashMap;
import org.json.JSONObject;
import splendor.Config;
import splendor.model.game.Color;
import splendor.model.game.payment.Bonus;
import splendor.model.game.payment.Cost;

/**
 * A splendor card.
 * Cards are immutable.
 */
public abstract class AbstractCard implements SplendorCard {

  private static final String CARDS_JSON = Config.getProperty("cards.json.location");
  private final int cardId;
  private final Cost cost;
  private final int prestigePoints;
  private final Bonus bonus;

  /**
   * Creates a new card. The Card values are based on the cardId.
   *
   * @param cardId the card id.
   */
  protected AbstractCard(int cardId) {
    this.cardId = cardId;
    this.cost = getCostFromJson();
    this.prestigePoints = getPrestigePointsFromJson();
    this.bonus = getBonusFromJson();
  }

  @Override
  public Cost getCost() {
    return cost;
  }

  @Override
  public int getPrestigePoints() {
    return prestigePoints;
  }

  @Override
  public int getCardId() {
    return cardId;
  }

  @Override
  public Bonus getBonus() {
    // Creates a copy to keep cards immutable.
    return bonus.copy();
  }

  /**
   * Returns the cost of the card based on the cardId.
   *
   * @return the cost of the card.
   */
  private Cost getCostFromJson() {
    JSONObject cost = getCardJson().getJSONObject("cost");
    HashMap<Color, Integer> costMap = new HashMap<>();
    for (Color color : Color.values()) {
      if (cost.has(color.toString())) {
        costMap.put(color, cost.getInt(color.toString()));
      }
    }
    return new Cost(costMap);
  }

  /**
   * Returns the prestige points of the card based on the cardId.
   *
   * @return the prestige points of the card.
   */
  private int getPrestigePointsFromJson() {
    if (getCardJson().has("prestigePoints")) {
      return getCardJson().getInt("prestigePoints");
    }
    return 0;
  }

  /**
   * Returns the bonus of the card based on the cardId.
   *
   * @return the bonus of the card.
   */
  private Bonus getBonusFromJson() {
    if (!getCardJson().has("bonus")) {
      return new Bonus();
    }
    JSONObject bonus = getCardJson().getJSONObject("bonus");
    HashMap<Color, Integer> bonusMap = new HashMap<>();
    for (Color color : Color.values()) {
      if (bonus.has(color.toString())) {
        bonusMap.put(color, bonus.getInt(color.toString()));
      }
    }
    return new Bonus(bonusMap);
  }

  private JSONObject getCardJson() {
    return new JSONObject(CARDS_JSON)
              .getJSONArray("cards")
              .getJSONObject(cardId);
  }
}
