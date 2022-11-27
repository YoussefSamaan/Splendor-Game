package splendor.model.game;

import javax.naming.InsufficientResourcesException;
import org.junit.jupiter.api.*;
import splendor.controller.lobbyservice.GameInfo;
import splendor.model.game.card.DevelopmentCard;
import splendor.model.game.card.SplendorCard;
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
			Assertions.assertTrue(player1.canAfford((SplendorCard) DevelopmentCard.get(1)));
			testSplendorGame.buyCard(player1, (SplendorCard) DevelopmentCard.get(1));
			//player1.canAfford((SplendorCard) DevelopmentCard.get(1))
		} catch (InsufficientResourcesException e) {
			Assertions.assertTrue(false);
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
}
