package de.repictures.wzz.internet;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;

import de.repictures.wzz.MainJokes;

public class witzNachUser implements Runnable {

    private static final String TAG = "witzNachUser";
    public static String witz;
    private String POST_GET_ID;

    public witzNachUser(String postGetId) {
        this.POST_GET_ID = postGetId;
    }

    @Override
    public void run(){
        internetWitze();
    }

    private void internetWitze(){
        try {
            witz = "";
            POST_GET_ID = POST_GET_ID.replace(" ", "+");
            URL url = new URL(MainJokes.HIGHSCORE_SERVER_BASE_URL + "?number=" + "1"
                    + "&user=" + POST_GET_ID);
            Log.d(TAG, "internetWitze " + url.toString());
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
                Log.v("UserInput", total.toString());
            } finally {
                in.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
