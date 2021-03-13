package com.cmput301w21t36.phenocount;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class PublishExperimentActivity extends AppCompatActivity {
    FirebaseFirestore db;
    ExpManager manager;
    Experiment exp;
    TextView expName;
    TextView expDesc;
    TextView expRegion;
    String expType;
    TextView expNum;
    CheckBox expGeoLoc;

    private final String TAG = "PhenoCount";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experiment_publish);

    }

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

    //@Override
    public void toAdd(View view) {
        db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection("Experiment");
        //manager.addExperiment(exp);
        //expAdapter.notifyDataSetChanged();
        ////////// here
        expName = findViewById(R.id.expName);
        expDesc = findViewById(R.id.expDesc);
        //expType = findViewById(R.id.typeOfTrial);
        expRegion = findViewById(R.id.expRegion);
        expNum = findViewById(R.id.expNum);
        expGeoLoc = findViewById(R.id.geoCheckBox);


        //final String Type = expType;
        final String desc = expDesc.getText().toString();
        HashMap<String, String> data = new HashMap<>();
        if (expType.length() > 0 && desc.length() > 0) {
            String id = db.collection("Experiment").document().getId();
            data.put("name", expName.getText().toString());
            data.put("description", desc);
            data.put("type", expType);
            data.put("region", expRegion.getText().toString());
            data.put("minimum_trials", expNum.getText().toString());
            data.put("owner","TRY TRY");
            data.put("require_geolocation", "NO");
            if (expGeoLoc.isChecked()){
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

            ///////////// here end
        }
        finish();
        Toast.makeText(this, "Your Experiment is published!!", Toast.LENGTH_SHORT).show();

    }
    public void toCancel(View view) {
        finish();
    }
}