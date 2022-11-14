package splendor.model.game;

import java.util.ArrayList;
import java.util.List;
import splendor.model.game.card.Noble;

/**
 * The board of the game. Contains the players, the nobles, the decks, and the tokens
 */
public class Board {
  private final Player[] players;
  private final List<Deck> decks = new ArrayList<>();
  private final List<Noble> nobles = new ArrayList<>();
  private final Bank bank = new TokenBank();

  /**
   * Creates a new board.
   *
   * @param players the players. Only 2-4 players are allowed.
   */
  public Board(Player... players) {
    if (players.length < 2 || players.length > 4) {
      throw new IllegalArgumentException("Only 2-4 players are allowed.");
    }
    this.players = players;
  }
}
