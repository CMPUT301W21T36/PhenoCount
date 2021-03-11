package com.cmput301w21t36.phenocount;

import java.io.Serializable;

public class Trial implements Serializable {

    private String name;
    private String desc;
    private String owner;
    private String type;

    private boolean result;
    private int count;
    private float measurement;
    private int value;

    public Trial(String name,String desc,User owner,String type){
        this.count = 0;
        this.measurement=0;
        this.value =0;
    }

    void isSuccess() {this.result = true;}
    void isFailure() {this.result = false;}

    void setCount(int count) {this.count = count;}
    void setMeasurement(float measurement){this.measurement = measurement;}
    void setValue(int value) {this.value = value;}
    void isCount(){this.count++;}


    public float getMeasurement() {
        return measurement;
    }

    public int getCount() {
        return count;
    }

    public int getValue() {
        return value;
    }


}
