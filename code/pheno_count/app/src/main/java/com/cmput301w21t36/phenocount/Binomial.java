package com.cmput301w21t36.phenocount;

import java.io.Serializable;

public class Binomial extends Trial implements Serializable {

    private boolean result;


    /**
     * constructor for new Binomial object
     *
     * @param owner This is an object of type User that stores the owner of the trial
     */
    public Binomial(User owner) {
        super(owner);
        result = false;

    }

    /**
     * This method sets the result attribute to true
     */
    void isSuccess() {this.result = true;}

    /**
     * This method sets the result attribute to false
     */
    void isFailure() {this.result = false;}

    public boolean isResult() {
        return result;
    }


    public void setResult(boolean result) {
        this.result = result;
    }


    public boolean getResult() {
        return this.result;
    }





}
