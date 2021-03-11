package com.cmput301w21t36.phenocount;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

/**
 * @author: Charffy
 * This QuestionActivity class display basic information about a question.
 * When one question on the list view
 * in the discussion forum is clicked, you are transferred to the question's own
 * activity page, where you can browse all its replied and give replies.
 */
public class QuestionActivity extends AppCompatActivity{
    private ListView rList;
    private ArrayList<Reply> replies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Intent i = new Intent(this, LocationActivity.class);
        //a i.putExtra("info", info);
        //startActivityForResult(i, 1);

    }

}
