package splendor.model.game.deck;

import splendor.model.game.Color;
import splendor.model.game.card.DevelopmentCardI;

/**
 * This interface separates the deck from a card source. It is used to
 * represent the decks on the board.
 */
public interface SplendorDeck {
  /**
   * Take a card face up card.
   * Card is replaced with a new card from the deck.
   *
   * @return the card.
   */
  DevelopmentCardI takeCard(int pos);

  /**
   * The face up cards in the deck.
   *
   * @return array of length 3 of the face up cards.
   */
  DevelopmentCardI[] getFaceUpCards();

  /**
   * The number of cards in the deck.
   *
   * @return the number of cards in the deck.
   */
  int getCardCount();

  /**
   * The color of the deck.
   */
  Color getColor();

  /**
   * The level of the deck.
   */
  int getLevel();
}
