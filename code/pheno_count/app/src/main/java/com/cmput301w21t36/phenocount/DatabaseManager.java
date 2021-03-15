package com.cmput301w21t36.phenocount;

import com.google.firebase.firestore.FirebaseFirestore;

public class DatabaseManager {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();





    public FirebaseFirestore getDb() {
        return db;
    }
}
