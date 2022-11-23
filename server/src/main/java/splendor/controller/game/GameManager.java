package splendor.controller.game;

import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import splendor.controller.game.action.Action;
import splendor.controller.game.action.ActionGenerator;
import splendor.controller.lobbyservice.GameInfo;
import splendor.model.game.Board;
import splendor.model.game.SplendorGame;
import splendor.model.game.player.SplendorPlayer;

/**
 * Class responsible for storing all the ongoing games.
 * There should only be one instance of this class, instantiated by Spring.
 */
@Component
public class GameManager {
  private HashMap<Long, SplendorGame> games = new HashMap<>();
  private ActionGenerator actionGenerator = new ActionGenerator();

  private GameManager(@Autowired ActionGenerator actionGenerator) {
    this.actionGenerator = actionGenerator;
  }

  /**
   * Checks if a game with the given id exists.
   *
   * @param gameId the id of the game
   * @return true if the game exists, false otherwise
   */
  public boolean exists(long gameId) {
    return games.containsKey(gameId);
  }

  /**
   * Returns the board of a specific game.
   *
   * @param gameId the id of the game
   * @return the board of the game
   * @pre exists(gameId) is true
   */
  public Board getBoard(long gameId) {
    return games.get(gameId).getBoard();
  }

  /**
   * Creates a new game, and tracks it.
   *
   * @param gameInfo the info of the game to create
   * @throws IllegalArgumentException if the game already exists
   */
  public void createGame(GameInfo gameInfo, long gameId) throws IllegalArgumentException {
    if (exists(gameId)) {
      throw new IllegalArgumentException(String.format("Game with id %d already exists", gameId));
    }
    if (gameInfo == null) {
      throw new IllegalArgumentException("GameInfo cannot be null");
    }
    games.put(gameId, new SplendorGame(gameInfo));
  }

  /**
   * Called when a game is deleted.
   *
   * @param gameId the id of the game to be deleted
   */
  public void deleteGame(long gameId) {
    if (!exists(gameId)) {
      throw new IllegalArgumentException(String.format("Game with id %d does not exist", gameId));
    }
    games.remove(gameId);
  }

  /**
   * Generates the actions for a specific player.
   *
   * @param gameId the id of the game
   * @param playerName the name of the player
   * @return the actions for the player
   */
  public List<Action> generateActions(long gameId, String playerName) {
    SplendorGame game = games.get(gameId);
    SplendorPlayer player = game.getPlayer(playerName);
    return actionGenerator.generateActions(games.get(gameId), player);
  }
}
