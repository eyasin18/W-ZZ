package de.repictures.wzz.AsyncTasks;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.Arrays;

import de.repictures.wzz.MainJokes;
import de.repictures.wzz.R;
import de.repictures.wzz.adapter.SectionPagerAdapter;
import de.repictures.wzz.fragments.ProfileJokesFragment;
import de.repictures.wzz.fragments.jokes.Beliebteste;
import de.repictures.wzz.fragments.jokes.Heute;
import de.repictures.wzz.fragments.jokes.Neu;

public class JokesAsyncTask extends AsyncTask<String, Void, String>{

    private static final String TAG = "JokesAsyncTask";
    private final ViewPager viewPager;
    private final TabLayout tabLayout;
    private final FragmentManager fragManager;
    private ProgressBar progressBar;
    private final Activity activity;

    public JokesAsyncTask(ViewPager viewPager, TabLayout tabLayout, FragmentManager fragManager, ProgressBar progressBar, Activity activity) {

        this.viewPager = viewPager;
        this.tabLayout = tabLayout;
        this.fragManager = fragManager;
        this.progressBar = progressBar;
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
            viewPager.setOffscreenPageLimit(3);
    }

    @Override
    protected String doInBackground(String... POST_GET_ID) {
        try {
            String witz = "";
            URL url = new URL(MainJokes.HIGHSCORE_SERVER_BASE_URL + "?number=13"
                    + "&katego=" + POST_GET_ID[0]
                    + "&profileKey=" + liesKey());
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
                return URLDecoder.decode(witz, "UTF-8").replace("<br />", "\n");
            } finally {
                in.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String liesKey(){
        SharedPreferences pref = activity.getSharedPreferences("Account", 0);
        return pref.getString("Key", "");
    }

    @Override
    protected void onPostExecute(String jokes) {
        String[] tabTitles = activity.getResources().getStringArray(R.array.tabTitles);
        viewPager.setAdapter(new JokesPagerAdapter(fragManager, tabTitles, jokes.split("</~>")));
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(1);
        viewPager.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    private class JokesPagerAdapter extends FragmentPagerAdapter {
        String[] tabTitles;
        private final String[] split;

        public JokesPagerAdapter(FragmentManager fragManager, String[] tabTitles, String[] split) {
            super(fragManager);
            this.tabTitles = tabTitles;
            this.split = split;
        }

        @Override
        public Fragment getItem(int position) {
            Bundle bundle = new Bundle();
            switch (position) {
                case 0:
                    bundle = new Bundle();
                    bundle.putString("jokes", split[0]);
                    Log.i(TAG, "getItem: " + split[0]);
                    Neu fragment = new Neu();
                    fragment.setArguments(bundle);
                    return fragment;
                case 1:
                default:
                    bundle.putString("jokes", split[1]);
                    Log.i(TAG, "getItem: " + split[1]);
                    Heute fragment1 = new Heute();
                    fragment1.setArguments(bundle);
                    return fragment1;
                case 2:
                    bundle = new Bundle();
                    bundle.putString("jokes", split[2]);
                    Log.i(TAG, "getItem: " + split[2]);
                    Beliebteste fragment2 = new Beliebteste();
                    fragment2.setArguments(bundle);
                    return fragment2;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return tabTitles[1];
                case 1:
                default:
                    return tabTitles[0];
                case 2:
                    return tabTitles[2];
            }
        }
    }
}