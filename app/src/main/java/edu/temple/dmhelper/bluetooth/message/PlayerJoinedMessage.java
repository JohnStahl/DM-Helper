package edu.temple.dmhelper.bluetooth.message;

import edu.temple.dmhelper.Character;

public class PlayerJoinedMessage extends Message {
    public static final String TYPE = "player_joined";

    private final Character player;

    public PlayerJoinedMessage(Character player) {
        super(TYPE);
        this.player = player;
    }

    public Character getPlayer() {
        return player;
    }
}
