package com.cmput301w21t36.phenocount;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestBinomial {
    private Binomial mockTrial(){return new Binomial(mockUser());}
    private User mockUser(){return new User("123456789",mockProfile());}
    private Profile mockProfile(){return new Profile("Marzookh","7806601902");}

    @Test
    public void testisSuccess() {
        Binomial trial = mockTrial();
        assertEquals(false, trial.getResult());
        trial.isSuccess();
        assertEquals(true, trial.getResult());
    }

    @Test
    public void testisFailure() {
        Binomial trial = mockTrial();
        trial.isSuccess();
        assertEquals(true, trial.getResult());
        trial.isFailure();
        assertEquals(false, trial.getResult());
    }
}
