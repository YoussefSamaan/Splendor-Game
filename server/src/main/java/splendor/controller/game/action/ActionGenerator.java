package splendor.controller.game.action;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;
import splendor.model.game.SplendorGame;
import splendor.model.game.player.SplendorPlayer;

/**
 * Class responsible for generating all possible actions for a given game state.
 */
@Component
public class ActionGenerator {
  public ActionGenerator() {
  }

  /**
   * Returns all possible actions for a given game state.
   *
   * @param game the game
   * @param player the player
   * @return all possible actions for a given game state
   */
  public List<Action> generateActions(SplendorGame game, SplendorPlayer player) {
    List<Action> actions = new ArrayList<>();
    if (!game.isTurnPlayer(player)) {
      return actions;
    }
    // TODO: ADD more actions
    actions.addAll(DevelopmentCardAction.getLegalActions(game, player));
    return actions;
  }

}
