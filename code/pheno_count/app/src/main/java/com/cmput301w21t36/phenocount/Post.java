package com.cmput301w21t36.phenocount;


import android.os.Build;
import androidx.annotation.RequiresApi;

import java.io.Serializable;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

/**
 * @author: Charffy
 * This Post abstract class can be specialized as class Question, and class Reply;
 * it contains basic information for a post in the discussion forum.
 */
public abstract class Post implements Serializable {
    protected String ID = "";
    protected String author; //not necessary
    protected String text;
    protected LocalDateTime date; //not necessary


    public Post(String text){
        this.text = text;
    }
    public Post(){

    }

    /**
     * It returns the current date when the post is created.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void getCurrentDate(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        date = LocalDateTime.now();
    }
    public String getText() {
        return text;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
}
