
package com.cmput301w21t36.phenocount;

import com.jjoe64.graphview.series.DataPoint;
import com.cmput301w21t36.phenocount.Experiment;
import com.cmput301w21t36.phenocount.Trial;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
/**
 * This PlotsManager class provides as a helper for the PlotsActivity class and has functionality
 * that helps with computing data points for the plots.
 */
public class PlotsManager implements Serializable {

    private Experiment exp;
    private ArrayList<Trial> trials;
    private ArrayList<String> dates;
    private String yTitle;

    public String getyTitle() {
        return yTitle;
    }

    public void setyTitle(String yTitle) {
        this.yTitle = yTitle;
    }

    public PlotsManager(Experiment exp) {
        this.exp = exp;//defining the Experiment object
        trials = exp.getTrials();
        dates = new ArrayList<>();
        yTitle = "";
    }

    /**
     * This method computes the list of data points for plotting an experiment's line chart
     * @return
     * Data Points for experiment
     * @see PlotsActivity
     */
    public DataPoint[] compute() {
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

    /**
     * This function gets all dates a trial was conducted on.
     * @return
     * Unique String array with dates.
     */
    public ArrayList<String> getDates() {
        for (Trial trial : trials) {
            if (!dates.contains(trial.getDate()))
                dates.add(trial.getDate());
        }
        //sorting according to date
        dates = sortDates(dates);
        return dates;

    }

    /**
     * This function sorts dates that are in a string format
     * @param dates
     * @return
     * Sorted array with dates to be plotted on x axis
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

    //each activity has different way of computing data points

    /**
     * This function computes data points for an experiment of binomial type.
     * @return
     * Data points to be plotted
     */
    public DataPoint[] binomial_plot() {

        yTitle = "SUCCESSES";
        ArrayList<DataPoint> dpList = new ArrayList<>();
        int i = 0;
        int success_count = 0;
        for (String date : dates) {

            for (Trial trial : trials) {

                Binomial bTrial = (Binomial) trial;
                if (bTrial.getDate().equals(date) && bTrial.getResult() == true && bTrial.getStatus()) {
                    success_count++;
                    //ms = bTrial.getDate();
                }
            }
            dpList.add(new DataPoint(i, success_count));
            i++;

        }

        DataPoint[] dp = new DataPoint[dpList.size()];
        dp = dpList.toArray(dp);

        return dp;
    }
    /**
     * This function computes data points for an experiment of count type.
     * @return
     * Data points to be plotted
     */
    public DataPoint[] count_plot() {
        yTitle = "COUNT";
        ArrayList<DataPoint> dpList = new ArrayList<>();
        int i = 0;
        int count = 0;
        for (String date : dates) {

            for (Trial trial : trials) {

                Count cTrial = (Count) trial;
                if (cTrial.getDate().equals(date) && cTrial.getStatus()) {
                    count++;
                }
            }

            dpList.add(new DataPoint(i, count));
            i++;

        }
        DataPoint[] dp = new DataPoint[dpList.size()];
        dp = dpList.toArray(dp);
        return dp;
    }
    /**
     * This function computes data points for an experiment of measurement type.
     * @return
     * Data points to be plotted
     */
    public DataPoint[] measurement_plot() {
        ArrayList<DataPoint> dpList = new ArrayList<>();
        int i = 0;
        yTitle = "MEAN";
        double trials_sum = 0;
        int count = 0;
        for (String date : dates) {
            for (Trial trial : trials) {
                Measurement mTrial = (Measurement) trial;
                if (mTrial.getDate().equals(date) && mTrial.getStatus()) {
                    trials_sum += mTrial.getMeasurement();
                    count++;
                }

            }
            double mean = (double) trials_sum / count;
            dpList.add(new DataPoint(i, mean));
            i++;

        }
        DataPoint[] dp = new DataPoint[dpList.size()];
        dp = dpList.toArray(dp);

        return dp;
    }

    /**
     * This function computes data points for an experiment of non-negative count type.
     * @return
     * Data points to be plotted
     */
    public DataPoint[] nonNegative_plot() {
        yTitle = "MEAN";
        ArrayList<DataPoint> dpList = new ArrayList<>();
        int i = 0;
        double trials_sum = 0;
        int count = 0;
        for (String date : dates) {

            for (Trial trial : trials) {
                NonNegativeCount mTrial = (NonNegativeCount) trial;
                if (mTrial.getDate().equals(date) && mTrial.getStatus()) {
                    trials_sum += mTrial.getValue();
                    count++;
                }

            }
            double mean = (double) trials_sum / count;
            dpList.add(new DataPoint(i, mean));
            i++;

        }
        DataPoint[] dp = new DataPoint[dpList.size()];
        dp = dpList.toArray(dp);

        return dp;
    }


}
