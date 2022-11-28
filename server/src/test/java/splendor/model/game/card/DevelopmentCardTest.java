package splendor.model.game.card;

import org.junit.jupiter.api.*;

import splendor.model.game.Color;
import splendor.model.game.payment.Bonus;
import splendor.model.game.payment.Cost;

public class DevelopmentCardTest {
	
	@Test
	void validateGetCardColor() {
		Assertions.assertEquals(DevelopmentCard.get(1).getColor(),Color.GREEN);
	}

	@Test
	void validateGetDevelopmentCardClass() {
		Assertions.assertEquals(DevelopmentCard.class, DevelopmentCard.get(1).getClass());
	}
	
	@Test
	void tryGetIllegalDevelopmentCard() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			DevelopmentCard.get(-1);
		});
		Assertions.assertTrue(true);
	}
	
	@Test
	void validateGetCost() {
		Cost testCost = DevelopmentCard.get(4).getCost();
		Assertions.assertEquals(testCost.getValue(Color.BROWN), 4);
	}
	
	@Test
	void validateGetBonus() {
		Bonus testBonus = DevelopmentCard.get(4).getBonus();
		Assertions.assertEquals(testBonus.getBonus(Color.GREEN), 1);
	}
	
	// TODO: Test non-zero prestige points when JSON file is complete
	@Test
	void validateGetPrestigePoints() {
		int testPrestigePoints = DevelopmentCard.get(1).getPrestigePoints();
		Assertions.assertEquals(testPrestigePoints, 0);
	}
	
	@Test
	void validateGetId() {
		int testId = DevelopmentCard.get(3).getCardId();
		Assertions.assertEquals(testId, 3);
	}
}
