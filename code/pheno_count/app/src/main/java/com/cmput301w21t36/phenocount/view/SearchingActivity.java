package com.cmput301w21t36.phenocount;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * Will grab all experiments from SearchManager and will display all in a listview for a graphical interface
 * Any experiments clicked will open DisplayExperimentActivity
 * @see SearchingManager
 * @see DisplayExperimentActivity
 */
public class SearchingActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference experimentRef = db.collection("Experiment");

    private SearchingManager searchManag;
    private ArrayList<Experiment> expDataList = new ArrayList<Experiment>();
    private ArrayList<Experiment> allExpDataList = new ArrayList<Experiment>();
    private ResultAdapter adapter;
    private ListView experimentListView;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    androidx.appcompat.widget.Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_PhenoCount);
        setContentView(R.layout.activity_searching);

        navigationSettings();

        experimentListView = findViewById(R.id.listView);

        searchManag = new SearchingManager(this);

        adapter = new ResultAdapter(this, expDataList);
        experimentListView.setAdapter(adapter);

        searchManag.getAllExp(db, expDataList, adapter);

        // When experiment in listview is clicked, we open it and call new activity
        experimentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent (SearchingActivity.this,DisplayExperimentActivity.class);
                Experiment exp_obj = (Experiment) experimentListView.getAdapter().getItem(position);
                System.out.println(position);
                intent.putExtra("experiment",exp_obj);
                intent.putExtra("position",position);

                int second_activity = 1;
                startActivityForResult(intent,second_activity);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //https://stackoverflow.com/questions/27378981/how-to-use-searchview-in-toolbar-android
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_search,menu);

        MenuItem menuItem = menu.findItem(R.id.searchView);

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchManag.getSearchExp(query, adapter, expDataList, experimentListView);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchManag.getSearchExp(newText, adapter, expDataList, experimentListView);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    /*
    public void searchExperiments(String keyword) {
        keyword = keyword.toLowerCase();

        if (keyword.length() > 0) {

            ArrayList<Experiment> foundExp = new ArrayList<>();
            for (Experiment exp : expDataList) {
                if (exp.getDescription().toLowerCase().contains(keyword) || exp.getName().toLowerCase().contains(keyword) ){
                    foundExp.add(exp);
                }
            }
            updateList(foundExp);
        }
        else {
            updateList(expDataList);
        }

    }

    public void updateList(ArrayList<Experiment> listExp) {

        adapter = new ResultAdapter(this, listExp);
        experimentListView.setAdapter(adapter);

        adapter.notifyDataSetChanged();
    }

     */


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
        String UUID = sharedPrefs.getString("ID", "");
        Intent intent = new Intent();
        switch (item.getItemId()){
            case R.id.nav_my_exp:
                intent = new Intent(SearchingActivity.this,MainActivity.class);
                break;
            case R.id.nav_search:
                intent = new Intent(SearchingActivity.this,SearchingActivity.class);
                break;
            case R.id.nav_user:
                intent = new Intent(SearchingActivity.this,ProfileActivity.class);
                intent.putExtra("UUID",UUID);
                break;
            case R.id.nav_add:
                intent = new Intent(SearchingActivity.this,PublishExperimentActivity.class);
                intent.putExtra("AutoId",UUID);
                intent.putExtra("mode",0);
                break;
            case R.id.nav_sub_exp:
                intent = new Intent(SearchingActivity.this,ShowSubscribedListActivity.class);
                intent.putExtra("owner",UUID);
                break;

        }

        startActivity(intent);
        return true;
    }
}




