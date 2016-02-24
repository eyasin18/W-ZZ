package de.repictures.wzz.internet;


import android.support.v4.app.FragmentActivity;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.repictures.wzz.MainJokes;
import de.repictures.wzz.R;

public class getVotes implements Runnable{

    private static final String TAG = "getVotes";
    private String key, katego;
    public static List<String> votedKeys = new ArrayList<>();
    List<String> kategos;
    int index;

    public getVotes(String key, String katego, FragmentActivity activity) { // TODO: 16.09.2015 Reparieren!!
        this.key = key;
        kategos = Arrays.asList(activity.getResources().getStringArray(R.array.urls));
        index = kategos.indexOf(katego) + 1;
    }

    @Override
    public void run() {
        try {
            String resp = "";
            URL url = new URL(MainJokes.HIGHSCORE_SERVER_BASE_URL + "?number=9"
                    + "&voteChecker=" + "true"
                    + "&profileKey=" + key
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
                resp = resp.substring(1, resp.length()-1);
                votedKeys = Arrays.asList(resp.split(", "));
                Log.d(TAG, "run " + resp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
