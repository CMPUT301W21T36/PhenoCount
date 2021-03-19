package com.cmput301w21t36.phenocount;

import android.annotation.SuppressLint;
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
 * This class represents binomial trials
 */
public class BinomialActivity extends AppCompatActivity {
    Binomial trial;
    Experiment newexp;//defining the Experiment object
    Boolean location=false;
    DecimalFormat numberFormat;
    TextView coordinates;
    //TrialManager trialManager;

    private final String TAG = "PhenoCount";

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // recieving intent object
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trial_binomial);
        numberFormat = new DecimalFormat("#.0000");

        newexp = (Experiment) getIntent().getSerializableExtra("experiment");//defining the Experiment object
        trial = new Binomial(newexp.getOwner());
        trial.setType("Binomial");



        // Capture the layout's TextView and set the string as its text

        TextView desc = findViewById(R.id.desc1);
        desc.setText("Description:" + String.valueOf(newexp.getDescription()));

        TextView owner = findViewById(R.id.owner1);
        owner.setText("Owner:" + newexp.getOwner().getProfile().getUsername());

        TextView status = findViewById(R.id.status1);
        status.setText("Status:" + String.valueOf(newexp.getExpStatus()));

        TextView exptype= findViewById(R.id.exptype1);
        exptype.setText("Experiment Type: Binomial Trial");

        coordinates= findViewById(R.id.coordinates);
        coordinates.setText("Location : NOT ADDED");



        final Button sbtn = findViewById((R.id.successbtn));
        sbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(location || !newexp.isRequireLocation()){
                //increment successes
                trial.isSuccess();
                //trials.add(trial);
                newexp.getTrials().add(trial);
                Intent returnIntent = new Intent();
                returnIntent.putExtra("experiment",newexp);
                setResult(Activity.RESULT_OK,returnIntent);
                Toast.makeText(
                        BinomialActivity.this,
                        "Success Recorded",
                        Toast.LENGTH_LONG).show();
                finish(); // closes this activity
            }else{
                    Toast.makeText(
                            BinomialActivity.this,
                            "Please add a location first",
                            Toast.LENGTH_LONG).show();
                }

            }
        });

        final Button fbtn = findViewById((R.id.failbtn));
        fbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(location || !newexp.isRequireLocation()) {
                    //increments fails
                    trial.isFailure();
                    newexp.getTrials().add(trial);

                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("experiment", newexp);
                    setResult(Activity.RESULT_OK, returnIntent);

                    Toast.makeText(
                            BinomialActivity.this,
                            "Failure Recorded",
                            Toast.LENGTH_LONG).show();

                    finish(); // closes this activity
                }else{
                    Toast.makeText(
                            BinomialActivity.this,
                            "Please add a location first",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        final Button lbtn = findViewById(R.id.locationbtn);
        lbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent (BinomialActivity.this,MapsActivity.class);
                intent.putExtra("trial_obj",trial);

                int LAUNCH_SECOND_ACTIVITY = 1;
                startActivityForResult(intent,LAUNCH_SECOND_ACTIVITY); }
        });


    }




    @Override
    //Sends the experiment object and retrieves the updated
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int LAUNCH_SECOND_ACTIVITY = 1;
        if (requestCode == LAUNCH_SECOND_ACTIVITY) {
            if(resultCode == Activity.RESULT_OK){
                location = true;
                trial = (Binomial) data.getSerializableExtra("trial_obj");


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

