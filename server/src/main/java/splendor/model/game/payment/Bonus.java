package splendor.model.game.payment;

import java.util.HashMap;
import java.util.Iterator;
import splendor.model.game.Color;

/**
 * A set of bonuses mapping colors to integers.
 */
public class Bonus implements Iterable<Color> {
  private final HashMap<Color, Integer> bonusMap;

  /**
   * Creates a new bonus.
   */
  public Bonus(HashMap<Color, Integer> bonusMap) {
    this.bonusMap = bonusMap;
  }

  /**
   * Empty Bonus constructor.
   */
  public Bonus() {
    this.bonusMap = new HashMap<>();
  }

  /**
   * Returns the bonus for the given color. If no bonus exists for the given color,
   * 0 is returned.
   *
   * @param color the color
   * @return the bonus
   */
  public int getBonus(Color color) {
    return bonusMap.getOrDefault(color, 0);
  }

  /**
   * Adds the given bonus to this bonus.
   *
   * @param bonus a Bonus to add
   */
  public void add(Bonus bonus) {
    for (Color color : bonus) {
      bonusMap.put(color, getBonus(color) + bonus.getBonus(color));
    }
  }

  /**
   * Returns a copy of this bonus.
   *
   * @return a copy of this bonus.
   */
  public Bonus copy() {
    return new Bonus(bonusMap);
  }

  @Override
  public Iterator<Color> iterator() {
    return bonusMap.keySet().iterator();
  }
}
