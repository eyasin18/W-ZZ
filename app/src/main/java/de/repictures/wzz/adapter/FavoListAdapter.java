package de.repictures.wzz.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.repictures.wzz.R;
import de.repictures.wzz.uiHelper.ColorImage;

public class FavoListAdapter extends RecyclerView.Adapter<FavoViewHolder>  {
    List<String> rawJokes = new ArrayList<>(), inhaltText = new ArrayList<>(),
            userText = new ArrayList<>(), profileLinks = new ArrayList<>(),
            keyText = new ArrayList<>();
    List<Boolean> isAlreadyFavoed = new ArrayList<>();
    Activity activity;

    public FavoListAdapter (String s, Activity activity){
        this.rawJokes = Arrays.asList(s.split("~"));
        this.activity = activity;
    }

    @Override
    public FavoViewHolder onCreateViewHolder(ViewGroup viewGroup,int i) {
        final View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.favolist, viewGroup, false);
        return new FavoViewHolder(v);
    }

    public void onBindViewHolder(FavoViewHolder holder, int position) {
        for (int i = 0; i < rawJokes.size(); i += 3) {
            inhaltText.add(rawJokes.get(i));
        }
        for (int c = 1; c < rawJokes.size(); c += 3) {
            profileLinks.add(rawJokes.get(c));
        }
        for (int c = 2; c < rawJokes.size(); c += 3){
            keyText.add(rawJokes.get(c));
            isAlreadyFavoed.add(true);
        }
        holder.inhalt.setText(inhaltText.get(position));
        Picasso.with(activity).load(profileLinks.get(position)).into(holder.profilePic);
        new ColorImage(R.drawable.ic_heart_white_18dp, R.color.heart_red, holder.favorite, activity);
        holder.setClickListener(new FavoViewHolder.ClickListener() {
            @Override
            public void onClick(View v, int position, boolean isLongClick) {
                onClickListener(v, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return rawJokes.size()/2;
    }

    private void onClickListener(View v, int position) {
        FavoViewHolder holder = (FavoViewHolder) v.getTag();
        if (v instanceof ImageView && v.getId() == R.id.favolist_favor) {
            if (!isAlreadyFavoed.get(position)) {
                isAlreadyFavoed.set(position, true);
                SharedPreferences pref = activity.getSharedPreferences("Favoriten", 0);
                SharedPreferences.Editor editor = pref.edit();
                Set<String> set = pref.getStringSet("favoed", new HashSet<String>());
                set.add(inhaltText.get(position) + "~"
                        + profileLinks.get(position) + "~"
                        + keyText.get(position) + "~");
                editor.putStringSet("favoed", set);
                Set<String> favoedKeys = pref.getStringSet("favoedKeys", new HashSet<String>());
                favoedKeys.add(keyText.get(position));
                editor.putStringSet("favoedKeys", favoedKeys);
                editor.apply();
                new ColorImage(R.drawable.ic_heart_white_18dp, R.color.heart_red, holder.favorite, activity);
            } else {
                isAlreadyFavoed.set(position, false);
                SharedPreferences pref = activity.getSharedPreferences("Favoriten", 0);
                SharedPreferences.Editor editor = pref.edit();
                Set<String> set = pref.getStringSet("favoed", new HashSet<String>());
                set.remove(inhaltText.get(position) + "~"
                        + profileLinks.get(position) + "~"
                        + keyText.get(position) + "~");
                editor.putStringSet("favoed", set);
                Set<String> favoedKeys = pref.getStringSet("favoedKeys", new HashSet<String>());
                favoedKeys.remove(keyText.get(position));
                editor.putStringSet("favoedKeys", favoedKeys);
                editor.apply();
                holder.favorite.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_heart_grey600_18dp));
            }
        }
    }

}
