package de.repictures.wzz.fragments.jokes;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mopub.nativeads.MoPubRecyclerAdapter;
import com.mopub.nativeads.MoPubStaticNativeAdRenderer;
import com.mopub.nativeads.RequestParameters;
import com.mopub.nativeads.ViewBinder;

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
    MoPubRecyclerAdapter myMoPubAdapter;
    RequestParameters mRequestParameters;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.view_pager_element, container, false);
        mBeliebtesteJokes = new LinkedList<>(Arrays.asList(getArguments().getString("jokes").split("</we>")));
        TextView noJokes = (TextView) rootView.findViewById(R.id.jokes_no_jokes);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.jokes_recycler_view);
        if (mBeliebtesteJokes.size() < 2){
            mRecyclerView.setVisibility(View.GONE);
            noJokes.setVisibility(View.VISIBLE);
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            noJokes.setVisibility(View.GONE);
            initRecyclerview();
        }
        postId = MainJokes.katego;
        return rootView;
    }

    private void initRecyclerview() {
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new JokesAdapter(mBeliebtesteJokes, false, MainJokes.coordinatorLayoutView, getActivity());
        ViewBinder myViewBinder = new ViewBinder.Builder(R.layout.ad_jokeslist_layout)
                .titleId(R.id.ad_jokeslist_title)
                .textId(R.id.ad_jokeslist_text)
                .mainImageId(R.id.ad_jokeslist_image)
                .iconImageId(R.id.ad_jokeslist_icon)
                .build();

        MoPubStaticNativeAdRenderer myRenderer = new MoPubStaticNativeAdRenderer(myViewBinder);
        myMoPubAdapter = new MoPubRecyclerAdapter(getContext(), mAdapter);

        myMoPubAdapter.registerAdRenderer(myRenderer);
        mRecyclerView.setAdapter(myMoPubAdapter);
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
                    if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                        loading = false;
                        new loadMoreJokes(mRecyclerView, sort, totalItemCount, myMoPubAdapter, postId, getActivity());
                    }
                }
            }
        });
    }

    @Override
    public void onResume(){
        if (mBeliebtesteJokes.size() >= 2) {
            // Set up your request parameters
            mRequestParameters = new RequestParameters.Builder()
                    .location(null)
                    .build();

            // Request ads when the user returns to this activity.
            myMoPubAdapter.loadAds(MainJokes.AD_UNIT_ID);
        }
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        if (mBeliebtesteJokes.size() >= 2) {
            myMoPubAdapter.destroy();
        }
        super.onDestroy();
    }
}