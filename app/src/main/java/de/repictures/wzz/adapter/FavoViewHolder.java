package de.repictures.wzz.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import de.repictures.wzz.R;

public class FavoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

    public TextView inhalt;
    public ImageView profilePic;
    public ImageView favorite;
    private ClickListener clickListener;

    public FavoViewHolder(View v) {
        super(v);
        v.setClickable(true);
        v.setTag(this);
        inhalt = (TextView) v.findViewById(R.id.detaillist_inhalt);
        profilePic = (ImageView) v.findViewById(R.id.detaillist_profile_pic);
        favorite = (ImageView) v.findViewById(R.id.favolist_favor);
        favorite.setOnClickListener(this);
        favorite.setOnLongClickListener(this);
        favorite.setClickable(true);
        favorite.setTag(this);
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
