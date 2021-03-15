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

    ImageButton searchButton;
    ImageButton profileButton;
    FirebaseFirestore db;

    ListView experiments;
    ArrayList<Experiment> expDataList;
    ArrayAdapter<Experiment> expAdapter;
    ExpManager manager = new ExpManager();


    static final String AutoID = "ID";
    private String UUID;
    private String username;
    private String phone_number;
    Experiment newexp;
    int test1 = 0;




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
        username = sharedPrefs.getString("Username", "");
        System.out.println(username);


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
        getExpData1();
        //addTrialsData();

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

    public void openSearch() {
        Intent intent = new Intent(this, SearchingActivity.class);
        startActivity(intent);
    }

    public void openProfile() {
        Intent intent = new Intent(this, ProfileActivity.class);
        String ID = UUID;
        intent.putExtra("UUID",ID);
        startActivity(intent);
    }

    ///2 need to check with expManager as not working with that

    public void getExpData1(){

        //final CollectionReference collectionReference = db.collection("Experiment").whereEqualTo("owner",UUID).;
        db.collection("Experiment").whereEqualTo("owner",UUID).addSnapshotListener(new EventListener<QuerySnapshot>() {
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
                        String owner = (String) doc.getData().get("owner");

                        boolean reqLoc;
                        if (reqGeo.equals("YES")) {
                            reqLoc = true;
                        } else {
                            reqLoc = false;
                        }
                        int minTrial =  1;
                        if (!minInt.isEmpty()){
                            minTrial=Integer.parseInt(minInt);}
                        int expStatus = 0;
                        if (!mStat.isEmpty()){
                            expStatus = Integer.parseInt(mStat);}
                        ////////////////////newnew
                        int finalMinTrial = minTrial;
                        int finalExpStatus = expStatus;
                        ArrayList<Trial> trials = new ArrayList<>();
                        Experiment newexp = new Experiment(name, description, region, type, finalMinTrial, reqLoc, finalExpStatus, expID);
                        SharedPreferences sharedPrefs = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPrefs.edit();

                        //creating a user object
                        username = sharedPrefs.getString("Username", "");
                        phone_number = sharedPrefs.getString("Number","");
                        System.out.println(username);
                        UUID = sharedPrefs.getString(AutoID, "");

                        //creating a profile object
                        Profile newprofile = new Profile(username,phone_number);
                        User current_user = new User(UUID,newprofile);

                        newexp.setOwner(current_user);


                        expDataList.add(newexp); // Adding the cities and provinces from FireStore
                    }
                }
                ////////////////////
                int i =0;
                while(i<expDataList.size()) {
                    Experiment exp = expDataList.get(i);
                    int finalI = i;
                    db.collection("Trials").whereEqualTo("ExpID", exp.getID()).addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                            ArrayList<Trial> trials = new ArrayList<>();
                            for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                                Log.d("pheno", String.valueOf(doc.getId()));
                                String tname = (String) doc.getData().get("name");
                                String tdescription = (String) doc.getData().get("description");
                                String ttype = (String) doc.getData().get("type");
                                User user = exp.getOwner();

                                Trial trial = new Trial(tname, tdescription,user, ttype);

                                //retriving result from firebase
                                String result = (String) doc.getData().get("result");
                                if(ttype.equals("Binomial")) {
                                    trial.setResult(Boolean.parseBoolean(result));
                                }
                                else if (ttype.equals("Count")) {
                                    trial.setCount(Integer.parseInt(result));
                                }
                                else if (ttype.equals("Measurement")){
                                    trial.setMeasurement(Float.parseFloat(result));
                                }
                                else if (ttype.equals("Non Negative Count")){
                                    trial.setValue(Integer.parseInt(result));
                                }

                                trials.add(trial);
                                System.out.println("DESCRIPTION: " + trial.getName());
                            }
                            exp.setTrials(trials);
                            expDataList.set(finalI,exp);
                            System.out.println("SIZE:" + trials.size());

                            //newexp = new Experiment(name, description, region, type, finalMinTrial, reqLoc, finalExpStatus, expID,trials)); // Adding the cities and provinces from FireStore

                        }
                    });

                    i++;
                }

                expAdapter.notifyDataSetChanged(); // Notifying the adapter to render any new data fetched from the cloud
            }
        });

        ////////////////////newnew


    }

}