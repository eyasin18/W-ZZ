package de.repictures.wzz.internet;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import de.repictures.wzz.MainJokes;

public class postComment implements Runnable {

    String inhalt, key, profileKey;

    public postComment(String comment, String key, String profileKey) {
        this.inhalt = comment;
        this.key = key;
        this.profileKey = profileKey;
    }

    @Override
    public void run() {
        inhalt = inhalt.replace(" ", "+");
        try {
            URL url = new URL(MainJokes.HIGHSCORE_SERVER_BASE_URL + "?number=" + "4"
                    + "&inhalt=" + inhalt
                    + "&key=" + key
                    + "&profileKey=" + profileKey);
            URLConnection urlConnection = url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
