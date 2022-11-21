package splendor.controller.lobbyservice;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import javax.annotation.PostConstruct;
import javax.naming.AuthenticationException;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import splendor.controller.helper.TokenHelper;

/**
 * This class is used to register game server with the LS on startup.
 */
@Component
public class Registrator {
  private static final String REGISTRATION_RESOURCE = "/api/gameservices";

  private final Logger logger = org.slf4j.LoggerFactory.getLogger(Registrator.class);

  private GameServiceParameters gameServiceParameters;

  private TokenHelper tokenHelper;

  /**
   * Constructor.
   *
   * @param gameServiceParameters the game service parameters used to register with LS
   * @param tokenHelper helper to resolve tokens
   */
  public Registrator(@Autowired GameServiceParameters gameServiceParameters,
                     @Autowired TokenHelper tokenHelper) {
    this.gameServiceParameters = gameServiceParameters;
    this.tokenHelper = tokenHelper;
    logger.info("Instantiated with gameServiceParameters: " + gameServiceParameters.toJson());
  }

  /**
   * This method is called after the bean is created and all the properties are set.
   * It registers the game service with the lobby service.
   */
  @PostConstruct
  public void register() {
    // Register on a separate thread to avoid blocking the main thread.
    new Thread(() -> {
      try {
        registerAtLobbyService(tokenHelper.get(gameServiceParameters.getOauth2Name(),
                gameServiceParameters.getOauth2Password()), 3);
      } catch (UnirestException | AuthenticationException e) {
        throw new RuntimeException(e);
      }
    }).start();
  }

  /**
   * Registers the game service with the lobby service.
   *
   * @param token OAuth2 access token
   * @param retries number of tries to register
   * @throws RuntimeException in case the request fails more than the number of retries
   */
  private void registerAtLobbyService(String token, int retries) {
    logger.info("Registering at lobby service. {} attempts remaining.", retries);
    String url =
            gameServiceParameters.getLobbyServiceLocation() + REGISTRATION_RESOURCE
            + "/" + gameServiceParameters.getName();
    try {
      HttpResponse<String> response = Unirest
              .put(url)
              .header("Authorization", "Bearer " + token)
              .header("Content-Type", "application/json")
              .body(gameServiceParameters.toJson())
              .asString();
      if (response.getStatus() != 200) {
        logger.error("Could not register at lobby service.\n Status: {}\n Body: {}",
                response.getStatus(), response.getBody());
        throw new UnirestException("Could not register at lobby service.");
      }
      logger.info("Successfully registered at lobby service.");
    } catch (UnirestException e) {
      if (retries > 0) {
        // sleep for 1 second and try again
        try {
          Thread.sleep(1000);
        } catch (InterruptedException e1) {
          throw new RuntimeException(e1);
        }
        registerAtLobbyService(token, retries - 1);
      } else {
        logger.error("Could not register at lobby service. Giving up.");
        throw new RuntimeException("Could not register at lobby service. Giving up.");
      }
    }
  }
}
