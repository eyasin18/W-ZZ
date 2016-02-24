package de.repictures.wzz.fragments.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.repictures.wzz.MainKatego;
import de.repictures.wzz.R;
import de.repictures.wzz.adapter.ReKaAdapter;

public class HomeFragment extends Fragment {

    View rekacicle;
    String[] classes;
    String sharedElementsName;
    Toolbar toolbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home2, container, false);
        MainKatego.toolbar = (Toolbar) rootView.findViewById(R.id.home_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(MainKatego.toolbar);
        getActivity().setTitle(R.string.nav_katego);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        rekacicle = rootView.findViewById(R.id.rekacicle);
        sharedElementsName = "cicle";
        classes = getActivity().getResources().getStringArray(R.array.classes);
        RecyclerView mRecyclerView = (RecyclerView) rootView.findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        RecyclerView.Adapter mAdapter = new ReKaAdapter(classes, getActivity());
        mRecyclerView.setAdapter(mAdapter);
        return rootView;
    }
}
