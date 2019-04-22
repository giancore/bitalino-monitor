package com.example.bitalinomonitor.adapters;

import android.view.View;
import android.widget.TextView;

import com.example.bitalinomonitor.R;
import com.google.android.material.button.MaterialButton;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class BluetoothItensViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.device_name)
    public TextView deviceName;

    @BindView(R.id.device_address)
    public TextView deviceAddress;

    public BluetoothItensViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}