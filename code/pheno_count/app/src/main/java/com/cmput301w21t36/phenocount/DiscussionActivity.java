package com.cmput301w21t36.phenocount;

import android.content.Intent;
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
    private ArrayAdapter<Question> queAdapter;
    private ArrayList<Question> queData;
    private Experiment experiment;
    private DiscussionManager disManager;
    private User user; //I think we need to get who is currently viewing this forum



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discussion);
        experiment = (Experiment) getIntent().getSerializableExtra("experiment");//defining the Experiment object
        qListView = findViewById(R.id.question_list_view);
        getSupportActionBar().setTitle("Discussion Forum");
        disManager = new DiscussionManager(experiment);
        disManager.updateQueData();
        queData = disManager.getQueDataList();
        queAdapter = new QuestionAdapter(this, queData);
        qListView.setAdapter(queAdapter);

        queAdapter.notifyDataSetChanged(); // Notifying the adapter to render any new data fetched from the cloud.





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
                System.out.println("WHAT IS THIS GIVING? " + queData.get(position).getID());
                Question queTarget = queData.get(position);
                browseReplies(queTarget);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        queData = disManager.getQueDataList();
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
        disManager.addQueDoc(text);
        queData = disManager.getQueDataList();
        queAdapter.notifyDataSetChanged(); // Notifying the adapter to render any new data fetched from the cloud.

        Toast.makeText(DiscussionActivity.this, "A new question is posted!", Toast.LENGTH_SHORT).show();
}

    /**
     * @param target
     * When you click on a question in the list view, you will be
     * transferred to this question's activity page, where you
     * can browse all its replies and add replies
     */
    public void browseReplies(Question target){
        //String questionText = target.getText();
        Intent intent = new Intent(DiscussionActivity.this, QuestionActivity.class);
        intent.putExtra("experiment", experiment);
        intent.putExtra("question123", target);
        System.out.println("IN BROWSE REPLIES"+ target.getID());
        startActivity(intent);

    }




}