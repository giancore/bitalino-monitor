package com.example.bitalinomonitor.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bitalinomonitor.R;
import com.example.bitalinomonitor.commands.CommandResult;
import com.example.bitalinomonitor.commands.GetExamCommandResult;
import com.example.bitalinomonitor.models.ExamModel;
import com.example.bitalinomonitor.models.FrameModel;
import com.example.bitalinomonitor.models.PatientModel;
import com.example.bitalinomonitor.network.RetrofitConfig;
import com.google.android.material.button.MaterialButton;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GraphViewActivity extends AppCompatActivity {
    private ExamModel exam;
    private PatientModel patient;
    private RetrofitConfig retrofitConfig;

    @BindView(R.id.graph)
    GraphView graph;

    @BindView(R.id.exam_info)
    TextView examInfo;

    @BindView(R.id.button_filter)
    MaterialButton btnFilter;

    @BindView(R.id.button_medicalRecord)
    MaterialButton btnMedicalRecord;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        ButterKnife.bind(this);
        retrofitConfig = new RetrofitConfig();

        Intent intent = getIntent();
        UUID idexam = (UUID) intent.getSerializableExtra("IDEXAM");
        getExam(idexam);

        Intent intent2 = getIntent();
        patient = (PatientModel) intent2.getSerializableExtra("PATIENT");

        btnFilter.setOnClickListener(view -> getFilterExamResult());
        btnMedicalRecord.setOnClickListener(view -> goToMedicalRecords());
    }

    private void getExam(UUID idExam){
        Call<GetExamCommandResult> call = retrofitConfig.getPatientService().getExam(idExam);
        call.enqueue(new Callback<GetExamCommandResult>()
        {
            @Override
            public void onResponse(Call<GetExamCommandResult> call, Response<GetExamCommandResult> response) {
                exam = response.body().data;

                if (exam != null) {
                    String duration = formatDateLabel(exam.getDuration());
                    String labelInfo = String.format("%s - %s - %s - %s Hz", exam.getName(), patient.getName(), duration , exam.getFrequency());
                    examInfo.setText(labelInfo);
                    buildGraph();

                    // não é ecg
                    if (exam.getChannel() != 1) {
                        btnMedicalRecord.setVisibility(View.INVISIBLE);
                    }
                }
            }
            @Override
            public void onFailure(Call<GetExamCommandResult> call, Throwable t) {
                Toast.makeText(GraphViewActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getFilterExamResult(){
        Call<CommandResult> call = retrofitConfig.getPatientService().getFilterExamResult(exam.getId());
        call.enqueue(new Callback<CommandResult>()
        {
            @Override
            public void onResponse(Call<CommandResult> call, Response<CommandResult> response) {
                boolean isSuccess = response.body().success;

                if(isSuccess) {
                    ArrayList<Double> filteredFrames = (ArrayList<Double>)response.body().data;

                    double timeCounter = 0;
                    double xValue = 0;
                    int samplingFrequency = exam.getFrequency();

                    ArrayList<DataPoint> dataPoints = new ArrayList<>();

                    if (filteredFrames != null) {
                        for (double frame : filteredFrames) {
                            timeCounter++;
                            xValue = timeCounter / samplingFrequency * 1000;
                            dataPoints.add(new DataPoint(xValue, frame));
                        }

                        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPoints.toArray(new DataPoint[dataPoints.size()]));
                        series.setColor(Color.MAGENTA);

                        graph.addSeries(series);
                    }
                } else {
                    String message = response.body().message;
                    Toast.makeText(GraphViewActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<CommandResult> call, Throwable t) {
                Toast.makeText(GraphViewActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public static String formatDateLabel(long value) {
        //double value = (double)longValue;

        return String.format("%02d:%02d:%02d",(int) ((value / (1000*60*60)) % 24), (int) ((value / (1000*60)) % 60), (int) (value / 1000) % 60);
    }

    private void buildGraph(){
        double timeCounter = 0;
        double xValue = 0;
        int samplingFrequency = exam.getFrequency();
        ArrayList<DataPoint> dataPoints = new ArrayList<>();

        for (FrameModel frame : exam.getFrames()) {

            timeCounter++;
            xValue = timeCounter / samplingFrequency * 1000;
            double analog = frame.getAnalog(exam.getChannel());
            dataPoints.add(new DataPoint(xValue, analog));
        }

        graph.removeAllSeries();

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPoints.toArray(new DataPoint[dataPoints.size()]));

        if(exam.getChannel() == 0) {
            series.setColor(Color.BLUE);
        }
        else if (exam.getChannel() == 1) {
            series.setColor(Color.CYAN);
        }
        else if (exam.getChannel() == 2) {
            series.setColor(Color.YELLOW);
        }
        else if (exam.getChannel() == 3) {
            series.setColor(Color.GREEN);
        }

        graph.addSeries(series);
        graphConfiguration();
    }

    private void graphConfiguration() {
        //graph.getViewport().setYAxisBoundsManual(true);

        if(exam.getChannel() == 0) { //EMG
            graph.getViewport().setMinY(-1.65);
            graph.getViewport().setMaxY(1.65);
        }
        else if (exam.getChannel() == 1) { //ECG
            graph.getViewport().setMinY(1.5);
            graph.getViewport().setMaxY(-1.5);
        }
        else if (exam.getChannel() == 2) { //EDA
            graph.getViewport().setMinY(0);
            graph.getViewport().setMaxY(25);
        }
        else if (exam.getChannel() == 3) { //EEG
            graph.getViewport().setYAxisBoundsManual(true);

            graph.getViewport().setMinY(-1);
            graph.getViewport().setMaxY(1);

            graph.getViewport().setScrollableY(true);
        }

        // enable scaling
        //graph.getViewport().setMaxX(2000);
        graph.getViewport().setScrollable(true);
        graph.getViewport().setScalable(true);
        graph.getViewport().setScalableY(true);

        graph.getGridLabelRenderer().setHighlightZeroLines(false);
        graph.getGridLabelRenderer().setHorizontalAxisTitle("Milissegundos");
        graph.getGridLabelRenderer().setHorizontalAxisTitleColor(Color.BLACK);

        if(exam.getChannel() == 0 || exam.getChannel() == 1) {
            graph.getGridLabelRenderer().setVerticalAxisTitle("mV");
        }
        else if (exam.getChannel() == 2) {
            graph.getGridLabelRenderer().setVerticalAxisTitle("us");
        }
        else if (exam.getChannel() == 3) {
            graph.getGridLabelRenderer().setVerticalAxisTitle("uV");
        }

        graph.getGridLabelRenderer().setVerticalAxisTitleColor(Color.BLACK);
        graph.getGridLabelRenderer().setVerticalLabelsColor(Color.BLACK);
        graph.getGridLabelRenderer().setHorizontalLabelsColor(Color.BLACK);

/*        graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @SuppressLint("DefaultLocale")
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    // show normal x values
                    // super.formatLabel(value, isValueX);
                    if (value < 0.000){
                        return "00:00:00";
                    }

                    return GraphViewActivity.formatDateLabel((long)value);
                } else {
                    // show currency for y values
                    return super.formatLabel(value, isValueX);
                }
            }
        });*/
    }

    private void goToMedicalRecords(){
        Intent intent = new Intent(GraphViewActivity.this, MedicalRecordsActivity.class);
        intent.putExtra("IDEXAM", exam.getId());
        startActivity(intent);
    }
}
