package splendor.controller.lobbyservice;

import org.junit.Before;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import splendor.controller.game.GameManager;
import splendor.controller.action.ActionGenerator;
import splendor.model.game.SaveGameManager;
import splendor.model.game.player.Player;

@ExtendWith(MockitoExtension.class)
public class GameHandlerTest {
  private final ActionGenerator actionGenerator = new ActionGenerator();
  private final SaveGameManager saveGameManager = new SaveGameManager("savegame");
  private final GameManager gameManager;
  private final GameHandler gameHandler;
  @Mock
  private final Registrator registrator;
  private final long gameId = 1;
  private final GameInfo gameInfo;

  public GameHandlerTest(@Mock Registrator registrator) {
    Player[] players = new Player[4];
    players[0] = new Player("player1", "blue");
    players[1] = new Player("player2", "red");
    players[2] = new Player("player3", "green");
    players[3] = new Player("player4", "yellow");
    gameInfo = new GameInfo("gameServer", players[0].getName(), players,
        null);
    this.gameManager = new GameManager(actionGenerator, saveGameManager, registrator);
    this.gameHandler = new GameHandler(gameManager);
    this.registrator = registrator;
  }

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    doNothing().when(registrator).saveGame(any());
  }

  @Test
  public void testStartGameSuccess() {
    gameHandler.startGame(gameId, gameInfo);
    assertTrue(gameManager.exists(gameId));
  }

  @Test
  public void testStartGameFailureNullGameInfo() {
    ResponseEntity response = gameHandler.startGame(gameId, null);
    assertEquals(400, response.getStatusCodeValue());
  }

  @Test
  public void testStartGameWhenGameAlreadyExists() {
    gameHandler.startGame(gameId, gameInfo);
    ResponseEntity resposnse = gameHandler.startGame(gameId, gameInfo);
    assertEquals(400, resposnse.getStatusCodeValue());
  }

  @Test
  public void testDeleteGameSuccess() {
    gameHandler.startGame(gameId, gameInfo);
    gameHandler.deleteGame(gameId);
    assertFalse(gameManager.exists(gameId));
  }

  @Test
  public void testDeleteGameWhenGameDoesNotExist() {
    ResponseEntity response = gameHandler.deleteGame(gameId);
    assertEquals(400, response.getStatusCodeValue());
  }
}
