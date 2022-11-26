package splendor.controller.game.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.springframework.stereotype.Component;
import splendor.model.game.SplendorGame;
import splendor.model.game.player.SplendorPlayer;

/**
 * Class responsible for generating all possible actions for a given game state.
 */
@Component
public class ActionGenerator {
  private HashMap<Long, List<Action>> gameActions = new HashMap<>();

  /**
   * Returns all possible actions for a given game state.
   *
   * @param game the game
   * @param player the player
   * @return all possible actions for a given game state
   */
  public List<Action> generateActions(SplendorGame game, long gameId,  SplendorPlayer player) {
    List<Action> actions = new ArrayList<>();
    if (!game.isTurnPlayer(player)) {
      return actions;
    }
    // TODO: ADD more actions
    actions.addAll(DevelopmentCardAction.getLegalActions(game, player));
    gameActions.put(gameId, actions); // save the actions for later validation
    return actions;
  }

  /**
   * Returns a previously generated action.
   *
   * @param gameId the id of the game
   * @param actionId the id of the action
   * @return the action
   */
  public Action getGeneratedAction(long gameId, long actionId) {
    return gameActions.get(gameId).stream()
        .filter(action -> action.getId() == actionId)
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Action not found"));
  }
}
