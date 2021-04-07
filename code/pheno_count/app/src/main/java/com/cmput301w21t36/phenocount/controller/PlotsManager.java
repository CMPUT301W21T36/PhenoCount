package com.cmput301w21t36.phenocount;

import com.jjoe64.graphview.series.DataPoint;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class PlotsManager implements Serializable {

    Experiment exp;
    ArrayList<Trial> trials;
    ArrayList<String> dates;

    public PlotsManager(Experiment exp) {
        this.exp = exp;//defining the Experiment object
        trials = exp.getTrials();
        dates = new ArrayList<>();
        System.out.println("TRIALS IN PLOT MANAGER "+ trials);
    }


    public DataPoint[] compute(){
        String type = exp.getExpType();
        DataPoint[] dp;
        getDates();
        if (type.equals("Binomial"))
            dp = binomial_plot();
        else if (type.equals("Count"))
            dp = count_plot();
        else if (type.equals("NonNegativeCount"))
            dp = nonNegative_plot();
        else
            dp = measurement_plot();
        return dp;
    }

    public ArrayList<String> getDates() {


        //dates = new ArrayList<>(); //for unique date?
        for (Trial trial : trials) {
            if (!dates.contains(trial.getDate()))
                dates.add(trial.getDate());
        }

        System.out.println("UNIQUE DATES " + dates);
        //sorting according to date
        dates = sortDates(dates);
        return dates;

    }


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

    public DataPoint[] binomial_plot(){

        System.out.println("BINOMIAL WORKING");
        //probably separate date too


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

        return dp;
    }

    public DataPoint[] count_plot(){
        System.out.println("COUNT WORKING");
        //probably separate date too


        ArrayList<DataPoint> dpList = new ArrayList<>();
        int i = 0;
        //Collections.sort(dates);
        int count = 0;
        for (String date : dates) {

            for (Trial trial : trials) {

                Count cTrial = (Count) trial;
                System.out.println(date + "===" + cTrial.getDate());
                if (cTrial.getDate().equals(date) && cTrial.getStatus()) {
                    count++;
                    //ms = bTrial.getDate();
                }
            }
            System.out.println("Success " + count);
            //Long date_mili = dates_ms.get(dates.indexOf(date));
            dpList.add(new DataPoint(i, count));
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

        return dp;
    }

    public DataPoint[] measurement_plot(){
        System.out.println("MEASUREMENT WORKING");
        ArrayList<DataPoint> dpList = new ArrayList<>();
        int i = 0;
        //Collections.sort(dates);
        double trials_sum = 0;
        int count = 0;
        for (String date : dates) {
            for (Trial trial : trials) {
                Measurement mTrial = (Measurement) trial;
                System.out.println(date + "===" + mTrial.getDate());
                if (mTrial.getDate().equals(date) && mTrial.getStatus()) {
                    trials_sum += mTrial.getMeasurement();
                    count++;
                    //ms = bTrial.getDate();
                }

            }
            System.out.println("Total " + trials_sum);
            //Long date_mili = dates_ms.get(dates.indexOf(date));
            double mean = (double)trials_sum/count;
            dpList.add(new DataPoint(i, mean));
            System.out.println("SIZE OF ARRAY " + dpList.size());
            i++;

        }
        DataPoint[] dp = new DataPoint[dpList.size()];
        dp = dpList.toArray(dp);
        System.out.println("SIZE OF ARRAY " + dp.length);
        for (int j = 0; j < dp.length; j++) {
            System.out.println("DATA POINT " + (j + 1) + "x : " + dp[j].getX() + ", y =" + dp[j].getY());
        }

        return dp;
    }

    public DataPoint[] nonNegative_plot(){
        System.out.println("NON NEGATIVE WORKING");
        ArrayList<DataPoint> dpList = new ArrayList<>();
        int i = 0;
        //Collections.sort(dates);
        double trials_sum = 0;
        int count = 0;
        for (String date : dates) {

            for (Trial trial : trials) {
                NonNegativeCount mTrial = (NonNegativeCount) trial;
                System.out.println(date + "===" + mTrial.getDate());
                if (mTrial.getDate().equals(date) && mTrial.getStatus()) {
                    trials_sum += mTrial.getValue();
                    count++;
                    //ms = bTrial.getDate();
                }

            }
            System.out.println("Total " + trials_sum);
            //Long date_mili = dates_ms.get(dates.indexOf(date));
            double mean = (double)trials_sum/count;
            dpList.add(new DataPoint(i, mean));
            System.out.println("SIZE OF ARRAY " + dpList.size());
            i++;

        }
        DataPoint[] dp = new DataPoint[dpList.size()];
        dp = dpList.toArray(dp);
        System.out.println("SIZE OF ARRAY " + dp.length);
        for (int j = 0; j < dp.length; j++) {
            System.out.println("DATA POINT " + (j + 1) + "x : " + dp[j].getX() + ", y =" + dp[j].getY());
        }

        return dp;
    }


}
