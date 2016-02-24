package de.repictures.wzz.adapter;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.repictures.wzz.MainJokes;
import de.repictures.wzz.R;
import de.repictures.wzz.uiHelper.JokesClickListener;
import de.repictures.wzz.uiHelper.getPictures;

public class JokesAdapter extends RecyclerView.Adapter<JokesViewHolder>{
    private static final String TAG = "JokesAdapter";
    private final boolean end;
    Activity activity;
    List<String> strings;
    final CoordinatorLayout fabLayout;
    private List<Boolean> isAlreadyFavoed = new ArrayList<>(), isAlreadyVoted = new ArrayList<>();

    public JokesAdapter(List<String> witzSplit, boolean end, CoordinatorLayout coordinatorLayoutView, Activity activity) {
        this.fabLayout = coordinatorLayoutView;
        this.end = end;
        this.strings = witzSplit;
        Log.d(TAG, "strings size is: " + strings.size());
        this.activity = activity;
    }

    @Override
    public JokesViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.jokeslist, viewGroup, false);
        return new JokesViewHolder(v,viewType);
    }

    @Override
    public void onBindViewHolder(final JokesViewHolder holder, final int position) {
        isAlreadyVoted.add(false);
        isAlreadyFavoed.add(false);
        final String[] infos = strings.get(position).split("~");
        Log.d(TAG, "Position: " + position + " Inhalt: " + Arrays.toString(infos));
        if (infos.length == 12){
            holder.progressBar.setVisibility(View.GONE);
            holder.layout.setVisibility(View.VISIBLE);
            holder.inhalt.setText(infos[2]);
            holder.voteCounter.setText(infos[3]);
            Boolean favoed = liesFavoKeysName().contains(infos[5]);
            if (favoed) {
                Drawable heart_red = activity.getResources().getDrawable(R.drawable.ic_heart_white_18dp);
                int red = activity.getResources().getColor(R.color.heart_red);
                ColorFilter filter = new PorterDuffColorFilter(red, PorterDuff.Mode.MULTIPLY);
                if (heart_red != null) {
                    heart_red.setColorFilter(filter);
                }
                holder.favorite.setImageDrawable(heart_red);
                isAlreadyFavoed.set(position, true);
            } else {
                holder.favorite.setImageResource(R.drawable.ic_heart_grey600_18dp);
            }
            Boolean voted = Boolean.parseBoolean(infos[8]);
            if (voted) {
                Drawable thumbUp_cyan = activity.getResources().getDrawable(R.drawable.ic_thumb_up_white_18dp);
                int cyan = activity.getResources().getColor(R.color.colorPrimary);
                ColorFilter filter = new PorterDuffColorFilter(cyan, PorterDuff.Mode.MULTIPLY);
                if (thumbUp_cyan != null) {
                    thumbUp_cyan.setColorFilter(filter);
                }
                holder.thumbUp.setImageDrawable(thumbUp_cyan);
                isAlreadyVoted.set(position, true);
            } else {
                holder.thumbUp.setImageResource(R.drawable.ic_thumb_up_grey600_18dp);
            }
            Log.d(TAG, "Voted: " + voted + " Favored: " + favoed);
            new Thread(new getPictures(infos[6], holder.profilePic, null, activity, true, true, false)).start();
            holder.setClickListener(new JokesViewHolder.ClickListener() {
                @Override
                public void onClick(View v, int position, boolean isLongClick) {
                    new JokesClickListener(v, position, isLongClick, isAlreadyFavoed, infos[6], infos[1], infos[2],
                            infos[7], TAG, activity, isAlreadyVoted, infos[5], infos[10], fabLayout,
                            holder.thumbUp, holder.favorite, infos[3], infos[4], holder.voteCounter,
                            holder.voteCounterAnimator, infos[11]);
                }
            });
        } else if (Boolean.parseBoolean(infos[0])){
            holder.layout.setVisibility(View.GONE);
            holder.progressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return strings.size();
    }

    private Set liesFavoKeysName(){
        SharedPreferences pref = activity.getSharedPreferences("Favoriten", 0);
        return pref.getStringSet("favoedKeys", new HashSet<String>());
    }
}