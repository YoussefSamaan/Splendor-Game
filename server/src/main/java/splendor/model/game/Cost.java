package splendor.model.game;

import java.util.HashMap;

/**
 * Cost is a map of token colors to integers. It represents the cost of a card.
 */
public class Cost {
  private final HashMap<Color, Integer> costMap;

  /**
   * Creates a new cost.
   */
  public Cost(HashMap<Color, Integer> costMap) {
    this.costMap = costMap;
  }

  /**
   * Returns the value of the cost for the given color. If no cost exists for the given color,
   * 0 is returned.
   *
   * @param color the color
   * @return the cost
   */
  public int getValue(Color color) {
    return costMap.getOrDefault(color, 0);
  }

  /**
   * Checks if the given resources are sufficient to pay this cost.
   *
   * @param resources a Cost to add
   */
  public boolean isAffordable(HashMap<Color, Integer> resources) {
    for (Color color : costMap.keySet()) {
      if (resources.getOrDefault(color, 0) < costMap.get(color)) {
        return false;
      }
    }
    return true;
  }
}
