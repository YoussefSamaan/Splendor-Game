package splendor.controller.action;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Field;
import java.util.List;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import splendor.controller.lobbyservice.GameInfo;
import splendor.model.game.Color;
import splendor.model.game.SplendorGame;
import splendor.model.game.player.Inventory;
import splendor.model.game.player.Player;

public class ReserveCardActionTest {

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
    Action action = actions.get(17); // should be reserve card
    action.performAction(player1, game.getBoard());
    assertEquals(1, (int) player1.getTokens().get(Color.GOLD));
//    assertTrue(player1.getCardsReserved().size()  == 1); // should work when we have a method for it.
  }
}
