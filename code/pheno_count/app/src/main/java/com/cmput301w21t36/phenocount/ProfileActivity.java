package com.cmput301w21t36.phenocount;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {

    private Profile profile;
    TextView usernameTextView;
    TextView contactTextView;
    Button editButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        String extras = getIntent().getExtras().getString("UID");

        System.out.println("Profile" + extras);
    }
}