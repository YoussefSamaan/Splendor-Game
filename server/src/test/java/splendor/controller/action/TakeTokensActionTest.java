package splendor.controller.action;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;
import java.util.List;
import org.junit.jupiter.api.Test;
import splendor.controller.lobbyservice.GameInfo;
import splendor.model.game.Color;
import splendor.model.game.SplendorGame;
import splendor.model.game.player.Inventory;
import splendor.model.game.player.Player;

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
    action.preformAction(player1, game.getBoard());

    int numOfTokens = 3;
    for (Color c : player1.getTokens().keySet()){
      numOfTokens -= player1.getTokens().get(c);
    }
    assertTrue(numOfTokens == 1 || numOfTokens == 0);
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
