package splendor.controller.lobbyservice;

import javax.naming.AuthenticationException;
import splendor.controller.helper.TokenHelper;

/**
 * This class is used to authenticate all requests made to the Server.
 * Authentication is done by validating the authentication token with the Lobby Service.
 * The authentication token should correspond to the user making the request.
 */
public class Authenticator {

  /**
   * Empty constructor.
   */
  public Authenticator() {}

  /**
   * This method is used to authenticate a request.
   *
   * @param token    the authentication token
   * @param username the username of the user making the request
   * @throws AuthenticationException if the authentication fails
   */
  public static void authenticate(String token, String username) throws AuthenticationException {
    if (!TokenHelper.validate(token, username)) {
      throw new AuthenticationException("Authentication token is invalid for user " + username);
    }
    if (!TokenHelper.isPlayer(token)) {
      throw new AuthenticationException("Token does not belong to a player.");
    }
  }
}
