package splendor.controller.game.action;


import splendor.model.game.card.SplendorCard;

/**
 * All actions that can be performed on a development card or noble by a player.
 */
public abstract class CardAction implements Action {

  private final CardActionType actionType;
  private final SplendorCard card;

  private final int actionId;

  /**
   * Creates a new card action.
   *
   * @param actionType the type of the action
   * @param card the card
   */
  protected CardAction(CardActionType actionType, SplendorCard card) {
    this.actionType = actionType;
    this.card = card;
    this.actionId = card.getCardId();
  }

  /**
   * Returns the type of the action.
   *
   * @return the type of the action
   */
  public CardActionType getType() {
    return actionType;
  }

  /**
   * Returns the index of the card.
   *
   * @return the index of the card
   */
  public SplendorCard getCard() {
    return card;
  }

  /**
   * Returns the id of the action.
   *
   * @return the id of the action
   */
  @Override
  public int getId() {
    return actionId;
  }
}
