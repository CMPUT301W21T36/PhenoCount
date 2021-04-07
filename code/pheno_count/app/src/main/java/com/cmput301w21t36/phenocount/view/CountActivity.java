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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.text.DecimalFormat;

/**
 * This class represents Count trials and is part of the GUI
 */
public class CountActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    com.cmput301w21t36.phenocount.Count trial;
    com.cmput301w21t36.phenocount.Experiment newexp;//defining the Experiment object
    Boolean location=false;
    DecimalFormat numberFormat;
    TextView coordinates;
    SharedPreferences sharedPrefs;
    int qrCount = -1;
    Menu expMenu;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    androidx.appcompat.widget.Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_PhenoCount);
        setContentView(R.layout.trial_count);
        numberFormat = new DecimalFormat("#.0000");

        navigationSettings();

        // receiving intent object
        newexp = (com.cmput301w21t36.phenocount.Experiment) getIntent().getSerializableExtra("experiment");//defining the Experiment object

        // get the intent object from the Qr activity
        if (newexp == null) {
            newexp = (com.cmput301w21t36.phenocount.Experiment) getIntent().getSerializableExtra("QrExperiment");
            qrCount = 1;
        }

        //setting user to owner of trial
        sharedPrefs = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        String username = sharedPrefs.getString("Username", "");
        String number = sharedPrefs.getString("Number", "");
        String UUID = sharedPrefs.getString("ID", "");
        Profile profile = new Profile(username,number);
        User user = new User(UUID,profile);
        trial = new com.cmput301w21t36.phenocount.Count(user);
        //newexp.getDates().add(trial.getDate());
        //setting type of trial
        trial.setType("Count");

        // Capture the layout's TextView and set the string as its text
        TextView expName = findViewById(R.id.toolbar_title);
        expName.setText(newexp.getName());
        /*
        TextView desc = findViewById(R.id.desc2);
        desc.setText("" + String.valueOf(newexp.getDescription()));*/

/*
        TextView owner = findViewById(R.id.owner2);
        owner.setText("Owner: " + newexp.getOwner().getProfile().getUsername());

        TextView status = findViewById(R.id.status2);
        status.setText("Status:" + String.valueOf(newexp.getExpStatus()));

        TextView exptype= findViewById(R.id.exptype2);
        exptype.setText("Type: Count");
*/

        TextView count = findViewById(R.id.thecount);
        count.setText("Count:"+String.valueOf(trial.getCount()));

        coordinates = findViewById(R.id.coordinates);
        coordinates.setText("Location : NOT ADDED");

        final Button recordcountbtn = findViewById(R.id.recordcountbtn);
        recordcountbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //checks if location is provided
                if(location || !newexp.isRequireLocation()) {
                    trial.isCount();
                    count.setText("Count: " + trial.getCount());
                    Toast.makeText(
                            CountActivity.this,
                            "Count Recorded",
                            Toast.LENGTH_SHORT).show();

                    newexp.getTrials().add(trial);

                    //passing the experiment object back to DisplayExperimentActivity
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("experiment", newexp);
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }else {
                    Toast.makeText(
                            CountActivity.this,
                            "Please add a location first",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        if (qrCount != -1) {
            recordcountbtn.performClick();
        }

/*        final Button countbtn = findViewById((R.id.addbtn));
        countbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(location || !newexp.isRequireLocation()) {
                    //increment successes
                    trial.isCount();
                    count.setText("Count: " + trial.getCount());

                }else {
                    Toast.makeText(
                            CountActivity.this,
                            "Please add a location first",
                            Toast.LENGTH_LONG).show();
                }
            }
        }
        );*/

        final Button lbtn = findViewById(R.id.locationbtn2);
        lbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //passing trial object to get location updated
                Intent intent = new Intent (CountActivity.this, com.cmput301w21t36.phenocount.MapsActivity.class);
                intent.putExtra("trial_obj",trial);

                int LAUNCH_SECOND_ACTIVITY = 1;
                startActivityForResult(intent,LAUNCH_SECOND_ACTIVITY); }

        });

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
            Intent intent = new Intent(CountActivity.this, com.cmput301w21t36.phenocount.MainActivity.class);
            startActivity(intent);
        }
        if (item.getItemId() == R.id.search) {
            Intent intent = new Intent(CountActivity.this, com.cmput301w21t36.phenocount.SearchingActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
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
                trial = (com.cmput301w21t36.phenocount.Count) data.getSerializableExtra("trial_obj");

                if(trial.getLatitude() == 200 && trial.getLongitude() == 200) //location has not been added as these values can never be achieved.
                    coordinates.setText("Location : NOT ADDED");
                else
                    coordinates.setText("Location : ("+numberFormat.format(trial.getLatitude())+","+numberFormat.format(trial.getLongitude())+")");
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
                intent = new Intent(CountActivity.this, com.cmput301w21t36.phenocount.MainActivity.class);
                break;
            case R.id.nav_search:
                intent = new Intent(CountActivity.this, com.cmput301w21t36.phenocount.SearchingActivity.class);
                break;
            case R.id.nav_user:
                intent = new Intent(CountActivity.this, com.cmput301w21t36.phenocount.ProfileActivity.class);
                intent.putExtra("UUID",UUID);
                break;
            case R.id.nav_add:
                intent = new Intent(CountActivity.this, com.cmput301w21t36.phenocount.PublishExperimentActivity.class);
                intent.putExtra("AutoId",UUID);
                intent.putExtra("mode",0);
                break;
            case R.id.nav_sub_exp:
                intent = new Intent(CountActivity.this, com.cmput301w21t36.phenocount.ShowSubscribedListActivity.class);
                intent.putExtra("owner",UUID);
                break;

        }

        startActivity(intent);
        return true;
    }

}

