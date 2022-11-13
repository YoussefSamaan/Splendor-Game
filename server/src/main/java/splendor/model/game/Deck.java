package splendor.model.game;

import java.util.ArrayList;
import java.util.List;

/**
 * A deck of splendor cards. The deck is shuffled when it is created.
 */
public class Deck implements CardSource, SplendorDeck {
  private final List<Icard> cards = new ArrayList<>();
  private Color color;
  private int level;

  public Deck(Color color) {
    this.color = color;
    this.level = 1;
  }


  /*
    * @pre: !isEmpty()
    * @return: the card at the top of the deck.
   */
  @Override
  public Icard drawCard() {
    return cards.remove(cards.size() - 1);
  }

  @Override
  public int getCardCount() {
    return cards.size();
  }

  @Override
  public boolean isEmpty() {
    return cards.isEmpty();
  }

  @Override
  public Color getColor() {
    return color;
  }

  @Override
  public int getLevel() {
    return level;
  }
}
