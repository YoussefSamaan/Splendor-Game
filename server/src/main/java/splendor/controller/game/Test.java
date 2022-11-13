package splendor.controller.game;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import splendor.controller.lobbyservice.Authenticator;

/**
 * This class is used for testing purposes.
 */
@RestController
public class Test {

  public Test() {
    System.out.println("Test");
  }

  @GetMapping("/test")
  public boolean test(@RequestParam("token") String token,
                      @RequestParam("username") String username) {
    return Authenticator.authenticate(token, username);
  }
}
