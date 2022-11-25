package splendor.model.game.player;

import splendor.model.game.Color;
import splendor.model.game.SplendorGame;
import splendor.model.game.card.DevelopmentCardI;

/**
 * Interface for a splendor player. Can buy/reserve cards and nobles, and can take tokens.
 */
public interface SplendorPlayer {
  void buyCard(DevelopmentCardI card);

  void reserveCard(int cardIndex, Color color, SplendorGame game);

  void takeToken(Color color, SplendorGame game);

  /**
   * Checks if player has enough resources to buy a card.
   *
   * @param card the card to buy
   * @return true if player has enough resources to buy the card
   */
  boolean canAfford(DevelopmentCardI card);
}
