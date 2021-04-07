package com.cmput301w21t36.phenocount;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ShowSubscribedListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    FirebaseFirestore db;
    ListView subExperiments;
    ArrayList<Experiment> expDataList;
    ArrayAdapter<Experiment> expAdapter;
    ExpManager manager = new ExpManager();
    String UUID;
    DatabaseManager dbManager = new DatabaseManager();
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    androidx.appcompat.widget.Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_PhenoCount);
        setContentView(R.layout.activity_show_subscribed_list);
        //getSupportActionBar().setTitle("My Subscribed Experiments");
        navigationSettings();
        db = dbManager.getDb();
        UUID = getIntent().getExtras().getString("owner");
        subExperiments = findViewById(R.id.subList);
        expDataList = new ArrayList<>();
        expAdapter = new ExperimentAdapter(this,expDataList);
        subExperiments.setAdapter(expAdapter);
        manager.getSubExpData(db, expDataList, expAdapter, UUID, 1); // To populate our experiment list

        subExperiments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent (ShowSubscribedListActivity.this,DisplayExperimentActivity.class);
                Experiment exp_obj = expDataList.get(position);
                intent.putExtra("experiment",exp_obj);
                intent.putExtra("position",position);

                int LAUNCH_SECOND_ACTIVITY = 1;
                startActivityForResult(intent,LAUNCH_SECOND_ACTIVITY);
            }
        });
        expAdapter.notifyDataSetChanged();
    }

    public void navigationSettings(){
        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        navigationView.bringToFront();
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
        String uUID = sharedPrefs.getString("ID", "");
        Intent intent = new Intent();
        switch (item.getItemId()){
            case R.id.nav_my_exp:
                intent = new Intent(ShowSubscribedListActivity.this,MainActivity.class);
                break;
            case R.id.nav_search:
                intent = new Intent(ShowSubscribedListActivity.this,SearchingActivity.class);
                break;
            case R.id.nav_user:
                intent = new Intent(ShowSubscribedListActivity.this,ProfileActivity.class);
                intent.putExtra("UUID",uUID);
                break;
            case R.id.nav_add:
                intent = new Intent(ShowSubscribedListActivity.this,PublishExperimentActivity.class);
                intent.putExtra("AutoId",uUID);
                intent.putExtra("mode",0);
                break;
            case R.id.nav_sub_exp:
                intent = new Intent(ShowSubscribedListActivity.this,ShowSubscribedListActivity.class);
                intent.putExtra("owner",uUID);
                break;

        }

        startActivity(intent);
        return true;
    }


}