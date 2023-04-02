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
import splendor.model.game.card.City;
import splendor.model.game.card.DevelopmentCardI;
import splendor.model.game.card.Noble;
import splendor.model.game.card.SplendorCard;
import splendor.model.game.payment.CoatOfArms;
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

  /**
   * Getter for player's preferred colour.
   *
   * @return player's colour
   */
  @Override
  public String getPreferredColour() {
    return color;
  }

  /**
   * Equals function to compare Player objects.
   *
   * @param o is a Player object
   * @return whether the players are equal or not
   */
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
   * @return hashmap of tokens to return to the bank
   * @throws InsufficientResourcesException if player does not have enough resources
   */
  @Override
  public HashMap<Color, Integer> buyCard(DevelopmentCardI card)
          throws InsufficientResourcesException {
    HashMap<Color, Integer> tokens = inventory.payFor(card.getCost());
    inventory.addBoughtCard(card);
    prestigePoints += card.getPrestigePoints();
    return tokens;
  }

  /**
   * Adds a reserved card and, optionally, a gold token to the player inventory.
   *
   * @param card the reserved card
   * @param addGoldToken whether to add a gold token or not
   */
  @Override
  public void reserveCard(DevelopmentCardI card, boolean addGoldToken) {
    inventory.addReservedCard(card);
    if (addGoldToken) {
      inventory.addTokens(Token.of(Color.GOLD), 1);
    }
  }

  /**
   * The player takes a token.
   *
   * @param color the colour of the token taken
   * @param game the game where this action takes place.
   */
  @Override
  public void takeToken(Color color, SplendorGame game) {

  }

  /**
   * Decides if a player can afford a card.
   *
   * @param card the card to buy
   * @return whether the player can afford a card or not.
   */
  @Override
  public boolean canAfford(SplendorCard card) {
    return card.getCost().isAffordable(inventory.getResources());
  }

  /**
   * Adds a future action that the player can make during their turn.
   *
   * @param action to add to the nextActions list
   */
  public void addNextAction(ActionType action) {
    nextActions.add(action);
  }

  //TODO: Add removeOneNextActions()

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

  /**
   * Getter for bought cards.
   *
   * @return cards that player has bought
   */
  public List<DevelopmentCardI> getCardsBought() {
    return this.inventory.getBoughtCards();
  }


  /**
   * Adds noble and corresponding prestige points to player inventory.
   *
   * @param noble that gets added.
   */
  public void addNoble(Noble noble) {
    this.inventory.addNoble(noble);
    this.prestigePoints += noble.getPrestigePoints();
  }

  /**
   * Adds city to player inventory.
   *
   * @param city that gets added.
   */
  public void addCity(City city) {
    this.inventory.addCity(city);
  }

  /**
   * Adds card and corresponding prestige points to player inventory.
   *
   * @param card that gets added.
   */
  public void addCard(DevelopmentCardI card) {
    this.inventory.addBoughtCard(card);
    this.prestigePoints += card.getPrestigePoints();
  }

  /**
   * Retrieves the tokens in the player's inventory.
   *
   * @return tokens in player's inventory.
   */
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

  /**
   * get the number of cities the player has.
   *
   * @return the number of cities
   */
  public int getCitiesCount() {
    return inventory.getCitiesCount();
  }

  /**
   * Removes tokens from player inventory.
   *
   * @param tokens to remove
   */
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
    int i = 0;
    for (Color c : inventory.getTokens().keySet()) {
      i += inventory.getTokens().get(c);
    }
    if (i > 10) {
      this.nextActions.add(ActionType.RETURN_TOKENS);
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

  public List<DevelopmentCardI> getReservedCards() {
    return this.inventory.getReservedCards();
  }

  public void removeReservedCard(DevelopmentCardI card) {
    this.inventory.removeReservedCard(card);
  }

  public void removeCardBought(DevelopmentCardI card) {
    this.inventory.removeCardBought(card);
  }
}
