package splendor.model.game.action;

import java.util.ArrayList;
import java.util.List;
import splendor.model.game.SplendorGame;
import splendor.model.game.card.DevelopmentCardI;
import splendor.model.game.card.SplendorCard;
import splendor.model.game.deck.SplendorDeck;
import splendor.model.game.player.SplendorPlayer;

/**
 * An action that can be performed on a development card.
 */
public class DevelopmentCardAction extends CardAction {
  private final CardType cardType = CardType.DevelopmentCard;

  /**
   * Creates a new card action.
   *
   * @param actionType the type of the action
   * @param card the card
   */
  private DevelopmentCardAction(CardActionType actionType, SplendorCard card) {
    super(actionType, card);
  }

  /**
   * Returns all legal actions for the given player in the given game state.
   *
   * @param game  the game instance
   * @param player the player that performs the action
   * @return all legal actions for the given player in the given game state
   */
  public static List<Action> getLegalActions(SplendorGame game, SplendorPlayer player) {
    List<Action> actions = new ArrayList<>();
    for (SplendorDeck deck : game.getBoard().getDecks()) {
      for (DevelopmentCardI card : deck.getFaceUpCards()) {
        if (card == null) {
          continue;
        }
        if (player.canAfford(card)) {
          actions.add(new DevelopmentCardAction(CardActionType.BUY, card));
        }
        actions.add(new DevelopmentCardAction(CardActionType.RESERVE, card));
      }
    }
    return actions;
  }
}
