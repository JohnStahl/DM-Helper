package edu.temple.dmhelper.bluetooth.message;

public class GameStartMessage extends BluetoothMessage {
    public static final String TYPE = "game_start";

    public GameStartMessage() {
        super(TYPE);
    }
}
