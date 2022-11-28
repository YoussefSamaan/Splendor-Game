package splendor.controller.lobbyservice;

import java.util.Arrays;
import splendor.model.game.player.Player;

/**
 * Class responsible for storing all information about a game received from LS.
 */
public class GameInfo {

  private final String gameServer;
  private final String creator;
  private final Player[] players;
  private final String savegame;

  /**
   * Instantiates a new Game info.
   *
   * @param gameServer the game server name, just for verification
   * @param creator    the creator of the session
   * @param players    the players
   * @param savegame   whether to save the game
   */
  public GameInfo(String gameServer, String creator, Player[] players, String savegame) {
    this.gameServer = gameServer;
    this.creator = creator;
    this.players = players;
    this.savegame = savegame;
  }

  public Player[] getPlayers() {
    return players;
  }

  /**
   * Nicely formats the game info.
   *
   * @return String representation of the game info
   */
  @Override
  public String toString() {
    return "GameInfo{"
            + "gameServer='" + gameServer + '\''
            + ", creator=" + creator
            + ", players=" + Arrays.toString(players)
            + ", savegame='" + savegame + '\''
            + '}';
  }
}
