package splendor.model.game.card;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import splendor.model.game.Color;
import splendor.model.game.payment.Bonus;
import splendor.model.game.payment.Cost;

/**
 * A splendor card.
 * Cards are immutable.
 */
public abstract class AbstractCard implements SplendorCard {

  private static final String CARDS_JSON = "src/main/resources/cards.json";
  private static JSONObject json;
  private final int cardId; // 1 indexed
  private final transient Cost cost;
  private final transient int prestigePoints;
  private final transient Bonus bonus;

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
    return bonus;
  }

  /**
   * Returns the cost of the card based on the cardId.
   *
   * @return the cost of the card.
   */
  protected Cost getCostFromJson() {
    JSONObject cost = getCardJson().getJSONObject("cost");
    HashMap<Color, Integer> costMap = getMapFromJson(cost);
    return new Cost(costMap);
  }

  /**
   * Returns the prestige points of the card based on the cardId.
   *
   * @return the prestige points of the card.
   */
  protected int getPrestigePointsFromJson() {
    if (getCardJson().has("prestige_points")) {
      return getCardJson().getInt("prestige_points");
    }
    return 0;
  }

  /**
   * Returns the bonus of the card based on the cardId.
   *
   * @return the bonus of the card.
   */
  protected Bonus getBonusFromJson() {
    if (!getCardJson().has("bonus")) {
      return new Bonus();
    }
    JSONObject bonus = getCardJson().getJSONObject("bonus");
    HashMap<Color, Integer> bonusMap = getMapFromJson(bonus);
    return new Bonus(bonusMap);
  }

  private HashMap<Color, Integer> getMapFromJson(JSONObject bonus) {
    HashMap<Color, Integer> bonusMap = new HashMap<>();
    for (Color color : Color.values()) {
      String colorString = color.toString().toLowerCase();
      if (bonus.has(colorString)) {
        bonusMap.put(color, bonus.getInt(colorString));
      }
    }
    return bonusMap;
  }

  /**
   * Returns the card json object.
   *
   * @return the card json object.
   */
  protected JSONObject getCardJson() {
    JSONArray cards = getJson().getJSONArray("cards");
    // TODO: replace above by below when nobles are added to json:
    // if (this instanceof Noble) {
    //   cards = getJson().getJSONArray("nobles");
    // }
    // else {
    //   cards = getJson().getJSONArray("cards");
    // }
    return cards.getJSONObject(cardId - 1); // -1 because cardId is 1 indexed
  }

  /**
   * Returns the json object of the entire file.
   *
   * @return the json object.
   */
  protected JSONObject getJson() {
    try {
      if (json == null) {
        json = new JSONObject(new JSONTokener(new FileReader(CARDS_JSON)));
      }
      return json;
    } catch (FileNotFoundException e) {
      throw new RuntimeException("Could not find cards.json");
    }
  }
}
