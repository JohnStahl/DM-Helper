package edu.temple.dmhelper.bluetooth.message;

public class GameEndMessage extends BluetoothMessage {
    public static final String TYPE = "game_end";

    public GameEndMessage() {
        super(TYPE);
    }
}
