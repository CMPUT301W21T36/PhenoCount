package com.cmput301w21t36.phenocount;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class Measurement extends AppCompatActivity {
    Trial trial = new Trial();
    Experiment newexp = (Experiment) getIntent().getSerializableExtra("experiment");//defining the Experiment object
    ArrayList<Trial> trials = newexp.getTrials(); // stores the list of trial objects in trials
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // receiving intent object
        super.onCreate(savedInstanceState);
        // Capture the layout's TextView and set the string as its text

        TextView desc = findViewById(R.id.desc);
        desc.setText("Description:" + String.valueOf(newexp.getOwner()));

        TextView owner = findViewById(R.id.owner);
        owner.setText("Owner:" + String.valueOf(newexp.getOwner()));

        TextView status = findViewById(R.id.status);
        status.setText("Status:" + String.valueOf(newexp.getExpStatus()));

        TextView exptype= findViewById(R.id.exptype);
        exptype.setText("Experiment Type: Measurement");

        EditText measurement = findViewById(R.id.measurement_editText);


        final Button recordvbtn = findViewById((R.id.recordvbtn));
        recordvbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String temp = measurement.getText().toString();
                float value = 0;
                if (!"".equals(temp)){
                    value=Float.parseFloat(temp);    //https://javawithumer.com/2019/07/get-value-edittext.html
                }
                trial.setMeasurement(value);

                Intent returnIntent = new Intent();
                returnIntent.putExtra("newexp",newexp);
                setResult(Activity.RESULT_OK,returnIntent);

                finish();

            }
        });

        final Button lbtn = findViewById(R.id.locationbtn3);
        lbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent (Measurement.this,LocationActivity.class);
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
