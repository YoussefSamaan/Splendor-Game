package splendor.controller.lobbyservice.communicationbeans;

import splendor.model.ModelAccessException;
import splendor.model.lobbyservice.PlayerReadOnly;

/**
 * Represents player scores. Can be created at any point during the gameplay or at the end of the game for final stats.
 *
 * @Author Maximilian Schiedermeier
 * @Date December 2020
 */
public class Ranking {

  private boolean gameOver;

  // Represents all players, ranked by their scored in descending order.
  private final PlayerReadOnly[] playersDescending;

  // Represents the scores of all players, in descending order. (players match playerDecending array order)
  private final int[] scoresDescending;

  public Ranking(PlayerReadOnly[] playersDescending, int[] scoresDescending, boolean gameOver) {

    if (playersDescending.length != scoresDescending.length)
      throw new RuntimeException("Unable to create ranking. Provided players and scored differ in size.");

    this.playersDescending = playersDescending;
    this.scoresDescending = scoresDescending;

    this.gameOver = gameOver;
  }

  public PlayerReadOnly[] getPlayersDescending() {
    return playersDescending;
  }

  public int[] getScoresDescending() {
    return scoresDescending;
  }

  public int getScoreForPlayer(String playerName) throws ModelAccessException {
    for (int i = 0; i < playersDescending.length; i++) {
      if (playersDescending[i].getName().toLowerCase().equals(playerName.toLowerCase()))
        return scoresDescending[i];
    }
    throw new ModelAccessException("Can not retrieve score for player " + playerName + ". No mention of player in this ranking.");
  }

  public boolean isGameOver() {
    return gameOver;
  }
}
