package com.cmput301w21t36.phenocount;

import android.content.SharedPreferences;
import android.util.Log;
import android.widget.ArrayAdapter;

import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnSuccessListener;
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
import java.util.List;

public class ExpManager {
    //FirebaseFirestore db;
//    private ArrayList<Experiment> expDataList= new ArrayList<>();
    ArrayList<Experiment> expList = new ArrayList<>();
    private final String TAG = "PhenoCount";
    ArrayAdapter<Experiment> expAdapter;
    //db = FirebaseFirestore.getInstance();
    private String ownerName;

    // adds experiment to the data list
    public void  addExperiment(Experiment newExp,ArrayList<Experiment> expDataList ){
        expDataList.add(newExp);
        expAdapter.notifyDataSetChanged();

    }

    /*public ArrayList<Experiment> getExpData(){
  //  public void  getExpData(ArrayList<Experiment> expDataList){
        FirebaseFirestore db;
        db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection("Experiment");
//        final DocumentReference expReference = collectionReference.document();
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) {
                expList.clear();
                for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
                {
                    if (error ==null ) {
                        Log.d(TAG, String.valueOf(doc.getId()));
                        String expId = doc.getId();
                        String name = (String) doc.getData().get("name");
                        String description = (String) doc.getData().get("description");
                        String region = (String) doc.getData().get("region");
                        String type = (String) doc.getData().get("type");
                        String minInt = (String) doc.getData().get("minimum_trials");
                        String reqGeo = (String) doc.getData().get("require_geolocation");
                        String mStat = (String) doc.getData().get("status");
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
                        //expList.add(new Experiment(name, description, region, type, minTrial, reqLoc,expStatus,expId,)); // Adding the cities and provinces from FireStore
                    }
                }
//                expAdapter.notifyDataSetChanged(); // Notifying the adapter to render any new data fetchedn from the cloud
            }
        });

        return expList;

    }

     */

    public void getExpData(FirebaseFirestore db, ArrayList<Experiment> expDataList, ArrayAdapter<Experiment> expAdapter, String UUID){
        //final CollectionReference collectionReference = db.collection("Experiment").whereEqualTo("owner",UUID).;
        db.collection("Experiment").whereEqualTo("owner",UUID).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) {
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
                        ArrayList<Trial> trials = new ArrayList<>();
                        Experiment newExp = new Experiment(name, description, region, type, minTrial, reqLoc, expStatus, expID);
                        //SharedPreferences sharedPrefs = getSharedPreferences("sharedPrefs", 0);
                        //SharedPreferences.Editor editor = sharedPrefs.edit();

                        //creating a user object
                        //username = sharedPrefs.getString("Username","");
                        //phone_number = sharedPrefs.getString("Number","");
                        //System.out.println(username);
                        //UUID = sharedPrefs.getString(AutoID, "");

                        //creating a profile object
                        Profile newprofile = new Profile(userName);
                        User current_user = new User(owner, newprofile);

                        newExp.setOwner(current_user);
                        //newExp.setExpID(expID);

                        expDataList.add(newExp);




                    }

                }

                int i =0;
                while(i<expDataList.size()) {
                    Experiment exp = expDataList.get(i);
                    int finalI = i;
                    db.collection("Experiment").document(exp.getID()).collection("Trials").addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                            ArrayList<Trial> trials = new ArrayList<>();
                                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                                    Log.d("pheno", String.valueOf(doc.getId()));
                                    String ttype = (String) doc.getData().get("type");
                                User user = exp.getOwner();

                                Trial trial = new Trial(user);

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

                expAdapter.notifyDataSetChanged(); // Notifying the adapter to render any new data fetchedn from the cloud
            }
        });
    }
}
