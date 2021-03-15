package com.cmput301w21t36.phenocount;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {
    private ArrayList<Experiment> expPublished;
    private ArrayList<Experiment> expSubscribed;
    private String UID;
    private Profile profile;

    public User(String UID, Profile profile) {
        this.UID = UID;
        this.profile = profile;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }
}
