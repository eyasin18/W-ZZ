package de.repictures.wzz.fragments.jokes;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Arrays;
import java.util.LinkedList;

import de.repictures.wzz.MainJokes;
import de.repictures.wzz.R;
import de.repictures.wzz.adapter.JokesAdapter;
import de.repictures.wzz.internet.loadMoreJokes;

public class Beliebteste extends Fragment {

    private RecyclerView mRecyclerView;
    public static LinkedList<String> mBeliebtesteJokes = new LinkedList<>();
    String postId;
    Boolean sort = true;
    private RecyclerView.Adapter mAdapter;
    int totalItemCount = 0;
    public static boolean loading = true;
    public static boolean removed = false;
    int pastVisiblesItems, visibleItemCount;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.view_pager_element, container, false);
        mBeliebtesteJokes = new LinkedList<>(Arrays.asList(getArguments().getString("jokes").split("</we>")));
        initRecyclerview(rootView);
        postId = MainJokes.katego;
        return rootView;
    }

    private void initRecyclerview(View rootView) {
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.jokes_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new JokesAdapter(mBeliebtesteJokes, false, MainJokes.coordinatorLayoutView, getActivity());
        mRecyclerView.setAdapter(mAdapter);
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    MainJokes.fabbutton.hide();
                } else {
                    MainJokes.fabbutton.show();
                }
                visibleItemCount = mLayoutManager.getChildCount();
                totalItemCount = mLayoutManager.getItemCount();
                pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();
                if (loading) {
                    if ( (visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                        new loadMoreJokes(mRecyclerView, sort, totalItemCount, mAdapter, postId, getActivity());
                        loading = false;
                    }
                }
            }
        });
    }
}