package splendor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * This class is responsible for account management.
 */
@RestController
public class Account {

  /**
   * Constructor for the Account class.
   */
  public Account() {
  }

  /**
   * This method is responsible for handling the login requests.
   *
   * @param username the username of the user.
   * @param password the password of the user.
   * @return the response of the login request to the Lobby Service.
   */
  @PostMapping("account/login")
  public String login(@RequestParam("username") String username,
                      @RequestParam("password") String password) throws IOException {
    System.out.println(
            "Received login request for user " + username + " at " + System.currentTimeMillis());
    String urlString = String.format(
            "http://127.0.0.1:4242/oauth/token?grant_type=password&username=%s&password=%s", username, password
    );
    URL url = new URL(urlString);
    HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
    httpConn.setRequestMethod("POST");

    byte[] message = ("bgp-client-name:bgp-client-pw").getBytes(StandardCharsets.UTF_8);
    String encoded = Base64.getEncoder().encodeToString(message);
    httpConn.setRequestProperty("Authorization", "Basic " + encoded);

    BufferedReader in = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
    String inputLine;
    StringBuilder result = new StringBuilder();
    while ((inputLine = in.readLine()) != null) {
      result.append(inputLine);
    }
    in.close();
    return result.toString();
  }
}
