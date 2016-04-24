package de.repictures.wzz.fragments.profile;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.repictures.wzz.R;
import de.repictures.wzz.fragments.dummy.DummyContent;

public class MyProfileFollowingFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_following_list, container, false);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.list);
        String[] abos = getArguments().getStringArray("following");

        if (abos != null && abos.length > 0) {
            recyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setAdapter(new MyProfileFollowingAdapter(abos, getActivity()));
        } else {
            rootView.findViewById(R.id.no_abos_text).setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
        return rootView;
    }
}
