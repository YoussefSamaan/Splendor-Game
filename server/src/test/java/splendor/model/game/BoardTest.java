package splendor.model.game;

import org.junit.jupiter.api.*;
import splendor.model.game.card.DevelopmentCard;
import splendor.model.game.card.DevelopmentCardI;
import splendor.model.game.card.Noble;
import splendor.model.game.card.SplendorCard;
import splendor.model.game.deck.SplendorDeck;
import splendor.model.game.player.Player;
import splendor.model.game.player.SplendorPlayer;

import javax.naming.InsufficientResourcesException;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;



public class BoardTest {
	
	static Player player1 = new Player("Wassim", "Blue");
	static Player player2 = new Player("Youssef", "Red");
	static Player player3 = new Player("Felicia", "Green");
	static Player player4 = new Player("Jessie", "Brown");
	static Player player5 = new Player("Kevin", "White");
	static Player player6 = new Player("Rui", "Yellow");
	static Board testBoard;
	
	
	@BeforeAll
	static void setUp() throws Exception {
	}
	
	@Test
	void initBoardWithOnePlayer() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			new Board(player1);
		});
		Assertions.assertTrue(true);
	}
	
	@Test
	void initBoardWithTwoPlayers() {
		new Board(player1,player2);
		Assertions.assertTrue(true);
	}
	
	@Test
	void initBoardWithThreePlayers() {
		new Board(player1,player2,player3);
		Assertions.assertTrue(true);
	}
	
	@Test
	void initBoardWithFourPlayers() {
		new Board(player1,player2,player3,player4);
		Assertions.assertTrue(true);
	}
	
	@Test
	void initBoardWithFivePlayers() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			new Board(player1,player2,player3,player4,player5);
		});
		Assertions.assertTrue(true);
	}
	
	@Test
	void initBoardWithSixPlayers() {
		 Assertions.assertThrows(IllegalArgumentException.class, () -> {
			new Board(player1,player2,player3,player4,player5,player6);
		});
		Assertions.assertTrue(true);
	}
	
	@Test
	void initBoardWithDuplicatePlayers() {
		 Assertions.assertThrows(IllegalArgumentException.class, () -> {
			new Board(player1,player2,player2);
		});
		Assertions.assertTrue(true);
	}
	
	@Test
	void validateDecks() {
		Assertions.assertEquals(SplendorDeck[].class,testBoard.getDecks().getClass());
	}
	
	// TODO: TEST buyCard method

	@Test
	void cannotBuyNoble(){
		testBoard = new Board(player1, player2, player3, player4);
		Noble noble1 = Noble.get(1);
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			testBoard.buyCard(player1, noble1);
		});
		Assertions.assertTrue(true);
	}

	@Test
	void cannotReserveNoble(){
		testBoard = new Board(player1, player2, player3, player4);
		Noble noble1 = Noble.get(1);
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			testBoard.buyCard(player1, noble1);
		});
		Assertions.assertTrue(true);
	}

	@Test
	void validateFirstTurn() {
		testBoard = new Board(player1,player2,player3,player4);
		Assertions.assertTrue(testBoard.isTurnPlayer(player1));
	}
	
	@Test
	void validateSecondTurn() {
		testBoard = new Board(player1,player2,player3,player4);
		testBoard.nextTurn();
		Assertions.assertTrue(testBoard.isTurnPlayer(player2));
	}

	@Test
	void preserveCard(){
		testBoard = new Board(player1,player2,player3,player4);
		testBoard.nextTurn();

	}

	@Test
	void reserveDevelopmentCard(){

	}

	@Test
	void removeNoble(){

	}

	@Test
	void removeCard(){

	}

	@Test
	void hasGoldToken(){


	}

	@Test
	void addTokens(){

	}


}
