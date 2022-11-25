package splendor.controller.game.action;

import splendor.model.game.SplendorGame;
import splendor.model.game.player.SplendorPlayer;

/**
 * Interface for all actions that can be performed by a player.
 * All actions can be executed.
 */
public interface Action {
  void execute(SplendorGame game, SplendorPlayer player);

  boolean isLegal(SplendorGame game, SplendorPlayer player);
}
