package de.repictures.wzz.fragments;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import de.repictures.wzz.MainJokes;
import de.repictures.wzz.R;
import de.repictures.wzz.adapter.SectionPagerAdapter;


public class JokesFragment extends Fragment{

    String postId;
    public static Toolbar mToolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    private FragmentActivity myContext;

    public JokesFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_short, container, false);
        mToolbar = (Toolbar) rootView.findViewById(R.id.jokesFragmentToolbar);
        tabLayout = (TabLayout) rootView.findViewById(R.id.jokesFragmentTabs);
        viewPager = (ViewPager) rootView.findViewById(R.id.jokesViewPager);
        FragmentManager fragManager = myContext.getSupportFragmentManager();
        String[] tabTitles = getActivity().getResources().getStringArray(R.array.tabTitles);
        viewPager.setAdapter(new SectionPagerAdapter(fragManager, tabTitles));
        viewPager.setOffscreenPageLimit(3);
        viewPager.setCurrentItem(1);
        //initProgressBar(rootView);
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        myContext = (FragmentActivity) activity;
        super.onAttach(activity);
    }

    private void initProgressBar(View rootView) {
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
    }

    @Override
    public void onStart() {
        tabLayout.setupWithViewPager(viewPager);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        postId = MainJokes.katego;
        super.onStart();
    }
}
