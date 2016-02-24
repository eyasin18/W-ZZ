package de.repictures.wzz.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

import de.repictures.wzz.MainJokes;
import de.repictures.wzz.R;
import de.repictures.wzz.ReportActivity;
import de.repictures.wzz.adapter.MenuDialogAdapter;
import de.repictures.wzz.uiHelper.JokesClickListener;

public class MenuDialog extends DialogFragment implements View.OnClickListener {

    TextView report, share, favor, like;
    List<String> menuList;
    String[] extras;
    String extras_raw;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.JokesDialogStyle));
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View rootView = inflater.inflate(R.layout.jokes_dialog, null);
        builder.setView(rootView);
        extras_raw = getArguments().getString("detailExtra");
        if (extras_raw != null)extras = extras_raw.split("~");
        else {
            extras = new String[7];
            for (int i = 0; i < 8; i += 1){
                extras[i] = getActivity().getResources().getString(R.string.error);
            }
        }
        menuList = Arrays.asList(getActivity().getResources().getStringArray(R.array.menu_dialog));
        report = (TextView) rootView.findViewById(R.id.textView5);
        report.setOnClickListener(this);
        report.setText(menuList.get(0));
        share = (TextView) rootView.findViewById(R.id.textView6);
        share.setOnClickListener(this);
        share.setText(menuList.get(1));
        favor = (TextView) rootView.findViewById(R.id.textView7);
        favor.setOnClickListener(this);
        favor.setText(menuList.get(2));
        like = (TextView) rootView.findViewById(R.id.textView8);
        like.setOnClickListener(this);
        like.setText(menuList.get(3));
        return builder.create();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.textView5:
                dismiss();
                Intent i = new Intent(getActivity(), ReportActivity.class);
                i.putExtra("detailExtras", extras);
                startActivity(i);
                Log.d("MenuDialog", "report clicked");
                break;
            case R.id.textView6:
                Log.d("MenuDialog", "share clicked");
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String key = extras[0].replace("ahBzfmRlcndpdHplc2VydmVyc", "");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "derwitzeserver.appspot.com/wizz?id=" + key);
                startActivity(Intent.createChooser(sharingIntent, getActivity().getResources().getString(R.string.share_via)));
                dismiss();
                break;
            case R.id.textView7:
                Log.d("MenuDialog", "favor clicked");
                JokesClickListener.favorite.performClick();
                dismiss();
                break;
            case R.id.textView8:
                Log.d("MenuDialog", "like clicked");
                JokesClickListener.thumbUp.performClick();
                dismiss();
                break;
        }
    }
}
