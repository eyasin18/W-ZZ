package de.repictures.wzz;

import android.app.ActivityManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import java.util.Objects;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.repictures.wzz.account.Facebook;
import de.repictures.wzz.fragments.ProfileFragment;
import de.repictures.wzz.uiHelper.CheckRegistration;
import de.repictures.wzz.uiHelper.getPictures;

public class LoginActivity extends AppCompatActivity implements OnClickListener, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks ,DialogInterface.OnDismissListener {

    private static final int RC_SIGN_IN = 0;
    private static final String TAG = "LoginActivity";
    private boolean mIntentInProgress;
    public static final String TWITTER_KEY = "wSARmHHCPiMzzfl7z8nJg8fke";
    public static final String TWITTER_SECRET = "RkksvDODGFnZd5PB1EtO3la1FhQEx3uYEt5DqSUgbebw7r5HNE";
    public static int platform = 0;

    private boolean mSignInClicked;
    private ConnectionResult mConnectionResult;
    private GoogleApiClient mGoogleApiClient;

    @Bind(R.id.registerButton) Button registerButton;
    @Bind(R.id.register_profilepic) public ImageView profilePic;
    @Bind(R.id.email) EditText email_edit;
    @Bind(R.id.username) EditText username_edit;
    @Bind(R.id.password1) EditText password1_edit;
    @Bind(R.id.password2) EditText password2_edit;
    @Bind(R.id.about) EditText about_edit;
    @Bind(R.id.profile_sending) ProgressBar progressBar;

    public String profilePicUrl, coverUrl, mEmail, mUsername, mPassword1, mPassword2, mAbout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
            LoginActivity.this.setTaskDescription(new ActivityManager.TaskDescription("W!ZZ", appIcon, color));
            appIcon.recycle();
        }

        ButterKnife.bind(this);
        String email_name = getIntent().getStringExtra("EmailName");
        email_edit.setText(email_name);
        profilePic.setOnClickListener(this);
        registerButton.setOnClickListener(this);

        //Google Vorbereitungen
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .build();
    }

    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    private void resolveSignInError() {
        if (mConnectionResult.hasResolution()) {
            try {
                mIntentInProgress = true;
                mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
            } catch (IntentSender.SendIntentException e) {
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (!result.hasResolution()) {
            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this,
                    0).show();
            return;
        }

        if (!mIntentInProgress) {
            mConnectionResult = result;

            if (mSignInClicked) {
                resolveSignInError();
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode,
                                    Intent intent) {
        if (platform == 1) {
            if (requestCode == RC_SIGN_IN) {
                if (responseCode != RESULT_OK) {
                    mSignInClicked = false;
                }
                mIntentInProgress = false;
                if (!mGoogleApiClient.isConnecting()) {
                    mGoogleApiClient.connect();
                }
            }
        }
        else if (platform == 2){
            DialogFragment fragment = new ProfileFragment();
            fragment.onActivityResult(requestCode, responseCode, intent);
            new Thread(new getPictures(profilePicUrl, profilePic, null, this, false, true, false)).start();
        }
        else if (platform == 3) Facebook.callbackmanager.onActivityResult(requestCode, responseCode, intent);
    }

    @Override
    public void onClick(View v) {
        SharedPreferences pref = getSharedPreferences("LoginValues", 0);
        SharedPreferences.Editor editor = pref.edit();
        switch (v.getId()) {
            case R.id.facebook_button:
                platform = 3;
                editor.putBoolean("isSignedIn", true).apply();
                break;
            case R.id.register_profilepic:
                DialogFragment newFragment = new ProfileFragment();
                newFragment.show(getSupportFragmentManager(), "addProfliePic");
                break;
            case R.id.registerButton:
                mEmail = email_edit.getText().toString();
                mUsername = username_edit.getText().toString();
                mPassword1 = password1_edit.getText().toString();
                mPassword2 = password2_edit.getText().toString();
                mAbout = about_edit.getText().toString();
                if (checkEditTexts()) {
                    CheckRegistration mAuthTask = new CheckRegistration(mEmail, mUsername, mPassword1, mPassword2, mAbout, profilePicUrl, coverUrl,
                            progressBar, registerButton, this, email_edit);
                    registerButton.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                    mAuthTask.execute((Void) null);
                } break;
        }
        editor.apply();
    }

    private Boolean checkEditTexts() {
        email_edit.setError(null);
        username_edit.setError(null);
        password1_edit.setError(null);
        password2_edit.setError(null);
        if (profilePicUrl.length() == 0){
            return false;
        } else if (coverUrl.length() == 0){
            return false;
        } else if (!isEmailValid(mEmail)){
            email_edit.setError(getString(R.string.error_invalid_email));
            return false;
        } else if (!isUsernameValid(mUsername)){
            username_edit.setError(getString(R.string.error_username_6_letters));
            return false;
        } else if (!isPasswordValid(mPassword1)) {
            password1_edit.setError(getString(R.string.error_password_6_letters));
            return false;
        } else if (!passwordEqualsOther(mPassword1, mPassword2)){
            password2_edit.setError(getString(R.string.error_password_not_matching));
            return false;
        } else {
            return true;
        }
    }

    private boolean isPasswordValid(String mPassword1) {
        return mPassword1.length() > 6;
    }

    private boolean passwordEqualsOther(String mPassword1, String mPassword2){
        return Objects.equals(mPassword1, mPassword2);
    }

    private boolean isUsernameValid(String mUsername) {
        return mUsername.length() >= 6;
    }

    private boolean isEmailValid(String email) {
        return email.contains("@") && email.length() != 0;
    }

    @Override
    public void onBackPressed() {
        supportFinishAfterTransition();
        super.onBackPressed();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        Log.d(TAG, "Dialog dismissed");
        if (platform == 1) googleSignIn();
    }

    public void googleSignIn() {
        if (!mGoogleApiClient.isConnecting()) {
            mSignInClicked = true;
            resolveSignInError();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        mSignInClicked = false;
        Person person = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
        profilePicUrl = person.getImage().getUrl();
        profilePicUrl = profilePicUrl.substring(0,
                profilePicUrl.length() - 2)
                + 200;
        Person.Cover.CoverPhoto cover = person.getCover().getCoverPhoto();
        coverUrl = cover.getUrl();
        Log.d(TAG, coverUrl);
        new Thread(new getPictures(profilePicUrl, profilePic, null, this, false, true, false)).start();
        Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }
}