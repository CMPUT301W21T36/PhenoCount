package com.cmput301w21t36.phenocount;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;
import java.util.ArrayList;

public class StatsActivity extends AppCompatActivity implements Serializable {
    Experiment exp;
    private Statistic statManager;
    private ArrayList<Trial> acceptedTrials;
    double mean = 0.0;
    double median = 0.0;
    double sd = 0.0;
    double q1 = 0.0;
    double q3 = 0.0;
    double iqr = 0.0;
    Menu expMenu;
    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_PhenoCount);
        setContentView(R.layout.activity_stats);

        //initializing attributes
        exp = (Experiment) getIntent().getSerializableExtra("experiment");//defining the Experiment object
        statManager = new Statistic();
        acceptedTrials = new ArrayList<>();

        //Only calculating stats of accepted trials
        for(Trial trial: exp.getTrials()){
            if (trial.getStatus()){
                acceptedTrials.add(trial);
            }
        }

        //setting views
        TextView meanView = findViewById(R.id.mean);
        TextView medianView = findViewById(R.id.median);
        TextView quartile1 = findViewById(R.id.q1);
        TextView quartile3 = findViewById(R.id.q3);
        TextView IQR = findViewById(R.id.iqr);
        TextView stdev = findViewById(R.id.sd);

        //if there are accepted trials
        if (!acceptedTrials.isEmpty()) {
            mean = statManager.getMean(acceptedTrials, exp.getExpType());
            median = statManager.getMedian(acceptedTrials, exp.getExpType());
            q1 = statManager.getQ1();
            q3 = statManager.getQ3();
            iqr = q3-q1;
            sd = statManager.getSd();
        }

        //setting values of textviews
        meanView.setText(String.format("Mean: %.2f", mean));
        medianView.setText(String.format("Median: %.2f", median));
        quartile1.setText(String.format("1st Quartile: %.2f",q1));
        quartile3.setText(String.format("3rd Quartile: %.2f", q3));
        IQR.setText(String.format("IQR: %.2f", iqr));
        stdev.setText(String.format("Standard Deviation: %.2f", sd));

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.general_menu, menu);
        expMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.myList) {
            Intent intent = new Intent(StatsActivity.this, MainActivity.class);
            startActivity(intent);
        }
        if (item.getItemId() == R.id.search) {
            Intent intent = new Intent(StatsActivity.this, SearchingActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }


}
