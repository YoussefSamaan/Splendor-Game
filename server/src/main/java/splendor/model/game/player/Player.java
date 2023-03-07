package splendor.model.game.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.naming.InsufficientResourcesException;
import splendor.controller.action.ActionType;
import splendor.model.game.Color;
import splendor.model.game.SplendorGame;
import splendor.model.game.card.DevelopmentCardI;
import splendor.model.game.card.Noble;
import splendor.model.game.card.SplendorCard;
import splendor.model.game.payment.CoatOfArms;
import splendor.model.game.payment.Cost;
import splendor.model.game.payment.Token;

/**
 * A player in the game.
 */
public class Player implements PlayerReadOnly, SplendorPlayer {
  private final String name;
  private final String color;
  private final Inventory inventory;
  private int prestigePoints = 0;

  private final List<ActionType> nextActions;

  private final Set<CoatOfArms> coatOfArms = new HashSet<>();

  /**
   * Creates a new player.
   *
   * @param name  the name of the player
   * @param color the preferred color of the player
   */
  public Player(String name, String color) {
    this.name = name;
    this.color = color;
    nextActions = new ArrayList<>();
    //FIXME: Only for demo
    this.inventory = Inventory.getDemoInventory();
  }

  /**
   * Returns the name of the player.
   *
   * @return the name
   */
  public String getName() {
    return name;
  }

  @Override
  public String getPreferredColour() {
    return color;
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof Player)) {
      return false;
    }
    Player p = (Player) o;
    return p.getName().equals(this.getName());
  }

  /**
   * Nicely formats the player.
   *
   * @return String representation of the player
   */
  @Override
  public String toString() {
    return name;
  }

  /**
   * Buy card using player's resources.
   * Updates prestige points.
   *
   * @param card the card to buy
   * @throws InsufficientResourcesException if player does not have enough resources
   */
  @Override
  public void buyCard(DevelopmentCardI card) throws InsufficientResourcesException {
    Cost cost = card.getCost();
    HashMap<Color, Integer> resources = inventory.getResources();
    List<Token> totalPaidTokens = new ArrayList<>();
    for (Color color : cost) {
      if (resources.getOrDefault(color, 0) < cost.getValue(color)) {
        throw new InsufficientResourcesException("Not enough resources to buy card");
      }
      inventory.payFor(color, cost.getValue(color));
    }
    inventory.addBoughtCard(card);
    prestigePoints += card.getPrestigePoints();
  }

  @Override
  public void reserveCard(DevelopmentCardI card, boolean addGoldToken) {
    inventory.addReservedCard(card);
    if (addGoldToken) {
      inventory.addTokens(Token.of(Color.GOLD), 1);
    }
  }

  @Override
  public void takeToken(Color color, SplendorGame game) {

  }

  @Override
  public boolean canAfford(SplendorCard card) {
    return card.getCost().isAffordable(inventory.getResources());
  }

  public void addNextAction(ActionType action) {
    nextActions.add(action);
  }


  /**
   * resets the next actions so game is not stuck in a loop.
   *
   */
  public void resetNextActions() {
    nextActions.clear();
  }

  /**
   * used to get the next action that has to be done by the player.
   *
   * @return special action if they have to do one. Otherwise, null.
   */
  public ActionType nextAction() {
    if (nextActions.size() == 0) {
      return null;
    } else {
      return nextActions.get(nextActions.size() - 1);
    }
  }

  public List<DevelopmentCardI> getCardsBought() {
    return this.inventory.getBoughtCards();
  }


  public void addNoble(Noble noble) {
    this.inventory.addNoble(noble);
    this.prestigePoints += noble.getPrestigePoints();
  }

  public void addCard(DevelopmentCardI card) {
    this.inventory.addBoughtCard(card);
    this.prestigePoints += card.getPrestigePoints();
  }

  public HashMap<Color, Integer> getTokens() {
    return inventory.getTokens();
  }

  /**
   * Returns the number of prestige points the player has.
   *
   * @return the number of prestige points
   */
  public int getPrestigePoints() {
    return prestigePoints;
  }

  /**
   * Returns all the resources of a player (tokens + discounts).
   *
   * @return a hashmap of the color and the number of resources
   */
  public HashMap<Color, Integer> getResources() {
    return inventory.getResources();
  }

  /**
   * get the number of nobles the player has.
   *
   * @return the number of nobles
   */
  public int getNoblesCount() {
    return inventory.getNoblesCount();
  }

  public void removeTokens(HashMap<Color, Integer> tokens) {
    inventory.removeTokens(tokens);
  }

  /**
   * gives tokens to the player.
   *
   * @param tokens  a hashmap of the color and the tokens to give to the player.
   */
  public void addTokens(HashMap<Color, Integer> tokens) {
    for (Color c : tokens.keySet()) {
      inventory.addTokens(Token.of(c), tokens.get(c));
    }
  }

  /**
   * get the bonuses of a player.
   *
   * @return a hashmap of the color and the number of bonuses
   */
  public HashMap<Color, Integer> getBonuses() {
    return inventory.getBonuses();
  }

  /**
   * add coat of arms to the player.
   *
   * @param coatOfArms the coat of arms to add
   */
  public void addUnlockedCoatOfArms(CoatOfArms coatOfArms) {
    boolean alreadyUnlocked = this.coatOfArms.contains(coatOfArms);
    this.coatOfArms.add(coatOfArms);
    if (!alreadyUnlocked && coatOfArms.appliesOnce()) {
      coatOfArms.apply(this);
    }
  }

  /**
   * get the coat of arms of the player.
   *
   * @return the coat of arms
   */
  public Set<CoatOfArms> getCoatOfArms() {
    return coatOfArms;
  }

  /**
   * add prestige points to the player.
   *
   * @param prestigePoints the number of prestige points to add
   */
  public void addPrestigePoints(int prestigePoints) {
    this.prestigePoints += prestigePoints;
  }
}
