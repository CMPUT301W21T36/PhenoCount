package com.cmput301w21t36.phenocount;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class ResultAdapterTest extends FirestoreRecyclerAdapter<Experiment, ResultAdapterTest.ExperimentHolder> {

    public ResultAdapterTest(@NonNull FirestoreRecyclerOptions<Experiment> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ExperimentHolder holder, int position, @NonNull Experiment model) {
        holder.expName.setText(model.getName());
        holder.expOwner.setText(model.getOwner());
        holder.expStatus.setText(model.getExpStatus());
        holder.expDescription.setText((model.getDescription()));

    }

    @NonNull
    @Override
    public ExperimentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_experiment, parent, false);
        return new ExperimentHolder(v);
    }

    class ExperimentHolder extends RecyclerView.ViewHolder {
        TextView expName;
        TextView expOwner;
        TextView expStatus;
        TextView expDescription;

        public ExperimentHolder(View view) {
            super(view);
            expName = view.findViewById(R.id.expNameTextView);
            expOwner = view.findViewById(R.id.expOwnerTextView);
            expStatus = view.findViewById(R.id.expStatusTextView);
            expDescription = view.findViewById(R.id.expDescriptionTextView);
        }
    }
}
