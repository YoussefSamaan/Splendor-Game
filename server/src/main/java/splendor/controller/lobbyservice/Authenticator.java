package splendor.controller.lobbyservice;

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
   * @throws LogicException if the authentication fails
   */
  public static void authenticate(String token, String username) throws LogicException {
    if (!TokenValidator.isMatchingPlayer(username, token)) {
      throw new LogicException("Received token does not match player of to accessed resource.");
    }

    // Verify the provided token is a player token
    if (!TokenValidator.isPlayerToken(token)) {
      throw new LogicException("Received token is an admin token but a player token is required.");
    }
  }
}
