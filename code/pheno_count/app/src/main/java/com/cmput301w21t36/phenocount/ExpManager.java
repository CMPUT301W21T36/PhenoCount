package com.cmput301w21t36.phenocount;

import java.util.ArrayList;

public class ExpManager {
    Experiment exp ;
    private ArrayList<Experiment> expDataList= new ArrayList<>();

    // adds experiment to the data list
    public void  addExperiment(Experiment newExp){
        expDataList.add(newExp);

    }


}
