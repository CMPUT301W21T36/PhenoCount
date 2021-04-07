package com.cmput301w21t36.phenocount;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ShowSubscribedListActivity extends AppCompatActivity {
    FirebaseFirestore db;
    ListView subExperiments;
    ArrayList<Experiment> expDataList;
    ArrayAdapter<Experiment> expAdapter;
    ExpManager manager = new ExpManager();
    String UUID;
    DatabaseManager dbManager = new DatabaseManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_PhenoCount);
        setContentView(R.layout.activity_show_subscribed_list);
        //getSupportActionBar().setTitle("My Subscribed Experiments");

        db = dbManager.getDb();
        UUID = getIntent().getExtras().getString("owner");
        subExperiments = findViewById(R.id.subList);
        expDataList = new ArrayList<>();
        expAdapter = new ExperimentAdapter(this,expDataList);
        subExperiments.setAdapter(expAdapter);
        manager.getSubExpData(db, expDataList, expAdapter, UUID, 1); // To populate our experiment list

        subExperiments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent (ShowSubscribedListActivity.this,DisplayExperimentActivity.class);
                Experiment exp_obj = expDataList.get(position);
                intent.putExtra("experiment",exp_obj);
                intent.putExtra("position",position);

                int LAUNCH_SECOND_ACTIVITY = 1;
                startActivityForResult(intent,LAUNCH_SECOND_ACTIVITY);
            }
        });
        expAdapter.notifyDataSetChanged();
    }


}