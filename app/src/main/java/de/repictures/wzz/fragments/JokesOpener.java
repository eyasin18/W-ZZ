package de.repictures.wzz.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import de.repictures.wzz.MainJokes;
import de.repictures.wzz.MainKatego;
import de.repictures.wzz.R;

public class JokesOpener {

    public JokesOpener(int extra, Toolbar mToolbar, String[] mTitles, String[] mUrls) {
        Fragment fragment;
        if (extra >= 0) {
            switch (extra) {
                case 0:
                    MainJokes.katego = mUrls[extra];
                    MainJokes.toolbarTitle = mTitles[extra];
                    break;
                case 1:
                    MainJokes.katego = mUrls[extra];
                    MainJokes.toolbarTitle = mTitles[extra];
                    break;
                case 2:
                    MainJokes.katego = mUrls[extra];
                    MainJokes.toolbarTitle = mTitles[extra];
                    break;
                case 3:
                    MainJokes.katego = mUrls[extra];
                    MainJokes.toolbarTitle = mTitles[extra];
                    break;
                case 4:
                    MainJokes.katego = mUrls[extra];
                    MainJokes.toolbarTitle = mTitles[extra];
                    break;
                case 5:
                    MainJokes.katego = mUrls[extra];
                    MainJokes.toolbarTitle = mTitles[extra];
                    break;
                case 6:
                    MainJokes.katego = mUrls[extra];
                    MainJokes.toolbarTitle = mTitles[extra];
                    break;
                case 7:
                    MainJokes.katego = mUrls[extra];
                    MainJokes.toolbarTitle = mTitles[extra];
                    break;
                case 8:
                    MainJokes.katego = mUrls[extra];
                    MainJokes.toolbarTitle = mTitles[extra];
                    break;
                case 9:
                    MainJokes.katego = mUrls[extra];
                    MainJokes.toolbarTitle = mTitles[extra];
                    break;
                case 10:
                    MainJokes.katego = mUrls[extra];
                    MainJokes.toolbarTitle = mTitles[extra];
                    break;
                case 11:
                    MainJokes.katego = mUrls[extra];
                    MainJokes.toolbarTitle = mTitles[extra];
                    break;
                case 12:
                    MainJokes.katego = mUrls[extra];
                    MainJokes.toolbarTitle = mTitles[extra];
                    break;
                case 13:
                    MainJokes.katego = mUrls[extra];
                    MainJokes.toolbarTitle = mTitles[extra];
                    break;
            }
            /*fragment = new JokesFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.jokesContainer, fragment)
                    .commit();
            Log.v("isFragmentOpened", MainKatego.isFragmentOpened.toString());*/
        }
    }
}
