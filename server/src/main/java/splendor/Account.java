package splendor;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;

@RestController
public class Account {

  public Account() {
  }

  @PostMapping("account/login")
  public String login(@RequestParam("username") String username, @RequestParam("password") String password) throws IOException {
    String urlString = String.format("http://127.0.0.1:4242/oauth/token?grant_type=password&username=%s&password=%s", username, password);
    URL url = new URL(urlString);
    HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
    httpConn.setRequestMethod("POST");

    byte[] message = ("bgp-client-name:bgp-client-pw").getBytes("UTF-8");
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
