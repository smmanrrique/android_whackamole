package com.example.whackamole.game;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.DialogFragment;

import com.example.whackamole.R;

public class MessageGameActivity extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("R.string.dialog_fire_missiles")
                .setPositiveButton("R.string.fire", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                    }
                })
                .setNegativeButton("R.string.cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}

//    public void showAlertDialogButtonClicked(View view) {
//
//        // setup the alert builder
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("AlertDialog");
//        builder.setMessage("Would you like to continue learning how to use Android alerts?");
//
//        // add the buttons
//        builder.setPositiveButton("Continue", null);
//        builder.setNegativeButton("Cancel", null);
//
//        // create and show the alert dialog
//        AlertDialog dialog = builder.create();
//        dialog.show();
//    }