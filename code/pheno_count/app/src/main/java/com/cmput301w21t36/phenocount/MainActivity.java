package com.cmput301w21t36.phenocount;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * This is the MainActivity and the every first screen of the app
 * It has an add button to publish an experiment,
 * a search button to search the databse for experiments,
 * a profile button to view and/or edit the profile of the current user
 */
public class MainActivity extends AppCompatActivity {
    FirebaseFirestore db;
    ListView experiments;
    Button searchButton;
    Button profileButton;
    ArrayList<Experiment> expDataList;
    ArrayAdapter<Experiment> expAdapter;
    ExpManager manager = new ExpManager();
    DatabaseManager dbManager = new DatabaseManager();
    static final String AutoID = "ID";
    private String UUID;
    private String username;
    private String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("My Experiments");



        experiments = findViewById(R.id.expList);
        expDataList = new ArrayList<>();

        // To get instance of the database
        db = dbManager.getDb();

        SharedPreferences sharedPrefs = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        boolean firstStart = sharedPrefs.getBoolean("firstStart",true );

        //checks if this is the first time the user is using the application
        if (firstStart) {
            makeUser(sharedPrefs, db);
        }

        // This is the UUID for the current user using the app
        // It will save over instances of the app and is only updated upon first open after install
        UUID = sharedPrefs.getString(AutoID, "");

        /**
         * Will retrieve the Username for the user and set the variable username
         * to the returned String
         */
        DocumentReference userRef = db.collection("User").document(UUID);
        userRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        username = documentSnapshot.getString("Username");
                        phoneNumber = documentSnapshot.getString("ContactInfo");

                        SharedPreferences sharedPrefs = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPrefs.edit();

                        editor.putString("Username", username);
                        editor.putString("Number",phoneNumber);
                        editor.apply();
                    }
                });

        searchButton = findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSearch();
            }
        });

        profileButton = findViewById(R.id.profileButton);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openProfile();
            }
        });

        phoneNumber = sharedPrefs.getString("Number", "");

        expAdapter = new ExperimentAdapter(this,expDataList);
        experiments.setAdapter(expAdapter);
        manager.getExpData(db, expDataList, expAdapter, UUID); // To populate our experiment list

        experiments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent (MainActivity.this,DisplayExperimentActivity.class);
                Experiment exp_obj = expDataList.get(position);
                intent.putExtra("experiment",exp_obj);
                intent.putExtra("position",position);

                int LAUNCH_SECOND_ACTIVITY = 1;
                startActivityForResult(intent,LAUNCH_SECOND_ACTIVITY);
            }
        });
        expAdapter.notifyDataSetChanged();
    }

    /**
     * This method is called when the addButton is clicked and Switches MainActivity to
     * PublishExperimentActivity
     */
    public void addExperiment(View view){
        Intent intent = new Intent(this, PublishExperimentActivity.class);
        String mstr = UUID;
        intent.putExtra("AutoId",mstr);
        intent.putExtra("mode",0);
        startActivity(intent);
    }

    public void showSubList(View view){
        //SharedPreferences sharedPrefs = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        //phoneNumber = sharedPrefs.getString("Number", "");
        Intent intent = new Intent(this, ShowSubscribedListActivity.class);
        intent.putExtra("owner",UUID);
        startActivity(intent);
    }

    /**
     * This method is called when the searchButton is clicked and Switches MainActivity to
     * SearchingActivity
     */
    public void openSearch() {
        Intent intent = new Intent(this, SearchingActivity.class);
        startActivity(intent);
    }

    /**
     * This method is called when the profileButton is clicked and Switches MainActivity to
     * ProfileActivity
     */
    public void openProfile() {
        Intent intent = new Intent(this, ProfileActivity.class);
        String ID = UUID;
        String name = username;
        intent.putExtra("UUID",ID);
        startActivity(intent);
    }

    public void makeUser(SharedPreferences sharedPrefs, FirebaseFirestore db) {
        final DocumentReference userReference = db.collection("User").document();
        // Auto-ID created for the document
        String GrabbedID = userReference.getId();

        SharedPreferences.Editor editor = sharedPrefs.edit();

        editor.putString(AutoID, GrabbedID);

        editor.putBoolean("firstStart", false);
        editor.apply();

        // This is the UUID for the current user using the app
        // It will save over instances of the app and is only updated upon first open after install
        UUID = sharedPrefs.getString(AutoID, "");

        Map<String, Object> user = new HashMap<>();
        user.put("UID", UUID);
        user.put("Username", "New User");
        user.put("ContactInfo", "No contact info");

        db.collection("User").document(UUID).set(user);
    }
}