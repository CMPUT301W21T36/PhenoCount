package com.cmput301w21t36.phenocount;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;

/**
 * @author Charffy
 * This DiscussionActivity class stores all the questions asked by
 * users related to a certain experiment.
 * Context: ExperimentActivity -> MenuActivity-> 'Discuss' button -> this;
 * Click 'Discuss' button in the MenuActivity can transfer user to the this activity,
 * so the DiscussionActivity class has its own UI.
 *
 */
public class DiscussionActivity extends AppCompatActivity implements ShowFragment.OnFragmentInteractionListener{
    //a collection of question posts of a certain experiment
    private ListView qList;
    ArrayAdapter<Experiment> expAdapter;
    private ArrayList<Question> questions;
    private Experiment experiment;
    private User user; //I think we need to get who is currently viewing this forum

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.discussion_activity);

        /*
        When the 'add question' button is pressed in this activity,
        a fragment will display to let the user ask a new question.
         */
        final ExtendedFloatingActionButton addQueButton = findViewById(R.id.add_question_btn);
        addQueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addQuestion();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        expAdapter.notifyDataSetChanged();

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
     * Inside this 'ask question' fragment, if 'OK' is pressed, then
     * this question is saved to the discussion forum's question list.
     * If 'Cancel' is pressed, this question is deleted.
     */
    @Override
    public void onOkPressedAdd(String text) {
        Question newQue = new Question(user, text);
        questions.add(newQue);
        Toast.makeText(DiscussionActivity.this, "A new question is posted!", Toast.LENGTH_SHORT).show();
    }




}
