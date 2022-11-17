package splendor.controller.helper;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import splendor.controller.lobbyservice.LogicException;
import splendor.controller.lobbyservice.Role;

/**
 * Inspired from BoardGamePlatform TokenResolver class. This class is used to facilitate the
 * validation of authentication tokens.
 */
@Component
public class TokenHelper {

  private static final String ROLE_RESOURCE = "/oauth/role";
  private static final String NAME_RESOURCE = "/oauth/username";

  @Value("${lobbyservice.location}")
  private static String lobbyServiceLocation;

  /**
   * Gets the name of the player associated with the provided token.
   *
   * @param token OAuth2 access token
   * @return the name of the user associated with the provided token
   * @throws LogicException   in case the lobby service throws an exception
   * @throws UnirestException in case the request to the lobby service failed
   */
  public static String name(String token) throws LogicException, UnirestException {
    HttpResponse<String> response = Unirest
            .get(lobbyServiceLocation + NAME_RESOURCE)
            .header("Authorization", "Bearer " + token)
            .asString();
    if (response.getStatus() != 200) {
      throw new LogicException("Could not resolve token to name.");
    }
    return response.getBody();
  }

  /**
   * Gets the role of the player associated with the provided token.
   *
   * @param token OAuth2 access token
   * @return the role of the user associated with the provided token
   * @throws LogicException   in case the lobby service throws an exception
   * @throws UnirestException in case the request to the lobby service failed
   */
  public static String role(String token) throws LogicException, UnirestException {
    HttpResponse<String> response = Unirest
            .get(lobbyServiceLocation + ROLE_RESOURCE)
            .header("Authorization", "Bearer " + token)
            .asString();
    if (response.getStatus() != 200) {
      throw new LogicException("Could not resolve token to role.");
    }
    return response.getBody();
  }

  /**
   * Checks whether the provided token has the desired role.
   *
   * @param role the role of the user
   * @param desiredRole the role to check for
   * @return true if the token has the desired role, false otherwise
   */
  private static boolean hasRole(String role, Role desiredRole) {
    return role.toLowerCase().contains(desiredRole.toString().toLowerCase());
  }

  public static boolean validate(String token, String username) {
    try {
      return name(token).equalsIgnoreCase(username);
    } catch (LogicException | UnirestException e) {
      return false;
    }
  }

  public static boolean validate(String token, String username, Role role) {
    try {
      return validate(token, username) && hasRole(role(token), role);
    } catch (LogicException | UnirestException e) {
      return false;
    }
  }

  /**
   * Checks whether the provided token belongs to a player.
   *
   * @param token OAuth2 access token
   * @return true if the token belongs to a player, false otherwise
   */
  public static boolean isPlayer(String token) {
    try {
      String role = role(token);
      return hasRole(role, Role.PLAYER) || hasRole(role, Role.ADMIN);
    } catch (LogicException | UnirestException e) {
      return false;
    }
  }
}

