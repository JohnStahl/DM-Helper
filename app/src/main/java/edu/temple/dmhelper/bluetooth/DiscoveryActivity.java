package edu.temple.dmhelper.bluetooth;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import edu.temple.dmhelper.R;

public class DiscoveryActivity extends AppCompatActivity {
    private BluetoothAdapter btAdapter = null;

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device != null) onDeviceDiscovered(device, intent);
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                onDiscoveryComplete(intent);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discovery);

        btAdapter = BluetoothAdapter.getDefaultAdapter();

        registerReceiver(receiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
        registerReceiver(receiver, new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED));

        startDiscovery();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopDiscovery();
        unregisterReceiver(receiver);
    }

    private void startDiscovery() {
        if (btAdapter.isDiscovering())
            btAdapter.cancelDiscovery();

        btAdapter.startDiscovery();
    }

    private void stopDiscovery() {
        if (btAdapter != null)
            btAdapter.cancelDiscovery();
    }

    private void onDeviceDiscovered(BluetoothDevice device, Intent intent) {
        String deviceName = device.getName();
        String deviceHardwareAddress = device.getAddress(); // MAC address
    }

    private void onDiscoveryComplete(Intent intent) {
        stopDiscovery();
    }
}