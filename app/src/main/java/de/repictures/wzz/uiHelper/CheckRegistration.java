package de.repictures.wzz.uiHelper;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import de.repictures.wzz.LoginActivity;
import de.repictures.wzz.MainJokes;
import de.repictures.wzz.MainKatego;
import de.repictures.wzz.R;


public class CheckRegistration extends AsyncTask<Void, Void, Boolean> {

    private String profilePicUrl, coverUrl, mEmail, mUsername, mPassword1, mPassword2, mAbout;
    private ProgressBar progressBar;
    private Button registerButton;
    Activity activity;
    Boolean result, emailVorhanden;
    private EditText email_edit;

    public CheckRegistration(String email, String username, String password1, String password2,
                             String about, String profilePicUrl, String coverUrl,
                             ProgressBar progressBar, Button registerButton, LoginActivity loginActivity, EditText email_edit) {
        this.profilePicUrl = profilePicUrl;
        this.coverUrl = coverUrl;
        this.mEmail = email;
        this.mUsername = username;
        this.mPassword1 = password1;
        this.mPassword2 = password2;
        this.mAbout = about;
        this.activity = loginActivity;
        this.progressBar = progressBar;
        this.registerButton = registerButton;
        this.email_edit = email_edit;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        try {
            String resp = "";
            URL url = new URL(MainJokes.HIGHSCORE_SERVER_BASE_URL + "?number=" + "7"
                    + "&user=" + URLEncoder.encode(mUsername, "UTF-8")
                    + "&email=" + URLEncoder.encode(mEmail, "UTF-8")
                    + "&photoUrl=" + profilePicUrl
                    + "&coverUrl=" + coverUrl
                    + "&devise=" + URLEncoder.encode(mAbout, "UTF-8")
                    + "&password=" + URLEncoder.encode(mPassword2, "UTF-8"));
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
                SharedPreferences pref = activity.getSharedPreferences("Account", 0);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("Key", resp);
                editor.apply();
                emailVorhanden = resp.length() == 4;
                result = resp.length() > 4;
            }
            return result;
        } catch (IOException e) {
            Log.d("Registration", e.toString());
            return false;
        }
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        if (success){
            SharedPreferences pref = activity.getSharedPreferences("LoginValues", 0);
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("isSignedIn", true);
            editor.apply();
            Intent i = new Intent(activity, MainKatego.class);
            activity.startActivity(i);
        } else if (emailVorhanden){
            email_edit.setError(activity.getString(R.string.error_invalid_email));
            progressBar.setVisibility(View.GONE);
            registerButton.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
            registerButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onCancelled() {

    }
}