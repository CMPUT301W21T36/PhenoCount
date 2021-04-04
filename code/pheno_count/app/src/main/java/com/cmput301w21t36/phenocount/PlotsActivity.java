package com.cmput301w21t36.phenocount;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

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
    ArrayList<Long> dates;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plots);
        dates = new ArrayList<>();
        exp = (Experiment) getIntent().getSerializableExtra("exp");//defining the Experiment object
        System.out.println(exp.getName());
        trials = exp.getTrials();
        if(trials.isEmpty()){
            Toast.makeText(
                    PlotsActivity.this,
                    "No Plot to show",
                    Toast.LENGTH_LONG).show();
            finish();
        }

        for(Trial trial: trials){
            if(! dates.contains(trial.getDate()))
                dates.add(trial.getDate());
        }

        System.out.println("DATESS" + dates);
        Collections.sort(dates);

        String pattern = "dd/MM";
        DateFormat df = new SimpleDateFormat(pattern);



        try {
                pointSeries = new PointsGraphSeries<>(getDataPoint());
            } catch (ParseException e) {
                e.printStackTrace();
            }


            graphView = (GraphView) findViewById(R.id.graph);
            try {
                series = new LineGraphSeries<>(getDataPoint());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            graphView.addSeries(series);
            graphView.addSeries(pointSeries);

            graphView.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter(){
                @Override
                public String formatLabel(double value, boolean isValueX) {
                    //System.out.println("VALUE inside "+ (long)value);
                    if(isValueX){

                        Date date = new Date((long)value);
                        System.out.println("IS DATE PROPER? "+ date);
                        String str = df.format(date);
                        System.out.println("FORMATTER "+ str);
                        return str;
                        //return "$$" + super.formatLabel(value, isValueX);
                    }
                    return super.formatLabel(value, isValueX);
                }

            });

            graphView.getViewport().setScrollable(true);
            graphView.getViewport().setScalable(true);
            graphView.getViewport().setScalableY(true);
            graphView.getViewport().setScrollableY(true);
            pointSeries.setShape(PointsGraphSeries.Shape.POINT);
            pointSeries.setSize(12);
            series.setColor(R.color.purple_200);
            graphView.getGridLabelRenderer().setHorizontalAxisTitle("DATE");
            //graphView.getGridLabelRenderer().setNumHorizontalLabels(5);
    }

        private DataPoint[] getDataPoint() throws ParseException {

            ArrayList<DataPoint> dpList = new ArrayList<>();

            //Collections.sort(dates);
            int success_count = 0;
            for(Long date: dates){
                for(Trial trial: trials){
                    Binomial bTrial = (Binomial)trial;
                    if(bTrial.getDate() == date && bTrial.getResult() == true ){
                        success_count++;
                    }
                }
                System.out.println("Success "+success_count);
                dpList.add(new DataPoint(date,success_count));

            }

            //dpList.add(new DataPoint(1617648045000L, 5));
            DataPoint[] dp = new DataPoint[dpList.size()];
            dp = dpList.toArray(dp);
            //DataPoint[] dp = new DataPoint[]{new DataPoint(date_1.getTime(), 30),
                   // new DataPoint(date_2.getTime(), 35),new DataPoint(date_2.getTime(),37), new DataPoint(date_3.getTime(),32),
            //};
            return dp;
        }

    }