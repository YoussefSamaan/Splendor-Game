package splendor.controller.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import splendor.model.game.Board;
import splendor.model.game.Color;
import splendor.model.game.player.Player;
import splendor.model.game.player.SplendorPlayer;


/**
 * return tokens action class.
 */
public class ReturnTokensAction implements Action {

  private HashMap<Color, Integer> tokens;
  private final ActionType actionType;
  private final long actionId;

  /**
   * Creates a remove token action.
   *
   * @param actionType to know what type of action it is
   * @param tokens  tokens that can be returned.
   */
  public ReturnTokensAction(ActionType actionType, HashMap<Color, Integer> tokens) {
    this.actionType = actionType;
    this.tokens = tokens;
    this.actionId = (long) (Math.random() * Long.MAX_VALUE);
  }

  private static List<HashMap<Color, Integer>> getTokensToRemove(HashMap<Color, Integer> tokens,
                                                          int numOfTokens) {
    List<HashMap<Color, Integer>> combinations = new ArrayList<>();
    Color[] colors = Color.tokenColors();

    if (numOfTokens == 3) {
      for (int i = 0; i < colors.length; i++) {
        Color color1 = colors[i];
        int count1 = tokens.getOrDefault(color1, 0);
        if (count1 == 0) {
          continue; // skip colors with no tokens
        }

        for (int j = i; j < colors.length; j++) {
          Color color2 = colors[j];
          int count2 = tokens.getOrDefault(color2, 0);
          if (count2 == 0) {
            continue; // skip colors with no tokens
          }

          for (int k = j; k < colors.length; k++) {
            Color color3 = colors[k];
            int count3 = tokens.getOrDefault(color3, 0);
            if (count3 == 0) {
              continue; // skip colors with no tokens
            }

            HashMap<Color, Integer> combination = new HashMap<>();
            combination.put(color1, 1);
            combination.put(color2, 1);
            combination.put(color3, 1);
            combinations.add(combination);

          }
        }
      }
    }

    if (numOfTokens == 2) {
      for (int i = 0; i < colors.length; i++) {
        Color color1 = colors[i];
        int count1 = tokens.getOrDefault(color1, 0);
        if (count1 == 0) {
          continue; // skip colors with no tokens
        }

        for (int j = i; j < colors.length; j++) {
          Color color2 = colors[j];
          int count2 = tokens.getOrDefault(color2, 0);
          if (count2 == 0) {
            continue; // skip colors with no tokens
          }

          HashMap<Color, Integer> combination = new HashMap<>();
          combination.put(color1, 1);
          combination.put(color2, 1);
          combinations.add(combination);

        }
      }
    }

    if (numOfTokens == 1) {
      for (int i = 0; i < colors.length; i++) {
        Color color1 = colors[i];
        int count1 = tokens.getOrDefault(color1, 0);
        if (count1 == 0) {
          continue; // skip colors with no tokens
        }

        HashMap<Color, Integer> combination = new HashMap<>();
        combination.put(color1, 1);
        combinations.add(combination);
      }
    }

    return combinations;
  }

  private static int getNumOfTokens(HashMap<Color, Integer> tokens) {
    int numOfTokens = 0;
    Color[] colors = Color.tokenColors();

    for (int i = 0; i < colors.length; i++) {
      Color color1 = colors[i];
      numOfTokens += tokens.getOrDefault(color1, 0);
    }

    return numOfTokens;
  }

  /**
   * Generates the list of actions for the player.
   *
   * @param player the player we are generating actions for.
   * @return all legal actions for the given player in the given game state.
   */
  public static List<Action> getLegalActions(SplendorPlayer player) {
    List<Action> actions = new ArrayList<>();
    int numOfTokensToReturn = getNumOfTokens(player.getTokens());
    List<HashMap<Color, Integer>> possibleActions = getTokensToRemove(player.getTokens(), numOfTokensToReturn-10);
    for (HashMap<Color, Integer> pa: possibleActions) {
      actions.add(new ReturnTokensAction(ActionType.RETURN_TOKENS, pa));
    }
    return actions;
  }

  @Override
  public long getId() {
    return this.actionId;
  }

  @Override
  public void preformAction(Player player, Board board) {
    player.removeTokens(this.tokens);
    board.addTokens(this.tokens);
  }
}
