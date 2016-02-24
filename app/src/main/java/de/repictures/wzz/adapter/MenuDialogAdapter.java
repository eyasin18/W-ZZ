package de.repictures.wzz.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

import de.repictures.wzz.R;

public class MenuDialogAdapter extends RecyclerView.Adapter<MenuDialogAdapter.ViewHolder> {

    Activity activity;
    List<String> menu;

    public MenuDialogAdapter(Activity activity) {
        this.activity = activity;
        menu = Arrays.asList(activity.getResources().getStringArray(R.array.menu_dialog));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        final View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.menu_dialog_list, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.dialogText.setText(menu.get(position));
    }

    @Override
    public int getItemCount() {
        return menu.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView dialogText;

        public ViewHolder(View v) {
            super(v);
            dialogText = (TextView) v.findViewById(R.id.menu_dialog_text);
        }
    }
}
