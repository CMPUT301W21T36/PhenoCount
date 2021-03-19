package com.cmput301w21t36.phenocount;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * This class represents NonNegativeCountActivity trials
 */
public class NonNegativeCountActivity extends AppCompatActivity {
    NonNegativeCount trial;
    Experiment newexp;//defining the Experiment object
    Boolean location=false;
    TextView coordinates;
    DecimalFormat numberFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // receiving intent object
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trial_non_negative_count);

        newexp = (Experiment) getIntent().getSerializableExtra("experiment");//defining the Experiment object
        trial = new NonNegativeCount(newexp.getOwner());
        trial.setType("NonNegativeCountActivity");

        numberFormat = new DecimalFormat("#.0000");


        // Capture the layout's TextView and set the string as its text

        TextView desc = findViewById(R.id.desc4);
        desc.setText("Description:" + String.valueOf(newexp.getDescription()));

        TextView owner = findViewById(R.id.owner4);
        owner.setText("Owner:" + newexp.getOwner().getProfile().getUsername());

        TextView status = findViewById(R.id.status4);
        status.setText("Status:" + String.valueOf(newexp.getExpStatus()));

        TextView exptype= findViewById(R.id.exptype4);
        exptype.setText("Experiment Type: Non-Negative Count");

        EditText count = findViewById(R.id.count_editText);

        coordinates= findViewById(R.id.coordinates);
        coordinates.setText("Location : NOT ADDED");


        final Button recordcbtn = findViewById((R.id.recordcbtn));
        recordcbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(location || !newexp.isRequireLocation()) {

                    String temp = count.getText().toString();
                    int value = 0;
                    if (!"".equals(temp)) {
                        value = Integer.parseInt(temp);
                    }
                    trial.setValue(value);
                    newexp.getTrials().add(trial);
                    Toast.makeText(
                            NonNegativeCountActivity.this,
                            "Count Recorded",
                            Toast.LENGTH_SHORT).show();
                    newexp.getTrials().add(trial);

                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("experiment", newexp);
                    setResult(Activity.RESULT_OK, returnIntent);

                    finish();
                }else{
                    Toast.makeText(
                            NonNegativeCountActivity.this,
                            "Please add a location first",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        final Button lbtn = findViewById(R.id.locationbtn4);
        lbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent (NonNegativeCountActivity.this,MapsActivity.class);
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
                location = true;
                trial = (NonNegativeCount) data.getSerializableExtra("trial_obj");
                newexp.getTrials().add(trial);

                if(trial.getLatitude() == 200 && trial.getLongitude() == 200) //location has not been added as these values can never be achieved.
                    coordinates.setText("Location : NOT ADDED");
                else
                    coordinates.setText("Location : ("+numberFormat.format(trial.getLatitude())+","+numberFormat.format(trial.getLongitude())+")");
                //newexp.getTrials().add(trial);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                System.out.println("No Data");
            }
        }
    }
}
