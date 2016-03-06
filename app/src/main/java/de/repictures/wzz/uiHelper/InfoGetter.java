package de.repictures.wzz.uiHelper;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.plus.model.people.Person;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.User;

import de.repictures.wzz.MainKatego;
import de.repictures.wzz.R;
import de.repictures.wzz.SplashActivity;
import de.repictures.wzz.AsyncTasks.PassData;
import de.repictures.wzz.internet.getProfile;
import retrofit.http.GET;
import retrofit.http.Query;

public class InfoGetter {

    private static final String TAG = "InfoGetter";
    public static String coverUrl, profileUrl, email;
    private TwitterSession session;
    private Activity activity;
    private final Person person;

    public InfoGetter(int platform, String id, Activity activity, String drawerName, String drawerEmail,
                      ImageView drawerPic, ImageView drawerCover, Person person){
        this.activity = activity;
        this.person = person;
        switch (platform){
            case 1:
                getTwitter();
                Log.i(TAG, "InfoGetter: getTwitter");
                break;
            case 2:
                getFacebook(id);
                Log.i(TAG, "InfoGetter: getFacebook");
                break;
            case 3:
                getGoogle();
                Log.i(TAG, "InfoGetter: getGoogle");
                break;
        }
    }

    private void getGoogle() {
        String coverUrl = null;
        Person.Cover cover = person.getCover();
        if (cover != null) {
            Person.Cover.CoverPhoto coverPhoto = cover.getCoverPhoto();
            if (coverPhoto != null) {
                coverUrl = coverPhoto.getUrl();
                Log.i("Main TAG", "Cover photo Url :" + coverUrl);
            }
        } else {
            coverUrl = activity.getResources().getString(R.string.cover_placeholder);
            Log.i("TAG NO COVER", "Person has no cover");
        }
        Log.d("google", "user's profile is "
                + person.getImage().getUrl()
                + "~" + coverUrl
                + "~" + person.getDisplayName()
                + "~" + person.getId());
        String personPic = person.getImage().getUrl();
        personPic = personPic.substring(0,
                personPic.length() - 2)
                + 500;
        String personName = person.getDisplayName();
        InfoGetter.email = person.getId();
        if (personName != getProfile.infos[0] || personPic != getProfile.infos[1]||
                coverUrl != getProfile.infos[4]){
            PassData mAuthTask = new PassData(person.getId(), getProfile.infos[0],
                    2, personPic, coverUrl,
                    activity, false, getProfile.infos[5], null, null, 0, null, null);
            mAuthTask.execute((Void) null);
        }
        SplashActivity.drawerName = getProfile.infos[0];
        InfoGetter.coverUrl = coverUrl;
        InfoGetter.profileUrl = personPic;
        SplashActivity.drawerEmail = getProfile.infos[9];
        SplashActivity.coverUrl = coverUrl;
        SplashActivity.picUrl = profileUrl;
        Intent i = new Intent(activity, MainKatego.class);
        activity.startActivity(i);
        activity.finish();
    }

    private void getFacebook(String idStr) {
        String personName = getProfile.infos[0];
        String personPhotoUrl = "https://graph.facebook.com/" + idStr + "/picture?type=large";
        coverUrl = getProfile.infos[4];
        if (personName != getProfile.infos[0] || personPhotoUrl != getProfile.infos[1]||
                coverUrl != getProfile.infos[4]){
            PassData mAuthTask = new PassData(idStr, personName,
                    2, personPhotoUrl, coverUrl,
                    activity, false, getProfile.infos[5], null, null, 0, null, null);
            mAuthTask.execute((Void) null);
        }
        SplashActivity.drawerName = personName;
        SplashActivity.coverUrl = coverUrl;
        SplashActivity.picUrl = personPhotoUrl;
        InfoGetter.profileUrl = personPhotoUrl;
        SplashActivity.drawerEmail = getProfile.infos[9];
        Intent i = new Intent(activity, MainKatego.class);
        activity.startActivity(i);
        activity.finish();
    }

    private void getTwitter() {
        session = Twitter.getSessionManager().getActiveSession();
        new MyTwitterApiClient(session).getUsersService().show(session.getId(), null, true,
                new Callback<User>() {
                    @Override
                    public void success(Result<User> result) {
                        String email = result.data.idStr;
                        InfoGetter.email = email;
                        String profilePic = result.data.profileImageUrlHttps;
                        profilePic = profilePic.replace("_normal", "");
                        Log.d("twitter", "user's profile is "
                                + profilePic
                                + "~" + result.data.profileBannerUrl
                                + "~" + result.data.name
                                + "~" + result.data.idStr);
                        String coverUrl = result.data.profileBannerUrl;
                        String name = getProfile.infos[0];
                        if (profilePic != getProfile.infos[1]||
                                coverUrl != getProfile.infos[4]){
                            PassData mAuthTask = new PassData(email, name,
                                    2, profilePic, coverUrl,
                                    activity, false, getProfile.infos[5], null, null, 0, null, null);
                            mAuthTask.execute((Void) null);
                        }
                        SplashActivity.drawerName = name.replace("+", " ");
                        SplashActivity.drawerEmail = getProfile.infos[9];
                        InfoGetter.coverUrl = coverUrl;
                        InfoGetter.profileUrl = profilePic;
                        SplashActivity.coverUrl = coverUrl;
                        SplashActivity.picUrl = profilePic;
                        Intent i = new Intent(activity, MainKatego.class);
                        activity.startActivity(i);
                        activity.finish();
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
