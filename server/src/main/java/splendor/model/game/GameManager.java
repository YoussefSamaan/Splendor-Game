package splendor.model.game;

import java.util.HashMap;
import org.springframework.stereotype.Component;
import splendor.controller.lobbyservice.GameInfo;

/**
 * Class responsible for storing all the ongoing games.
 * There should only be one instance of this class, instantiated by Spring.
 */
@Component
public class GameManager {
  private HashMap<Long, SplendorGame> games = new HashMap<>();

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
}
