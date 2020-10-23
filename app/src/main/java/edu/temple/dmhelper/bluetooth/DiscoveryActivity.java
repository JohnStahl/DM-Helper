package edu.temple.dmhelper.bluetooth;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import edu.temple.dmhelper.R;

public class DiscoveryActivity extends AppCompatActivity {
    public static final String EXTRA_DEVICE = "device";
    private BluetoothAdapter btAdapter = null;

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device != null) onDeviceDiscovered(device);
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                onDiscoveryComplete();
            }
        }
    };

    private TextView title;
    private ProgressBar progressBar;

    private BluetoothDeviceAdapter deviceAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discovery);
        setResult(RESULT_CANCELED);
        registerReceiver(receiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
        registerReceiver(receiver, new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED));

        btAdapter = BluetoothAdapter.getDefaultAdapter();
        deviceAdapter = new BluetoothDeviceAdapter(btAdapter.getBondedDevices());

        title = findViewById(R.id.discoverTitle);
        progressBar = findViewById(R.id.progressBar);

        Button cancelButton = findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        ListView deviceList = findViewById(R.id.deviceList);
        deviceList.setAdapter(deviceAdapter);
        deviceList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                stopDiscovery();
                Intent intent = new Intent();
                intent.putExtra(EXTRA_DEVICE, deviceAdapter.getItem(position));
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        startDiscovery();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopDiscovery();
        unregisterReceiver(receiver);
    }

    private void startDiscovery() {
        progressBar.setVisibility(View.VISIBLE);
        title.setText(R.string.bluetooth_discovering);

        if (btAdapter.isDiscovering())
            btAdapter.cancelDiscovery();

        btAdapter.startDiscovery();
    }

    private void stopDiscovery() {
        progressBar.setVisibility(View.GONE);

        title.setText(deviceAdapter.getCount() < 1 ?
                R.string.bluetooth_no_devices : R.string.bluetooth_finished_discover);

        if (btAdapter != null)
            btAdapter.cancelDiscovery();
    }

    private void onDeviceDiscovered(BluetoothDevice device) {
        deviceAdapter.addDevice(device);
    }

    private void onDiscoveryComplete() {
        stopDiscovery();
    }
}