package com.cmput301w21t36.phenocount;

import android.content.SharedPreferences;
import android.util.Log;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
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
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This Class acts as an controller class for Experiments
 */
public class ExpManager {
    private final String TAG = "PhenoCount";
    ArrayAdapter<Experiment> expAdapter;

    /**
     * This method populates the list of current user's experiments in the MainActivity
     * @param db
     * @param expDataList
     * @param expAdapter
     * @param UUID
     * @see MainActivity
     */
    public void getExpData(FirebaseFirestore db, ArrayList<Experiment> expDataList, ArrayAdapter<Experiment> expAdapter, String UUID){
        //Google Developers, 2021-02-11, CCA 4.0/ Apache 2.0, https://firebase.google.com/docs/reference/android/com/google/firebase/firestore/Query
        db.collection("Experiment").whereEqualTo("owner",UUID).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) {
                getdata(db,expDataList,expAdapter,queryDocumentSnapshots,error);
            }
        });
    }

    public void updateTrialData(FirebaseFirestore db, Experiment exp,String username,String UUID){

        if (exp != null) {
            //exp = newexp; //updating the current exp object(to show updated exp desc)
            System.out.println("SIZE:"+exp.getTrials().size());

            //Intent intent = getIntent();
            //Bundle bundle = getIntent().getExtras();
            //String owner = bundle.get("AutoId").toString();

            db = FirebaseFirestore.getInstance();
            final CollectionReference collectionReference = db.collection("Trials");
            //manager.addExperiment(exp);
            //expAdayDataSetChanged();
            ////////// here
            // final String Type = expType;

            HashMap<String, String> fdata = new HashMap<>();
            String id = db.collection("Trials").document().getId();
            Trial trial = exp.getTrials().get(exp.getTrials().size()-1);

            //fdata.put("expID", exp.getID()); // dont need it anymore
            if(exp.getExpType().equals("Binomial")) {
                fdata.put("result",String.valueOf(trial.getResult()));
            }
            else if (exp.getExpType().equals("Count")) {
                fdata.put("result",String.valueOf(trial.getCount()));
            }
            else if (exp.getExpType().equals("Measurement")){
                fdata.put("result",String.valueOf(trial.getMeasurement()));
            }
            else if (exp.getExpType().equals("NonNegativeCount")){
                fdata.put("result",String.valueOf(trial.getValue()));
            }
            fdata.put("type", exp.getExpType());
            fdata.put("owner", username);
            fdata.put("userID",UUID);

            //location
            fdata.put("Latitude",""+trial.getLatitude());
            fdata.put("Longitude",""+trial.getLongitude());


            db.collection("Experiment")
                    .document(exp.getExpID()).collection("Trials")
                    .add(fdata)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG, "Data added successfully!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // These are a method which gets executed if there’s any problem
                            Log.d(TAG, "Data could not be added!" + e.toString());
                        }
                    });
        } else {
//                    String testt = data.getSerializableExtra("scannedText").toString();
//                    TextView test = findViewById(R.id.scannedTextView);
//                    test.setText(testt);
        }
    }

    /**
     * General method for querying the Experiment collectio in fireStore
     * @param db
     * @param expDataList
     * @param expAdapter
     * @param queryDocumentSnapshots
     * @param error
     */
    public void getdata(FirebaseFirestore db, ArrayList<Experiment> expDataList, ArrayAdapter<Experiment> expAdapter,QuerySnapshot queryDocumentSnapshots,FirebaseFirestoreException error){
        expDataList.clear();
        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {

            if (error == null) {
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
                String userName = (String) doc.getData().get("owner_name");

                boolean reqLoc;
                if (reqGeo.equals("YES")) {
                    reqLoc = true;
                } else {
                    reqLoc = false;
                }
                int minTrial = 1;
                if (!minInt.isEmpty()) {
                    minTrial = Integer.parseInt(minInt);
                }
                int expStatus = 0;
                if (!mStat.isEmpty()) {
                    expStatus = Integer.parseInt(mStat);
                }

                Experiment newExp = new Experiment(name, description, region, type, minTrial, reqLoc, expStatus, expID);

                //creating a profile object
                Profile newprofile = new Profile(userName);
                User current_user = new User(owner, newprofile);

                newExp.setOwner(current_user);
                expDataList.add(newExp);
            }
        }

        int i =0;
        while(i<expDataList.size()) {
            Experiment exp = expDataList.get(i);
            int finalI = i;
            db.collection("Experiment").document(exp.getExpID()).collection("Trials").addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                    ArrayList<Trial> trials = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        Log.d("pheno", String.valueOf(doc.getId()));
                        //query to get user data of trial owner
                        String username = (String) doc.getData().get("owner");
                        String userID = (String) doc.getData().get("userID");
                        String latitude = (String) doc.getData().get("Latitude");
                        String longitude = (String) doc.getData().get("Longitude");
                        String ttype = (String) doc.getData().get("type");

                        Profile profile = new Profile(username);//if phone number needed, do query
                        User user = new User(userID,profile);
                        Trial trial = new Trial(user);

                        System.out.println("Latitude"+latitude);

                        if(latitude !=null && longitude != null){
                            trial.setLatitude(Float.parseFloat(latitude));
                            trial.setLongitude(Float.parseFloat(longitude));
                        }

                        trial.setType(ttype);

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
                    }
                    exp.setTrials(trials);
                    expDataList.set(finalI,exp);
                    System.out.println("SIZE:" + trials.size());
                }
            });
            i++;
        }
        expAdapter.notifyDataSetChanged();
    }
}
