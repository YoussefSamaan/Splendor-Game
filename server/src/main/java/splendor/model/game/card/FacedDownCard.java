package splendor.model.game.card;

import splendor.model.game.payment.Bonus;
import splendor.model.game.payment.Cost;

public class FacedDownCard implements SplendorCard{

  private FacedDownCardTypes type;

  public FacedDownCard(FacedDownCardTypes type) {
    this.type = type;
  }

  public FacedDownCardTypes getType() {
    return this.type;
  }

  @Override
  public Cost getCost() {
    return null;
  }

  @Override
  public int getPrestigePoints() {
    return 0;
  }

  @Override
  public int getCardId() {
    return 0;
  }

  @Override
  public Bonus getBonus() {
    return null;
  }
}
