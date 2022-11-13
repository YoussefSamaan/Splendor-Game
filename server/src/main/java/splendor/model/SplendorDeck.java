package splendor.model;

/**
 * This interface separates the deck from a card source. It is used to
 * represent the decks on the board.
 */
public interface SplendorDeck {

  /**
   * The color of the deck.
   */
  public Color getColor();

  /**
   * The level of the deck.
   */
  public int getLevel();
}
