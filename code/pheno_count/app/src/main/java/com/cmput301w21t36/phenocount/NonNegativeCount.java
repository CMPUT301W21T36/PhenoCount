package com.cmput301w21t36.phenocount;

import java.io.Serializable;

public class NonNegativeCount extends Trial implements Serializable {

    private int value;
    /**
     * constructor for new Trial object
     *
     * @param owner This is an object of type User that stores the owner of the trial
     */
    public NonNegativeCount(User owner) {
        super(owner);
        value = 0;
    }


    public float getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
