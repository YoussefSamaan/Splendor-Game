package splendor.controller.action;

import java.util.ArrayList;
import java.util.List;
import splendor.model.game.Board;
import splendor.model.game.card.DevelopmentCardI;
import splendor.model.game.card.SplendorCard;
import splendor.model.game.player.Player;
import splendor.model.game.player.SplendorPlayer;

public class CloneCardAction extends CardAction{
  /**
   * Creates a new card action.
   *
   * @param card       the card
   */
  protected CloneCardAction(SplendorCard card) {
    super(ActionType.CLONE_CARD, card);
  }

  public static List<Action> getLegalActions(SplendorPlayer player) {
    List<Action> actions = new ArrayList<>();
    for (DevelopmentCardI c : player.getCardsBought()) {
      actions.add(new CloneCardAction(c));
    }
    return actions;
  }

  @Override
  public void performAction(Player player, Board board) {
    player.addCard((DevelopmentCardI) this.getCard());
  }
}
