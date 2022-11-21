package splendor.controller.lobbyservice;

import javax.naming.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import splendor.controller.helper.TokenHelper;

/**
 * This class is used to authenticate all requests made to the Server.
 * Authentication is done by validating the authentication token with the Lobby Service.
 * The authentication token should correspond to the user making the request.
 */
@Component
public class Authenticator {
  private TokenHelper tokenHelper;

  /**
   * Empty constructor.
   */
  public Authenticator(@Autowired TokenHelper tokenHelper) {
    this.tokenHelper = tokenHelper;
  }

  /**
   * This method is used to authenticate a request.
   *
   * @param token    the authentication token
   * @param username the username of the user making the request
   * @throws AuthenticationException if the authentication fails
   */
  public void authenticate(String token, String username) throws AuthenticationException {
    if (!tokenHelper.validate(token, username)) {
      throw new AuthenticationException("Authentication token is invalid for user " + username);
    }
    if (!tokenHelper.isPlayer(token)) {
      throw new AuthenticationException("Token does not belong to a player.");
    }
  }

  /**
   * This method is used to authenticate a request is from an admin.
   *
   * @param token the authentication token
   * @throws AuthenticationException if the authentication fails
   */
  public void authenticateAdmin(String token, String username)
          throws AuthenticationException {
    if (!tokenHelper.validate(token, username, Role.ADMIN)) {
      throw new AuthenticationException("Admin privileges are required for this request");
    }
  }
}
