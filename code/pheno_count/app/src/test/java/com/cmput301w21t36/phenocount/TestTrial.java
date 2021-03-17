package com.cmput301w21t36.phenocount;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class TestTrial {

    private Trial mockTrial(){return new Trial(mockUser());}
    private User mockUser(){return new User("123456789",mockProfile());}
    private Profile mockProfile(){return new Profile("Marzookh","7806601902");}

    @Test
    public void testisSuccess() {
        Trial trial = mockTrial();
        assertEquals(false, trial.getResult());
        trial.isSuccess();
        assertEquals(true, trial.getResult());
    }

    @Test
    public void testisFailure() {
        Trial trial = mockTrial();
        trial.isSuccess();
        assertEquals(true, trial.getResult());
        trial.isFailure();
        assertEquals(false, trial.getResult());
    }

    @Test
    public void testisCount() {
        Trial trial = mockTrial();
        assertEquals(0, trial.getCount());
        trial.isCount();
        assertEquals(1, trial.getCount());
    }



}
