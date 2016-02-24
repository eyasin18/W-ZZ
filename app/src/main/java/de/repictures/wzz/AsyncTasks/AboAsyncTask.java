package de.repictures.wzz.AsyncTasks;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.Set;

import de.repictures.wzz.MainJokes;
import de.repictures.wzz.ProfileActivity;
import de.repictures.wzz.R;

public class AboAsyncTask extends AsyncTask<String, Void, String[]> {
    private static final String TAG = "AboAsyncTask";
    private CoordinatorLayout cl;
    private final FloatingActionButton fab;
    private final Activity activity;
    Boolean votedUp;

    public AboAsyncTask(CoordinatorLayout cl, FloatingActionButton fab, Activity activity) {
        this.cl = cl;
        this.fab = fab;
        this.activity = activity;
    }

    @Override
    protected String[] doInBackground(String... params) {
        String resp = "";
        votedUp = isAbo(params[0]);
        try {
            URL url = new URL(MainJokes.HIGHSCORE_SERVER_BASE_URL + "?number=" + "15"
                    + "&key=" + liesKey(activity)
                    + "&profileKey=" + URLEncoder.encode(params[0], "UTF-8")
                    + "&votedUp=" + votedUp);
            URLConnection urlConnection = url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            try {
                BufferedReader r = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                StringBuilder total = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) {
                    total.append(line);
                }
                resp += total;
            } finally {
                resp = URLDecoder.decode(resp, "UTF-8");
                in.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String[] {resp, params[0]};
    }

    @Override
    protected void onPostExecute(String[] string) {
        Log.d(TAG, "onPostExecute: " + string[0]);
        if (string[0].length() == 7){
            if (votedUp) {
                fab.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_heart_white_18dp));
                Snackbar.make(cl, activity.getResources().getString(R.string.subscribed), Snackbar.LENGTH_LONG).show();
                addAbo(string[1], true);
            } else {
                fab.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_heart_outline_white_18dp));
                Snackbar.make(cl, activity.getResources().getString(R.string.unsubscribed), Snackbar.LENGTH_LONG).show();
                addAbo(string[1], false);
            }
        }
    }

    private void addAbo(String user, boolean abo) {
        SharedPreferences pref = activity.getSharedPreferences("Abos", 0);
        SharedPreferences.Editor editor = pref.edit();
        Set<String> set = pref.getStringSet("Abonniert", new HashSet<String>());
        if (abo) set.add(user);
        else set.remove(user);
        editor.putStringSet("Abonniert", set);
        editor.apply();
    }

    private String liesKey(Activity activity){
        SharedPreferences pref = activity.getSharedPreferences("Account", 0);
        return pref.getString("Key", "");
    }

    private Boolean isAbo(String user) {
        SharedPreferences pref = activity.getSharedPreferences("Abos", 0);
        return !pref.getStringSet("Abonniert", new HashSet<String>()).contains(user);
    }
}
