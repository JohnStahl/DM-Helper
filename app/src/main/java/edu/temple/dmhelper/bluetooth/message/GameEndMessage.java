package edu.temple.dmhelper.bluetooth.message;

public class GameEndMessage extends Message {
    public static final String TYPE = "game_end";

    public GameEndMessage() {
        super(TYPE);
    }
}
