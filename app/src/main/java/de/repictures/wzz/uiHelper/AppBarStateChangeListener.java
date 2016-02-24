package de.repictures.wzz.uiHelper;

import android.app.Activity;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;

public abstract class AppBarStateChangeListener implements AppBarLayout.OnOffsetChangedListener {

    private static final String TAG = "AppBarStateChange";
    private Toolbar toolbar;
    private Activity activity;

    public AppBarStateChangeListener(Toolbar toolbar, Activity activity) {
        this.toolbar = toolbar;
        this.activity = activity;
    }

    public enum State {
        EXPANDED,
        COLLAPSED,
        IDLE,
        MAX
    }

    private State mCurrentState = State.IDLE;

    @Override
    public final void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        if (i == 0) {
            if (mCurrentState != State.EXPANDED) {
                onStateChanged(appBarLayout, State.EXPANDED);
            }
            mCurrentState = State.EXPANDED;
        } else if(Math.abs(i) == getTotalHeigt(appBarLayout)){
            if (mCurrentState != State.MAX){
                onStateChanged(appBarLayout, State.MAX);
            }
        } else if (Math.abs(i) >= getAppbarHeight(toolbar, appBarLayout)) { //appBarLayout.getTotalScrollRange() - 173
            if (mCurrentState != State.COLLAPSED) {
                onStateChanged(appBarLayout, State.COLLAPSED);
            }
            mCurrentState = State.COLLAPSED;
        }  else {
            if (mCurrentState != State.IDLE) {
                onStateChanged(appBarLayout, State.IDLE);
            }
            mCurrentState = State.IDLE;
        }
    }

    private int getTotalHeigt(AppBarLayout appBarLayout) {
        int statusbar = 0;
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                statusbar = activity.getResources().getDimensionPixelSize(resourceId);
                Log.i(TAG, "Statusbar: " + statusbar + "\n" + "TotalRange: " + appBarLayout.getTotalScrollRange());
            }
        }*/
        return appBarLayout.getTotalScrollRange() - statusbar;
    }

    private int getAppbarHeight(Toolbar toolbar, AppBarLayout appBarLayout) {
        return appBarLayout.getTotalScrollRange() - toolbar.getHeight()*2/3;
    }

    public abstract void onStateChanged(AppBarLayout appBarLayout, State state);
}
