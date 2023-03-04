package splendor.controller.action;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.Before;
import org.junit.Test;
import splendor.controller.lobbyservice.GameInfo;
import splendor.model.game.SplendorGame;
import splendor.model.game.player.Player;

public class BuyCardActionTest {
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

  @Test
  public void preformAction(){
    Player[] testPlayers = {player1,player2};
    GameInfo testGameInfo = new GameInfo("testServer","SplendorGameTest",testPlayers,"testSave");
    game = new SplendorGame(testGameInfo);

    long gameId = 1;
    List<Action> actions = actionGenerator.generateActions(game, gameId, player1);
    System.out.println(actions.get(50));
    actions.get(50).performAction(player1, game.getBoard()); //should be buy card
    assertEquals(1, player1.getCardsBought().size());
  }
}
