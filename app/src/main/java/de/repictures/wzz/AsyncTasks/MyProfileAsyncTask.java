package de.repictures.wzz.AsyncTasks;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import de.repictures.wzz.MainJokes;
import de.repictures.wzz.R;
import de.repictures.wzz.adapter.ProfilePagerAdapter;
import de.repictures.wzz.fragments.ProfileJokesFragment;
import de.repictures.wzz.fragments.profile.MyProfileAboutFragment;
import de.repictures.wzz.fragments.profile.MyProfileFollowingFragment;
import de.repictures.wzz.fragments.profile.MyProfileJokesFragment;
import de.repictures.wzz.fragments.profile.ProfileAboutFragment;
import de.repictures.wzz.uiHelper.getPictures;

public class MyProfileAsyncTask extends AsyncTask<String, Void, String[]>{

    private static final String TAG = "ProfileAsyncTask";
    private final ViewPager viewPager;
    private final TabLayout tabLayout;
    private final ImageView pb;
    private final ImageView cover;
    private final String[] tabTitles;
    private final FragmentManager fragmentManager;
    private Activity activity;
    private NestedScrollView scrollView;
    private final String userKey;

    public MyProfileAsyncTask(ViewPager viewPager, TabLayout tabLayout, ImageView pb, ImageView cover,
                            String[] tabTitles, FragmentManager fragmentManager, Activity activity,
                            NestedScrollView scrollView, String userUrl) {
        this.viewPager = viewPager;
        this.tabLayout = tabLayout;
        this.pb = pb;
        this.cover = cover;
        this.tabTitles = tabTitles;
        this.fragmentManager = fragmentManager;
        this.activity = activity;
        this.scrollView = scrollView;
        this.userKey = userUrl;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String[] doInBackground(String... key) {
        String resp = "";
        try {
            Log.d(TAG, "doInBackground: " + key[0]);
            URL url = new URL(MainJokes.HIGHSCORE_SERVER_BASE_URL + "?number=" + "1"
                    + "&user=" + URLDecoder.decode(key[0], "UTF-8"));
            URLConnection urlConnection = url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader r = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            StringBuilder total = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                total.append(line);
            }
            resp += total;
            resp = URLDecoder.decode(resp, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resp.split("</we>");
    }

    @Override
    protected void onPostExecute(String[] response) {
        Log.i(TAG, "onPostExecute: " + Arrays.toString(response));
        new Thread(new getPictures(response[0].split("~")[2], pb, null, activity, true, true, false)).start();
        new Thread(new getPictures(response[0].split("~")[5], cover, null, activity, false, true, true)).start();
        scrollView.setVisibility(View.GONE);
        viewPager.setVisibility(View.VISIBLE);
        viewPager.setAdapter(new ProfilePagerAdapter(fragmentManager, tabTitles, response, userKey));
        viewPager.setCurrentItem(0);
        tabLayout.setupWithViewPager(viewPager);
    }
}

