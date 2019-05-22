package com.example.bitalinomonitor.activities;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bitalinomonitor.R;
import com.example.bitalinomonitor.commands.CommandResult;
import com.example.bitalinomonitor.models.BitalinoConfiguration;
import com.example.bitalinomonitor.models.ExamModel;
import com.example.bitalinomonitor.models.PatientModel;
import com.example.bitalinomonitor.network.RetrofitConfig;
import com.example.bitalinomonitor.utils.PausableChronometer;
import com.example.bitalinomonitor.utils.UserApplication;
import com.google.android.material.button.MaterialButton;
import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import info.plux.pluxapi.Communication;
import info.plux.pluxapi.bitalino.BITalinoCommunication;
import info.plux.pluxapi.bitalino.BITalinoCommunicationFactory;
import info.plux.pluxapi.bitalino.BITalinoDescription;
import info.plux.pluxapi.bitalino.BITalinoException;
import info.plux.pluxapi.bitalino.BITalinoFrame;
import info.plux.pluxapi.bitalino.BITalinoState;
import info.plux.pluxapi.bitalino.bth.OnBITalinoDataAvailable;
import pl.bclogic.pulsator4droid.library.PulsatorLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static info.plux.pluxapi.Constants.ACTION_COMMAND_REPLY;
import static info.plux.pluxapi.Constants.ACTION_DATA_AVAILABLE;
import static info.plux.pluxapi.Constants.ACTION_DEVICE_READY;
import static info.plux.pluxapi.Constants.ACTION_EVENT_AVAILABLE;
import static info.plux.pluxapi.Constants.ACTION_MESSAGE_SCAN;
import static info.plux.pluxapi.Constants.ACTION_STATE_CHANGED;
import static info.plux.pluxapi.Constants.EXTRA_COMMAND_REPLY;
import static info.plux.pluxapi.Constants.EXTRA_DATA;
import static info.plux.pluxapi.Constants.EXTRA_DEVICE_SCAN;
import static info.plux.pluxapi.Constants.EXTRA_STATE_CHANGED;
import static info.plux.pluxapi.Constants.IDENTIFIER;
import static info.plux.pluxapi.Constants.States;

public class DeviceActivity extends AppCompatActivity implements OnBITalinoDataAvailable {

    public final static String FRAME = "info.plux.pluxapi.sampleapp.DeviceActivity.Frame";
    private boolean isUpdateReceiverRegistered = false;
    private boolean isBITalino2 = false;
    private boolean isAcquisition = false;
    private PatientModel patient;
    private ExamModel exam = new ExamModel();

    private UserApplication userApplication;
    private BluetoothDevice bluetoothDevice;
    private BitalinoConfiguration bitalinoConfiguration;
    private BITalinoCommunication bitalino;
    private List<BITalinoFrame> bitalinoFrames = new ArrayList<>();
    private RetrofitConfig retrofitConfig;
    private ProgressDialog dialog;
    private Handler handler;

    @BindView(R.id.button_process_exam)
    MaterialButton btnProcessExam;

    @BindView(R.id.chronometer)
    PausableChronometer chronometer;

    @BindView(R.id.textview_status)
    TextView txtStatus;

    @BindView(R.id.textview_device)
    TextView txtDevice;

    @BindView(R.id.spinner_examlist)
    Spinner spinnerExamList;

    @BindView(R.id.spinner_frequency)
    Spinner spinnerFrequency;

    @BindView(R.id.pulsator)
    PulsatorLayout pulsator;

    @BindView(R.id.button_play)
    Button btnPlay;

