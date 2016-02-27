package de.repictures.wzz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.crashlytics.android.Crashlytics;
import com.facebook.FacebookSdk;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.mopub.common.MoPub;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.HashSet;

import de.repictures.wzz.internet.getProfile;
import de.repictures.wzz.internet.getProfileSplash;
import de.repictures.wzz.internet.wizzByKey;
import de.repictures.wzz.uiHelper.InfoGetter;
import io.fabric.sdk.android.Fabric;

public class SplashActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "Gbh5RprrKf8lBh8SSQt9xi5Tl";
    private static final String TWITTER_SECRET = "b1uVjJ980WqJ7RZSzHB1Lq3ZHJFytZXYtdsPLKMZnzLBVEZoi3";
    private static final String TAG = "Splash";
    public static int platform;
    public static GoogleApiClient mGoogleApiClient;
    public static String drawerName, drawerEmail, coverUrl, picUrl;
    public static ImageView drawerPic, drawerCover;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        // TODO: 21.08.2015 Wenn Beta: Isuetracker anschalten!!
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig), new Crashlytics());
        FacebookSdk.sdkInitialize(getApplicationContext());

        if (!getSignedIn()){
            Intent i = new Intent(SplashActivity.this, NewLoginActivity.class);
            startActivity(i);
            finish();
        }
        platform = getPlatform();
        Log.i(TAG, "onCreate: " + platform);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .addScope(Plus.SCOPE_PLUS_PROFILE)
                .build();
    }

    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        Uri data = intent.getData();
        if (data != null){
            String key = "ahBzfmRlcndpdHplc2VydmVyc" + data.toString().split("=")[1];
            if (!data.toString().contains("&#43")) {
                new Thread(new wizzByKey(key, this)).start(); // TODO: 21.02.16 Defaultlink auf apppage setzen!!
            } else if (data.toString().contains("&#43")){
                key = "ahBzfmRlcndpdHplc2VydmVyc" + data.toString().split("=")[1].split("&#43")[0];
                Intent i = new Intent(this, ProfileActivity.class);
                i.putExtra("userUrl", key);
                try {
                    i.putExtra("userName", URLDecoder.decode(data.toString().split("=")[1].split("&#43")[1], "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                startActivity(i);
            }
        } else {
            Log.i(TAG, "onCreate: " + platform);
            if (platform == 3){
                mGoogleApiClient.connect();
            } else {
                Log.i(TAG, "onStart: getProfile started");
                getProfileInformation(null);
            }
        }
    }

    private boolean getSignedIn(){
        SharedPreferences pref = getSharedPreferences("LoginValues", 0);
        return pref.getBoolean("isSignedIn", false);
    }

    @Override
    public void onConnected(Bundle bundle) {
        Person person = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
        Log.i(TAG, "onConnected: connected" + person.getDisplayName());
        getProfileInformation(person);
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {

    }

    private int getPlatform() {
        SharedPreferences pref = getSharedPreferences("LoginValues", 0);
        return pref.getInt("platform", 0);
    }

    private void getProfileInformation(final Person person){
        new Thread() {
            public void run() {
                new getProfile(liesKey(), "500");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (getProfile.infos != null){
                            SharedPreferences pref = getSharedPreferences("Abos", 0);
                            SharedPreferences.Editor editor = pref.edit();
                            HashSet<String> set;
                            try {
                                set = new HashSet<>(Arrays.asList(getProfile.infos[6].split("~")));
                            } catch (ArrayIndexOutOfBoundsException ignored){
                                set = new HashSet<>();
                            }
                            editor.putStringSet("Abonniert", set);
                            editor.apply();
                            new InfoGetter(platform, getProfile.infos[2], SplashActivity.this,
                                    drawerName, drawerEmail, drawerPic, drawerCover, person);
                        }
                    }
                });
            }
        }.start();
    }

    private String liesKey(){
        SharedPreferences pref = getSharedPreferences("Account", 0);
        return pref.getString("Key", "");
    }
}
