package com.cmput301w21t36.phenocount;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;

/**
 * @author: Charffy
 * This QuestionActivity class display basic information about a question.
 * When one question on the list view
 * in the discussion forum is clicked, you are transferred to the question's own
 * activity page, where you can browse all its replied and give replies.
 */
public class QuestionActivity extends AppCompatActivity implements ShowFragment.OnFragmentInteractionListener{
    //a collection of question posts of a certain experiment
    private ListView rListView;
    private ArrayAdapter<Reply> repAdapter;
    private ArrayList<Reply> repData;
    private Experiment experiment;
    private Question question;
    private DiscussionManager disManager;
    private User user; //I think we need to get who is currently viewing this forum

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        experiment = (Experiment) getIntent().getSerializableExtra("experiment");//defining the Experiment object
        question = (Question) getIntent().getSerializableExtra("question123");//defining the Experiment object

        rListView = findViewById(R.id.reply_list_view);
        //System.out.println("TYPE OF LIST VIEW " + rListView.getClass());
        System.out.println("QUESTION" + question.getID());
        System.out.println("EXPERIMENT" + experiment.getName());

        disManager = new DiscussionManager(experiment, question);


        disManager.updateRepData();


        System.out.println(disManager);
        repData = disManager.getRepDataList();
        System.out.println("SIZE OF REP DATA" + repData.size());
        repAdapter = new ReplyAdapter(this, repData);


        rListView.setAdapter(repAdapter);
        /*
        When the 'add reply' button is pressed in this activity,
        a fragment will display to let the user add a new reply.
        */

        final ExtendedFloatingActionButton addQueButton = findViewById(R.id.add_reply_btn);
        addQueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addReply();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        repData = disManager.getRepDataList();
        repAdapter.notifyDataSetChanged();

    }

    /**
     * This method allows user to add a reply to the question,
     * the newly added question will be stored in the discussion forum.
     * how: click '+' button in UI, a fragment shows, has two text boxes,
     * the top one is a title, says 'Add a Reply',
     * the bottom one is the an edit text, let the user type in the reply body.
     */
    public void addReply(){
        //show the fragment for adding a reply
        String type = "reply";
        new ShowFragment(type).show(getSupportFragmentManager(), "ADD_REP");
        //depend on the user click 'OK' or 'Cancel',
        //the newly created reply can be saved or discarded

    }

    /**
     * @param text
     * Inside this 'add reply' fragment, if 'OK' is pressed, then
     * this reply is saved to the question's reply list.
     * If 'Cancel' is pressed, this reply is deleted.
     */
    @Override
    public void onOkPressedAdd(String text) {
        disManager.addRepDoc(text);
        Toast.makeText(QuestionActivity.this, "A new reply is posted!", Toast.LENGTH_SHORT).show();

    }
}
