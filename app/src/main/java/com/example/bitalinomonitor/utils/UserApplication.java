package com.example.bitalinomonitor.utils;

import android.app.Application;
import android.bluetooth.BluetoothDevice;
import android.content.res.Configuration;

public class UserApplication extends Application {
    private static UserApplication singleton;

    private String nome;
    private String login;
    private BluetoothDevice selectedDevice;

    public UserApplication() {
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public BluetoothDevice getSelectedDevice() {
        return selectedDevice;
    }

    public void setSelectedDevice(BluetoothDevice selectedDevice) {
        this.selectedDevice = selectedDevice;
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
