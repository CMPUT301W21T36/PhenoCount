package com.cmput301w21t36.phenocount;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ResultAdapter extends ArrayAdapter<Experiment> implements Filterable {
    private ArrayList<Experiment> experiments = null;
    private ArrayList<Experiment> filteredExperiments = null;
    private Context context;
    private Filter mFilter = null;

    public ResultAdapter(Context context, ArrayList<Experiment> experiments) {
        super(context, 0, experiments);
        this.experiments = experiments;
        this.filteredExperiments = experiments;
        this.context = context;
    }

    @Override
    public int getCount() {
        return filteredExperiments.size();
    }

    @Override
    public Experiment getItem(int position) {
        return filteredExperiments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.content_experiment, parent, false);
        }

        TextView expName = view.findViewById(R.id.expNameTextView);
        TextView expOwner = view.findViewById(R.id.expOwnerTextView);
        TextView expStatus = view.findViewById(R.id.expStatusTextView);
        TextView expDescription = view.findViewById(R.id.expDescriptionTextView);

        Experiment exp = experiments.get(position);

        expName.setText(exp.getName());
        expOwner.setText(exp.getOwner().getProfile().getUsername());
        expName.setTextColor(Color.parseColor("#FF69B4"));
        expOwner.setTextColor(Color.parseColor("#FF69B4"));
        expDescription.setTextColor(Color.parseColor("#FF69B4"));
        if (exp.getExpType().equals("Binomial")) {
            expName.setTextColor(Color.parseColor("#008000"));
            expOwner.setTextColor(Color.parseColor("#008000"));
            expDescription.setTextColor(Color.parseColor("#008000"));
        } else if (exp.getExpType().equals("Count")) {
            expName.setTextColor(Color.parseColor("#8B4513"));
            expOwner.setTextColor(Color.parseColor("#8B4513"));
            expDescription.setTextColor(Color.parseColor("#8B4513"));
        } else if (exp.getExpType().equals("NonNegativeCount")) {
            expName.setTextColor(Color.parseColor("#191970"));
            expOwner.setTextColor(Color.parseColor("#191970"));
            expDescription.setTextColor(Color.parseColor("#191970"));
        }

        String expStat = "";
        switch (exp.getExpStatus()) {
            case 1:
                expStat = "Published";
                expStatus.setTextColor(Color.parseColor("#FF018786"));
                break;
            case 2:
                expStat = "Ended";
                expStatus.setTextColor(Color.parseColor("#B00200"));
                break;
            case 3:
                expStat = "Unpublished";
                expStatus.setTextColor(Color.parseColor("#FF8800"));
                break;
            default:
                expStat = "Added";
                expStatus.setTextColor(Color.parseColor("#7189FF"));
        }
        expStatus.setText(expStat);
        expDescription.setText(exp.getDescription());
        return view;
    }







    // Doesn't work
    /*
    public Filter getFilter() {
        if (mFilter == null)
            mFilter = new CustomFilter();
        return mFilter;

    }
    private class CustomFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            constraint = constraint.toString().toLowerCase();
            FilterResults result = new FilterResults();
            if (constraint != null && constraint.toString().length() > 0) {
                ArrayList<Experiment> filt = new ArrayList<Experiment>(); //filtered list
                for (int i = 0; i < experiments.size(); i++) {
                    Experiment m = experiments.get(i);
                    if (m.getName().toLowerCase().contains(constraint)) {
                        filt.add(m); //add only items which matches
                    }
                }
                result.count = filt.size();
                result.values = filt;
            } else { // return original list
                synchronized (this) {
                    result.values = experiments;
                    result.count = experiments.size();
                }
            }
            return result;
        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            if (results != null) {
                setList((ArrayList<Experiment>) results.values); // notify data set changed
            } else {
                setList(experiments);
            }
        }
    }

    public void setList(ArrayList<Experiment> data) {
        //mList = data; // set the adapter list to data
        ResultAdapter.this.notifyDataSetChanged(); // notify data set change
    }

     */



}
