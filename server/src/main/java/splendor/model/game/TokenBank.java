package splendor.model.game;

import java.util.HashMap;
import splendor.model.game.payment.Token;

/**
 * The bank of tokens on the board.
 */
public class TokenBank implements Bank<Token> {

  private final HashMap<Color, Integer> tokens = new HashMap<>();

  /**
   * Creates a new token bank with the maximum number of tokens of each color.
   */
  public TokenBank() {
    for (Color color : Color.tokenColors()) {
      tokens.put(color, Token.of(color).maxAmount());
    }
  }

  @Override
  public void add(Token element) {
    assert tokens.get(element.getColor()) < element.maxAmount();
    tokens.put(element.getColor(), tokens.get(element.getColor()) + 1);
  }

  @Override
  public void remove(Token element) {
    assert contains(element);
    tokens.put(element.getColor(), tokens.get(element.getColor()) - 1);
  }

  @Override
  public boolean contains(Token element) {
    return tokens.get(element.getColor()) > 0;
  }

  @Override
  public int count(Token element) {
    return tokens.get(element.getColor());
  }
}
