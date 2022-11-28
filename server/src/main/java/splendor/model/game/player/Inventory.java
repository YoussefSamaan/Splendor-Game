package splendor.model.game.player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.IntStream;
import splendor.model.game.Color;
import splendor.model.game.TokenBank;
import splendor.model.game.card.DevelopmentCardI;
import splendor.model.game.card.Noble;
import splendor.model.game.card.SplendorCard;
import splendor.model.game.payment.Bonus;
import splendor.model.game.payment.Token;


/**
 * A splendor player's Inventory during a game. Can hold cards, nobles, and tokens.
 */
public class Inventory {
  private final TokenBank tokens;
  private final List<DevelopmentCardI> boughtCards = new ArrayList<>();
  private final List<DevelopmentCardI> reservedCards = new ArrayList<>();
  private final HashMap<Color, Integer> discounts = new HashMap<>();
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
  public void addBoughtCard(DevelopmentCardI card) {
    this.boughtCards.add(card);
    addBonus(Collections.singletonList(card), this.discounts);
  }

  /**
   * Adds a development card to the inventory as a reserved card.
   *
   * @param card the card to add
   */
  public void addReservedCard(DevelopmentCardI card) {
    this.reservedCards.add(card);
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
   * @param token the tokens to add
   * @param amount the number of tokens to add
   */
  public void addTokens(Token token, int amount) {
    for (int i = 0; i < amount; i++) {
      this.tokens.add(token);
    }
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
    addBonus(boughtCards, resources);
    addBonus(nobles, resources);
    return resources;
  }

  private void addBonus(List<? extends SplendorCard> cards, HashMap<Color, Integer> resources) {
    for (SplendorCard card : cards) {
      Bonus bonus = card.getBonus();
      for (Color color : card.getBonus()) {
        resources.put(color, resources.getOrDefault(color, 0) + bonus.getBonus(color));
      }
    }
  }

  /**
   * For the demo.
   */
  public static Inventory getDemoInventory() {
    Inventory inventory = new Inventory();
    for (Color color : Color.tokenColors()) {
      inventory.addTokens(Token.of(color), 3);
    }
    return inventory;
  }

  /**
   * To make a payment of some color.
   *
   * @param color the color to pay
   * @param amount the amount to pay
   * @pre player has enough resources
   */
  public void payFor(Color color, int amount) {
    int discount = nobles.stream().mapToInt(noble -> noble.getBonus().getBonus(color)).sum();
    discount += boughtCards.stream().mapToInt(card -> card.getBonus().getBonus(color)).sum();
    int toPay = amount - discount;
    IntStream.range(0, toPay).forEach(i -> tokens.remove(Token.of(color)));
  }
}
