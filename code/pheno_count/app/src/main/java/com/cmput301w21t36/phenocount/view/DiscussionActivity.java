package com.cmput301w21t36.phenocount;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.navigation.NavigationView;

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
public class DiscussionActivity extends AppCompatActivity implements ShowFragment.OnFragmentInteractionListener,NavigationView.OnNavigationItemSelectedListener{
    //a collection of question posts of a certain experiment
    private ListView qListView;
    private QuestionAdapter queAdapter;
    private ArrayList<Question> queData = new ArrayList<>();
    private Experiment experiment;
    private DiscussionManager disManager;
    private Menu expMenu;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private androidx.appcompat.widget.Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_PhenoCount);
        setContentView(R.layout.activity_discussion);
        //getSupportActionBar().setTitle("Discussion Forum");
        navigationSettings();

        experiment = (Experiment) getIntent().getSerializableExtra("experiment");//defining the Experiment object
        qListView = findViewById(R.id.question_list_view);
        queAdapter = new QuestionAdapter(this, queData);
        qListView.setAdapter(queAdapter);

        disManager = new DiscussionManager(experiment);
        disManager.updateQueData(queData, queAdapter, qListView);
        queData = disManager.getQueDataList();
        queAdapter.notifyDataSetChanged(); // Notifying the adapter to render any new data fetched from the cloud.


        System.out.println("data size is: ");
        System.out.println(queData.size());
        System.out.println(queData);

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

    public void navigationSettings(){
        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        navigationView.bringToFront();
        toolbar.bringToFront();
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        SharedPreferences sharedPrefs = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        String UUID = sharedPrefs.getString("ID", "");
        Intent intent = new Intent();
        switch (item.getItemId()){
            case R.id.nav_my_exp:
                intent = new Intent(DiscussionActivity.this,MainActivity.class);
                break;
            case R.id.nav_search:
                intent = new Intent(DiscussionActivity.this,SearchingActivity.class);
                break;
            case R.id.nav_user:
                intent = new Intent(DiscussionActivity.this,ProfileActivity.class);
                intent.putExtra("UUID",UUID);
                break;
            case R.id.nav_add:
                intent = new Intent(DiscussionActivity.this,PublishExperimentActivity.class);
                intent.putExtra("AutoId",UUID);
                intent.putExtra("mode",0);
                break;
            case R.id.nav_sub_exp:
                intent = new Intent(DiscussionActivity.this,ShowSubscribedListActivity.class);
                intent.putExtra("owner",UUID);
                break;

        }

        startActivity(intent);
        return true;
    }


}