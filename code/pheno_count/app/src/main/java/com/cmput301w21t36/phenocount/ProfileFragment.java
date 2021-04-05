package com.cmput301w21t36.phenocount;

/**
 * This is a general fragment class that shows the fragment when you want to
 * add a question, reply, or something else.
 */

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;


public class ProfileFragment extends DialogFragment {
    private TextView nameView;
    private TextView phoneView;
    private String username;
    private String phone;
    private OnFragmentInteractionListener listener;

    public interface OnFragmentInteractionListener {
        void onOkPressedAdd(String text);
    }

    //constructor of fragment, you have to know the type,
    // thus we can set up the title and hint for it.
    public ProfileFragment(String username, String phone) {
        this.username = username;
        if(phone == null){
            this.phone = "unknown";
        }else{
            this.phone = phone;
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
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_profile, null);
        nameView = view.findViewById(R.id.name_view);
        phoneView = view.findViewById(R.id.phone_view);
        nameView.setText("Username: "+ username);
        phoneView.setText("Contact information: " + phone);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("User Profile")
                .setNegativeButton("OK", null)
                .create();
    }
}

