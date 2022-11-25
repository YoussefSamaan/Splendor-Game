package splendor.model.game;

/**
 * This interface represents a splendor card.
 */
public interface Icard {

  /**
   * The cost of the card.
   *
   * @return the cost of the card.
   */
  public Cost getCost();

  /**
   * The prestige points of the card.
   *
   * @return the prestige points of the card.
   */
  public int getPrestigePoints();

  /**
   * The card id.
   *
   * @return the card id.
   */
  public int getCardId();

  /**
   * The color of the card.
   *
   * @return the color of the card.
   */
  public Color getColor();

  /**
   * The bonus of the card.
   *
   * @return the bonus of the card.
   */
  public Bonus getBonus();
}
