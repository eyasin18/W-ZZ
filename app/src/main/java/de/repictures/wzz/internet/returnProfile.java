package de.repictures.wzz.internet;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import de.repictures.wzz.DetailActivity;
import de.repictures.wzz.MainJokes;

public class returnProfile implements Runnable{

    private static final String TAG = "returnProfile";
    public static String[] infos;
    private String key;

    public returnProfile(String key){
        this.key = key;
    }

    @Override
    public void run() {
        try {
            String profile = "";
            URL url = new URL(MainJokes.HIGHSCORE_SERVER_BASE_URL + "?number=" + "6"
                    + "&key=" + key);
            URLConnection urlConnection = url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            try {
                BufferedReader r = new BufferedReader(new InputStreamReader(in, "iso-8859-1"));
                StringBuilder total = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) {
                    total.append(line);
                }
                profile += total;
            } finally {
                in.close();
                infos = profile.split("~");
                Log.d(TAG, "run " + profile);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
