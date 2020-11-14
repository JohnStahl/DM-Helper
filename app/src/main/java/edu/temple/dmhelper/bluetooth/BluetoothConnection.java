package edu.temple.dmhelper.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import edu.temple.dmhelper.bluetooth.message.BluetoothMessage;

public class BluetoothConnection {
    private final BluetoothSocket socket;
    private final BluetoothDevice device;
    private final Handler handler;

    private final ObjectInputStream in;
    private final ObjectOutputStream out;


    private Thread readThread = new Thread(new Runnable() {
        @Override
        public void run() {
            while (!Thread.interrupted()) {
                try {
                    Message message = Message.obtain();
                    message.obj = in.readObject();
                    handler.sendMessage(message);
                } catch (ClassNotFoundException | IOException e) {
                    e.printStackTrace();
                }
            }
        }
    });

    public BluetoothConnection(BluetoothSocket socket, BluetoothDevice device, Handler handler) throws IOException {
        this.socket = socket;
        this.device = device;
        this.handler = handler;

        this.in = new ObjectInputStream(socket.getInputStream());
        this.out = new ObjectOutputStream(socket.getOutputStream());

        readThread.start();
    }

    void sendMessage(BluetoothMessage message) {
        try {
            out.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void disconnect() {
        try {
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.readThread.interrupt();
    }

    public static BluetoothConnection connect(BluetoothDevice device, Handler handler) throws IOException {
        BluetoothSocket socket = device.createRfcommSocketToServiceRecord(BluetoothService.SERVICE_ID);
        socket.connect();
        return new BluetoothConnection(socket, device, handler);
    }
}
