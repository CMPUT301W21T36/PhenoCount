package com.cmput301w21t36.phenocount;

import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class ExperimentList extends ArrayAdapter<Experiment> {
    private ArrayList<Experiment> experiments;
    private Context context;


    public ExperimentList(Context context, int resource, ArrayList<Experiment> experiments) {
        super(context, resource, experiments);
        this.experiments = experiments;
        this.context = context;
    }
}
