package edu.temple.dmhelper.bluetooth;

import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;

import java.util.UUID;

import edu.temple.dmhelper.bluetooth.message.BluetoothMessage;

public class BluetoothService extends Service {
    public static final UUID SELF_ID = UUID.randomUUID();

    public BluetoothService() {
    }

    class BluetoothServiceBinder extends Binder {
        void startServer(Handler handler) {
            BluetoothService.this.handler = handler;
            BluetoothService.this.startServer();
        }

        void stopAcceptingClients() {
            BluetoothService.this.stopAcceptingClients();
        }

        void stopServer() {
            BluetoothService.this.stopServer();
        }

        void broadcastMessage(BluetoothMessage message) {
            BluetoothService.this.broadcastMessage(message);
        }

        void connectToServer(BluetoothDevice device, Handler handler) {
            BluetoothService.this.handler = handler;
            BluetoothService.this.connectToServer(device);
        }

        void sendToServer(BluetoothMessage message) {
            BluetoothService.this.sendToServer(message);
        }

        void disconnect() {
            BluetoothService.this.disconnect();
        }
    }

    private Handler handler;

    @Override
    public IBinder onBind(Intent intent) {
        return new BluetoothServiceBinder();
    }

    void startServer() {

    }

    void stopAcceptingClients() {

    }

    void stopServer() {

    }

    void broadcastMessage(BluetoothMessage message) {

    }

    void connectToServer(BluetoothDevice device) {

    }

    void sendToServer(BluetoothMessage message) {

    }

    void disconnect() {

    }
}
