package splendor.controller.lobbyservice.communicationbeans;

/**
 * Just a bean to encapsulate all data transferred to the LS upon registration of Xox as a new gameserver. This class
 * can be copied and reused as is in other game-implementations. Only the constructor parameters need to be adjusted.
 *
 * @Author Maximilian Schiedermeier
 * @Date December 2020
 */
public class GameServerParameters {

  // Name of the gameServer to be registered. Will we converted to lowercase internally.
  private String name;

  // Name of the game as it will be displayed to Lobby Service users. All characters can be used here, including whitespaces.
  private String displayName;

  // Location of the server's base URL. If WebSupport is set to true, this URL must return the webclient of the
  // registered game. In any case the registered game must provide subresource, "games/{gameId}" with "PUT", for the
  // LS to launch the session on game-server side.
  private String location;

  // Upper and lower bounds for players allowed in sessions of this gameserver.
  private int maxSessionPlayers;
  private int minSessionPlayers;

  /* Indicates whether a generic BGP web clients can page forward to the server location on session launch (gameid as
   session parameter). Encodes a boolean, but must be a string, for non-existence is auto-interpreted as "false" for
   boolean values. If set, convention is that the generic JS client forwards on session launch to the provided server
   location, passing required params such as username, access_token, gameid as URL parameters.*/
  private String webSupport;

  // default CTR required for JSON deserialization.
  public GameServerParameters() {

  }

  /**
   * @param name as the unique name of this game that can be used by a lobby service instance as unique id.
   * @param displayName as identifier how to display this service in a lobby service ui.
   * @param location
   * @param minSessionPlayers
   * @param maxSessionPlayers
   */
  public GameServerParameters(String name, String displayName, String location, int minSessionPlayers, int maxSessionPlayers, String webSupport) {
    this.name = name;
    this.displayName = displayName;
    this.location = location;
    this.minSessionPlayers = minSessionPlayers;
    this.maxSessionPlayers = maxSessionPlayers;
    this.webSupport = webSupport;
  }


  public String getName() {
    return name;
  }

  public String getDisplayName() {
    return displayName;
  }

  public String getLocation() {
    return location;
  }

  public int getMaxSessionPlayers() {
    return maxSessionPlayers;
  }

  public int getMinSessionPlayers() {
    return minSessionPlayers;
  }

  public boolean isWebSupport() {
    return Boolean.parseBoolean(webSupport);
  }

  /**
   * Helper method to determine wether a registered gameserver runs in phantom (p2p) mode. This method simply checks
   * whether no location was provided.
   *
   * @return
   */
  public boolean isPhantom() {
    return location.isEmpty();
  }
}
