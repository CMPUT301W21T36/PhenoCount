package com.cmput301w21t36.phenocount;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.health.SystemHealthManager;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity {
    FirebaseFirestore db;
    ListView experiments;
    Button searchButton;
    Button profileButton;
    ArrayList<Experiment> expDataList;
    ArrayAdapter<Experiment> expAdapter;
    ExpManager manager = new ExpManager();
    DatabaseManager dbmanager = new DatabaseManager();
    static final String AutoID = "ID";
    private String UUID;
    private String username;
    private String phone_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        experiments = findViewById(R.id.expList);
        expDataList = new ArrayList<>();

        // To get instance of the database
        db = dbmanager.getDb();


        SharedPreferences sharedPrefs = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        boolean firstStart = sharedPrefs.getBoolean("firstStart",true );

        /**
         *
         */

        if (firstStart) {
            final DocumentReference userReference = db.collection("User").document();
            // Auto-ID created for the document
            String GrabbedID = userReference.getId();

            sharedPrefs = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
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
                        phone_number = documentSnapshot.getString("ContactInfo");

                        SharedPreferences sharedPrefs = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPrefs.edit();

                        editor.putString("Username", username);
                        editor.putString("Number",phone_number);
                        editor.apply();
                    }
                });

        // Username for the current user
        // username = sharedPrefs.getString("Username", "");
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


        expAdapter = new ExperimentAdapter(this,expDataList);
        experiments.setAdapter(expAdapter);
        // To populate our experiment list
        manager.getExpData(db, expDataList, expAdapter, UUID);

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

        SharedPreferences sharedPrefs = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        username = sharedPrefs.getString("Username", "");

        intent.putExtra("AutoId",mstr);
        intent.putExtra("username",username);
        startActivity(intent);

    }

    public void openSearch() {
        Intent intent = new Intent(this, SearchingActivity.class);
        intent.putExtra("expID",UUID);
        startActivity(intent);
    }

    public void openProfile() {
        Intent intent = new Intent(this, ProfileActivity.class);
        String ID = UUID;
        String name = username;
        intent.putExtra("UUID",ID);
        startActivity(intent);
    }
}