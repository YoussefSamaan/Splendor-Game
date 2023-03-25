package splendor.model.game.player;

import java.util.HashMap;
import java.util.List;
import javax.naming.InsufficientResourcesException;
import splendor.controller.action.ActionType;
import splendor.model.game.Color;
import splendor.model.game.SplendorGame;
import splendor.model.game.card.DevelopmentCardI;
import splendor.model.game.card.SplendorCard;

/**
 * Interface for a splendor player. Can buy/reserve cards and nobles, and can take tokens.
 */
public interface SplendorPlayer {
  HashMap<Color, Integer> buyCard(DevelopmentCardI card) throws InsufficientResourcesException;

  void reserveCard(DevelopmentCardI card, boolean addGoldToken);

  void takeToken(Color color, SplendorGame game);

  /**
   * Checks if player has enough resources to buy a card.
   *
   * @param card the card to buy
   * @return true if player has enough resources to buy the card
   */
  boolean canAfford(SplendorCard card);

  /**
   * Get the player's name.
   *
   * @return the player's name
   */
  String getName();

  /**
   * gets the next action that needs to be done by the player.
   *
   * @return  a list of the next actions.
   */
  ActionType nextAction();

  /**
   * resets the next action.
   */
  void resetNextActions();

  /**
   * returns a lost of the cards bought by the player.
   *
   * @return  the cards bought by the player.
   */
  List<DevelopmentCardI> getCardsBought();

  /**
   * get a hashmap of the tokens of the player.
   *
   * @return  a hashmap of tokens.
   */
  HashMap<Color, Integer> getTokens();

  List<DevelopmentCardI> getReservedCards();
}
