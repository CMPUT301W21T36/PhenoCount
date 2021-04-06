package com.cmput301w21t36.phenocount;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
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

        /**
        dates = new ArrayList<>(); //for unique date?
        for (Trial trial : trials) {
            if (!dates.contains(trial.getDate()))
                dates.add(trial.getDate());
        }

        System.out.println("UNIQUE DATES " + dates);
        //sorting according to date
        dates = sortDates(dates);

        ArrayList<DataPoint> dpList = new ArrayList<>();
        int i = 0;
        //Collections.sort(dates);
        int success_count = 0;
        for (String date : dates) {

            for (Trial trial : trials) {

                Binomial bTrial = (Binomial) trial;
                System.out.println(date + "===" + bTrial.getDate());
                if (bTrial.getDate().equals(date) && bTrial.getResult() == true && bTrial.getStatus()) {
                    success_count++;
                    //ms = bTrial.getDate();
                }
            }
            System.out.println("Success " + success_count);
            //Long date_mili = dates_ms.get(dates.indexOf(date));
            dpList.add(new DataPoint(i, success_count));
            System.out.println("SIZE OF ARRAY " + dpList.size());
            i++;

        }

        //dpList.add(new DataPoint(1617648045000L, 5));
        DataPoint[] dp = new DataPoint[dpList.size()];
        dp = dpList.toArray(dp);
        System.out.println("SIZE OF ARRAY " + dp.length);
        for (int j = 0; j < dp.length; j++) {
            System.out.println("DATA POINT " + (j + 1) + "x : " + dp[j].getX() + ", y =" + dp[j].getY());
        }
        */
        DataPoint[] dp = plotsManager.compute();
        dates = plotsManager.getDates();
        //System.out.println("DATES IN PLOTS ACTIVITY "+ dates);
        final int dp_length = dp.length;
        graphView.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter(){
            @Override
            public String formatLabel(double value, boolean isValueX) {
                //System.out.println("VALUE inside "+ (long)value);
                if (value == (int)value){
                    if (isValueX) {
                        System.out.println("VALUE = " + value);
                        return dates.get((int) value);
                        //return "lol";
                    }
                    return super.formatLabel(value, isValueX);
                }
                return "";
            }

        });



        pointSeries = new PointsGraphSeries<>(dp);
        series = new LineGraphSeries<>(dp);
        graphView.addSeries(series);
        graphView.addSeries(pointSeries);

        series.setColor(R.color.purple_200);

        TextView xAxisLabel = findViewById(R.id.xAxisLabel);
        xAxisLabel.setText("TIME (Days)");
        TextView yAxisLabel = findViewById(R.id.yAxisLabel);
        yAxisLabel.setText("SUCCESSES");
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

        /**
        //final String datesString[] = new String[];
        final int dp_length = dp.length;
        graphView.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter(){
            @Override
            public String formatLabel(double value, boolean isValueX) {
                //System.out.println("VALUE inside "+ (long)value);
                if(isValueX && value < dp_length){
                    System.out.println("VALUE = "+ value);
                    return dates.get((int)value);
                    //return "lol";
                }
                return super.formatLabel(value, isValueX);
            }

        });

        pointSeries.setShape(PointsGraphSeries.Shape.POINT);
        pointSeries.setSize(12);
        series.setColor(R.color.purple_200);
        graphView.getGridLabelRenderer().setHorizontalAxisTitle("DATE");
        graphView.getGridLabelRenderer().setHorizontalAxisTitleTextSize(40f);
        graphView.getGridLabelRenderer().setVerticalAxisTitle("SUCCESSES");
        graphView.getGridLabelRenderer().setHorizontalLabelsAngle(15);
        graphView.getGridLabelRenderer().setNumHorizontalLabels(3);




        /**
        //graphView.getViewport().setXAxisBoundsManual(true);
        dates = new ArrayList<>();
        dates_ms = new ArrayList<>();



        //graphView.getViewport().setMinX();

        //System.out.println("one of the trial "+ new Date(trials.get(trials.size()-1).getDate()));



        formatter = new SimpleDateFormat("dd MM/yyyy");
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






        String pattern = "dd MMM";
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




            pointSeries.setShape(PointsGraphSeries.Shape.POINT);
            pointSeries.setSize(12);
            series.setColor(R.color.purple_200);
            graphView.getGridLabelRenderer().setHorizontalAxisTitle("DATE");
            graphView.getGridLabelRenderer().setHorizontalAxisTitleTextSize(40f);
            graphView.getGridLabelRenderer().setVerticalAxisTitle("SUCCESSES");
            graphView.getGridLabelRenderer().setHorizontalLabelsAngle(15);
            graphView.getGridLabelRenderer().setNumHorizontalLabels(3);

            final String[] xlabels = new String[] {
                    "foo", "bar", "third", "bla", "more"
            };

            //graphView.getViewport().scrollToEnd();

            graphView.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter(){
            @Override
            public String formatLabel(double value, boolean isValueX) {
                //System.out.println("VALUE inside "+ (long)value);
                if(isValueX){
                    System.out.println("VALUE = "+ value);
                    Date date = new Date((long)value);
                    //System.out.println("IS DATE PROPER? "+ date);
                    //String str = df.format(date);
                    //System.out.println("FORMATTER "+ str);
                    return xlabels[(int)value];
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
            int i = 0;
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
                dpList.add(new DataPoint(i,success_count));
                System.out.println("SIZE OF ARRAY "+ dpList.size());
                i++;

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
         */