package com.cmput301w21t36.phenocount;

import android.util.Log;
import android.widget.ArrayAdapter;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ExpManager {
    //FirebaseFirestore db;
    Experiment exp ;
//    private ArrayList<Experiment> expDataList= new ArrayList<>();
    ArrayList<Experiment> expList = new ArrayList<>();
    private final String TAG = "PhenoCount";
    ArrayAdapter<Experiment> expAdapter;
    //db = FirebaseFirestore.getInstance();

    // adds experiment to the data list
    public void  addExperiment(Experiment newExp,ArrayList<Experiment> expDataList ){
        expDataList.add(newExp);
        expAdapter.notifyDataSetChanged();

    }

    public ArrayList<Experiment> getExpData(){
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
                        expList.add(new Experiment(name, description, region, type, minTrial, reqLoc,expStatus,expId)); // Adding the cities and provinces from FireStore
                    }
                }
//                expAdapter.notifyDataSetChanged(); // Notifying the adapter to render any new data fetchedn from the cloud
            }
        });

        return expList;

    }


}
