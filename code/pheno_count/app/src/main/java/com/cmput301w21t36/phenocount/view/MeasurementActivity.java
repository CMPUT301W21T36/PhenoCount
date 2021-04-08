package com.cmput301w21t36.phenocount;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * This class represents MeasurementActivity trials and is part of the GUI
 * sets measurement to 0.0 if field is left blank
 */
public class MeasurementActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    Measurement trial;
    Experiment newexp;//defining the Experiment object
    Boolean location=false;
    TextView coordinates;
    DecimalFormat numberFormat;
    SharedPreferences sharedPrefs;
    Menu expMenu;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    androidx.appcompat.widget.Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // receiving intent object
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_PhenoCount);
        setContentView(R.layout.trial_measurement);
        numberFormat = new DecimalFormat("#.0000");

        navigationSettings();

        // receiving intent object
        newexp = (Experiment) getIntent().getSerializableExtra("experiment");//defining the Experiment object
        //setting user to owner of trial
        sharedPrefs = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        String username = sharedPrefs.getString("Username", "");
        String number = sharedPrefs.getString("Number", "");
        String UUID = sharedPrefs.getString("ID", "");
        Profile profile = new Profile(username,number);
        User user = new User(UUID,profile);
        trial = new Measurement(user);

        //newexp.getDates().add(trial.getDate());
        //setting type of trial
        trial.setType("Measurement");

        // Capture the layout's TextView and set the string as its text

        TextView expName = findViewById(R.id.toolbar_title);
        expName.setText(newexp.getName());

/*        TextView desc = findViewById(R.id.desc3);
        desc.setText("" + String.valueOf(newexp.getDescription()));

        TextView owner = findViewById(R.id.owner3);
        owner.setText("Owner: " + newexp.getOwner().getProfile().getUsername());

        TextView status = findViewById(R.id.status3);
        status.setText("Status:" + String.valueOf(newexp.getExpStatus()));

        TextView exptype= findViewById(R.id.exptype3);
        exptype.setText("Type: Measurement");*/

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
                intent = new Intent(MeasurementActivity.this,MainActivity.class);
                break;
            case R.id.nav_search:
                intent = new Intent(MeasurementActivity.this,SearchingActivity.class);
                break;
            case R.id.nav_user:
                intent = new Intent(MeasurementActivity.this,ProfileActivity.class);
                intent.putExtra("UUID",UUID);
                break;
            case R.id.nav_add:
                intent = new Intent(MeasurementActivity.this,PublishExperimentActivity.class);
                intent.putExtra("AutoId",UUID);
                intent.putExtra("mode",0);
                break;
            case R.id.nav_sub_exp:
                intent = new Intent(MeasurementActivity.this,ShowSubscribedListActivity.class);
                intent.putExtra("owner",UUID);
                break;

        }

        startActivity(intent);
        return true;
    }
}
