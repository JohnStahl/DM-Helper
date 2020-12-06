package edu.temple.dmhelper.bluetooth.message;

public class NextRoundMessage extends BluetoothMessage {
    public static final String TYPE = "next_round";

    public NextRoundMessage() {
        super(TYPE);
    }
}
