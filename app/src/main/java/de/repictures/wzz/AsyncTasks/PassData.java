package de.repictures.wzz.AsyncTasks;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;

import de.repictures.wzz.MainJokes;
import de.repictures.wzz.MainKatego;
import de.repictures.wzz.SplashActivity;
import de.repictures.wzz.StartActivity;
import de.repictures.wzz.uiHelper.InfoGetter;
import de.repictures.wzz.uiHelper.getPictures;

public class PassData extends AsyncTask<Void, Void, Boolean> {

    private static final String TAG = "PassData";
    private String email;
    private String name;
    private final String personPic;
    private final String coverUrl;
    private Activity activity;
    private String resp = "";
    private int platform = 0;
    private Boolean firstConnect;
    private String devise;
    URLConnection urlConnection;

    public PassData(String email, String name, int platform, String personPic, String coverUrl, Activity activity,
                    boolean firstConnect, String devise) {
        this.email = email;
        this.name = name;
        this.personPic = personPic;
        this.coverUrl = coverUrl;
        this.activity = activity;
        this.platform = platform;
        this.firstConnect = firstConnect;
        this.devise = getDevise(devise);
    }

    private String getDevise(String devise) {
        if (devise != null){
            try {
                return URLEncoder.encode(devise, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        try {
            Log.i(TAG, "doInBackground: executed");
            name = URLEncoder.encode(name, "UTF-8");
            email = URLEncoder.encode(email, "UTF-8");
            int number;
            if (firstConnect) number = 7; else number = 11;
            URL url = new URL(MainJokes.HIGHSCORE_SERVER_BASE_URL + "?number=" + number
                    + "&user=" + name
                    + "&email=" + email
                    + "&photoUrl=" + personPic
                    + "&coverUrl=" + coverUrl
                    + "&platform=" + platform
                    + "&devise=" + devise
                    + "&key=" + liesKey());
            Log.i(TAG, "doInBackground: " + url);
            urlConnection = url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            try {
                BufferedReader r = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                StringBuilder total = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) {
                    total.append(line);
                }
                resp += total;
                resp = URLDecoder.decode(resp, "UTF-8");
                return true;
            } finally {
                in.close();
            }
        } catch (IOException e) {
            Log.e(TAG, "run: error", e);
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity, "Fehler", Toast.LENGTH_SHORT).show();
                }
            });
            return false;
        }
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        if (success){
            if (firstConnect) {
                SharedPreferences pref = activity.getSharedPreferences("Account", 0);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("Key", resp);
                editor.apply();
                pref = activity.getSharedPreferences("LoginValues", 0);
                editor = pref.edit();
                editor.putBoolean("isSignedIn", true);
                editor.putInt("platform", platform);
                editor.apply();
                Intent i = new Intent(activity, StartActivity.class);
                activity.finish();
                activity.startActivity(i);
            }
            if (resp != ""){
                //Toast.makeText(activity, "Profile updated", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onCancelled() {
    }

    private String liesKey(){
        SharedPreferences pref = activity.getSharedPreferences("Account", 0);
        return pref.getString("Key", "null");
    }
}
