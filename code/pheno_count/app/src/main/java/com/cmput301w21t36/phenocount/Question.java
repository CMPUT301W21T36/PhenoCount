package com.cmput301w21t36.phenocount;

import java.util.ArrayList;

public class Question extends Post{
    private ArrayList<Reply> replies;

    public Question(User author, String text){
        super(author, text);
    }

    public void addReply(){
        Reply reply = new Reply();


    }

}
