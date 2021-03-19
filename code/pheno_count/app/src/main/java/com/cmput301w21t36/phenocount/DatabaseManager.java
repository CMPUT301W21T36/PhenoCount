package com.cmput301w21t36.phenocount;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

/*
 * Role: controller
 * Goal: grant permission of using the database for other manager classes
 */

public class DatabaseManager {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference quecollectionReference;
    private CollectionReference repcollectionReference;



    public FirebaseFirestore getDb() {
        return db;
    }
    public CollectionReference getQuecollectionReference() {
        return quecollectionReference;
    }

    public void setQuecollectionReference(CollectionReference quecollectionReference) {
        this.quecollectionReference = quecollectionReference;
    }

    public CollectionReference getRepcollectionReference() {
        return repcollectionReference;
    }

    public void setRepcollectionReference(CollectionReference repcollectionReference) {
        this.repcollectionReference = repcollectionReference;
    }
}
