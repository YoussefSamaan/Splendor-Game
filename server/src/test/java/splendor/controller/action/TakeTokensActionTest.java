package splendor.controller.action;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import org.junit.jupiter.api.Test;
import splendor.controller.lobbyservice.GameInfo;
import splendor.model.game.Color;
import splendor.model.game.SplendorGame;
import splendor.model.game.player.Inventory;
import splendor.model.game.player.Player;
 import splendor.model.game.Color;
public class TakeTokensActionTest {
  ActionGenerator actionGenerator = new ActionGenerator();
  SplendorGame game;
  Player player1 = new Player("Wassim", "Blue");
  Player player2 = new Player("Youssef", "Red");

//  @Before
//  public void setUp() {
//    Player[] testPlayers = {player1,player2};
//    GameInfo testGameInfo = new GameInfo("testServer","SplendorGameTest",testPlayers,"testSave");
//    game = new SplendorGame(testGameInfo);
//  }

  private void clearPlayerTokens(Player player) throws NoSuchFieldException {
    Field inventory = player.getClass().getDeclaredField("inventory");
    inventory.setAccessible(true);
    try {
      inventory.set(player, new Inventory());
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void preformAction(){
    Player[] testPlayers = {player1,player2};
    GameInfo testGameInfo = new GameInfo("testServer","SplendorGameTest",testPlayers,"testSave");
    game = new SplendorGame(testGameInfo);

    try {
      clearPlayerTokens(player1);
    } catch (NoSuchFieldException e) {
      throw new RuntimeException(e);
    }

    long gameId = 1;
    List<Action> actions = actionGenerator.generateActions(game, gameId, player1);
    Action action = actions.get(0);
    action.performAction(player1, game.getBoard());

    int numOfTokens = 3;
    for (Color c : player1.getTokens().keySet()){
      numOfTokens -= player1.getTokens().get(c);
    }
    assertTrue(numOfTokens == 1 || numOfTokens == 0);
  }

  @Test
  public void performTakeOneTokenAction(){
    Player[] testPlayers = {player1,player2};
    GameInfo testGameInfo = new GameInfo("testServer","SplendorGameTest",testPlayers,"testSave");
    game = new SplendorGame(testGameInfo);

    try {
      clearPlayerTokens(player1);
    } catch (NoSuchFieldException e) {
      throw new RuntimeException(e);
    }

    HashMap<Color, Integer> token = new HashMap<Color, Integer>();
    token.put(Color.BLUE, 1);

    long gameId = 1;
    TakeOneTokenAction tester = new TakeOneTokenAction(ActionType.TAKE_ONE_TOKEN,token);
    tester.performAction(player1, game.getBoard());
    assertEquals(true, player1.getTokens().get(Color.BLUE) == 1);
  }



  @Test
  public void performTakeTwoTokenActionWithReturn(){
    long gameId = 1;
    Player[] testPlayers = {player1,player2};
    GameInfo testGameInfo = new GameInfo("testServer","SplendorGameTest",testPlayers,"testSave");
    game = new SplendorGame(testGameInfo);

    try {
      clearPlayerTokens(player1);
    } catch (NoSuchFieldException e) {
      throw new RuntimeException(e);
    }
    HashMap<Color, Integer> tenBlueToken = new HashMap<Color, Integer>();
    tenBlueToken.put(Color.BLUE, 10);
    player1.addTokens(tenBlueToken);

    HashMap<Color, Integer> twoBluetoken = new HashMap<Color, Integer>();
    twoBluetoken.put(Color.BLUE, 2);

    ReturnTokensAction return2Blue = new ReturnTokensAction(ActionType.RETURN_TOKENS, twoBluetoken);
    return2Blue.performAction(player1, game.getBoard());

    TakeTokensAction tester = new TakeTokensAction(ActionType.TAKE_TOKENS,twoBluetoken);
    tester.performAction(player1, game.getBoard());
    assertEquals(true, player1.getTokens().get(Color.BLUE) == 10);
  }

  @Test
  public void performTakeThreeTokenAction(){
    Player[] testPlayers = {player1,player2};
    GameInfo testGameInfo = new GameInfo("testServer","SplendorGameTest",testPlayers,"testSave");
    game = new SplendorGame(testGameInfo);

    try {
      clearPlayerTokens(player1);
    } catch (NoSuchFieldException e) {
      throw new RuntimeException(e);
    }

    HashMap<Color, Integer> token = new HashMap<Color, Integer>();
    token.put(Color.BLUE, 3);

    long gameId = 1;
    TakeTokensAction tester = new TakeTokensAction(ActionType.TAKE_TOKENS,token);
    tester.performAction(player1, game.getBoard());
    assertEquals(true, player1.getTokens().get(Color.BLUE) == 3);
  }

  @Test
  public void getLegalActions(){
    Player[] testPlayers = {player1,player2};
    GameInfo testGameInfo = new GameInfo("testServer","SplendorGameTest",testPlayers,"testSave");
    game = new SplendorGame(testGameInfo);

    try {
      clearPlayerTokens(player1);
    } catch (NoSuchFieldException e) {
      throw new RuntimeException(e);
    }

    HashMap<Color, Integer> token = new HashMap<Color, Integer>();
    token.put(Color.BLUE, 1);

    long gameId = 1;
    TakeOneTokenAction tester = new TakeOneTokenAction(ActionType.TAKE_ONE_TOKEN,token);
    assertEquals(true, tester.getLegalActions(game).size() > 0);
  }

  @Test
  public void getActionType(){
    HashMap<Color, Integer> token = new HashMap<Color, Integer>();
    TakeOneTokenAction tester = new TakeOneTokenAction(ActionType.TAKE_ONE_TOKEN,token);
    assertEquals(true, tester.getActionType() == ActionType.TAKE_ONE_TOKEN);
  }

  @Test
  public void getActionId(){
    HashMap<Color, Integer> token = new HashMap<Color, Integer>();
    TakeOneTokenAction tester = new TakeOneTokenAction(ActionType.TAKE_ONE_TOKEN,token);
    assertTrue(tester.getId() > 0);
  }

//  @Test // not working for some reason
//  public void generatingTake2TokensAndTake1Token(){
//    Player[] testPlayers = {player1,player2};
//    GameInfo testGameInfo = new GameInfo("testServer","SplendorGameTest",testPlayers,"testSave");
//    game = new SplendorGame(testGameInfo);
//
//    long gameId = 1;
//    List<Action> actions = actionGenerator.generateActions(game, gameId, player1);
//    actions.get(0).preformAction(player1, game.getBoard());
//    game.getBoard().nextTurn();
//    actions = actionGenerator.generateActions(game, gameId, player2);
//    actions.get(0).preformAction(player2, game.getBoard());
//    game.getBoard().nextTurn();
//    actions = actionGenerator.generateActions(game, gameId, player1);
//    actions.get(0).preformAction(player1, game.getBoard());
//    game.getBoard().nextTurn();
//    actions = actionGenerator.generateActions(game, gameId, player2);
//    actions.get(0).preformAction(player2, game.getBoard());
//    game.getBoard().nextTurn();
//    actions = actionGenerator.generateActions(game, gameId, player1);
//    actions.get(0).preformAction(player1, game.getBoard());
//    game.getBoard().nextTurn();
//    actions = actionGenerator.generateActions(game, gameId, player2);
//    actions.get(0).preformAction(player2, game.getBoard());
//    game.getBoard().nextTurn();
//    actions = actionGenerator.generateActions(game, gameId, player1);
//    actions.get(0).preformAction(player1, game.getBoard());
//    game.getBoard().nextTurn();
//    actions = actionGenerator.generateActions(game, gameId, player2);
////    System.out.println(actions.size());
//    System.out.println(game.getBoard().getTokens());
////    actions.get(0).preformAction(player2, game.getBoard());
//  }
}
