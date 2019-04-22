package com.example.bitalinomonitor.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.*;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.example.bitalinomonitor.R;
import com.example.bitalinomonitor.adapters.BluetoothItensAdapter;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import info.plux.pluxapi.BTHDeviceScan;
import info.plux.pluxapi.Constants;

import java.util.ArrayList;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;

public class ScanDevicesActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 10000;
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 2;

    private static final int REQUEST_ENABLE_BT = 1;
    private boolean isScanDevicesUpdateReceiverRegistered = false;

    private Handler mHandler;
    private BluetoothAdapter mBluetoothAdapter;
    private BTHDeviceScan bthDeviceScan;
    private BluetoothItensAdapter bluetoothItensAdapter;

    @BindView(R.id.device_list)
    RecyclerView deviceListRecyclerView;

    @BindView(R.id.swiperefresh)
    SwipeRefreshLayout pullToRefresh;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scandevices);
        ButterKnife.bind(this);

        mHandler = new Handler();

        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        // Checks if Bluetooth is supported on the device.
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Error - Bluetooth not supported", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        permissionCheck();

        bthDeviceScan = new BTHDeviceScan(this);

        setupRecycler();

        pullToRefresh.setOnRefreshListener(this);

        pullToRefresh.post(new Runnable() {
            @Override
            public void run() {
                pullToRefresh.setRefreshing(true);
                scanDevice();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(pullToRefresh.isRefreshing()) {
                            pullToRefresh.setRefreshing(false);
                        }
                    }
                }, SCAN_PERIOD);
            }
        });
    }

    private void permissionCheck(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            //Android Marshmallow and above permission check
            if(this.checkSelfPermission(ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getString(R.string.permission_check_dialog_title))
                        .setMessage(getString(R.string.permission_check_dialog_message))
                        .setPositiveButton(getString(R.string.permission_check_dialog_positive_button), null)
                        .setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @TargetApi(Build.VERSION_CODES.M)
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {
                                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
                            }
                        });
                builder.show();
            }
        }
    }

    private final BroadcastReceiver scanDevicesUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (action.equals(Constants.ACTION_MESSAGE_SCAN)) {
                BluetoothDevice bluetoothDevice = intent.getParcelableExtra(Constants.EXTRA_DEVICE_SCAN);
                bluetoothItensAdapter.updateList(bluetoothDevice);
            }
        }
    };

  private void setupRecycler(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        deviceListRecyclerView.setLayoutManager(layoutManager);

        bluetoothItensAdapter = new BluetoothItensAdapter(this, new ArrayList<>(0));
        deviceListRecyclerView.setAdapter(bluetoothItensAdapter);

        deviceListRecyclerView.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    private void scanDevice() {
        bluetoothItensAdapter.removeAllItens();

        // Stops scanning after a pre-defined scan period.
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                stopScanDevice();
            }
        }, SCAN_PERIOD);

        bthDeviceScan.doDiscovery();
    }

    private void stopScanDevice() {
        bthDeviceScan.stopScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // User chose not to enable Bluetooth.
        if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {
            finish();
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();

        registerReceiver(scanDevicesUpdateReceiver, new IntentFilter(Constants.ACTION_MESSAGE_SCAN));
        isScanDevicesUpdateReceiverRegistered = true;

        // Ensures Bluetooth is enabled on the device.  If Bluetooth is not currently enabled,
        // fire an intent to display a dialog asking the user to grant permission to enable it.
        if (!mBluetoothAdapter.isEnabled()) {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }

        pullToRefresh.post(new Runnable() {
            @Override
            public void run() {
                pullToRefresh.setRefreshing(true);
                scanDevice();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(pullToRefresh.isRefreshing()) {
                            pullToRefresh.setRefreshing(false);
                        }
                    }
                }, SCAN_PERIOD);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopScanDevice();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (bthDeviceScan != null) {
            bthDeviceScan.closeScanReceiver();
        }

        if (isScanDevicesUpdateReceiverRegistered) {
            unregisterReceiver(scanDevicesUpdateReceiver);
        }
    }

    @Override
    public void onRefresh() {
        scanDevice();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(pullToRefresh.isRefreshing()) {
                    pullToRefresh.setRefreshing(false);
                }
            }
        }, SCAN_PERIOD);
    }
}
