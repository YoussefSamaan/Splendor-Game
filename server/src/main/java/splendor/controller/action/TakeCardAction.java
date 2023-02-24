package splendor.controller.action;

import java.util.ArrayList;
import java.util.List;
import splendor.model.game.Board;
import splendor.model.game.SplendorGame;
import splendor.model.game.card.DevelopmentCardI;
import splendor.model.game.card.SplendorCard;
import splendor.model.game.deck.SplendorDeck;
import splendor.model.game.player.Player;

/**
 * Taking card action without payment class.
 */
public class TakeCardAction extends CardAction {
  private static CardType cardType = CardType.DevelopmentCard;


  /**
   * Creates a new card action.
   *
   * @param card the card
   */
  protected TakeCardAction(SplendorCard card) {
    super(CardActionType.TAKE_CARD, card);
  }

  @Override
  public void preformAction(Player player, Board board) {
    player.addCard((DevelopmentCardI) this.getCard());
    board.removeCard(this.getCard());
  }

  /**
   * Generates the list of taking card actions for the player.
   *
   * @param game   the current game that is being played.
   * @return all legal taking card actions for the given player in the given game state.
   */
  public static List<Action> getLegalActions(SplendorGame game, int level) {
    List<Action> actions = new ArrayList<>();
    SplendorDeck[] decks = game.getBoard().getDecks();
    if (level == 1) {
      SplendorDeck yellowCards = decks[1];
      SplendorDeck red2Cards = decks[4];

      for (DevelopmentCardI card : yellowCards.getFaceUpCards()) {
        if (card != null) {
          actions.add(new ReserveCardAction(card));
        }
      }

      for (DevelopmentCardI card : red2Cards.getFaceUpCards()) {
        if (card != null) {
          actions.add(new ReserveCardAction(card));
        }
      }

    } else if (level == 2) {
      SplendorDeck greenCards = decks[0];
      SplendorDeck red1Cards = decks[3];

      for (DevelopmentCardI card : greenCards.getFaceUpCards()) {
        if (card != null) {
          actions.add(new ReserveCardAction(card));
        }
      }

      for (DevelopmentCardI card : red1Cards.getFaceUpCards()) {
        if (card != null) {
          actions.add(new ReserveCardAction(card));
        }
      }

    }
    return actions;
  }
}
