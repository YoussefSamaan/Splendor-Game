package splendor.model.game;

/**
 * This interface represents a splendor card. It can be a noble or a normal card.
 */
public interface SplendorCard {

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
   * The bonus of the card.
   *
   * @return the bonus of the card.
   */
  public Bonus getBonus();
}
