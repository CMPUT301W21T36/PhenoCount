package com.cmput301w21t36.phenocount;

public class User {

    private String UID = "111"; // This is a garbage UID that will be updated when connected to Firestore
    private String displayName;
    private String contact;



    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setContact(String contact) {

        this.contact = contact;
    }
}
