package com.cmput301w21t36.phenocount;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

/**
 * Will grab all experiments from SearchManager and will display all in a listview for a graphical interface
 * Any experiments clicked will open DisplayExperimentActivity
 * @see SearchingManager
 * @see DisplayExperimentActivity
 */
public class SearchingActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference experimentRef = db.collection("Experiment");

    private com.cmput301w21t36.phenocount.SearchingManager searchManag;
    private ArrayList<com.cmput301w21t36.phenocount.Experiment> expDataList = new ArrayList<com.cmput301w21t36.phenocount.Experiment>();
    private ArrayList<com.cmput301w21t36.phenocount.Experiment> allExpDataList = new ArrayList<com.cmput301w21t36.phenocount.Experiment>();
    private com.cmput301w21t36.phenocount.ResultAdapter adapter;
    private ListView experimentListView;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    androidx.appcompat.widget.Toolbar toolbar;

    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_PhenoCount);
        setContentView(R.layout.activity_searching);

        navigationSettings();

        experimentListView = findViewById(R.id.listView);

        searchManag = new com.cmput301w21t36.phenocount.SearchingManager(this);

        adapter = new com.cmput301w21t36.phenocount.ResultAdapter(this, expDataList);
        experimentListView.setAdapter(adapter);

        searchManag.getAllExp(db, expDataList, adapter);
        // When experiment in listview is clicked, we open it and call new activity
        experimentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent (SearchingActivity.this, com.cmput301w21t36.phenocount.DisplayExperimentActivity.class);
                com.cmput301w21t36.phenocount.Experiment exp_obj = (com.cmput301w21t36.phenocount.Experiment) experimentListView.getAdapter().getItem(position);
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

        Button button = findViewById(R.id.QrButton);

        searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SearchingActivity.this, com.cmput301w21t36.phenocount.ScanBarcodeActivity.class);
                startActivityForResult(i, 1);
//                searchView.setQuery(searchText, true);
            }
        });

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
                intent = new Intent(SearchingActivity.this, com.cmput301w21t36.phenocount.MainActivity.class);
                break;
            case R.id.nav_search:
                intent = new Intent(SearchingActivity.this,SearchingActivity.class);
                break;
            case R.id.nav_user:
                intent = new Intent(SearchingActivity.this, com.cmput301w21t36.phenocount.ProfileActivity.class);
                intent.putExtra("UUID",UUID);
                break;
            case R.id.nav_add:
                intent = new Intent(SearchingActivity.this, com.cmput301w21t36.phenocount.PublishExperimentActivity.class);
                intent.putExtra("AutoId",UUID);
                intent.putExtra("mode",0);
                break;
            case R.id.nav_sub_exp:
                intent = new Intent(SearchingActivity.this, com.cmput301w21t36.phenocount.ShowSubscribedListActivity.class);
                intent.putExtra("owner",UUID);
                break;

        }

        startActivity(intent);
        return true;
    }

    @Override
    //Sends the experiment object and retrieves the updated object
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                String scannedText = data.getSerializableExtra("scannedText").toString();
                searchView.setQuery(scannedText, true);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                System.out.println("No Data");
            }
        }

    }
}




