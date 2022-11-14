package splendor.model.game;

/**
 * A player in the game.
 */
public class Player {
  private final String name;

  /**
   * Creates a new player.
   *
   * @param name the name of the player
   */
  public Player(String name) {
    this.name = name;
  }

  /**
   * Returns the name of the player.
   *
   * @return the name
   */
  public String getName() {
    return name;
  }
}
