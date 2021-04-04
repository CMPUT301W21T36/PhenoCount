package com.cmput301w21t36.phenocount;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

/**
 * This model class passes a trials list to TrialAdapter to display results of the trials conducted
 * It also generates and displays a Qr code when a trial is clicked in the list
 */
public class ResultsActivity extends AppCompatActivity {
    ListView trials;
    ArrayAdapter<Trial> trialAdapter;
    ArrayList<Trial> trialList;
    ArrayList<String> blacklist;
    Experiment exp;//defining the Experiment object
    ImageView qr;
    Button statsButton;
    Menu expMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        //initializing attributes
        trials =findViewById(R.id.trial_list);
        trialList = new ArrayList<>();
        blacklist = new ArrayList<>();
        qr = findViewById(R.id.qrView);

        //getting intent
        exp = (Experiment) getIntent().getSerializableExtra("experiment");//defining the Experiment object
        trialList = exp.getTrials();

        //ignoring trials of the user
        for (Trial trial:trialList){
            if(!trial.getStatus()) {
                String UUID = trial.getOwner().getUID();
                blacklist.add(UUID);
            }
        }
        for (Trial trial : trialList){
            if(blacklist.contains(trial.getOwner().getUID())){
                trial.setStatus(false);
            }
        }
        for (Trial trial:trialList){
            System.out.println("Status: "+trial.getStatus());
        }

        SharedPreferences sharedPrefs = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        sharedPrefs = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        String UUID = sharedPrefs.getString("ID", "");


        //initializing adapter
        trialAdapter = new TrialAdapter(this,trialList,UUID,exp.getOwner().getUID());
        trials.setAdapter(trialAdapter);

        trials.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                generateQr(position);
            }
        });

        statsButton = findViewById(R.id.statsButton);
        statsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openStats();
            }
        });
        Intent returnIntent = new Intent();
        returnIntent.putExtra("experiment", exp);
        setResult(Activity.RESULT_OK,returnIntent);
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

    /**
     * This method is called when the statsButton is clicked and Switches ResultsActivity to
     * StatsActivity
     */
    public void openStats() {
        Intent intent = new Intent(this, StatsActivity.class);
        intent.putExtra("experiment",exp);
        startActivity(intent);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.general_menu, menu);
        expMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.myList) {
            Intent intent = new Intent(ResultsActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("experiment", exp);
            startActivity(intent);
        }
        if (item.getItemId() == R.id.search) {
            Intent intent = new Intent(ResultsActivity.this, SearchingActivity.class);
            intent.putExtra("experiment", exp);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }


}
