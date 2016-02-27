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

public class Heute extends Fragment{

    private RecyclerView mRecyclerView;
    public static LinkedList<String> mHeuteJokes = new LinkedList<>();
    String postId;
    Boolean sort = false;
    private RecyclerView.Adapter mAdapter;
    MoPubRecyclerAdapter myMoPubAdapter;
    RequestParameters mRequestParameters;
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
        mHeuteJokes = new LinkedList<>(Arrays.asList(getArguments().getString("jokes").split("</we>")));
        TextView noJokes = (TextView) rootView.findViewById(R.id.jokes_no_jokes);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.jokes_recycler_view);
        if (mHeuteJokes.size() < 2){
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
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new JokesAdapter(mHeuteJokes, false, MainJokes.coordinatorLayoutView, getActivity());
        ViewBinder myViewBinder = new ViewBinder.Builder(R.layout.ad_jokeslist_layout)
                .titleId(R.id.ad_jokeslist_title)
                .textId(R.id.ad_jokeslist_text)
                .mainImageId(R.id.ad_jokeslist_image)
                .iconImageId(R.id.ad_jokeslist_icon)
                .callToActionId(R.id.ad_jokeslist_relativelayout)
                .build();

        MoPubStaticNativeAdRenderer myRenderer = new MoPubStaticNativeAdRenderer(myViewBinder);
        myMoPubAdapter = new MoPubRecyclerAdapter(getContext(), mAdapter);

        myMoPubAdapter.registerAdRenderer(myRenderer);
        mRecyclerView.setAdapter(myMoPubAdapter);
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
                        new loadMoreJokes(mRecyclerView, sort, totalItemCount, myMoPubAdapter, postId, getActivity());
                        loading = false;
                    }
                }
            }
        });
    }
    @Override
    public void onResume() {
        if (mHeuteJokes.size() >= 2){
            // Set up your request parameters
            mRequestParameters = new RequestParameters.Builder()
                    .build();

            // Request ads when the user returns to this activity.
            myMoPubAdapter.loadAds(MainJokes.AD_UNIT_ID, mRequestParameters);
        }
        super.onResume();
    }

    @Override
    public void onDestroy() {
        if (mHeuteJokes.size() >= 2) {
            myMoPubAdapter.destroy();
        }
        super.onDestroy();
    }
}