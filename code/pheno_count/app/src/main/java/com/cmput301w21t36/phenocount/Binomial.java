package com.cmput301w21t36.phenocount;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class Binomial extends AppCompatActivity {
    Trial trial;
    Experiment newexp;//defining the Experiment object
    Boolean location=false;
    DecimalFormat numberFormat;

    private final String TAG = "PhenoCount";

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // recieving intent object
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trial_binomial);
        numberFormat = new DecimalFormat("#.0000");

        newexp = (Experiment) getIntent().getSerializableExtra("experiment");//defining the Experiment object
        trial = new Trial(newexp.getOwner());
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

        TextView coordinates= findViewById(R.id.coordinates);
        if(trial.getLatitude() == 200 && trial.getLongitude() == 200) //location has not been added as these values can never be achieved.
            coordinates.setText("Location : NOT ADDED");
        else
            coordinates.setText("Location : ("+numberFormat.format(trial.getLatitude())+","+numberFormat.format(trial.getLongitude())+")");





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
                        Binomial.this,
                        "Success Recorded",
                        Toast.LENGTH_LONG).show();
                finish(); // closes this activity
            }else{
                    Toast.makeText(
                            Binomial.this,
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
                            Binomial.this,
                            "Failure Recorded",
                            Toast.LENGTH_LONG).show();

                    finish(); // closes this activity
                }else{
                    Toast.makeText(
                            Binomial.this,
                            "Please add a location first",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        final Button lbtn = findViewById(R.id.locationbtn);
        lbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent (Binomial.this,MapsActivity.class);
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
                Trial newtrial = (Trial) data.getSerializableExtra("trial_obj");
                trial = newtrial;
                TextView coordinates= findViewById(R.id.coordinates);
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

