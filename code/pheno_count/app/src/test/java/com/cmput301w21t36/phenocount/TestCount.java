package com.cmput301w21t36.phenocount;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestCount {
    private Count mockTrial(){return new Count(mockUser());}
    private User mockUser(){return new User("123456789",mockProfile());}
    private Profile mockProfile(){return new Profile("Marzookh","7806601902");}

    @Test
    public void testisCount() {
        Count trial = mockTrial();
        assertEquals(0, trial.getCount());
        trial.isCount();
        assertEquals(1, trial.getCount());
    }


}
