package com.cmput301w21t36.phenocount;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.cmput301w21t36.phenocount.PlotsManager;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class PlotsActivity extends AppCompatActivity {

    GraphView graphView;
    LineGraphSeries<DataPoint> series;
    PointsGraphSeries<DataPoint> pointSeries;
    Experiment exp;
    ArrayList<Trial> trials;
    ArrayList<String> dates;
    //ArrayList<Long> dates_ms;
    //SimpleDateFormat formatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_PhenoCount);
        setContentView(R.layout.activity_plots);
        graphView = (GraphView) findViewById(R.id.graph);

        graphView.getViewport().setScrollable(true);
        graphView.getViewport().setScalable(true);
        graphView.getViewport().setScalableY(true);
        graphView.getViewport().setScrollableY(true);
        graphView.getGridLabelRenderer().setHighlightZeroLines(true);

        exp = (Experiment) getIntent().getSerializableExtra("exp");//defining the Experiment object
        trials = exp.getTrials();

        PlotsManager plotsManager = new PlotsManager(exp);

        if (trials.isEmpty()) { //empty plot
            Toast.makeText(
                    PlotsActivity.this,
                    "No Data to show",
                    Toast.LENGTH_LONG).show();
            finish();
        }


        DataPoint[] dp = plotsManager.compute();
        dates = plotsManager.getDates();
        //System.out.println("DATES IN PLOTS ACTIVITY "+ dates);
        final int dp_length = dp.length;
        graphView.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter(){
            @Override
            public String formatLabel(double value, boolean isValueX) {
                //System.out.println("VALUE inside "+ (long)value);

                    if (isValueX) {
                        if (value == (int)value){
                        System.out.println("VALUE = " + value);
                        return dates.get((int) value);
                        }
                        //return "lol";
                    }
                    return super.formatLabel(value, isValueX);


            }

        });


        graphView.invalidate();
        pointSeries = new PointsGraphSeries<>(dp);
        series = new LineGraphSeries<>(dp);
        graphView.addSeries(series);
        graphView.addSeries(pointSeries);
        pointSeries.setShape(PointsGraphSeries.Shape.POINT);
        pointSeries.setColor(R.color.purple_200);
        //series.setColor(R.color.teal_700);
        series.setDrawDataPoints(true);
        series.setDataPointsRadius(0.2f);
        String yAxis = plotsManager.yTitle;
        TextView xAxisLabel = findViewById(R.id.xAxisLabel);
        xAxisLabel.setText("TIME (Days)");
        TextView yAxisLabel = findViewById(R.id.yAxisLabel);
        yAxisLabel.setText(yAxis);
        TextView expName = findViewById(R.id.expName);
        expName.setText(exp.getName());
        TextView expDet = findViewById(R.id.expDetails);
        expDet.setText(exp.getDescription());

        //graphView.getGridLabelRenderer().setVerticalAxisTitle("SUCCESSES");
        //graphView.getGridLabelRenderer().setHorizontalLabelsAngle(10);
        graphView.getGridLabelRenderer().setNumHorizontalLabels(dp_length);

        graphView.getGridLabelRenderer().setNumVerticalLabels(dp_length+2);
        graphView.getViewport().setScrollable(true);
        graphView.getViewport().setScalable(true);
        graphView.getViewport().setScalableY(true);
        graphView.getViewport().setScrollableY(true);
        graphView.getGridLabelRenderer().setHumanRounding(false,true);
        graphView.getViewport().setMinX(0);
        graphView.getViewport().setMaxX(dp_length-1);
        graphView.getViewport().setXAxisBoundsManual(true);
        graphView.getViewport().setYAxisBoundsManual(true);
        //graphView.getViewport().setMinY(dp[0].getY());
       // graphView.getViewport().setMaxY(dp[dp_length-1].getY());


        graphView.getGridLabelRenderer().setPadding(70);
        //graphView.getGridLabelRenderer().setHorizontalLabels
    }


    //Put it in a "plots manager"
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
}




