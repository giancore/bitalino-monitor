package com.example.bitalinomonitor.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bitalinomonitor.R;
import com.example.bitalinomonitor.models.BitalinoConfiguration;
import com.example.bitalinomonitor.models.ExamModel;
import com.example.bitalinomonitor.models.FrameModel;
import com.example.bitalinomonitor.models.PatientModel;
import com.example.bitalinomonitor.network.RetrofitConfig;
import com.example.bitalinomonitor.utils.SensorTransferFunctions;
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

    @BindView(R.id.get_graph_progress)
    ProgressBar progressBar;

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
    }

    private void getExam(UUID idExam){
        progressBar.setVisibility(View.VISIBLE);

        Call<ExamModel> call = retrofitConfig.getPatientService().getExam(idExam);
        call.enqueue(new Callback<ExamModel>()
        {
            @Override
            public void onResponse(Call<ExamModel> call, Response<ExamModel> response) {
                exam = response.body();

                if (exam != null) {
                    String duration = formatDateLabel(exam.getDuration());
                    String labelInfo = String.format("%s - %s - %s - %s Hz", exam.getName(), patient.getName(), duration , exam.getFrequency());
                    examInfo.setText(labelInfo);
                    construirGrafico();
                }

                progressBar.setVisibility(View.INVISIBLE);
            }
            @Override
            public void onFailure(Call<ExamModel> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(GraphViewActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public static String formatDateLabel(long value) {
        return String.format("%02d:%02d:%02d",(int) ((value / (1000*60*60)) % 24), (int) ((value / (1000*60)) % 60), (int) (value / 1000) % 60);
    }

    private void construirGrafico(){
        double timeCounter = 0;
        double xValue = 0;
        double yValue = 0;
        int samplingFrequency = exam.getFrequency();
        int visualizationFrequency = 100;

        ArrayList<DataPoint> dataPoints = new ArrayList<>();
        double samplingCounter = 0;
        double samplingFrames = visualizationFrequency / samplingFrequency;

        for (FrameModel frame : exam.getFrames()) {

            if (samplingCounter++ >= samplingFrames) {
                timeCounter++;

                xValue = timeCounter / samplingFrequency * 1000;

                int analog = frame.getAnalog(exam.getChannel());

                if(exam.getChannel() == BitalinoConfiguration.EMG_Channel){
                    yValue = SensorTransferFunctions.calculateElectromyographyValue(analog);
                } else if(exam.getChannel() == BitalinoConfiguration.ECG_Channel){
                    yValue = SensorTransferFunctions.calculateElectrocardiographyValue(analog);
                } else if(exam.getChannel() == BitalinoConfiguration.EDA_Channel){
                    yValue = SensorTransferFunctions.calculateElectrodermalActivityValue(analog);
                } else {
                    yValue = SensorTransferFunctions.calculateElectroencephalographyValue(analog);
                }

                dataPoints.add(new DataPoint(xValue, yValue));

                samplingCounter -= samplingFrames;
            }
        }

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPoints.toArray(new DataPoint[dataPoints.size()]));

        if(exam.getChannel() == 0) {
            series.setColor(Color.BLUE);
        }
        else if (exam.getChannel() == 1) {
            series.setColor(Color.RED);
        }
        else if (exam.getChannel() == 2) {
            series.setColor(Color.MAGENTA);
        }
        else if (exam.getChannel() == 3) {
            series.setColor(Color.GREEN);
        }

        graph.addSeries(series);

        //graph.getViewport().setYAxisBoundsManual(true);
        //graph.getViewport().setMinY(-150);
        //graph.getViewport().setMaxY(150);
        //graph.getViewport().setXAxisBoundsManual(true);
        //graph.getViewport().setMinX(4);
        //graph.getViewport().setMaxX(80);

        // enable scaling
        graph.getViewport().setMaxY(5);
        graph.getViewport().setMaxX(2000);
        //graph.getViewport().setScrollable(true);
        graph.getViewport().setScalableY(true);

        graph.getGridLabelRenderer().setHighlightZeroLines(false);

        graph.getGridLabelRenderer().setHorizontalAxisTitle("Minutes");
        graph.getGridLabelRenderer().setHorizontalAxisTitleColor(Color.BLACK);
        graph.getGridLabelRenderer().setVerticalAxisTitle("mV");
        graph.getGridLabelRenderer().setVerticalAxisTitleColor(Color.BLACK);
        //graph.setLegendRenderer(new UniqueLegendRenderer(graph));
        //graph.getLegendRenderer().setVisible(true);

        graph.getGridLabelRenderer().setVerticalLabelsColor(Color.BLACK);
        graph.getGridLabelRenderer().setHorizontalLabelsColor(Color.BLACK);

        graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
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
        });
    }
}
