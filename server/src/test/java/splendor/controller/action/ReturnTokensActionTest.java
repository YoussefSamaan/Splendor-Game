package splendor.controller.action;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import splendor.controller.lobbyservice.GameInfo;
import splendor.model.game.Color;
import splendor.model.game.SplendorGame;
import splendor.model.game.player.Inventory;
import splendor.model.game.player.Player;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReturnTokensActionTest {
  ActionGenerator actionGenerator = new ActionGenerator();
  SplendorGame game;
  Player player1 = new Player("Wassim", "Blue");
  Player player2 = new Player("Youssef", "Red");

  @Before
  public void setUp() {
    Player[] testPlayers = {player1,player2};
    GameInfo testGameInfo = new GameInfo("testServer","SplendorGameTest",testPlayers,"testSave");
    game = new SplendorGame(testGameInfo);
  }

  /***
  private void clearPlayerTokens(Player player) throws NoSuchFieldException {
    Field inventory = player.getClass().getDeclaredField("inventory");
    inventory.setAccessible(true);
    try {
      inventory.set(player, new Inventory());
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
  }***/

  private void clearPlayerTokens(Player player) throws NoSuchFieldException {
    for (Color key : player.getTokens().keySet()) {
      player.getTokens().put(key, 0);
    }
  }

  /***
   * The player has 10 red tokens, so the player cannot take any tokens
   * Return 1 tokens so that the player can take tokens

   * ***/
  @Test
  public void performReturnOneTokenAction(){
    long gameId = 1;
    Player[] testPlayers = {player1,player2};
    GameInfo testGameInfo = new GameInfo("testServer","SplendorGameTest",testPlayers,"testSave");
    game = new SplendorGame(testGameInfo);

    try {
      clearPlayerTokens(player1);
    } catch (NoSuchFieldException e) {
      throw new RuntimeException(e);
    }

    HashMap<Color, Integer> tenRedTokens = new HashMap<Color, Integer>();
    tenRedTokens.put(Color.RED, 10);
    player1.addTokens(tenRedTokens);

    /***
    HashMap<Color, Integer> sixBlueToken = new HashMap<Color, Integer>();
    sixBlueToken.put(Color.BLUE, 6);
    HashMap<Color, Integer>  allTokensFromBoard = game.getBoard().getTokens();
    game.getBoard().removeTokens(allTokensFromBoard);
    game.getBoard().addTokens(sixBlueToken);***/

    List<Action> actions = actionGenerator.generateActions(game, gameId, player1);
    for (Action a : actions) {
      System.out.println(a.getActionType());
   }

    Action action = actions.get(0);
    action.performAction(player1, game.getBoard());
    assertEquals(true, player1.getTokens().get(Color.RED) == 9);
  }

  /***
   * There is six blue tokens in the board
   * The player have no tokens
   * The player should take two blue tokens
   ***/
  @org.junit.jupiter.api.Test
  public void performTakeTwoTokenAction(){
    long gameId = 1;
    Player[] testPlayers = {player1,player2};
    GameInfo testGameInfo = new GameInfo("testServer","SplendorGameTest",testPlayers,"testSave");
    game = new SplendorGame(testGameInfo);

    try {
      clearPlayerTokens(player1);
    } catch (NoSuchFieldException e) {
      throw new RuntimeException(e);
    }

    HashMap<Color, Integer> sixBlueToken = new HashMap<Color, Integer>();
    sixBlueToken.put(Color.BLUE, 6);

    HashMap<Color, Integer>  allTokensFromBoard = game.getBoard().getTokens();
    game.getBoard().removeTokens(allTokensFromBoard);
    game.getBoard().addTokens(sixBlueToken);

    List<Action> actions = actionGenerator.generateActions(game, gameId, player1);
//    for (Action a : actions) {
//      System.out.println(a.getActionType());
//    }

    Action action = actions.get(1);
    action.performAction(player1, game.getBoard());
    assertEquals(true, player1.getTokens().get(Color.BLUE) == 2);
  }

  /***
   * There is one blue token, one red token, and one green token
   * The player have no tokens
   * The player should take one blue token, one red token, and one green token
   ***/
  @org.junit.jupiter.api.Test
  public void performTakeThreeTokenAction(){
    long gameId = 1;
    Player[] testPlayers = {player1,player2};
    GameInfo testGameInfo = new GameInfo("testServer","SplendorGameTest",testPlayers,"testSave");
    game = new SplendorGame(testGameInfo);

    try {
      clearPlayerTokens(player1);
    } catch (NoSuchFieldException e) {
      throw new RuntimeException(e);
    }

    HashMap<Color, Integer> tokensWithDiffColor = new HashMap<Color, Integer>();
    tokensWithDiffColor.put(Color.BLUE, 1);
    tokensWithDiffColor.put(Color.RED, 1);
    tokensWithDiffColor.put(Color.GREEN, 1);

    HashMap<Color, Integer>  allTokensFromBoard = game.getBoard().getTokens();
    game.getBoard().removeTokens(allTokensFromBoard);
    game.getBoard().addTokens(tokensWithDiffColor);

    List<Action> actions = actionGenerator.generateActions(game, gameId, player1);
    Action action = actions.get(0);
    action.performAction(player1, game.getBoard());
    assertEquals(true, (player1.getTokens().get(Color.BLUE) == 1) && (player1.getTokens().get(Color.RED) == 1) && (player1.getTokens().get(Color.GREEN) == 1));
  }


//  @Test
//  public void GenerateActions() {
//    long gameId = 1;
//    List<Action> actions = actionGenerator.generateActions(game, gameId, player1);
//    try {
//      clearPlayerTokens(player1);
//    } catch (NoSuchFieldException e) {
//      throw new RuntimeException(e);
//    }
//    actions.get(0).performAction(player1, game.getBoard());
//    game.getBoard().nextTurn();
//    game.getBoard().nextTurn();
//    actions = actionGenerator.generateActions(game, gameId, player1);
//    actions.get(0).preformAction(player1, game.getBoard());
//    game.getBoard().nextTurn();
//    game.getBoard().nextTurn();
//    actions = actionGenerator.generateActions(game, gameId, player1);
//    actions.get(0).preformAction(player1, game.getBoard());
//    game.getBoard().nextTurn();
//    game.getBoard().nextTurn();
//    actions = actionGenerator.generateActions(game, gameId, player1);
//    actions.get(0).preformAction(player1, game.getBoard());
//    game.getBoard().nextTurn();
//    game.getBoard().nextTurn();
//    player1.addNextAction(ActionType.RETURN_TOKENS);
//    game.getBoard().nextTurn();
//    actions = actionGenerator.generateActions(game, gameId, player2);
//    game.getBoard().nextTurn();
//    actions = actionGenerator.generateActions(game, gameId, player1);
//    System.out.println(player1.nextAction());
//    for (Action a: actions){
//      System.out.println(a);
//    }
//    assertEquals(51, actions.size());
//  }

  @Test
  public void preformAction() {

  }
}
