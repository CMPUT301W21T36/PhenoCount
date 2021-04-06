package com.cmput301w21t36.phenocount;

import java.io.Serializable;

/**
 * This class represents the Profile object which is had by a User
 */

public class Profile implements Serializable {

    private String username;
    private String phone;

    /**
     * Constructor for Profile that will utilize a username and phone
     * @param username String representing a sequence of characters the user chooses
     * @param phone String representing contact information for the respective user
     */
    public Profile(String username, String phone) {
        this.username = username;
        this.phone = phone;
    }

    /**
     * Constructor for Profile
     */
    public Profile(){}

    /**
     * Getter for a Profile's username
     * @return String representing username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Setter for a Profile's username and will be used when the user wants to update their username
     * @param username String representing a username
     */
    public void setUsername(String username) {
      this.username = username;
    }

    /**
     * Getter for a Profile's contact information
     * @return String representing contact information
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Setter for a Profile's contact information and will be called when user wants to update their contact info
     * @param phone String representing contact information
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }
}


