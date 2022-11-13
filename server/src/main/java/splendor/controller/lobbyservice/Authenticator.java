package splendor.controller.lobbyservice;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import splendor.Config;

/**
 * This class is used to authenticate all requests made to the Server.
 * Authentication is done by validating the authentication token with the Lobby Service.
 * The authentication token should correspond to the user making the request.
 */
public class Authenticator {
  private static final String ENDPOINT = "/oauth/username";

  /**
   * This method is used to authenticate a request.
   *
   * @param token    the authentication token
   * @param username the username of the user making the request
   * @return true if the token is valid, false otherwise
   */
  public static boolean authenticate(String token, String username) {
    try {
      String lobbyServiceUrl = Config.getProperty("lobbyServiceURL")
              + ENDPOINT + "?access_token=" + token;
      System.out.println(lobbyServiceUrl);
      URL url = new URL(lobbyServiceUrl);
      HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
      httpConn.setRequestMethod("GET");

      InputStream responseStream = httpConn.getResponseCode() == 200
              ? httpConn.getInputStream()
              : httpConn.getErrorStream();
      Scanner s = new Scanner(responseStream).useDelimiter("\\A");
      String response = s.hasNext() ? s.next() : "";
      System.out.println(response);
      return response.equals(username);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
