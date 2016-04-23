package de.repictures.wzz.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import de.repictures.wzz.fragments.profile.MyProfileAboutFragment;
import de.repictures.wzz.fragments.profile.MyProfileFollowingFragment;
import de.repictures.wzz.fragments.profile.MyProfileJokesFragment;

public class ProfilePagerAdapter extends FragmentStatePagerAdapter {

    private final String[] tabTitles;
    private String[] profileInfos;
    private String[] jokes;
    private String[] abos;

    public ProfilePagerAdapter(FragmentManager fragManager, String[] tabTitles, String[] rawSplit, String userKey) {
        super(fragManager);
        this.tabTitles = tabTitles;
        String[] response = rawSplit[0].split("~");
        try {
            abos = rawSplit[1].split("</~>");
        } catch (ArrayIndexOutOfBoundsException ignore){
            abos = null;
        }
        List<String> preprofile = new LinkedList<>(Arrays.asList(Arrays.copyOfRange(response, 0, 8)));
        preprofile.add(userKey);
        this.profileInfos = new String[preprofile.size()];
        this.profileInfos = preprofile.toArray(this.profileInfos);
        this.jokes = Arrays.copyOfRange(response, 8, response.length);

    }

    @Override
    public android.support.v4.app.Fragment getItem(int position) {
        switch (position) {
            case 0:
                Bundle bundle = new Bundle();
                bundle.putStringArray("jokes", jokes);
                bundle.putStringArray("profile", profileInfos);
                MyProfileJokesFragment jfragment = new MyProfileJokesFragment();
                jfragment.setArguments(bundle);
                return jfragment;
            case 1:
            default:
                bundle = new Bundle();
                bundle.putStringArray("profile", profileInfos);
                MyProfileAboutFragment afragment = new MyProfileAboutFragment();
                afragment.setArguments(bundle);
                return afragment;
            case 2:
                if (abos != null){
                    bundle = new Bundle();
                    bundle.putStringArray("following", abos);
                    MyProfileFollowingFragment ffragment = new MyProfileFollowingFragment();
                    ffragment.setArguments(bundle);
                    return ffragment;
                } else {
                    return new Fragment();
                }
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
