package com.cmput301w21t36.phenocount;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
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
 * This class represents MeasurementActivity trials and is part of the GUI
 * sets measurement to 0.0 if field is left blank
 */
public class MeasurementActivity extends AppCompatActivity {
    Measurement trial;
    Experiment newexp;//defining the Experiment object
    Boolean location=false;
    TextView coordinates;
    DecimalFormat numberFormat;
    SharedPreferences sharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // receiving intent object
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trial_measurement);
        numberFormat = new DecimalFormat("#.0000");

        // receiving intent object
        newexp = (Experiment) getIntent().getSerializableExtra("experiment");//defining the Experiment object
        //setting user to owner of trial
        sharedPrefs = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        String username = sharedPrefs.getString("Username", "");
        String UUID = sharedPrefs.getString("ID", "");
        Profile profile = new Profile(username);
        User user = new User(UUID,profile);
        trial = new Measurement(user);

        newexp.getDates().add(trial.getDate().getTime());
        //setting type of trial
        trial.setType("Measurement");

        // Capture the layout's TextView and set the string as its text
        TextView desc = findViewById(R.id.desc3);
        desc.setText("Description:" + String.valueOf(newexp.getDescription()));

        TextView owner = findViewById(R.id.owner3);
        owner.setText("Owner:" + newexp.getOwner().getProfile().getUsername());

        TextView status = findViewById(R.id.status3);
        status.setText("Status:" + String.valueOf(newexp.getExpStatus()));

        TextView exptype= findViewById(R.id.exptype3);
        exptype.setText("Experiment Type: MeasurementActivity");

        EditText measurement = findViewById(R.id.measurement_editText);

        //setting location coordinates
        coordinates = findViewById(R.id.coordinates);
        coordinates.setText("Location : NOT ADDED");

        final Button recordvbtn = findViewById((R.id.recordvbtn));
        recordvbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //checks if location is provided
                if(location || !newexp.isRequireLocation()) {
                    String temp = measurement.getText().toString();
                    float value = 0;
                    if (!"".equals(temp)) {
                        value = Float.parseFloat(temp);    //https://javawithumer.com/2019/07/get-value-edittext.html
                    }
                    trial.setMeasurement(value);
                    newexp.getTrials().add(trial);

                    Toast.makeText(
                            MeasurementActivity.this,
                            "Measurement Recorded",
                            Toast.LENGTH_SHORT).show();

                    //passing the experiment object back to DisplayExperimentActivity
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("experiment", newexp);
                    setResult(Activity.RESULT_OK, returnIntent);

                    finish();
                }else{  Toast.makeText(
                        MeasurementActivity.this,
                        "Please add a location first",
                        Toast.LENGTH_LONG).show();
                }

            }
        });

        final Button lbtn = findViewById(R.id.locationbtn3);
        lbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //passing trial object to get location updated
                Intent intent = new Intent (MeasurementActivity.this,MapsActivity.class);
                intent.putExtra("trial_obj",trial);

                int LAUNCH_SECOND_ACTIVITY = 1;
                startActivityForResult(intent,LAUNCH_SECOND_ACTIVITY); }
        });

        final Button cameraButton = findViewById(R.id.cameraButton);
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MeasurementActivity.this, ScanBarcodeActivity.class);
                startActivityForResult(i, 1);
            }
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
                //catching the trial object back
                trial = (Measurement) data.getSerializableExtra("trial_obj");
                if (trial != null) {
                    if(trial.getLatitude() == 200 && trial.getLongitude() == 200) //location has not been added as these values can never be achieved.
                        coordinates.setText("Location : NOT ADDED");
                    else
                        coordinates.setText("Location : ("+numberFormat.format(trial.getLatitude())+","+numberFormat.format(trial.getLongitude())+")");

                } else {
                    String scannedText = data.getSerializableExtra("scannedText").toString();
                    EditText input = findViewById(R.id.measurement_editText);
                    input.setText(scannedText, TextView.BufferType.EDITABLE);
                }

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                System.out.println("No Data");
            }
        }
    }
}
