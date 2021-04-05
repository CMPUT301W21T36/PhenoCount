package com.cmput301w21t36.phenocount;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ExperimentAdapter extends ArrayAdapter<Experiment> {
    private ArrayList<Experiment> experiments;
    private Context context;


    public ExperimentAdapter(Context context, ArrayList<Experiment> experiments) {
        super(context, 0, experiments);
        this.experiments = experiments;
        this.context = context;
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.content,parent,false);
        }

        Experiment experiment = experiments.get(position);

        TextView expName = view.findViewById(R.id.expname);
        TextView expstatus = view.findViewById(R.id.expstatus);

        expName.setText(experiment.getName());
        if (experiment.getName().length()==0){
                expName.setText(experiment.getDescription());
        }

        expName.setTextColor(Color.parseColor("#FF69B4"));
        if (experiment.getExpType().equals("Binomial")){
            expName.setTextColor(Color.parseColor("#008000"));
        } else if (experiment.getExpType().equals("Count")){
            expName.setTextColor(Color.parseColor("#8B4513"));
        } else if (experiment.getExpType().equals("NonNegativeCount")){
            expName.setTextColor(Color.parseColor("#191970"));

        }

        String mStat = "" ;
        switch(experiment.getExpStatus()){
            case 1:
                mStat = "Published";
                //expName.setTextColor(Color.parseColor("#FF018786"));
                expstatus.setTextColor(Color.parseColor("#FF018786"));
                break;
            case 2:
                mStat= "Ended";
                //expName.setTextColor(Color.parseColor("#B00200"));
                expstatus.setTextColor(Color.parseColor("#B00200"));
                break;
            case 3:
                mStat = "Unpublished";
                //expName.setTextColor(Color.parseColor("#FF8800"));
                expstatus.setTextColor(Color.parseColor("#FF8800"));
                break;
            default:
                mStat= "Added";
                //expName.setTextColor(Color.parseColor("#7189FF"));
                expstatus.setTextColor(Color.parseColor("#7189FF"));

        }
        expstatus.setText(mStat);

        return view;
    }
}
