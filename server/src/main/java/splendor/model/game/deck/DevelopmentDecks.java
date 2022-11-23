package splendor.model.game.deck;

import java.util.Arrays;
import java.util.Iterator;
import splendor.model.game.Color;

/**
 * A composite deck is a deck that contains multiple decks.
 * It represents the decks on the board.
 */
public class DevelopmentDecks implements CompositeDeck {
  private final SplendorDeck[] decks = new SplendorDeck[3];

  /**
   * Creates a new set of development decks.
   *
   */
  public DevelopmentDecks() {
    // FIXME: Actually create the decks
    decks[0] = new Deck(Color.GREEN);
    //    decks[1] = new Deck(Color.YELLOW);
    //    decks[2] = new Deck(Color.BLUE);
  }

  @Override
  public Iterator<SplendorDeck> iterator() {
    return Arrays.stream(decks).iterator();
  }
}
