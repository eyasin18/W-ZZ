package de.repictures.wzz.fragments.profile;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import de.repictures.wzz.R;
import de.repictures.wzz.fragments.dummy.DummyContent.DummyItem;

public class MyProfileFollowingAdapter extends RecyclerView.Adapter<MyProfileFollowingAdapter.ViewHolder> {

    private final List<DummyItem> mValues;

    public MyProfileFollowingAdapter(List<DummyItem> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_following_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View view) {
            super(view);
        }
    }
}
