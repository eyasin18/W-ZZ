package de.repictures.wzz.uiHelper;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.twitter.sdk.android.Twitter;

import de.repictures.wzz.SplashActivity;

public class LogAllOut {

    private Activity activity;

    public LogAllOut(Activity activity, GoogleApiClient mGoogleApiClient) {
        this.activity = activity;

        SharedPreferences pref = activity.getSharedPreferences("LoginValues", 0);
        int platform = pref.getInt("platform", 0);
        if (platform == 3) {
            logOutGoogle();
            SharedPreferences.Editor editor = pref.edit();
            editor.clear();
            editor.apply();
            pref = activity.getSharedPreferences("Account", 0);
            editor = pref.edit();
            editor.clear();
            editor.apply();
            Intent i = new Intent(activity, SplashActivity.class);
            activity.startActivity(i);
        } else if (platform == 2){
            logOutFacebook();
            SharedPreferences.Editor editor = pref.edit();
            editor.clear();
            editor.apply();
            pref = activity.getSharedPreferences("Account", 0);
            editor = pref.edit();
            editor.clear();
            editor.apply();
            Intent i = new Intent(activity, SplashActivity.class);
            activity.startActivity(i);
        } else if (platform == 1){
            logOutTwitter();
            SharedPreferences.Editor editor = pref.edit();
            editor.clear();
            editor.apply();
            pref = activity.getSharedPreferences("Account", 0);
            editor = pref.edit();
            editor.clear();
            editor.apply();
            Intent i = new Intent(activity, SplashActivity.class);
            activity.startActivity(i);
        } else {
            Toast.makeText(activity, "Error", Toast.LENGTH_SHORT).show();
        }
    }

    private void logOutTwitter() {
        CookieSyncManager.createInstance(activity);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeSessionCookie();
        Twitter.getSessionManager().clearActiveSession();
        Twitter.logOut();
    }

    private void logOutFacebook() {
        LoginManager.getInstance().logOut();
    }

    private void logOutGoogle() {
        Plus.AccountApi.clearDefaultAccount(SplashActivity.mGoogleApiClient);
        Plus.AccountApi.revokeAccessAndDisconnect(SplashActivity.mGoogleApiClient);
        SplashActivity.mGoogleApiClient.disconnect();
    }
}
