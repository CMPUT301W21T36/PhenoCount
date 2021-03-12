package com.cmput301w21t36.phenocount;

import android.app.Application;
import android.util.Log;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//This class is used to manage the question list, it is used to store all available experiments
//created by the user.
//It is used in the class MainActivity for retrieving the experiment list.
//It is used in the class RecordActivity for retrieve the current experiment for recording,
//either just created or selected to recreate.

public class DiscussionManager{
    private ArrayList<Question> queDataList = new ArrayList<>();

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Question");
    private String TAG = "Discussion";


    public DiscussionManager(){
    }

    //update the collection Question in the database
    public void updateQueCol(){
        // Now listening to all the changes in the database and get notified, note that offline support is enabled by default.
        // Note: The data stored in Firestore is sorted alphabetically and per their ASCII values. Therefore, adding a new city will not be appended to the list.
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            //tracking the changes in the collection 'Question'
            public void onEvent(@Nullable QuerySnapshot questions, @Nullable FirebaseFirestoreException e) {
                // clear the old list
                queDataList.clear();
                //add documents in the collection to the list view
                for (QueryDocumentSnapshot que : questions) {
                    Log.d(TAG, String.valueOf(que.getId()));
                    String qText = (String) que.getData().get("text");
                    User qAuthor =  (User) que.getData().get("author");
                    queDataList.add(new Question(qAuthor, qText));
                }
                //queAdapter.notifyDataSetChanged(); // Notifying the adapter to render any new data fetched from the cloud.
            }
        });
    }


    public void addQueDoc(String text){
        //Use HashMap to store a key-value pair in firestore.
        HashMap<String, String> data = new HashMap<>();
        if (text.length() > 0) { // We do not add anything if the fields are empty.

            // If there is some data in the EditText field, then we create a new key-value pair.
            data.put("text", text);

            // The set method sets a unique id for the document.
            collectionReference
                    .add(data)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            // These are a method which gets executed when the task is successful.
                            Log.d(TAG, "Question has been added successfully!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // This method gets executed if there is any problem.
                            Log.d(TAG, "Question could not be added!" + e.toString());
                        }
                    });

            // Setting the fields to null so the user can add a new city.
        }

    }




    public ArrayList<Question> getQuestions() {
        return queDataList;
    }




    public Question getCurrentQuestion(int index) {
        Question currentQue = queDataList.get(index);
        return currentQue;
    }

}
