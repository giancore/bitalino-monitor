package com.example.bitalinomonitor.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.os.Bundle;

import com.example.bitalinomonitor.R;
import com.example.bitalinomonitor.models.ExamOptionModel;
import com.example.bitalinomonitor.utils.SensorTransferFunctions;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.UniqueLegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import info.plux.pluxapi.bitalino.BITalinoFrame;

public class GraphViewActivity extends AppCompatActivity {

    private List<BITalinoFrame> bitalinoFrames = new ArrayList<>();
    private int selectedChannel;

    @BindView(R.id.graph)
    GraphView graph;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        ButterKnife.bind(this);

        //bitalinoFrames = getIntent().getParcelableArrayListExtra("FRAMES");

        int[] a1 = {669,498,0,522,38,0};
        BITalinoFrame bitalinoFrame1 = new BITalinoFrame("1", 0, a1,null );
        bitalinoFrames.add(bitalinoFrame1);

        int[] a2 = {663,491,0,521,38,0};
        BITalinoFrame bitalinoFrame2 = new BITalinoFrame("2", 1, a2,null );
        bitalinoFrames.add(bitalinoFrame2);

        int[] a3 = {654,488,0,523,38,0};
        BITalinoFrame bitalinoFrame3 = new BITalinoFrame("3", 2, a3,null );
        bitalinoFrames.add(bitalinoFrame3);

        int[] a4 = {723,481,0,519,38,0};
        BITalinoFrame bitalinoFrame4 = new BITalinoFrame("4", 3, a4,null );
        bitalinoFrames.add(bitalinoFrame4);

        int[] a5 = {895,469,0,518,38,0};
        BITalinoFrame bitalinoFrame5 = new BITalinoFrame("5", 4, a5,null );
        bitalinoFrames.add(bitalinoFrame5);

        int[] a6 = {992,458,0,516,38,0};
        BITalinoFrame bitalinoFrame6 = new BITalinoFrame("6", 5, a6,null );
        bitalinoFrames.add(bitalinoFrame6);

        int[] a7 = {528,467,0,515,38,0};
        BITalinoFrame bitalinoFrame7 = new BITalinoFrame("7", 6, a7,null );
        bitalinoFrames.add(bitalinoFrame7);

        int[] a8 = {248,477,0,510,38,0};
        BITalinoFrame bitalinoFrame8 = new BITalinoFrame("8", 7, a8,null );
        bitalinoFrames.add(bitalinoFrame8);

        int[] a9 = {246,482,0,502,38,0};
        BITalinoFrame bitalinoFrame9 = new BITalinoFrame("9", 8, a9,null );
        bitalinoFrames.add(bitalinoFrame9);

        int[] a10 = {10,496,0,514,38,0};
        BITalinoFrame bitalinoFrame10 = new BITalinoFrame("10", 9, a10,null );
        bitalinoFrames.add(bitalinoFrame10);

        int[] a11 = {94,506,0,530,38,0};
        BITalinoFrame bitalinoFrame11 = new BITalinoFrame("11", 10, a11,null );
        bitalinoFrames.add(bitalinoFrame11);

        int[] a12 = {208,508,0,544,38,0};
        BITalinoFrame bitalinoFrame12 = new BITalinoFrame("12", 11, a12,null );
        bitalinoFrames.add(bitalinoFrame12);

        int[] a13 = {357,508,0,553,38,0};
        BITalinoFrame bitalinoFrame13 = new BITalinoFrame("13", 12, a13,null );
        bitalinoFrames.add(bitalinoFrame13);

        int[] a14 = {341,507,0,560,38,0};
        BITalinoFrame bitalinoFrame14 = new BITalinoFrame("14", 13, a14,null );
        bitalinoFrames.add(bitalinoFrame14);

        int[] a15 = {671,498,0,551,38,0};
        BITalinoFrame bitalinoFrame15 = new BITalinoFrame("15", 14, a15,null );
        bitalinoFrames.add(bitalinoFrame15);

        int[] a16 = {713,498,0,542,38,0};
        BITalinoFrame bitalinoFrame16 = new BITalinoFrame("0", 15, a16,null );
        bitalinoFrames.add(bitalinoFrame16);

        int[] a17 = {680,499,0,533,38,0};
        BITalinoFrame bitalinoFrame17 = new BITalinoFrame("1", 0, a17,null );
        bitalinoFrames.add(bitalinoFrame17);

        int[] a18 = {660,496,0,525,38,0};
        BITalinoFrame bitalinoFrame18 = new BITalinoFrame("2", 1, a18,null );
        bitalinoFrames.add(bitalinoFrame18);

        int[] a19 = {651,496,0,523,38,0};
        BITalinoFrame bitalinoFrame19 = new BITalinoFrame("3", 2, a19,null );
        bitalinoFrames.add(bitalinoFrame19);

