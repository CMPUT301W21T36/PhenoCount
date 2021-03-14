package com.cmput301w21t36.phenocount;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

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

    ImageButton profileButton;
    FirebaseFirestore db;

    ListView experiments;
    ArrayList<Experiment> expDataList;
    ArrayAdapter<Experiment> expAdapter;
    ExpManager manager = new ExpManager();


    static final String AutoID = "ID";
    private String UUID;
    private String username;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        experiments = findViewById(R.id.expList);
        //Intent i = new Intent(this, LocationActivity.class);
        //a i.putExtra("info", info);
        //startActivityForResult(i, 1);
        expDataList = new ArrayList<>();

        // Will get instance of the database
        db = FirebaseFirestore.getInstance();


        //DocumentReference userReference;



       /* Experiment exp = new Experiment("Coin Flip", "We flip a coin in this experiment","North America","Binomial", 10, true);
        expDataList.add(exp);
        Experiment exp2 = new Experiment("Number of Cars", "We count the number of cars in this experiment","North America","Count", 10, true);
        expDataList.add(exp2);
        Experiment exp3 = new Experiment("Temperature In Edmonton", "We measure the Temperature in this experiment","North America","Measurement", 10, true);
        expDataList.add(exp3);
        Experiment exp4 = new Experiment("Number of Eggs that cracked", "We count the number of eggs that cracked in this experiment","North America","Non Negative Count", 10, true);
        expDataList.add(exp4);

        */
        expAdapter = new ExperimentAdapter(this,expDataList);
        experiments.setAdapter(expAdapter);
        getExpData1();


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
            user.put("Username", "Test1");
            user.put("ContactInfo", "7801111111");

            db.collection("User").document(UUID).set(user);
        }

        // This is the UUID for the current user using the app
        // It will save over instances of the app and is only updated upon first open after install

        UUID = sharedPrefs.getString(AutoID, "");


        /**
         * Will retrieve the Username for the user and set the variable username
         * to the returned String
         */
        DocumentReference userRef = db.collection("User").document();
            userRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        username = documentSnapshot.getString("Username");
                        System.out.println(username);
                    }
                });


        profileButton = findViewById(R.id.profileButton);


        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openProfile();
            }
        });


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

    public void displayExperiment(View view){
        Intent intent = new Intent(this, DisplayExperimentActivity.class);
        startActivity(intent);
    }

    public void addExperiment(View view){
        Intent intent = new Intent(this, PublishExperimentActivity.class);
        String mstr = UUID;
        intent.putExtra("AutoId",mstr);
        startActivity(intent);
    }

    public void openProfile(){
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    ///2 need to check with expManager as not working with that
    public void getExpData1(){
        final CollectionReference collectionReference = db.collection("Experiment");

        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) {
                expDataList.clear();
                for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
                {
                    if (error ==null ) {
                        Log.d("pheno", String.valueOf(doc.getId()));
                        String expID = doc.getId();
                        String name = (String) doc.getData().get("name");
                        String description = (String) doc.getData().get("description");
                        String region = (String) doc.getData().get("region");
                        String type = (String) doc.getData().get("type");
                        String minInt = (String) doc.getData().get("minimum_trials");
                        String reqGeo = (String) doc.getData().get("require_geolocation");
                        String mStat = (String) doc.getData().get("status");

                        boolean reqLoc;
                        if (reqGeo == "YES") {
                            System.out.println("HELOOOOOOOOOOOO");
                            reqLoc = true;
                        } else {
                            System.out.println("nOOOOOOOOOOOO");

                            reqLoc = false;
                        }
                        int minTrial =  1;
                        if (!minInt.isEmpty()){
                            minTrial=Integer.parseInt(minInt);}
                        int expStatus = 0;
                        if (!mStat.isEmpty()){
                            expStatus = Integer.parseInt(mStat);}
                        expDataList.add(new Experiment(name, description, region, type, minTrial, reqLoc,expStatus,expID)); // Adding the cities and provinces from FireStore
                    }
                }
                expAdapter.notifyDataSetChanged(); // Notifying the adapter to render any new data fetched from the cloud
            }
        });
    }

    ///2


}