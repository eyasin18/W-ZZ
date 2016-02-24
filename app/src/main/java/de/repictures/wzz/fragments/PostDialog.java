package de.repictures.wzz.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import de.repictures.wzz.MainJokes;
import de.repictures.wzz.R;
import de.repictures.wzz.internet.postWitze;

public class PostDialog extends DialogFragment {

    EditText writeWizzle;
    Activity context;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        context = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.JokesDialogStyle));
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View rootView = inflater.inflate(R.layout.main_jokes_old, null);
        writeWizzle = (EditText) rootView.findViewById(R.id.writeWizzleOld);
        builder.setTitle(R.string.editWizzle)
                .setView(rootView)
                .setPositiveButton(R.string.add_joke, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (writeWizzle.getText().toString() != ""){
                            new Thread(new postWitze(MainJokes.katego,
                                    writeWizzle.getText().toString(), liesKey(), getActivity(), context, false)).start();
                            Log.v("postInfos", MainJokes.katego + "~" + liesUserName() + "~"
                                    + writeWizzle.getText().toString());
                    }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    private String liesUserName(){
        SharedPreferences pref = getActivity().getSharedPreferences("UserInformations", 0);
        return pref.getString("UserName", "");
    }
    private String liesUserEmail(){
        SharedPreferences pref = getActivity().getSharedPreferences("UserInformations", 0);
        return pref.getString("UserEmail", "");
    }

    private String liesKey(){
        SharedPreferences pref = getActivity().getSharedPreferences("Account", 0);
        return pref.getString("Key", "");
    }

}
