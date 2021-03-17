package com.cmput301w21t36.phenocount;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SearchingActivity extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference experimentRef = db.collection("Experiment");

    private ResultAdapterTest adapter;
    ListView experimentListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searching);
        //setUpRecyclerView();







        /*
        db.collection("Experiment").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                // Make an empty arrayList
                ArrayList<Experiment> experimentList = new ArrayList<>();

                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document : task.getResult()) {
                        // BROKEN LINE BECAUSE CAST STRING TO USER WHICH IS NOT POSSIBLE
                        //experimentList.add(document.toObject(Experiment.class));
                    }

                    ResultAdapter resultAdapter = new ResultAdapter(SearchingActivity.this, experimentList);

                    resultAdapter.notifyDataSetChanged();
                    experimentListView.setAdapter(resultAdapter);

                    experimentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent (SearchingActivity.this, DisplayExperimentActivity.class);
                            Experiment exp_obj = experimentList.get(position);
                            System.out.println("Searching activity " + exp_obj);
                            System.out.println("EXP ID " + exp_obj.getID());
                            System.out.println("Experiment Type " + exp_obj.getExpType());
                            intent.putExtra("experiment",exp_obj);
                            intent.putExtra("position",position);

                            int LAUNCH_SECOND_ACTIVITY = 1;
                            startActivityForResult(intent,LAUNCH_SECOND_ACTIVITY);
                        }
                    });
                }
            }
        });

         */




    }

    private void setUpRecyclerView() {
        Query query = experimentRef;

        System.out.println("Broken here 1");

        FirestoreRecyclerOptions<Experiment> options = new FirestoreRecyclerOptions.Builder<Experiment>()
                .setQuery(query, Experiment.class)
                .build();

        System.out.println("Broken here 2");

        adapter = new ResultAdapterTest(options);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        System.out.println("Broken here 3");
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}