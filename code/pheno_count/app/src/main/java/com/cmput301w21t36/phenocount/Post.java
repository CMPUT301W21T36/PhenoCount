package com.cmput301w21t36.phenocount;


import android.os.Build;
import androidx.annotation.RequiresApi;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

public abstract class Post {
    private User author;
    private String text;
    private LocalDateTime date;

    public Post(User author, String text){
        this.author = author;
        this.text = text;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void getCurrentDate(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        date = LocalDateTime.now();
    }
}
