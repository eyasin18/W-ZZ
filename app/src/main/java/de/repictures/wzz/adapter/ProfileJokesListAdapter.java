package de.repictures.wzz.adapter;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.repictures.wzz.DetailActivity;
import de.repictures.wzz.MainJokes;
import de.repictures.wzz.ProfileActivity;
import de.repictures.wzz.R;
import de.repictures.wzz.fragments.MenuDialog;
import de.repictures.wzz.internet.VoteInternet;
import de.repictures.wzz.uiHelper.ColorImage;
import de.repictures.wzz.uiHelper.JokesClickListener;
import de.repictures.wzz.uiHelper.updateNumber;

public class ProfileJokesListAdapter extends RecyclerView.Adapter<ProfileJokesViewHolder> {
    private static final String TAG = "ProfileAdapter";
    private final Activity activity;
    private final String[] jokes;
    private String[] profile;
    private Boolean time = true;
    Handler handler = new Handler();
    private ArrayList<String> inhalt = new ArrayList<>(), votes = new ArrayList<>(), reports = new ArrayList<>(),
                                key = new ArrayList<>(), votingKey = new ArrayList<>();
    private ArrayList<Boolean> voted = new ArrayList<>(), reported = new ArrayList<>(), favoed = new ArrayList<>();

    public ProfileJokesListAdapter(Activity activity, String[] jokes, String[] profile) {
        this.activity = activity;
        this.jokes = jokes;
        this.profile = profile;
        Log.i(TAG, "ProfileJokesListAdapter: " + Arrays.toString(jokes));
        for (int i = 0; i < jokes.length; i += 7){
            inhalt.add(jokes[i]);
            votes.add(jokes[i+1]);
            reports.add(jokes[i+2]);
            key.add(jokes[i+3]);
            voted.add(Boolean.parseBoolean(jokes[i+4]));
            reported.add(Boolean.parseBoolean(jokes[i+5]));
            votingKey.add(jokes[i+6]);
            //Log.d(TAG, "ProfileJokesListAdapter: " + votingKey.get(i-1));
            favoed.add(false);
        }
    }

