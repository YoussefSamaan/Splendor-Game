package splendor.model.game;

import java.util.Arrays;
import splendor.controller.lobbyservice.GameInfo;
import splendor.model.game.player.Player;

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
   * Returns the game board.
   *
   * @return the game board
   */
  public Board getBoard() {
    return board;
  }

  /**
   * Returns a player.
   *
   * @return a player
   */
  public Player getPlayer(String name) {
    return Arrays.stream(gameInfo.getPlayers())
            .filter(player -> player.getName().equals(name))
            .findFirst()
            .orElse(null);
  }
}