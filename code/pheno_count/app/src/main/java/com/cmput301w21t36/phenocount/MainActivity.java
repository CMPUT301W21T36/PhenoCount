package com.cmput301w21t36.phenocount;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
<<<<<<< HEAD
    private ImageButton profileButton;
    FirebaseFirestore db;
=======
    ListView experiments;
    ArrayList<Experiment> expDataList;
    ArrayAdapter<Experiment> expAdapter;

>>>>>>> 30f8586974f31896f209375cd64981c26839d33d

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        experiments = findViewById(R.id.expList);
        //Intent i = new Intent(this, LocationActivity.class);
        //a i.putExtra("info", info);
        //startActivityForResult(i, 1);
        expDataList = new ArrayList<Experiment>();

        Experiment exp = new Experiment("Coin Flip", "We flip a coin in this experiment","North America","Binomial", 10, false);
        expDataList.add(exp);

<<<<<<< HEAD
        db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection("User");

        profileButton = findViewById(R.id.profileButton);
=======
        expAdapter = new ExperimentList(this,expDataList);
        experiments.setAdapter(expAdapter);
>>>>>>> 30f8586974f31896f209375cd64981c26839d33d

        experiments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent (MainActivity.this,DisplayExperimentActivity.class);
                Experiment exp_obj = expDataList.get(position);
                intent.putExtra("experiment",exp_obj);
                intent.putExtra("position",position);

                int LAUNCH_SECOND_ACTIVITY = 1;
                startActivityForResult(intent,LAUNCH_SECOND_ACTIVITY);
            }
        });
    }

    public void displayExperiment(View view){
        Intent intent = new Intent(this, DisplayExperimentActivity.class);
        startActivity(intent);
    }

    public void addExperiment(View view){
        Intent intent = new Intent(this, PublishExperimentActivity.class);
        startActivity(intent);

    }

}