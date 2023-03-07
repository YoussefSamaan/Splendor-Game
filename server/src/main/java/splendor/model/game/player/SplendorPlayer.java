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
  void buyCard(DevelopmentCardI card) throws InsufficientResourcesException;

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

  ActionType nextAction();

  void resetNextActions();

  List<DevelopmentCardI> getCardsBought();

  HashMap<Color, Integer> getTokens();
}
