package edu.temple.dmhelper.bluetooth;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import edu.temple.dmhelper.bluetooth.message.BluetoothMessage;

public class BluetoothService extends Service {
    public static final UUID SERVICE_ID = UUID.fromString("95a433f3-8d97-446a-a6d8-d8f7d40fe097");
    public static final String TAG = "BluetoothService";

    public BluetoothService() {
    }

    public class BluetoothBinder extends Binder {
        public void startServer(Handler handler) throws IOException {
            BluetoothService.this.handler = handler;
            BluetoothService.this.startServer();
        }

        public void stopAcceptingClients() {
            BluetoothService.this.stopAcceptingClients();
        }

        public void stopServer() throws IOException {
            BluetoothService.this.stopServer();
        }

        public void sendMessage(BluetoothMessage message) {
            BluetoothService.this.sendMessage(message);
        }

        public void connectToServer(BluetoothDevice device, Handler handler, BluetoothConnection.OnConnectCallback cb) throws IOException {
            BluetoothService.this.handler = handler;
            BluetoothService.this.connectToServer(device, cb);
        }

        public void disconnect() {
            BluetoothService.this.disconnect();
        }
    }

    // Properties for server
    private volatile boolean acceptClients = true;
    private BluetoothServerSocket serverSocket = null;
    private final List<BluetoothConnection> clients = new ArrayList<>();
    private final Thread clientAcceptor = new Thread() {
        @Override
        public void run() {
            if (!connected || !server) return;

            while (acceptClients) {
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
        }
    };

    // Properties for client
    private BluetoothConnection serverConnection;

    // Both
    private Handler handler;
    private boolean server;
    private boolean connected;
    private BluetoothAdapter btAdapter;

    @Override
    public IBinder onBind(Intent intent) {
        return new BluetoothBinder();
    }

    @Override
    public void onCreate() {
        btAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    void startServer() throws IOException {
        if (!connected) {
            server = true;
            connected = true;
            this.serverSocket = this.btAdapter.listenUsingRfcommWithServiceRecord("DM's Device", SERVICE_ID);
            this.clientAcceptor.start();
        }
    }

    void stopAcceptingClients() {
        this.acceptClients = false;
    }

    void stopServer() throws IOException {
        if (connected && server) {
            connected = false;
            acceptClients = false;
            this.serverSocket.close();
            this.serverSocket = null;
        }
    }

    void sendMessage(BluetoothMessage message) {
        if (server) {
            for (BluetoothConnection client : clients)
                client.sendMessage(message);
        } else {
            serverConnection.sendMessage(message);
        }
    }

    void connectToServer(BluetoothDevice device, final BluetoothConnection.OnConnectCallback cb) throws IOException {
        if (!connected) {
            server = false;
            BluetoothConnection.connect(device, handler, new BluetoothConnection.OnConnectCallback() {
                @Override
                public void onConnect(BluetoothConnection connection) {
                    Log.d(TAG, "Successfully connected to the server");
                    connected = true;
                    serverConnection = connection;
                    cb.onConnect(connection);
                }

                @Override
                public void onConnectFailed() {
                    Log.e(TAG, "Failed to connect to the server");
                    connected = false;
                    cb.onConnectFailed();
                }
            });
        }
    }

    void disconnect() {
        if (connected && !server) {
            connected = false;
            this.serverConnection.disconnect();
            this.serverConnection = null;
        }
    }
}
