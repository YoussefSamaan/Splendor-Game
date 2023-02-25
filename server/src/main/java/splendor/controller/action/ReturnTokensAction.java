package splendor.controller.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import splendor.model.game.Board;
import splendor.model.game.Color;
import splendor.model.game.payment.Token;
import splendor.model.game.player.Player;


/**
 * return tokens action class.
 */
public class ReturnTokensAction implements Action {

  private HashMap<Token, Integer> tokens;
  private final ActionType actionType;
  private final long actionId;

  /**
   * Creates a remove token action.
   *
   * @param actionType to know what type of action it is
   * @param tokens  tokens that can be returned.
   */
  public ReturnTokensAction(ActionType actionType, HashMap<Token, Integer> tokens) {
    this.actionType = actionType;
    this.tokens = tokens;
    this.actionId = (long) (Math.random() * Long.MAX_VALUE);
  }

  private List<HashMap<Token, Integer>> getTokensToRemove(HashMap<Token, Integer> tokens,
                                                          int numOfTokens) {
    List<HashMap<Token, Integer>> combinations = new ArrayList<>();
    Color[] colors = Color.tokenColors();

    if (numOfTokens == 3) {
      for (int i = 0; i < colors.length; i++) {
        Color color1 = colors[i];
        int count1 = tokens.getOrDefault(Token.of(color1), 0);
        if (count1 == 0) {
          continue; // skip colors with no tokens
        }

        for (int j = i; j < colors.length; j++) {
          Color color2 = colors[j];
          int count2 = tokens.getOrDefault(Token.of(color2), 0);
          if (count2 == 0) {
            continue; // skip colors with no tokens
          }

          for (int k = j; k < colors.length; k++) {
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
    }

    if (numOfTokens == 2) {
      for (int i = 0; i < colors.length; i++) {
        Color color1 = colors[i];
        int count1 = tokens.getOrDefault(Token.of(color1), 0);
        if (count1 == 0) {
          continue; // skip colors with no tokens
        }

        for (int j = i; j < colors.length; j++) {
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

    if (numOfTokens == 1) {
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

  private int getNumOfTokens(HashMap<Token, Integer> tokens) {
    int numOfTokens = 0;
    Color[] colors = Color.tokenColors();

    for (int i = 0; i < colors.length; i++) {
      Color color1 = colors[i];
      numOfTokens += tokens.getOrDefault(Token.of(color1), 0);
    }

    return numOfTokens;
  }


  @Override
  public long getId() {
    return 0;
  }

  @Override
  public void preformAction(Player player, Board board) {

  }
}
