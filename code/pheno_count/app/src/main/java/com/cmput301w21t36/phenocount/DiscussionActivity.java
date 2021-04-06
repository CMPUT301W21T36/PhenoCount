package com.cmput301w21t36.phenocount;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import java.util.ArrayList;


/*
 * Role: view
 * This DiscussionActivity class display all the questions asked by
 * users related to a certain experiment.
 * Context: DisplayExperimentActivity ->'Discuss' button -> this.
 * How to use:
 * Click 'Discuss' button in the menu can transfer user to the this activity,
 * When a question in the list view is clicked, the user is transferred to
 * the QuestionActivity page, where they can browse all the replies the question
 * has received.
 */
public class DiscussionActivity extends AppCompatActivity implements ShowFragment.OnFragmentInteractionListener{
    //a collection of question posts of a certain experiment
    private ListView qListView;
    private QuestionAdapter queAdapter;
    private ArrayList<Question> queData = new ArrayList<>();
    private Experiment experiment;
    private DiscussionManager disManager;
    private Menu expMenu;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_PhenoCount);
        setContentView(R.layout.activity_discussion);
        //getSupportActionBar().setTitle("Discussion Forum");

        experiment = (Experiment) getIntent().getSerializableExtra("experiment");//defining the Experiment object
        qListView = findViewById(R.id.question_list_view);
        queAdapter = new QuestionAdapter(this, queData);
        qListView.setAdapter(queAdapter);

        disManager = new DiscussionManager(experiment);
        disManager.updateQueData(queData, queAdapter);
        queData = disManager.getQueDataList();
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
    }

    /*
     * This method allows user to ask a question of the experiment,
     * the newly added question will be stored in the discussion forum.
     * how: click 'Ask Question' button in UI, a fragment shows, has two text boxes,
     * the top one is a title, says 'Ask a Question',
     * the bottom one is the an edit text, let the user type in the question body.
     */
    public void addQuestion(){
        //show the fragment for asking a question
        String type = "question";
        new ShowFragment(type).show(getSupportFragmentManager(), "ADD_QUE");
        //depend on the user click 'OK' or 'Cancel',
        //the newly created question can be saved or discarded

    }

    /*
     * @param text
     * Inside this 'ask question' fragment, if 'OK' is pressed, then
     * this question is saved to the discussion forum's question list.
     * If 'Cancel' is pressed, this question is deleted.
     */
    @Override
    public void onOkPressedAdd(String text) {
        disManager.addQueDoc(text);
        Toast.makeText(DiscussionActivity.this, "A new question is posted!", Toast.LENGTH_SHORT).show();
    }

    /*
     * @param target
     * When you click on a question in the list view, you will be
     * transferred to this question's activity page, where you
     * can browse all its replies and add replies
     */
    public void browseReplies(Question target){
        //String questionText = target.getText();
        Intent intent = new Intent(DiscussionActivity.this, QuestionActivity.class);
        intent.putExtra("experiment", experiment);
        intent.putExtra("question", target);
        startActivity(intent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.general_menu, menu);
        expMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.myList) {
            Intent intent = new Intent(DiscussionActivity.this, MainActivity.class);
            startActivity(intent);
        }
        if (item.getItemId() == R.id.search) {
            Intent intent = new Intent(DiscussionActivity.this, SearchingActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }


}