package splendor.controller.game.action;

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

  public List<Action> generateActions(SplendorGame game, SplendorPlayer player) {
    return DevelopmentCardAction.getLegalActions(game, player);
  }

}
