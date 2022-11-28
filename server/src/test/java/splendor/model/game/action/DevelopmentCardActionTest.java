package splendor.model.game.action;

import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import splendor.controller.lobbyservice.GameInfo;
import splendor.model.game.SplendorGame;
import splendor.model.game.player.Player;


public class DevelopmentCardActionTest {

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
  void gettingActionsTest() {
    List<Action> actions = DevelopmentCardAction.getLegalActions(testSplendorGame, player1);
    Assertions.assertNotEquals(actions.size(), 0);
  }

  @Test
  void  gettingActionInfoTest() {
    List<Action> actions = DevelopmentCardAction.getLegalActions(testSplendorGame, player1);
    CardAction action1 = (CardAction) actions.get(0);
    Assertions.assertNotEquals(action1.getId(), 0);
    Assertions.assertNotNull(action1.getCard());
    Assertions.assertTrue(action1.getType() == CardActionType.RESERVE || action1.getType() == CardActionType.BUY);
  }

}
