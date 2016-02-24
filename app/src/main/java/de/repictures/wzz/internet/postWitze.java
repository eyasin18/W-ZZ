package de.repictures.wzz.internet;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

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

import de.repictures.wzz.AddJoke.AddJokeDialog;
import de.repictures.wzz.MainJokes;
import de.repictures.wzz.MainKatego;
import de.repictures.wzz.R;

public class postWitze implements Runnable {

    private static final String TAG = "postWitze";
    private String userName, katego, inhalt, key;
    private final Activity activity;
    private Context context;
    private final boolean isDialog;

    public postWitze(String katego, String writeWizzle, String key, Activity activity, Context context, boolean isDialog) {
        this.katego = katego;
        this.inhalt = writeWizzle;
        this.key = key;
        this.activity = activity;
        this.context = context;
        this.isDialog = isDialog;
    }

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

    @Override
    public void run(){
        try {
            String resp = "";
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
                if (resp != "success"){
                    Snackbar
                            .make(MainJokes.coordinatorLayoutView, R.string.post_success, Snackbar.LENGTH_LONG)
                            .show();
                } else {
                    Snackbar
                            .make(MainJokes.coordinatorLayoutView, R.string.error, Snackbar.LENGTH_LONG)
                            .setAction(R.string.save_joke, clickListener)
                            .show();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}