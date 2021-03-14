package com.cmput301w21t36.phenocount;

import android.content.Context;
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
        String mStat = "" ;
        switch(experiment.getExpStatus()){
            case 1:
                mStat = "Published";
                break;
            case 2:
                mStat= "Ended";
                break;
            case 3:
                mStat = "Unpublished";
                break;
            default:
                mStat= "Added";

        }
        expstatus.setText(mStat);


        return view;
    }
}
