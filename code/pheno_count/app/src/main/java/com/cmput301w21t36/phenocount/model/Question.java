package com.cmput301w21t36.phenocount;

import java.io.Serializable;
/**
 * Role: model
 * This Question class extends an abstract class Post;
 * it contains basic information of a question.
 */
public class Question extends Post implements Serializable {
    private long reply_num = 0;
    public Question(String text){
        super(text);
    }
    public Question(){
    }

    public long getReply_num() {
        return reply_num;
    }

    public void setReply_num(long reply_num) {
        this.reply_num = reply_num;
    }
}
