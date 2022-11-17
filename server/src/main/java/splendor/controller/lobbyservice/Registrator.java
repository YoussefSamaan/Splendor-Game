package splendor.controller.lobbyservice;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import splendor.controller.lobbyservice.communicationbeans.GameServerParameters;
import splendor.controller.lobbyservice.communicationbeans.GameServiceRegistrationDetails;

import javax.annotation.PostConstruct;

/**
 * Util class that handles the registration / un-registration of a game-service based on the parameters provided in
 * application.properties. Configurable annotation allows injection of primitives form application.properties as class
 * fields at runtime.
 *
 * @Author: Maximilian Schiedermeier
 * @Date: December 2020
 */
@Component
public class Registrator {

  private final Logger logger;
  @Autowired
  private GameServiceRegistrationDetails lobbyServiceLocation;
  private String gameServiceLocation;
  private String gameServicePort;
  @Value("${oauth2.name}")
  private String serviceOauthName;
  @Value("${oauth2.password}")
  private String serviceOauthPassword;
  private GameServerParameters registrationParameters;
  @Value("${debug.skip.registration}")
  private boolean skipLobbyServiceCallbacks;
  @Value("${registration.retry}")
  private boolean registrationRetry;
  @Value("${registration.retry.timer}")
  private int registrationRetryTimerSeconds;

  @Autowired
  Registrator(@Value("${gameservice.name}")
              String gameServiceName,
              @Value("${gameservice.displayname}")
              String displayName,
              @Value("${gameservice.location}")
              String gameServiceLocation,
              @Value("${server.port}")
              String gameServicePort) {
    this.gameServiceLocation = gameServiceLocation;
    this.gameServicePort = gameServicePort;
    registrationParameters = new GameServerParameters(gameServiceName, displayName, gameServiceLocation, 2, 2, "true");

    logger = LoggerFactory.getLogger(Registrator.class);
  }

  /**
   * This method is implicitly called by spring upon creation of the Registrator bean. It ensures this game service
   * contacts the lobby service to register as available game server.
   */
  @PostConstruct
  private void init() throws InterruptedException {
    logger.info("Inferred registration location: " + registrationParameters.getLocation());

    // Actual registration procedure is started in extra thread, to avoid blocking deployment of queued
    // applications this game might depend on.
    new Thread(() -> safeRegisterAtLobbyService(registrationRetry)).start();
  }


  /**
   * Retrieves an OAuth2 token that allows registration / unregistration of Xox as a new GameService at the LS.
   * Credentials of an LS admin account are required.
   *
   * @return a string encoded OAuth token. Special characters are not yet URL enoded.
   * @throws UnirestException in case of a communication error with the LS
   */
  private String getToken() throws UnirestException {

    if (skipLobbyServiceCallbacks) {
      logger.warn("Token retrieval skipped.");
      return "DUMMY";
    }

    String lobbyServiceUrl = lobbyServiceLocation.getAssociatedLobbyLocation() + "/oauth/token";
    logger.info("Obtaining OAuth2 token using URL: " + lobbyServiceUrl);

    String bodyString = "grant_type=password&username=" + serviceOauthName + "&password=" + serviceOauthPassword;
    HttpResponse<String> response = Unirest
            .post(lobbyServiceUrl)
            // Authorization parameter is the base64 encoded string: "bgp-client-name:bgp-client-pw".
            // Can remain unchanged for future games.
            .header("Authorization", "Basic YmdwLWNsaWVudC1uYW1lOmJncC1jbGllbnQtcHc=")
            .header("Content-Type", "application/x-www-form-urlencoded")
            .body(bodyString)
            .asString();
    if (response.getStatus() != 200) {
      String noTokenError = "LS rejected Xox credentials. Make sure the \"xox\" user exists!";
      logger.error(noTokenError);
      throw new RuntimeException(noTokenError);
    }
    // Extract token of response JSON, escape potential special characters
    JsonObject responseJson = new JsonParser().parse(response.getBody()).getAsJsonObject();
    String token = responseJson.get("access_token").toString().replaceAll("\"", "");
    logger.info("Retrieved xox oauth2 token: " + token);
    return token;
  }

