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
    Trial trial = new Trial();
    Experiment newexp = (Experiment) getIntent().getSerializableExtra("experiment");//defining the Experiment object
    ArrayList<Trial> trials = newexp.getTrials(); // stores the list of trial objects in trials
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // recieving intent object
        super.onCreate(savedInstanceState);


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

