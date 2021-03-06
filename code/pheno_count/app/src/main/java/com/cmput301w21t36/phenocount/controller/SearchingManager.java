package com.cmput301w21t36.phenocount;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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

    private ExpManager expManager = new ExpManager();
    private Context context;

    // Constructor that will take the searching activity as context
    public SearchingManager(Context context) {
        this.context = context;
    }

    /**
     * Utilizes expManager getData method to fill the list with all experiments in the database except those unpublished
     * @param db
     * @param expDataList
     * @param expAdapter
     * @see ExpManager
     */
    public void getAllExp(FirebaseFirestore db, ArrayList<Experiment> expDataList,
                          ArrayAdapter<Experiment> expAdapter){
        db.collection("Experiment").whereNotEqualTo("status", "3").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) {
                expManager.getdata(db, expDataList, expAdapter, 0, queryDocumentSnapshots, error);
            }
        });
    }

    /**
     * Takes in a key word, will see which experiments in ArrayList satisfy the conditions and update the adapter and listView
     * @param keyword
     * @param adapter
     * @param expDataList
     * @param experimentListView
     */
    public void getSearchExp(String keyword, ResultAdapter adapter, ArrayList<Experiment> expDataList, ListView experimentListView) {
        keyword = keyword.toLowerCase();

        if (keyword.length() > 0) {

            ArrayList<Experiment> foundExp = new ArrayList<>();
            for (Experiment exp : expDataList) {
                if (exp.getDescription().toLowerCase().contains(keyword) || exp.getName().toLowerCase().contains(keyword) || exp.getOwner().getProfile().getUsername().toLowerCase().contains(keyword)){
                    foundExp.add(exp);
                }
            }
            adapter = new ResultAdapter(context, foundExp);
            if(foundExp.size() == 0){
                experimentListView.setBackgroundResource(R.drawable.hint_search);
            }else{
                experimentListView.setBackgroundResource(R.drawable.hint_white);
            }

        }
        // Refill list with all experiments when nothing is there
        else {
            adapter = new ResultAdapter(context, expDataList);

        }

        // Update listView
        experimentListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

}