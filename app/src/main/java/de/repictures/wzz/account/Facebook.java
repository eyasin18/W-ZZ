package de.repictures.wzz.account;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import de.repictures.wzz.AsyncTasks.CheckData;
import de.repictures.wzz.StartActivity;

public class Facebook {

    public static CallbackManager callbackmanager;
    private String personName, personPhotoUrl, coverUrl, email;
    private Activity activity;

    public Facebook(Activity activity) {

        this.activity = activity;
        onFblogin();
    }

    private void onFblogin() {
        callbackmanager = CallbackManager.Factory.create();
        LoginManager.getInstance().logInWithReadPermissions(activity, Arrays.asList("email", "user_photos", "public_profile"));

        LoginManager.getInstance().registerCallback(callbackmanager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(final LoginResult loginResult) {
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,last_name,link,cover,email,picture");
                        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {
                                if (jsonObject != null) {
                                    try {
                                        email = jsonObject.getString("id");
                                        personName = jsonObject.getString("name");
                                        personPhotoUrl = "https://graph.facebook.com/" + email + "/picture?type=large";
                                        JSONObject JOSource = jsonObject.optJSONObject("cover");
                                        coverUrl = JOSource.getString("source");
                                        Log.d("facebook", email + "~" + personName + "~" + personPhotoUrl + " ~ " + coverUrl);
                                        String[] data = {email, personName,
                                                "2", personPhotoUrl, coverUrl, "true", null, null, null, null};
                                        Intent i = new Intent(activity, StartActivity.class);
                                        i.putExtra("data", data);
                                        CheckData mAuthTask = new CheckData(activity, i, "2");
                                        mAuthTask.execute(email);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
                        request.setParameters(parameters);
                        request.executeAsync();
                    }

                    @Override
                    public void onCancel() {
                        Log.d("F Login canceled", "On cancel");
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.d("F Login Error", error.toString());
                    }
                });
    }
}
