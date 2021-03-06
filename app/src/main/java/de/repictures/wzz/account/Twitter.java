package de.repictures.wzz.account;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.User;

import de.repictures.wzz.AsyncTasks.CheckData;
import de.repictures.wzz.R;
import de.repictures.wzz.StartActivity;
import retrofit.http.GET;
import retrofit.http.Query;

public class Twitter {

    public Twitter(TwitterSession session, Activity activity) {
        getProfileInformation(session, activity);
    }

    private void getProfileInformation(final TwitterSession session, final Activity activity) {
        new MyTwitterApiClient(session).getUsersService().show(session.getId(), null, true,
                new Callback<User>() {
                    @Override
                    public void success(Result<User> result) {
                        String profilePic = result.data.profileImageUrlHttps;
                        profilePic = profilePic.replace("_normal", "");
                        Log.d("twitter", "user's profile is "
                                + profilePic
                                + "~" + result.data.profileBannerUrl
                                + "~" + result.data.name
                                + "~" + result.data.idStr);
                        String[] data = {result.data.idStr, result.data.name,
                                "1", profilePic, result.data.profileBannerUrl, "true", null, null, null, null};
                        Intent i = new Intent(activity, StartActivity.class);
                        i.putExtra("data", data);
                        CheckData mAuthTask = new CheckData(activity, i, "1");
                        mAuthTask.execute(result.data.idStr);
                    }

                    @Override
                    public void failure(TwitterException exception) {
                        Toast.makeText(activity, activity.getResources().getString(R.string.info_error), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    class MyTwitterApiClient extends TwitterApiClient {
        public MyTwitterApiClient(TwitterSession session) {
            super(session);
        }

        public UsersService getUsersService() {
            return getService(UsersService.class);
        }
    }

    interface UsersService {
        @GET("/1.1/users/show.json")
        void show(@Query("user_id") Long userId,
                  @Query("screen_name") String screenName,
                  @Query("include_entities") Boolean includeEntities,
                  Callback<User> cb);
    }

}
