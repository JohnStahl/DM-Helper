package edu.temple.dmhelper.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class ClientAcceptor implements Runnable {
    private static final String TAG = "ClientAcceptor";

    private Thread worker;
    private final AtomicBoolean running = new AtomicBoolean(false);
    private final AtomicBoolean stopped = new AtomicBoolean(true);
    private final BluetoothServerSocket serverSocket;
    private final Handler handler;
    private final ArrayList<BluetoothConnection> clients;

    public ClientAcceptor(BluetoothServerSocket serverSocket, Handler handler, ArrayList<BluetoothConnection> clients) {
        this.serverSocket = serverSocket;
        this.handler = handler;
        this.clients = clients;
    }

    public void start() {
        this.worker = new Thread(this);
        this.worker.start();
    }

    public void interrupt() {
        this.running.set(false);
        this.worker.interrupt();
    }

    @Override
    public void run() {
        running.set(true);
        stopped.set(false);

        while (running.get()) {
            BluetoothSocket socket;
            try {
                socket = serverSocket.accept();
            } catch (IOException ex) {
                Log.e(TAG, "Failed to accept client connection", ex);
                break;
            }

            if (socket == null) continue;

            try {
                BluetoothDevice device = socket.getRemoteDevice();
                Log.d(TAG, "Opening connection to the client " + device.getName() + " (" + device.getAddress() + ")");
                BluetoothConnection conn = new BluetoothConnection(socket, device, handler, true);
                clients.add(conn);
                conn.start();
            } catch (IOException ex) {
                Log.e(TAG, "Failed to open connection to client", ex);
                break;
            }
        }

        stopped.set(true);
    }
}
