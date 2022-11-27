package splendor.controller.game;

import com.google.gson.Gson;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import javax.naming.AuthenticationException;
import javax.naming.InsufficientResourcesException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.bcel.Const;
import org.junit.Before;
import org.junit.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.springframework.http.ResponseEntity;
import splendor.controller.helper.Authenticator;
import splendor.model.game.Board;
import splendor.model.game.action.Action;
import splendor.model.game.action.ActionData;
import splendor.model.game.action.CardActionType;
import splendor.model.game.action.DevelopmentCardAction;
import splendor.model.game.action.InvalidAction;
import splendor.model.game.card.DevelopmentCard;
import splendor.model.game.card.SplendorCard;
import splendor.model.game.player.Player;

public class SplendorControllerTest {
  private GameManager gameManager = mock(GameManager.class);
  private Authenticator authenticator = mock(Authenticator.class);
  private SplendorController splendorController = new SplendorController(gameManager, authenticator);
  private long gameId = 1;
  Player[] players = new Player[2];
  private Board board;

  private Action action;

  @Before
  public void setUp() throws AuthenticationException, InvalidAction, InsufficientResourcesException {
    players[0] = new Player("player1", "blue");
    players[1] = new Player("player2", "red");
    board = new Board(players);
    action = getAction();
    doNothing().when(authenticator).authenticate(anyString(), anyString());
    when(gameManager.exists(gameId)).thenReturn(true);
    when(gameManager.getBoard(anyLong())).thenReturn(board);
    when(gameManager.generateActions(anyLong(), anyString())).thenReturn(Collections.singletonList(action));
    when(gameManager.playerInGame(anyLong(), anyString())).thenReturn(true);
    doNothing().when(gameManager).performAction(anyLong(), anyString(), anyString(), any());
  }

  private Action getAction() {
    try {
      Constructor<DevelopmentCardAction> constructor = DevelopmentCardAction.class
          .getDeclaredConstructor(CardActionType.class, SplendorCard.class);
      constructor.setAccessible(true);
      return constructor.newInstance(CardActionType.BUY, DevelopmentCard.get(1));
    } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
             IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void testGetBoard() {
    ResponseEntity response = splendorController.getBoard(gameId);
    assertEquals(response.getStatusCodeValue(), 200);
    assertEquals(response.getBody(), new Gson().toJson(board));
  }

  @Test
  public void testGetBoardInvalidGameId() {
    when(gameManager.exists(gameId)).thenReturn(false);
    ResponseEntity response = splendorController.getBoard(gameId);
    assertEquals(response.getStatusCodeValue(), 400);
  }

  @Test
  public void testGetActions() {
    ResponseEntity response = splendorController.getActions(gameId, "player1");
    assertEquals(response.getStatusCodeValue(), 200);
    assertEquals(response.getBody(), new Gson().toJson(Collections.singletonList(action)));
  }

  @Test
  public void testGetActionsInvalidGameId() {
    when(gameManager.exists(gameId)).thenReturn(false);
    ResponseEntity response = splendorController.getActions(gameId, "player1");
    assertEquals(response.getStatusCodeValue(), 400);
  }

  @Test
  public void testGetActionsInvalidPlayer() {
    when(gameManager.playerInGame(gameId, "player5")).thenReturn(false);
    ResponseEntity response = splendorController.getActions(gameId, "player5");
    assertEquals(response.getStatusCodeValue(), 400);
  }

  @Test
  public void testPreHandleSuccess() {
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse httpResponse = mock(HttpServletResponse.class);
    Object handler = mock(Object.class);
    when(request.getParameter(anyString())).thenReturn("String");
    when(request.getRequestURI()).thenReturn("http://fake.com");
    try {
      boolean response = splendorController.preHandle(
          request, httpResponse, handler);
      assertTrue(response);
    } catch (Exception e) {
      fail();
    }
  }

  @Test
  public void testPreHandleFailure() throws Exception {
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse httpResponse = mock(HttpServletResponse.class);
    Object handler = mock(Object.class);
    when(request.getParameter(anyString())).thenReturn("String");
    when(request.getRequestURI()).thenReturn("http://fake.com");
    doThrow(new AuthenticationException("Failed")).when(authenticator).authenticate(anyString(),
        anyString());
    assertFalse(splendorController.preHandle(request, httpResponse, handler));
  }

  @Test
  public void testPerformAction() {
    ResponseEntity response = splendorController.performAction(gameId, "player1",
        String.valueOf(action.getId()));
    assertEquals(response.getStatusCodeValue(), 200);
  }

  @Test
  public void testPerformActionInvalidGameId() {
    when(gameManager.exists(gameId)).thenReturn(false);
    ResponseEntity response = splendorController.performAction(gameId, "player1",
        String.valueOf(action.getId()));
    assertEquals(response.getStatusCodeValue(), 400);
  }

  @Test
  public void testPerformActionInvalidPlayer() {
    when(gameManager.playerInGame(gameId, "player5")).thenReturn(false);
    ResponseEntity response = splendorController.performAction(gameId, "player5",
        String.valueOf(action.getId()));
    assertEquals(response.getStatusCodeValue(), 400);
  }
}