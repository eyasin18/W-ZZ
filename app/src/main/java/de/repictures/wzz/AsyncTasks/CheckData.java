package de.repictures.wzz.AsyncTasks;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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
import de.repictures.wzz.SplashActivity;

public class CheckData extends AsyncTask<String, Void, String> {

    private final Activity activity;
    private final Intent i;
    private int platform;

    public CheckData(Activity activity, Intent i, String platform) {
        this.activity = activity;
        this.i = i;
        this.platform = Integer.parseInt(platform);
    }

    @Override
    protected String doInBackground(String... params) {
        String resp = "";
        try {
            URL url = new URL(MainJokes.HIGHSCORE_SERVER_BASE_URL + "?number=" + 17
                    + "&email=" + params[0]);
            URLConnection urlConnection = url.openConnection();
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
                return resp;
            } finally {
                in.close();
            }
        } catch (IOException e) {
            Log.e("CheckData", "run: error", e);
            return "Error!";
        }
    }

    @Override
    protected void onPostExecute(String resp) {
        if (resp.length() == 5){
            activity.startActivity(i);
        } else if (resp.length() > 5){
            SharedPreferences pref = activity.getSharedPreferences("Account", 0);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("Key", resp);
            editor.apply();
            pref = activity.getSharedPreferences("LoginValues", 0);
            editor = pref.edit();
            editor.putBoolean("isSignedIn", true);
            editor.putInt("platform", platform);
            editor.apply();
            Intent i = new Intent(activity, SplashActivity.class);
            activity.finish();
            activity.startActivity(i);
        }
    }
}