    @Override
    public ProfileJokesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_jokeslist, parent, false);
        return new ProfileJokesViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ProfileJokesViewHolder holder, int position) {
        holder.inhalt.setText(inhalt.get(position));
        holder.voteCounter.setText(votes.get(position));
        if (liesFavoKeysName().contains(key.get(position))){
            favoed.set(position, true);
            new ColorImage(R.drawable.ic_heart_white_18dp, R.color.heart_red, holder.favorite, activity);
        } else {
            holder.favorite.setImageResource(R.drawable.ic_heart_grey600_18dp);
        }
        if (voted.get(position)) {
            new ColorImage(R.drawable.ic_thumb_up_white_18dp, R.color.colorPrimary, holder.thumbUp, activity);
        } else {
            holder.thumbUp.setImageResource(R.drawable.ic_thumb_up_grey600_18dp);
        }
        holder.setClickListener(new ProfileJokesViewHolder.ClickListener() {
            @Override
            public void onClick(View v, int position, boolean isLongClick) {
                clickListener(v, position, isLongClick);
            }
        });
    }

    @Override
    public int getItemCount() {
        return jokes.length/7;
    }

    private Set liesFavoKeysName(){
        SharedPreferences pref = activity.getSharedPreferences("Favoriten", 0);
        return pref.getStringSet("favoedKeys", new HashSet<String>());
    }

    private void clickListener(View v, int position, boolean isLongClick) {
        if (v instanceof TextView && v.getId() == R.id.ad_jokeslist_placeholder && !isLongClick) {
            Log.d(TAG, "clickListener: " + position);
            String detailExtra = key.get(position)
                    + "~" + profile[1]
                    + "~" + profile[0]
                    + "~" + inhalt.get(position)
                    + "~" + profile[5]
                    + "~" + votes.get(position)
                    + "~" + reports.get(position)
                    + "~" + votingKey.get(position)
                    + "~" + profile[6];
            Log.d(TAG, "onClick " + detailExtra);
            Intent i = new Intent(activity, DetailActivity.class);
            i.putExtra("DetailExtra", detailExtra);
            i.putExtra("VoterAvailable", false);
            activity.startActivity(i);
        } else if (v instanceof TextView && v.getId() == R.id.ad_jokeslist_placeholder && isLongClick) {
            String detailExtra = key.get(position)
                    + "~" + profile[1]
                    + "~" + profile[0]
                    + "~" + inhalt.get(position)
                    + "~" + profile[5]
                    + "~" + votes.get(position)
                    + "~" + reports.get(position)
                    + "~" + votingKey.get(position);
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
        }else if (v instanceof ImageView && v.getId() == R.id.jokeslist_thumb_up && time) {
            time = false;
            handler.postDelayed(new Runnable() {
                public void run() {
                    time = true;
                }
            }, 300);
            if (!voted.get(position)) {
                android.util.Log.d(TAG, "isAlreadyVoted = false");
                ProfileJokesViewHolder holder = (ProfileJokesViewHolder) v.getTag();
                voteInternet(true, position, holder, activity, votingKey);
                voted.set(position, true);
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
                new updateNumber(holder.voteCounter, holder.voteCounterAnimator, votesNeu, myNum);
                holder.thumbUp.setImageDrawable(thumbUp_cyan);
            } else {
                android.util.Log.d(TAG, "isAlreadyVoted = true");
                ProfileJokesViewHolder holder = (ProfileJokesViewHolder) v.getTag();
                voteInternet(false, position, holder, activity, votingKey);
                voted.set(position, false);
                int votesNeu = 0;
                int myNum = 0;
                try {
                    myNum = Integer.parseInt((String) holder.voteCounter.getText());
                    votesNeu = myNum - 1;
                    //VotingKeys.set(position, String.valueOf(votesNeu));
                } catch (NumberFormatException nfe) {
                    System.out.println("Could not parse " + nfe);
                }
                new updateNumber(holder.voteCounter, holder.voteCounterAnimator, votesNeu, myNum);
                holder.thumbUp.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_thumb_up_grey600_18dp));
            }
        } else if (v instanceof ImageView && v.getId() == R.id.jokeslist_favor) {
            ProfileJokesViewHolder holder = (ProfileJokesViewHolder) v.getTag();
            if (!favoed.get(position)) {
                favoed.set(position, true);
                SharedPreferences pref = activity.getSharedPreferences("Favoriten", 0);
                SharedPreferences.Editor editor = pref.edit();
                Set<String> set = pref.getStringSet("favoed", new HashSet<String>());
                set.add(inhalt.get(position) + "~"
                        + profile[1] + "~"
                        + key.get(position) + "~");
                editor.putStringSet("favoed", set);
                Set<String> favoedKeys = pref.getStringSet("favoedKeys", new HashSet<String>());
                favoedKeys.add(key.get(position));
                editor.putStringSet("favoedKeys", favoedKeys);
                editor.apply();
                new ColorImage(R.drawable.ic_heart_white_18dp, R.color.heart_red, holder.favorite, activity);
                Snackbar
                        .make(ProfileActivity.cl, R.string.favored, Snackbar.LENGTH_SHORT)
                        .show();
            } else {
                favoed.set(position, false);
                SharedPreferences pref = activity.getSharedPreferences("Favoriten", 0);
                SharedPreferences.Editor editor = pref.edit();
                Set<String> set = pref.getStringSet("favoed", new HashSet<String>());
                set.remove(inhalt.get(position) + "~"
                        + profile[1] + "~"
                        + key.get(position) + "~");
                editor.putStringSet("favoed", set);
                Set<String> favoedKeys = pref.getStringSet("favoedKeys", new HashSet<String>());
                favoedKeys.remove(key.get(position));
                editor.putStringSet("favoedKeys", favoedKeys);
                editor.apply();
                holder.favorite.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_heart_grey600_18dp));
                Snackbar
                        .make(ProfileActivity.cl, R.string.defavored, Snackbar.LENGTH_SHORT)
                        .show();
            }

        }
    }

    private void voteInternet(Boolean votedUp, int position, ProfileJokesViewHolder holder,
                              Activity activity, List<String> keyText) {
        String keyRaw = keyText.get(position);
        Thread t = new Thread(new VoteInternet(keyRaw, votedUp, true, activity, MainJokes.katego, ProfileActivity.cl));
        t.start();
    }
}
