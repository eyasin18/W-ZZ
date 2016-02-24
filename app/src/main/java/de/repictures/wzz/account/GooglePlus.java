package de.repictures.wzz.account;

import android.app.Activity;
import android.util.Log;

import com.google.android.gms.plus.model.people.Person;

import de.repictures.wzz.AsyncTasks.PassData;
import de.repictures.wzz.R;

public class GooglePlus {

    private static final String TAG = "GooglePlus";

    public GooglePlus(Activity activity, Person person) {
        getProfileInformation(activity, person);
    }

    private void getProfileInformation(Activity activity, Person person) {
        Log.d(TAG, "getProfileInformation: getProfileInformation executed");
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
        String personName = person.getDisplayName();
        String profilePic = person.getImage().getUrl().substring(0, personName.length() - 2) + 500;
        PassData mAuthTask = new PassData(person.getId(), personName,
                3, profilePic, coverUrl, activity, true, null);
        mAuthTask.execute((Void) null);
    }
}