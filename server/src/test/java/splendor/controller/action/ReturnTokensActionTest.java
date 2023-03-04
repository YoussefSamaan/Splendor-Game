package splendor.controller.action;

import java.lang.reflect.Field;
import org.junit.Before;
import org.junit.Test;
import splendor.controller.lobbyservice.GameInfo;
import splendor.model.game.SplendorGame;
import splendor.model.game.player.Inventory;
import splendor.model.game.player.Player;

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

  private void clearPlayerTokens(Player player) throws NoSuchFieldException {
    Field inventory = player.getClass().getDeclaredField("inventory");
    inventory.setAccessible(true);
    try {
      inventory.set(player, new Inventory());
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
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
////    game.getBoard().nextTurn();
////    game.getBoard().nextTurn();
////    actions = actionGenerator.generateActions(game, gameId, player1);
////    actions.get(0).preformAction(player1, game.getBoard());
////    game.getBoard().nextTurn();
////    game.getBoard().nextTurn();
////    actions = actionGenerator.generateActions(game, gameId, player1);
////    actions.get(0).preformAction(player1, game.getBoard());
////    game.getBoard().nextTurn();
////    game.getBoard().nextTurn();
////    actions = actionGenerator.generateActions(game, gameId, player1);
////    actions.get(0).preformAction(player1, game.getBoard());
////    game.getBoard().nextTurn();
////    game.getBoard().nextTurn();
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
