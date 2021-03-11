package com.cmput301w21t36.phenocount;

import com.google.firebase.firestore.FirebaseFirestore;

public class Profile {
    // Will grab the current user
    private User user;
    FirebaseFirestore db;


    public Profile(User user) {
        this.user = user;
    }

    public void editDisplayName(String displayName){
        this.user.setDisplayName(displayName);
    }

    public void editContactInfo(String contactInfo){
        this.user.setContact(contactInfo);
    }

    // Testing code, ignore
    // String uid = user.getUID();
}
