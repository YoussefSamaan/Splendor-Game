package splendor.model.game;

/**
 * The colors used in the game.
 */
public enum Color {
  WHITE,
  GREEN,
  BLUE,
  RED,
  BROWN,
  GOLD,
  YELLOW;

  /**
   * The colors of tokens.
   *
   * @return the colors of tokens
   */
  public static Color[] tokenColors() {
    return new Color[] {WHITE, GREEN, BLUE, RED, BROWN, GOLD};
  }
}
