// Alexander Filbert, LAB5, 2021-02-25, https://eclass.srv.ualberta.ca/pluginfile.php/6714046/mod_resource/content/0/Lab%205%20Firestore%20Integration%20Instructions.pdf
package com.cmput301w21t36.phenocount;

import org.junit.Test;


import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.* ;

/**
 * Test class for Experiment. All the JUnit tests are written here.
 * junit 5 is used
 * @author  Anisha
 */
public class TestExperiment {
    /**
     * This method returns a mock experiment object for testing
     * @return
     * Returns an experiment object
     */
    private Experiment mockExp(){
        Experiment experiment = new Experiment("Coin Flip",
                "To note the number of coin flips",
                "Edmonton","Binomial",10,
                false, 1,"2019387465");
        experiment.setTrials(mockTrials());
        return experiment;
    }

    /**
     * This method returns a mock array list of trials for testing
     * @return
     * returns an array list of trials
     */
    private ArrayList<Trial> mockTrials(){
        ArrayList<Trial> trials = new ArrayList<>();
        trials.add(mockTrial());
        return trials;
    }

    /**
     * This method returns a mock trial object for testing
     * @return
     * returns a trial
     */
    private Trial mockTrial(){return new Binomial(mockUser());}

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
    private Profile mockProfile(){return new Profile("Pikachu","7809615243");}

    /**
     * Tests the isPublished method of Experiment class
     * which returns a boolean and checks the status to the
     * experiment object
     */
    @Test
    public void testIsPublished(){
    Experiment exp = mockExp();
    assertEquals(true,exp.isPublished());
    exp.setExpStatus(2);
    assertEquals(false,exp.isPublished());
    }

    /**
     * Tests the isEnded method of Experiment class
     * which returns a boolean and checks the status to the
     * experiment object
     */
    @Test
    public void testIsEnded(){
        Experiment exp = mockExp();
        assertEquals(false,exp.isEnded());
        exp.setExpStatus(2);
        assertEquals(true,exp.isEnded());
    }

    /**
     * Tests the isUnpublished method of Experiment class
     * which returns a boolean and checks the status to the
     * experiment object
     */
    @Test
    public void testIsUnpublished(){
        Experiment exp = mockExp();
        assertEquals(false,exp.isUnpublished());
        exp.setExpStatus(3);
        assertEquals(true,exp.isUnpublished());
    }

    /**
     * Tests the isSubscribed method of Experiment class
     * which returns a boolean and checks the status to the
     * experiment object
     */
    @Test
    public void testIsSubscribed(){
        Experiment exp = mockExp();
        assertEquals(false,exp.isSubscribed());
        exp.setSubscribe(1);
        assertEquals(true,exp.isSubscribed());
    }

    /**
     * Tests the removeTrial method of Experiment class
     * which removes the trial at a specified index from Experiment
     * object's array list of trials
     */
    @Test
    public void testRemoveTrial(){
        Experiment exp = mockExp();
        Trial trial = mockTrial();
        assertEquals ( 1 , exp.getTrials().size()) ;
        //assertTrue(exp.getTrials().contains(trial));
        exp.removeTrial(0);
        assertEquals ( 0 , exp.getTrials().size()) ;
    }
}
