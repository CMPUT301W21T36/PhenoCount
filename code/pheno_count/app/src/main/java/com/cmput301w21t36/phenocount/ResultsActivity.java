package com.cmput301w21t36.phenocount;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ResultsActivity extends AppCompatActivity {
    ListView trials;
    ArrayAdapter<Trial> trialAdapter;
    ArrayList<Trial> trialList;
    Experiment exp;//defining the Experiment object

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        //initializing attributes
        trials =findViewById(R.id.trial_list);
        trialList = new ArrayList<>();

        //getting intent
        exp = (Experiment) getIntent().getSerializableExtra("experiment");//defining the Experiment object
        trialList = exp.getTrials();

        //initializing adapter
        trialAdapter = new TrialAdapter(this,trialList);
        trials.setAdapter(trialAdapter);








    }

}