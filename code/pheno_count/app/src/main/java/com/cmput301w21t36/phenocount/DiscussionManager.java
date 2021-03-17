package com.cmput301w21t36.phenocount;

import android.provider.ContactsContract;
import android.util.Log;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;


//This class is used to manage the question list, it is used to store all available experiments
//created by the user.
//It is used in the class MainActivity for retrieving the experiment list.
//It is used in the class RecordActivity for retrieve the current experiment for recording,
//either just created or selected to recreate.

public class DiscussionManager{
    private ArrayList<Question> queDataList = new ArrayList<>();
    private ArrayAdapter<Question> queAdapter;
    private ArrayList<Reply> repDataList = new ArrayList<>();
    private DatabaseManager dbManager = new DatabaseManager();
    private String TAG = "Discussion";

    public FirebaseFirestore getDb() {
        return dbManager.getDb();
    }

    public DiscussionManager(DatabaseManager dbManager){
        this.dbManager = dbManager;
    }

    public DiscussionManager(Experiment experiment){
        String expID = experiment.getID();
        setUpQueCol(expID);
    }

    public DiscussionManager(Experiment experiment, Question question){
        String expID = experiment.getID();
        String qID = question.getID();
        setUpRepCol(expID, qID);
    }

    private void setUpQueCol(String expID) {
        setQuecollectionReference(getDb().collection("Experiment")
                                .document(expID)
                                .collection("Question"));
    }



    private void setUpRepCol(String expID, String qID) {

        setRepcollectionReference(getDb().collection("Experiment")
                .document(expID)
                .collection("Question")
                .document(qID)
                .collection("Reply"));
    }


    //update the Question ListView in the discussion forum activity
    public void updateQueData(ArrayList<Question> qDataList, QuestionAdapter qAdapter){
        this.queDataList = qDataList;
        System.out.println("Charffy: update  works?");
        // Now listening to all the changes in the database and get notified, note that offline support is enabled by default.
        // Note: The data stored in Firestore is sorted alphabetically and per their ASCII values. Therefore, adding a new city will not be appended to the list.
        getQuecollectionReference().addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            //tracking the changes in the collection 'Question'
            public void onEvent(@Nullable QuerySnapshot questions, @Nullable FirebaseFirestoreException e) {
                System.out.println("Charffy: event listener works?");

                // clear the old list
                queDataList.clear();
                //add documents in the question collection to the list view
                for (QueryDocumentSnapshot que : questions) {
                    System.out.println("Charffy: query doc snap shot works?");
                    String qID =  que.getId();
                    Log.d(TAG, qID);
                    String qText = (String) que.getData().get("text");
                    //User qAuthor =  (User) que.getData().get("author");
                    Question newQue = new Question(qText);
                    newQue.setID(qID);
                    queDataList.add(newQue);
                    System.out.println(qText);
                }
                System.out.println("Charffy Update: size of qDataList" + queDataList.size());
                qAdapter.notifyDataSetChanged(); // Notifying the adapter to render any new data fetched from the cloud.
            }
        });
    }

    //update the Reply ListView in the question activity
    public void updateRepData(){
        // Now listening to all the changes in the database and get notified, note that offline support is enabled by default.
        // Note: The data stored in Firestore is sorted alphabetically and per their ASCII values. Therefore, adding a new city will not be appended to the list.
        getRepcollectionReference().addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            //tracking the changes in the collection 'Reply'
            public void onEvent(@Nullable QuerySnapshot replies, @Nullable FirebaseFirestoreException e) {

                // clear the old list
                repDataList.clear();
                //add documents in the question collection to the list view
                for (QueryDocumentSnapshot rep : replies) {

                    String rID =  rep.getId();
                    Log.d(TAG, rID);
                    String rText = (String) rep.getData().get("text");
                    //User qAuthor =  (User) que.getData().get("author");
                    Reply newRep = new Reply(rText);
                    newRep.setID(rID);
                    repDataList.add(newRep);
                    System.out.println("HELLOOOO updateDataREP()" + repDataList.size());
                }
                //repAdapter.notifyDataSetChanged(); // Notifying the adapter to render any new data fetched from the cloud.
            }
        });
    }


    public void addQueDoc(String text){
        //Use HashMap to store a key-value pair in firestore.
        HashMap<String, String> data = new HashMap<>();
        if (text.length() > 0) { // We do not add anything if the fields are empty.
            String id = getQuecollectionReference().document().getId();

            // If there is some data in the EditText field, then we create a new key-value pair.
            data.put("text", text);
            // The set method sets a unique id for the document.
            getQuecollectionReference()
                    .document(id)
                    .set(data)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
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

            //updateQueData();
        }

    }

    public void addRepDoc(String text){
        //Use HashMap to store a key-value pair in firestore.
        HashMap<String, String> data = new HashMap<>();
        if (text.length() > 0) { // We do not add anything if the fields are empty.
            String id = getRepcollectionReference().document().getId();
            // If there is some data in the EditText field, then we create a new key-value pair.
            data.put("text", text);
            // The set method sets a unique id for the document.
            getRepcollectionReference()
                    .document(id)
                    .set(data)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // These are a method which gets executed when the task is successful.
                            Log.d(TAG, "Reply has been added successfully!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // This method gets executed if there is any problem.
                            Log.d(TAG, "Reply could not be added!" + e.toString());
                        }
                    });

            //updateRepData();
        }

    }

    public ArrayList<Question> getQueDataList() {
        System.out.println("Charffy getData: size of qDataList" + queDataList.size());
        return queDataList;
    }

    public ArrayList<Reply> getRepDataList() {
        return repDataList;
    }

    public CollectionReference getQuecollectionReference() {
        return dbManager.getQuecollectionReference();
    }

    public void setQuecollectionReference(CollectionReference quecollectionReference) {
        dbManager.setQuecollectionReference(quecollectionReference);
    }

    public CollectionReference getRepcollectionReference() {
        return dbManager.getRepcollectionReference();}

    public void setRepcollectionReference(CollectionReference repcollectionReference) {
        dbManager.setRepcollectionReference(repcollectionReference);
    }
}
