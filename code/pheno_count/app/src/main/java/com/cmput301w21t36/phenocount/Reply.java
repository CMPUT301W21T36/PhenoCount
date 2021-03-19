package com.cmput301w21t36.phenocount;

/**
 * @author: Charffy
 * This Peply class contains basic information of a reply
 */
public class Reply extends Post{

    //since it is so small, can we just make it a string attribute in
    //a question object? Then Reply doesn't have to remember its parent question.

    User author;

    public Reply(String text){
        super(text);
    }
    public Reply(){


    }



}
