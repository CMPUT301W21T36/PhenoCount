/** This PlotsActivity deals with the functionality of displaying an experiment's result's
 * line plot
 * Context: Comes from trial result's page.
 */
package com.cmput301w21t36.phenocount;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.cmput301w21t36.phenocount.PlotsManager;
import com.cmput301w21t36.phenocount.Experiment;
import com.cmput301w21t36.phenocount.Trial;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_PhenoCount);
        setContentView(R.layout.activity_plots);
        graphView = (GraphView) findViewById(R.id.graph);
        //graph properties
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
        final int dp_length = dp.length;
        graphView.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter(){
            @Override
            public String formatLabel(double value, boolean isValueX) {
                    //special formatting for x axis values
                    if (isValueX) {
                        if (value == (int)value){
                        System.out.println("VALUE = " + value);
                        return dates.get((int) value);
                        }

                    }
                    return super.formatLabel(value, isValueX);


            }

        });


        //adding line and points
        pointSeries = new PointsGraphSeries<>(dp);
        series = new LineGraphSeries<>(dp);
        graphView.addSeries(series);
        graphView.addSeries(pointSeries);
        pointSeries.setShape(PointsGraphSeries.Shape.POINT);
        pointSeries.setColor(R.color.purple_200);
        series.setDrawDataPoints(true);
        series.setDataPointsRadius(0.2f);
        //more graph properties
        String yAxis = plotsManager.yTitle;
        TextView xAxisLabel = findViewById(R.id.xAxisLabel);
        xAxisLabel.setText("TIME (Days)");
        TextView yAxisLabel = findViewById(R.id.yAxisLabel);
        yAxisLabel.setText(yAxis);
        TextView expName = findViewById(R.id.expName);
        expName.setText(exp.getName());
        TextView expDet = findViewById(R.id.expDetails);
        expDet.setText(exp.getDescription());
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
        graphView.getGridLabelRenderer().setPadding(70);

    }


}




