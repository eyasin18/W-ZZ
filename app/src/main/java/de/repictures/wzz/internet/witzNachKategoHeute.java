package de.repictures.wzz.internet;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import de.repictures.wzz.MainJokes;

public class witzNachKategoHeute implements Runnable{

    private static final String TAG = "witzNachKategoHeute";
    public static String witz;
    private String POST_GET_ID;
    private Boolean sortDirection;
    private int itemCount;
    Activity activity;

    public witzNachKategoHeute(String postGetId, Boolean sort, int totalItemCount, Activity activity) {
        this.POST_GET_ID = postGetId;
        this.sortDirection = sort;
        this.itemCount = totalItemCount;
        this.activity = activity;
    }

    @Override
    public void run(){
        internetWitze();
    }

    private void internetWitze(){
        try {
            Log.d(TAG, "internetWitze() called with: " + "Success");
            witz = "";
            URL url = new URL(MainJokes.HIGHSCORE_SERVER_BASE_URL + "?number=0"
                    + "&katego=" + POST_GET_ID
                    + "&count=" + itemCount
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
                witz += total;
                witz = witz.replace("<br />", "\n");
                Log.v("HeuteInput", total.toString());
            } finally {
                in.close();
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