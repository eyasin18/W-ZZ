package de.repictures.wzz.adapter;

import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import de.repictures.wzz.DetailActivity;
import de.repictures.wzz.MainJokes;
import de.repictures.wzz.MainKatego;
import de.repictures.wzz.R;

public class ReKaAdapter extends RecyclerView.Adapter<ReKaAdapter.ViewHolder> implements View.OnClickListener {

    private static final String TAG = "ReKaAdapter";
    private String[] mClasses;
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_ITEM = 1;
    private Activity activity;
    private TypedArray imgs;

    public ReKaAdapter(String[] classes, Activity activity) {
        this.mClasses = classes;
        this.activity = activity;
        imgs = activity.getResources().obtainTypedArray(R.array.icons);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == TYPE_ITEM) {
            final View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rekalist, viewGroup, false);
            ViewHolder vhItem = new ViewHolder(v,viewType);
            vhItem.titles.setOnClickListener(this);
            vhItem.titles.setTag(vhItem);
            return vhItem;
        } else if (viewType == TYPE_HEADER) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rekalistheader, viewGroup, false);
            return new ViewHolder(v, viewType);
        }
        return null;
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        if (holder.holderid == 1){
            holder.titles.setText(mClasses[position - 1]);
            holder.cicles.setImageResource(imgs.getResourceId(position - 1, -1));
        }
    }

    @Override
    public void onClick(View v) {
        if (v instanceof TextView && v.getId() == R.id.rekatext || v != null && v.getId() == R.id.rekacicle) {
            ViewHolder holder = (ViewHolder) v.getTag();
            int currentPosition = holder.getLayoutPosition() - 1;
            Intent intent = new Intent(activity, MainJokes.class);
            intent.putExtra("ActivityValue", currentPosition);
            activity.startActivity(intent);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView titles;
        public ImageView cicles;
        int holderid;

        public ViewHolder(View v, int viewType) {
            super(v);
            if (viewType == ReKaAdapter.TYPE_ITEM){
                v.setClickable(true);
                v.setTag(this);
                titles = (TextView) v.findViewById(R.id.rekatext);
                cicles = (ImageView) v.findViewById(R.id.rekacicle);
                holderid = 1;
            } else {
                holderid = 0;
            }
        }
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
    public int getItemCount() {
        return mClasses.length + 1;
    }

}
