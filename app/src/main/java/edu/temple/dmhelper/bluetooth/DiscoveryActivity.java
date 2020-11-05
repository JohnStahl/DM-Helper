package edu.temple.dmhelper.bluetooth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import edu.temple.dmhelper.R;

public class DiscoveryActivity extends AppCompatActivity {
    private static final String TAG = "DiscoveryActivity";

    private static final int REQUEST_LOCATION_PERMISSION = 1;

    public static final String ACTION_DEVICE_SELECTED = "edu.temple.dmhelper.discovery.action.DEVICE_SELECTED";
    public static final String EXTRA_DEVICE = "edu.temple.dmhelper.discovery.extra.DEVICE";
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
    private Button refreshButton;

    private BluetoothDeviceAdapter deviceAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discovery);
        setResult(RESULT_CANCELED);

        btAdapter = BluetoothAdapter.getDefaultAdapter();

        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(receiver, filter);

        deviceAdapter = new BluetoothDeviceAdapter(btAdapter.getBondedDevices());

        title = findViewById(R.id.discoverTitle);
        progressBar = findViewById(R.id.progressBar);

        refreshButton = findViewById(R.id.refreshButton);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetDeviceList();
                startDiscovery();
            }
        });

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
                Intent intent = new Intent(ACTION_DEVICE_SELECTED);
                intent.putExtra(EXTRA_DEVICE, deviceAdapter.getItem(position));
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        checkBluetoothEnabled();
        checkLocationPermission();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopDiscovery();
        unregisterReceiver(receiver);
    }

    private void resetDeviceList() {
        deviceAdapter.resetDevices();
        deviceAdapter.addAllDevices(btAdapter.getBondedDevices());
    }

    private void startDiscovery() {
        progressBar.setVisibility(View.VISIBLE);
        refreshButton.setVisibility(View.GONE);
        title.setText(R.string.bluetooth_discovering);

        if (btAdapter.isDiscovering())
            btAdapter.cancelDiscovery();

        Log.d(TAG, "Discovering devices");
        btAdapter.startDiscovery();
    }

    private void stopDiscovery() {
        Log.d(TAG, "Cancelling discovery");
        progressBar.setVisibility(View.GONE);
        refreshButton.setVisibility(View.VISIBLE);

        title.setText(deviceAdapter.getCount() < 1 ?
                R.string.bluetooth_no_devices : R.string.bluetooth_finished_discover);

        if (btAdapter != null)
            btAdapter.cancelDiscovery();
    }

    private void onDeviceDiscovered(BluetoothDevice device) {
        Log.d(TAG, "Discovered new device: " + device.toString());
        deviceAdapter.addDevice(device);
    }

    private void onDiscoveryComplete() {
        Log.d(TAG, "Discovery complete");
        stopDiscovery();
    }

    private void checkLocationPermission() {
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[] { Manifest.permission.ACCESS_FINE_LOCATION }, REQUEST_LOCATION_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    new AlertDialog.Builder(this)
                            .setTitle(R.string.location_rationale_title)
                            .setMessage(R.string.location_rationale_message)
                            .setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    checkLocationPermission();
                                }
                            })
                            .create().show();
                } else {
                    Toast.makeText(this, R.string.cannot_request_location, Toast.LENGTH_LONG).show();
                }
            } else {
                startDiscovery();
            }
        }
    }

    private void checkBluetoothEnabled() {
        if (!btAdapter.isEnabled())
            if (btAdapter.enable())
                Toast.makeText(this, R.string.bluetooth_failed_to_enable, Toast.LENGTH_LONG).show();
    }
}