package de.repictures.wzz.uiHelper;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.repictures.wzz.DetailActivity;
import de.repictures.wzz.MainJokes;
import de.repictures.wzz.R;
import de.repictures.wzz.adapter.JokesViewHolder;
import de.repictures.wzz.fragments.MenuDialog;
import de.repictures.wzz.internet.VoteInternet;

public class JokesClickListener {

    public static ImageView thumbUp, favorite;
    public static Boolean time = true;
    Handler handler = new Handler();

    public JokesClickListener(View v, int position, boolean isLongClick, List<Boolean> isAlreadyFavoed,
                              String picLink, String userText, String inhaltText,
                              String deviseText, String TAG, Activity activity, List<Boolean> isAlreadyVoted,
                              String keyText, String VotingKey, CoordinatorLayout fabLayout,
                              ImageView thumbUp, ImageView favorite, String votes, String reports,
                              TextView voteCounter, TextView voteCounterAnimator, String userKey) {


        JokesClickListener.thumbUp = thumbUp;
        JokesClickListener.favorite = favorite;

        if (v instanceof TextView && v.getId() == R.id.ad_jokeslist_placeholder && !isLongClick) {
            String detailExtra = keyText
                    + "~" + picLink
                    + "~" + userText
                    + "~" + inhaltText
                    + "~" + deviseText
                    + "~" + votes
                    + "~" + reports
                    + "~" + VotingKey
                    + "~" + userKey;
            Log.d(TAG, "onClick " + detailExtra);
            Intent i = new Intent(activity, DetailActivity.class);
            i.putExtra("DetailExtra", detailExtra);
            i.putExtra("VoterAvailable", true);
            activity.startActivity(i);
        } else if (v instanceof TextView && v.getId() == R.id.ad_jokeslist_placeholder && isLongClick) {
            String detailExtra = keyText
                    + "~" + picLink
                    + "~" + userText
                    + "~" + inhaltText
                    + "~" + deviseText
                    + "~" + votes
                    + "~" + reports
                    + "~" + VotingKey;
            DialogFragment newFragment = new MenuDialog();
            Bundle args = new Bundle();
            args.putString("detailExtra", detailExtra);
            newFragment.setArguments(args);
            FragmentManager fm = activity.getFragmentManager();
            fm.beginTransaction()
                    .show(newFragment)
                    .commit();
            newFragment.show(fm, "MenuFragment");
            Log.d("LongClicked", position + " was long clicked");
        } else if (v instanceof ImageView && v.getId() == R.id.jokeslist_thumb_up && time) {
            time = false;
            handler.postDelayed(new Runnable() {
                public void run() {
                    time = true;
                }
            }, 300);
            if (!isAlreadyVoted.get(position)) {
                android.util.Log.d(TAG, "isAlreadyVoted = false");
                JokesViewHolder holder = (JokesViewHolder) v.getTag();
                voteInternet(true, fabLayout, activity, VotingKey);
                isAlreadyVoted.set(position, true);
                int votesNeu = 0;
                int myNum = 0;
                try {
                    myNum = Integer.parseInt((String) holder.voteCounter.getText());
                    votesNeu = myNum + 1;
                    //VotingKeys.set(position, String.valueOf(votesNeu));
                } catch (NumberFormatException nfe) {
                    System.out.println("Could not parse " + nfe);
                }
                Drawable thumbUp_cyan = activity.getResources().getDrawable(R.drawable.ic_thumb_up_white_18dp);
                int cyan = activity.getResources().getColor(R.color.colorPrimary);
                ColorFilter filter = new PorterDuffColorFilter(cyan, PorterDuff.Mode.MULTIPLY);
                if (thumbUp_cyan != null) {
                    thumbUp_cyan.setColorFilter(filter);
                }
                new updateNumber(voteCounter, voteCounterAnimator, votesNeu, myNum);
                holder.thumbUp.setImageDrawable(thumbUp_cyan);
            } else {
                android.util.Log.d(TAG, "isAlreadyVoted = true");
                JokesViewHolder holder = (JokesViewHolder) v.getTag();
                voteInternet(false, fabLayout, activity, VotingKey);
                isAlreadyVoted.set(position, false);
                int votesNeu = 0;
                int myNum = 0;
                try {
                    myNum = Integer.parseInt((String) holder.voteCounter.getText());
                    votesNeu = myNum - 1;
                    //VotingKeys.set(position, String.valueOf(votesNeu));
                } catch (NumberFormatException nfe) {
                    System.out.println("Could not parse " + nfe);
                }
                new updateNumber(voteCounter, voteCounterAnimator, votesNeu, myNum);
                holder.thumbUp.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_thumb_up_grey600_18dp));
            }
        } else if (v instanceof ImageView && v.getId() == R.id.jokeslist_favor) {
            JokesViewHolder holder = (JokesViewHolder) v.getTag();
            if (!isAlreadyFavoed.get(position)) {
                isAlreadyFavoed.set(position, true);
                SharedPreferences pref = activity.getSharedPreferences("Favoriten", 0);
                SharedPreferences.Editor editor = pref.edit();
                Set<String> set = pref.getStringSet("favoed", new HashSet<String>());
                set.add(inhaltText + "~"
                        + picLink + "~"
                        + keyText + "~");
                editor.putStringSet("favoed", set);
                Set<String> favoedKeys = pref.getStringSet("favoedKeys", new HashSet<String>());
                favoedKeys.add(keyText);
                editor.putStringSet("favoedKeys", favoedKeys);
                editor.apply();
                new ColorImage(R.drawable.ic_heart_white_18dp, R.color.heart_red, holder.favorite, activity);
                Snackbar
                        .make(fabLayout, R.string.favored, Snackbar.LENGTH_SHORT)
                        .show();
            } else {
                isAlreadyFavoed.set(position, false);
                SharedPreferences pref = activity.getSharedPreferences("Favoriten", 0);
                SharedPreferences.Editor editor = pref.edit();
                Set<String> set = pref.getStringSet("favoed", new HashSet<String>());
                set.remove(inhaltText + "~"
                        + picLink + "~"
                        + keyText + "~");
                editor.putStringSet("favoed", set);
                Set<String> favoedKeys = pref.getStringSet("favoedKeys", new HashSet<String>());
                favoedKeys.remove(keyText);
                editor.putStringSet("favoedKeys", favoedKeys);
                editor.apply();
                holder.favorite.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_heart_grey600_18dp));
                Snackbar
                        .make(fabLayout, R.string.defavored, Snackbar.LENGTH_SHORT)
                        .show();
            }

        }
    }

    private void voteInternet(Boolean votedUp, CoordinatorLayout fablayout,
                              Activity activity, String key) {
        Thread t = new Thread(new VoteInternet(key, votedUp, true, activity, MainJokes.katego, fablayout));
        t.start();
    }
}
