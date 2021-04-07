package com.cmput301w21t36.phenocount;

import android.provider.ContactsContract;
import android.util.Log;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
/*
 * Role: controller
 * Goal: This class is used to manage the question list in the DiscussionActivity,
 * and reply list in the QuestionActivity
 * How to use:
 * create an DatabaseManager object, which grants the access of collections Question and Reply
 * in the database.
 * It can be used to add documents to the collections,
 * and update the listview in DiscussionActivity and QuestionActivity
 */

public class DiscussionManager{
    private ArrayList<Question> queDataList = new ArrayList<>();
    private ArrayList<Reply> repDataList = new ArrayList<>();
    private DatabaseManager dbManager = new DatabaseManager();
    private String TAG = "Discussion";

    public DiscussionManager(Experiment experiment){
        String expID = experiment.getExpID();
        setUpQueCol(expID);
    }

    public DiscussionManager(Experiment experiment, Question question){
        String expID = experiment.getExpID();
        String qID = question.getID();
        setUpRepCol(expID, qID);
    }
    public FirebaseFirestore getDb() {
        return dbManager.getDb();
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
        // Now listening to all the changes in the database and get notified, note that offline support is enabled by default.
        // Note: The data stored in Firestore is sorted alphabetically and per their ASCII values. Therefore, adding a new city will not be appended to the list.
        getQuecollectionReference().addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            //tracking the changes in the collection 'Question'
            public void onEvent(@Nullable QuerySnapshot questions, @Nullable FirebaseFirestoreException e) {
                // clear the old list
                queDataList.clear();
                //add documents in the question collection to the list view
                for (QueryDocumentSnapshot que : questions) {
                    if(que!= null){
                        System.out.println("Que exist");

                    }else{
                        System.out.println("Que not exist");
                    }
                    String qID =  que.getId();
                    Log.d(TAG, qID);
                    String qText = (String) que.getData().get("text");

                    if(que.getData().get("text") != null){
                        System.out.println("Text is: ");
                        System.out.println((String) que.getData().get("text"));
                    }
                    if(que.getData().get("reply") != null){
                        System.out.println("Reply data is not null!");
                    }
                    long qReply = (long) que.getData().get("reply");
                    Question newQue = new Question(qText);
                    newQue.setID(qID);
                    newQue.setReply_num(qReply);
                    queDataList.add(newQue);
                }
                qAdapter.notifyDataSetChanged(); // Notifying the adapter to render any new data fetched from the cloud.
            }
        });
    }

    //update the Reply ListView in the question activity
    public void updateRepData(ArrayList<Reply> rDataList, ReplyAdapter rAdapter){
        this.repDataList = rDataList;
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
                    Reply newRep = new Reply(rText);
                    newRep.setID(rID);
                    repDataList.add(newRep);
                }
                rAdapter.notifyDataSetChanged(); // Notifying the adapter to render any new data fetched from the cloud.
            }
        });
    }


    public void addQueDoc(String text){
        //Use HashMap to store a key-value pair in firestore.
        HashMap<String, Object> data = new HashMap<>();
        if (text.length() > 0) { // We do not add anything if the fields are empty.
            String id = getQuecollectionReference().document().getId();
            // If there is some data in the EditText field, then we create a new key-value pair.
            data.put("text", text);
            data.put("reply", 0);
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
        }
    }

    public void addRepDoc(String text){
        //Use HashMap to store a key-value pair in firestore.
        HashMap<String, String> data = new HashMap<>();
        if (text.length() > 0) { // We do not add anything if the fields are empty.
            String id = getRepcollectionReference().document().getId();

            final long[] replies = {0};
            DocumentReference parent = getRepcollectionReference().getParent();
            parent.get().addOnCompleteListener(new OnCompleteListener <DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.getData()!= null) {
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            replies[0] = (long)document.getData().get("reply");
                            parent.update("reply", replies[0] + 1)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d(TAG, "DocumentSnapshot successfully updated!");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(TAG, "Error updating document", e);
                                        }
                                    });
                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });
            System.out.println("Original replies: ");
            System.out.println(replies[0]);


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
        }

    }

    public ArrayList<Question> getQueDataList() {
        return queDataList;
    }

    public ArrayList<Reply> getRepDataList() {
        return repDataList;
    }

    public CollectionReference getQuecollectionReference() {
        return dbManager.getQueCollectionReference();
    }

    public void setQuecollectionReference(CollectionReference queCollectionReference) {
        dbManager.setQueCollectionReference(queCollectionReference);
    }

    public CollectionReference getRepcollectionReference() {
        return dbManager.getRepCollectionReference();}

    public void setRepcollectionReference(CollectionReference repCollectionReference) {
        dbManager.setRepCollectionReference(repCollectionReference);
    }
}