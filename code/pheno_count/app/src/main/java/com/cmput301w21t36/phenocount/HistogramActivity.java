package com.cmput301w21t36.phenocount;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Gets the trial data for the experiment
 * Puts the trial data into bar data sets
 * Create bar graph from the data sets
 *
 * Uses MPAndroidChart to create the graphs
 * REFERENCES
 * Philipp Jahoda, 03-19-19,  Apache License, https://github.com/PhilJay/MPAndroidChart
 */
public class HistogramActivity extends AppCompatActivity {
    BarChart barchart;
    BarData barData;
    Experiment experiment;
    String type;
    XAxis xAxis;
    YAxis yLeftAxis;
    YAxis yRightAxis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_PhenoCount);
        setContentView(R.layout.activity_histogram);

        experiment = (Experiment) getIntent().getSerializableExtra("experiment");//get the experiment from intent

        barchart = (BarChart) findViewById(R.id.bargraph);

        barData = new BarData(); //data to put into the bar charts

        ArrayList<Trial> trialList = experiment.getTrials();
        ArrayList<BarEntry> dataSet1 = new ArrayList<>();
        ArrayList<BarEntry> dataSet2 = new ArrayList<>();
        ArrayList<String> datesList = new ArrayList<>();
        ArrayList<String> measurementLabels = new ArrayList<>();

        type = experiment.getExpType(); //get the type of the experiment

        xAxis = barchart.getXAxis(); //get a reference to the x-axis
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(15);
        xAxis.setLabelRotationAngle(-45);

        Description description = new Description(); //create a description
        description.setTextColor(Color.BLACK);
        description.setTextSize(10);

        barchart.animateXY(3000, 3000); //animate the bar graphs

        int i = 0;
        int j = 0;
        int barRange = 10;
        int num = 0;
        int numAdded = 0;
        float measurement = 0;
        switch (type) {
            case "Binomial":
                final String[] labels = new String[] {"Success", "Fail"};
                description.setText("Binomial Trial Histogram");

                //calculate the amount of success and failures logged in the experiment
                int successCount = 0;
                int failCount = 0;
                for (i = 0; i < trialList.size(); i++) {
                    if (((Binomial) trialList.get(i)).getResult()) {
                        successCount++;
                    } else {
                        failCount++;
                    }
                }

                String[] binomialLabel = new String[] {"Fail", "Success"};

                xAxis.setValueFormatter(new LabelFormatter(binomialLabel));
                xAxis.setGranularity(1);

                //add the data to the data sets
                dataSet2.add(new BarEntry(0, failCount));
                dataSet1.add(new BarEntry(1, successCount));

                //create the bar data sets
                BarDataSet successDataSet = new BarDataSet(dataSet1, "Success");
                successDataSet.setColor(Color.GREEN);
                successDataSet.setValueTextSize(10);
                BarDataSet failDataSet = new BarDataSet(dataSet2, "Fails");
                failDataSet.setColor(Color.RED);
                failDataSet.setValueTextSize(10);

                //add the data to the bar graphs
                barData.addDataSet(failDataSet);
                barData.addDataSet(successDataSet);
                break;
            case "Count":
                int count = 0;
                String currentDate = null;

                description.setText("Count Trial Histogram");

                //get the dates for the trials and put them in an array
                for (i = 0; i < trialList.size(); i++){
                    datesList.add(trialList.get(i).getDate());
                }
                String[] dates = new String[datesList.size()];
                dates = datesList.toArray(dates);

                //group the counts by date
                if (trialList.size() != 0) {
                    for (i = 0; i < trialList.size(); i++) {
                        currentDate = trialList.get(i).getDate();
                        for (j = 0; j < trialList.size(); j++) {
                            if (trialList.get(0).getDate().equals(currentDate)) {
                                count++;
                            }
                        }
                        dataSet1.add(new BarEntry(i, count));
                        count = 0;
                    }

                    //set the x-axis labels to the dates
                    xAxis.setValueFormatter(new LabelFormatter(dates));
                    xAxis.setGranularity(1);

                    //change y-axis label ranges
                    YAxis yRightAxis2;
                    yRightAxis2 = barchart.getAxisRight();
                    yRightAxis2.setGranularity(1);

                    YAxis yLeftAxis2;
                    yLeftAxis2 = barchart.getAxisLeft();
                    yLeftAxis2.setGranularity(1);

                    //add the data to the bar data sets
                    BarDataSet CountDataSet = new BarDataSet(dataSet1, "Counts");
                    barData.addDataSet(CountDataSet);
                }

                break;
            case "Measurement":
                // get smallest value in list
                double min = ((Measurement) trialList.get(0)).getMeasurement();
                for (i = 1; i < trialList.size(); i++) {
                    float value = ((Measurement) trialList.get(i)).getMeasurement();
                    if (value < min) {
                        min = value;
                    }
                }

                description.setText("Measurement Trial Histogram");

                processData(type, trialList, measurementLabels, dataSet1, min);

                //add the data to the bar data sets
                BarDataSet measurementDataSet = new BarDataSet(dataSet1, "Measurements");
                barData.addDataSet(measurementDataSet);
                break;
            case "NonNegativeCount":
                description.setText("Non-Negative Count Histogram");

                processData(type, trialList, measurementLabels, dataSet1, 0);

                //add the data to the bar data sets
                BarDataSet nonNegDataSet = new BarDataSet(dataSet1, "Measurements");
                barData.addDataSet(nonNegDataSet);
                break;
        }

        barchart.setDescription(description);
        barData.setValueTextSize(0);
