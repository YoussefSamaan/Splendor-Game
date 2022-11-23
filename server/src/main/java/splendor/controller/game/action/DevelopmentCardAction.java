package splendor.controller.game.action;

import java.util.ArrayList;
import java.util.List;
import splendor.model.game.SplendorGame;
import splendor.model.game.deck.SplendorDeck;
import splendor.model.game.player.SplendorPlayer;

/**
 * An action that can be performed on a development card.
 */
public class DevelopmentCardAction extends CardAction {
  private final CardType cardType = CardType.DevelopmentCard;
  private final SplendorDeck deck;

  /**
   * Creates a new card action.
   *
   * @param actionType the type of the action
   * @param cardIndex  the index of the card
   */
  protected DevelopmentCardAction(CardActionType actionType, SplendorDeck deck, int cardIndex) {
    super(actionType, cardIndex);
    this.deck = deck;
  }

  /**
   * Executes the action on a game instance.
   *
   * @param game  the game instance
   * @param player the player that performs the action
   * @pre isLegal(game) is true
   */
  @Override
  public void execute(SplendorGame game, SplendorPlayer player) {
    switch (super.getType()) {
      case BUY:
        player.buyCard(getCardIndex(), deck.getColor(), game);
        break;
      case RESERVE:
        player.reserveCard(getCardIndex(), deck.getColor(), game);
        break;
      default:
        throw new IllegalStateException("Unknown action type: " + super.getType());
    }
  }

  /**
   * Returns whether the action is legal in the given game state.
   *
   * @param game  the game instance
   * @param player the player that performs the action
   * @return whether the action is legal in the given game state
   */
  @Override
  public boolean isLegal(SplendorGame game, SplendorPlayer player) {
    return false;
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
      actions.add(new DevelopmentCardAction(CardActionType.BUY, deck, 0));
      actions.add(new DevelopmentCardAction(CardActionType.RESERVE, deck, 0));
    }
    return actions;
  }
}
