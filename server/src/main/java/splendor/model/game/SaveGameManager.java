package splendor.model.game;

import com.google.gson.Gson;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * This class is responsible for saving games into json files, and loading them.
 */
public class SaveGameManager {

  private static String saveGamePath = System.getenv("SAVED_GAMES_PATH");
  private static final Logger LOGGER = Logger.getLogger(SaveGameManager.class.getName());

  /**
  * Save a game into a json file.
  *
  * @param gameId id of the game to save. File name will be gameId.json.
  * @param game the game to save.
  */
  public static void saveGame(long gameId, SplendorGame game) {
    LOGGER.info("Saving game " + gameId);
    String json = new Gson().toJson(game);
    writeToFile(json, gameId);
  }

  /**
  * Load a game from a json file.
  *
  * @param gameId id of the game to load. File name will be gameId.json.
  * @return the loaded game
  */
  public static SplendorGame loadGame(long gameId) {
    LOGGER.info("Loading game " + gameId);
    String fileName = saveGamePath + "/" + gameId + ".json";
    return readFromFile(fileName);
  }

  /**
   * Write a json string to a file.
   *
   * @param json the json string to write
   * @param gameId the id of the game
   */
  private static void writeToFile(String json, long gameId) {
    String fileName = saveGamePath + "/" + gameId + ".json";
    try (FileWriter file = new FileWriter(fileName)) {
      file.write(json);
      file.flush();
      LOGGER.info("Game saved to file " + fileName);
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException("Could not write to file " + fileName);
    }
  }

  /**
   * Read a json string from a file.
   *
   * @param fileName the name of the file to read from
   * @return the board read from the file
   */
  private static SplendorGame readFromFile(String fileName) {
    Gson gson = new Gson();
    try (FileReader reader = new FileReader(fileName)) {
      SplendorGame game = gson.fromJson(reader, SplendorGame.class);
      LOGGER.info("Game loaded from file " + fileName);
      return game;
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException("Could not read file");
    }
  }
}
