package edu.temple.dmhelper.bluetooth.message;

import edu.temple.dmhelper.Character;

public class PlayerLeftMessage extends Message {
    public static final String TYPE = "player_left";

    private final Character player;

    public PlayerLeftMessage(Character player) {
        super(TYPE);
        this.player = player;
    }

    public Character getPlayer() {
        return player;
    }
}
