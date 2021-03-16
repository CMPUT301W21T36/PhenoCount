package com.cmput301w21t36.phenocount;


import java.io.Serializable;
import com.google.android.gms.maps.model.LatLng;


public class Trial implements Serializable {

    private String name;
    private String desc;
    private User owner;
    private String type;
    private double Latitude;
    private double Longitude;

    private boolean result;
    private int count;
    private float measurement;
    private int value;

    public Trial(User owner){
        this.name = name;
        this.desc = desc;
        this.owner = owner;
        this.type = type;
        this.count = 0;
        this.measurement=0;
        this.value =0;
        this.result = false; // do we do this
        this.Latitude = 0;
        this.Longitude =0;
    }

    void isSuccess() {this.result = true;}
    void isFailure() {this.result = false;}

    void setCount(int count) {this.count = count;}
    void setMeasurement(float measurement){this.measurement = measurement;}
    void setValue(int value) {this.value = value;}
    void isCount(){this.count++;}
    void setLongitude(double longitude){
        this.Longitude = longitude;
    }
    void setLatitude(double latitude){
        this.Latitude = latitude;
    }
    void setResult(boolean result){this.result = result;}
    void setType(String type){this.type = type;};

    public boolean getResult(){return result;};

    public float getMeasurement() {
        return measurement;
    }

    public int getCount() {
        return count;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }



    public double getLatitude() {
        return Latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public User getOwner() {
        return owner;
    }

    public String getType() {
        return type;
    }
}
