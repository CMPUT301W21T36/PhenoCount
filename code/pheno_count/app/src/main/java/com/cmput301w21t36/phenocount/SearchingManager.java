package com.cmput301w21t36.phenocount;

import android.util.Log;
import android.widget.ArrayAdapter;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class SearchingManager {

    private ArrayList<Experiment> allExperimentArray;
    private ArrayList<Experiment> resultExperimentArray;
    private ExpManager expManager;


    public SearchingManager() {
        allExperimentArray = new ArrayList<Experiment>();
    }

    public ArrayList<Experiment> getAllExperimentArray() {
        return allExperimentArray;
    }

    public void setAllExperimentArray(ArrayList<Experiment> allExperimentArray) {
        this.allExperimentArray = allExperimentArray;
    }

    public ArrayList<Experiment> getResultExperimentArray() {
        return resultExperimentArray;
    }

    public void setResultExperimentArray(ArrayList<Experiment> resultExperimentArray) {
        this.resultExperimentArray = resultExperimentArray;
    }



    public void getAllExp(FirebaseFirestore db, ArrayList<Experiment> expDataList, ArrayAdapter<Experiment> expAdapter){

        db.collection("Experiment").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) {
                expManager = new ExpManager();
                expManager.getdata(db,expDataList,expAdapter,queryDocumentSnapshots,error);

                /*expDataList.clear();
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
                    db.collection("Trials").whereEqualTo("ExpID", exp.getExpID()).addSnapshotListener(new EventListener<QuerySnapshot>() {
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
                                else if (ttype.equals("MeasurementActivity")){
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
                expAdapter.notifyDataSetChanged();*/
            }
        });
    }

}
