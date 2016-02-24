package de.repictures.wzz.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import de.repictures.wzz.R;

public class DetailViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

    public TextView devise;
    public ImageView profilepic;
    public TextView joketext;
    private ClickListener clickListener;
    public TextView username, inhalt, voteCounter, noComments, voteCounterAnimator;
    public ImageView profilePic, thumbUp, answer;
    public ProgressBar loadComments;
    public int holderId;
    ImageView detailReport, detailLike;
    TextView detailNumberReport, detailNumberLike;

    public DetailViewHolder(View v, int viewType) {
        super(v);
        if (viewType == DetailListAdapter.TYPE_ITEM) {
            v.setOnClickListener(this);
            v.setOnLongClickListener(this);
            v.setClickable(true);
            v.setTag(this);
            username = (TextView) v.findViewById(R.id.detaillist_username);
            inhalt = (TextView) v.findViewById(R.id.detaillist_inhalt);
            voteCounter = (TextView) v.findViewById(R.id.detaillist_voteCounter);
            voteCounterAnimator = (TextView) v.findViewById(R.id.detaillist_voteCounterAnimator);
            profilePic = (ImageView) v.findViewById(R.id.detaillist_profile_pic);
            thumbUp = (ImageView) v.findViewById(R.id.detaillist_thumb_up);
            answer = (ImageView) v.findViewById(R.id.detaillist_answer);
            holderId = 1;
        } else if (viewType == DetailListAdapter.TYPE_HEADER){
            joketext = (TextView) v.findViewById(R.id.detail_joke);
            username = (TextView) v.findViewById(R.id.detail_username);
            devise = (TextView) v.findViewById(R.id.detail_devise);
            profilepic = (ImageView) v.findViewById(R.id.detail_profile_pic);
            detailReport = (ImageView) v.findViewById(R.id.detail_report);
            detailLike = (ImageView) v.findViewById(R.id.detail_thumb_up);
            detailNumberLike = (TextView) v.findViewById(R.id.detail_number_like);
            detailNumberReport = (TextView) v.findViewById(R.id.detail_number_report);
            loadComments = (ProgressBar) v.findViewById(R.id.load_comments);
            noComments = (TextView) v.findViewById(R.id.no_comments);
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

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public void onClick(View v) {
        clickListener.onClick(v, getLayoutPosition(), false);
    }

    @Override
    public boolean onLongClick(View v) {
        clickListener.onClick(v, getLayoutPosition(), true);
        return false;
    }
}
