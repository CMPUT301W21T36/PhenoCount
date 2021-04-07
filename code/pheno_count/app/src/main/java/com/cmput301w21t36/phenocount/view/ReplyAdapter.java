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
 * This ReplyAdapter class helps to display contents of a reply
 * in the listview in QuestionActivity.
 */
public class ReplyAdapter extends ArrayAdapter<Reply> {
    private ArrayList<Reply> replies;
    private Context context;


    public ReplyAdapter(Context context, ArrayList<Reply> replies) {
        super(context,0,replies);
        this.replies= replies;
        this.context = context;
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.content_reply,parent,false);
        }

        Reply reply = replies.get(position);

        TextView repText = view.findViewById(R.id.reply_text_view_in_list);

        repText.setText(reply.getText());

        if (position % 2 == 1) {
            repText.setTextColor(Color.parseColor("#EFCCEB"));
        } else {
            repText.setTextColor(Color.parseColor("#9AE8E1"));
        }


        return view;
    }
}
