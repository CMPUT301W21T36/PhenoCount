package com.cmput301w21t36.phenocount;

import android.widget.ListView;

import java.util.ArrayList;
/**
 * @author: Charffy
 * This Question class extends the Post class;
 * it contains basic information about a question.
 */
public class Question extends Post{
    public Question(User author, String text){
        super(author, text);
    }

    //do I have to create an attribute author, text?
    //if not, how to set up getters?

    private ListView rList;
    private ArrayList<Reply> replies;

    /**
     * This method allows user to reply a question of the experiment,
     * the newly added reply will be stored in the question's page.
     * how: click '+' button in question's UI, a fragment shows, has two text boxes,
     * the top one is a title, says 'New Reply',
     * the bottom one is the an edit text, let the user type in the reply body.
     */
    public void addReply(){
        String replyText = "";
        Reply reply = new Reply(author, replyText);
        replies.add(reply);
    }


}
