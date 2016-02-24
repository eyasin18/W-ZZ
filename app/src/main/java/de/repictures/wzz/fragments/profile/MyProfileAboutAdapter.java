package de.repictures.wzz.fragments.profile;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.repictures.wzz.R;
import de.repictures.wzz.fragments.dummy.DummyContent.DummyItem;

import java.util.List;

public class MyProfileAboutAdapter extends RecyclerView.Adapter<MyProfileAboutAdapter.ViewHolder> {

    private final List<DummyItem> mValues;

    public MyProfileAboutAdapter(List<DummyItem> items) {
        mValues = items;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_my_profileabout_list, parent, false);
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
