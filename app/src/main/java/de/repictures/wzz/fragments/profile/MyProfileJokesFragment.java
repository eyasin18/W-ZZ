package de.repictures.wzz.fragments.profile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Arrays;

import de.repictures.wzz.R;
import de.repictures.wzz.adapter.MyJokesAdapter;

public class MyProfileJokesFragment extends Fragment {

    private static final String TAG = "MyProfileFragment";

    public MyProfileJokesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_profile_jokes, container, false);
        String[] jokes = getArguments().getStringArray("jokes");
        Log.i(TAG, "onCreateView: " + Arrays.toString(jokes));
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.profile_recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        RecyclerView.Adapter recyclerAdapter = new MyJokesAdapter(jokes, getActivity());
        if (jokes != null && jokes.length > 0){
            recyclerView.setAdapter(recyclerAdapter);
        }
        return rootView;
    }
}
