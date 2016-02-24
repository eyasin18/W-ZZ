package de.repictures.wzz;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;

public class StartActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "StartActivity";
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private TextView like, dislike;
    public ProgressBar progressBar;
    int progress = 0;
    int[] likes = new int[5];

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            Slide slide = new Slide();
            slide.setSlideEdge(Gravity.LEFT);
            getWindow().setExitTransition(slide);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        like = (TextView) findViewById(R.id.start_like);
        dislike = (TextView) findViewById(R.id.start_dislike);
        like.setOnClickListener(this);
        dislike.setOnClickListener(this);
        progressBar = (ProgressBar) findViewById(R.id.start_progressbar);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.start_like:
                if (progress != 80) {
                    likes[progress/20] = 1;
                    progress += 20;
                    mViewPager.setCurrentItem((progress/20));
                    progressBar.setProgress(progress);
                } else {
                    likes[progress/20] = 1;
                    Log.d(TAG, "onClick: " + Arrays.toString(likes));
                    Intent i = new Intent(this, ApplyActivity.class);
                    i.putExtra("likes", likes);
                    i.putExtra("data", getIntent().getStringArrayExtra("data"));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        finish();
                        startActivity(i,
                                ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
                    } else {
                        finish();
                        startActivity(i);
                    }
                }
                break;
            case R.id.start_dislike:
                if (progress != 80) {
                    likes[progress/20] = 0;
                    progress += 20;
                    progressBar.setProgress(progress);
                    mViewPager.setCurrentItem((progress/20));
                } else {
                    likes[progress/20] = 0;
                    Log.d(TAG, "onClick: " + Arrays.toString(likes));
                    Intent i = new Intent(this, ApplyActivity.class);
                    i.putExtra("likes", likes);
                    i.putExtra("data", getIntent().getStringArrayExtra("data"));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        finish();
                        startActivity(i,
                                ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
                    } else {
                        finish();
                        startActivity(i);
                    }
                }
                break;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_start, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.start_wizzle);
            String wizzle = getResources().getString(R.string.error);
            switch (getArguments().getInt(ARG_SECTION_NUMBER)){
                case 1:
                    wizzle = getResources().getString(R.string.bsp_joke_1);
                    break;
                case 2:
                    wizzle = getResources().getString(R.string.bsp_joke_2);
                    break;
                case 3:
                    wizzle = getResources().getString(R.string.bsp_joke_3);
                    break;
                case 4:
                    wizzle = getResources().getString(R.string.bsp_joke_4);
                    break;
                case 5:
                    wizzle = getResources().getString(R.string.bsp_joke_5);
                    break;
            }
            try {
                textView.setText(URLDecoder.decode(wizzle, "UTF-8"));
            } catch (UnsupportedEncodingException ignore) {}
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 5;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
                case 3:
                    return "SECTION 4";
                case 4:
                    return "SECTION 5";
            }
            return null;
        }
    }
}
