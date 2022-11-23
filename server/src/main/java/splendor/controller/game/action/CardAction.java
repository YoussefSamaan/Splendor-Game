package splendor.controller.game.action;


/**
 * All actions that can be performed on a development card or noble by a player.
 */
public abstract class CardAction implements Action {

  private final CardActionType actionType;
  private final int cardIndex;

  /**
   * Creates a new card action.
   *
   * @param actionType the type of the action
   * @param cardIndex the index of the card
   */
  protected CardAction(CardActionType actionType, int cardIndex) {
    this.actionType = actionType;
    this.cardIndex = cardIndex;
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
  public int getCardIndex() {
    return cardIndex;
  }
}
