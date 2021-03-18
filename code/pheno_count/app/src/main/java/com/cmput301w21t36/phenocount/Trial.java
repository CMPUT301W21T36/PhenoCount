package com.cmput301w21t36.phenocount;


import java.io.Serializable;
import com.google.android.gms.maps.model.LatLng;

/**
 * This class represents the Trial objects
 * @author Marzookh
 */
public class Trial implements Serializable {

    private User owner;
    private String type;
    private double Latitude;
    private double Longitude;

    private boolean result;
    private int count;
    private float measurement;
    private int value;

    /**
     * constructor for new Trial object
     * @param owner
     * This is an object of type User that stores the owner of the trial
     */
    public Trial(User owner){
        this.owner = owner;
        this.type = "";

        this.count = 0;
        this.measurement=0;
        this.value =0;
        this.result = false;

        this.Latitude = 200; //@rao: these values are outside the range of latitude and longitude.
        this.Longitude = 200; //If these values are encountered, that means location has not been added yet.
    }


    /**
     * This method sets the result attribute to true
     */
    void isSuccess() {this.result = true;}

    /**
     * This method sets the result attribute to false
     */
    void isFailure() {this.result = false;}

    /**
     * This method increments the count attribute by 1
     */
    void isCount(){this.count++;}

    void setCount(int count) {this.count = count;}
    void setMeasurement(float measurement){this.measurement = measurement;}
    void setValue(int value) {this.value = value;}
    void setLongitude(double longitude){
        this.Longitude = longitude;
    }
    void setLatitude(double latitude){
        this.Latitude = latitude;
    }
    void setResult(boolean result){this.result = result;}
    void setType(String type){this.type = type;}
    void setOwner(User owner){this.owner = owner;}

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
