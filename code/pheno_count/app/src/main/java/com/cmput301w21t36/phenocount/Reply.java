package com.cmput301w21t36.phenocount;

public class Reply extends Post{

    //since it is so small, can we just make it a string attribute in
    //a question object?
    public Reply(User author, String text) {
        super(author, text);
    }

}
