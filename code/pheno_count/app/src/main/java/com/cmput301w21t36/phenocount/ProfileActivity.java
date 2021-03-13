package com.cmput301w21t36.phenocount;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity implements ProfileDialog.ProfileDialogListener {

    private Profile profile;
    TextView UID;
    TextView usernameTextView;
    TextView contactTextView;
    Button editButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        UID = (TextView) findViewById(R.id.UIDTextView);
        usernameTextView = (TextView) findViewById(R.id.displayNameTextView);
        contactTextView = (TextView) findViewById(R.id.contactTextView);
        editButton = (Button) findViewById(R.id.editInfoButton);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });


        String UUID = getIntent().getExtras().getString("UID");
        System.out.println(UUID);
    }

    public void openDialog() {
        ProfileDialog profileDialog = new ProfileDialog();

        profileDialog.show(getSupportFragmentManager(), "Profile Dialog");

    }

    @Override
    public void applyChanges(String username, String contact) {
        usernameTextView.setText(username);
        contactTextView.setText(contact);

    }
}