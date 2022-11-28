package splendor.model.game.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.naming.InsufficientResourcesException;
import splendor.model.game.Color;
import splendor.model.game.SplendorGame;
import splendor.model.game.card.DevelopmentCardI;
import splendor.model.game.card.SplendorCard;
import splendor.model.game.payment.Cost;
import splendor.model.game.payment.Token;

/**
 * A player in the game.
 */
public class Player implements PlayerReadOnly, SplendorPlayer {
  private final String name;
  private final String color;
  private final Inventory inventory;
  private final int prestigePoints = 0;

  /**
   * Creates a new player.
   *
   * @param name  the name of the player
   * @param color the preferred color of the player
   */
  public Player(String name, String color) {
    this.name = name;
    this.color = color;
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
}
