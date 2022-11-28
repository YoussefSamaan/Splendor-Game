package splendor.controller.game;

import com.google.gson.Gson;
import java.lang.reflect.Field;
import java.util.logging.Logger;
import javax.naming.AuthenticationException;
import javax.naming.InsufficientResourcesException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import splendor.controller.helper.Authenticator;
import splendor.model.game.Bank;
import splendor.model.game.Board;
import splendor.model.game.action.ActionData;
import splendor.model.game.action.InvalidAction;
import splendor.model.game.payment.Token;

/**
 * Controller responsible for all HTTP requests specific to a game.
 */
@RestController
public class SplendorController extends HandlerInterceptorAdapter {
  private static final Logger LOGGER = Logger.getLogger(SplendorController.class.getName());
  private final GameManager gameManager;
  private final Authenticator authenticator;

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
  @GetMapping(value = "/api/games/{gameId}/board")
  public ResponseEntity getBoard(@PathVariable long gameId) {
    LOGGER.info(String.format("Received request to get board of game with id %d", gameId));
    if (!gameManager.exists(gameId)) {
      LOGGER.warning(String.format("Game with id %d does not exist", gameId));
      return ResponseEntity.badRequest().body(String.format("Game with id %d does not exist",
          gameId));
    }
    LOGGER.info(String.format("Returning board of game with id %d", gameId));
    String body = new Gson().toJson(gameManager.getBoard(gameId));
    return ResponseEntity.ok().body(body);
  }

  /**
   * Generates all the actions that can be performed by the player.
   * This is used by the client to generate the buttons that the player can click.
   *
   * @param gameId   the id of the game.
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
    if (!gameManager.playerInGame(gameId, username)) {
      return ResponseEntity.badRequest().body(String.format("Player %s is not in game with id %d",
          username, gameId));
    }
    String body = new Gson().toJson(gameManager.generateActions(gameId, username));
    return ResponseEntity.ok().body(body);
  }

  /**
   * Performs a previously generated action.
   *
   * @param gameId     the id of the game.
   * @param username   the username of the player.
   * @param actionId   the id of the action.
   */
  @PostMapping("/api/games/{gameId}/players/{username}/actions/{actionId}")
  public ResponseEntity performAction(@PathVariable long gameId,
                                      @PathVariable String username,
                                      @PathVariable String actionId) {
    LOGGER.info(String.format("Received request to perform action %s of player %s in game %d",
        actionId, username, gameId));
    if (!gameManager.exists(gameId)) {
      return ResponseEntity.badRequest().body(String.format("Game with id %d does not exist",
          gameId));
    }
    if (!gameManager.playerInGame(gameId, username)) {
      return ResponseEntity.badRequest().body(String.format("Player %s is not in game with id %d",
          username, gameId));
    }
    try {
      ActionData actionData = new ActionData(); // dummy for now
      gameManager.performAction(gameId, username, actionId, actionData);
      LOGGER.info(String.format("Performed action %s of player %s in game with id %d",
          actionId, username, gameId));
      return ResponseEntity.ok().build();
    } catch (InvalidAction | InsufficientResourcesException e) {
      LOGGER.warning(e.getMessage());
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }
}
