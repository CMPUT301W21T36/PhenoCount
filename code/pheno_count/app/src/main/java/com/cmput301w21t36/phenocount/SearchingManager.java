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

/**
 * Will recieve all experiments from ExpManager and will allow for a keyword to search through all relevant experiments and
 * return the related experiments
 * @see ExpManager
 */
public class SearchingManager {

    private ExpManager expManager;

    public void getAllExp(FirebaseFirestore db, ArrayList<Experiment> expDataList, ArrayAdapter<Experiment> expAdapter){
        db.collection("Experiment").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) {
                expManager = new ExpManager();
                expManager.getdata(db,expDataList,expAdapter,queryDocumentSnapshots,error);

            }
        });
    }

}
