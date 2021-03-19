package com.cmput301w21t36.phenocount;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

/**
 * This Controller/View class accepts parameters from ResultsActivity and displays the results
 * of the trials
 * @author Marzookh
 */
public class TrialAdapter extends ArrayAdapter<Trial> {
    private ArrayList<Trial> trialList;
    private Context context;


    public TrialAdapter(Context context, ArrayList<Trial> trialList) {
        super(context,0,trialList);
        this.trialList = trialList;
        this.context = context;
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.content_trials,parent,false);
        }

        //getting the trial object
        Trial trial = getItem(position);
        System.out.println("Type:" + trial.getType());

/*        Measurement trial = (Measurement) trialList.get(position);
        System.out.println("NEW MEASUREMENT :"+ trial.getMeasurement());*/

        //initializing textviews
        TextView trial_no = view.findViewById(R.id.trial_no);
        TextView trial_owner = view.findViewById(R.id.trial_owner);
        TextView trial_outcome = view.findViewById(R.id.trial_outcome);

        //trial_outcome.setText("Result: "+trial.getMeasurement());


        //setting common trial attributes
        trial_owner.setText(trial.getOwner().getProfile().getUsername());
        trial_no.setText("Trial "+(position+1));

        //checking type of trial and setting result
        if (trial.getType().equals("Binomial")) {
            Binomial btrial = (Binomial) getItem(position);
            trial_outcome.setText("Result: "+btrial.getResult());
            //trial_owner.setText("Owner : "+btrial.getOwner().getProfile().getUsername());
        }
        if (trial.getType().equals("Count")) {
            Count ctrial = (Count) getItem(position);
            trial_outcome.setText("Result: "+ctrial.getCount());
            //trial_owner.setText("Owner : "+ctrial.getOwner().getProfile().getUsername());
        }
        if (trial.getType().equals("Measurement")) {
            Measurement mtrial = (Measurement) trial;
            trial_outcome.setText("Result: "+mtrial.getMeasurement());
            //trial_owner.setText("Owner : "+mtrial.getOwner().getProfile().getUsername());

        }
        if (trial.getType().equals("NonNegativeCount")) {
            NonNegativeCount ntrial = (NonNegativeCount) trial ;
            trial_outcome.setText("Result: "+ntrial.getValue());
            //trial_owner.setText("Owner : "+ntrial.getOwner().getProfile().getUsername());

        }
        return view;
    }



}
