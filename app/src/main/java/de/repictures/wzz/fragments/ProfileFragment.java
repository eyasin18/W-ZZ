package de.repictures.wzz.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import de.repictures.wzz.LoginActivity;
import de.repictures.wzz.R;
import io.fabric.sdk.android.Fabric;

public class ProfileFragment extends DialogFragment implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    EditText writeWizzle;
    Activity context;
    GoogleApiClient mGoogleApiClient;
    SignInButton GoogleSignIn;
    private static final int RC_SIGN_IN = 0;
    private TwitterLoginButton twitterLogIn;
    public static String twitterProfilePicUrl;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder =
                new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.JokesDialogStyle));
        context = getActivity();
        TwitterAuthConfig authConfig = new TwitterAuthConfig(LoginActivity.TWITTER_KEY, LoginActivity.TWITTER_SECRET);
        Fabric.with(getActivity(), new Twitter(authConfig));
        twitterProfilePicUrl = null;
        LoginActivity.platform = 0;
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View rootView = inflater.inflate(R.layout.fragment_my_profile, null);
        GoogleSignIn = (SignInButton) rootView.findViewById(R.id.google_sign_in_button);
        GoogleSignIn.setOnClickListener(this);
        twitterLogIn = (TwitterLoginButton) rootView.findViewById(R.id.twitter_button);
        twitterLogIn.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                Log.d("ProfileFragment", "twitter loged in");
                TwitterSession session = Twitter.getSessionManager().getActiveSession();
                new de.repictures.wzz.account.Twitter(session, getActivity());
                dismiss();
            }

            @Override
            public void failure(TwitterException exception) {
                Log.d("ProfileFragment", "twitter log in failed");
                Toast.makeText(getActivity(), getResources().getString(R.string.login_error), Toast.LENGTH_SHORT).show();
            }
        });
        twitterLogIn.setOnClickListener(this);
        builder.setTitle(R.string.import_image)
                .setView(rootView)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dismiss();
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.google_sign_in_button:
                LoginActivity.platform = 1;
                dismiss();
                break;
            case R.id.twitter_button:
                Log.d("ProfileFragment", "twitter button clicked");
                LoginActivity.platform = 2;
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        final Activity activity = getActivity();
        if (activity instanceof DialogInterface.OnDismissListener) {
            ((DialogInterface.OnDismissListener) activity).onDismiss(dialog);
        }
    }
}
