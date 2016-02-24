package de.repictures.wzz;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.repictures.wzz.fragments.PostComment;
import de.repictures.wzz.AsyncTasks.DetailAsyncTask;
import de.repictures.wzz.uiHelper.JokesClickListener;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "DetailActivity";
    @Bind(R.id.detail_recyclerview) RecyclerView detailRecycler;
    public static ArrayList<String[]> infosList = new ArrayList<>();
    private String witz_raw;
    private String[] extras;
    Boolean voter;
    @Bind(R.id.detail_fab) FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if(getResources().getBoolean(R.bool.portrait_only)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        ButterKnife.bind(this);
        witz_raw = getIntent().getStringExtra("DetailExtra");
        extras = witz_raw.split("~");
        voter = getIntent().getBooleanExtra("VoterAvailable", false);
        Log.i(TAG, "onCreate: " + Arrays.toString(extras));
        floatingActionButton.setOnClickListener(this);
        new DetailAsyncTask(detailRecycler, witz_raw, this, voter, (CoordinatorLayout)findViewById(R.id.detail_layout)).execute(extras[0]);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (voter) getMenuInflater().inflate(R.menu.menu_detail_voter, menu);
        else getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.report:
                Intent i = new Intent(this, ReportActivity.class);
                i.putExtra("detailExtras", extras);
                startActivity(i);
                Log.d("MenuDialog", "report clicked");
                break;
            case R.id.share:
                Log.d("MenuDialog", "share clicked");
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String key = extras[0].replace("ahBzfmRlcndpdHplc2VydmVyc", "");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "derwitzeserver.appspot.com/wizz?id=" + key);
                startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.share_via)));
                break;
            case R.id.favor:
                Log.d("MenuDialog", "favor clicked");
                JokesClickListener.favorite.performClick();
                break;
            case R.id.detaillist_thumb_up:
                Log.d("MenuDialog", "like clicked");
                JokesClickListener.thumbUp.performClick();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v == floatingActionButton){
            DialogFragment newFragment = new PostComment(witz_raw);
            newFragment.show(getSupportFragmentManager(), "addJoke");
        }
    }
}