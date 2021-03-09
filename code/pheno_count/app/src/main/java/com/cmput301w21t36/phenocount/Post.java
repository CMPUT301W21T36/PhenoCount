package com.cmput301w21t36.phenocount;


import android.os.Build;
import androidx.annotation.RequiresApi;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

/**
 * @author: Charffy
 * This Post abstract class can be specialized as class Question, and class Reply;
 * it contains basic information for a post in the discussion forum.
 */
public abstract class Post {
    protected User author;
    protected String text;
    protected LocalDateTime date;

    public Post(User author, String text){
        this.author = author;
        this.text = text;
    }

    /**
     * It returns the current date when the post is created.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void getCurrentDate(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        date = LocalDateTime.now();
    }
}
