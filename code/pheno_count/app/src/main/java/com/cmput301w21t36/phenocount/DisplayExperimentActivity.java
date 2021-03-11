package com.cmput301w21t36.phenocount;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class DisplayExperimentActivity extends AppCompatActivity {

    private Experiment exp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experiment_display);
        exp = new Experiment("try", "just to see","Edmot", 10, false);
        //exp.setOwner("1");

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
    }
}