        int[] a20 = {643,495,0,519,38,0};
        BITalinoFrame bitalinoFrame20 = new BITalinoFrame("4", 3, a20,null );
        bitalinoFrames.add(bitalinoFrame20);

        int[] a21 = {860,485,0,532,38,0};
        BITalinoFrame bitalinoFrame21 = new BITalinoFrame("5", 4, a21,null );
        bitalinoFrames.add(bitalinoFrame21);

        int[] a22 = {952,470,0,517,38,0};
        BITalinoFrame bitalinoFrame22 = new BITalinoFrame("6", 5, a22,null );
        bitalinoFrames.add(bitalinoFrame22);

        int[] a23 = {908,457,0,515,38,0};
        BITalinoFrame bitalinoFrame23 = new BITalinoFrame("7", 6, a23,null );
        bitalinoFrames.add(bitalinoFrame23);

        int[] a24 = {335,471,0,510,38,0};
        BITalinoFrame bitalinoFrame24 = new BITalinoFrame("8", 7, a24,null );
        bitalinoFrames.add(bitalinoFrame24);

        int[] a25 = {260,485,0,507,38,0};
        BITalinoFrame bitalinoFrame25 = new BITalinoFrame("9", 8, a25,null );
        bitalinoFrames.add(bitalinoFrame25);

        if (bitalinoFrames != null) {
            construirGrafico();
        }
    }

    private void construirGrafico(){
        double timeCounter = 0;
        double xValue = 0;
        double yValue = 0;

        ArrayList<DataPoint> dataPoints = new ArrayList<>();
        double samplingCounter = 0;
        double samplingFrames = 100 / 50;

        for (BITalinoFrame frame : bitalinoFrames) {

            if (samplingCounter++ >= samplingFrames) {

                timeCounter++;

                xValue = timeCounter / 100 * 1000;

                if(selectedChannel == ExamOptionModel.EMG_Channel){
                    yValue = SensorTransferFunctions.calculateElectromyographyValue(frame.getAnalog(selectedChannel));
                } else if(selectedChannel == ExamOptionModel.ECG_Channel){
                    yValue = SensorTransferFunctions.calculateElectrocardiographyValue(frame.getAnalog(selectedChannel));
                } else if(selectedChannel == ExamOptionModel.EDA_Channel){
                    yValue = SensorTransferFunctions.calculateElectrodermalActivityValue(frame.getAnalog(selectedChannel));
                } else {
                    yValue = SensorTransferFunctions.calculateElectroencephalographyValue(frame.getAnalog(selectedChannel));
                }

                dataPoints.add(new DataPoint(xValue, yValue));

                samplingCounter -= samplingFrames;
            }
        }

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPoints.toArray(new DataPoint[dataPoints.size()]));

        series.setColor(Color.rgb(115,211,230));

        graph.addSeries(series);

        //graph.getViewport().setYAxisBoundsManual(true);
        //graph.getViewport().setMinY(-150);
        //graph.getViewport().setMaxY(150);
        //graph.getViewport().setXAxisBoundsManual(true);
        //graph.getViewport().setMinX(4);
        //graph.getViewport().setMaxX(80);

        // enable scaling
		graph.getViewport().setMaxY(2);
        graph.getViewport().setMaxX(2000);
        graph.getViewport().setScrollable(true);
        graph.getViewport().setScalableY(true);
        //graph.getViewport().setScalableY(true);

        graph.getGridLabelRenderer().setHighlightZeroLines(false);
        graph.getGridLabelRenderer().setHorizontalAxisTitle("Minutes");
        graph.getGridLabelRenderer().setHorizontalAxisTitleColor(Color.BLACK);
        graph.getGridLabelRenderer().setVerticalAxisTitle("mV");
        graph.getGridLabelRenderer().setVerticalAxisTitleColor(Color.BLACK);
        graph.setLegendRenderer(new UniqueLegendRenderer(graph));
        graph.getLegendRenderer().setVisible(true);

        graph.getGridLabelRenderer().setVerticalLabelsColor(Color.BLACK);
        graph.getGridLabelRenderer().setHorizontalLabelsColor(Color.BLACK);

        graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @SuppressLint("DefaultLocale")
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    // show normal x values
                    //super.formatLabel(value, isValueX);
                    if (value < 0.000){
                        return "00:00:00";
                    }
                    return String.format("%02d:%02d:%02d",(int) ((value / (1000*60*60)) % 24), (int) ((value / (1000*60)) % 60), (int) (value / 1000) % 60);

                } else {
                    // show currency for y values
                    return super.formatLabel(value, isValueX);
                }
            }
        });
    }
}
