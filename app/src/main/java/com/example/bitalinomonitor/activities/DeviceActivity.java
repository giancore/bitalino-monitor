package com.example.bitalinomonitor.activities;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Chronometer;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.bitalinomonitor.R;
import com.example.bitalinomonitor.models.ExamOptionModel;
import com.example.bitalinomonitor.utils.UserApplication;

import com.google.android.material.button.MaterialButton;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import info.plux.pluxapi.Communication;
import info.plux.pluxapi.bitalino.*;
import info.plux.pluxapi.bitalino.bth.OnBITalinoDataAvailable;

import static info.plux.pluxapi.Constants.*;

public class DeviceActivity extends AppCompatActivity implements OnBITalinoDataAvailable {

    public static final int REQUEST_CODE_SCAN_DEVICES = 11;
    public static final int RESULT_CODE_SCAN_DEVICES = 1;

    public final static String FRAME = "info.plux.pluxapi.sampleapp.DeviceActivity.Frame";
    private boolean isUpdateReceiverRegistered = false;
    private boolean isBITalino2 = false;
    private int selectedChannel = 0;
    private long timeWhenStopped = 0;

    private UserApplication userApplication;
    private BluetoothDevice bluetoothDevice;
    private BITalinoCommunication bitalino;
    private List<BITalinoFrame> bitalinoFrames = new ArrayList<>();

    private Handler handler;

    @BindView(R.id.start_button)
    MaterialButton btnStart;

    @BindView(R.id.stop_button)
    MaterialButton btnStop;

    @BindView(R.id.chronometer)
    Chronometer chronometer;

    @BindView(R.id.txtStatus)
    TextView txtStatus;

