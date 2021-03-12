package com.cmput301w21t36.phenocount;

import com.google.type.LatLng;

import java.io.Serializable;

public class Trial implements Serializable {

    private String name;
    private String desc;
    private User owner;
    private String type;
    com.google.android.gms.maps.model.LatLng location;

    private boolean result;
    private int count;
    private float measurement;
    private int value;

    public Trial(String name,String desc,User owner,String type){
        this.name = name;
        this.desc = desc;
        this.owner = owner;
        this.type = type;
        this.count = 0;
        this.measurement=0;
        this.value =0;
        this.result = false; // do we do this
    }

    void isSuccess() {this.result = true;}
    void isFailure() {this.result = false;}

    void setCount(int count) {this.count = count;}
    void setMeasurement(float measurement){this.measurement = measurement;}
    void setValue(int value) {this.value = value;}
    void isCount(){this.count++;}
    void setLocation(com.google.android.gms.maps.model.LatLng location){
        this.location = location;
    }


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
