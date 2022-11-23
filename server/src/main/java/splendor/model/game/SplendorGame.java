package splendor.model.game;

import java.util.Set;
import splendor.controller.lobbyservice.GameInfo;
import splendor.model.game.player.Player;
import splendor.model.game.player.PlayerReadOnly;

/**
 * Class responsible for storing metadata about a game.
 */
public class SplendorGame {
  private final GameInfo gameInfo;
  private boolean isFinished;
  private final Board board;

  /**
   * Creates a new game, with a fresh board.
   *
   * @param gameInfo the game info
   */
  public SplendorGame(GameInfo gameInfo) {
    this.gameInfo = gameInfo;
    this.board = new Board(gameInfo.getPlayers());
    isFinished = false;
  }

  /**
   * Returns the game players.
   *
   * @return the game players
   */
  public Player[] getPlayers() {
    return gameInfo.getPlayers();
  }
}
