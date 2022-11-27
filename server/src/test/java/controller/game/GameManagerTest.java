package controller.game;

import java.lang.reflect.Field;
import org.junit.Before;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import splendor.controller.game.GameManager;
import splendor.controller.lobbyservice.GameInfo;
import splendor.model.game.SplendorGame;
import splendor.model.game.TokenBank;
import splendor.model.game.action.ActionGenerator;
import splendor.model.game.player.Player;

public class GameManagerTest {
  private final ActionGenerator actionGenerator = new ActionGenerator();
  private final GameManager gameManager = new GameManager(actionGenerator);
  private final long gameId = 1;
  private final Player[] players = new Player[4];
  private final GameInfo gameInfo;

  public GameManagerTest() {
    players[0] = new Player("player1", "blue");
    players[1] = new Player("player2", "red");
    players[2] = new Player("player3", "green");
    players[3] = new Player("player4", "yellow");
    gameInfo = new GameInfo("gameServer", players[0].getName(), players,
        "savegame");
  }

  private static void fillPlayerInventory(Player player) throws NoSuchFieldException,
      IllegalAccessException {
    // use reflection to fill the inventory
    Field inventory = player.getClass().getDeclaredField("inventory");
    Field tokens = inventory.getType().getDeclaredField("tokens");
    tokens.setAccessible(true);
    tokens.set(inventory, new TokenBank(true));
  }

  private static SplendorGame getGame(GameManager gameManager, long gameId) throws
      NoSuchFieldException, IllegalAccessException {
    Field games = gameManager.getClass().getDeclaredField("games");
    games.setAccessible(true);
    return ((SplendorGame[]) games.get(gameManager))[Math.toIntExact(gameId)];
  }

  @Before
  public void setUp() {

  }

  @Test
  public void testCreateGameSuccess() {
    gameManager.createGame(gameInfo, gameId);
    assertTrue(gameManager.exists(gameId));
  }

  @Test
  public void testCreateGameFailureNullGameInfo() {
    assertThrows(IllegalArgumentException.class, () -> {
      gameManager.createGame(null, gameId);
    });
  }

  @Test
  public void testCreateGameFailureGameExists() {
    gameManager.createGame(gameInfo, gameId);
    assertThrows(IllegalArgumentException.class, () -> {
      gameManager.createGame(gameInfo, gameId);
    });
  }

  @Test
  public void testDeleteGameSuccess() {
    gameManager.createGame(gameInfo, gameId);
    gameManager.deleteGame(gameId);
    assertFalse(gameManager.exists(gameId));
  }

  @Test
  public void testDeleteGameFailureGameDoesNotExist() {
    assertThrows(IllegalArgumentException.class, () -> {
      gameManager.deleteGame(gameId);
    });
  }
}
