package com.cmput301w21t36.phenocount;

import android.content.Intent;
import android.content.SharedPreferences;
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
 * This QuestionActivity class display all the replies given by
 * users related to a certain question.
 * Context: DisplayExperimentActivity ->'Discuss' button -> DiscussionActivity -> this;
 * How to use:
 * In DiscussionActivity, when a question in the list view is clicked,
 * the user is transferred to the QuestionActivity page, where they can
 * browse all the replies the question has received.
 */
public class QuestionActivity extends AppCompatActivity implements ShowFragment.OnFragmentInteractionListener,NavigationView.OnNavigationItemSelectedListener {
    //a collection of question posts of a certain experiment
    private ListView rListView;
    private ReplyAdapter repAdapter;
    private ArrayList<Reply> repData = new ArrayList<>();
    private Experiment experiment;
    private Question question;
    private DiscussionManager disManager;
    private Menu expMenu;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    androidx.appcompat.widget.Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_PhenoCount);
        setContentView(R.layout.activity_question);
        //getSupportActionBar().setTitle("Replies");
        navigationSettings();

        experiment = (Experiment) getIntent().getSerializableExtra("experiment");//defining the Experiment object
        question = (Question) getIntent().getSerializableExtra("question");//defining the Experiment object

        rListView = findViewById(R.id.reply_list_view);
        repAdapter = new ReplyAdapter(this, repData);
        rListView.setAdapter(repAdapter);

        disManager = new DiscussionManager(experiment, question);
        disManager.updateRepData(repData, repAdapter,rListView);
        repData = disManager.getRepDataList();
        repAdapter.notifyDataSetChanged();


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
    }

    /*
     * This method allows user to add a reply to the question,
     * the newly added question will be stored in the discussion forum.
     * how: click 'Add Reply' button in UI, a fragment shows, has two text boxes,
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

    /*
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
                intent = new Intent(QuestionActivity.this,MainActivity.class);
                break;
            case R.id.nav_search:
                intent = new Intent(QuestionActivity.this,SearchingActivity.class);
                break;
            case R.id.nav_user:
                intent = new Intent(QuestionActivity.this,ProfileActivity.class);
                intent.putExtra("UUID",UUID);
                break;
            case R.id.nav_add:
                intent = new Intent(QuestionActivity.this,PublishExperimentActivity.class);
                intent.putExtra("AutoId",UUID);
                intent.putExtra("mode",0);
                break;
            case R.id.nav_sub_exp:
                intent = new Intent(QuestionActivity.this,ShowSubscribedListActivity.class);
                intent.putExtra("owner",UUID);
                break;

        }

        startActivity(intent);
        return true;
    }

}