    @BindView(R.id.process_exam_progress)
    ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);
        ButterKnife.bind(this);

        retrofitConfig = new RetrofitConfig();
        bitalinoConfiguration = new BitalinoConfiguration();
        userApplication = (UserApplication)getApplicationContext();
        progressBar.setVisibility(View.INVISIBLE);

        loadSpinners();

        btnPlay.setOnClickListener(view -> play());
        btnProcessExam.setOnClickListener(view -> processExam());
    }

    private void loadSpinners() {
        List<String> examOptions = bitalinoConfiguration.getExamsAsArray();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.dropdown, examOptions);
        spinnerExamList.setPrompt(getString(R.string.exam_prompt));
        spinnerExamList.setAdapter(adapter);
        spinnerExamList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                int channel = bitalinoConfiguration.getChannelByExamName(item);
                exam.setChannel(channel);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        List<String> frequencyOptions = bitalinoConfiguration.getFrequenciesAsArray();
        ArrayAdapter<String> adapterFrequency = new ArrayAdapter<>(this, R.layout.dropdown, frequencyOptions);
        spinnerFrequency.setAdapter(adapterFrequency);
        spinnerFrequency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                int frequency = bitalinoConfiguration.getFrequencyByName(item);
                exam.setFrequency(frequency);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = getIntent();
        patient = (PatientModel) intent.getSerializableExtra("PATIENT");

        registerReceiver(updateReceiver, makeUpdateIntentFilter());
        isUpdateReceiverRegistered = true;

        bluetoothDevice = userApplication.getSelectedDevice();

        if (bluetoothDevice == null){
            Intent goToScanDevices = new Intent(DeviceActivity.this, ScanDevicesActivity.class);
            intent.putExtra("PATIENT", patient);
            startActivity(goToScanDevices);
        } else {
            txtDevice.setText(bluetoothDevice.getName());

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
                        bitalinoFrames.add(frame);
                    }
                }
            };

            connect();
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
    }

    /*
     * Local Broadcast
     */
    private final BroadcastReceiver updateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (ACTION_STATE_CHANGED.equals(action)) {
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
                        btnPlay.setEnabled(true);
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

            } else if (ACTION_DATA_AVAILABLE.equals(action)) {
                BITalinoFrame frame = intent.getParcelableExtra(EXTRA_DATA);
                Log.d("BITalinoFrame", frame.toString());

            } else if (ACTION_COMMAND_REPLY.equals(action)) {
                String identifier = intent.getStringExtra(IDENTIFIER);
                Parcelable parcelable = intent.getParcelableExtra(EXTRA_COMMAND_REPLY);
                if(parcelable.getClass().equals(BITalinoState.class)){
                    Log.d("BITalinoState", parcelable.toString());
                } else if(parcelable.getClass().equals(BITalinoDescription.class)){
                    Log.d("BITalinoDescription", "isBITalino2: " +
                            ((BITalinoDescription)parcelable).isBITalino2() + "; FwVersion:" + String.valueOf(((BITalinoDescription)parcelable).getFwVersion()));
                }
            } else if (ACTION_MESSAGE_SCAN.equals(action)){
                BluetoothDevice device = intent.getParcelableExtra(EXTRA_DEVICE_SCAN);
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

    private void play(){
        isAcquisition = !isAcquisition;

        if (isAcquisition) {
            chronometer.start();
            pulsator.start();
            btnPlay.setText("Parar");
            btnProcessExam.setVisibility(View.INVISIBLE);
            spinnerExamList.setEnabled(false);
            spinnerFrequency.setEnabled(false);

            try {
                chronometer.start();

                //bitalino.start(new int[]{0,1,2,3,4,5}, 1);
                bitalino.start(new int[]{ exam.getChannel() }, exam.getFrequency());
            } catch (BITalinoException e) {
                e.printStackTrace();
            }

        } else {
            chronometer.stop();
            pulsator.stop();
            btnPlay.setText("Iniciar");
            btnProcessExam.setVisibility(View.VISIBLE);

            try {
                bitalino.stop();
                chronometer.stop();

                Log.d("Dados", bitalinoFrames.toString());

            } catch (BITalinoException e) {
                e.printStackTrace();
            }
        }
    }

    private void processExam(){
        btnPlay.setEnabled(false);

        exam.setDate(new Date());
        exam.setBitalinoFrames(bitalinoFrames);
        exam.setIdPatient(patient.getId());
        exam.setDuration(chronometer.getCurrentTime());

        progressBar.setVisibility(View.VISIBLE);

        Call<CommandResult> call = retrofitConfig.getPatientService().saveExam(exam);
        call.enqueue(new Callback<CommandResult>()
        {
            @Override
            public void onResponse(Call<CommandResult> call, Response<CommandResult> response) {
                boolean isSuccess = response.body().success;

                progressBar.setVisibility(View.INVISIBLE);

                if(isSuccess) {
                    LinkedTreeMap<String, String> data = (LinkedTreeMap<String, String>) response.body().data;
                    //List<LinkedTreeMap<?, ?>> parsedResults = (List<LinkedTreeMap<?, ?>>) response.body().data;
                    UUID idExam =  UUID.fromString(data.get("id"));

                    String message = response.body().message;
                    Toast.makeText(DeviceActivity.this, message, Toast.LENGTH_SHORT).show();

                    Intent goToGraphView = new Intent(DeviceActivity.this, GraphViewActivity.class);
                    goToGraphView.putExtra("IDEXAM", idExam);
                    goToGraphView.putExtra("PATIENT", patient);
                    startActivity(goToGraphView);
                    finish();
                } else {
                    String message = response.body().message;
                    Toast.makeText(DeviceActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommandResult> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);

                Toast.makeText(DeviceActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void connect(){
        try {
            bitalino.connect(bluetoothDevice.getAddress());
        } catch (BITalinoException e) {
            e.printStackTrace();
        }
    }
}
