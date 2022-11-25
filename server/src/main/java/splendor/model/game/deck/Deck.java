package splendor.model.game.deck;

import static java.util.Collections.shuffle;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import splendor.model.game.Color;
import splendor.model.game.card.DevelopmentCard;
import splendor.model.game.card.DevelopmentCardI;

/**
 * A deck of splendor cards. The deck is shuffled when it is created.
 */

public class Deck implements SplendorDeck {
  private static String CARDS_JSON = "src/main/resources/cards.json";
  private transient JSONObject cardsJson;
  private final transient List<DevelopmentCardI> faceDown = new ArrayList<>();
  private final DevelopmentCardI[] faceUpCards = new DevelopmentCardI[3];

  private int numRemainingCards; // This field is only used for serialization
  private final Color color;
  private final int level;

  /**
   * Creates a new deck. The deck is filled with cards and shuffled.
   * This constructor is used for green/yellow/blue decks.
   *
   * @param color the color of the deck.
   */
  public Deck(Color color) {
    this.color = color;
    this.level = 1;
    try {
      cardsJson = new JSONObject(new JSONTokener(new FileReader(CARDS_JSON)));
    } catch (FileNotFoundException e) {
      throw new RuntimeException("Could not find cards.json");
    }
    addAllCards();
    shuffle(faceDown);
    fillFaceUpCards();
    numRemainingCards = faceDown.size();
  }

  /**
   * Constructor used for Red cards, since specifying a level is required.
   *
   * @param color the color of the deck.
   * @param level the level of the deck.
   */
  public Deck(Color color, int level) {
    if (color != Color.RED) {
      throw new IllegalArgumentException("Only red decks require a level");
    }
    if (level < 1 || level > 3) {
      throw new IllegalArgumentException("Level must be between 1 and 3");
    }
    this.color = color;
    this.level = level;
    addAllCards();
  }

  /**
   * Draws the top card from the deck.
   *
    * @pre !isEmpty()
    * @return the card at the top of the deck.
   */
  @Override
  public DevelopmentCardI takeCard(int pos) {
    DevelopmentCardI card = faceUpCards[pos];
    if (!faceDown.isEmpty()) {
      faceUpCards[pos] = faceDown.remove(0);
      numRemainingCards--;
    } else {
      faceUpCards[pos] = null;
    }
    return card;
  }

  @Override
  public int getCardCount() {
    return faceDown.size();
  }

  @Override
  public int isFaceUp(DevelopmentCardI card) {
    for (int i = 0; i < faceUpCards.length; i++) {
      // This can be done since cards are flyweights
      if (faceUpCards[i] == card) {
        return i;
      }
    }
    return -1;
  }

  @Override
  public DevelopmentCardI[] getFaceUpCards() {
    return new DevelopmentCardI[] {faceUpCards[0], faceUpCards[1], faceUpCards[2]};
  }

  @Override
  public Color getColor() {
    return color;
  }

  @Override
  public int getLevel() {
    return level;
  }

  /**
   * Adds all cards of the given color and level to the deck.
   */
  private void addAllCards() {
    int[] indices = getStartAndEndIds();
    for (int i = indices[0]; i <= indices[1]; i++) {
      // Creates a new card and adds it to the deck.
      this.faceDown.add(DevelopmentCard.get(i));
    }
  }

  /**
   * Fills the face up cards with cards from the deck.
   */
  private void fillFaceUpCards() {
    for (int i = 0; i < faceUpCards.length; i++) {
      faceUpCards[i] = faceDown.remove(0);
    }
  }

  private int[] getStartAndEndIds() {
    JSONArray json = cardsJson
            .getJSONObject("color_map")
            .getJSONArray(toString());
    return new int[]{json.getInt(0), json.getInt(1)};
  }

  /**
   * Returns a string representation of the deck.
   *
   * @return a string representation of the deck.
   */
  @Override
  public String toString() {
    if (color == Color.RED) {
      return color.toString().toLowerCase() + level;
    }
    return color.toString().toLowerCase();
  }
}
