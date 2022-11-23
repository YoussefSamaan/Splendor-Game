package splendor.model.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import splendor.model.game.card.Noble;
import splendor.model.game.deck.CompositeDeck;
import splendor.model.game.deck.DevelopmentDecks;
import splendor.model.game.player.Player;

/**
 * The board of the game. Contains the players, the nobles, the decks, and the tokens
 */
public class Board {
  private final Set<Player> players;
  private final CompositeDeck decks;
  private final List<Noble> nobles = new ArrayList<>();
  private final Bank bank = new TokenBank();

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
    this.players = Set.of(players);
    if (this.players.size() != players.length) {
      throw new IllegalArgumentException("Players must be unique");
    }
    decks = new DevelopmentDecks();
  }

  /**
   * Returns the decks.
   *
   * @return the decks
   */
  public CompositeDeck getDecks() {
    return decks;
  }
}
