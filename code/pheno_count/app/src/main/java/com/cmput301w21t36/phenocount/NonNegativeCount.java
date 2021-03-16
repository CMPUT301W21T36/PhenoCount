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

import java.util.ArrayList;

public class NonNegativeCount extends AppCompatActivity {
    Trial trial;
    Experiment newexp;//defining the Experiment object
    Boolean location=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // receiving intent object
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trial_non_negative_count);

        newexp = (Experiment) getIntent().getSerializableExtra("experiment");//defining the Experiment object
        trial = new Trial(newexp.getOwner());


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
                            NonNegativeCount.this,
                            "Count Recorded",
                            Toast.LENGTH_SHORT).show();
                    newexp.getTrials().add(trial);

                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("experiment", newexp);
                    setResult(Activity.RESULT_OK, returnIntent);

                    finish();
                }else{
                    Toast.makeText(
                            NonNegativeCount.this,
                            "Please add a location first",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        final Button lbtn = findViewById(R.id.locationbtn4);
        lbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent (NonNegativeCount.this,MapsActivity.class);
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
                Trial trial = (Trial) data.getSerializableExtra("trial_obj");
                newexp.getTrials().add(trial);

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                System.out.println("No Data");
            }
        }
    }
}
