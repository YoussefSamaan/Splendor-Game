package splendor.model.game;

import java.util.List;
import javax.naming.InsufficientResourcesException;
import org.junit.jupiter.api.*;
import splendor.controller.lobbyservice.GameInfo;
import splendor.controller.action.Action;
import splendor.controller.action.ActionData;
import splendor.controller.action.ActionGenerator;
import splendor.model.game.card.DevelopmentCard;
import splendor.model.game.player.Player;

public class SplendorGameTest {
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
	void validateReturnedBoardClass() {
		Assertions.assertEquals(testSplendorGame.getBoard().getClass(),Board.class);
	}
	
	@Test
	void findValidPlayer() {
		Assertions.assertEquals(testSplendorGame.getPlayer("Wassim"),player1);
	}
	
	@Test
	void findInvalidPlayer() {
		Assertions.assertNull(testSplendorGame.getPlayer("Rui"));
	}
	
	// TODO: Test buyCard: Incomplete test because it appears that it always passes
	@Test
	void buyCardTest() {
		try {
			Assertions.assertTrue(player1.canAfford(DevelopmentCard.get(1)));
			testSplendorGame.buyCard(player1, DevelopmentCard.get(1));
			//player1.canAfford((SplendorCard) DevelopmentCard.get(1))
		} catch (InsufficientResourcesException e) {
			Assertions.fail();
		}
		Assertions.assertTrue(true);
	}
	
	// TODO: Test reserveCard
	
	// TODO: Test buyCard
	
	@Test
	void validateFirstTurn() {
		Assertions.assertTrue(testSplendorGame.isTurnPlayer(player1));
	}
	
	// TODO: Test performAction
	@Test
	public void testPerformAction() {
		ActionGenerator actionGenerator = new ActionGenerator();
		List<Action> actions = actionGenerator.generateActions(testSplendorGame, 1, player1);
		Action action = actions.get(0);
		ActionData actionData = new ActionData();
		try {
			testSplendorGame.performAction(action, player1.getName(), actionData);
		}
		catch (Exception e) {
			Assertions.fail();
		}
		Assertions.assertTrue(true);
	}

	@Test
	public void testPerformingActionUpdatesTurn() {
		testPerformAction();
		Assertions.assertFalse(testSplendorGame.isTurnPlayer(player1));
	}
}
