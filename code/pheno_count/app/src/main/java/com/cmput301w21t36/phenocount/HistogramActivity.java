package com.cmput301w21t36.phenocount;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;

public class HistogramActivity extends AppCompatActivity {
    BarChart barchart;
    Experiment experiment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_histogram);

        //get the experiment from intent
        experiment = (Experiment) getIntent().getSerializableExtra("trials");

        barchart = (BarChart) findViewById(R.id.bargraph);

        ArrayList<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(0, 20));
        BarDataSet barDataSet = new BarDataSet(barEntries, "Heads");
        barDataSet.setColor(Color.GREEN);

        ArrayList<BarEntry> fail = new ArrayList<>();
        fail.add(new BarEntry(1, 30));
        BarDataSet failset = new BarDataSet(fail, "Tails");
        failset.setColor(Color.RED);


        Description description = new Description();
        description.setText("Coin Flip Histogram");
        description.setTextColor(Color.BLACK);
        description.setTextSize(10);
        barchart.setDescription(description);

        XAxis xAxis;
        xAxis = barchart.getXAxis();
        xAxis.setEnabled(false);

        barchart.animateXY(5000, 5000);

        BarData data = new BarData();
        data.addDataSet(barDataSet);
        data.addDataSet(failset);
        barchart.setData(data);
        barchart.setTouchEnabled(true);
        barchart.setDragEnabled(true);
    }
}