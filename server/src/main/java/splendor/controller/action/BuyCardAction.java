package splendor.controller.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.naming.InsufficientResourcesException;
import splendor.model.game.Board;
import splendor.model.game.Color;
import splendor.model.game.SplendorGame;
import splendor.model.game.card.DevelopmentCardI;
import splendor.model.game.card.SplendorCard;
import splendor.model.game.deck.SplendorDeck;
import splendor.model.game.player.Player;
import splendor.model.game.player.SplendorPlayer;

/**
 * Buying a card action class.
 */
public class BuyCardAction extends CardAction {
  private static CardType cardType = CardType.DevelopmentCard;

  /**
   * Creates a new card action.
   *
   * @param card the card.
   */
  protected BuyCardAction(SplendorCard card) {
    super(ActionType.BUY, card);
  }


  /**
   * Generates the list of buy actions for the player.
   *
   * @param game   the current game that is being played.
   * @param player the player we are generating actions for.
   * @return all legal buy actions for the given player in the given game state.
   */
  public static List<Action> getLegalActions(SplendorGame game, SplendorPlayer player) {
    List<Action> actions = new ArrayList<>();
    for (SplendorDeck deck : game.getBoard().getDecks()) {
      for (DevelopmentCardI card : deck.getFaceUpCards()) {
        if (card != null && player.canAfford(card)) {
          actions.add(new BuyCardAction(card));
        }
      }
    }
    return actions;
  }

  /**
   * Performs the BuyCard action for a player.
   *
   * @param player the player we are performing an action for.
   * @param board the board where we are performing the action.
   */
  @Override
  public void performAction(Player player, Board board) {
    DevelopmentCardI card;
    try {
      card = (DevelopmentCardI) this.getCard();
      HashMap<Color, Integer> tokensToGiveBack = player.buyCard(card); // should always work
      board.giveBackTokens(tokensToGiveBack);
    } catch (InsufficientResourcesException e) {
      throw new RuntimeException(e);
    }
    board.removeCard(this.getCard());
    card.getSpecialActions().forEach(player::addNextAction); // add special actions to player
  }
}
