package edu.temple.dmhelper.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.temple.dmhelper.R;

public class BluetoothDeviceAdapter extends BaseAdapter {
    private List<BluetoothDevice> devices = new ArrayList<>();

    BluetoothDeviceAdapter(Collection<BluetoothDevice> initialDevices) {
        devices.addAll(initialDevices);
    }

    @Override
    public int getCount() {
        return devices.size();
    }

    @Override
    public BluetoothDevice getItem(int position) {
        return devices.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.bluetooth_device, parent, false);
        }

        BluetoothDevice device = getItem(position);

        boolean isPresent = device != null;
        String name = isPresent ? device.getName() : "";
        String address = isPresent ? device.getAddress() : "";

        ((TextView) convertView.findViewById(R.id.deviceName)).setText(name);
        ((TextView) convertView.findViewById(R.id.deviceAddress)).setText(address);
        return convertView;
    }

    public void addDevice(BluetoothDevice device) {
        this.devices.add(device);
        notifyDataSetChanged();
    }
}
