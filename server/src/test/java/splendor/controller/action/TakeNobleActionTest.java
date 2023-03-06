package splendor.controller.action;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import splendor.controller.action.TakeNobleAction;
import splendor.controller.lobbyservice.GameInfo;
import splendor.model.game.Board;
import splendor.model.game.SplendorGame;
import splendor.model.game.card.Noble;
import splendor.model.game.player.Inventory;
import splendor.model.game.player.Player;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import splendor.model.game.Board;
import splendor.model.game.Color;
import splendor.model.game.SplendorGame;
import splendor.model.game.card.DevelopmentCardI;
import splendor.model.game.card.Noble;
import splendor.model.game.card.SplendorCard;
import splendor.model.game.payment.Bonus;
import splendor.model.game.payment.Cost;
import splendor.model.game.player.Player;
import splendor.model.game.player.SplendorPlayer;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class TakeNobleActionTest {
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

        //TODO: actions.get(index of a noble card), then assert true when player has the Noble
        long gameId = 1;
        List<Action> actions = actionGenerator.generateActions(game, gameId, player1);
        Action action = actions.get(17); //get the action of take noble
        action.performAction(player1, game.getBoard());

    }

    @Test
    public void getLegalActionsFailsWithNoToken() {
        Player[] testPlayers = {player1,player2};
        GameInfo testGameInfo = new GameInfo("testServer","SplendorGameTest",testPlayers,"testSave");
        game = new SplendorGame(testGameInfo);

        try {
            clearPlayerTokens(player1);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }

        //TODO: actions.get(index of a noble card), then assert true when player has the Noble
        long gameId = 1;
        List<Action> actions = actionGenerator.generateActions(game, gameId, player1);
        Action action = actions.get(17); //get the action of take noble
        //Assertions.assertEquals(action.getLegalActions(game, player1, true), );
        }





    }


