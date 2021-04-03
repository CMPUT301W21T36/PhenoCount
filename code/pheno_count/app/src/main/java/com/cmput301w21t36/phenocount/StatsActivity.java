package com.cmput301w21t36.phenocount;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;
import java.util.ArrayList;

public class StatsActivity extends AppCompatActivity implements Serializable {
    Experiment exp;
    private ExpManager expManager;
    double mean = 0.0;
    double median = 0.0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        //initializing attributes
        exp = (Experiment) getIntent().getSerializableExtra("experiment");//defining the Experiment object
        expManager = new ExpManager();

        //setting views
        TextView meanView = findViewById(R.id.mean);
        mean = expManager.getMean(exp.getTrials(),exp.getExpType());
        meanView.setText(String.format("Mean: %.2f",mean));

        TextView medianView = findViewById(R.id.median);
        median = expManager.getMedian(exp.getTrials(),exp.getExpType());
        medianView.setText(String.format("Median: %.2f",median));

        TextView quartile1 = findViewById(R.id.q1);
        quartile1.setText(String.format("1st Quartile: %.2f",expManager.getQ1()));

        TextView quartile3 = findViewById(R.id.q3);
        quartile3.setText(String.format("3rd Quartile: %.2f",expManager.getQ3()));

        TextView IQR = findViewById(R.id.iqr);
        IQR.setText(String.format("IQR: %.2f",expManager.getQ3()-expManager.getQ1()));

        TextView stdev = findViewById(R.id.sd);
        stdev.setText(String.format("Standard Deviation: %.2f",expManager.getSd()));

    }

}
