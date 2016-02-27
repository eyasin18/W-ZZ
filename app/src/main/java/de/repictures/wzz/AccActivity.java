package de.repictures.wzz;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.repictures.wzz.adapter.JokesAdapter;
import de.repictures.wzz.internet.witzNachUser;

public class AccActivity extends AppCompatActivity implements View.OnClickListener {

    @Bind(R.id.accCover) ImageView accCover;
    Thread t;
    @Bind(R.id.accCollapsingToolbar) Toolbar toolbar;
    String postId = "kurzeWitze";
    private String witzUi;
    @Bind(R.id.logout_fab) FloatingActionButton logout;
    Bitmap paletteBitmap;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acc);
        ButterKnife.bind(this);
        initRecyclerview();
        logout.setOnClickListener(this);
        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbarLayout);
        collapsingToolbarLayout.setTitle(MainKatego.drawerName.getText());

        accCover.setImageDrawable(MainKatego.drawerCover.getDrawable());
        paletteBitmap = ((BitmapDrawable) MainKatego.drawerCover.getDrawable()).getBitmap();
        Palette.from(paletteBitmap).generate(new Palette.PaletteAsyncListener() {
            public void onGenerated(Palette p) {
                Palette.Swatch vibrant =
                        p.getVibrantSwatch();
                if (vibrant != null){
                    collapsingToolbarLayout.setContentScrimColor(vibrant.getRgb());
                    collapsingToolbarLayout.setStatusBarScrimColor(p.getDarkVibrantSwatch().getRgb());
                }
            }
        });
        this.setSupportActionBar(toolbar);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //applyBlur();

        t = new Thread(new witzNachUser(MainKatego.drawerEmail.getText().toString()));
        t.start();
        Thread z = new Thread(new Runnable() {
            @Override
            public void run() {
                internetRunOnUiThread();
            }
        });
        z.start();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_fragment_acc, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public String toString() {
        return "RenderScript";
    }

    private String liesUserEmail(){
        SharedPreferences pref = getSharedPreferences("UserInformations", 0);
        return pref.getString("UserEmail", "");
    }
    private String liesUserName(){
        SharedPreferences pref = getSharedPreferences("UserInformations", 0);
        return pref.getString("UserName", "");
    }

    private void initRecyclerview() {
        mRecyclerView = (RecyclerView) findViewById(R.id.profile_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    private void internetRunOnUiThread() {
        try {
            t.join();
            witzUi = witzNachUser.witz;
            Log.v("UserInputList", witzUi);
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (witzUi != "" && witzUi != null) {
                        mAdapter = new JokesAdapter(Arrays.asList(witzUi.split("~")), false, MainJokes.coordinatorLayoutView, AccActivity.this);
                        mRecyclerView.swapAdapter(mAdapter, true);
                    }
                }
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.logout_fab:
                /*if (mGoogleApiClient.isConnected()){
                    Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
                    mGoogleApiClient.disconnect();
                    mGoogleApiClient.connect();
                    SharedPreferences pref = getSharedPreferences("UserInformations", 0);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.remove("UserName");
                    editor.remove("UserEmail");
                    editor.apply();
                    SharedPreferences pref2 = getSharedPreferences("LoginValues", 0);
                    pref2.edit().putBoolean("isSignedIn", false).apply();
                    Intent i = new Intent(AccActivity.this, LoginActivity.class);
                    startActivity(i);
                }*/
        }
    }
}
