package splendor.model.game.card;

import java.util.List;
import splendor.controller.action.ActionType;
import splendor.model.game.Color;

/**
 * Interface for a development card.
 */
public interface DevelopmentCardI extends SplendorCard {
  Color getColor();

  List<ActionType> getSpecialActions();
}
