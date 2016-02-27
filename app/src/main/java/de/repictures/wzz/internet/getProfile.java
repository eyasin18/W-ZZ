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

import de.repictures.wzz.DetailActivity;
import de.repictures.wzz.MainJokes;

public class getProfile{

    private static final String TAG = "getProfile";
    public static String infos[];
    private String key;

    public getProfile(String key, String PROFILE_PIC_SIZE) {
        try {
            String profile = "";
            URL url = new URL(MainJokes.HIGHSCORE_SERVER_BASE_URL + "?number=" + "6"
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
                profile += total;
            } finally {
                in.close();
                profile = URLDecoder.decode(profile, "UTF-8");
                infos = profile.split("</we>");
                Log.d(TAG, "run " + profile);
                DetailActivity.infosList.add(infos);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
