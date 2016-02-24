package de.repictures.wzz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.ColorFilter;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashSet;

import de.repictures.wzz.AsyncTasks.AboAsyncTask;
import de.repictures.wzz.AsyncTasks.ProfileAsyncTask;
import de.repictures.wzz.fragments.ProfileJokesFragment;
import de.repictures.wzz.fragments.profile.MyProfileAboutFragment;
import de.repictures.wzz.fragments.profile.MyProfileFollowingFragment;
import de.repictures.wzz.fragments.profile.MyProfileJokesFragment;
import de.repictures.wzz.uiHelper.AppBarStateChangeListener;
import de.repictures.wzz.uiHelper.AutoResizeTextView;
import de.repictures.wzz.uiHelper.getPictures;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "ProfileActivity";
    public static CoordinatorLayout cl;
    Boolean collapsed = false;
    ImageView profileCover, profilePB, share, report, back;
    AutoResizeTextView profileTitle;
    FloatingActionButton abbo_fab;
    ViewPager viewPager;
    NestedScrollView scrollView;
    String userUrl, userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.profileTitleToolbar);
        setSupportActionBar(toolbar);

        cl = (CoordinatorLayout) findViewById(R.id.main_content);
        Intent intent = getIntent();
        userUrl = intent.getStringExtra("userUrl");
        userName = intent.getStringExtra("userName");
        abbo_fab = (FloatingActionButton) findViewById(R.id.abbo_fab);
        if (isAbo(userUrl)) {
            Drawable thumbUp_cyan = getResources().getDrawable(R.drawable.ic_heart_white_18dp);
            int cyan = getResources().getColor(R.color.white);
            ColorFilter filter = new PorterDuffColorFilter(cyan, PorterDuff.Mode.MULTIPLY);
            if (thumbUp_cyan != null) {
                thumbUp_cyan.setColorFilter(filter);
            }
            abbo_fab.setImageDrawable(thumbUp_cyan);
        }
        abbo_fab.setOnClickListener(this);
        scrollView = (NestedScrollView) findViewById(R.id.profile_scrollview);
        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbarLayout);
        collapsingToolbarLayout.setContentScrimColor(getResources().getColor(R.color.profile_primary));
        toolbar.setTitle("");
        share = (ImageView) findViewById(R.id.share);
        share.setOnClickListener(this);
        report = (ImageView) findViewById(R.id.report);
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(this);
        final TabLayout tabLayout = (TabLayout) findViewById(R.id.profileTabs);
        AppBarLayout appBar = (AppBarLayout) findViewById(R.id.appbar);
        appBar.addOnOffsetChangedListener(new AppBarStateChangeListener(toolbar, this) {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                final View view = findViewById(R.id.statusbar_background);
                int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
                if (state == State.COLLAPSED && !collapsed){
                    abbo_fab.hide();
                    view.animate().setDuration(shortAnimTime).translationY(-toolbar.getHeight() * 3 / 2).start();
                    share.animate().setDuration(shortAnimTime).alpha(1).start();
                    report.animate().setDuration(shortAnimTime).alpha(1).start();
                    collapsed = true;
                    Log.d(TAG, "onStateChanged: changed");
                } else if (state == State.IDLE && collapsed) {
                    view.animate().setDuration(shortAnimTime).translationY(tabLayout.getX()).start();
                    share.animate().setDuration(shortAnimTime).alpha(0).start();
                    report.animate().setDuration(shortAnimTime).alpha(0).start();
                    abbo_fab.show();
                    if (isAbo(userUrl)) {
                        Drawable thumbUp_cyan = getResources().getDrawable(R.drawable.ic_heart_white_18dp);
                        int cyan = getResources().getColor(R.color.white);
                        ColorFilter filter = new PorterDuffColorFilter(cyan, PorterDuff.Mode.MULTIPLY);
                        if (thumbUp_cyan != null) {
                            thumbUp_cyan.setColorFilter(filter);
                        }
                        abbo_fab.setImageDrawable(thumbUp_cyan);
                    }
                    collapsed = false;
                }
            }
        });

        viewPager = (ViewPager) findViewById(R.id.profileViewPager);
        String jokes = getResources().getString(R.string.jokes);
        String about = getResources().getString(R.string.profile);
        String following = getResources().getString(R.string.prof_table_abos);
        String[] tabTitles = new String[] {jokes, about, following};
        profileTitle = (AutoResizeTextView) findViewById(R.id.profileTitleText);
        profileTitle.setText(userName);
        profileCover = (ImageView) findViewById(R.id.profileCover);
        profilePB = (ImageView) findViewById(R.id.profilePb);
        new ProfileAsyncTask(viewPager, tabLayout, profilePB, profileCover, tabTitles,
                getSupportFragmentManager(), this, scrollView, userUrl)
                .execute(userUrl);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.abbo_fab:
                new AboAsyncTask(cl, abbo_fab, this).execute(userUrl);
                break;
            case R.id.share:
                Log.d("ProfileActivity", "share clicked");
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String key = userUrl.replace("ahBzfmRlcndpdHplc2VydmVyc", "");
                try {
                    sharingIntent.putExtra(Intent.EXTRA_TEXT, "derwitzeserver.appspot.com/wizz?pro=" + key
                                                + "&#43" + URLEncoder.encode(userName, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.share_via)));
                break;
        }
    }

    private Boolean isAbo(String user) {
        SharedPreferences pref = getSharedPreferences("Abos", 0);
        return pref.getStringSet("Abonniert", new HashSet<String>()).contains(user);
    }
}
