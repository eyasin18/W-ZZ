package de.repictures.wzz.AsyncTasks;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import java.util.Locale;

import de.repictures.wzz.MainJokes;
import de.repictures.wzz.SplashActivity;

public class PassData extends AsyncTask<Void, Void, Boolean> {

    private static final String TAG = "PassData";
    private final String vName;
    private String email;
    private String name;
    private final String personPic;
    private final String coverUrl;
    private Activity activity;
    private String resp = "";
    private int platform = 0;
    private Boolean firstConnect;
    private final ProgressBar progressbar;
    private final ImageView check;
    private final int crazyValue;
    private final String about;
    private String devise;
    URLConnection urlConnection;
    private Boolean female;

    public PassData(String email, String name, int platform, String personPic, String coverUrl, Activity activity,
                    Boolean firstConnect, String devise, ProgressBar progressbar, ImageView check,
                    int crazyValue, String about, String vName, Boolean female) {
        this.female = female;
        this.email = encoder(email);
        this.name = encoder(name);
        this.personPic = personPic;
        this.coverUrl = coverUrl;
        this.activity = activity;
        this.platform = platform;
        this.firstConnect = firstConnect;
        this.progressbar = progressbar;
        this.check = check;
        this.crazyValue = crazyValue;
        this.vName = encoder(vName);
        this.about = encoder(about);
        this.devise = encoder(devise);
    }

    private String encoder(String string) {
        if (string != null){
            try {
                return URLEncoder.encode(string, "UTF-8");
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
            String langage = null;
            if (firstConnect) {
                number = 7;
                langage = URLEncoder.encode(Locale.getDefault().getDisplayLanguage(), "UTF-8");
            } else {
                number = 11;
            }
            URL url = new URL(MainJokes.HIGHSCORE_SERVER_BASE_URL + "?number=" + number
                    + "&user=" + name
                    + "&email=" + email
                    + "&photoUrl=" + personPic
                    + "&coverUrl=" + coverUrl
                    + "&platform=" + platform
                    + "&devise=" + devise
                    + "&key=" + liesKey()
                    + "&count=" + crazyValue
                    + "&inhalt=" + about
                    + "&katego=" + vName
                    + "&lang=" + langage
                    + "&votedUp=" + female); //gender
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
                int shortAnimTime = activity.getResources().getInteger(android.R.integer.config_shortAnimTime);
                progressbar.animate().setDuration(shortAnimTime).alpha(0f).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        progressbar.setVisibility(View.INVISIBLE);

                    }
                }).start();
                check.setVisibility(View.VISIBLE);
                check.animate().setDuration(shortAnimTime).alpha(1f).start();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(activity, SplashActivity.class);
                        activity.finish();
                        activity.startActivity(i);
                    }
                }, 1200);
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
