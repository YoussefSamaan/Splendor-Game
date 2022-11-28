package splendor.model.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.naming.InsufficientResourcesException;
import splendor.model.game.card.DevelopmentCardI;
import splendor.model.game.card.Noble;
import splendor.model.game.card.SplendorCard;
import splendor.model.game.deck.Deck;
import splendor.model.game.deck.SplendorDeck;
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
  private final List<Noble> nobles = new ArrayList<>();
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

  /**
   * Reserve a card from the deck.
   *
   * @param player the player reserving the card
   * @param card the card to reserve
   */
  public void reserveCard(SplendorPlayer player, SplendorCard card) {
    if (card instanceof Noble) {
      throw new IllegalArgumentException("Cannot reserve a noble yet");
    }
    reserveDevelopmentCard(player, (DevelopmentCardI) card);
  }

  private void buyDevelopmentCard(SplendorPlayer player, DevelopmentCardI card)
      throws InsufficientResourcesException {
    if (!player.canAfford(card)) {
      throw new IllegalArgumentException("Player cannot afford card");
    }

    takeCardFromDeck(card);
    player.buyCard(card);
  }

  private void reserveDevelopmentCard(SplendorPlayer player, DevelopmentCardI card) {
    takeCardFromDeck(card);
    boolean addGoldToken = false;
    if (bank.contains(Token.of(Color.GOLD))) {
      bank.remove(Token.of(Color.GOLD));
      addGoldToken = true;
    }
    player.reserveCard(card, addGoldToken);
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
}
