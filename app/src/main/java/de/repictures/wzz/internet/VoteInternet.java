package de.repictures.wzz.internet;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.List;

import de.repictures.wzz.MainJokes;
import de.repictures.wzz.R;
import de.repictures.wzz.adapter.JokesViewHolder;

public class VoteInternet implements Runnable {

    private static final String TAG = "VoteInternet";
    private String key;
    private Boolean votedUp;
    private Boolean forJokes;
    private final CoordinatorLayout cl;
    private Activity activity;
    List<String> kategos;
    int index;

    public VoteInternet(String keyRaw, Boolean votedUp, Boolean forJokes, Activity activity, String katego,
                        CoordinatorLayout cl) {
        this.key = keyRaw;
        this.votedUp = votedUp;
        this.activity = activity;
        this.forJokes = forJokes;
        this.cl = cl;
        kategos = Arrays.asList(activity.getResources().getStringArray(R.array.urls));
        index = kategos.indexOf(katego) + 1;
    }

    @Override
    public void run() {
        int number;
        if (forJokes) number = 9;
        else number = 4;
        String resp = "";
        try {
            Log.d(TAG, key);
            URL url = new URL(MainJokes.HIGHSCORE_SERVER_BASE_URL + "?number=" + number
                    + "&votedUp=" + votedUp
                    + "&voteChecker=" + "false"
                    + "&key=" + key
                    + "&profileKey=" + liesKey()
                    + "&katego=" + index);
            URLConnection urlConnection = url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            try {
                BufferedReader r = new BufferedReader(new InputStreamReader(in, "iso-8859-1"));
                StringBuilder total = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) {
                    total.append(line);
                }
                resp += total;
            } finally {
                in.close();
                if (votedUp) {
                    try {
                        if (resp != "") {
                            Snackbar
                                    .make(cl, R.string.already_voted, Snackbar.LENGTH_LONG)
                                    .show();
                        } else {
                            Snackbar
                                    .make(cl, R.string.error, Snackbar.LENGTH_SHORT)
                                    .show();
                        }
                    } catch (NullPointerException ignore){}
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String liesKey(){
        SharedPreferences pref = activity.getSharedPreferences("Account", 0);
        return pref.getString("Key", "");
    }

}
