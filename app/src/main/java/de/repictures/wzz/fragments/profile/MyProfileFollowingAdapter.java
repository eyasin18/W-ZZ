package de.repictures.wzz.fragments.profile;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import de.repictures.wzz.ProfileActivity;
import de.repictures.wzz.R;
import de.repictures.wzz.uiHelper.getPictures;

public class MyProfileFollowingAdapter extends RecyclerView.Adapter<MyProfileFollowingAdapter.ViewHolder> implements View.OnClickListener {

    private final String[] abos;
    private Activity activity;

    public MyProfileFollowingAdapter(String[] abos, Activity activity) {
        this.abos = abos;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_following, parent, false);
        ViewHolder vhItem = new ViewHolder(view);
        vhItem.name.setOnClickListener(this);
        vhItem.name.setTag(vhItem);
        return vhItem;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        String[] infos = abos[position].split("~");
        holder.name.setText(infos[1]);
        new Thread(new getPictures(infos[2], holder.pic, null, activity, true, true, false)).start();
    }

    @Override
    public int getItemCount() {
        return abos.length;
    }

    @Override
    public void onClick(View v) {
        if (v instanceof TextView && v.getId() == R.id.following_name){
            ViewHolder holder = (ViewHolder) v.getTag();
            int position = holder.getLayoutPosition();
            String[] infos = abos[position].split("~");
            Intent i = new Intent(activity, ProfileActivity.class);
            i.putExtra("userUrl", infos[0]);
            i.putExtra("userName", infos[1]);
            activity.startActivity(i);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView pic;

        public ViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.following_name);
            pic = (ImageView) view.findViewById(R.id.following_image);
        }
    }
}
