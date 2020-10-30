package edu.temple.dmhelper.bluetooth.message;

import edu.temple.dmhelper.Character;

public class PlayerJoinMessage extends BluetoothMessage {
    public static final String TYPE = "player_joined";

    private final Character player;

    public PlayerJoinMessage(Character player) {
        super(TYPE);
        this.player = player;
    }

    public Character getPlayer() {
        return player;
    }
}
