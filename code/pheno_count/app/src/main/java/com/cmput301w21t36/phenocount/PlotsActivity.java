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
    ArrayList<String> dates;
    ArrayList<Long> dates_ms;
    SimpleDateFormat formatter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plots);
        graphView = (GraphView) findViewById(R.id.graph);
        dates = new ArrayList<>();
        dates_ms = new ArrayList<>();
        exp = (Experiment) getIntent().getSerializableExtra("exp");//defining the Experiment object
        //System.out.println(exp.getName());
        trials = exp.getTrials();

        //System.out.println("one of the trial "+ new Date(trials.get(trials.size()-1).getDate()));
        if(trials.isEmpty()){
            Toast.makeText(
                    PlotsActivity.this,
                    "No Plot to show",
                    Toast.LENGTH_LONG).show();
            finish();
        }

        formatter = new SimpleDateFormat("dd/MM/yyyy");
        for(Trial trial: trials){

            Date dateObj = new Date(trial.getDate());

            String strDate = formatter.format(dateObj);
            if(dates.contains(strDate) == false) {
                dates.add(strDate);
                dates_ms.add(trial.getDate()); }

        }
        System.out.println();
        System.out.println("all the unique dates that exist" + dates);
        Collections.sort(dates_ms);

        String pattern = "dd/MM";
        DateFormat df = new SimpleDateFormat(pattern);


            //graphView.getViewport().setMinY(0);


        try {
                pointSeries = new PointsGraphSeries<>(getDataPoint());
            } catch (ParseException e) {
                e.printStackTrace();
            }



            try {
                series = new LineGraphSeries<>(getDataPoint());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            graphView.addSeries(series);
            graphView.addSeries(pointSeries);



            graphView.getViewport().setScrollable(true);
            graphView.getViewport().setScalable(true);
            graphView.getViewport().setScalableY(true);
            graphView.getViewport().setScrollableY(true);
            pointSeries.setShape(PointsGraphSeries.Shape.POINT);
            pointSeries.setSize(12);
            series.setColor(R.color.purple_200);
            graphView.getGridLabelRenderer().setHorizontalAxisTitle("DATE");
            graphView.getGridLabelRenderer().setHorizontalAxisTitleTextSize(40f);
            graphView.getGridLabelRenderer().setVerticalAxisTitle("SUCCESSES");
            graphView.getGridLabelRenderer().setHorizontalLabelsAngle(15);
            //graphView.getViewport().scrollToEnd();

            graphView.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter(){
            @Override
            public String formatLabel(double value, boolean isValueX) {
                //System.out.println("VALUE inside "+ (long)value);
                if(isValueX){

                    Date date = new Date((long)value);
                    //System.out.println("IS DATE PROPER? "+ date);
                    String str = df.format(date);
                    //System.out.println("FORMATTER "+ str);
                    return str;
                    //return "$$" + super.formatLabel(value, isValueX);
                }
                return super.formatLabel(value, isValueX);
            }

        });
            //finish();
            //graphView.getGridLabelRenderer().setNumHorizontalLabels(5);
    }

        private DataPoint[] getDataPoint() throws ParseException {

            ArrayList<DataPoint> dpList = new ArrayList<>();

            //Collections.sort(dates);
            int success_count = 0;
            for(String date: dates){
                long ms = 0;
                for(Trial trial: trials){

                    Binomial bTrial = (Binomial)trial;
                    Date dateObj = new Date(bTrial.getDate());
                    String strDate = formatter.format(dateObj);
                    System.out.println("dates" + dateObj + "== " + success_count);

                    if( strDate.equals(date) && bTrial.getResult() == true ){
                        success_count++;
                        ms = bTrial.getDate();
                    }
                }
                System.out.println("Success "+success_count);
                //Long date_mili = dates_ms.get(dates.indexOf(date));
                dpList.add(new DataPoint(ms,success_count));

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