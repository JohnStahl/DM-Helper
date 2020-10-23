package edu.temple.dmhelper.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import edu.temple.dmhelper.bluetooth.message.Message;

public class BluetoothConnection {
    private final BluetoothSocket socket;
    private final BluetoothDevice device;
    private Thread readThread;

    public BluetoothConnection(BluetoothSocket socket, BluetoothDevice device) {
        this.socket = socket;
        this.device = device;
    }

    void sendMessage(Message message) {

    }
}
