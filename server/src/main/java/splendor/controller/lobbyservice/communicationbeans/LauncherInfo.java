package eu.kartoffelquadrat.lobbyservice.samplegame.controller.communcationbeans;

import java.util.LinkedList;

/**
 * Bean that compiles all information of the json object received for every session start.
 *
 * @Author: Maximilian Schiedermeier
 * @Date: December 2020
 */
public class LauncherInfo {

    // Just for verification, the name of the registered game.
    String gameServer;

    // List of players, in seating order and the preferred colours.
    LinkedList<Player> players;

    // Creator of the game. Typically the first player.
    String creator;

    // Optional id of a savegame to load.
    String savegame;

    public LauncherInfo() {
    }

    public LauncherInfo(String gameServer, LinkedList<Player> players, String creator) {
        this.gameServer = gameServer;
        this.players = players;
        this.creator = creator;
        savegame = "";
    }

    public LauncherInfo(String gameServer, LinkedList<Player> players, String creator, String savegame) {
        this.gameServer = gameServer;
        this.players = players;
        this.creator = creator;
        this.savegame = savegame;
    }

    public String getGameServer() {
        return gameServer;
    }

    public void setGameServer(String gameServer) {
        this.gameServer = gameServer;
    }

    public LinkedList<Player> getPlayers() {
        return players;
    }

    public void setPlayers(LinkedList<Player> players) {
        this.players = players;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getSavegame() {
        return savegame;
    }

    public void setSavegame(String savegame) {
        this.savegame = savegame;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("");
        sb.append("GameServer: " + gameServer + "\n");
        sb.append("Players:\n");
        for (Player playerinfo : players) {
            sb.append("\t> ").append(playerinfo.toString()).append("\n");
        }
        sb.append("Creator: ").append(creator);
        sb.append("Savegame: ").append(savegame);
        return sb.toString();
    }

}
