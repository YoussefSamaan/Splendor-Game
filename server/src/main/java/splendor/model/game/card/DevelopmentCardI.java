package splendor.model.game.card;

import splendor.model.game.Color;

/**
 * Interface for a development card.
 */
public interface DevelopmentCard extends SplendorCard {
  public int getLevel();

  public Color getColor();
}
