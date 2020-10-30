package edu.temple.dmhelper.bluetooth.message;

import java.io.Serializable;

public abstract class BluetoothMessage implements Serializable {
    private final String type;

    public BluetoothMessage(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
