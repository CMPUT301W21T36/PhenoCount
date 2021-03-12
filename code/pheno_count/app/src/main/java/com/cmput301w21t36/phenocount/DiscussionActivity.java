package com.cmput301w21t36.phenocount;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Charffy
 * This DiscussionActivity class stores all the questions asked by
 * users related to a certain experiment.
 * Context: ExperimentActivity -> MenuActivity-> 'Discuss' button -> this;
 * Click 'Discuss' button in the MenuActivity can transfer user to the this activity,
 * so the DiscussionActivity class has its own UI.
 * When a question in the lsit view is clicked, the user is transferred to
 * the QuestionActivity page, where they can browse all the replies the question
 * has received.
 */
public class DiscussionActivity extends AppCompatActivity implements ShowFragment.OnFragmentInteractionListener{
    //a collection of question posts of a certain experiment
    private ListView qListView;
    ArrayAdapter<Question> queAdapter;
    private ArrayList<Question> queData;
    private Experiment experiment;
    private User user; //I think we need to get who is currently viewing this forum
    private FirebaseFirestore db;
    private CollectionReference collectionReference;
    private String TAG = "Discussion";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discussion);

        qListView = findViewById(R.id.question_list_view);
        queAdapter = new CustomAdapter(this, R.layout.content_question, queData);
        qListView.setAdapter(queAdapter);

        //Access a Cloud Firestore instance from your Activity
        db = FirebaseFirestore.getInstance();
        // Get a top-level reference to the collection.
        collectionReference = db.collection("Question");


        // Now listening to all the changes in the database and get notified, note that offline support is enabled by default.
        // Note: The data stored in Firestore is sorted alphabetically and per their ASCII values. Therefore, adding a new city will not be appended to the list.
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            //tracking the changes in the collection 'Question'
            public void onEvent(@Nullable QuerySnapshot questions, @Nullable FirebaseFirestoreException e) {
                // clear the old list
                queData.clear();
                //add documents in the collection to the list view
                for (QueryDocumentSnapshot que : questions) {
                    Log.d(TAG, String.valueOf(que.getId()));
                    String qText = (String) que.getData().get("text");
                    User qAuthor =  (User) que.getData().get("author");
                    queData.add(new Question(qAuthor, qText));
                }
                queAdapter.notifyDataSetChanged(); // Notifying the adapter to render any new data fetched from the cloud.
            }
        });

        /*
        When the 'ask question' button is pressed in this activity,
        a fragment will display to let the user ask a new question.
         */
        final ExtendedFloatingActionButton addQueButton = findViewById(R.id.add_question_btn);
        addQueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addQuestion();
            }
        });

        /*
         Select a question on the list view for browsing its replies or add replies
         */
        qListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //when you click on a question for browsing/add replies
                Question queTarget = queData.get(position);
                browseReplies(queTarget);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        queAdapter.notifyDataSetChanged();

    }

    /**
     * This method allows user to ask a question of the experiment,
     * the newly added question will be stored in the discussion forum.
     * how: click '+' button in UI, a fragment shows, has two text boxes,
     * the top one is a title, says 'Add a Question',
     * the bottom one is the an edit text, let the user type in the question body.
     */
    public void addQuestion(){
        //show the fragment for asking a question
        String type = "question";
        new ShowFragment(type).show(getSupportFragmentManager(), "ADD_QUE");
        //depend on the user click 'OK' or 'Cancel',
        //the newly created question can be saved or discarded

    }

    /**
     * @param text
     * Inside this 'ask question' fragment, if 'OK' is pressed, then
     * this question is saved to the discussion forum's question list.
     * If 'Cancel' is pressed, this question is deleted.
     */
    @Override
    public void onOkPressedAdd(String text) {
        //Use HashMap to store a key-value pair in firestore.
        HashMap<String, String> data = new HashMap<>();
        if (text.length() > 0) { // We do not add anything if the fields are empty.

            // If there is some data in the EditText field, then we create a new key-value pair.
            data.put("text", text);

            // The set method sets a unique id for the document.
            collectionReference
                    .document(text) //should this be the question's text as the document reference?
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

            // Setting the fields to null so the user can add a new city.
        }
    }




        Question newQue = new Question(user, text);
        queData.add(newQue);
        Toast.makeText(DiscussionActivity.this, "A new question is posted!", Toast.LENGTH_SHORT).show();
    }

    /**
     * @param target
     * When you click on a question in the list view, you will be
     * transferred to this question's activity page, where you
     * can browse all its replies and add replies
     */
    public void browseReplies(Question target){
        String questionText = target.getText();
        Intent intent = new Intent(this, QuestionActivity.class);
        intent.putExtra("questionText", questionText);
        startActivity(intent);

    }




}
