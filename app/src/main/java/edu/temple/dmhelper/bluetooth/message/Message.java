package edu.temple.dmhelper.bluetooth.message;

import java.io.Serializable;

public abstract class Message implements Serializable {
    private final String type;

    public Message(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
