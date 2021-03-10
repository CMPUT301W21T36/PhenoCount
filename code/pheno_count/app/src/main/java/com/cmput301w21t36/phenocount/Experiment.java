package com.cmput301w21t36.phenocount;

import java.util.ArrayList;

/**
 * @author Anisha
 * This class represents the Experiment objects
 */
public class Experiment {
    private String name;
    private String description;
    private String region;
    private int minimumTrials;
    private ArrayList<Trial> trials;
    private Statistic stats;
    private int expStatus; // 0 for add, 1 for published, 2 for ended, 3 for unpublished, 4 for subscribed
    private boolean requireLocation;

    public Experiment(String name, String description, String region, int minimumTrials, boolean requireLocation) {
        this.name = name;
        this.description = description;
        this.region = region;
        this.minimumTrials = minimumTrials;
        this.requireLocation = requireLocation;
    }

    public boolean isPublished() {
        if (expStatus==1){
            return true;
        }
        return false;
    }

    public boolean isEnded() {
        if (expStatus==2){
            return true;
        }
        return false;
    }

    public boolean isUnpublished() {
        if (expStatus==3){
            return true;
        }
        return false;
    }

    public boolean isSubscribed() {
        if (expStatus==4){
            return true;
        }
        return false;
    }

    public void createOR(){

    }

    public void removeTrial(int index){
        trials.remove(index);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public int getMinimumTrials() {
        return minimumTrials;
    }

    public void setMinimumTrials(int minimumTrials) {
        this.minimumTrials = minimumTrials;
    }

    public ArrayList<Trial> getTrials() {
        return trials;
    }

    public void setTrials(ArrayList<Trial> trials) {
        this.trials = trials;
    }

    public Statistic getStats() {
        return stats;
    }

    public void setStats(Statistic stats) {
        this.stats = stats;
    }

    public int getExpStatus() {
        return expStatus;
    }

    public void setExpStatus(int expStatus) {
        this.expStatus = expStatus;
    }

    public boolean isRequireLocation() {
        return requireLocation;
    }

    public void setRequireLocation(boolean requireLocation) {
        this.requireLocation = requireLocation;
    }
}
