package splendor.model.game;

import java.util.Arrays;
import javax.naming.InsufficientResourcesException;
import splendor.controller.action.Action;
import splendor.controller.action.ActionData;
import splendor.controller.lobbyservice.GameInfo;
import splendor.model.game.card.SplendorCard;
import splendor.model.game.player.Player;
import splendor.model.game.player.SplendorPlayer;

/**
 * Class responsible for storing metadata about a game.
 */
public class SplendorGame {
  private final GameInfo gameInfo;
  private final Board board;
  private final boolean isFinished;

  /**
   * Creates a new game, with a fresh board.
   *
   * @param gameInfo the game info
   */
  public SplendorGame(GameInfo gameInfo) {
    this.gameInfo = gameInfo;
    this.board = new Board(gameInfo.getPlayers());
    isFinished = false;
  }

  /**
   * Returns the game board.
   *
   * @return the game board
   */
  public Board getBoard() {
    return board;
  }

  /**
   * Returns a player.
   *
   * @return a player
   */
  public Player getPlayer(String name) {
    return Arrays.stream(gameInfo.getPlayers())
        .filter(player -> player.getName().equals(name))
        .findFirst()
        .orElse(null);
  }

  /**
   * Buy a card.
   *
   * @param player the player
   * @param card   the card
   */
  public void buyCard(SplendorPlayer player, SplendorCard card)
      throws InsufficientResourcesException {
    board.buyCard(player, card);
  }

  public void reserveCard(SplendorPlayer player, SplendorCard card) {
    board.reserveCard(player, card);
  }

  /**
   * Checks if it's a player's turn.
   *
   * @param player the player
   * @return true if it's the player's turn
   */
  public boolean isTurnPlayer(SplendorPlayer player) {
    return board.isTurnPlayer(player);
  }

  /**
   * Performs an action.
   * Checking for the turn must have been done before.
   *
   * @param action     the action
   * @param username   the username
   * @param actionData the action data
   * @post updates the turn
   */
  public void performAction(Action action, String username, ActionData actionData)
      throws InsufficientResourcesException {
    // No use of action data for now, system automatically decides tokens to use for payment
    Player player = getPlayer(username);
    action.performAction(player, board);

    // add logic to next turn
    board.nextTurn();
  }
}
