package de.repictures.wzz.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import de.repictures.wzz.R;

public class MyJokesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
    public TextView inhalt, voteCounter;
    public ImageView profilePic, thumbUp, favorite;
    public ProgressBar progressBar;
    private ClickListener clickListener;
    int holderId;

    public MyJokesViewHolder(View v, int viewType){
        super(v);

        if (viewType == MyJokesAdapter.TYPE_ITEM){
            inhalt = (TextView) v.findViewById(R.id.ad_my_jokeslist_placeholder);
            voteCounter = (TextView) v.findViewById(R.id.my_jokeslist_voteCounter);
            profilePic = (ImageView)v.findViewById(R.id.my_jokeslist_profile_pic);
            inhalt.setOnClickListener(this);
            inhalt.setOnLongClickListener(this);
            inhalt.setClickable(true);
            inhalt.setTag(this);
            holderId = 1;
        } else {
            progressBar = (ProgressBar) v.findViewById(R.id.progressBarJokesList);
            holderId = 0;
        }
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

