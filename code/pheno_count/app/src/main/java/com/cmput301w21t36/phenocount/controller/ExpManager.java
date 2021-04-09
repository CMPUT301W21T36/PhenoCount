package com.cmput301w21t36.phenocount;

import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
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

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This Class acts as an controller class for Experiments
 */
public class ExpManager {
    private final String TAG = "PhenoCount";

    /**
     * This method populates the list of current user's experiments in the MainActivity
     * @param db
     * @param expDataList
     * @param expAdapter
     * @param UUID
     * @see MainActivity
     */
    public void getExpData(FirebaseFirestore db, ArrayList<com.cmput301w21t36.phenocount.Experiment> expDataList,
                           ArrayAdapter<com.cmput301w21t36.phenocount.Experiment> expAdapter, String UUID, int mode, ListView expListView){
        //Google Developers, 2021-02-11, CCA 4.0/ Apache 2.0, https://firebase.google.com/docs/reference/android/com/google/firebase/firestore/Query
        db.collection("Experiment")
            .whereEqualTo("owner",UUID).orderBy("status")
            .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) {
                if(queryDocumentSnapshots.isEmpty()){
                    expListView.setBackgroundResource(R.drawable.hint_main);
                }else{
                    expListView.setBackgroundResource(R.drawable.hint_white);
                }
                getdata(db, expDataList, expAdapter, mode, queryDocumentSnapshots, error);
            }
        });
    }

    /**
     * This method populates the list of current user's subcribed experiments
     * in the ShowSubscribedListActivity
     * @param db
     * @param expDataList
     * @param expAdapter
     * @param UUID
     * @param mode
     * @param subListView
     */
    public void getSubExpData(FirebaseFirestore db, ArrayList<com.cmput301w21t36.phenocount.Experiment> expDataList,
                              ArrayAdapter<com.cmput301w21t36.phenocount.Experiment> expAdapter, String UUID, int mode, ListView subListView){
        db.collection("Experiment")
                .whereArrayContains("sub_list",UUID)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                            FirebaseFirestoreException error) {
                        if(queryDocumentSnapshots.isEmpty()){
                            subListView.setBackgroundResource(R.drawable.hint_sub);
                        }else{
                            subListView.setBackgroundResource(R.drawable.hint_white);
                        }
                        getdata(db, expDataList, expAdapter, mode, queryDocumentSnapshots, error);
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
     */
    public void updateTrialData(FirebaseFirestore db, com.cmput301w21t36.phenocount.Experiment exp, String username){
        if (exp != null) {
            final CollectionReference collectionReference = db.collection("Trials");

            HashMap<String, String> fdata = new HashMap<>();
            String id = collectionReference.document().getId();

            //common attributes
            if(exp.getTrials().size()!=0) {
                com.cmput301w21t36.phenocount.Trial trial = exp.getTrials().get(exp.getTrials().size() - 1);
                fdata.put("Latitude", "" + trial.getLatitude());
                fdata.put("Longitude", "" + trial.getLongitude());
                fdata.put("type", exp.getExpType());
                fdata.put("owner", username);
                fdata.put("userID", trial.getOwner().getUID());
                fdata.put("status",Boolean.toString(trial.getStatus()));
                fdata.put("date",trial.getDate());

                if (exp.getExpType().equals("Binomial")) {
                    com.cmput301w21t36.phenocount.Binomial btrial = (com.cmput301w21t36.phenocount.Binomial) exp.getTrials().get(exp.getTrials().size() - 1);
                    fdata.put("result", String.valueOf(btrial.getResult()));
                } else if (exp.getExpType().equals("Count")) {
                    com.cmput301w21t36.phenocount.Count ctrial = (com.cmput301w21t36.phenocount.Count) exp.getTrials().get(exp.getTrials().size() - 1);
                    fdata.put("result", String.valueOf(ctrial.getCount()));
                } else if (exp.getExpType().equals("Measurement")) {
                    com.cmput301w21t36.phenocount.Measurement mtrial = (com.cmput301w21t36.phenocount.Measurement) exp.getTrials().get(exp.getTrials().size() - 1);
                    fdata.put("result", String.valueOf(mtrial.getMeasurement()));
                } else if (exp.getExpType().equals("NonNegativeCount")) {
                    com.cmput301w21t36.phenocount.NonNegativeCount ntrial = (com.cmput301w21t36.phenocount.NonNegativeCount) exp.getTrials().get(exp.getTrials().size() - 1);
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

    /**
     * Method for ignoring the trials
     * @param exp
     */
    public void ignoreTrial(com.cmput301w21t36.phenocount.Experiment exp){
        com.cmput301w21t36.phenocount.DatabaseManager dm = new com.cmput301w21t36.phenocount.DatabaseManager();
        FirebaseFirestore db = dm.getDb();
        for (com.cmput301w21t36.phenocount.Trial trial: exp.getTrials()){
            if (!trial.getStatus()){
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
    public void getdata(FirebaseFirestore db, ArrayList<com.cmput301w21t36.phenocount.Experiment> expDataList,
                        ArrayAdapter<com.cmput301w21t36.phenocount.Experiment> expAdapter, int mode,
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

                com.cmput301w21t36.phenocount.Experiment newExp = new com.cmput301w21t36.phenocount.Experiment(name, description, region, type, minTrial,
                        reqLoc, expStatus, expID);

                Profile newProfile = new Profile();
                User currentUser = new User(owner, newProfile);
                newExp.setOwner(currentUser);
                newExp.setSubscribers(sList);

                Task<DocumentSnapshot> userDocument = db.collection("User")
                        .document(newExp.getOwner().getUID())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.getResult().getData()!=null) {
                                    String phoneNumber = (String) task.getResult().getData().get("ContactInfo");
                                    String username = (String) task.getResult().getData().get("Username");
                                    newExp.getOwner().getProfile().setUsername(username);
                                    newExp.getOwner().getProfile().setPhone(phoneNumber);
                                    expAdapter.notifyDataSetChanged();
                                }
                            }
                        });

                //adding the list of trials to each exp object
                db.collection("Experiment").document(newExp.getExpID()).collection("Trials").addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                        ArrayList<com.cmput301w21t36.phenocount.Trial> trials = new ArrayList<>();
                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                            Log.d("pheno", String.valueOf(doc.getId()));

                            String username = (String) doc.getData().get("owner");
                            String userID = (String) doc.getData().get("userID");
                            String latitude = (String) doc.getData().get("Latitude");
                            String longitude = (String) doc.getData().get("Longitude");
                            String status = (String) doc.getData().get("status");
                            String date = (String) doc.getData().get("date");
                            String ttype = newExp.getExpType();

                            Profile profile = new Profile();
                            profile.setUsername(username);
                            User user = new User(userID,profile);

                            //retrieving result from firebase
                            String result = (String) doc.getData().get("result");
                            if (result != null) {
                                if (ttype.equals("Count")) {
                                    com.cmput301w21t36.phenocount.Count ctrial = new com.cmput301w21t36.phenocount.Count(user);
                                    ctrial.setType(ttype);
                                    ctrial.setDate(date);
                                    ctrial.setStatus(Boolean.parseBoolean(status));
                                    ctrial.setLatitude(Float.parseFloat(latitude));
                                    ctrial.setLongitude(Float.parseFloat(longitude));
                                    ctrial.setCount(Integer.parseInt(result));
                                    trials.add(ctrial);
                                } else if (ttype.equals("Binomial")) {
                                    com.cmput301w21t36.phenocount.Binomial btrial = new com.cmput301w21t36.phenocount.Binomial(user);
                                    btrial.setType(ttype);
                                    btrial.setDate(date);
                                    btrial.setStatus(Boolean.parseBoolean(status));
                                    btrial.setLatitude(Float.parseFloat(latitude));
                                    btrial.setLongitude(Float.parseFloat(longitude));
                                    btrial.setResult(Boolean.parseBoolean(result));
                                    trials.add(btrial);
                                } else if (ttype.equals("Measurement")) {
                                    com.cmput301w21t36.phenocount.Measurement mtrial = new com.cmput301w21t36.phenocount.Measurement(user);
                                    mtrial.setType(ttype);
                                    mtrial.setDate(date);
                                    mtrial.setStatus(Boolean.parseBoolean(status));
                                    mtrial.setLatitude(Float.parseFloat(latitude));
                                    mtrial.setLongitude(Float.parseFloat(longitude));
                                    mtrial.setMeasurement(Float.parseFloat(result));
                                    trials.add(mtrial);
                                } else if (ttype.equals("NonNegativeCount")) {
                                    com.cmput301w21t36.phenocount.NonNegativeCount ntrial = new com.cmput301w21t36.phenocount.NonNegativeCount(user);
                                    ntrial.setType(ttype);
                                    ntrial.setDate(date);
                                    ntrial.setStatus(Boolean.parseBoolean(status));
                                    ntrial.setLatitude(Float.parseFloat(latitude));
                                    ntrial.setLongitude(Float.parseFloat(longitude));
                                    ntrial.setValue(Integer.parseInt(result));
                                    trials.add(ntrial);
                                }
                            }
                        }
                        newExp.setTrials(trials);
                    }
                });
                // To remove the unpublished experiments from the subscribed experiment list
                // mode 0 for search and owner's experiment list
                // mode 1 for subscribed experiment list
                if (mode == 1){
                    if (!(expStatus == 3)){
                        expDataList.add(newExp);
                    }
                }else if (mode == 0) {
                    expDataList.add(newExp);
                }
            }
        }
        expAdapter.notifyDataSetChanged();
    }
}
