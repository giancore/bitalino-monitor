package com.example.bitalinomonitor.utils;

import android.app.Application;
import android.bluetooth.BluetoothDevice;
import android.content.res.Configuration;

public class UserApplication extends Application {
    private static UserApplication singleton;

    private BluetoothDevice selectedDevice;
    private int selectedSamplingFrequency;

    public UserApplication() {
    }

    public BluetoothDevice getSelectedDevice() {
        return selectedDevice;
    }

    public int getSelectedSamplingFrequency() { return selectedSamplingFrequency; }

    public void setSelectedDevice(BluetoothDevice selectedDevice) {
        this.selectedDevice = selectedDevice;
    }

    public void setSelectedSamplingFrequency(int selectedSamplingFrequency) {
        this.selectedSamplingFrequency = selectedSamplingFrequency;
    }

    public UserApplication getInstance(){
        return singleton;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
