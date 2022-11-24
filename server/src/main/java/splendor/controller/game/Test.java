package splendor.controller.game;

import javax.naming.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import splendor.controller.lobbyservice.Authenticator;

/**
 * This class is used for testing purposes.
 */
@RestController
public class Test {

  private Authenticator authenticator;

  public Test(@Autowired Authenticator authenticator) {
    this.authenticator = authenticator;
    System.out.println("Test");
  }

  @GetMapping("/test")
  public void test(@RequestParam("token") String token,
                      @RequestParam("username") String username) throws AuthenticationException {
    authenticator.authenticate(token, username);
  }
}
