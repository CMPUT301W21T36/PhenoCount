package com.cmput301w21t36.phenocount;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

/**
 * This model class passes a trials list to TrialAdapter to display results of the trials conducted
 * It also starts an intent to the QR activity
 */
public class ResultsActivity extends AppCompatActivity {
    ListView trials;
    ArrayAdapter<Trial> trialAdapter;
    ArrayList<Trial> trialList;
    Experiment exp;//defining the Experiment object
    ImageView qr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        //initializing attributes
        trials =findViewById(R.id.trial_list);
        trialList = new ArrayList<>();
        qr = findViewById(R.id.qrView);

        //getting intent
        exp = (Experiment) getIntent().getSerializableExtra("experiment");//defining the Experiment object
        trialList = exp.getTrials();

        //initializing adapter
        trialAdapter = new TrialAdapter(this,trialList);
        trials.setAdapter(trialAdapter);

        trials.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                generateQr(position);
            }
        });
    }

    private void generateQr(int position) {
        qr.setVisibility(View.VISIBLE);
        trials.setVisibility(View.GONE);
        // Create QR Encoder with value to be encoded
        String type = exp.getExpType();
        String trial = "null";
        switch (type){
            case "Binomial":
                trial = String.valueOf(((Binomial) trialList.get(position)).getResult());
                break;
            case "Count":
                trial = String.valueOf(((Count) trialList.get(position)).getCount());
                break;
            case "Measurement":
                trial = String.valueOf(((Measurement) trialList.get(position)).getMeasurement());
                break;
            case "NonNegativeCount":
                trial = String.valueOf(((NonNegativeCount) trialList.get(position)).getValue());
                break;
        }
        QRGEncoder qrgEncoder = new QRGEncoder(trial, null, QRGContents.Type.TEXT, 500);
        try {
            // Getting QR as Bitmap
            Bitmap bitmap = qrgEncoder.getBitmap();
            // Set QR to ImageView
            qr.setImageBitmap(bitmap);
        } catch (Exception e) {
            Log.v("Exception", e.toString());
        }
    }
}
