package de.repictures.wzz.internet;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;

import de.repictures.wzz.LoginActivity;
import de.repictures.wzz.MainJokes;
import de.repictures.wzz.MainKatego;
import de.repictures.wzz.R;

public class postProfile implements Runnable{

    private String email;
    private String personName;
    private String personPhotoUrl;
    private String coverUrl;
    private String deviseStr;
    private Context loginActivity;

    public postProfile(String personName, String email, String personPhotoUrl, String coverUrl, String deviseStr, LoginActivity loginActivity) {
        this.personName = personName;
        this.email = email;
        this.personPhotoUrl = personPhotoUrl;
        this.coverUrl = coverUrl;
        this.loginActivity = loginActivity;
        this.deviseStr = deviseStr;
    }

    @Override
    public void run() {
        try {
            String resp = "";
            email = email.trim();
            personName = personName.replace(" ", "+");
            personName = personName.trim();
            deviseStr = deviseStr.replace(" ", "+");
            deviseStr = deviseStr.trim();
            URL url = new URL(MainJokes.HIGHSCORE_SERVER_BASE_URL + "?number=" + "7"
                    + "&user=" + personName
                    + "&email=" + email
                    + "&photoUrl=" + personPhotoUrl
                    + "&coverUrl=" + coverUrl
                    + "&devise=" + deviseStr);
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
                SharedPreferences pref = loginActivity.getSharedPreferences("Account", 0);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("Key", resp);
                editor.apply();
            } finally {
                in.close();
            }
        } catch (IOException e) {
            Toast.makeText(loginActivity, "Fehler", Toast.LENGTH_SHORT).show();
        }
    }
}
