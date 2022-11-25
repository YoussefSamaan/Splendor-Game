package splendor.model.game.player;

import splendor.model.game.Color;
import splendor.model.game.SplendorGame;

/**
 * A player in the game.
 */
public class Player implements PlayerReadOnly, SplendorPlayer {
  private final String name;
  private final String color;

  /**
   * Creates a new player.
   *
   * @param name  the name of the player
   * @param color the preferred color of the player
   */
  public Player(String name, String color) {
    this.name = name;
    this.color = color;
  }

  /**
   * Returns the name of the player.
   *
   * @return the name
   */
  public String getName() {
    return name;
  }

  @Override
  public String getPreferredColour() {
    return color;
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof Player)) {
      return false;
    }
    Player p = (Player) o;
    return p.getName().equals(this.getName());
  }

  /**
   * Nicely formats the player.
   *
   * @return String representation of the player
   */
  @Override
  public String toString() {
    return name;
  }

  @Override
  public void buyCard(int cardIndex, Color color, SplendorGame game) {

  }

  @Override
  public void reserveCard(int cardIndex, Color color, SplendorGame game) {

  }

  @Override
  public void takeToken(Color color, SplendorGame game) {

  }
}
