package de.repictures.wzz.adapter;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import de.repictures.wzz.ProfileActivity;
import de.repictures.wzz.R;
import de.repictures.wzz.internet.VoteComment;
import de.repictures.wzz.uiHelper.getPictures;
import de.repictures.wzz.uiHelper.updateNumber;

public class DetailListAdapter extends RecyclerView.Adapter<DetailViewHolder> implements View.OnClickListener {

    public static final int TYPE_HEADER = 0;
    public static final int TYPE_ITEM = 1;
    private static final String TAG = "DetailListAdapter";
    String[] witz_split;
    Activity activity;
    private final ArrayList<Boolean> voted;
    private final ArrayList<String> commentKeys;
    private final Boolean footer;
    private final Boolean voter;
    private final CoordinatorLayout coordinatorLayout;
    private List<String> names;
    private List<String> photos;
    public static Boolean time = true;
    Handler handler = new Handler();
    List<String> comments = new ArrayList<>(), votes = new ArrayList<>();

    public DetailListAdapter(Activity activity, String witz_raw, List<String> comments,
                             List<String> votes, List<String> names, List<String> photos,
                             ArrayList<Boolean> voted,
                             ArrayList<String> commentKeys, Boolean footer, Boolean voter,
                             CoordinatorLayout coordinatorLayout) {
        this.activity = activity;
        this.voted = voted;
        this.commentKeys = commentKeys;
        this.footer = footer;
        this.voter = voter;
        this.coordinatorLayout = coordinatorLayout;
        if (names != null) this.names = names;
        if (photos != null) this.photos = photos;
        if (comments != null) this.comments = comments;
        Log.d(TAG, "DetailListAdapter " + witz_raw);
        witz_split = witz_raw.split("~");
        this.votes = votes;
    }

    @Override
    public DetailViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == TYPE_ITEM && comments.size() != 0 && footer == null) {
            final View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.detaillist, viewGroup, false);
            DetailViewHolder vhItem = new DetailViewHolder(v, viewType);
            vhItem.inhalt.setOnClickListener(this);
            vhItem.inhalt.setTag(vhItem);
            vhItem.thumbUp.setOnClickListener(this);
            vhItem.thumbUp.setTag(vhItem);
            vhItem.answer.setOnClickListener(this);
            vhItem.answer.setTag(vhItem);
            return vhItem;
        } else if (viewType == TYPE_HEADER) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.detailheader, viewGroup, false);
            return new DetailViewHolder(v, viewType);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final DetailViewHolder holder, final int position) {
        if (holder.holderId == 0){
            holder.username.setText(witz_split[2]);
            holder.joketext.setText(witz_split[3]);
            holder.devise.setText(witz_split[4]);
            holder.detailReport.setImageDrawable(colorImage(R.drawable.ic_flag_variant_white_18dp, R.color.report_red));
            holder.detailLike.setImageDrawable(colorImage(R.drawable.ic_thumb_up_white_18dp, R.color.like_blue));
            holder.detailNumberLike.setText(witz_split[5]);
            holder.detailNumberReport.setText(witz_split[6]);
            new Thread(new getPictures(witz_split[1], holder.profilepic, null, activity, true, true, false)).start();
            holder.profilepic.setOnClickListener(this);
            if (footer == null) holder.loadComments.setVisibility(View.GONE);
            else if (footer) {
                holder.loadComments.setVisibility(View.GONE);
                holder.noComments.setVisibility(View.VISIBLE);
            } else {
                holder.loadComments.setVisibility(View.VISIBLE);
                holder.noComments.setVisibility(View.GONE);
            }
        } else if (holder.holderId == 1 && comments.size() != 0){
            holder.inhalt.setText(comments.get(position - 1));
            holder.username.setText(names.get(position - 1));
            new Thread(new getPictures(photos.get(position - 1), holder.profilePic, null, activity, true, true, false)).start();
            holder.voteCounter.setText(votes.get(position - 1));
            if (voted.get(position - 1)){
                holder.thumbUp.setImageDrawable(colorImage(R.drawable.ic_thumb_up_white_18dp, R.color.like_blue));
            } else {
                holder.thumbUp.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_thumb_up_grey600_18dp));
            }
        }
    }

    @Override
    public int getItemCount() {
        return comments.size() + 1;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position)) {
            return TYPE_HEADER;
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    public void onClick(View v) {
        if (v instanceof ImageView && v.getId() == R.id.detail_profile_pic && voter) {
            Intent i = new Intent(activity, ProfileActivity.class);
            i.putExtra("userUrl", witz_split[8]);
            i.putExtra("userName", witz_split[2]);

            activity.startActivity(i);
        } else if (v instanceof ImageView && v.getId() == R.id.detaillist_thumb_up){
            DetailViewHolder holder = (DetailViewHolder) v.getTag();
            int position = holder.getLayoutPosition() - 1;
            time = false;
            handler.postDelayed(new Runnable() {
                public void run() {
                    time = true;
                }
            }, 300);
            if (!voted.get(position)) {
                android.util.Log.d(TAG, "isAlreadyVoted = false");
                new VoteComment(commentKeys.get(position), liesKey(activity), true, coordinatorLayout);
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
                new VoteComment(commentKeys.get(position), liesKey(activity), false, coordinatorLayout);
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
        }
    }


    public Drawable colorImage(int viewId, int colorId){
        Drawable view = activity.getResources().getDrawable(viewId);
        int cyan = activity.getResources().getColor(colorId);
        ColorFilter filter = new PorterDuffColorFilter(cyan, PorterDuff.Mode.MULTIPLY);
        if (view != null) {
            view.setColorFilter(filter);
        }
        return view;
    }

    private String liesKey(Activity activity){
        SharedPreferences pref = activity.getSharedPreferences("Account", 0);
        return pref.getString("Key", "");
    }

}
