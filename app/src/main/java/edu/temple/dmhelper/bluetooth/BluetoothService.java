package edu.temple.dmhelper.bluetooth;

import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import edu.temple.dmhelper.bluetooth.message.Message;

public class BluetoothService extends Service {
    public BluetoothService() {
    }

    class BluetoothServiceBinder extends Binder {
        void startServer() {
            BluetoothService.this.startServer();
        }

        void stopAcceptingClients() {
            BluetoothService.this.stopAcceptingClients();
        }

        void stopServer() {
            BluetoothService.this.stopServer();
        }

        void broadcastMessage(Message message) {
            BluetoothService.this.broadcastMessage(message);
        }

        void connectToServer(BluetoothDevice device) {
            BluetoothService.this.connectToServer(device);
        }

        void sendToServer(Message message) {
            BluetoothService.this.sendToServer(message);
        }

        void disconnect() {
            BluetoothService.this.disconnect();
        }
    }

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

    void broadcastMessage(Message message) {

    }

    void connectToServer(BluetoothDevice device) {

    }

    void sendToServer(Message message) {

    }

    void disconnect() {

    }
}
