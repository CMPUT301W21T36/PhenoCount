package com.cmput301w21t36.phenocount;

import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
public class CustomAdapter extends ArrayAdapter<Object> {
    private ArrayList<Question> questions;
    private ArrayList<Experiment> experiments;
    private ArrayList<Reply> replies;
    private Context context;
    private int resource;
    /*
    ArrayAdapter<Object>


     */

    //need to add the resource: content layout
    public CustomAdapter(Context context, int resource, ArrayList<Question> questions) {
        super(context, resource, questions);
        this.resource = resource;
        this.context = context;
    }

    public CustomAdapter(Context context, int resource, ArrayList<Object> experiemnts) {
        super(context, resource, experiemnts);
        this.resource = resource;
        this.experiments = experiemnts;
        this.context = context;
    }
}
