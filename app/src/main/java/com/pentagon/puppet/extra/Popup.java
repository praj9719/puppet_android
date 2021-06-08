package com.pentagon.puppet.extra;

import android.app.Activity;
import android.app.AlertDialog;

public class Popup {

    private Activity activity;
    private String title;
    private String message;
    private AlertDialog alertDialog;

    public Popup(Activity activity, String message) {
        this.activity = activity;
        this.title = "";
        this.message = message;
    }

    public Popup(Activity activity, String title, String message) {
        this.activity = activity;
        this.title = title;
        this.message = message;
    }


    public void show(){
        activity.runOnUiThread(() -> {
            alertDialog = new AlertDialog.Builder(activity)
                    .setTitle(title)
                    .setMessage(message)
                    .setPositiveButton("Ok", null)
                    .create();
            alertDialog.show();
        });
    }

    public void onClick(String btn, PopupClickListener listener){
        activity.runOnUiThread(() -> {
            alertDialog = new AlertDialog.Builder(activity)
                    .setTitle(title)
                    .setMessage(message)
                    .setPositiveButton(btn, (dialogInterface, i) -> listener.onClick())
                    .create();
            alertDialog.show();
        });
    }


}
