package splendor.model.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.naming.InsufficientResourcesException;
import splendor.model.game.card.DevelopmentCardI;
import splendor.model.game.card.Noble;
import splendor.model.game.card.SplendorCard;
import splendor.model.game.deck.Deck;
import splendor.model.game.deck.NobleDeck;
import splendor.model.game.deck.SplendorDeck;
import splendor.model.game.payment.Cost;
import splendor.model.game.payment.Token;
import splendor.model.game.player.Player;
import splendor.model.game.player.SplendorPlayer;

/**
 * The board of the game. Contains the players, the nobles, the decks, and the tokens
 */
public class Board {
  private final Player[] players;
  private int currentTurn;
  private final SplendorDeck[] decks = new SplendorDeck[6];
  private final NobleDeck nobleDeck = new NobleDeck();
  private final TokenBank bank = new TokenBank(true);

  /**
   * Creates a new board.
   *
   * @param players the players. Only 2-4 players are allowed.
   */
  public Board(Player... players) {
    if (players.length < 2 || players.length > 4) {
      throw new IllegalArgumentException(
              String.format("Only 2-4 players are allowed, not %d", players.length));
    }
    this.players = players;
    // make sure there are no duplicate players
    if (Set.of(players).size() != players.length) {
      throw new IllegalArgumentException("Duplicate players are not allowed");
    }
    currentTurn = 0;
    decks[0] = new Deck(Color.GREEN);
    decks[1] = new Deck(Color.YELLOW);
    decks[2] = new Deck(Color.BLUE);
    decks[3] = new Deck(Color.RED, 1);
    decks[4] = new Deck(Color.RED, 2);
    decks[5] = new Deck(Color.RED, 3);
  }

  /**
   * Returns the decks.
   *
   * @return the decks
   */
  public SplendorDeck[] getDecks() {
    return decks;
  }

  /**
   * Buys a card from the deck.
   *
   * @param player the player buying the card
   * @param card the card to buy
   */
  public void buyCard(SplendorPlayer player, SplendorCard card)
      throws InsufficientResourcesException {
    if (card instanceof Noble) {
      throw new IllegalArgumentException("Cannot buy a noble yet");
    }
    buyDevelopmentCard(player, (DevelopmentCardI) card);
  }


  private void buyDevelopmentCard(SplendorPlayer player, DevelopmentCardI card)
      throws InsufficientResourcesException {
    if (!player.canAfford(card)) {
      throw new InsufficientResourcesException("Player cannot afford card");
    }

    takeCardFromDeck(card);
    HashMap<Color, Integer> tokensToGiveBack = player.buyCard(card);
    giveBackTokens(tokensToGiveBack);
  }

  private boolean takeCardFromDeck(DevelopmentCardI card) {
    for (SplendorDeck deck : decks) {
      int pos = deck.isFaceUp(card);
      if (pos != -1) {
        deck.takeCard(pos);
        return true;
      }
    }
    return false;
  }

  /**
   * Checks if it's a player's turn.
   *
   * @param player the player
   * @return true if it's the player's turn
   */
  public boolean isTurnPlayer(SplendorPlayer player) {
    return players[currentTurn].equals(player);
  }

  /**
   * Updates the board to the next turn.
   *
   */
  public void nextTurn() {
    currentTurn = (currentTurn + 1) % players.length;
  }

  /**
   * give back tokens to the bank.
   *
   * @param tokens the tokens to give back
   */
  private void giveBackTokens(HashMap<Color, Integer> tokens) {
    for (Color color : tokens.keySet()) {
      IntStream.range(0, tokens.get(color))
          .forEach(i -> bank.add(Token.of(color)));
    }
  }

  /**
   * Returns a list of the nobles. Removes nulls.
   *
   * @return a list of the nobles.
   */
  public List<Noble> getNobles() {
    Noble[] nobles = nobleDeck.getNobles();
    return Arrays.stream(nobles)
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
  }


  public void removeNoble(Noble noble) {
    nobleDeck.removeNoble(noble);
  }

  /**
   * removes the card from the deck.
   *
   * @param card the card that need to be removed.
   */
  public void removeCard(SplendorCard card) {
    for (SplendorDeck deck : decks) {
      int pos = deck.isFaceUp((DevelopmentCardI) card);
      if (pos != -1) {
        deck.takeCard(pos);
      }
    }
  }

  public boolean hasGoldToken() {
    return this.bank.contains(Token.of(Color.GOLD));
  }


  /**
   * adds tokens to the board.
   *
   * @param tokens  a hashmap of the color and the tokens to return to the bank.
   */
  public void addTokens(HashMap<Color, Integer> tokens) {
    for (Color c : tokens.keySet()) {
      for (int i = 0; i < tokens.get(c); i++) {
        this.bank.add(Token.of(c));
      }
    }
  }

  public HashMap<Color, Integer> getTokens() {
    return this.bank.getTokens();
  }

  /**
   * removes tokens form the board.
   *
   * @param tokens  a hashmap of the color and the tokens to remove from the bank.
   */
  public void removeTokens(HashMap<Color, Integer> tokens) {
    for (Color c : tokens.keySet()) {
      for (int i = 0; i < tokens.get(c); i++) {
        this.bank.remove(Token.of(c));
      }
    }
  }

  /**
   * Checks if the player can unlock a noble, and if so, unlocks it.
   *
   * @param player the player
   */
  public void updateNobles(Player player) {
    for (Noble noble : getNobles()) {
      if (noble.getCost().isAffordable(player.getBonuses())) {
        player.addNoble(noble);
        removeNoble(noble);
        return; // only unlock one noble per turn?
      }
    }
  }
}
