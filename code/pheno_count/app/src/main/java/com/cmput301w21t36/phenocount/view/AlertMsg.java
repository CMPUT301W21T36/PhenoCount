// Anisha Sethumadhavan,2021-02-12, CMPUT301 Assignmnet1 (sethumad-TrialBook)
// refered MysticMagicϡ,2014-09-29,CC BY-SA 3.0,https://stackoverflow.com/a/26097588
package com.cmput301w21t36.phenocount;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Message;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;

/**
 * Represents the alert message boxes and confirmation boxes
 */
public class AlertMsg {
    public AlertDialog alertDialog ;
    private final Message NO_HANDLER = null;

    /**
     * Constructor for the alert dialog
     * @param parent
     * @param aTitle
     * @param msg
     * @param where
     */
    public AlertMsg(Activity parent, String aTitle, String msg, int where) {
        alertDialog = new AlertDialog.Builder(parent).create();
        alertDialog.setTitle(aTitle);
        alertDialog.setMessage(msg) ;
        if (where == 0) {
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
        }else if (where == 1){
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Proceed",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
        }
        alertDialog.show();
    }

    /**
     * To set the spannable text for each alert dialog
     * @param myText
     * @param start
     * @param end
     */
    public void setColour(String myText, int start, int end){
        SpannableString msg = new SpannableString(myText);
        msg.setSpan(new ForegroundColorSpan(Color.parseColor("#FF6347")), start,
                end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        alertDialog.setMessage(msg);
    }

    /**
     * To change the color of the buttons
     */
    public void setButtonCol(){
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#67A3D9"));
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#67A3D9"));
        alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(Color.parseColor("#67A3D9"));
    }

    /**
     * To cancel the dialog box
     */
    public void cancelDialog(){
        alertDialog.cancel();
    }
}

