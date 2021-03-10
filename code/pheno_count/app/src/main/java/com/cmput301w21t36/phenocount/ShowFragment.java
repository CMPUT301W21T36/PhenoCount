package com.cmput301w21t36.phenocount;


/*
Citation:
Ruiqin Pi, "Lab 3", 2021-02-04, Public Domain,
URL: https://eclass.srv.ualberta.ca/pluginfile.php/6713986/mod_resource/content/0/Lab%203%20Instructions%20-%20Fragments.pdf
*/

/**
 * @author: Charffy
 * This is a general fragment class that shows the fragment when you want to
 * add a question, reply, or something else.
 */

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;


public class ShowFragment extends DialogFragment {
    private String type; //to add a question or a reply
    private EditText body;
    private String title;
    private OnFragmentInteractionListener listener;

    public interface OnFragmentInteractionListener {
        void onOkPressedAdd(String text);
    }

    //constructor of fragment, you have to know the type,
    // thus we can set up the title and hint for it.
    public ShowFragment(String type) {
        this.type = type;

        if(type == "question"){
            Question newQue = new Question(); //may have to pass user and experiment to it
            title = "Ask a Question";
        }else if (type == "reply"){
            Reply newRep = new Reply(); //may have to pass user and experiment to it
            title = "Give a Reply";
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener){
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        //may change the name of this layout later
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.add_question_fragment, null);
        body = view.findViewById(R.id.body_edit_text);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle(title)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String bodyText = body.getText().toString();
                        listener.onOkPressedAdd(bodyText);
                    }}).create();
    }
}

