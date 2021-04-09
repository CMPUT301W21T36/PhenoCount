package com.cmput301w21t36.phenocount;

import java.io.Serializable;

/**
 * This class represents objects used in Measurement trials
 */
public class Measurement extends Trial implements Serializable {
    private float measurement;

    /**
     * constructor for new Trial object
     *
     * @param owner This is an object of type User that stores the owner of the trial
     */
    public Measurement(User owner) {
        super(owner);
        measurement = 0;

    }

    public float getMeasurement() {
        return measurement;
    }

    public void setMeasurement(float measurement) {
        this.measurement = measurement;
    }
}
