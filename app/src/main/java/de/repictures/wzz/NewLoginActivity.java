package de.repictures.wzz;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.IntentSender;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.crashlytics.android.Crashlytics;
import com.facebook.login.widget.LoginButton;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.flaviofaria.kenburnsview.Transition;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.plus.People;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.google.android.gms.plus.model.people.PersonBuffer;
import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import de.repictures.wzz.account.Facebook;
import de.repictures.wzz.account.GooglePlus;
import de.repictures.wzz.uiHelper.BlurTransform;
import io.fabric.sdk.android.Fabric;

import static android.Manifest.permission.READ_CONTACTS;

public class NewLoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor>, OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final int REQUEST_READ_CONTACTS = 0;
    private static final String TAG = "LoginActivity2";
    private static final String TWITTER_KEY = "Gbh5RprrKf8lBh8SSQt9xi5Tl";
    private static final String TWITTER_SECRET = "b1uVjJ980WqJ7RZSzHB1Lq3ZHJFytZXYtdsPLKMZnzLBVEZoi3";
    private UserLoginTask mAuthTask = null;
    private static final int TRANSITIONS_TO_SWITCH = 1;

    private ViewFlipper mViewSwitcher;

    private int mTransitionsCount = 0;
    private int platform = 0;

    private GoogleApiClient mGoogleApiClient;
    private ConnectionResult mConnectionResult;
    private static final int RC_SIGN_IN = 0;
    private boolean mIntentInProgress;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private Button mEmailSignInButton, mRegisterButton, mEmailLoginButton;
    public static Boolean emailValid = false;
    TwitterLoginButton mTwitterButton;
    SignInButton mGoogleButton;
    LoginButton mFacebookButton;
    private TextInputLayout mInputLayout, mEmailInput;
    private RelativeLayout mButtonsLayout, mLoginForm;
    private Boolean isEmailOpened = false;
    private String sPassword;
    private String sKey;
    private boolean mSignInClicked;
    private ImageView background;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_login);
        if(getResources().getBoolean(R.bool.portrait_only)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig), new Crashlytics());
        background = (ImageView) findViewById(R.id.login_background);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        long verhältnis = size.y/size.x;
        int height = Math.round(480*verhältnis);
        Picasso.with(NewLoginActivity.this).load(R.drawable.background).resize(size.x/4, size.y/4).centerCrop().transform(new BlurTransform(this, 8)).into(background);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
        mButtonsLayout = (RelativeLayout) findViewById(R.id.buttonsLayout);
        mLoginForm = (RelativeLayout) findViewById(R.id.email_login_form);
        mEmailLoginButton = (Button) findViewById(R.id.login_with_email_button);
        mEmailLoginButton.setOnClickListener(this);
        mGoogleButton = (SignInButton) findViewById(R.id.google_sign_in_button);
        mGoogleButton.setOnClickListener(this);
        mFacebookButton = (LoginButton) findViewById(R.id.facebook_button);
        mFacebookButton.setOnClickListener(this);
        mTwitterButton = (TwitterLoginButton) findViewById(R.id.twitter_button);
        mTwitterButton.setOnClickListener(this);
        mTwitterButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                TwitterSession session = Twitter.getSessionManager().getActiveSession();
                TwitterAuthToken authToken = session.getAuthToken();
                String token = authToken.token;
                String secret = authToken.secret;
                Log.d("ProfileFragment", "twitter loged in");
                new de.repictures.wzz.account.Twitter(session, NewLoginActivity.this);
            }

            @Override
            public void failure(TwitterException e) {

            }
        });
        mRegisterButton = (Button) findViewById(R.id.registerButton);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        mRegisterButton.setX(displaymetrics.widthPixels);
        mRegisterButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
        mProgressView = findViewById(R.id.login_progress);
        mInputLayout = (TextInputLayout) findViewById(R.id.inputlayout);
        mEmailInput = (TextInputLayout) findViewById(R.id.emailinput);
        mViewSwitcher = (ViewFlipper) findViewById(R.id.viewSwitcher);
        /*KenBurnsView img1 = (KenBurnsView) findViewById(R.id.img1);
        img1.setTransitionListener(this);
        KenBurnsView img2 = (KenBurnsView) findViewById(R.id.img2);
        img2.setTransitionListener(this);
        KenBurnsView img3 = (KenBurnsView) findViewById(R.id.img3);
        img3.setTransitionListener(this);*/

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
        mGoogleApiClient.connect();
    }

    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_SIGN_IN) {
            if (resultCode != RESULT_OK) {
                mSignInClicked = false;
            }
            mIntentInProgress = false;
            if (!mGoogleApiClient.isConnecting()) {
                mGoogleApiClient.connect();
            }
        }
        Log.i(TAG, "onActivityResult: executed");
        mTwitterButton.onActivityResult(requestCode, resultCode, data);
        if (platform == 3)Facebook.callbackmanager.onActivityResult(requestCode, resultCode, data);
    }

    private void register() {
        Intent i = new Intent(NewLoginActivity.this, LoginActivity.class);
        i.putExtra("EmailName", mEmailView.getText().toString());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            Pair<View, String> p1 = Pair.create((View) mRegisterButton, "profile");
            Pair<View, String> p2 = Pair.create((View) mEmailView, "palette");
            ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation(this, p1, p2);
            KenBurnsView currentImage = (KenBurnsView) mViewSwitcher.getCurrentView();
            currentImage.pause();
            startActivity(i, options.toBundle());
        } else {
            startActivity(i);
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            if (emailValid){
                mAuthTask = new UserLoginTask(email, password);
            } else {
                mAuthTask = new UserLoginTask(email, password);
            }
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mEmailSignInButton.setVisibility(show ? View.GONE : View.VISIBLE);
            mEmailSignInButton.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mEmailSignInButton.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mEmailSignInButton.setVisibility(show ? View.VISIBLE : View.GONE);
            mEmailSignInButton.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private void showEmail(final Boolean show) {
        final int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
        mRegisterButton.setVisibility(show ? View.VISIBLE : View.GONE);
        mRegisterButton.animate().setDuration(shortAnimTime).alpha(show ? 1 : 0)
                .setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mRegisterButton.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
        mButtonsLayout.setVisibility(show ? View.INVISIBLE : View.VISIBLE);
        mButtonsLayout.animate().setDuration(shortAnimTime).alpha(show ? 0 : 1)
                .setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mButtonsLayout.setVisibility(show ? View.INVISIBLE : View.VISIBLE);
            }
        });
        mLoginForm.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
        mLoginForm.animate().setDuration(shortAnimTime).alpha(show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mLoginForm.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
            }
        });
        isEmailOpened = show;
    }

    @Override
    public void onBackPressed() {
        if (isEmailOpened){
            showEmail(false);
        } else {
            finish();
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_with_email_button:
                showEmail(true);
                break;
            case R.id.twitter_button:
                platform = 2;
                break;
            case R.id.google_sign_in_button:
                googleSignIn();
                break;
            case R.id.facebook_button:
                platform = 3;
                new Facebook(this);
                break;
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
    public void onConnected(Bundle bundle) {
        mSignInClicked = false;
        Person person = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
        Log.d(TAG, "person name is " + person.getDisplayName());
        new GooglePlus(NewLoginActivity.this, person);
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
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

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(NewLoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void googleSignIn() {
        if (!mGoogleApiClient.isConnecting()) {
            mSignInClicked = true;
            resolveSignInError();
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private String[] output;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            if (!emailValid) {
                Boolean logedIn = null;
                try {
                    String response = "";
                    URL url = new URL(MainJokes.HIGHSCORE_SERVER_BASE_URL + "?number=" + "6"
                            + "&email=" + mEmail);
                    URLConnection urlConnection = url.openConnection();
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    try {
                        BufferedReader r = new BufferedReader(new InputStreamReader(in, "iso-8859-1"));
                        StringBuilder total = new StringBuilder();
                        String line;
                        while ((line = r.readLine()) != null) {
                            total.append(line);
                        }
                        response += total;
                    } finally {
                        in.close();
                        int counter = 0;
                        for (int i = 0; i < response.length(); i++) {
                            counter++;
                        }
                        if (counter == 4) {
                            logedIn = false;
                        } else {
                            logedIn = true;
                            output = response.split("~");
                            sKey = output[0];
                            sPassword = output[1];
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return logedIn;
            } else {
                return mPassword.equals(sPassword);
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success && !emailValid) {
                mRegisterButton.animate().translationYBy(mEmailView.getHeight() + mEmailSignInButton.getHeight() / 5);
                mProgressView.animate().translationYBy(mEmailView.getHeight() + mEmailSignInButton.getHeight() / 5);
                mEmailSignInButton.setText(NewLoginActivity.this.getResources().getString(R.string.action_sign_in_short));
                mEmailSignInButton.animate().translationYBy(mEmailView.getHeight() + mEmailSignInButton.getHeight() / 3);
                mInputLayout.setVisibility(View.VISIBLE);
                mInputLayout.animate().translationYBy(mEmailView.getHeight() + mEmailSignInButton.getHeight() / 5);
                mInputLayout.animate().alpha(1);
                emailValid = true;
            } else if (success){
                SharedPreferences pref = getSharedPreferences("Account", 0);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("Key", sKey).apply();
                pref = getSharedPreferences("LoginValues", 0);
                editor = pref.edit();
                editor.putBoolean("isSignedIn", true).apply();
                emailValid = false;
                Intent i = new Intent(NewLoginActivity.this, SplashActivity.class);
                startActivity(i);
                finish();
            } else if (!emailValid){
                //email nicht vergeben
                emailValid = false;
                mEmailView.setError(getString(R.string.error_invalid_email));
                mEmailView.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        mEmailView.setError(null);
                        mRegisterButton.animate()
                                .translationX(mInputLayout.getX());
                    }
                }, 1500);
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}