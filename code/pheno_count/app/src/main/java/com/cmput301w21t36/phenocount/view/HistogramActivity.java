package com.cmput301w21t36.phenocount;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashSet;

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
    private BarChart barchart;
    private BarData barData;
    private com.cmput301w21t36.phenocount.Experiment experiment;
    private String type;
    private XAxis xAxis;
    private YAxis yLeftAxis;
    private YAxis yRightAxis;
    private TextView title;
    private TextView desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_PhenoCount);
        setContentView(R.layout.activity_histogram);

        experiment = (com.cmput301w21t36.phenocount.Experiment) getIntent().getSerializableExtra("experiment");//get the experiment from intent

        barchart = findViewById(R.id.barChart);
        title = findViewById(R.id.titleText);
        desc = findViewById(R.id.descriptionText);

        String barColour = "#F8B7CD";

        //set title and description of experiment
        title.setText(experiment.getName());
        desc.setText(experiment.getDescription());

        barData = new BarData(); //data to put into the bar charts

        ArrayList<com.cmput301w21t36.phenocount.Trial> trialList = experiment.getTrials();
        ArrayList<BarEntry> dataSet1 = new ArrayList<>();
        ArrayList<BarEntry> dataSet2 = new ArrayList<>();
        ArrayList<String> datesList = new ArrayList<>();
        ArrayList<String> measurementLabels = new ArrayList<>();

        type = experiment.getExpType(); //get the type of the experiment

        xAxis = barchart.getXAxis(); //get a reference to the x-axis
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(15);
        xAxis.setLabelRotationAngle(-45);

        yLeftAxis = barchart.getAxisLeft(); //get a reference to the y-axis
        yLeftAxis.setGranularity(1);
        yRightAxis = barchart.getAxisRight(); //get a reference to the y-axis
        yRightAxis.setGranularity(1);

        barchart.getDescription().setEnabled(false); //hide the decription
        barchart.animateXY(3000, 3000); //animate the bar graphs

        int i = 0;
        int j = 0;
        switch (type) {
            case "Binomial":
                final String[] labels = new String[] {"Success", "Fail"};

                //calculate the amount of success and failures logged in the experiment
                int successCount = 0;
                int failCount = 0;
                for (i = 0; i < trialList.size(); i++) {
                    if (trialList.get(i).getStatus()) { //check if the trial has been ignored
                        if (((com.cmput301w21t36.phenocount.Binomial) trialList.get(i)).getResult()) {
                            successCount++;
                        } else {
                            failCount++;
                        }
                    }
                }

                //set the x-axis labels
                String[] binomialLabel = new String[] {"Fail", "Success"};
                xAxis.setValueFormatter(new LabelFormatter(binomialLabel));
                xAxis.setGranularity(1);

                //add the data to the data sets
                dataSet2.add(new BarEntry(0, failCount));
                dataSet1.add(new BarEntry(1, successCount));

                //create the bar data sets
                BarDataSet successDataSet = new BarDataSet(dataSet1, "Success");
                successDataSet.setColor(Color.parseColor("#A6f18e"));
                successDataSet.setValueTextSize(10);
                BarDataSet failDataSet = new BarDataSet(dataSet2, "Fails");
                failDataSet.setColor(Color.parseColor("#F58c8c"));
                failDataSet.setValueTextSize(10);

                //add the data to the bar graphs
                barData.addDataSet(failDataSet);
                barData.addDataSet(successDataSet);
                break;
            case "Count":
                int count = 0;
                int numAdded = 0;
                int barIndex = 0;
                int dateIndex = 0;
                int numIgnored = 0;
                String currentDate = null;

                //get the dates for the trials and put them in an array
                for (i = 0; i < trialList.size(); i++){
                    if (trialList.get(i).getStatus()) {
                        datesList.add(trialList.get(i).getDate());
                    }
                }

                //sort the dates in ascending order
                datesList = sortDates(datesList);

                //use a hash set to remove the duplicate dates
                LinkedHashSet<String> hashSet = new LinkedHashSet<>(datesList);
                ArrayList<String> datesWithoutDuplicates = new ArrayList<>(hashSet);

                //convert the array list into an array
                String[] dates = new String[datesWithoutDuplicates.size()];
                dates = datesWithoutDuplicates.toArray(dates);

                //group the counts by date
                if (trialList.size() != 0) {
                    ArrayList<String> checkedDates = new ArrayList<>();
                    while ((numAdded + numIgnored) != trialList.size()) {
                        while (checkedDates.contains(currentDate)) {
                            dateIndex++;
                            if (trialList.get(dateIndex).getStatus()) { //check if the trial has been ignored
                                currentDate = trialList.get(dateIndex).getDate();
                            } else {
                                dateIndex++;
                                numIgnored++;
                                currentDate = trialList.get(dateIndex).getDate();
                            }
                        }
                            currentDate = trialList.get(dateIndex).getDate();
                            checkedDates.add(currentDate);
                        for (i = 0; i < trialList.size(); i++) {
                            if (trialList.get(i).getStatus()) {
                                if (trialList.get(i).getDate().equals(currentDate)) {
                                    count++;
                                }
                            } else {
                                numIgnored++;
                            }
                        }
                        dataSet1.add(new BarEntry(barIndex, count));
                        barIndex++;
                        numAdded += count;
                        count = 0;
                    }

                    //set the x-axis labels to the dates
                    if (dates.length != 0) {
                        xAxis.setValueFormatter(new LabelFormatter(dates));
                        xAxis.setGranularity(1);
                    }

                    //add the data to the bar data sets
                    BarDataSet CountDataSet = new BarDataSet(dataSet1, "Counts");
                    CountDataSet.setColor(Color.parseColor(barColour));
                    barData.addDataSet(CountDataSet);
                }
                break;
            case "Measurement":
                // get smallest value in list
                double min = ((com.cmput301w21t36.phenocount.Measurement) trialList.get(0)).getMeasurement();
                for (i = 1; i < trialList.size(); i++) {
                    if (trialList.get(i).getStatus()) { //check if the trial has been ignored
                        float value = ((com.cmput301w21t36.phenocount.Measurement) trialList.get(i)).getMeasurement();
                        if (value < min) {
                            min = value;
                        }
                    }
                }

                //create the data sets
                processData(type, trialList, measurementLabels, dataSet1, min);

                //add the data to the bar data sets
                BarDataSet measurementDataSet = new BarDataSet(dataSet1, "Measurements");
                measurementDataSet.setColor(Color.parseColor(barColour));
                barData.addDataSet(measurementDataSet);
                break;
            case "NonNegativeCount":
                //create the data sets
                processData(type, trialList, measurementLabels, dataSet1, 0);

                //add the data to the bar data sets
                BarDataSet nonNegDataSet = new BarDataSet(dataSet1, "Measurements");
                nonNegDataSet.setColor(Color.parseColor(barColour));
                barData.addDataSet(nonNegDataSet);
                break;
        }

        //put the data sets into the histogram
        barData.setValueTextSize(0);
        barchart.setData(barData);
    }

    /**
     * gets all the data from the experiment and groups them into the appropriate ranges
     * @param expType
     * @param trialList
     * @param measurementLabels
     * @param dataSet1
     * @param min
     */
    public void processData(String expType, ArrayList<com.cmput301w21t36.phenocount.Trial> trialList, ArrayList<String> measurementLabels, ArrayList<BarEntry> dataSet1, double min) {
        int barRange = 10;
        int num = 0;
        int numAdded = 0;
        int numIgnored = 0;
        float measurement = 0;
        int barMultiplier = 1;
        double barMultiplierFloat = 0;

        barData.setBarWidth(7);

        //check which way to round the multiplier
        if (min >= 0) {
            barMultiplierFloat = Math.floor(min/barRange);
        } else {
            barMultiplierFloat = Math.ceil(min/barRange);
        }

        //group the trial data into the appropriate bar ranges
        while ((numAdded + numIgnored) != trialList.size()) {
            for (int i = 0; i < trialList.size(); i++) {
                if (trialList.get(i).getStatus()) { //check if the trial has been ignored
                    if (expType.equals("Measurement")) {
                        measurement = ((com.cmput301w21t36.phenocount.Measurement) trialList.get(i)).getMeasurement();
                        if ((measurement <= barRange * barMultiplierFloat) && (measurement > barRange * (barMultiplierFloat - 1))) {
                            num++;
                        }
                    } else {
                        measurement = ((com.cmput301w21t36.phenocount.NonNegativeCount) trialList.get(i)).getValue();
                        if ((measurement <= barRange * barMultiplier) && (measurement > barRange * (barMultiplier - 1))) {
                            num++;
                        }
                    }
                } else {
                    numIgnored++;
                }
            }

            //create the x-axis labels
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

        //set the axis label interval
        xAxis = barchart.getXAxis();
        xAxis.setGranularity(10);
    }

    /**
     * Sort the dates in ascending order
     * @param dates
     * @return
     */
    public ArrayList<String> sortDates(ArrayList<String> dates) {

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        ArrayList<Date> dateObjs = new ArrayList<Date>();
        for (String date : dates) {
            try {
                Date obj = formatter.parse(date);
                dateObjs.add(obj);
            } catch (Exception e) {
                System.out.println("FAIL");
            }

        }
        Collections.sort(dateObjs);
        ArrayList<String> sorted_str_dates = new ArrayList<>();

        for (Date date : dateObjs) {
            sorted_str_dates.add(formatter.format(date));
        }

        return (sorted_str_dates);
    }

    /**
     * used to format the labels for the axises
     */
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