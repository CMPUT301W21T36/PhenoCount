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

public class ResultAdapter extends ArrayAdapter<Experiment> {
    private ArrayList<Experiment> experiments;
    private Context context;

    public ResultAdapter(Context context, ArrayList<Experiment> experiments) {
        super(context, 0, experiments);
        this.experiments = experiments;
        this.context = context;
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.content_experiment,parent,false);
        }

        TextView expName = view.findViewById(R.id.expNameTextView);
        TextView expOwner = view.findViewById(R.id.expOwnerTextView);
        TextView expStatus = view.findViewById(R.id.expStatusTextView);
        TextView expDescription = view.findViewById(R.id.expDescriptionTextView);

        Experiment exp = getItem(position);

        expName.setText(exp.getName());
        expOwner.setText(exp.getOwner().getProfile().getUsername());

        String expStat = "" ;
        switch(exp.getExpStatus()){
            case 1:
                expStat = "Published";
                break;
            case 2:
                expStat= "Ended";
                break;
            case 3:
                expStat = "Unpublished";
                break;
            default:
                expStat= "Added";

        }
        expStatus.setText(expStat);
        expDescription.setText(exp.getDescription());

        return view;
    }
}
