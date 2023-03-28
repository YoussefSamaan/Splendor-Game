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
import splendor.model.game.payment.Bonus;
import splendor.model.game.payment.CoatOfArms;
import splendor.model.game.player.Player;
import splendor.model.game.player.SplendorPlayer;

/**
 * Buying a card action class.
 */
public class BuyCardAction extends CardAction {
  private static CardType cardType = CardType.DevelopmentCard;
  private HashMap<Color, Integer> tokenPayment;
  private List<DevelopmentCardI> cardPayment;

  /**
   * Creates a new card action.
   *
   * @param card the card.
   */
  protected BuyCardAction(SplendorCard card, HashMap<Color, Integer> tokenPayment, List<DevelopmentCardI> cardPayment) {
    super(ActionType.BUY, card);
    this.tokenPayment = tokenPayment;
    this.cardPayment = cardPayment;
  }

  private static List<HashMap<Color, Integer>> getDifferentWaysToPayForCard(HashMap<Color, Integer> cost, int numberOfGoldTokens){
    List<HashMap<Color, Integer>> differentWaysToPay = new ArrayList<>();
    differentWaysToPay.add(cost);
    Color[] colors = Color.tokenColors();

    // remove 1 from each
    if (numberOfGoldTokens >= 1) {
      for (Color c : cost.keySet()) {
        HashMap<Color,Integer> temp = new HashMap<>();
        for (int i = 0; i < colors.length - 1; i++) {
          Color color1 = colors[i];
          int count = cost.getOrDefault(color1, 0);
          if (count > 0 && c == color1) {
            temp.put(c, count-1);
          }else if (c != color1){
            temp.put(color1, count);
          }
        }
        if (temp.containsKey(c)) {
          differentWaysToPay.add(temp);
        }
      }
    }
    // remove 2 from each
    // remove 1 from 2 colors
    if (numberOfGoldTokens >= 2) {
      // TODO
    }
    // remove 3 from each
    // remove 1 from 3 colors
    // remove 2 from one color and 1 from another
    if (numberOfGoldTokens >= 3) {
      // TODO
    }

    return differentWaysToPay;
  }

  private static HashMap<Color, Integer> newCardCost(HashMap<Color, Integer> playerBonuses, HashMap<Color, Integer> cardCost){
    HashMap<Color, Integer> newCost = new HashMap<>();
    for (Color c : cardCost.keySet()) {
      int value = cardCost.get(c) - playerBonuses.getOrDefault(c,0);
      if (value >= 0){
        newCost.put(c, value);
      }
    }
    return newCost;
  }

  // assumes the payment with cards is only with 2 cards
  private static List<List<DevelopmentCardI>> differentWaysToPayWithCards(List<DevelopmentCardI> playerCards, HashMap<Color, Integer> cardCost){
    List<List<DevelopmentCardI>> differentWaysToPay = new ArrayList<>();
    Color color = null;
    for (Color c : cardCost.keySet()) {
      if (cardCost.getOrDefault(c, 0) != 0) {
        color = c;
        break;
      }
    }
    List<DevelopmentCardI> cardsOfSameColor = new ArrayList<>();
    for (DevelopmentCardI c : playerCards) {
      Bonus bonus = c.getBonus();
      if (c.getBonus().getBonus(color) != 0) {
        cardsOfSameColor.add(c);
      }
    }
    // create different ways to pay for the card and send them back.
    for (int i=0; i<cardsOfSameColor.size(); i++) {
      for (int j=i; j<cardsOfSameColor.size(); j++) {
        List<DevelopmentCardI> cards = new ArrayList<>();
        cards.add(cardsOfSameColor.get(i));
        cards.add(cardsOfSameColor.get(j));
        differentWaysToPay.add(cards);
      }
    }

    return differentWaysToPay;
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
        if (card != null) {
          int cid = card.getCardId();
          // placeholder for now
          if (cid == 115 || cid == 116 || cid == 117 || cid == 119 || cid == 120 ) {
            List<List<DevelopmentCardI>> waysToPayWithCards = differentWaysToPayWithCards(player.getCardsBought(), card.getCost()
                .getCost());
            for (List<DevelopmentCardI> cards : waysToPayWithCards) {
              actions.add(new BuyCardAction(card, null, cards));
            }

          } else if (player.canAfford(card)) {
            HashMap<Color,Integer> newCost = newCardCost(player.getBonuses(), card.getCost().getCost());
            for (HashMap<Color, Integer> h : getDifferentWaysToPayForCard(newCost, newCost.getOrDefault(Color.GOLD,0) - player.getTokens().getOrDefault(Color.GOLD, 0))) {
              actions.add(new BuyCardAction(card, h, null));
            }
          }
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
    // need to remove the tokens depending on the hashmap
    // TODO
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

    // coat of arms special power
    if (player.getCoatOfArms().contains(CoatOfArms.get(1))) {
      player.addNextAction(ActionType.TAKE_ONE_TOKEN);
    }
  }
}
