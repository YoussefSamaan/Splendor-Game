package splendor.controller.game;

import eu.kartoffelquadrat.asyncrestlib.BroadcastContentManager;
import java.util.HashMap;
import java.util.List;
import javax.naming.InsufficientResourcesException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import splendor.controller.action.Action;
import splendor.controller.action.ActionData;
import splendor.controller.action.ActionGenerator;
import splendor.controller.action.InvalidAction;
import splendor.controller.lobbyservice.GameInfo;
import splendor.model.game.Board;
import splendor.model.game.SaveGameManager;
import splendor.model.game.SplendorGame;
import splendor.model.game.player.SplendorPlayer;

/**
 * Class responsible for storing all the ongoing games.
 * There should only be one instance of this class, instantiated by Spring.
 */
@Component
public class GameManager {
  private final HashMap<Long, SplendorGame> games = new HashMap<>();

  private final HashMap<Long, BroadcastContentManager<Board>> boardManagers = new HashMap<>();
  private final ActionGenerator actionGenerator;
  private final SaveGameManager saveGameManager;

  public GameManager(@Autowired ActionGenerator actionGenerator,
                     @Autowired SaveGameManager saveGameManager) {
    this.actionGenerator = actionGenerator;
    this.saveGameManager = saveGameManager;
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
    boardManagers.put(gameId, new BroadcastContentManager<>(getBoard(gameId)));
  }

  /**
   * Save the current state of a game.
   *
   * @param gameId the id of the game to save
   */
  public void saveGame(long gameId) {
    if (!exists(gameId)) {
      throw new IllegalArgumentException(String.format("Game with id %d does not exist", gameId));
    }
    saveGameManager.saveGame(gameId, games.get(gameId));
  }

  /**
   * Load a game from a json file.
   *
   * @param gameId the id of the game to load
   */
  public SplendorGame loadGame(long gameId) {
    if (exists(gameId)) {
      return games.get(gameId);
    }
    SplendorGame game = saveGameManager.loadGame(gameId);
    games.put(gameId, game);
    boardManagers.put(gameId, new BroadcastContentManager<>(getBoard(gameId)));
    return game;
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
    boardManagers.remove(gameId);
  }

  /**
   * Generates the actions for a specific player.
   *
   * @param gameId     the id of the game
   * @param playerName the name of the player
   * @return the actions for the player
   */
  public List<Action> generateActions(long gameId, String playerName) {
    SplendorGame game = games.get(gameId);
    SplendorPlayer player = game.getPlayer(playerName);
    return actionGenerator.generateActions(game, gameId, player);
  }

  /**
   * Check if a player is in a game.
   *
   * @param gameId     the id of the game
   * @param playerName the name of the player
   */
  public boolean playerInGame(long gameId, String playerName) {
    return exists(gameId) && games.get(gameId).getPlayer(playerName) != null;
  }

  /**
   * Performs an action on a game.
   *
   * @param gameId     the id of the game
   * @param username   the name of the player performing the action
   * @param actionId   the id of the action
   * @param actionData the data of the action
   * @throws InvalidAction if the action is invalid
   */
  public void performAction(long gameId, String username, String actionId, ActionData actionData)
      throws InvalidAction, InsufficientResourcesException {
    Action action = actionGenerator.getGeneratedAction(gameId, Long.parseLong(actionId));
    games.get(gameId).performAction(action, username, actionData);
    actionGenerator.removeActions(gameId);
    boardManagers.get(gameId).updateBroadcastContent(getBoard(gameId));
    // must use touch because the hash of the board is not updating.
    boardManagers.get(gameId).touch();
  }

  /**
   * Returns the board manager for a specific game.
   *
   * @param gameId the id of the game
   * @return the board manager for the game
   */
  public BroadcastContentManager<Board> getBoardManager(long gameId) {
    return boardManagers.get(gameId);
  }
}
