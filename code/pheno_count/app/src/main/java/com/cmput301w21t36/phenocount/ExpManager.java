package com.cmput301w21t36.phenocount;

import android.content.SharedPreferences;
import android.util.Log;
import android.widget.ArrayAdapter;
import java.util.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.common.primitives.Booleans;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firestore.v1.Target;

import android.content.SharedPreferences;

import java.lang.reflect.Array;
import java.sql.NClob;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * This Class acts as an controller class for Experiments
 */
public class ExpManager {
    private final String TAG = "PhenoCount";

    /*public String getPhoneNumber(FirebaseFirestore db, String UUID){
        /*ds.collection("User").whereEqualTo("UID", UUID)
            .addSnapshotListener((queryDocumentSnapshots, error) -> {
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    Log.d("pheno", String.valueOf(doc.getId()));
                    phoneNumber = (String) doc.getData().get("ContactInfo");
                }
            });


        Task<DocumentSnapshot> userDocument = db.collection("User")
                .document(UUID).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.getResult()!=null) {
                    phoneNumber = (String) task.getResult().getData().get("ContactInfo");
                    String username = (String) task.getResult().getData().get("Username");
                }
            }
        });
        return phoneNumber;
    }

     */

    /**
     * This method populates the list of current user's experiments in the MainActivity
     * @param db
     * @param expDataList
     * @param expAdapter
     * @param UUID
     * @see MainActivity
     */
    public void getExpData(FirebaseFirestore db, ArrayList<Experiment> expDataList,
                           ArrayAdapter<Experiment> expAdapter, String UUID){
        //Google Developers, 2021-02-11, CCA 4.0/ Apache 2.0, https://firebase.google.com/docs/reference/android/com/google/firebase/firestore/Query
        db.collection("Experiment")
            .whereEqualTo("owner",UUID).orderBy("status")
            .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) {
                getdata(db, expDataList, expAdapter, queryDocumentSnapshots, error);
            }
        });
    }

    public void getSubExpData(FirebaseFirestore db, ArrayList<Experiment> expDataList,
                              ArrayAdapter<Experiment> expAdapter, String UUID){
        db.collection("Experiment")
                .whereArrayContains("sub_list",UUID).orderBy("status")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                            FirebaseFirestoreException error) {
                        getdata(db, expDataList, expAdapter, queryDocumentSnapshots, error);
                    }
                });
    }

    /**
     * This method is called to update trial data received from the respective trial classes
     * to firebase
     * it creates a hash map object and adds the data to the collection
     * @param db
     * the Firestore Database
     * @param exp
     * the updated experiment object
     * @param username
     * the username of the owner of the trial
     * @param UUID
     * the unique ID of the owner of the trial
     */
    public void updateTrialData(FirebaseFirestore db, Experiment exp,String username,String UUID){

        if (exp != null) {

            //db = FirebaseFirestore.getInstance();
            final CollectionReference collectionReference = db.collection("Trials");

            HashMap<String, String> fdata = new HashMap<>();
            String id = collectionReference.document().getId();

            //common attributes
            System.out.println("SIZE:"+exp.getTrials().size());
            if(exp.getTrials().size()!=0) {
                Trial trial = exp.getTrials().get(exp.getTrials().size() - 1);
                fdata.put("Latitude", "" + trial.getLatitude());
                fdata.put("Longitude", "" + trial.getLongitude());
                fdata.put("type", exp.getExpType());
                fdata.put("owner", username);
                fdata.put("userID", UUID);
                fdata.put("status",Boolean.toString(trial.getStatus()));
                fdata.put("date",trial.getDate());

                if (exp.getExpType().equals("Binomial")) {
                    Binomial btrial = (Binomial) exp.getTrials().get(exp.getTrials().size() - 1);
                    fdata.put("result", String.valueOf(btrial.getResult()));
                } else if (exp.getExpType().equals("Count")) {
                    Count ctrial = (Count) exp.getTrials().get(exp.getTrials().size() - 1);
                    fdata.put("result", String.valueOf(ctrial.getCount()));
                } else if (exp.getExpType().equals("Measurement")) {
                    Measurement mtrial = (Measurement) exp.getTrials().get(exp.getTrials().size() - 1);
                    fdata.put("result", String.valueOf(mtrial.getMeasurement()));
                } else if (exp.getExpType().equals("NonNegativeCount")) {
                    NonNegativeCount ntrial = (NonNegativeCount) exp.getTrials().get(exp.getTrials().size() - 1);
                    fdata.put("result", String.valueOf(ntrial.getValue()));
                }

                //adding data to firebase
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
            }
        }
    }

    public void ignoreTrial(Experiment exp){
        DatabaseManager dm = new DatabaseManager();
        FirebaseFirestore db = dm.getDb();
        for (Trial trial: exp.getTrials()){
            if (!trial.getStatus()){
                System.out.println("HELOOO");
                String UUID = trial.getOwner().getUID();
                db.collection("Experiment").document(exp.getExpID())
                        .collection("Trials")
                        .whereEqualTo("userID",UUID)
                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                for (QueryDocumentSnapshot document : value) {
                                    if(error ==null){
                                        db.collection("Experiment")
                                                .document(exp.getExpID()).collection("Trials")
                                                .document(document.getId())
                                                .update("status","false");
                                    }
                                }
                            }
                        });
            }
        }
    }
    /**
     * General method for querying the Experiment collection in fireStore
     * @param db
     * @param expDataList
     * @param expAdapter
     * @param queryDocumentSnapshots
     * @param error
     */
    public void getdata(FirebaseFirestore db, ArrayList<Experiment> expDataList,
                        ArrayAdapter<Experiment> expAdapter,
                        QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException error){
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
                //String userName = (String) doc.getData().get("owner_name");
                ArrayList sList = (ArrayList) doc.getData().get("sub_list");


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

                Experiment newExp = new Experiment(name, description, region, type, minTrial,
                        reqLoc, expStatus, expID);

                Profile newProfile = new Profile();
                User currentUser = new User(owner, newProfile);
                newExp.setOwner(currentUser);
                newExp.setSubscribers(sList);

                expDataList.add(newExp);
            }
        }

        //loops through the exp objects and adds trials to it from firebase
        for(int i = 0;i<expDataList.size();i++) {

            Experiment exp = expDataList.get(i);
            int finalI = i;
            db.collection("Experiment").document(exp.getExpID()).collection("Trials").addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                    ArrayList<Trial> trials = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        Log.d("pheno", String.valueOf(doc.getId()));

                        String username = (String) doc.getData().get("owner");
                        String userID = (String) doc.getData().get("userID");
                        String latitude = (String) doc.getData().get("Latitude");
                        String longitude = (String) doc.getData().get("Longitude");
                        String status = (String) doc.getData().get("status");
                        String date = (String) doc.getData().get("date");
                        String ttype = exp.getExpType();

                        Profile profile = new Profile();
                        profile.setUsername(username);
                        User user = new User(userID,profile);

                        Binomial newtrial = new Binomial(user);
                        Trial trial = newtrial;
                        trial.setType(ttype);
                        trial.setDate(date);
                        trial.setStatus(Boolean.parseBoolean(status));
                        trial.setLatitude(Float.parseFloat(latitude));
                        trial.setLongitude(Float.parseFloat(longitude));

                        //retrieving result from firebase
                        String result = (String) doc.getData().get("result");
                        if (result != null) {
                            if (ttype.equals("Binomial")) {
                                Binomial btrial = (Binomial) trial;
                                btrial.setResult(Boolean.parseBoolean(result));
                                trials.add(trial);
                            } else if (ttype.equals("Count")) {
                                Count ctrial = new Count(user);
                                ctrial.setCount(Integer.parseInt(result));
                                trials.add(trial);
                            } else if (ttype.equals("Measurement")) {
                                Measurement mtrial = new Measurement(user);
                                mtrial.setMeasurement(Float.parseFloat(result));
                                trials.add(trial);
                            } else if (ttype.equals("NonNegativeCount")) {
                                NonNegativeCount ntrial = new NonNegativeCount(user);
                                ntrial.setValue(Integer.parseInt(result));
                                trials.add(trial);
                            }
                        }
                    }
                    exp.setTrials(trials);
                    expDataList.set(finalI,exp); //adding updated trial object to original list
                }
            });

            Task<DocumentSnapshot> userDocument = db.collection("User")
                    .document(exp.getOwner().getUID()).get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.getResult()!=null) {
                        String phoneNumber = (String) task.getResult().getData().get("ContactInfo");
                        String username = (String) task.getResult().getData().get("Username");
                        exp.getOwner().getProfile().setUsername(username);
                        exp.getOwner().getProfile().setPhone(phoneNumber);
                    }
                }
            });
        }
        expAdapter.notifyDataSetChanged();
    }
}