  /**
   * Registers Xox as a Game-service at the LS game registry. Admin credentials are required to authorize this
   * operation.
   *
   * @throws InterruptedException in case thread sleep failed for delayed retry.
   */
  public void registerAtLobbyService(boolean retry) throws InterruptedException {

    if (skipLobbyServiceCallbacks) {
      logger.warn("Registration skipped.");
      return;
    }

    // Build full qualified lobbyservice location URL string.
    String lobbyServiceUrl = lobbyServiceLocation.getAssociatedLobbyLocation() + "/api/gameservices/"+registrationParameters.getName();
    logger.info("Registering using URL: " + lobbyServiceUrl);

    // try / catch block to allow for second connection attempt in case BGP not yet powered up.
    try {
      // Get a valid access token, to authenticate for the registration.
      String accessToken = getToken();


      // Build and send an authenticated registration request to the LS API.
      String bodyJson = new Gson().toJson(registrationParameters);
      HttpResponse<String> response = Unirest
              .put(lobbyServiceUrl)
              .header("Authorization", "Bearer " + accessToken)
              .header("Content-Type", "application/json")
              .body(bodyJson)
              .asString();

      // Verify the registration was accepted
      if (response.getStatus() != 200) {
        logger.error("LobbyService (" + lobbyServiceUrl + ") rejected registration of Game. Server replied:\n" + response.getStatus() + " - " + response.getBody());
        throw new RuntimeException("LobbyService rejected registration of Game. Server replied:\n" + response.getStatus() + " - " + response.getBody());
      }
      logger.info("Succesfully registered at LobbyService.");
    }

    // In case of connection issues (and retry flag set) give it a second try in 30 seconds.
    catch (UnirestException unirestException) {

      if (retry) {
        logger.info("First connection attempt to BGP not successful. Will retry one more time in "+registrationRetryTimerSeconds+" seconds.");
        Thread.sleep(registrationRetryTimerSeconds * 1000);
        registerAtLobbyService(false);
        return;
      } else {
        String errorMessage = "LobbyService not reachable at provided location: " + lobbyServiceUrl;
        logger.error(errorMessage);
        throw new RuntimeException(errorMessage);
      }
    }
  }

  /**
   * Sleep safe wrapper for previous method. Allows usage in anonymous lambdas.
   *
   * @param retry to indicate whether registration should be reattempted after a predefined delay.
   */
  public void safeRegisterAtLobbyService(boolean retry) {
    try {
      registerAtLobbyService(retry);
    } catch (InterruptedException e) {
      throw new RuntimeException("Failed to await registration retry.");
    }
  }

  /**
   * Revoke a previous registration at the LS game registry. This is typically performed prior to shutdown. Admin
   * credentials are required to authorize this operation.
   */
  public void unregisterAtLobbyService() throws UnirestException {

    if (skipLobbyServiceCallbacks) {
      logger.warn("Unregistration skipped.");
      return;
    }

    // Get a valid access token, to authenticate for the un-registration.
    String accessToken = getToken();

    String lobbyServiceUrl = lobbyServiceLocation.getAssociatedLobbyLocation() + "/api/gameservices/Xox";
    logger.info("Unregistering using URL: " + lobbyServiceUrl);

    // Build and send an authenticated un-registration request to the LS API.
    HttpResponse<String> response = Unirest
            .delete(lobbyServiceUrl)
            .header("Authorization", "Bearer " + accessToken)
            .asString();

    // Verify the registration was accepted
    if (response.getStatus() != 200)
      logger.error("LobbyService rejected unregistration of Xox. Server replied:\n" + response.getStatus() + " - " + response.getBody());
  }

  // ToDo: Add method to notify LS about an ended game.
  public void notifyGameOver(long gameId) throws UnirestException {
    if (skipLobbyServiceCallbacks) {
      logger.warn("Notify game-over skipped.");
      return;
    }

    // Get a valid access token, to authenticate for the un-registration.
    String accessToken = getToken();

    String lobbyServiceUrl = lobbyServiceLocation.getAssociatedLobbyLocation() + "/api/sessions/" + gameId;
    logger.info("Notifying LS about gameover using URL: " + lobbyServiceUrl);


    // Build and send an authenticated game-over request to the LS API.
    HttpResponse<String> response = Unirest
            .delete(lobbyServiceUrl)
            .header("Authorization", "Bearer " + accessToken)
            .asString();

    // Verify the registration was accepted
    if (response.getStatus() != 200)
      logger.error("LobbyService rejected Game-Over notification of Xox session. Server replied:\n" + response.getStatus() + " - " + response.getBody());

  }
}
