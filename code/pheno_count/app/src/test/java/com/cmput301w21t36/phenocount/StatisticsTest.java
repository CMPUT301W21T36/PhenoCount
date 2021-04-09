package com.cmput301w21t36.phenocount;

import com.cmput301w21t36.phenocount.Measurement;

import org.junit.Test;

import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.* ;

public class StatisticsTest {
    private Experiment mockExp() {
        Experiment experiment = new Experiment("Coin Flip",
                "To note the number of coin flips",
                "Edmonton", "Measurement", 10,
                false, 1, "2019387465");
        experiment.setTrials(mockTrials());
        return experiment;
    }

    /**
     * This method returns a mock array list of trials for testing
     *
     * @return returns an array list of trials
     */
    private ArrayList<Trial> mockTrials() {
        ArrayList<Trial> trials = new ArrayList<>();
        trials.add(mockTrial());
        return trials;
    }

    private Trial mockTrial(){
       Measurement trial = new Measurement(mockUser());
       trial.setMeasurement(5);
       return trial;
    }

    /**
     * This method returns a mock User for testing
     * @return
     * returns an User
     */
    private User mockUser(){return new User("9012873456",mockProfile());}

    /**
     * This method returns a mock profile for testing
     * @return
     * returns a profile
     */
    private Profile mockProfile(){return new Profile("Charizard","7809615243");}

    private Statistic mockStatistic(){return new Statistic();}

    @Test
    public void testStatistics(){
        Experiment exp = mockExp();
        Statistic statistic = mockStatistic();
        Measurement trial = new Measurement(mockUser());
        trial.setMeasurement(6.5f);
        assertEquals(6.5f,trial.getMeasurement());
        exp.getTrials().add(trial);
        //System.out.println(exp.getTrials().get(1));
        double mean = statistic.getMean(exp.getTrials(),exp.getExpType());
        double median = statistic.getMedian(exp.getTrials(),exp.getExpType());
        assertEquals(5.75,mean);
        assertEquals(5.75,median);
    }
}
