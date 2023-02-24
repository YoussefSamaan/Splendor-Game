package splendor.controller.action;

import java.util.ArrayList;
import java.util.List;
import splendor.model.game.Board;
import splendor.model.game.SplendorGame;
import splendor.model.game.card.DevelopmentCardI;
import splendor.model.game.card.SplendorCard;
import splendor.model.game.deck.SplendorDeck;
import splendor.model.game.player.Player;
import splendor.model.game.player.SplendorPlayer;

/**
 * reserving card action.
 */
public class ReserveCardAction extends CardAction {
  private static CardType cardType = CardType.DevelopmentCard;


  /**
   * Creates a new card action.
   *
   * @param card the card
   */
  protected ReserveCardAction(SplendorCard card) {
    super(CardActionType.RESERVE, card);
  }

  /**
   * Generates the list of actions for the player.
   *
   * @param game   the current game that is being played.
   * @param player the player we are generating actions for.
   * @return all legal actions for the given player in the given game state.
   */
  public static List<Action> getLegalActions(SplendorGame game, SplendorPlayer player) {
    List<Action> actions = new ArrayList<>();
    for (SplendorDeck deck : game.getBoard().getDecks()) {
      for (DevelopmentCardI card : deck.getFaceUpCards()) {
        if (card != null) {
          actions.add(new ReserveCardAction(card));
        }
      }
    }
    return actions;
  }


  @Override
  public void preformAction(Player player, Board board) {
    board.removeCard(this.getCard());
    boolean hasGoldToken = board.hasGoldToken();
    player.reserveCard((DevelopmentCardI) this.getCard(), hasGoldToken);
  }
}
