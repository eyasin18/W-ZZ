package de.repictures.wzz;

import android.app.ActivityManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.appevents.AppEventsLogger;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import de.repictures.wzz.fragments.home.AboFragment;
import de.repictures.wzz.fragments.home.FavFragment;
import de.repictures.wzz.fragments.home.HomeFragment;
import de.repictures.wzz.fragments.home.MyJokesFragment;
import de.repictures.wzz.uiHelper.getPictures;

public class MainKatego extends AppCompatActivity {

    public static String personPhotoUrl;

    public static Toolbar toolbar;
    public static Drawable plusCover;

    private DrawerLayout drawerLayout;

    public static ImageView drawerPb, drawerCover;
    public static TextView drawerEmail, drawerName;
    private RelativeLayout startLayout;

    Intent i;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_katego);
        if(getResources().getBoolean(R.bool.portrait_only)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            Bitmap appIcon;
            TypedValue typedValue = new TypedValue();
            Resources.Theme theme = getTheme();
            theme.resolveAttribute(R.attr.colorPrimaryDark, typedValue, true);
            int color = typedValue.data;
            appIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
            MainKatego.this.setTaskDescription(new ActivityManager.TaskDescription("W!ZZ", appIcon, color));
            appIcon.recycle();
        }

        //startLayout = (RelativeLayout) findViewById(R.id.start_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.katego_drawer);
        drawerLayout = (DrawerLayout) findViewById(R.id.katego_drawerlayout);
        View drawerHeader = navigationView.inflateHeaderView(R.layout.drawer_header);

        drawerPb = (ImageView) drawerHeader.findViewById(R.id.drawer_header_pb);
        drawerCover = (ImageView) drawerHeader.findViewById(R.id.drawer_header_cover);
        drawerEmail = (TextView) drawerHeader.findViewById(R.id.drawer_header_email);
        drawerName = (TextView) drawerHeader.findViewById(R.id.drawer_header_name);

        new Thread(new getPictures(SplashActivity.picUrl, drawerPb, null, this, true, true, false)).start();
        new Thread(new getPictures(SplashActivity.coverUrl, drawerCover, null, this, false, false, false)).start();
        drawerEmail.setText(SplashActivity.drawerEmail);
        drawerName.setText(SplashActivity.drawerName);

        final Fragment[] fragment = {null};
        final FragmentManager fragmentManager = getSupportFragmentManager();
        fragment[0] = new HomeFragment();
        fragmentManager.beginTransaction()
                .replace(R.id.frame_container, fragment[0])
                .commit();

        navigationView.getMenu().getItem(0).setChecked(true);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                drawerLayout.closeDrawers();
                switch (menuItem.getItemId()) {
                    case R.id.nav_katego:
                        fragment[0] = new HomeFragment();
                        fragmentManager.beginTransaction()
                                .replace(R.id.frame_container, fragment[0])
                                .commit();
                        return true;
                    case R.id.nav_favo:
                        fragment[0] = new FavFragment();
                        fragmentManager.beginTransaction()
                                .replace(R.id.frame_container, fragment[0])
                                .commit();
                        return true;
                    case R.id.nav_abo:
                        fragment[0] = new AboFragment();
                        fragmentManager.beginTransaction()
                                .replace(R.id.frame_container, fragment[0])
                                .commit();
                        return true;
                    case R.id.nav_acc:
                        fragment[0] = new MyJokesFragment();
                        fragmentManager.beginTransaction()
                                .replace(R.id.frame_container, fragment[0])
                                .commit();
                        return true;
                    default:
                        Toast.makeText(MainKatego.this, getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                        return false;
                }
            }
        });
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawer_open, R.string.drawer_close){
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        setJokesState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setJokesState() {
        SharedPreferences pref = getSharedPreferences("MyProfile", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isSet", false);
        editor.apply();
    }
}