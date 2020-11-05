package edu.temple.dmhelper.bluetooth.message;

public class GameStartMessage extends Message {
    public static final String TYPE = "game_start";

    public GameStartMessage() {
        super(TYPE);
    }
}
