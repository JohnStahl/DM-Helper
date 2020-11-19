package edu.temple.dmhelper.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import edu.temple.dmhelper.bluetooth.message.BluetoothMessage;

public class BluetoothConnection extends Thread {
    private static final String TAG = "BluetoothConnection";

    private final BluetoothSocket socket;
    private final BluetoothDevice device;
    private final Handler handler;

    private final ObjectInputStream in;
    private final ObjectOutputStream out;

    public BluetoothConnection(BluetoothSocket socket, BluetoothDevice device, Handler handler, boolean server) throws IOException {
        this.socket = socket;
        this.device = device;
        this.handler = handler;

        if (server) {
            this.in = new ObjectInputStream(socket.getInputStream());
            this.out = new ObjectOutputStream(socket.getOutputStream());
        } else {
            this.out = new ObjectOutputStream(socket.getOutputStream());
            this.in = new ObjectInputStream(socket.getInputStream());
        }
    }

    @Override
    public void run() {
        while (!interrupted()) {
            try {
                BluetoothMessage btMsg = (BluetoothMessage) in.readObject();
                Log.d(TAG, "Received message: " + btMsg);
                btMsg.setConnection(BluetoothConnection.this);
                Message message = Message.obtain();
                message.obj = btMsg;
                handler.sendMessage(message);
            } catch (ClassNotFoundException | IOException e) {
                Log.e(TAG, "Unable to receive message", e);
            }
        }
    }

    public void sendMessage(BluetoothMessage message) {
        try {
            Log.d(TAG, "Sending message " + message);
            out.writeObject(message);
        } catch (IOException e) {
            Log.e(TAG, "Unable to send message", e);
        }
    }

    void disconnect() {
        try {
            this.in.close();
            this.out.close();
            this.socket.close();
        } catch (IOException e) {
            Log.e(TAG, "Failed to disconnect from the socket", e);
        }

        this.interrupt();
    }

    public interface OnConnectCallback {
        void onConnect(BluetoothConnection connection);
        void onConnectFailed();
    }

    public static void connect(final BluetoothDevice device, final Handler handler, final OnConnectCallback cb) throws IOException {
        final BluetoothSocket socket = device.createRfcommSocketToServiceRecord(BluetoothService.SERVICE_ID);

        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "Connecting to " + device.getName() + " (" + device.getAddress() + ")");
                try {
                    socket.connect();
                    Log.d(TAG, "Successfully connected");
                    BluetoothConnection conn = new BluetoothConnection(socket, device, handler, false);
                    conn.start();
                    cb.onConnect(conn);
                } catch (IOException e) {
                    Log.e(TAG, "Unable to connect to server", e);
                    try {
                        socket.close();
                    } catch (IOException e2) {
                        Log.e(TAG, "Unable to close socket", e2);
                    }
                    cb.onConnectFailed();
                }
            }
        }).start();
    }
}
