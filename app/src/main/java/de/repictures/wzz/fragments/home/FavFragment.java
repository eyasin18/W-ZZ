package de.repictures.wzz.fragments.home;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import de.repictures.wzz.MainKatego;
import de.repictures.wzz.R;
import de.repictures.wzz.adapter.FavoListAdapter;

public class FavFragment extends Fragment {

    RecyclerView favoRecycler;
    private RecyclerView.Adapter favoAdapter;
    String listString = "";

    public FavFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_fav, container, false);
        MainKatego.toolbar = (Toolbar) rootView.findViewById(R.id.fav_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(MainKatego.toolbar);
        getActivity().setTitle(R.string.nav_favo);
        //noinspection ConstantConditions
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        //noinspection ConstantConditions
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences pref = getActivity().getSharedPreferences("Favoriten", 0);
        Set<String> set = pref.getStringSet("favoed", new HashSet<String>());
        ArrayList<String> list = new ArrayList<>();
        list.addAll(set);
        for (String s: list){
            listString += s;
        }
        initRecyclerview(rootView);
        return rootView;
    }

    private void initRecyclerview(View rootView) {
        favoRecycler = (RecyclerView) rootView.findViewById(R.id.favo_recycler_view);
        favoRecycler.setHasFixedSize(true);
        favoAdapter = new FavoListAdapter(listString, getActivity());
        favoRecycler.setAdapter(favoAdapter);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        favoRecycler.setLayoutManager(mLayoutManager);
    }

}
