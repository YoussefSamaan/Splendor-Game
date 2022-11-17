package splendor.controller.lobbyservice.communicationbeans;

import splendor.model.lobbyservice.PlayerReadOnly;

/**
 * Encodes minimal information required for launch instructions.
 *
 * @Author Maximilian Schiedermeier
 * @Date December 2020
 */
public class Player implements PlayerReadOnly {

  String name;
  String preferredColour;

  public Player(String name, String preferredColour) {
    this.name = name;
    this.preferredColour = preferredColour;
  }

  public Player() {
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPreferredColour() {
    return preferredColour;
  }

  public void setPreferredColour(String preferredColour) {
    this.preferredColour = preferredColour;
  }

  @Override
  public boolean equals(Object other) {

    if (other == null)
      return false;

    if (other.getClass() != Player.class)
      return false;

    return (name == ((Player) other).name) && (preferredColour == ((Player) other).preferredColour);
  }

  @Override
  public String toString() {
    return "Name: " + name + "- PreferredColour: " + preferredColour;
  }


}
