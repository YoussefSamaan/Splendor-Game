package splendor.model;

import java.util.HashMap;

/**
 * A splendor card.
 */
public class Card implements Icard {

  private final int cardId;
  private final Color color;
  private final Cost cost;
  private final int prestigePoints;
  private final Bonus bonus;

  /**
   * Creates a new card. The Card values are based on the cardId.
   *
   * @param cardId the card id.
   */
  public Card(int cardId) {
    this.cardId = cardId;
    this.color = Color.BLUE;
    this.cost = new Cost(new HashMap<Color, Integer>());
    this.prestigePoints = 0;
    this.bonus = new Bonus(new HashMap<Color, Integer>());
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
  public Color getColor() {
    return color;
  }

  @Override
  public Bonus getBonus() {
    return bonus;
  }
}