    @BindView(R.id.spinner)
    Spinner spinner;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);
        ButterKnife.bind(this);

        userApplication = (UserApplication)getApplicationContext();

        List<String> examOptions = ExamOptionModel.getOptionsAsString();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.dropdown, examOptions);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();

                if(item.equals(ExamOptionModel.EMG)){
                    selectedChannel = ExamOptionModel.EMG_Channel;
                } else if(item.equals(ExamOptionModel.ECG)){
                    selectedChannel = ExamOptionModel.ECG_Channel;
                } else if(item.equals(ExamOptionModel.EDA)){
                    selectedChannel = ExamOptionModel.EDA_Channel;
                } else {
                    selectedChannel = ExamOptionModel.EEG_Channel;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnStart.setOnClickListener(view -> start());
        btnStop.setOnClickListener(view -> stop());
    }

    @Override
    protected void onResume() {
        super.onResume();

        registerReceiver(updateReceiver, makeUpdateIntentFilter());
        isUpdateReceiverRegistered = true;

        bluetoothDevice = userApplication.getSelectedDevice();

        if (bluetoothDevice == null){
            Intent goToScanDevices = new Intent(DeviceActivity.this, ScanDevicesActivity.class);
            startActivity(goToScanDevices);
        } else {
            Communication communication = Communication.getById(bluetoothDevice.getType());
            if(communication.equals(Communication.DUAL)){
                communication = Communication.BLE;
            }

            bitalino = new BITalinoCommunicationFactory().getCommunication(communication,this, this);

            handler = new Handler(getMainLooper()){
                @Override
                public void handleMessage(Message msg) {
                    Bundle bundle = msg.getData();
                    BITalinoFrame frame = bundle.getParcelable(FRAME);

                    if(frame != null){ //BITalino
                        //resultsTextView.setText(frame.toString());
                    }
                }
            };

            connect();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_SCAN_DEVICES) {
                bluetoothDevice = userApplication.getSelectedDevice();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(isUpdateReceiverRegistered) {
            unregisterReceiver(updateReceiver);
            isUpdateReceiverRegistered = false;
        }

        if(bitalino != null){
            bitalino.closeReceivers();
            try {
                bitalino.disconnect();
            } catch (BITalinoException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBITalinoDataAvailable(BITalinoFrame bitalinoFrame) {
        Message message = handler.obtainMessage();
        Bundle bundle = new Bundle();
        bundle.putParcelable(FRAME, bitalinoFrame);
        message.setData(bundle);
        handler.sendMessage(message);

        bitalinoFrames.add(bitalinoFrame);
    }

    /*
     * Local Broadcast
     */
    private final BroadcastReceiver updateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if(ACTION_STATE_CHANGED.equals(action)){
                States state = States.getStates(intent.getIntExtra(EXTRA_STATE_CHANGED, 0));
                txtStatus.setText(state.name());

                switch (state) {
                    case NO_CONNECTION:
                        txtStatus.setText("Sem Conexão");
                        break;
                    case LISTEN:
                        txtStatus.setText("Escutando");
                        break;
                    case CONNECTING:
                        txtStatus.setText("Conectando");
                        break;
                    case CONNECTED:
                        txtStatus.setText("Conectado");
                        break;
                    case ACQUISITION_TRYING:
                        break;
                    case ACQUISITION_OK:
                        txtStatus.setText("Adquirindo Dados");
                        break;
                    case ACQUISITION_STOPPING:
                        break;
                    case DISCONNECTED:
                        txtStatus.setText("Descontectado");
                        break;
                    case ENDED:
                        break;
                }
            }
            else if(ACTION_DATA_AVAILABLE.equals(action)){
                if(intent.hasExtra(EXTRA_DATA)){
                    Parcelable parcelable = intent.getParcelableExtra(EXTRA_DATA);

                    if(parcelable.getClass().equals(BITalinoFrame.class)){ //BITalino
                        BITalinoFrame frame = (BITalinoFrame) parcelable;

                        frame.getSequence();
                        frame.getAnalogArray();
                        frame.getIdentifier();
                        //resultsTextView.setText(frame.toString());
                    }
                }
            }
            else if(ACTION_COMMAND_REPLY.equals(action)){
                String identifier = intent.getStringExtra(IDENTIFIER);

                if(intent.hasExtra(EXTRA_COMMAND_REPLY) && (intent.getParcelableExtra(EXTRA_COMMAND_REPLY) != null)){
                    Parcelable parcelable = intent.getParcelableExtra(EXTRA_COMMAND_REPLY);

                    if(parcelable.getClass().equals(BITalinoState.class)){ //BITalino

                        //   resultsTextView.setText(parcelable.toString());
                    }
                    else if(parcelable.getClass().equals(BITalinoDescription.class)){ //BITalino
                        isBITalino2 = ((BITalinoDescription)parcelable).isBITalino2();

                        //  resultsTextView.setText("isBITalino2: " + isBITalino2 + "; FwVersion: " + String.valueOf(((BITalinoDescription)parcelable).getFwVersion()));

                    }
                }
            }
        }
    };

    private IntentFilter makeUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_STATE_CHANGED);
        intentFilter.addAction(ACTION_DATA_AVAILABLE);
        intentFilter.addAction(ACTION_EVENT_AVAILABLE);
        intentFilter.addAction(ACTION_DEVICE_READY);
        intentFilter.addAction(ACTION_COMMAND_REPLY);
        return intentFilter;
    }

    private void start(){
        chronometer.setBase(SystemClock.elapsedRealtime() + timeWhenStopped);
        chronometer.start();

        try {
            chronometer.start();

            //bitalino.start(new int[]{0,1,2,3,4,5}, 1);
            bitalino.start(new int[]{ selectedChannel }, 1);
        } catch (BITalinoException e) {
            e.printStackTrace();
        }
    }

    private void stop(){
        timeWhenStopped = chronometer.getBase() - SystemClock.elapsedRealtime();
        chronometer.stop();

        try {
            bitalino.stop();
            chronometer.stop();

            Intent goToGraphView = new Intent(DeviceActivity.this, GraphViewActivity.class);
            goToGraphView.putExtra("FRAMES", (ArrayList<BITalinoFrame>) bitalinoFrames);
            startActivity(goToGraphView);
            finish();

            Log.d("Dados", bitalinoFrames.toString());

        } catch (BITalinoException e) {
            e.printStackTrace();
        }
    }

    private void connect(){
        try {
            bitalino.connect(bluetoothDevice.getAddress());
        } catch (BITalinoException e) {
            e.printStackTrace();
        }
    }
}
