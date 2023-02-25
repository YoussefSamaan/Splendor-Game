package splendor.controller.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import splendor.model.game.Board;
import splendor.model.game.Color;
import splendor.model.game.SplendorGame;
import splendor.model.game.payment.Token;
import splendor.model.game.player.Player;
import splendor.model.game.player.SplendorPlayer;

/**
 * take tokens action class.
 */
public class TakeTokensAction implements Action {
  private HashMap<Token, Integer> tokens;
  private final ActionType actionType;
  private final long actionId;

  /**
   * Creates a take token action.
   *
   * @param actionType to know what type of action it is
   * @param tokens  tokens that can be taken.
   */
  public TakeTokensAction(ActionType actionType, HashMap<Token, Integer> tokens) {
    this.actionType = actionType;
    this.tokens = tokens;
    this.actionId = (long) (Math.random() * Long.MAX_VALUE);
  }

  private List<HashMap<Token, Integer>> get3Tokens(HashMap<Token, Integer> tokens) {
    List<HashMap<Token, Integer>> combinations = new ArrayList<>();
    Color[] colors = Color.tokenColors();

    for (int i = 0; i < colors.length; i++) {
      Color color1 = colors[i];
      int count1 = tokens.getOrDefault(Token.of(color1), 0);
      if (count1 == 0) {
        continue; // skip colors with no tokens
      }

      for (int j = i + 1; j < colors.length; j++) {
        Color color2 = colors[j];
        int count2 = tokens.getOrDefault(Token.of(color2), 0);
        if (count2 == 0) {
          continue; // skip colors with no tokens
        }

        for (int k = j + 1; k < colors.length; k++) {
          Color color3 = colors[k];
          int count3 = tokens.getOrDefault(Token.of(color3), 0);
          if (count3 == 0) {
            continue; // skip colors with no tokens
          }

          HashMap<Token, Integer> combination = new HashMap<>();
          combination.put(Token.of(color1), 1);
          combination.put(Token.of(color2), 1);
          combination.put(Token.of(color3), 1);
          combinations.add(combination);

        }
      }
    }

    // Handle case where we only take 2 tokens
    if (combinations.size() == 0) {
      for (int i = 0; i < colors.length; i++) {
        Color color1 = colors[i];
        int count1 = tokens.getOrDefault(Token.of(color1), 0);
        if (count1 == 0) {
          continue; // skip colors with no tokens
        }

        for (int j = i + 1; j < colors.length; j++) {
          Color color2 = colors[j];
          int count2 = tokens.getOrDefault(Token.of(color2), 0);
          if (count2 == 0) {
            continue; // skip colors with no tokens
          }

          HashMap<Token, Integer> combination = new HashMap<>();
          combination.put(Token.of(color1), 1);
          combination.put(Token.of(color2), 1);
          combinations.add(combination);

        }
      }
    }

    // Handle case where we only take 1 token
    if (combinations.size() == 0) {
      for (int i = 0; i < colors.length; i++) {
        Color color1 = colors[i];
        int count1 = tokens.getOrDefault(Token.of(color1), 0);
        if (count1 == 0) {
          continue; // skip colors with no tokens
        }

        HashMap<Token, Integer> combination = new HashMap<>();
        combination.put(Token.of(color1), count1 - 1);
        combinations.add(combination);
      }
    }

    return combinations;
  }

  // need to add logic for the special power.
  private List<HashMap<Token, Integer>> get2Tokens(HashMap<Token, Integer> tokens,
                                                   int limit,
                                                   boolean specialPower) {
    List<HashMap<Token, Integer>> combinations = new ArrayList<>();
    Color[] colors = Color.tokenColors();

    for (int i = 0; i < colors.length; i++) {
      Color color1 = colors[i];
      int count1 = tokens.getOrDefault(Token.of(color1), 0);
      if (count1 <= (2 + limit)) {
        continue; // skip colors
      }

      HashMap<Token, Integer> combination = new HashMap<>();
      combination.put(Token.of(color1), 2);
      combinations.add(combination);
    }

    return combinations;
  }

  /**
   * generates a list of actions that the player can make.
   *
   * @param game The game that the player is playing in.
   * @param player  the player to generate the actions for.
   * @return  a list of possible action that the player can take.
   */
  public static List<Action> getLegalActions(SplendorGame game, SplendorPlayer player) {
    List<Action> actions = new ArrayList<>();

    return actions;
  }

  @Override
  public long getId() {
    return this.actionId;
  }

  @Override
  public void preformAction(Player player, Board board) {

  }
}
