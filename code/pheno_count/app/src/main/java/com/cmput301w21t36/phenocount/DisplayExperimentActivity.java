// references: BrainCrash,2011-09-03,CC BY-SA 3.0, https://stackoverflow.com/a/6932112
package com.cmput301w21t36.phenocount;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

/**
 * This activity deals with displaying the contents of an experiment
 * To access this activity: Open the app -> click on the listView
 * or the experiment
 * @see MainActivity
 * @author Anisha
 * @author Marzook
 */
public class DisplayExperimentActivity extends AppCompatActivity {
    private Experiment exp; // catch object passed from mainlist
    FirebaseFirestore db;
    private final String TAG = "PhenoCount";
    private String username;
    private String UUID;
    private ExpManager expManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experiment_display);
        exp = (Experiment) getIntent().getSerializableExtra("experiment");//defining the Experiment object

        TextView expName = findViewById(R.id.nameTextView);
        TextView expDesc = findViewById(R.id.descTextView);
        TextView expOwner = findViewById(R.id.ownerTextView);
        TextView expRegion = findViewById(R.id.regionTextView);
        TextView expMinTrial = findViewById(R.id.minTrialView);
        TextView expStatus = findViewById(R.id.statusTextView);
        TextView expType = findViewById(R.id.expTypeText);
        TextView expReqLoc = findViewById(R.id.reqLocText);

        expName.setText(exp.getName());
        expDesc.setText(exp.getDescription());
        expOwner.setText(exp.getOwner().getProfile().getUsername());
        expRegion.setText(exp.getRegion());
        expMinTrial.setText(Integer.toString(exp.getMinimumTrials()));
        expType.setText(exp.getExpType());
        String mStat = "" ;
        switch(exp.getExpStatus()){
            case 1:
                mStat = "Published";
                break;
            case 2:
                mStat= "Ended";
                break;
            case 3:
                mStat = "Unpublished";
                break;
            default:
                mStat= "Added";

        }
        expStatus.setText(mStat);
        // Adding icon programmatically : BrainCrash,2011-09-03,CC BY-SA 3.0, https://stackoverflow.com/a/6932112
        if(exp.isRequireLocation()== true) {
            expReqLoc.setText(" REQUIRED");
            expReqLoc.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_warning, 0, 0, 0);
        }
        else {expReqLoc.setText("NOT REQUIRED");}

        final Button camerabtn = findViewById((R.id.camerabtn));
        camerabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DisplayExperimentActivity.this, ScanBarcodeActivity.class);
                startActivityForResult(i, 1);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.example_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //exp = (Experiment) getIntent().getSerializableExtra("experiment");//defining the Experiment object

            if(item.getItemId() == R.id.item1) {
                if (exp.getExpType().equals("Binomial")) {
                    Intent bintent = new Intent(DisplayExperimentActivity.this, BinomialActivity.class);
                    bintent.putExtra("experiment", exp);
                    int LAUNCH_SECOND_ACTIVITY = 1;
                    startActivityForResult(bintent, LAUNCH_SECOND_ACTIVITY);
                }
                if (exp.getExpType().equals("Count")) {
                    Intent cintent = new Intent(DisplayExperimentActivity.this, CountActivity.class);
                    cintent.putExtra("experiment", exp);
                    int LAUNCH_SECOND_ACTIVITY = 1;
                    startActivityForResult(cintent, LAUNCH_SECOND_ACTIVITY);
                }
                if (exp.getExpType().equals("Measurement")) {
                    Intent mintent = new Intent(DisplayExperimentActivity.this, MeasurementActivity.class);
                    mintent.putExtra("experiment", exp);
                    int LAUNCH_SECOND_ACTIVITY = 1;
                    startActivityForResult(mintent, LAUNCH_SECOND_ACTIVITY);
                }
                if (exp.getExpType().equals("NonNegativeCount")) {
                    Intent nintent = new Intent(DisplayExperimentActivity.this, NonNegativeCountActivity.class);
                    nintent.putExtra("experiment", exp);
                    int LAUNCH_SECOND_ACTIVITY = 1;
                    startActivityForResult(nintent, LAUNCH_SECOND_ACTIVITY);
                }
            }
            else if(item.getItemId() == R.id.item3) {
                Intent dintent = new Intent(DisplayExperimentActivity.this, DiscussionActivity.class);
                dintent.putExtra("experiment", exp);
                int LAUNCH_SECOND_ACTIVITY = 1;
                startActivity(dintent);
            }
            else if (item.getItemId() == R.id.item4){
                Intent tintent = new Intent(DisplayExperimentActivity.this, ResultsActivity.class);
                tintent.putExtra("experiment", exp);

                startActivity(tintent);
            }
        return super.onOptionsItemSelected(item);
    }

    @Override
    //Sends the experiment object and retrieves the updated object
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int LAUNCH_SECOND_ACTIVITY = 1;
        if (requestCode == LAUNCH_SECOND_ACTIVITY) {
            if(resultCode == Activity.RESULT_OK){
                exp = (Experiment) data.getSerializableExtra("experiment");

                SharedPreferences sharedPrefs = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
                sharedPrefs = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
                username = sharedPrefs.getString("Username", "");
                UUID = sharedPrefs.getString("ID", "");
                expManager = new ExpManager();
                expManager.updateTrialData(db,exp,username,UUID);
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                System.out.println("No Data");
            }
        }
    }
