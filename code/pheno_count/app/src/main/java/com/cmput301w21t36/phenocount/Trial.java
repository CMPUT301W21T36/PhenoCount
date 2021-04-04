package com.cmput301w21t36.phenocount;


import java.io.Serializable;
import java.util.Date;

import com.google.android.gms.maps.model.LatLng;

/**
 * This class represents the abstract trial
 * it is extended by the different types of trials : Binomial, Count
 * , NonNegativeCount, Measurement
 */
public abstract class Trial implements Serializable {

    private User owner;
    private String type;
    private double Latitude;
    private double Longitude;
    private boolean status;
    private Date date;

    /**
     * constructor for new Trial object
     * @param owner
     * This is an object of type User that stores the owner of the trial
     */
    public Trial(User owner){
        this.owner = owner;
        this.type = "";
        this.status = true;
        this.date = new Date();
/*        this.count = 0;
        this.measurement=0;
        this.value =0;
        this.result = false;*/

        this.Latitude = 200; //@rao: these values are outside the range of latitude and longitude.
        this.Longitude = 200; //If these values are encountered, that means location has not been added yet.
    }


    void setLongitude(double longitude){
        this.Longitude = longitude;
    }
    void setLatitude(double latitude){
        this.Latitude = latitude;
    }
    void setType(String type){this.type = type;}
    void setOwner(User owner){this.owner = owner;}
    void setStatus(Boolean status){this.status = status;}

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
    public boolean getStatus(){return status;}
}
