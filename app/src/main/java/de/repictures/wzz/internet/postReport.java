package de.repictures.wzz.internet;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import de.repictures.wzz.MainJokes;
import de.repictures.wzz.R;
import de.repictures.wzz.ReportActivity;

public class postReport implements Runnable {

    String art, key;
    Activity activity;

    public postReport(String art, String key, Activity activity) {
        this.art = art;
        this.key = key;
        this.activity = activity;
    }

    @Override
    public void run() {
        String resp = "";
        try {
            URL url = new URL(MainJokes.HIGHSCORE_SERVER_BASE_URL + "?number=" + "8"
                              + "&key=" + key
                              + "&inhalt=" + URLEncoder.encode(art, "UTF-8")
                              + "&profileKey=" + liesKey());
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
                if (resp != "success") {
                    Snackbar
                            .make(MainJokes.coordinatorLayoutView, R.string.reported, Snackbar.LENGTH_LONG)
                            .show();
                } else {
                    Snackbar
                            .make(MainJokes.coordinatorLayoutView, R.string.error, Snackbar.LENGTH_SHORT)
                            .show();
                }
                activity.finish();
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
