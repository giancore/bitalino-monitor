package com.example.bitalinomonitor.adapters;
import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.bitalinomonitor.R;
import com.example.bitalinomonitor.activities.DeviceActivity;
import com.example.bitalinomonitor.utils.UserApplication;

import java.util.List;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BluetoothItensAdapter extends RecyclerView.Adapter<BluetoothItensViewHolder> {
    private final List<BluetoothDevice> devices;
    private final Activity context;
    private UserApplication userApplication;
    public static final int REQUEST_CODE_SCAN_DEVICES = 11;

    public BluetoothItensAdapter(Activity context, List<BluetoothDevice> devices) {
        this.devices = devices;
        this.context = context;
    }
    @NonNull
    @Override
    public BluetoothItensViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        userApplication = (UserApplication)parent.getContext().getApplicationContext();

        return new BluetoothItensViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_bluetoothdevices, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BluetoothItensViewHolder holder, int position) {
        BluetoothDevice device = devices.get(position);

        holder.deviceName.setText(device.getName());
        holder.deviceAddress.setText(device.getAddress());
        holder.itemView.setOnClickListener(view -> connectDevice(view, device));
        //holder.deviceConnectButton.setOnClickListener(view -> connectDevice(view, device));
    }

    @Override
    public int getItemCount() {
        return devices == null ? 0 : devices.size();
    }

    public void updateList(BluetoothDevice device) {
        if (device != null && !devices.contains(device)) {
            devices.add(device);
            notifyItemInserted(getItemCount());
        }
    }

    public void removeAllItens() {
        for (int i = 0; i < devices.size(); i++) {
            devices.remove(i);
            notifyItemRemoved(i);
            notifyItemRangeChanged(i, devices.size());
        }
    }

    private void connectDevice(View view, BluetoothDevice device){
        userApplication.setSelectedDevice(device);

        Intent goToScanDevices = new Intent(view.getContext(), DeviceActivity.class);
        context.startActivityForResult(goToScanDevices, REQUEST_CODE_SCAN_DEVICES);
        context.finish();
        //view.getContext().startActivity(goToScanDevices);
    }
}
