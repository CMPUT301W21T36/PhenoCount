// references: BrainCrash,2011-09-03,CC BY-SA 3.0, https://stackoverflow.com/a/6932112
package com.cmput301w21t36.phenocount;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * This activity deals with displaying the contents of an experiment
 * To access this activity: Open the app -> click on the listView
 * or the experiment
 * @see MainActivity
 */
public class DisplayExperimentActivity extends AppCompatActivity implements ProfileFragment.OnFragmentInteractionListener, NavigationView.OnNavigationItemSelectedListener {
    private Experiment exp; // catch object passed from mainlist
    FirebaseFirestore db;
    private final String TAG = "PhenoCount";
    private String username;
    private String UUID;
    private ExpManager expManager;
    Menu expMenu;
    TextView expStatus;
    CollectionReference collectionReference;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    androidx.appcompat.widget.Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_PhenoCount);
        setContentView(R.layout.activity_experiment_display);
        navigationSettings();
        //getSupportActionBar().setTitle("Experiment Info");

        TextView toolBarTitle = (TextView)findViewById(R.id.toolbar_title);
        toolBarTitle.setText("Experiment Info");

        exp = (Experiment) getIntent().getSerializableExtra("experiment");//defining the Experiment object
        db = FirebaseFirestore.getInstance();
        collectionReference = db.collection("Experiment");
        SharedPreferences sharedPrefs = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        sharedPrefs = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        username = sharedPrefs.getString("Username", "");
        UUID = sharedPrefs.getString("ID", "");


        TextView expName = findViewById(R.id.nameTextView);
        TextView expDesc = findViewById(R.id.descTextView);
        TextView expOwner = findViewById(R.id.ownerTextView);
        TextView expRegion = findViewById(R.id.regionTextView);
        TextView expMinTrial = findViewById(R.id.minTrialView);
        expStatus = findViewById(R.id.statusTextView);
        TextView expType = findViewById(R.id.expTypeText);
        TextView expReqLoc = findViewById(R.id.reqLocText);


        expName.setText(exp.getName());
        expDesc.setText(exp.getDescription());
        expOwner.setText(exp.getOwner().getProfile().getUsername());
        expRegion.setText(exp.getRegion());
        expMinTrial.setText(Integer.toString(exp.getMinimumTrials()));
        expType.setText(exp.getExpType());
        String mStat = "" ;
        switch(exp.getExpStatus()){
            case 1:
                mStat = "Published";
                break;
            case 2:
                mStat= "Ended";
                break;
            case 3:
                mStat = "Unpublished";
                break;
            default:
                mStat= "Added";
        }
        expStatus.setText(mStat);
        // Adding icon programmatically : BrainCrash,2011-09-03,CC BY-SA 3.0, https://stackoverflow.com/a/6932112
        if(exp.isRequireLocation()== true) {
            expReqLoc.setText(" REQUIRED");
            expReqLoc.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_warning, 0, 0, 0);
        }
        else {expReqLoc.setText("NOT REQUIRED");}

        final Button camerabtn = findViewById((R.id.camerabtn));
        camerabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DisplayExperimentActivity.this, ScanQRActivity.class);
                i.putExtra("experiment", exp);
                startActivityForResult(i, 1);
            }
        });

        final Button mapsBtn = findViewById((R.id.mapsBtn));
        mapsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DisplayExperimentActivity.this, GifActivity.class);
                i.putExtra("gifTrials",exp.getTrials());
                startActivity(i);
            }
        });

        final Button histogramBtn = findViewById(R.id.histogramButton);
        histogramBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DisplayExperimentActivity.this, HistogramActivity.class);
                i.putExtra("experiment", exp);
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.example_menu, menu);
        expMenu = menu;
        menuOpt();
        return true;
    }

    void menuOpt() {
        expMenu.findItem(R.id.ownerAction).setVisible(true);
        expMenu.findItem(R.id.unpublishButton).setEnabled(true);
        expMenu.findItem(R.id.endButton).setEnabled(true);
        expMenu.findItem(R.id.addTrialButon).setEnabled(true);
        expMenu.findItem(R.id.subscribeButton).setEnabled(true);

        if(!(UUID.equals(exp.getOwner().getUID()))){
            expMenu.findItem(R.id.ownerAction).setVisible(false);
        }
        if (exp.getExpStatus()==3){
            expMenu.findItem(R.id.unpublishButton).setEnabled(false);
        }
        if (exp.getExpStatus()==2){
            expMenu.findItem(R.id.endButton).setEnabled(false);
            expMenu.findItem(R.id.addTrialButon).setEnabled(false);
        }
        if (exp.getSubscribers().contains(UUID)){
            expMenu.findItem(R.id.subscribeButton).setEnabled(false);
        }
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.addTrialButon) {
            if (exp.getExpType().equals("Binomial")) {
                Intent bintent = new Intent(DisplayExperimentActivity.this, BinomialActivity.class);
                bintent.putExtra("experiment", exp);
                int LAUNCH_SECOND_ACTIVITY = 1;
                startActivityForResult(bintent, LAUNCH_SECOND_ACTIVITY);
            }
            if (exp.getExpType().equals("Count")) {
                Intent cintent = new Intent(DisplayExperimentActivity.this, CountActivity.class);
                cintent.putExtra("experiment", exp);
                int LAUNCH_SECOND_ACTIVITY = 1;
                startActivityForResult(cintent, LAUNCH_SECOND_ACTIVITY);
            }
            if (exp.getExpType().equals("Measurement")) {
                Intent mintent = new Intent(DisplayExperimentActivity.this, MeasurementActivity.class);
                mintent.putExtra("experiment", exp);
                int LAUNCH_SECOND_ACTIVITY = 1;
                startActivityForResult(mintent, LAUNCH_SECOND_ACTIVITY);
            }
            if (exp.getExpType().equals("NonNegativeCount")) {
                Intent nintent = new Intent(DisplayExperimentActivity.this, NonNegativeCountActivity.class);
                nintent.putExtra("experiment", exp);
                int LAUNCH_SECOND_ACTIVITY = 1;
                startActivityForResult(nintent, LAUNCH_SECOND_ACTIVITY);
            }
        } else if (item.getItemId() == R.id.item3) {
            Intent dintent = new Intent(DisplayExperimentActivity.this, DiscussionActivity.class);
            dintent.putExtra("experiment", exp);
            int LAUNCH_SECOND_ACTIVITY = 1;
            startActivity(dintent);
        } else if (item.getItemId() == R.id.item4) {
            Intent tintent = new Intent(DisplayExperimentActivity.this, ResultsActivity.class);
            tintent.putExtra("experiment", exp);
            //System.out.println("IN DISPLAY EXP ACTIVITY "+ new Date(exp.getTrials().get(0).getDate()));
            int LAUNCH_THIRD_ACTIVITY = 3;
            startActivityForResult(tintent,LAUNCH_THIRD_ACTIVITY);
        } else if (item.getItemId() == R.id.subscribeButton) {
            ArrayList expSubList = exp.getSubscribers();
            expSubList.add(UUID);
            HashMap<String, Object> data = new HashMap<>();
            String id = exp.getExpID();
            data.put("sub_list", expSubList);

            collectionReference
                    .document(id)
                    .update(data)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // These are a method which gets executed when the task is succeeded
                            Log.d(TAG, "Data has been updated successfully!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // These are a method which gets executed if there’s any problem
                            Log.d(TAG, "Data could not be updated!" + e.toString());
                        }
                    });
            menuOpt();

        } else if (item.getItemId() == R.id.unpublishButton) {
            AlertMsg confirmMsg = new AlertMsg(this, "Conformation",
                    "Please confirm if you want to unpublish this experiment",1);

            Button confirmButton=confirmMsg.alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
            confirmButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // this query updates the unpublish status
                    db.collection("Experiment").document(exp.getExpID())
                            .update("status", "3");
                    exp.setExpStatus(3);
                    menuOpt();
                    expStatus.setText("Unpublished");
                    confirmMsg.cancelDialog();
                }
            });
        } else if (item.getItemId() == R.id.endButton){
            AlertMsg confirmMsg = new AlertMsg(this, "Conformation",
                    "Please confirm if you want to end this experiment",1);

            Button confirmButton=confirmMsg.alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
            confirmButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    db.collection("Experiment").document(exp.getExpID())
                            .update("status", "2");
                    exp.setExpStatus(2);
                    menuOpt();
                    expStatus.setText("Ended");
                    //confirmMsg.alertDialog.cancel();
                    confirmMsg.cancelDialog();
                }
            });
        }else if (item.getItemId() == R.id.editButton){
            AlertMsg confirmMsg = new AlertMsg(this, "Conformation",
                    "Please confirm if you want to edit this experiment",1);

            Button confirmButton=confirmMsg.alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
            confirmButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent eIntent = new Intent(DisplayExperimentActivity.this, PublishExperimentActivity.class);
                    eIntent.putExtra("experiment", exp);
                    eIntent.putExtra("mode", 1);
                    startActivityForResult(eIntent, 1);
                    confirmMsg.cancelDialog();
                }
            });
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    //Sends the experiment object and retrieves the updated object
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int LAUNCH_SECOND_ACTIVITY = 1;
        int LAUNCH_THIRD_ACTIVITY = 3;
        if (requestCode == LAUNCH_SECOND_ACTIVITY) {
            if(resultCode == Activity.RESULT_OK){
                exp = (Experiment) data.getSerializableExtra("experiment");
                SharedPreferences sharedPrefs = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
                sharedPrefs = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
                username = sharedPrefs.getString("Username", "");
                UUID = sharedPrefs.getString("ID", "");
                expManager = new ExpManager();
                expManager.updateTrialData(db,exp,username);
            }
        }
        if(requestCode == LAUNCH_THIRD_ACTIVITY){
                System.out.println("Ignoring");
                exp = (Experiment) data.getSerializableExtra("experiment");
                expManager = new ExpManager();
                for(Trial trial:exp.getTrials()){
                    System.out.println("Status: "+trial.getStatus());
                }
                expManager.ignoreTrial(exp);

        }
        if (resultCode == Activity.RESULT_CANCELED) {
            System.out.println("No Data");
        }
    }

    public void showProfile(View v){
        String username = exp.getOwner().getProfile().getUsername();
        String phone = exp.getOwner().getProfile().getPhone();
        System.out.println(username);
        System.out.println(phone);
        new ProfileFragment(username, phone).show(getSupportFragmentManager(), "SHOW_PROFILE");

    }

    @Override
    public void onOkPressedAdd(String text) {

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

        Intent intent = new Intent();
        switch (item.getItemId()){
            case R.id.nav_my_exp:
                intent = new Intent(DisplayExperimentActivity.this,MainActivity.class);
                break;
            case R.id.nav_search:
                intent = new Intent(DisplayExperimentActivity.this,SearchingActivity.class);
                break;
            case R.id.nav_user:
                intent = new Intent(DisplayExperimentActivity.this,ProfileActivity.class);
                intent.putExtra("UUID",UUID);
                break;
            case R.id.nav_add:
                intent = new Intent(DisplayExperimentActivity.this,PublishExperimentActivity.class);
                intent.putExtra("AutoId",UUID);
                intent.putExtra("mode",0);
                break;
            case R.id.nav_sub_exp:
                intent = new Intent(DisplayExperimentActivity.this,ShowSubscribedListActivity.class);
                intent.putExtra("owner",UUID);
                break;

        }

        startActivity(intent);
        return true;
    }
}
