package edu.temple.dmhelper.bluetooth.message;

public class NextRoundMessage extends Message {
    public static final String TYPE = "next_round";

    public NextRoundMessage() {
        super(TYPE);
    }
}
