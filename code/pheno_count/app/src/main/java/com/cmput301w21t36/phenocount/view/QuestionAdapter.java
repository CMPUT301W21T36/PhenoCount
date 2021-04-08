package com.cmput301w21t36.phenocount;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

/*
 * Role: view
 * This QuestionAdapter class helps to display contents of a question
 * in the listview in DiscussionActivity.
 */

public class QuestionAdapter extends ArrayAdapter<Question> {
    private ArrayList<Question> questions;
    private Context context;


    public QuestionAdapter(Context context, ArrayList<Question> questions) {
        super(context,0,questions);
        this.questions= questions;
        this.context = context;
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.content_question,parent,false);
        }

        Question question = questions.get(position);

        TextView queText = view.findViewById(R.id.question_text_view_in_list);
        String textMessage = "Q: " + question.getText();
        queText.setText(textMessage);

        TextView numText = view.findViewById(R.id.reply_num);
        String numMessage = "Reply: " + question.getReply_num();
        numText.setText(numMessage);

        if (position % 2 == 1) {
            queText.setTextColor(Color.parseColor("#2196F3"));
            numText.setTextColor(Color.parseColor("#2196F3"));
        } else {
            queText.setTextColor(Color.parseColor("#E556D2"));
            numText.setTextColor(Color.parseColor("#E556D2"));
        }


        return view;
    }
}

