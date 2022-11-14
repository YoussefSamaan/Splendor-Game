package splendor.controller.lobbyservice;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.stereotype.Component;
import splendor.Config;

/**
 * Copied and adjusted from BoardGamePlatform Project.
 * Util class to resolve player tokens to names and roles. An instance of this class
 * can be injected anywhere user authentication or role authentication is required (notably
 * in access protected REST endpoints.)
 *
 * @Author Maximilian Schiedermeier, January 2021
 */
@Component
public class TokenResolver {

  private static final String TOKEN_ROLE_RESOURCE = "/oauth/role";
  private static final String TOKEN_NAME_RESOURCE = "/oauth/username";

  private static final String lobbyServiceLocation = Config.getProperty("lobbyServiceURL");

  /**
   * Resolves an OAuth2 token to the associated user.
   *
   * @param accessToken as the string encoded OAuth2 token.
   * @return the name of the associated user.
   * @throws LogicException  in case the lobby service replied with a non 200 (token not resolvable)
   * @throws UnirestException in case the lobby service is not reachable
   */
  public static String getOwnerName(String accessToken) throws LogicException, UnirestException {
    HttpResponse<String> response = Unirest
            .get(lobbyServiceLocation + TOKEN_NAME_RESOURCE)
            .header("Authorization", "Bearer " + accessToken)
            .asString();
    if (response.getStatus() != 200) {
      throw new LogicException("Unable to resolve provided token to a username!");
    }
    return response.getBody();
  }

  /**
   * Resolves an OAuth2 token to the associated role (admin / player).
   *
   * @param accessToken as the string encoded OAuth2 token.
   * @return the role of the associated user: "ROLE_ADMIN" / "ROLE_PLAYER"
   * @throws LogicException  in case the lobby service replied with a non 200 (token not resolvable)
   * @throws UnirestException in case the lobby service is not reachable
   */
  public static String getOwnerRole(String accessToken) throws LogicException, UnirestException {
    HttpResponse<String> response = Unirest
            .get(lobbyServiceLocation + TOKEN_ROLE_RESOURCE)
            .header("Authorization", "Bearer " + accessToken)
            .asString();
    if (response.getStatus() != 200) {
      throw new LogicException("Unable to resolve provided token to a role!");
    }
    return response.getBody();
  }

  /**
   * Helper method that returns true if a provided string can be resolved to an admin role.
   *
   * @param accessToken as the string encoded OAuth2 token.
   * @return true on successful resolve, false otherwise (belongs to player or a connection error.)
   */
  public static boolean isAdminToken(String accessToken) {
    try {
      String role = getOwnerRole(accessToken);
      return role.toLowerCase().contains("admin");
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * Helper method that returns true if a provided string can be resolved to a player or admin role.
   * (All admins are implicitly players, too)
   *
   * @param accessToken as the string encoded OAuth2 token.
   * @return true on successful resolve, false otherwise (belongs to admin or a connection error.)
   */
  public static boolean isPlayerToken(String accessToken) {
    try {
      String role = getOwnerRole(accessToken);
      return role.toLowerCase().contains("player") || role.toLowerCase().contains("admin");
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * Helper method that returns true if a provided string can be resolved to a specific player name.
   * Test lowercase matching of the provided nameString and resolved player name.
   *
   * @param playerName  as the name to match against
   * @param accessToken as the string encoded OAuth2 token.
   * @return true on successful resolve and match, false otherwise
   */
  public static boolean isMatchingPlayer(String playerName, String accessToken) {
    try {
      String resolvedName = getOwnerName(accessToken);
      return resolvedName.equalsIgnoreCase(playerName);
    } catch (Exception e) {
      return false;
    }
  }
}

