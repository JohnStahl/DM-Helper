package edu.temple.dmhelper.bluetooth.message;

public class PlayerJoinedMessage extends Message {
    public static final String TYPE = "player_joined";

    public PlayerJoinedMessage() {
        super(TYPE);
    }
}
