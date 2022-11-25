package splendor.model.game.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import splendor.model.game.Color;
import splendor.model.game.TokenBank;
import splendor.model.game.card.DevelopmentCardI;
import splendor.model.game.card.Noble;
import splendor.model.game.payment.Bonus;
import splendor.model.game.payment.Token;


/**
 * A splendor player's Inventory during a game. Can hold cards, nobles, and tokens.
 */
public class Inventory {
  private final TokenBank tokens;
  private final List<DevelopmentCardI> cards = new ArrayList<>();
  private List<Noble> nobles = new ArrayList<>();

  /**
   * Creates a new inventory.
   */
  public Inventory() {
    tokens = new TokenBank(false);
  }

  /**
   * Adds a development card to the inventory.
   *
   * @param card the card to add
   */
  public void addCard(DevelopmentCardI card) {
    cards.add(card);
  }

  /**
   * Adds a noble to the inventory.
   *
   * @param noble the noble to add
   */
  public void addNoble(Noble noble) {
    nobles.add(noble);
  }

  /**
   * Adds tokens to the inventory.
   *
   * @param tokens the tokens to add
   */
  public void addTokens(Token... tokens) {
    this.tokens.add(tokens);
  }

  /**
   * Gets all the resources in the inventory.
   * This includes discounts earned from cards and nobles.
   *
   * @return the resources
   */
  public HashMap<Color, Integer> getResources() {
    HashMap<Color, Integer> resources = new HashMap<>();
    for (Color color : Color.tokenColors()) {
      resources.put(color, tokens.count(Token.of(color)));
    }
    for (DevelopmentCardI card : cards) {
      Bonus bonus = card.getBonus();
      for (Color color : card.getBonus()) {
        resources.put(color, resources.get(color) + bonus.getBonus(color));
      }
    }
    for (Noble noble : nobles) {
      Bonus bonus = noble.getBonus();
      for (Color color : noble.getBonus()) {
        resources.put(color, resources.get(color) + bonus.getBonus(color));
      }
    }
    return resources;
  }
}
