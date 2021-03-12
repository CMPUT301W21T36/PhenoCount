package com.cmput301w21t36.phenocount;

import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class ExperimentAdapter extends ArrayAdapter<Experiment> { //tell Caleb this is renamed
    private ArrayList<Experiment> experiments;
    private Context context;
    /*
    ArrayAdapter<Object>


     */

    //need to add the resource: content layout
    public ExperimentAdapter(Context context, int resource, ArrayList<Experiment> experiments) {
        super(context, resource, experiments);
        this.experiments = experiments;
        this.context = context;
    }
}
