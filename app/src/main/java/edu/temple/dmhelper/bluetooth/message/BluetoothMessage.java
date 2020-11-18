package edu.temple.dmhelper.bluetooth.message;

import java.io.Serializable;

import edu.temple.dmhelper.bluetooth.BluetoothConnection;

public abstract class BluetoothMessage implements Serializable {
    private final String type;
    private transient BluetoothConnection connection;

    public BluetoothMessage(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public BluetoothConnection getFrom() {
        return connection;
    }

    public void setConnection(BluetoothConnection connection) {
        this.connection = connection;
    }
}
