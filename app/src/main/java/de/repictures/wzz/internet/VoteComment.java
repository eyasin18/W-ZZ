package de.repictures.wzz.internet;

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

import de.repictures.wzz.MainJokes;
import de.repictures.wzz.R;

public class VoteComment {
    public VoteComment(final String key, final String userKey, final boolean votedUp, final CoordinatorLayout cl) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String resp = "";
                try {
                    URL url = new URL(MainJokes.HIGHSCORE_SERVER_BASE_URL + "?number=" + 14
                            + "&votedUp=" + votedUp
                            + "&key=" + key
                            + "&profileKey=" + userKey);
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
                            if (resp != "") {
                                Snackbar
                                        .make(cl, R.string.already_voted, Snackbar.LENGTH_LONG)
                                        .show();
                            } else {
                                Snackbar
                                        .make(cl, R.string.error, Snackbar.LENGTH_SHORT)
                                        .show();
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
