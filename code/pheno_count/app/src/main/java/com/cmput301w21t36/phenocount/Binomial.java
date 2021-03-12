package com.cmput301w21t36.phenocount;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class Binomial extends AppCompatActivity {
    Trial trial;
    Experiment newexp;//defining the Experiment object
    ArrayList<Trial> trials; // stores the list of trial objects in trials
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // recieving intent object
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trial_binomial);


        newexp = (Experiment) getIntent().getSerializableExtra("experiment");//defining the Experiment object
        trials = newexp.getTrials(); // stores the list of trial objects in trials
        trial = new Trial(newexp.getName(),newexp.getDescription(),newexp.getOwner(),newexp.getExpType());


        // Capture the layout's TextView and set the string as its text

        TextView desc = findViewById(R.id.desc1);
        desc.setText("Description:" + String.valueOf(newexp.getDescription()));

        TextView owner = findViewById(R.id.owner1);
        owner.setText("Owner:" + String.valueOf(newexp.getOwner()));

        TextView status = findViewById(R.id.status1);
        status.setText("Status:" + String.valueOf(newexp.getExpStatus()));

        TextView exptype= findViewById(R.id.exptype1);
        exptype.setText("Experiment Type: Binomial Trial");

        final Button sbtn = findViewById((R.id.successbtn));
        sbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //increment successes
                trial.isSuccess();
                trials.add(trial);
                newexp.setTrials(trials);

                Intent returnIntent = new Intent();
                returnIntent.putExtra("experiment",newexp);
                setResult(Activity.RESULT_OK,returnIntent);
                System.out.println("RUNNING OKAY");

                finish(); // closes this activity

            }
        });

        final Button fbtn = findViewById((R.id.failbtn));
        fbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //increments fails
                trial.isFailure();
                trials.add(trial);
                newexp.setTrials(trials);

                Intent returnIntent = new Intent();
                returnIntent.putExtra("experiment",newexp);
                setResult(Activity.RESULT_OK,returnIntent);

                finish(); // closes this activity

            }
        });

        final Button lbtn = findViewById(R.id.locationbtn);
        lbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent (Binomial.this,LocationActivity.class);
                intent.putExtra("trial_obj",trial);
                int LAUNCH_SECOND_ACTIVITY = 1;
                startActivityForResult(intent,LAUNCH_SECOND_ACTIVITY); }
        });


    }

    @Override
    //Sends the experiment object and retrieves the updated object
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int LAUNCH_SECOND_ACTIVITY = 1;
        if (requestCode == LAUNCH_SECOND_ACTIVITY) {
            if(resultCode == Activity.RESULT_OK){
                Trial trial = (Trial) data.getSerializableExtra("trial_obj");
                trials.add(trial);
                newexp.setTrials(trials);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                System.out.println("No Data");
            }
        }
    }
}

