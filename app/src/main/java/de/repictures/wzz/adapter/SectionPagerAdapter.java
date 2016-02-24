package de.repictures.wzz.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import de.repictures.wzz.fragments.jokes.Heute;
import de.repictures.wzz.fragments.jokes.Neu;
import de.repictures.wzz.fragments.jokes.Beliebteste;

public class SectionPagerAdapter extends FragmentPagerAdapter {

    String[] tabTitles;

    public SectionPagerAdapter(FragmentManager fragManager, String[] tabTitles) {
        super(fragManager);
        this.tabTitles = tabTitles;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new Neu();
            case 1:
                default:
                return new Heute();
            case 2:
                return new Beliebteste();
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
