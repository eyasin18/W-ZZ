package de.repictures.wzz.internet;

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

public class getComments implements Runnable{

    private static final String TAG = "getComments";
    String key, resp;
    public static List<String> comments = new ArrayList<>(), userUrl = new ArrayList<>(), votes = new ArrayList<>(),
            splited;

    public getComments(String key) {
        this.key = key;
    }

    @Override
    public void run() {
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
        try {
            resp = "";

            URL url = new URL(MainJokes.HIGHSCORE_SERVER_BASE_URL + "?number=" + "3"
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
                resp += total;
            } finally {
                in.close();
                Log.d(TAG, "response: " + resp);
                if (resp != "") {
                    splited = Arrays.asList(resp.split("~"));
                    int size = splited.size();
                    for (int i = 0; i < size; i += 3) {
                        comments.add(splited.get(i));
                    }
                    for (int i = 1; i < size; i += 3) {
                        userUrl.add(splited.get(i));
                    }
                    for (int i = 2; i < size; i += 3) {
                        votes.add(splited.get(i));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
