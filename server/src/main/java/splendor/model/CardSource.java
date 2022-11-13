package splendor.model;

/**
 * This interface represents a source of cards, e.g. a deck or a player's hand.
 */
public interface CardSource {
  /**
   * Draws the top card from the deck.
   *
   * @return the card at the top of the deck.
   */
  public Icard drawCard();

  /**
   * The number of cards in the deck.
   *
   * @return the number of cards in the deck.
   */
  public int getCardCount();

  /**
   * Checks if the deck is empty.
   *
   * @return True or False.
   */
  public boolean isEmpty();
}
