package splendor.controller.game;

import com.google.gson.Gson;
import java.util.logging.Logger;
import javax.naming.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import splendor.controller.helper.Authenticator;

/**
 * Controller responsible for all HTTP requests specific to a game.
 */
@RestController
public class SplendorController extends HandlerInterceptorAdapter {
  private static final Logger LOGGER = Logger.getLogger(SplendorController.class.getName());
  private GameManager gameManager;
  private Authenticator authenticator;

  public SplendorController(@Autowired GameManager gameManager,
                            @Autowired Authenticator authenticator) {
    this.gameManager = gameManager;
    this.authenticator = authenticator;
  }

  /**
   * Validate all requests have correct access token.
   * Validate all requests have correct game id, if applicable.
   * This method is called before any request is processed.
   */
  @Override
  public boolean preHandle(javax.servlet.http.HttpServletRequest request,
                           javax.servlet.http.HttpServletResponse response,
                           Object handler) throws Exception {
    String accessToken = request.getParameter("access_token");
    String username = request.getParameter("username");
    LOGGER.info(String.format("Received request to %s with access token %s and username %s",
                              request.getRequestURI(), accessToken, username));
    // Check if access token is valid
    try {
      authenticator.authenticate(accessToken, username);
    } catch (AuthenticationException e) {
      LOGGER.warning(e.getMessage());
      response.sendError(401, e.getMessage());
      return false;
    }
    return true;
  }

  /**
   * Gets the board of a specific game. All information is shared with all players, so all
   * players get the same view of the board.
   *
   * @param gameId the id of the game.
   */
  @GetMapping("/api/games/{gameId}/board")
  public ResponseEntity getBoard(@PathVariable long gameId) {
    LOGGER.info(String.format("Received request to get board of game with id %d", gameId));
    if (!gameManager.exists(gameId)) {
      LOGGER.warning(String.format("Game with id %d does not exist", gameId));
      return ResponseEntity.badRequest().body(String.format("Game with id %d does not exist",
          gameId));
    }
    String body = new Gson().toJson(gameManager.getBoard(gameId));
    return ResponseEntity.ok().body(body);
  }

  /**
   * Generates all the actions that can be performed by the player.
   * This is used by the client to generate the buttons that the player can click.
   *
   * @param gameId the id of the game.
   * @param username the username of the player.
   * @return a list of actions that the player can perform.
   */
  @GetMapping("/api/games/{gameId}/players/{username}/actions")
  public ResponseEntity getActions(@PathVariable long gameId, @PathVariable String username) {
    LOGGER.info(String.format("Received request to get actions of player %s in game with id %d",
                              username, gameId));
    if (!gameManager.exists(gameId)) {
      return ResponseEntity.badRequest().body(String.format("Game with id %d does not exist",
          gameId));
    }
    String body = new Gson().toJson(gameManager.generateActions(gameId, username));
    return ResponseEntity.ok().body(body);
  }
}