//        barData.setBarWidth(7);
        barchart.setData(barData);
    }

    public void processData(String expType, ArrayList<Trial> trialList, ArrayList<String> measurementLabels, ArrayList<BarEntry> dataSet1, double min) {
        int barRange = 10;
        int num = 0;
        int numAdded = 0;
        float measurement = 0;
        int barMultiplier = 1;
        double barMultiplierFloat = 0;

        if (min >= 0) {
            barMultiplierFloat = Math.floor(min/barRange);
        } else {
            barMultiplierFloat = Math.ceil(min/barRange);
        }

        while (numAdded != trialList.size()) {
            for (int i = 0; i < trialList.size(); i++) {
                if (expType.equals("Measurement")) {
                    measurement = ((Measurement) trialList.get(i)).getMeasurement();
                    if ((measurement <= barRange * barMultiplierFloat) && (measurement > barRange * (barMultiplierFloat - 1))) {
                        num++;
                    }
                } else {
                    measurement = ((NonNegativeCount) trialList.get(i)).getValue();
                    if ((measurement <= barRange * barMultiplier) && (measurement > barRange * (barMultiplier - 1))) {
                        num++;
                    }
                }
            }
            if (expType.equals("Measurement")) {
                measurementLabels.add(String.valueOf(barRange * barMultiplierFloat));
                dataSet1.add(new BarEntry((float)(barRange * barMultiplierFloat), num));
                barMultiplierFloat++;
            } else {
                measurementLabels.add(String.valueOf(barRange * barMultiplier));
                dataSet1.add(new BarEntry(barRange * barMultiplier, num));
                barMultiplier++;
            }

            numAdded += num;
            num = 0;
        }

        xAxis = barchart.getXAxis();
        xAxis.setGranularity(10);

        yLeftAxis = barchart.getAxisLeft();
        yLeftAxis.setGranularity(1);

        yRightAxis = barchart.getAxisRight();
        yRightAxis.setGranularity(1);
    }


    public class LabelFormatter implements IAxisValueFormatter {
        private final String[] mLabels;

        public LabelFormatter(String[] labels) {
            mLabels = labels;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return mLabels[(int) value];
        }
    }
}