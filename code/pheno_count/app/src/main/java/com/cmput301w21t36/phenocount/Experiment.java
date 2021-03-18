package com.cmput301w21t36.phenocount;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class represents the Experiment objects
 * @author Anisha
 */
public class Experiment implements Serializable {
    private String expID; //store the ID inside this Experiment, so we can know which document it is
    private String name;
    private String description;
    private String region;
    private String expType;
    private int minimumTrials;
    private boolean requireLocation;
    private User owner;
    private ArrayList<Trial> trials;
    private Statistic stats;
    private int expStatus = -1; // 0 for add, 1 for published, 2 for ended, 3 for unpublished
    private int subscribe = 0;
   

    /**
     * This constructs a new experiment object
     * with name, description, region, minimum number of trials and/or geolocation
     * @param name
     * This is a name for the Experiment to be constructed
     * @param description
     * This is a description for the Experiment to be constructed
     * @param region
 * This is a region for the Experiment to be constructed
     * @param expType
* This specifies the type of the experiment being conducted
     * @param minimumTrials
* This is an Experiment's minimum number of trials
     * @param requireLocation
* This specifies if the geolocation is required for the Experiment
     * @param expStatus
     */
    public Experiment(String name, String description, String region, String expType, int minimumTrials, boolean requireLocation,int expStatus, String expID) {
        this.name = name;
        this.expID = expID;
        this.description = description;
        this.region = region;
        this.minimumTrials = minimumTrials;
        this.requireLocation = requireLocation;
        this.expType = expType;
        this.trials = new ArrayList<>();
        this.expStatus = expStatus;
        this.owner = owner;

    }

    public Experiment(String name, String description, String region, String expType, int minimumTrials, boolean requireLocation,int expStatus) {
        this.name = name;
        this.description = description;
        this.region = region;
        this.minimumTrials = minimumTrials;
        this.requireLocation = requireLocation;
        this.expType = expType;
        this.expStatus = expStatus;

    }



    public Experiment() {}

//    public Experiment(String expID, String name, String description, String region, String expType, int minimumTrials, boolean requireLocation, User owner, int expStatus) {
//        this.expID = expID;
//        this.name = name;
//        this.description = description;
//        this.region = region;
//        this.expType = expType;
//        this.minimumTrials = minimumTrials;
//        this.requireLocation = requireLocation;
//        this.expStatus = expStatus;
//    }


    /**
     * This method checks if the Experiment is published or not
     * @return
     * a boolean value; true if the experiment is published
     * false if the experiment's status is not published
     */
    public boolean isPublished() {
        if (expStatus==1){
            return true;
        }
        return false;
    }

    /**
     * This method checks if the Experiment has ended or not
     * @return
     * a boolean value; true if the experiment has ended
     * false if the experiment's status is not ended
     */
    public boolean isEnded() {
        if (expStatus==2){
            return true;
        }
        return false;
    }

    /**
     * This method checks if the Experiment is unpublished or not
     * @return
     * a boolean value; true if the experiment is unpublished
     * false if the experiment's status is not unpublished
     */
    public boolean isUnpublished() {
        if (expStatus==3){
            return true;
        }
        return false;
    }

    /**
     * This method checks if the Experiment is subscribed or not
     * @return
     * a boolean value; true if the experiment is subscribed
     * false if the experiment's status is not subscribed
     */
    public boolean isSubscribed() {
        if (subscribe==1){
            return true;
        }
        return false;
    }

    /**
     * This method reomves the trial from the trials list of experiment
     * @param index
     * This the index of the trial in the trials' list to be removed/ignored
     */
    public void removeTrial(int index){
        trials.remove(index);
    }

    /**
     * This method returns the name of the experiment
     * @return
     * returns the name of the experiment
     */
    public String getName() {
        return name;
    }

    /**
     * This method saves/adds the name to the experiment
     * @param name
     * The name for the experiment that has to be saved/added
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * This method returns the description of the experiment
     * @return
     * returns the description of the experiment
     */
    public String getDescription() {
        return description;
    }

    /**
     * This method saves/adds the description to the experiment
     * @param description
     * The description for the experiment that has to be saved/added
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * This method returns the region of the experiment
     * @return
     * returns the region of the experiment
     */
    public String getRegion() {
        return region;
    }

    /**
     * This method saves/adds the region to the experiment
     * @param region
     * The region for the experiment that has to be saved/added
     */
    public void setRegion(String region) {
        this.region = region;
    }

    /**
     * This method returns the minimum number of trial of the experiment
     * @return
     * returns the minimum number of trial of the experiment
     */
    public int getMinimumTrials() {
        return minimumTrials;
    }

    /**
     * This method saves/adds the minimum number of trial to the experiment
     * @param minimumTrials
     * The minimum number of trial for the experiment that has to be saved/added
     */
    public void setMinimumTrials(int minimumTrials) {
        this.minimumTrials = minimumTrials;
    }

    /**
     * This method returns the array list of trial of the experiment
     * @return
     * returns the array list of trial of the experiment
     */
    public ArrayList<Trial> getTrials() {
        return trials;
    }

    public void setTrials(ArrayList<Trial> trials) {
        this.trials = trials;
    }

    public Statistic getStats() {
        return stats;
    }

    public int getExpStatus() {
        return expStatus;
    }

    public void setExpStatus(int expStatus) {
        this.expStatus = expStatus;
    }

    /**
     * This method checks if the Experiment requires geolocation
     * @return
     * a boolean value; true for requiring the geolocation
     * and false for not requiring the geolocation
     */
    public boolean isRequireLocation() {
        return requireLocation;
    }

    public void setRequireLocation(boolean requireLocation) {
        this.requireLocation = requireLocation;
    }

    /**
     * This method returns the owner of the experiment
     * @return
     * returns the owner of the experiment
     */
    public User getOwner() {
        return owner;
    }

    /**
     * This method saves/adds the owner to the experiment
     * @param owner
     * The owner for the experiment that has to be saved/added
     */
    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getExpType(){ return expType; }

    public void setExpType(String expType){this.expType=expType;}

    public String getID() {
        return expID;
    }

    public void setExpID(String expID) {
        this.expID = expID;
    }

    public void setSubscribe(int subscribe) {
        this.subscribe = subscribe;
    }
}
