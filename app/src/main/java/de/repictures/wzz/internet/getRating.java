package de.repictures.wzz.internet;

import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import de.repictures.wzz.MainJokes;

public class getRating {

    public static String[] rating;

    public getRating(TextView voteCounter, String key) {
        Thread t = new Thread(new getRatingRunnable(key));
        t.start();
        try {
            t.join();
            voteCounter.setText(rating[0]);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private class getRatingRunnable implements Runnable {

        String key;

        public getRatingRunnable(String key) {
            this.key = key;
        }

        @Override
        public void run() {
            try {
                String resp = "";
                String[] respArray;
                URL url = new URL(MainJokes.HIGHSCORE_SERVER_BASE_URL + "?number=10"
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
                    respArray = resp.split("~");
                    getRating.rating = respArray;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
