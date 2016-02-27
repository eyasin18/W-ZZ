package de.repictures.wzz;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.ActivityManager;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Path;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.LinkedList;

import de.repictures.wzz.AddJoke.AddJokeDialog;
import de.repictures.wzz.AsyncTasks.JokesAsyncTask;
import de.repictures.wzz.adapter.SectionPagerAdapter;
import de.repictures.wzz.fragments.JokesOpener;
import de.repictures.wzz.fragments.PostDialog;
import de.repictures.wzz.fragments.jokes.Beliebteste;
import de.repictures.wzz.fragments.jokes.Heute;
import de.repictures.wzz.fragments.jokes.Neu;
import de.repictures.wzz.uiHelper.MainJokesFab;

public class MainJokes extends AppCompatActivity implements View.OnClickListener {

    public static int extra;
    Context context;
    ImageButton sendButton;
    public static FloatingActionButton fabbutton;
    String postId;
    public static Toolbar mToolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    String[] mUrls;
    public static int initialRadius;
    public static EditText writeWizzle;
    public static String toolbarTitle;
    public static String katego = "null";
    public static InputMethodManager imm;
    public static CoordinatorLayout coordinatorLayoutView;
    private Handler mHandler = new Handler();
    private View dataView, dataView2;
    private ProgressBar progressBar;

    public static final String HIGHSCORE_SERVER_BASE_URL = "http://derwitzeserver.appspot.com/derwitzeserver";
    public static final String AD_UNIT_ID = "231594b48c604751bf6a8247af1d2a75";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_jokes);
        if(getResources().getBoolean(R.bool.portrait_only)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        coordinatorLayoutView = (CoordinatorLayout) findViewById(R.id.mainJokesLayout);
        mToolbar = (Toolbar) findViewById(R.id.jokesFragmentToolbar);
        tabLayout = (TabLayout) findViewById(R.id.jokesFragmentTabs);
        viewPager = (ViewPager) findViewById(R.id.jokesViewPager);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        dataView = findViewById(R.id.dataView);
        dataView2 = findViewById(R.id.dataView2);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        context = getApplicationContext();
        fabbutton = (FloatingActionButton)findViewById(R.id.fab_2);
        writeWizzle = (EditText)findViewById(R.id.editWizzle);
        progressBar = (ProgressBar) findViewById(R.id.load_jokes);

        extra = getIntent().getIntExtra("ActivityValue", -1);
        String[] mTitles = getResources().getStringArray(R.array.classes);
        mUrls = getResources().getStringArray(R.array.urls);
        new JokesOpener(extra, mToolbar, mTitles, mUrls);
        if (katego != "null"){
            android.support.v4.app.FragmentManager fragManager = getSupportFragmentManager();
            if (getSupportActionBar() != null) getSupportActionBar().setTitle(toolbarTitle);
            new JokesAsyncTask(viewPager, tabLayout, fragManager, progressBar, this).execute(katego);
        }
        fabbutton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                    Intent intent = new Intent(MainJokes.this, AddJokeDialog.class);
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainJokes.this,
                            fabbutton, getString(R.string.transition_dialog));
                    startActivityForResult(intent, 100, options.toBundle());
                } else {
                    Intent i = new Intent(MainJokes.this, AddJokeDialog.class);
                    startActivityForResult(i, 100);
                }
            }
        });
        sendButton = (ImageButton) findViewById(R.id.sendbutton);
        sendButton.setOnClickListener(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            Bitmap appIcon;
            TypedValue typedValue = new TypedValue();
            Resources.Theme theme = getTheme();
            theme.resolveAttribute(R.attr.colorPrimaryDark, typedValue, true);
            int color = typedValue.data;
            appIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
            MainJokes.this.setTaskDescription(new ActivityManager.TaskDescription("W!ZZ", appIcon, color));
            appIcon.recycle();
        } else {
            fabbutton.setVisibility(View.GONE);
        }
        if (!liesFirstStart()) {
            SharedPreferences firstStartPref = getSharedPreferences("FirstStart", 0);
            firstStartPref.edit().putBoolean("firstStart", false);
            firstStartPref.edit().apply();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        initialRadius = writeWizzle.getWidth();
        Display display = getWindowManager().getDefaultDisplay();
        new MainJokesFab(v, display, fabbutton, sendButton, writeWizzle, imm, initialRadius,
                mHandler, katego, liesUserName(), liesUserEmail(), dataView, dataView2, context, liesKey());

        internetRunOnUiThread();
    }

    @Override
    public void onBackPressed() {
        if (writeWizzle.getVisibility() == View.VISIBLE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                int yr, xr;
                xr = writeWizzle.getWidth() - fabbutton.getWidth()*3;
                yr = writeWizzle.getHeight() - fabbutton.getHeight()/2;
                float x1 = dataView2.getX();
                float y1 = dataView2.getY();
                float x2 = dataView.getX();
                float y3 = dataView.getY();

                final Path path2 = new Path();
                path2.moveTo(x1, y1);
                path2.cubicTo(x1, y1, x2, y1, x2, y3);

                final ObjectAnimator anim2 = ObjectAnimator.ofFloat(fabbutton, View.X, View.Y, path2);
                anim2.setDuration(100);
                anim2.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        super.onAnimationStart(animation);
                        fabbutton.setVisibility(View.VISIBLE);
                    }
                });
                Animator anim =
                        ViewAnimationUtils.createCircularReveal(writeWizzle, xr, yr, initialRadius, 0);
                anim.setInterpolator(new AccelerateDecelerateInterpolator());
                anim.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        MainJokes.animationEnd();
                    }
                });
                imm.hideSoftInputFromWindow(writeWizzle.getWindowToken(), 0);
                sendButton.setVisibility(View.GONE);
                anim2.setStartDelay(anim.getDuration()-50);
                anim2.start();
                anim.start();
            }
        } else {
            finish();
        }
    }

    private String liesUserName(){
        SharedPreferences pref = getSharedPreferences("UserInformations", 0);
        return pref.getString("UserName", "");
    }

    private String liesUserEmail(){
        SharedPreferences pref = getSharedPreferences("UserInformations", 0);
        return pref.getString("UserEmail", "");
    }

    private String liesKey(){
        SharedPreferences pref = getSharedPreferences("Account", 0);
        return pref.getString("Key", "");
    }

    public static void animationEnd(){
        writeWizzle.setVisibility(View.INVISIBLE);
        imm.showSoftInput(writeWizzle, InputMethodManager.SHOW_IMPLICIT);
    }

    public void internetRunOnUiThread() {
        MainJokes.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                writeWizzle.setText("");
            }
        });
    }

    private Boolean liesFirstStart() {
        SharedPreferences pref = getSharedPreferences("FirstStart", 0);
        return pref.getBoolean("firstStart", true);
    }

    @Override
    protected void onDestroy() {
        Beliebteste.mBeliebtesteJokes = new LinkedList<>();
        Beliebteste.loading = true;
        Beliebteste.removed = false;
        Heute.mHeuteJokes = new LinkedList<>();
        Heute.loading = true;
        Heute.removed = false;
        Neu.mNeuJokes = new LinkedList<>();
        Neu.loading = true;
        Neu.removed = false;
        super.onDestroy();
    }
}