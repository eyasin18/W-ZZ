package de.repictures.wzz.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import de.repictures.wzz.R;

public class JokesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
    public TextView inhalt, voteCounter, voteCounterAnimator, noJokes;
    public ImageView profilePic, thumbUp, favorite;
    public ProgressBar progressBar;
    public RelativeLayout layout;
    private ClickListener clickListener;

    public JokesViewHolder(View v, int viewType){
        super(v);
        inhalt = (TextView) v.findViewById(R.id.ad_jokeslist_placeholder);
        thumbUp = (ImageView) v.findViewById(R.id.jokeslist_thumb_up);
        voteCounter = (TextView) v.findViewById(R.id.jokeslist_voteCounter);
        voteCounterAnimator = (TextView) v.findViewById(R.id.jokeslist_voteCounterAnimator);
        profilePic = (ImageView)v.findViewById(R.id.jokeslist_profile_pic);
        favorite = (ImageView) v.findViewById(R.id.jokeslist_favor);
        inhalt.setOnClickListener(this);
        inhalt.setOnLongClickListener(this);
        inhalt.setClickable(true);
        inhalt.setTag(this);
        thumbUp.setOnClickListener(this);
        thumbUp.setOnLongClickListener(this);
        thumbUp.setClickable(true);
        thumbUp.setTag(this);
        favorite.setOnClickListener(this);
        favorite.setOnLongClickListener(this);
        favorite.setClickable(true);
        favorite.setTag(this);
        layout = (RelativeLayout) v.findViewById(R.id.jokeslist_layout);
        progressBar = (ProgressBar) v.findViewById(R.id.jokeslist_progressbar);
    }

    public interface ClickListener {

        /**
         * Called when the view is clicked.
         *
         * @param v view that is clicked
         * @param position of the clicked item
         * @param isLongClick true if long click, false otherwise
         */
        void onClick(View v, int position, boolean isLongClick);

    }

    /* Setter for listener. */
    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public void onClick(View v) {

        // If not long clicked, pass last variable as false.
        clickListener.onClick(v, getLayoutPosition(), false);
    }

    @Override
    public boolean onLongClick(View v) {

        // If long clicked, passed last variable as true.
        clickListener.onClick(v, getLayoutPosition(), true);
        return true;
    }

}
