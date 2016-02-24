package de.repictures.wzz.fragments.home;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.Arrays;
import java.util.LinkedList;

import de.repictures.wzz.MainKatego;
import de.repictures.wzz.R;
import de.repictures.wzz.adapter.JokesAdapter;
import de.repictures.wzz.internet.getProfile;

public class AboFragment extends Fragment {

    RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_abo, container, false);

        MainKatego.toolbar = (Toolbar) rootView.findViewById(R.id.abo_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(MainKatego.toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getActivity().setTitle(R.string.nav_abo);
        CoordinatorLayout cl = (CoordinatorLayout) rootView.findViewById(R.id.abo_coordinator);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.abo_recyclerview);
        mRecyclerView.setHasFixedSize(true);
        LinkedList<String> mHeuteJokes = new LinkedList<>(Arrays.asList(getProfile.infos[7].split("<br />")));
        mAdapter = new JokesAdapter(mHeuteJokes, false, cl, getActivity());
        mRecyclerView.setAdapter(mAdapter);
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.menu_fragment_abo, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_refresh:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
