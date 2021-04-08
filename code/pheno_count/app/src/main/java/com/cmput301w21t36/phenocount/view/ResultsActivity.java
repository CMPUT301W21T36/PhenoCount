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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import java.util.ArrayList;
import java.util.Date;
import com.cmput301w21t36.phenocount.Trial;
import com.google.android.material.navigation.NavigationView;


import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

/**
 * This model class passes a trials list to TrialAdapter to display results of the trials conducted
 * It also generates and displays a Qr code when a trial is clicked in the list
 */
public class ResultsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    ListView trials;
    ArrayAdapter<Trial> trialAdapter;
    ArrayList<Trial> trialList;
    ArrayList<String> blacklist;
    Experiment exp;//defining the Experiment object
    ImageView qr;
    Button statsButton;
    ImageButton histogramButton;
    Menu expMenu;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    androidx.appcompat.widget.Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_PhenoCount);
        setContentView(R.layout.activity_results);

        navigationSettings();

        //initializing attributes
        trials =findViewById(R.id.trial_list);
        trialList = new ArrayList<>();
        blacklist = new ArrayList<>();
        qr = findViewById(R.id.qrView);
        histogramButton = findViewById(R.id.histogramButtonn);

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

        if(trialList.size() == 0){
            trials.setBackgroundResource(R.drawable.hint_trial);
        }else{
            trials.setBackgroundResource(R.drawable.hint_white);
        }

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

        ImageButton plotsButton =(ImageButton)findViewById(R.id.plotButton);
        plotsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //System.out.println("IN RESULTS ACTIVITY "+ new Date(exp.getTrials().get(0).getDate()));
                Intent intent = new Intent(ResultsActivity.this, PlotsActivity.class );
                intent.putExtra("exp", exp);
                startActivity(intent);
            }
        });

        histogramButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ResultsActivity.this, HistogramActivity.class);
                i.putExtra("experiment", exp);
                startActivity(i);
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

    /**
     * This method is called when the statsButton is clicked and Switches ResultsActivity to
     * StatsActivity
     */
    public void openStats() {
        Intent intent = new Intent(this, StatsActivity.class);
        intent.putExtra("experiment",exp);
        startActivity(intent);
    }

    public void navigationSettings(){
        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        navigationView.bringToFront();
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        SharedPreferences sharedPrefs = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        String UUID = sharedPrefs.getString("ID", "");
        Intent intent = new Intent();
        switch (item.getItemId()){
            case R.id.nav_my_exp:
                intent = new Intent(ResultsActivity.this,MainActivity.class);
                break;
            case R.id.nav_search:
                intent = new Intent(ResultsActivity.this,SearchingActivity.class);
                break;
            case R.id.nav_user:
                intent = new Intent(ResultsActivity.this,ProfileActivity.class);
                intent.putExtra("UUID",UUID);
                break;
            case R.id.nav_add:
                intent = new Intent(ResultsActivity.this,PublishExperimentActivity.class);
                intent.putExtra("AutoId",UUID);
                intent.putExtra("mode",0);
                break;
            case R.id.nav_sub_exp:
                intent = new Intent(ResultsActivity.this,ShowSubscribedListActivity.class);
                intent.putExtra("owner",UUID);
                break;

        }

        startActivity(intent);
        return true;
    }


}
