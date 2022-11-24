package splendor.model.game;

/**
 * This interface separates the deck from a card source. It is used to
 * represent the decks on the board.
 */
public interface SplendorDeck extends CardSource {

  /**
   * The color of the deck.
   */
  Color getColor();

  /**
   * The level of the deck.
   */
  int getLevel();
}
