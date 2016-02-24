package de.repictures.wzz.internet;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;

import de.repictures.wzz.DetailActivity;
import de.repictures.wzz.MainJokes;

public class wizzByKey implements Runnable {
    private final String key;
    private final Activity activity;

    public wizzByKey(String key, Activity Activity) {

        this.key = key;
        activity = Activity;
    }

    @Override
    public void run() {
        try {
            String witz = "";
            URL url = new URL(MainJokes.HIGHSCORE_SERVER_BASE_URL + "?number=" + "16"
                    + "&key=" + key);
            URLConnection urlConnection = url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            try {
                BufferedReader r = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                StringBuilder total = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) {
                    total.append(line);
                }
                witz += total;
                witz = URLDecoder.decode(witz, "UTF-8");
                witz = witz.replace("<br />", "\n");
            } finally {
                in.close();
                Log.v("wizzInput", witz);
                String detailExtra = key + "~" + witz;
                Intent i = new Intent(activity, DetailActivity.class);
                i.putExtra("DetailExtra", detailExtra);
                i.putExtra("VoterAvailable", true);
                activity.startActivity(i);
                activity.finish();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
