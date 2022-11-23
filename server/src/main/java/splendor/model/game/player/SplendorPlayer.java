package splendor.model.game.player;

import splendor.model.game.Color;
import splendor.model.game.SplendorGame;

/**
 * Interface for a splendor player. Can buy/reserve cards and nobles, and can take tokens.
 */
public interface SplendorPlayer {
  void buyCard(int cardIndex, Color color, SplendorGame game);

  void reserveCard(int cardIndex, Color color, SplendorGame game);

  void takeToken(Color color, SplendorGame game);
}
