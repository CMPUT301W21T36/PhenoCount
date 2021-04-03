package com.cmput301w21t36.phenocount;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class represents the Experiment objects
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
    private ArrayList<Trial> trials = new ArrayList<>();
    private Statistic stats;
    private int expStatus = -1; // 0 for add, 1 for published, 2 for ended, 3 for unpublished
    private int subscribe = 0;
    private ArrayList subscribers = new ArrayList();

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
     * This specifies the type of the Experiment being conducted
     * @param minimumTrials
     * This is an Experiment's minimum number of trials for the Experiment to be constructed
     * @param requireLocation
     * This specifies if the geolocation is required for the Experiment to be constructed
     * @param expStatus
     * This parameter specifies the status of the Exepriment to be constructed
     * @param expID
     * Thisis the ID of the Experiment object to be constructed
     */
    public Experiment(String name, String description, String region, String expType, int minimumTrials, boolean requireLocation,int expStatus, String expID) {
        this.name = name;
        this.expID = expID;
        this.description = description;
        this.region = region;
        this.minimumTrials = minimumTrials;
        this.requireLocation = requireLocation;
        this.expType = expType;
        this.expStatus = expStatus;
    }

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
    public void removeTrial(int index){ trials.remove(index); }

    /**
     * This method returns the name of the experiment
     * @return
     * returns the name of the experiment
     */
    public String getName() { return name; }

    /**
     * This method saves/adds the name to the experiment
     * @param name
     * The name for the experiment that has to be saved/added
     */
    public void setName(String name) { this.name = name; }

    /**
     * This method returns the description of the experiment
     * @return
     * returns the description of the experiment
     */
    public String getDescription() { return description; }

    /**
     * This method saves/adds the description to the experiment
     * @param description
     * The description for the experiment that has to be saved/added
     */
    public void setDescription(String description) { this.description = description; }

    /**
     * This method returns the region of the experiment
     * @return
     * returns the region of the experiment
     */
    public String getRegion() { return region; }

    /**
     * This method saves/adds the region to the experiment
     * @param region
     * The region for the experiment that has to be saved/added
     */
    public void setRegion(String region) { this.region = region; }

    /**
     * This method returns the minimum number of trial of the experiment
     * @return
     * returns the minimum number of trial of the experiment
     */
    public int getMinimumTrials() { return minimumTrials; }

    /**
     * This method saves/adds the minimum number of trial to the experiment
     * @param minimumTrials
     * The minimum number of trial for the experiment that has to be saved/added
     */
    public void setMinimumTrials(int minimumTrials) { this.minimumTrials = minimumTrials; }

    /**
     * This method returns the array list of trial of the experiment
     * @return
     * returns the array list of trial of the experiment
     */
    public ArrayList<Trial> getTrials() { return trials; }

    /**
     * This method saves/adds the array list of trial to the experiment
     * @param trials
     * array list of trial of the experiment that has to be saved/added
     */
    public void setTrials(ArrayList<Trial> trials) { this.trials = trials; }

    /**
     * This method returns the statistics of the experiment
     * @return
     * returns the statistics for the experiment
     */
    public Statistic getStats() {return stats;}

    /**
     * This method saves/adds the the statistics for the experiment
     * @param stats
     * the statistics for the experiment
     */
    public void setStats(Statistic stats) { this.stats = stats; }

    /**
     * This method returns the status of the experiment
     * @return
     * returns the status of the experiment
     */
    public int getExpStatus() { return expStatus; }

    /**
     * This method saves/adds the status of the experiment
     * @param expStatus
     * status of the experiment to be saved/added
     */
    public void setExpStatus(int expStatus) { this.expStatus = expStatus; }

    /**
     * This method checks if the Experiment requires geolocation
     * @return
     * a boolean value; true for requiring the geolocation
     * and false for not requiring the geolocation
     */
    public boolean isRequireLocation() { return requireLocation; }

    /**
     * This method saves/adds the boolean value for the requirement of geolocation
     * @param requireLocation
     * a boolean value; true for requiring the geolocation
     * and false for not requiring the geolocation
     */
    public void setRequireLocation(boolean requireLocation) { this.requireLocation = requireLocation; }

    /**
     * This method returns the owner of the experiment
     * @return
     * returns the owner of the experiment
     */
    public User getOwner() { return owner; }

    /**
     * This method saves/adds the owner to the experiment
     * @param owner
     * The owner for the experiment that has to be saved/added
     */
    public void setOwner(User owner) { this.owner = owner; }

    /**
     * This method returns the type of the experiment
     * @return
     * returns the type for the experiment
     */
    public String getExpType(){ return expType; }

    /**
     * This method saves/adds the type of the experiment
     * @param expType
     * the type for the experiment to be saved/added
     */
    public void setExpType(String expType){this.expType=expType;}

    /**
     * This method returns the ID of the experiment
     * @return
     * returns the ID for the experiment
     */
    public String getExpID() { return expID; }

    /**
     * This method saves/adds the ID to the experiment
     * @param expID
     *  ID of the experiment to be added/saved
     */
    public void setExpID(String expID) { this.expID = expID; }

    /**
     * This method saves/adds the flag for subscribed/unsubscribed status to the experiment
     * @param subscribe
     * 0 for unsubscribed and 1 for subscribed
     */
    public void setSubscribe(int subscribe) { this.subscribe = subscribe; }

    /**
     * This method returns the flag for subcribed/unsubscribed status of the experiment
     * @return
     * 0 for unsubscribed and 1 for subscribed
     */
    public int getSubscribe() { return subscribe; }

    /**
     * This method returns the list of subcribers of the experiment
     * @return
     * returns the list of subcribers of the experiment
     */
    public ArrayList getSubscribers() {
        return subscribers;
    }

    /**
     * This method saves/adds the list of subcribers of the experiment
     * @param subscribers
     *  the list of subcribers of the experiment
     */
    public void setSubscribers(ArrayList subscribers) {
        this.subscribers = subscribers;
    }
}
