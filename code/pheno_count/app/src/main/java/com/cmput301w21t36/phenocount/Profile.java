package com.cmput301w21t36.phenocount;

public class Profile {
    // Will grab the current user
    private User user;


    public Profile(User user) {
        this.user = user;
    }

    String uid = user.getUID();
}
