package com.cmput301w21t36.phenocount;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DisplayExperimentActivity extends AppCompatActivity {

    private Experiment exp; // catch object passed from mainlist

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experiment_display);
        exp = (Experiment) getIntent().getSerializableExtra("experiment");//defining the Experiment object
        //exp.setOwner("1");
        System.out.println("Heloooooooooooooooooooooooooo");
        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();

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
        //expOwner.setText(exp.getOwner().toString());
        expRegion.setText(exp.getRegion());
        //expMinTrial.setText((String) exp.getMinimumTrials());
        expType.setText(exp.getExpType());
        String mStat = "" ;
        switch(exp.getExpStatus()){
            case 1:
                mStat = "Published";
            case 2:
                mStat= "Ended";
            case 3:
                mStat = "Unpublished";
            default:
                mStat= "Added but not yet published";

        }
        expStatus.setText(mStat);
        //expType.setText(exp.getTrials().get(1).getType());
        if(exp.isRequireLocation()== true) { expReqLoc.setText("YES"); }
        else {expReqLoc.setText("NO");}

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

        System.out.println(exp.getTrials());
        switch (item.getItemId()){
            case R.id.item1:
                if (exp.getExpType().equals("Binomial")) {
                    Intent bintent = new Intent(DisplayExperimentActivity.this, Binomial.class);
                    bintent.putExtra("experiment", exp);
                    int LAUNCH_SECOND_ACTIVITY = 1;
                    startActivityForResult(bintent, LAUNCH_SECOND_ACTIVITY);
                }
                if (exp.getExpType().equals("Count")) {
                    Intent cintent = new Intent(DisplayExperimentActivity.this, Count.class);
                    cintent.putExtra("experiment", exp);
                    int LAUNCH_SECOND_ACTIVITY = 1;
                    startActivityForResult(cintent, LAUNCH_SECOND_ACTIVITY);
                }
                if (exp.getExpType().equals("Measurement")) {
                    Intent mintent = new Intent(DisplayExperimentActivity.this, Measurement.class);
                    mintent.putExtra("experiment", exp);
                    int LAUNCH_SECOND_ACTIVITY = 1;
                    startActivityForResult(mintent, LAUNCH_SECOND_ACTIVITY);
                }
                if (exp.getExpType().equals("Non Negative Count")) {
                    Intent nintent = new Intent(DisplayExperimentActivity.this, NonNegativeCount.class);
                    nintent.putExtra("experiment", exp);
                    int LAUNCH_SECOND_ACTIVITY = 1;
                    startActivityForResult(nintent, LAUNCH_SECOND_ACTIVITY);
                }
            case R.id.item3:
                Intent dintent = new Intent(DisplayExperimentActivity.this, DiscussionActivity.class);
                dintent.putExtra("experiment", exp);
                int LAUNCH_SECOND_ACTIVITY = 1;
                startActivityForResult(dintent, LAUNCH_SECOND_ACTIVITY);


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
                Experiment newexp = (Experiment) data.getSerializableExtra("experiment");
                if (newexp != null) {
                    exp = newexp; //updating the current exp object(to show updated exp desc)
                    System.out.println("SIZE:"+exp.getTrials().size());
                } else {
                    String testt = data.getSerializableExtra("scannedText").toString();
                    TextView test = findViewById(R.id.scannedTextView);
                    test.setText(testt);
                }

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                System.out.println("No Data");
            }
        }
    }
}