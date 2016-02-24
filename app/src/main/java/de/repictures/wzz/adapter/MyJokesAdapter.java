package de.repictures.wzz.adapter;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.repictures.wzz.DetailActivity;
import de.repictures.wzz.MainJokes;
import de.repictures.wzz.R;
import de.repictures.wzz.fragments.MenuDialog;
import de.repictures.wzz.uiHelper.JokesClickListener;
import de.repictures.wzz.uiHelper.getPictures;

public class MyJokesAdapter extends RecyclerView.Adapter<MyJokesViewHolder>{
    private static final String TAG = "MyJokesAdapter";
    Activity activity;
    List<String> strings, inhaltText = new ArrayList<>(), userKeys = new ArrayList<>(),
            picLinks = new ArrayList<>(), deviseText = new ArrayList<>(), votes = new ArrayList<>(),
            reports = new ArrayList<>(), userNames = new ArrayList<>(), votingKeys = new ArrayList<>();
    private List<String> keyText = new ArrayList<>();
    List<Boolean> voted = new ArrayList<>(), reported = new ArrayList<>();
    public static final int TYPE_HEADER = 0;
    final CoordinatorLayout fabLayout = MainJokes.coordinatorLayoutView;
    public static final int TYPE_ITEM = 1;

    public MyJokesAdapter(String[] witzEinzeln, Activity activity) {
        this.strings = Arrays.asList(witzEinzeln);
        this.activity = activity;
    }

    @Override
    public MyJokesViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.my_jokes_card, viewGroup, false);
            return new MyJokesViewHolder(v,viewType);
        } else if (viewType == TYPE_HEADER) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.jokeslistfooter, viewGroup, false);
            return new MyJokesViewHolder(v,viewType);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final MyJokesViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: " + strings);
        if (holder.holderId == 1){
            for (int c = 0; c < strings.size(); c += 11) {
                userNames.add(strings.get(c));
            }
            for (int i = 1; i < strings.size(); i += 11) {
                inhaltText.add(strings.get(i).replace("<br />", "\n"));
            }
            for (int c = 2; c < strings.size(); c += 11) {
                votes.add(strings.get(c));
            }
            for (int c = 3; c < strings.size(); c += 11) {
                reports.add(strings.get(c));
            }
            for (int c = 4; c < strings.size(); c += 11) {
                keyText.add(strings.get(c));
            }
            for (int c = 5; c < strings.size(); c += 11){
                if (strings.get(c).contains("500")){
                    String picLink = strings.get(c).substring(0, strings.get(c).length() - 3);
                    picLink = picLink + "250";
                    picLinks.add(picLink);
                } else picLinks.add(strings.get(c));
            }
            for (int c = 6; c < strings.size(); c += 11) {
                deviseText.add(strings.get(c));
            }
            for (int c = 7; c < strings.size(); c += 11) {
                voted.add(Boolean.parseBoolean(strings.get(position)));
            }
            for (int c = 8; c < strings.size(); c += 11) {
                reported.add(Boolean.parseBoolean(strings.get(c)));
            }
            for (int c = 9; c < strings.size(); c += 11) {
                votingKeys.add(strings.get(c));
            }
            for (int c = 10; c < strings.size(); c += 11) {
                userKeys.add(strings.get(c));
            }
            holder.inhalt.setText(inhaltText.get(position));
            holder.voteCounter.setText(votes.get(position));
            new Thread(new getPictures(picLinks.get(position), holder.profilePic, null, activity, true, true, false)).start();
            holder.setClickListener(new MyJokesViewHolder.ClickListener() {
                @Override
                public void onClick(View v, int position, boolean isLongClick) {
                    if (v instanceof TextView && v.getId() == R.id.ad_my_jokeslist_placeholder) {
                        String detailExtra = keyText.get(position)
                                + "~" + picLinks.get(position)
                                + "~" + userNames.get(position)
                                + "~" + inhaltText.get(position)
                                + "~" + deviseText.get(position)
                                + "~" + votes.get(position)
                                + "~" + reports.get(position)
                                + "~" + votingKeys.get(position)
                                + "~" + userKeys.get(position);
                        Log.d(TAG, "onClick " + detailExtra);
                        Intent i = new Intent(activity, DetailActivity.class);
                        i.putExtra("DetailExtra", detailExtra);
                        i.putExtra("VoterAvailable", false);
                        activity.startActivity(i);
                    }
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position)) {
            return TYPE_HEADER;
        } else {
            return TYPE_ITEM;
        }
    }

    private boolean isPositionHeader(int position) {
        return false;
    } //position == (strings.size()/6)

    @Override
    public int getItemCount() {
        return strings.size()/11+1;
    }
}
