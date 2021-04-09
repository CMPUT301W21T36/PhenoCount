//References:
// codedmin, 2017-10-26, https://www.codingdemos.com/android-options-menu-icon/#:~:text=Click%20res%20%E2%86%92%20New%20%E2%86%92,grouped%20under%20a%20single%20Menu%20.
package com.cmput301w21t36.phenocount;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * This is the MainActivity and the every first screen of the app
 * and displays the experiments owned by the current user
 */
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private FirebaseFirestore db;
    private ListView experiments;
    private ArrayList<Experiment> expDataList;
    private ArrayAdapter<Experiment> expAdapter;
    private ExpManager manager = new ExpManager();
    private DatabaseManager dbManager = new DatabaseManager();
    static final String AutoID = "ID";
    private String UUID;
    private String username;
    private String phoneNumber;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private androidx.appcompat.widget.Toolbar toolbar;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_PhenoCount);
        setContentView(R.layout.activity_main);

        TextView toolBarTitle = (TextView)findViewById(R.id.toolbar_title);
        toolBarTitle.setText("My Experiments");
        navigationSettings();

        experiments = findViewById(R.id.expList);
        expDataList = new ArrayList<>();

        // To get instance of the database
        db = dbManager.getDb();

        SharedPreferences sharedPrefs = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        boolean firstStart = sharedPrefs.getBoolean("firstStart",true );

        //checks if this is the first time the user is using the application
        if (firstStart) {
            makeUser(sharedPrefs, db);
        }

        // This is the UUID for the current user using the app
        // It will save over instances of the app and is only updated upon first open after install
        UUID = sharedPrefs.getString(AutoID, "");

        /**
         * Will retrieve the Username for the user and set the variable username
         * to the returned String
         */
        DocumentReference userRef = db.collection("User").document(UUID);
        userRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        username = documentSnapshot.getString("Username");
                        phoneNumber = documentSnapshot.getString("ContactInfo");

                        SharedPreferences sharedPrefs = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPrefs.edit();

                        editor.putString("Username", username);
                        editor.putString("ContactInfo",phoneNumber);
                        editor.apply();
                    }
                });

        phoneNumber = sharedPrefs.getString("ContactInfo", "");

        expAdapter = new ExperimentAdapter(this,expDataList);
        experiments.setAdapter(expAdapter);
        manager.getExpData(db, expDataList, expAdapter, UUID, 0, experiments); // To populate our experiment list

        experiments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent (MainActivity.this,DisplayExperimentActivity.class);
                Experiment exp_obj = expDataList.get(position);
                intent.putExtra("experiment",exp_obj);
                intent.putExtra("position",position);

                int LAUNCH_SECOND_ACTIVITY = 1;
                startActivityForResult(intent,LAUNCH_SECOND_ACTIVITY);
            }
        });
        expAdapter.notifyDataSetChanged();
    }


    /**
     * This method is called when the option to
     * Publish an experiment is clicked and Switches to
     * PublishExperimentActivity
     */
    public void addExperiment(){
        Intent intent = new Intent(this, PublishExperimentActivity.class);
        String mstr = UUID;
        intent.putExtra("AutoId",mstr);
        intent.putExtra("mode",0);
        startActivity(intent);
    }

    /**
     * This method is called when the option to
     * view the subscribe list of experiments is clicked
     * and Switches to ShowSubscribedListActivity
     *
     */
    public void showSubList(){
        Intent intent = new Intent(this, ShowSubscribedListActivity.class);
        intent.putExtra("owner",UUID);
        startActivity(intent);
    }

    /**
     * This method is called when the option to
     * search is clicked and Switches to
     * SearchingActivity
     */
    public void openSearch() {
        Intent intent = new Intent(this, SearchingActivity.class);
        startActivity(intent);
    }

    /**
     * This method is called when the option to view
     * profile is clicked and Switches to
     * ProfileActivity
     */
    public void openProfile() {
        Intent intent = new Intent(this, ProfileActivity.class);
        String ID = UUID;
        String name = username;
        intent.putExtra("UUID",ID);
        startActivity(intent);
    }

    /**
     * It creates a new user
     * @param sharedPrefs
     * @param db
     */
    public void makeUser(SharedPreferences sharedPrefs, FirebaseFirestore db) {
        final DocumentReference userReference = db.collection("User").document();
        // Auto-ID created for the document
        String GrabbedID = userReference.getId();

        SharedPreferences.Editor editor = sharedPrefs.edit();

        editor.putString(AutoID, GrabbedID);

        editor.putBoolean("firstStart", false);
        editor.apply();

        // This is the UUID for the current user using the app
        // It will save over instances of the app and is only updated upon first open after install
        UUID = sharedPrefs.getString(AutoID, "");

        Map<String, Object> user = new HashMap<>();
        user.put("UID", UUID);
        user.put("Username", "New User");
        user.put("ContactInfo", "No contact info");

        db.collection("User").document(UUID).set(user);
    }

    /**
     * Settings for the navigation drawer
     */
    public void navigationSettings(){
        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);
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
        switch (item.getItemId()){
            case R.id.nav_my_exp:
                onBackPressed();
                break;
            case R.id.nav_search:
                openSearch();
                break;
            case R.id.nav_user:
                openProfile();
                break;
            case R.id.nav_add:
                drawerLayout.closeDrawer(GravityCompat.START);
                addExperiment();

                break;
            case R.id.nav_sub_exp:
                showSubList();
                break;
        }
        return true;
    }
}