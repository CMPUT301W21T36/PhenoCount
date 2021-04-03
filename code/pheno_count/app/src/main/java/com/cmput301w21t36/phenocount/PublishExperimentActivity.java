// References:
// For displaying red astrisk next to must fields(in string.xml):
// Gabriele Mariotti, 2020-05-05, CC BY-SA 4.0,https://stackoverflow.com/a/61622809
//
//Android Developers, 2020-11-18, Apache 2.0, https://developer.android.com/guide/topics/ui/controls/radiobutton#java
//
//Arash GM,2013-01-01, CC BY-SA 4.0,https://stackoverflow.com/a/14112280

package com.cmput301w21t36.phenocount;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This activity deals with publishing an experiment
 * To access this activity: Open the app -> click on the addBotton
 * in the bottom of the main screen/activity
 * @see MainActivity
 */
public class PublishExperimentActivity extends AppCompatActivity {
    FirebaseFirestore db;
    TextView expName;
    TextView expDesc;
    TextView expRegion;
    String expType="";
    TextView expNum;
    CheckBox expGeoLoc;
    String ownerName;
    String owner;
    int mode;
    Experiment exp;
    RadioGroup radioGroup;
    private final String TAG = "PhenoCount";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experiment_publish);
        getSupportActionBar().setTitle("Publish an Experiment");


        expName = findViewById(R.id.expName);
        expDesc = findViewById(R.id.expDesc);
        expRegion = findViewById(R.id.expRegion);
        expNum = findViewById(R.id.expNum);
        expGeoLoc = findViewById(R.id.geoCheckBox);

        Bundle bundle = getIntent().getExtras();
        mode = bundle.getInt("mode");
        if (mode == 0) {
            ownerName = bundle.get("username").toString();
            owner = bundle.get("AutoId").toString();
        }


        if(mode == 1) {
            getSupportActionBar().setTitle("Edit Experiment");
            exp = (Experiment) bundle.get("experiment");
            displayExp();
        }

        expDesc.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (expDesc.getText().toString().isEmpty()) {
                    expDesc.setError("Required field Description");
                } else {
                    expDesc.setError(null);
                }
            }
        });
    }

    //Android Developers, 2020-11-18, Apache 2.0, https://developer.android.com/guide/topics/ui/controls/radiobutton#java
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radioBinomial:
                if (checked)
                    expType="Binomial";
                break;
            case R.id.radioCount:
                if (checked)
                    expType="Count";
                break;
            case R.id.radioInt:
                if (checked)
                    expType="NonNegativeCount";
                break;
            case R.id.radioMeasure:
                if (checked)
                    expType="Measurement";
                break;
        }
    }

    public void displayExp(){
        radioGroup = findViewById(R.id.radio1);
        expName.setText(exp.getName());
        expDesc.setText(exp.getDescription());
        expRegion.setText(exp.getRegion());
        expNum.setText(Integer.toString(exp.getMinimumTrials()));
        expGeoLoc.setChecked(false);
        if (exp.isRequireLocation()) {
            expGeoLoc.setChecked(true);
        }
        expType=exp.getExpType();
        if (exp.getExpType().equals("Binomial")){
            radioGroup.check(R.id.radioBinomial);
        }else if (exp.getExpType().equals("Count")){
            radioGroup.check(R.id.radioCount);
        }else if (exp.getExpType().equals("NonNegativeCount")){
            radioGroup.check(R.id.radioInt);
        }else if (exp.getExpType().equals("Measurement")){
            radioGroup.check(R.id.radioMeasure);
        }

    }

    /**
     * This method adds the experiments to the database (firestore)
     * in other words its publishes an experiment when the ok button is pressed
     * @param view
     */
    public void toAdd(View view) {

        db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection("Experiment");

        boolean reqLoc = false;
        ArrayList sList = new ArrayList();


        final String desc = expDesc.getText().toString();

        // To make sure that the required fields are not left empty
        int proceed =1;
        if (!(desc.length()>0)){
            proceed=0;
        }
        if (!(expType.length()>0)){
            proceed=0;
        }
        if (proceed > 0) {

            if (expType.length() > 0 && desc.length() > 0) {
                if (mode == 0) {
                    HashMap<String, Object> data = new HashMap<>();
                    String id = db.collection("Experiment").document().getId();
                    data.put("name", expName.getText().toString());
                    data.put("description", desc);
                    data.put("type", expType);
                    data.put("region", expRegion.getText().toString());
                    data.put("minimum_trials", expNum.getText().toString());
                    data.put("owner", owner);
                    data.put("status", "1");
                    data.put("owner_name", ownerName);
                    data.put("require_geolocation", "NO");
                    data.put("sub_list", sList);
                    if (expGeoLoc.isChecked()) {
                        data.put("require_geolocation", "YES");
                    }

                    collectionReference
                            .document(id)
                            .set(data)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // These are a method which gets executed when the task is succeeded
                                    Log.d(TAG, "Data has been added successfully!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // These are a method which gets executed if there’s any problem
                                    Log.d(TAG, "Data could not be added!" + e.toString());
                                }
                            });
                }
                if (mode == 1) {
                    HashMap<String, Object> data = new HashMap<>();
                    String id = exp.getExpID();
                    data.put("name", expName.getText().toString());
                    data.put("description", desc);
                    data.put("type", expType);
                    data.put("region", expRegion.getText().toString());
                    data.put("minimum_trials", expNum.getText().toString());
                    data.put("owner", exp.getOwner().getUID());
                    data.put("status", Integer.toString(exp.getExpStatus()));
                    data.put("owner_name", exp.getOwner().getProfile().getUsername());
                    data.put("require_geolocation", "NO");
                    if (expGeoLoc.isChecked()) {
                        reqLoc = true;
                        data.put("require_geolocation", "YES");
                    }

                    collectionReference
                            .document(id)
                            .update(data)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // These are a method which gets executed when the task is succeeded
                                    Log.d(TAG, "Data has been editted successfully!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // These are a method which gets executed if there’s any problem
                                    Log.d(TAG, "Data could not be editted!" + e.toString());
                                }
                            });
                }
            }
            finish();
            if (mode == 0) {
                Toast.makeText(this, "Your Experiment is published!!",
                        Toast.LENGTH_SHORT).show();
            }else{
                Experiment expObj = new Experiment(expName.getText().toString(), desc, expRegion.getText().toString(),
                        expType, Integer.parseInt(expNum.getText().toString()), reqLoc, exp.getExpStatus(),
                        exp.getExpID());
                expObj.setOwner(exp.getOwner());
                Intent intent = new Intent (this,DisplayExperimentActivity.class);
                intent.putExtra("experiment",expObj);
                //Arash GM,2013-01-01, CC BY-SA 4.0,https://stackoverflow.com/a/14112280
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                Toast.makeText(this, "Your Experiment is updated!!",
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            AlertMsg Altmsg = new AlertMsg(this, "Error Message",
                    "Description/Type of Experiment is Required, TRY AGAIN!!");

        }
    }

    /**
     * To abort the PublishExperimentActivity on cancel button press
     * @param view
     */
    public void toCancel(View view) {
        finish();
    }
}