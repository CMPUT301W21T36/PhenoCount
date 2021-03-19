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
            System.out.println("SIZE:"+exp.getTrials().size());

            db = FirebaseFirestore.getInstance();
            final CollectionReference collectionReference = db.collection("Trials");


            HashMap<String, String> fdata = new HashMap<>();
            String id = collectionReference.document().getId();

            //fdata.put("expID", exp.getID()); // dont need it anymore
            if(exp.getExpType().equals("Binomial")) {
                Binomial trial = (Binomial) exp.getTrials().get(exp.getTrials().size()-1);
                fdata.put("result",String.valueOf(trial.getResult()));
                fdata.put("Latitude",""+trial.getLatitude());
                fdata.put("Longitude",""+trial.getLongitude());
            }
            else if (exp.getExpType().equals("Count")) {
                Count trial = (Count) exp.getTrials().get(exp.getTrials().size()-1);
                fdata.put("result",String.valueOf(trial.getCount()));
                fdata.put("Latitude",""+trial.getLatitude());
                fdata.put("Longitude",""+trial.getLongitude());
            }
            else if (exp.getExpType().equals("Measurement")){
                Measurement trial = (Measurement) exp.getTrials().get(exp.getTrials().size()-1);
                float value = trial.getMeasurement();
                System.out.println("VALUE: "+value);
                fdata.put("result",String.valueOf(value));
                fdata.put("Latitude",""+trial.getLatitude());
                fdata.put("Longitude",""+trial.getLongitude());
            }
            else if (exp.getExpType().equals("NonNegativeCount")){
                NonNegativeCount trial = (NonNegativeCount) exp.getTrials().get(exp.getTrials().size()-1);
                fdata.put("result",String.valueOf(trial.getValue()));
                fdata.put("Latitude",""+trial.getLatitude());
                fdata.put("Longitude",""+trial.getLongitude());
            }
            fdata.put("type", exp.getExpType());
            fdata.put("owner", username);
            fdata.put("userID",UUID);

            //location



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

        for(int i = 0;i<expDataList.size();i++) {

            Experiment exp = expDataList.get(i);
            System.out.println("expID"+exp.getExpID());
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
                        String ttype = exp.getExpType();

                        Profile profile = new Profile(username);//if phone number needed, do query
                        User user = new User(userID,profile);
                        //Trial trial;
                        System.out.println("Latitude"+latitude);

/*                      if(latitude !=null && longitude != null){
                            trial.setLatitude(Float.parseFloat(latitude));
                            trial.setLongitude(Float.parseFloat(longitude));///////////////
                        }*/


                        //retriving result from firebase
                        String result = (String) doc.getData().get("result");
                        System.out.println("RESULT"+result);
                        if (result != null) {
                            if (ttype.equals("Binomial")) {
                                Binomial trial = new Binomial(user);
                                trial.setResult(Boolean.parseBoolean(result));
                                trial.setOwner(user);
                                trial.setType(ttype);
                                trial.setLatitude(Float.parseFloat(latitude));
                                trial.setLongitude(Float.parseFloat(longitude));
                                trials.add(trial);
                            } else if (ttype.equals("Count")) {
                                Count trial = new Count(user);
                                trial.setCount(Integer.parseInt(result));
                                trial.setOwner(user);
                                trial.setType(ttype);
                                trial.setLatitude(Float.parseFloat(latitude));
                                trial.setLongitude(Float.parseFloat(longitude));
                                trials.add(trial);
                            } else if (ttype.equals("Measurement")) {
                                Measurement trial = new Measurement(user);
                                trial.setMeasurement(Float.parseFloat(result));
                                trial.setOwner(user);
                                trial.setType(ttype);
                                trial.setLatitude(Float.parseFloat(latitude));
                                trial.setLongitude(Float.parseFloat(longitude));
                                trials.add(trial);
                            } else if (ttype.equals("NonNegativeCount")) {
                                NonNegativeCount trial = new NonNegativeCount(user);
                                trial.setValue(Integer.parseInt(result));
                                trial.setOwner(user);
                                trial.setType(ttype);
                                trial.setLatitude(Float.parseFloat(latitude));
                                trial.setLongitude(Float.parseFloat(longitude));
                                trials.add(trial);
                            }
                        }
                    }
                    exp.setTrials(trials);
                    expDataList.set(finalI,exp); //adding updated trial object to original list
                    System.out.println("SIZE:" + trials.size());
                }
            });

        }
        expAdapter.notifyDataSetChanged();
    }
}
