package com.cmput301w21t36.phenocount;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;

/**
 * This class represents Count trials
 */
public class CountActivity extends AppCompatActivity {
    Count trial;
    Experiment newexp;//defining the Experiment object
    Boolean location=false;
    DecimalFormat numberFormat;
    TextView coordinates;
    //TrialManager trialManager = new TrialManager(); //??
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // recieving intent object
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trial_count);
        numberFormat = new DecimalFormat("#.0000");

        newexp = (Experiment) getIntent().getSerializableExtra("experiment");//defining the Experiment object
        trial = new Count(newexp.getOwner());
        trial.setType("Count");



        // Capture the layout's TextView and set the string as its text

        TextView desc = findViewById(R.id.desc2);
        desc.setText("Description:" + String.valueOf(newexp.getDescription()));

        TextView owner = findViewById(R.id.owner2);
        owner.setText("Owner:" + newexp.getOwner().getProfile().getUsername());

        TextView status = findViewById(R.id.status2);
        status.setText("Status:" + String.valueOf(newexp.getExpStatus()));

        TextView exptype= findViewById(R.id.exptype2);
        exptype.setText("Experiment Type: Count");

        TextView count = findViewById(R.id.thecount);
        count.setText("Count:"+String.valueOf(trial.getCount()));

        coordinates = findViewById(R.id.coordinates);
        coordinates.setText("Location : NOT ADDED");

        final Button recordcountbtn = findViewById(R.id.recordcountbtn);
        recordcountbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(location || !newexp.isRequireLocation()) {
                    Toast.makeText(
                            CountActivity.this,
                            "Count Recorded",
                            Toast.LENGTH_SHORT).show();

                    newexp.getTrials().add(trial);
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("experiment", newexp);
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }else {
                    Toast.makeText(
                            CountActivity.this,
                            "Please add a location first",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
        final Button countbtn = findViewById((R.id.addbtn));
        countbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(location || !newexp.isRequireLocation()) {
                    //increment successes
                    trial.isCount();
                    count.setText("Count: " + trial.getCount());

                }else {
                    Toast.makeText(
                            CountActivity.this,
                            "Please add a location first",
                            Toast.LENGTH_LONG).show();
                }
            }
        }
        );




        final Button lbtn = findViewById(R.id.locationbtn2);
        lbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent (CountActivity.this,MapsActivity.class);
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
                trial = (Count) data.getSerializableExtra("trial_obj");


                if(trial.getLatitude() == 200 && trial.getLongitude() == 200) //location has not been added as these values can never be achieved.
                    coordinates.setText("Location : NOT ADDED");
                else
                    coordinates.setText("Location : ("+numberFormat.format(trial.getLatitude())+","+numberFormat.format(trial.getLongitude())+")");
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                System.out.println("No Data");
            }
        }
    }

}

