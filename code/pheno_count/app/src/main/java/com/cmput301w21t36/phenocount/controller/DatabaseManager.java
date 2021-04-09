package com.cmput301w21t36.phenocount;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Role: controller
 * Goal: grant permission of using the database for other manager classes
 */

public class DatabaseManager {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference queCollectionReference;
    private CollectionReference repCollectionReference;


    /**
     *get the database from firestore
     * @return
     */
    public FirebaseFirestore getDb() {
        return db;
    }

    /**
     * get the question collection from the database
     * @return
     */
    public CollectionReference getQueCollectionReference() {
        return queCollectionReference;
    }

    /**
     * set up the collection reference for Question collection
     * @param queCollectionReference
     */
    public void setQueCollectionReference(CollectionReference queCollectionReference) {
        this.queCollectionReference = queCollectionReference;
    }

    /**
     * get the reply collection from the database
     * @return
     */
    public CollectionReference getRepCollectionReference() {
        return repCollectionReference;
    }

    /**
     * set up the collection reference for Reply collection
     * @param repCollectionReference
     */
    public void setRepCollectionReference(CollectionReference repCollectionReference) {
        this.repCollectionReference = repCollectionReference;
    }
}
