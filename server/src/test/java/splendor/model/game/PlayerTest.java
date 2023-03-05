package splendor.model.game;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import splendor.model.game.card.DevelopmentCard;
import splendor.model.game.card.Noble;
import splendor.model.game.player.Player;

public class PlayerTest {
    static Player player1 = new Player("Wassim", "Blue");
    static Player player2 = new Player("Youssef", "Red");
    static Player player3 = new Player("Felicia", "Green");
    static Player player4 = new Player("Jessie", "Brown");
    static Player player5 = new Player("Kevin", "White");
    static Player player6 = new Player("Rui", "Yellow");
    static Board testBoard;

    @Test
    void getPerferredColorTest(){
        Assertions.assertEquals("Blue",player1.getPreferredColour());
    }

    @Test
    void addCardTest(){
        DevelopmentCard card1 = DevelopmentCard.get(1);;
        player2.addCard(card1);
        Assertions.assertTrue(player2.getCardsBought().contains(card1));
    }

    //TODO : test for addNoble
    @Test
    void removeTokenTest(){
        player3.removeTokens(player3.getTokens());
        int num_red_token_after_removal = player3.getTokens().get(Color.RED);
        Assertions.assertTrue(  num_red_token_after_removal == 0);
    }

}
