package de.repictures.wzz.internet;

import android.app.Activity;
import android.content.SharedPreferences;
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
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import de.repictures.wzz.MainJokes;
import de.repictures.wzz.fragments.jokes.Beliebteste;
import de.repictures.wzz.fragments.jokes.Heute;
import de.repictures.wzz.fragments.jokes.Neu;

public class loadMoreJokes {

    private final RecyclerView mRecyclerView;
    private final Boolean sort;
    private final int totalItemCount;
    private final RecyclerView.Adapter mAdapter;
    private String postId;
    private final Activity activity;
    Boolean loading, removed;
    LinkedList<String> jokesList;
    List<String> addList = new LinkedList<>();

    public loadMoreJokes(RecyclerView mRecyclerView, Boolean sort, int totalItemCount,
                         RecyclerView.Adapter mAdapter, String postId, Activity activity){
        this.mRecyclerView = mRecyclerView;
        this.sort = sort;
        this.totalItemCount = totalItemCount;
        Log.d("...", "Count: " + totalItemCount);
        this.mAdapter = mAdapter;
        this.postId = postId;
        this.activity = activity;
        onPreExecute();
    }

    private void onPreExecute() {
        if (sort == null){
            addList = Neu.mNeuJokes;
            loading = Neu.loading;
            removed = Neu.removed;
        } else if (sort){
            addList = Beliebteste.mBeliebtesteJokes;
            loading = Beliebteste.loading;
            removed = Beliebteste.removed;
        } else {
            addList = Heute.mHeuteJokes;
            loading = Heute.loading;
            removed = Heute.removed;
        }
        if (!Neu.removed)startThread();
        else if (!Heute.removed)startThread();
        else if (!Beliebteste.removed)startThread();
    }

    private void doInBackground() {
        try {
            String witz = "";
            URL url = new URL(MainJokes.HIGHSCORE_SERVER_BASE_URL + "?number=0"
                    + "&katego=" + postId
                    + "&sortVotes=" + sort
                    + "&count=" + (totalItemCount-1)
                    + "&profileKey=" + liesKey());
            Log.d("...", "doInBackground: " + url);
            URLConnection urlConnection = url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            try {
                BufferedReader r = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                StringBuilder total = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) {
                    total.append(line);
                }
                witz += total;
            } finally {
                in.close();
                witz = URLDecoder.decode(witz, "UTF-8");
                activity.runOnUiThread(onPostExecute(witz.replace("<br />", "\n")));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Runnable onPostExecute(String jokes) {
        jokesList = new LinkedList<>(Arrays.asList(jokes.split("</we>")));
        int startPosition = jokesList.size();
        if ((startPosition) < 15 && !removed){
            addList.remove(addList.size() -1);
            mAdapter.notifyItemRemoved(addList.size() + 1); //nix
            for (int i = totalItemCount; i < totalItemCount + startPosition; i++){
                addItem(i);
            }
            if (sort == null)Neu.removed = true;
            else if (sort) Beliebteste.removed = true;
            else Heute.removed = true;
            addList.remove(addList.size() - 1);
            mAdapter.notifyItemRemoved(addList.size()); //-1
        } else if(!removed) {
            addList.remove(addList.size() -1);
            mAdapter.notifyItemRemoved(addList.size() + 1); //nix
            for (int i = totalItemCount; i < totalItemCount + startPosition; i++){
                addItem(i);
            }
            if (sort == null){Neu.loading = true;} else if (sort){Beliebteste.loading = true;}
            else {Heute.loading = true;}
        }
        return null;
    }

    private String liesKey(){
        SharedPreferences pref = activity.getSharedPreferences("Account", 0);
        return pref.getString("Key", "");
    }

    private void addItem(int position){
        if (position != RecyclerView.NO_POSITION){
            int i = position - totalItemCount;
            addList.add(jokesList.get(i));
            mAdapter.notifyItemInserted(position);
        }
    }

    private void startThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                doInBackground();
            }
        }).start();
    }

}
