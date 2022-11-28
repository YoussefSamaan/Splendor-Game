package splendor.model.game.action;

import java.util.List;
import javax.naming.InsufficientResourcesException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import splendor.controller.lobbyservice.GameInfo;
import splendor.model.game.SplendorGame;
import splendor.model.game.player.Player;

public class ActionGeneratorTest {

  private Player player1 = new Player("Wassim", "Blue");
  private Player player2 = new Player("Youssef", "Red");
  private SplendorGame testSplendorGame;

  @BeforeEach
  public void setUp() throws Exception {
    Player[] testPlayers = {player1,player2};
    GameInfo testGameInfo = new GameInfo("testServer","SplendorGameTest",testPlayers,"testSave");
    testSplendorGame = new SplendorGame(testGameInfo);
  }

  @Test
  void generatingActionsTest() throws InsufficientResourcesException {
    ActionGenerator actionGenerator = new ActionGenerator();
    List<Action> actions1 = actionGenerator.generateActions(testSplendorGame, 1, player1);
    List<Action> actions2 = actionGenerator.generateActions(testSplendorGame, 1, player2);
    Assertions.assertNotEquals(0,actions1.size());
    Assertions.assertEquals(actions2.size(),0);
    testSplendorGame.performAction(actions1.get(0), player1.getName(), new ActionData());
    actions2 = actionGenerator.generateActions(testSplendorGame, 1, player2);
    Assertions.assertEquals(actions1.size(), actions2.size());
  }

  @Test
  void gettingAndRemovingActions() throws InvalidAction {
    ActionGenerator actionGenerator = new ActionGenerator();
    List<Action> actions1 = actionGenerator.generateActions(testSplendorGame, 1, player1);
    Action action = actionGenerator.getGeneratedAction(1, actions1.get(0).getId());
    Assertions.assertEquals(actions1.get(0), action);
    actionGenerator.removeActions(1);
  }
}
