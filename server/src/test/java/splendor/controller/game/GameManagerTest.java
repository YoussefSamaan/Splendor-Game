package splendor.controller.game;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import javax.naming.InsufficientResourcesException;
import org.junit.Before;
import org.junit.Ignore;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import splendor.controller.lobbyservice.GameInfo;
import splendor.model.game.SplendorGame;
import splendor.model.game.TokenBank;
import splendor.model.game.action.Action;
import splendor.model.game.action.ActionData;
import splendor.model.game.action.ActionGenerator;
import splendor.model.game.action.CardActionType;
import splendor.model.game.action.DevelopmentCardAction;
import splendor.model.game.action.InvalidAction;
import splendor.model.game.card.DevelopmentCard;
import splendor.model.game.card.SplendorCard;
import splendor.model.game.player.Player;

public class GameManagerTest {
  ActionGenerator actionGenerator = mock(ActionGenerator.class);

  ActionData actionData = mock(ActionData.class);
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
    return ((HashMap<Long, SplendorGame>) games.get(gameManager)).get(gameId);
  }

  @Before
  public void setUp() throws InvalidAction, InvocationTargetException, InstantiationException, IllegalAccessException {
    // use reflection to make DevelopmentCardAction constructor public
    Constructor<DevelopmentCardAction> constructor;
    try {
      constructor = DevelopmentCardAction.class
          .getDeclaredConstructor(CardActionType.class, SplendorCard.class);
      constructor.setAccessible(true);
    } catch (NoSuchMethodException e) {
      throw new RuntimeException(e);
    }
    Action action = constructor.newInstance(CardActionType.RESERVE, DevelopmentCard.get(1));
    when(actionGenerator.generateActions(Mockito.any(), Mockito.anyLong(), Mockito.any()))
        .thenReturn(Collections.singletonList(action));
    when(actionGenerator.getGeneratedAction(Mockito.anyLong(), Mockito.anyLong()))
        .thenReturn(action);
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

  @Test
  public void testExistsSuccess() {
    gameManager.createGame(gameInfo, gameId);
    assertTrue(gameManager.exists(gameId));
  }

  @Test
  public void testExistsFailure() {
    assertFalse(gameManager.exists(gameId));
  }

  @Test
  public void testGetBoardSuccess() throws NoSuchFieldException, IllegalAccessException {
    gameManager.createGame(gameInfo, gameId);
    SplendorGame game = getGame(gameManager, gameId);
    assertEquals(gameManager.getBoard(gameId), game.getBoard());
  }

  @Test
  public void testGenerateActionsSuccess() throws NoSuchFieldException, IllegalAccessException {
    gameManager.createGame(gameInfo, gameId);
    SplendorGame game = getGame(gameManager, gameId);
    gameManager.generateActions(gameId, players[0].getName());
    verify(actionGenerator).generateActions(game, gameId, players[0]);
  }

  @Test
  public void testPlayerInGameTrue() throws NoSuchFieldException, IllegalAccessException {
    gameManager.createGame(gameInfo, gameId);
    SplendorGame game = getGame(gameManager, gameId);
    assertTrue(gameManager.playerInGame(gameId, players[0].getName()));
  }

  @Test
  public void testPlayerInGameFalse() throws NoSuchFieldException, IllegalAccessException {
    gameManager.createGame(gameInfo, gameId);
    SplendorGame game = getGame(gameManager, gameId);
    assertFalse(gameManager.playerInGame(gameId, "nonExistent"));
  }

  @Test
  public void testPerformActionSuccess() throws InsufficientResourcesException, InvalidAction, NoSuchFieldException, IllegalAccessException {
    SplendorGame game = mock(SplendorGame.class);
    ActionGenerator actionGenerator = mock(ActionGenerator.class);
    GameManager gameManager = new GameManager(actionGenerator);
    addGameToGameManager(game, gameManager);
    doNothing().when(game).performAction(Mockito.any(), Mockito.anyString(), Mockito.any());
    gameManager.performAction(gameId, players[0].getName(), "1", actionData);
    verify(game).performAction(Mockito.any(), Mockito.anyString(), Mockito.any());
  }

  @Test
  public void testPerformActionInvalidAction() throws NoSuchFieldException, IllegalAccessException, InvalidAction {
    SplendorGame game = mock(SplendorGame.class);
    ActionGenerator actionGenerator = mock(ActionGenerator.class);
    GameManager gameManager = new GameManager(actionGenerator);
    addGameToGameManager(game, gameManager);
    when(actionGenerator.getGeneratedAction(Mockito.anyLong(), Mockito.anyLong())).thenThrow(InvalidAction.class);
    assertThrows(InvalidAction.class, () -> {
      gameManager.performAction(gameId, players[0].getName(), "1", actionData);
    });
  }

  @Test
  public void testPerformActionRemovesActionAfter() throws InsufficientResourcesException,
      InvalidAction, NoSuchFieldException, IllegalAccessException {
    SplendorGame game = mock(SplendorGame.class);
    ActionGenerator actionGenerator = mock(ActionGenerator.class);
    GameManager gameManager = new GameManager(actionGenerator);
    addGameToGameManager(game, gameManager);
    doNothing().when(game).performAction(Mockito.any(), Mockito.anyString(), Mockito.any());
    doNothing().when(actionGenerator).removeAction(Mockito.anyLong(), Mockito.anyLong());
    gameManager.performAction(gameId, players[0].getName(), "1", actionData);
    verify(actionGenerator).removeAction(Mockito.anyLong(), Mockito.anyLong());
  }

  private void addGameToGameManager(SplendorGame game, GameManager gameManager) throws NoSuchFieldException,
      IllegalAccessException {
    Field games = gameManager.getClass().getDeclaredField("games");
    games.setAccessible(true);
    ((HashMap<Long, SplendorGame>) games.get(gameManager)).put(gameId, game);
  }
}
