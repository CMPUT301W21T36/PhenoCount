package com.cmput301w21t36.phenocount;

import java.util.ArrayList;

public class User {
    private ArrayList<Experiment> expPublished;
    private ArrayList<Experiment> expSubscribed;
    private String UID;
    private String displayName;
    private String contact;

    public User(String UID) {
        this.UID = UID;
    }

    public String getUID() {
        return UID;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}
