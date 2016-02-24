package de.repictures.wzz.internet;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import de.repictures.wzz.MainJokes;
import de.repictures.wzz.R;

public class postJokes extends AsyncTask<Void, Void, Boolean> {

    private static final String TAG = "postJokes";
    URLConnection urlConnection;
    private String katego, inhalt, key;
    private final Activity activity;
    private Context context;
    private final Button send;
    private final Button cancel;
    private final EditText writeWizzle;
    private ProgressBar progressBar;
    String resp = "";

    public postJokes(String katego, String writeWizzleStr, String key, Activity activity, Context context,
                     Button send, Button cancel, EditText writeWizzle, ProgressBar progressBar) {
        this.katego = katego;
        this.inhalt = writeWizzleStr;
        this.key = key;
        this.activity = activity;
        this.context = context;
        this.send = send;
        this.cancel = cancel;
        this.writeWizzle = writeWizzle;
        this.progressBar = progressBar;
        showProgress(true);
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        try {
            Date date = Calendar.getInstance().getTime();
            DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss z", Locale.getDefault());
            String dateStr = dateFormat.format(date);
            Log.v("Date", dateStr);
            inhalt = inhalt.trim();
            URL url = new URL(MainJokes.HIGHSCORE_SERVER_BASE_URL + "?number=" + "2"
                    + "&katego=" + katego
                    + "&datum=" + URLEncoder.encode(dateStr, "UTF-8")
                    + "&user=" + key
                    + "&inhalt=" + URLEncoder.encode(inhalt, "UTF-8"));
            Log.d(TAG, "run " + url.toString());
            URLConnection urlConnection = url.openConnection();
            try (InputStream in = new BufferedInputStream(urlConnection.getInputStream())) {
                BufferedReader r = new BufferedReader(new InputStreamReader(in, "iso-8859-1"));
                StringBuilder total = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) {
                    total.append(line);
                }
                resp += total;
                return true;
            }
        } catch (IOException e) {
            Log.e(TAG, "doInBackground: postJoke", e);
            return false;
        }
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        if (success){
            if (resp != "success") {
                activity.setResult(Activity.RESULT_CANCELED);
                writeWizzle.setVisibility(View.GONE);
                send.setVisibility(View.GONE);
                cancel.setVisibility(View.GONE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                    activity.finishAfterTransition();
                } else {
                    activity.finish();
                }
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Snackbar
                                .make(MainJokes.coordinatorLayoutView, R.string.post_success, Snackbar.LENGTH_LONG)
                                .show();
                    }
                }, 500);
            } else {
                activity.setResult(Activity.RESULT_CANCELED);
                writeWizzle.setVisibility(View.GONE);
                send.setVisibility(View.GONE);
                cancel.setVisibility(View.GONE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                    activity.finishAfterTransition();
                } else {
                    activity.finish();
                }
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Snackbar
                                .make(MainJokes.coordinatorLayoutView, R.string.error, Snackbar.LENGTH_LONG)
                                .setAction(R.string.save_joke, clickListener)
                                .show();
                    }
                }, 500);
            }
        }
    }

    @Override
    protected void onCancelled() {}

    final View.OnClickListener clickListener = new View.OnClickListener() {
        public void onClick(View v) {
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("label", inhalt);
            clipboard.setPrimaryClip(clip);
            Snackbar
                    .make(MainJokes.coordinatorLayoutView, R.string.save_to_clipboard, Snackbar.LENGTH_LONG)
                    .show();
        }
    };

    private void showProgress(final boolean show) {
        final int shortAnimTime = activity.getResources().getInteger(android.R.integer.config_shortAnimTime);
        send.setVisibility(show ? View.INVISIBLE : View.VISIBLE);
        send.animate().setDuration(shortAnimTime).alpha(show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                send.setVisibility(show ? View.INVISIBLE : View.VISIBLE);
            }
        });
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        progressBar.animate().setDuration(shortAnimTime).alpha(show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }
}

