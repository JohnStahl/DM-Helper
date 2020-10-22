package edu.temple.dmhelper.bluetooth.message;

public class PlayerLeftMessage extends Message {
    public static final String TYPE = "player_left";

    public PlayerLeftMessage() {
        super(TYPE);
    }
}
