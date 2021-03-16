// Anisha Sethumadhavan,2021-02-12, CMPUT301 Assignmnet1 (sethumad-TrialBook)
// refered MysticMagicϡ,2014-09-29,CC BY-SA 3.0,https://stackoverflow.com/a/26097588

package com.cmput301w21t36.phenocount;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Message;

public class AlertMsg {
    AlertDialog alertDialog ;
    private final Message NO_HANDLER = null;
    public AlertMsg(Activity parent, String aTitle, String mes)
    {
        alertDialog = new AlertDialog.Builder(parent).create();
        alertDialog.setTitle(aTitle);
        alertDialog.setMessage(mes) ;
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog.show();
    }
}

