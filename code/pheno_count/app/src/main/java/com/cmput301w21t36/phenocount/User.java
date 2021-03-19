package com.cmput301w21t36.phenocount;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Class that represents a User object
 * @see Profile
 */
public class User implements Serializable {
    private ArrayList<Experiment> expPublished;
    private ArrayList<Experiment> expSubscribed;
    private String UID;
    private Profile profile;

    /**
     * Constructor for a User object
     * @param UID is a String representing a unique identifier
     * @param profile is an object of type Profile that will be related to one user
     */
    public User(String UID, Profile profile) {
        this.UID = UID;
        this.profile = profile;
    }

    /**
     * Getter for a user UID
     * @return UID as a String
     */
    public String getUID() {
        return UID;
    }

    /**
     * Setter for a user UID
     * @param UID
     */
    public void setUID(String UID) {
        this.UID = UID;
    }

    /**
     * Getter for a User's related profile
     * @return object of type Profile
     */
    public Profile getProfile() {
        return profile;
    }

}
