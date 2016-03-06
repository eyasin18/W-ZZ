package de.repictures.wzz.fragments.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;

import java.util.HashSet;
import java.util.Set;

import de.repictures.wzz.AsyncTasks.MyProfileAsyncTask;
import de.repictures.wzz.MainKatego;
import de.repictures.wzz.ProfileActivity;
import de.repictures.wzz.R;
import de.repictures.wzz.SplashActivity;
import de.repictures.wzz.adapter.ProfilePagerAdapter;
import de.repictures.wzz.fragments.profile.MyProfileAboutFragment;
import de.repictures.wzz.fragments.profile.MyProfileFollowingFragment;
import de.repictures.wzz.fragments.profile.MyProfileJokesFragment;
import de.repictures.wzz.uiHelper.AppBarStateChangeListener;
import de.repictures.wzz.uiHelper.LogAllOut;
import de.repictures.wzz.uiHelper.getPictures;

public class MyJokesFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "MyJokesFragment";
    public static CoordinatorLayout myJokesCL;
    ImageView accCover, accPb, share, settings;
    Thread s;
    FloatingActionButton myProfile;
    ViewPager viewPager;
    Boolean collapsed = false;
    AppBarLayout appBar;
    NestedScrollView scrollView;
    TextView username;
    GoogleApiClient mGoogleApiClient;

    public MyJokesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_my_jokes, container, false);

        myJokesCL = (CoordinatorLayout) rootView.findViewById(R.id.main_content);
        myProfile = (FloatingActionButton) rootView.findViewById(R.id.logout_fab);
        myProfile.setOnClickListener(this);
        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) rootView.findViewById(R.id.collapsingToolbarLayout);
        setHasOptionsMenu(true);
        scrollView = (NestedScrollView) rootView.findViewById(R.id.profile_scrollview);
        appBar = (AppBarLayout) rootView.findViewById(R.id.appbar);
        final TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.accTabs);
        share = (ImageView) rootView.findViewById(R.id.my_jokes_share);
        //settings = (ImageView) rootView.findViewById(R.id.my_jokes_settings);
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .addScope(Plus.SCOPE_PLUS_PROFILE)
                .build();

        final int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
        appBar.addOnOffsetChangedListener(new AppBarStateChangeListener(MainKatego.toolbar, getActivity()) {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                final View view = rootView.findViewById(R.id.statusbar_background);
                if (state == State.COLLAPSED && !collapsed){
                    myProfile.hide();
                    view.animate().setDuration(shortAnimTime).translationY(-MainKatego.toolbar.getHeight()*3/2).start();
                    share.animate().setDuration(shortAnimTime).alpha(1).start();
                    //settings.animate().setDuration(shortAnimTime).alpha(1).start();
                    collapsed = true;
                    Log.d(TAG, "onStateChanged: changed");
                } else if (state == State.IDLE && collapsed){
                    view.animate().setDuration(shortAnimTime).translationY(tabLayout.getX()).start();
                    share.animate().setDuration(shortAnimTime).alpha(0).start();
                    //settings.animate().setDuration(shortAnimTime).alpha(0).start();
                    myProfile.show();
                    collapsed = false;
                }
            }
        });

        viewPager = (ViewPager) rootView.findViewById(R.id.profileViewPager);
        String jokes = getActivity().getResources().getString(R.string.jokes);
        String about = getActivity().getResources().getString(R.string.profile);
        String following = getActivity().getResources().getString(R.string.prof_table_abos);
        String[] tabTitles = new String[] {jokes, about, following};
        accCover = (ImageView) rootView.findViewById(R.id.accCover);
        accPb = (ImageView) rootView.findViewById(R.id.accPb);
        MainKatego.toolbar = (Toolbar) rootView.findViewById(R.id.accTitleToolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(MainKatego.toolbar);
        username = (TextView) rootView.findViewById(R.id.my_jokes_username);
        getActivity().setTitle("");
        username.setText(MainKatego.drawerName.getText());
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        new MyProfileAsyncTask(viewPager, tabLayout, accPb, accCover, tabTitles,
                getActivity().getSupportFragmentManager(), getActivity(), scrollView, liesKey(getActivity()))
                .execute(liesKey(getActivity()));
        return rootView;
    }

    @Override
    public void onClick(View v) {
        new LogAllOut(getActivity(), mGoogleApiClient);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity){
            FragmentActivity myContext = (FragmentActivity) context;
        }

    }

    private String liesKey(Activity activity){
        SharedPreferences pref = activity.getSharedPreferences("Account", 0);
        return pref.getString("Key", "");
    }
}
