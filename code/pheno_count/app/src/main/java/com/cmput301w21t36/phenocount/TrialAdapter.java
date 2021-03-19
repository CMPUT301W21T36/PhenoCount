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

        Trial trial = trialList.get(position);

        TextView trial_no = view.findViewById(R.id.trial_no);
        TextView trial_owner = view.findViewById(R.id.trial_owner);
        TextView trial_outcome = view.findViewById(R.id.trial_outcome);

        trial_no.setText("Trial "+position);
        trial_owner.setText("Owner : "+trial.getOwner().getProfile().getUsername());
        System.out.println(trial.getType());
        if (trial.getType().equals("Binomial")) {
            Binomial btrial = (Binomial) trialList.get(position);
            trial_outcome.setText("Result: "+btrial.getResult());
        }
        if (trial.getType().equals("Count")) {
            Count ctrial = (Count) trialList.get(position);
            trial_outcome.setText("Result: "+ctrial.getCount());
        }
        if (trial.getType().equals("Measurement")) {
            Measurement mtrial = (Measurement) trialList.get(position);
            trial_outcome.setText("Result: "+mtrial.getMeasurement());
        }
        if (trial.getType().equals("NonNegativeCount")) {
            NonNegativeCount ntrial = (NonNegativeCount) trialList.get(position);
            trial_outcome.setText("Result: "+ntrial.getValue());
        }

        return view;
    }



}
