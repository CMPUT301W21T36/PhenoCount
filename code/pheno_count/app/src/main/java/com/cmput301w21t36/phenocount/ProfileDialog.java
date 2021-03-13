package com.cmput301w21t36.phenocount;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

public class ProfileDialog extends AppCompatDialogFragment {

    private EditText editTextUsername;
    private EditText editTextContact;

    private ProfileDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.profile_dialog, null);

        builder.setView(view)
                .setTitle("Edit information")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String username = editTextUsername.getText().toString();
                        String contact = editTextContact.getText().toString();
                        listener.applyChanges(username, contact);

                    }
                });

        editTextUsername = view.findViewById(R.id.editUsername);
        editTextContact = view.findViewById(R.id.editContact);

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (ProfileDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "Must implement ProfileDialogListener");
        }
    }

    public interface ProfileDialogListener {
        void applyChanges(String username, String contact);
    }
}
