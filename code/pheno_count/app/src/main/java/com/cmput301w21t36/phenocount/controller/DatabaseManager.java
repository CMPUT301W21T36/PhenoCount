package com.cmput301w21t36.phenocount;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

/*
 * Role: controller
 * Goal: grant permission of using the database for other manager classes
 */

public class DatabaseManager {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference queCollectionReference;
    private CollectionReference repCollectionReference;



    public FirebaseFirestore getDb() {
        return db;
    }
    public CollectionReference getQueCollectionReference() {
        return queCollectionReference;
    }

    public void setQueCollectionReference(CollectionReference queCollectionReference) {
        this.queCollectionReference = queCollectionReference;
    }

    public CollectionReference getRepCollectionReference() {
        return repCollectionReference;
    }

    public void setRepCollectionReference(CollectionReference repCollectionReference) {
        this.repCollectionReference = repCollectionReference;
    }
}
