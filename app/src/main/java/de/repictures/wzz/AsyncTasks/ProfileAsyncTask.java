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
import java.util.LinkedList;
import java.util.List;

import de.repictures.wzz.MainJokes;
import de.repictures.wzz.R;
import de.repictures.wzz.fragments.ProfileJokesFragment;
import de.repictures.wzz.fragments.profile.MyProfileAboutFragment;
import de.repictures.wzz.fragments.profile.MyProfileFollowingFragment;
import de.repictures.wzz.fragments.profile.ProfileAboutFragment;
import de.repictures.wzz.uiHelper.getPictures;

public class ProfileAsyncTask extends AsyncTask<String, Void, String[]>{

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

    public ProfileAsyncTask(ViewPager viewPager, TabLayout tabLayout, ImageView pb, ImageView cover,
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
            URL url = new URL(MainJokes.HIGHSCORE_SERVER_BASE_URL + "?number=" + "12"
                    + "&user=" + key[0]
                    + "&profileKey=" + userKey);
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
            } finally {
                in.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resp.split("</we>");
    }

    @Override
    protected void onPostExecute(String[] response) {
        Log.i(TAG, "onPostExecute: " + Arrays.toString(response));
        new Thread(new getPictures(response[0].split("~")[1], pb, null, activity, true, true, false)).start();
        new Thread(new getPictures(response[0].split("~")[4], cover, null, activity, false, true, true)).start();
        scrollView.setVisibility(View.GONE);
        viewPager.setVisibility(View.VISIBLE);
        viewPager.setAdapter(new ProfilePagerAdapter(fragmentManager, tabTitles, response));
        viewPager.setCurrentItem(0);
        tabLayout.setupWithViewPager(viewPager);
    }

    private class ProfilePagerAdapter extends FragmentPagerAdapter {

        private final String[] tabTitles;
        private String[] profileInfos;
        private String[] jokes;
        private String[] abos;

        public ProfilePagerAdapter(FragmentManager fragManager, String[] tabTitles, String[] rawSplit) {
            super(fragManager);
            this.tabTitles = tabTitles;
            String[] response = rawSplit[0].split("~");
            Log.d(TAG, "ProfilePagerAdapter: " + Arrays.toString(response));
            abos = rawSplit[1].split("</~>");
            List<String> preprofile = new LinkedList<>(Arrays.asList(Arrays.copyOfRange(response, 0, 7)));
            preprofile.add(userKey);
            this.profileInfos = new String[preprofile.size()];
            this.profileInfos = preprofile.toArray(this.profileInfos);
            Log.d(TAG, "ProfilePagerAdapter: profileInfos: " + Arrays.toString(profileInfos));
            this.jokes = Arrays.copyOfRange(response, 7, response.length);
            Log.d(TAG, "ProfilePagerAdapter: jokes: " + Arrays.toString(jokes));

        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            switch (position) {
                case 0:
                    default:
                    Bundle bundle = new Bundle();
                    bundle.putStringArray("jokes", jokes);
                    bundle.putStringArray("profile", profileInfos);
                    Log.i(TAG, "getItem: " + Arrays.toString(jokes));
                    ProfileJokesFragment jfragment = new ProfileJokesFragment();
                    jfragment.setArguments(bundle);
                    return jfragment;
                case 1:
                    bundle = new Bundle();
                    bundle.putString("about", profileInfos[6]);
                    ProfileAboutFragment afragment = new ProfileAboutFragment();
                    afragment.setArguments(bundle);
                    return afragment;
                case 2:
                    bundle = new Bundle();
                    bundle.putStringArray("following", abos);
                    MyProfileFollowingFragment ffragment = new MyProfileFollowingFragment();
                    ffragment.setArguments(bundle);
                    return ffragment;
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
                    return tabTitles[0];
                case 1:
                default:
                    return tabTitles[1];
                case 2:
                    return tabTitles[2];
            }
        }
    }
}
