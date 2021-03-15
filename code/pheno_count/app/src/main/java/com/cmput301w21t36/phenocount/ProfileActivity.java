package com.cmput301w21t36.phenocount;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 *
 * @Author: Caleb Lonson
 */
public class ProfileActivity extends AppCompatActivity implements ProfileDialog.ProfileDialogListener {

    private Profile profile;
    TextView UIDTextView;
    TextView usernameTextView;
    TextView contactTextView;
    Button editButton;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Set all TextView variables
        UIDTextView = (TextView) findViewById(R.id.UIDTextView);
        usernameTextView = (TextView) findViewById(R.id.displayNameTextView);
        contactTextView = (TextView) findViewById(R.id.contactTextView);
        editButton = (Button) findViewById(R.id.editInfoButton);

        final String UUID = getIntent().getExtras().getString("UUID");
        DocumentReference docRef = db.collection("User").document(UUID);

        UIDTextView.setText(UUID);

        // Retrieve username and contact info from the database and set the respective textviews
        docRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String username = documentSnapshot.getString("Username");
                        String contact = documentSnapshot.getString("ContactInfo");

                        usernameTextView.setText(username);
                        contactTextView.setText(contact);
                    }
                });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

    }

    public void openDialog() {
        ProfileDialog profileDialog = new ProfileDialog();
        profileDialog.show(getSupportFragmentManager(), "Profile Dialog");

    }

    @Override
    public void applyChanges(String username, String contact) {
        usernameTextView.setText(username);
        contactTextView.setText(contact);

        SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        String UID = sharedPreferences.getString("ID","");

        Map<String, Object> map = new HashMap<>();
        map.put("ContactInfo", contact);
        map.put("UID", UID);
        map.put("Username", username);

        db.collection("User").document(UID).set(map);

    }
}