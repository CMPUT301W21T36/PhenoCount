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

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // recieving intent object
        super.onCreate(savedInstanceState);
        Bundle intent = getIntent().getExtras();
        Experiment newexp = (Experiment) getIntent().getSerializableExtra("experiment");//defining the Experiment object
        int position = intent.getInt("position"); //since we have bundle

        Trial trial = new Trial();
        ArrayList<Trial> trials = newexp.getTrials(); // stores the list of trial objects in trials

        // Capture the layout's TextView and set the string as its text

        TextView desc = findViewById(R.id.desc);
        desc.setText("Description:" + String.valueOf(newexp.getOwner()));

        TextView owner = findViewById(R.id.owner);
        owner.setText("Owner:" + String.valueOf(newexp.getOwner()));

        TextView status = findViewById(R.id.status);
        status.setText("Status:" + String.valueOf(newexp.getExpStatus()));

        TextView exptype= findViewById(R.id.exptype);
        exptype.setText("Experiment Type: Binomial Trial");

        /*TextView numofsuccess = findViewById(R.id.count);
        numofsuccess.setText("Successes:"+String.valueOf(newexp.getSuccess()));

        TextView numoffails = findViewById(R.id.fno);
        numoffails.setText("Fails:"+String.valueOf(newexp.getFail()));*/

        final Button sbtn = findViewById((R.id.recordbtn));
        sbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //increment successes
                trial.isSuccess();
                trials.add(trial);
                newexp.setTrials(trials);

                Intent returnIntent = new Intent();
                returnIntent.putExtra("newexp",newexp);
                returnIntent.putExtra("position",position);
                setResult(Activity.RESULT_OK,returnIntent);

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
                returnIntent.putExtra("newexp",newexp);
                returnIntent.putExtra("position",position);
                setResult(Activity.RESULT_OK,returnIntent);

                finish(); // closes this activity

            }
        });


    }


}

