package de.repictures.wzz.AsyncTasks;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;

import de.repictures.wzz.MainJokes;
import de.repictures.wzz.adapter.DetailListAdapter;

public class DetailAsyncTask extends AsyncTask<String, Void, String[]>{

    private static final String TAG = "DetailAsyncTask";
    private final RecyclerView recyclerView;
    private String raw_infos;
    private Activity activity;
    private Boolean voter;
    private CoordinatorLayout coordinatorLayout;
    private RecyclerView.Adapter recyclerAdapter;

    public DetailAsyncTask(RecyclerView recyclerView, String raw_infos, Activity activity,
                           Boolean voter, CoordinatorLayout coordinatorLayout) {
        this.recyclerView = recyclerView;
        this.raw_infos = raw_infos;
        this.activity = activity;
        this.voter = voter;
        this.coordinatorLayout = coordinatorLayout;
    }

    @Override
    protected void onPreExecute() {
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerAdapter = new DetailListAdapter(activity, raw_infos, null, null, null, null, null, null, null, false, true, coordinatorLayout);
        recyclerView.setAdapter(recyclerAdapter);
    }

    @Override
    protected String[] doInBackground(String... key) {
        String resp = "";
        try {
            URL url = new URL(MainJokes.HIGHSCORE_SERVER_BASE_URL + "?number=" + "3"
                    + "&key=" + URLEncoder.encode(key[0], "UTF-8")
                    + "&profileKey=" + liesKey(activity));
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
            } finally {
                resp = URLDecoder.decode(resp, "UTF-8");
                in.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resp.split("~");
    }

    @Override
    protected void onPostExecute(String[] result) {
        if (result.length > 1) {
            Log.i(TAG, "onPostExecute: " + Arrays.toString(result));
            ArrayList<String> comments = new ArrayList<>(), names = new ArrayList<>(), votes = new ArrayList<>(),
                    photos = new ArrayList<>(), userKeys = new ArrayList<>(), commentKeys = new ArrayList<>();
            ArrayList<Boolean> voted = new ArrayList<>();
            recyclerAdapter = new DetailListAdapter(activity, raw_infos, comments,
                    votes, names, photos, userKeys, voted, commentKeys, null, voter, coordinatorLayout);
            recyclerView.swapAdapter(recyclerAdapter, true);
            for (int c = 0; c < result.length; c += 7) {
                comments.add(result[c]);
                names.add(result[c + 1]);
                votes.add(result[c + 2]);
                photos.add(result[c + 3]);
                userKeys.add(result[c + 4]);
                voted.add(Boolean.parseBoolean(result[c + 5]));
                commentKeys.add(result[c + 6]);
            }
            for (int c = 0; c<result.length/8; c++){
                recyclerAdapter.notifyItemInserted(c);
            }
        } else {
            Log.i(TAG, "onPostExecute: length == 0");
            recyclerAdapter = new DetailListAdapter(activity, raw_infos, null,
                    null, null, null, null, null, null, true, voter, coordinatorLayout);
            recyclerView.swapAdapter(recyclerAdapter, false);
            recyclerAdapter.notifyDataSetChanged();
        }
    }

    private String liesKey(Activity activity){
        SharedPreferences pref = activity.getSharedPreferences("Account", 0);
        return pref.getString("Key", "");
    }
}
