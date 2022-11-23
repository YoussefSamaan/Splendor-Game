package splendor.controller.lobbyservice;

import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import splendor.model.game.GameManager;

/**
 * Rest Controller responsible for starting/deleting/saving games.
 */
@RestController
public class GameHandler {

  private GameManager gameManager;
  private static final Logger LOGGER = Logger.getLogger(GameHandler.class.getName());

  public GameHandler(@Autowired GameManager gameManager) {
    this.gameManager = gameManager;
  }

  /**
   * This endpoint is called by the LS when a new session is launched.
   * It creates a new game
   *
   * @param gameInfo the info of the game to create
   * @return the status of the request
   */
  @PutMapping(value = "/api/games/{gameId}", consumes = "application/json; charset=utf-8")
  public ResponseEntity startGame(@PathVariable long gameId, @RequestBody GameInfo gameInfo) {
    try {
      LOGGER.info(String.format("Received request to create game with gameInfo %s",
                                gameInfo.toString()));
      LOGGER.info(String.format("Starting game with id %d", gameId));
      gameManager.createGame(gameInfo, gameId);
    } catch (IllegalArgumentException e) {
      LOGGER.warning(e.getMessage());
      return ResponseEntity.badRequest().body(e.getMessage());
    }
    LOGGER.info(String.format("Game with id %d started", gameId));
    return ResponseEntity.ok().build();
  }

  /**
   * This endpoint is called by the LS when a session is deleted.
   * It deletes the game.
   *
   * @param gameId the id of the game to delete
   * @return the status of the request
   */
  @DeleteMapping("/api/games/{gameId}")
  public ResponseEntity deleteGame(@PathVariable long gameId) {
    try {
      LOGGER.info(String.format("Deleting game with id %d", gameId));
      gameManager.deleteGame(gameId);
    } catch (IllegalArgumentException e) {
      LOGGER.warning(e.getMessage());
      return ResponseEntity.badRequest().body(e.getMessage());
    }
    LOGGER.info(String.format("Game with id %d deleted", gameId));
    return ResponseEntity.ok().build();
  }

}
