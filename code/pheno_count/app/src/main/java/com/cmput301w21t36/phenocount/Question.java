package com.cmput301w21t36.phenocount;

import java.io.Serializable;
/**
 * Role: model
 * This Question class extends an abstract class Post;
 * it contains basic information of a question.
 */
public class Question extends Post implements Serializable {
    public Question(String text){
        super(text);
    }
    public Question(){
    }
}
