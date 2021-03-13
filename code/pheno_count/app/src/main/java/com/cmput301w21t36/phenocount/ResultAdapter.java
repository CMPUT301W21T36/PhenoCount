package com.cmput301w21t36.phenocount;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class ResultAdapter extends ArrayAdapter<Experiment> {
    private ArrayList<Experiment> experiments;
    private Context context;


    public ResultAdapter(Context context, ArrayList<Experiment> experiments) {
        super(context, 0, experiments);
        this.experiments = experiments;
        this.context = context;
    }


    //restore Caleb's work here please

}
