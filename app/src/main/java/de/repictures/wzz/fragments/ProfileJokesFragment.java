package de.repictures.wzz.fragments;

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
import de.repictures.wzz.adapter.DetailListAdapter;
import de.repictures.wzz.adapter.ProfileJokesListAdapter;

public class ProfileJokesFragment extends Fragment {

    private static final String TAG = "ProfileJokesFragment";

    public ProfileJokesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_profile_jokes, container, false);
        String[] jokes = getArguments().getStringArray("jokes");
        String[] profile = getArguments().getStringArray("profile");
        Log.i(TAG, "onCreateView: " + Arrays.toString(jokes));
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.profile_recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        RecyclerView.Adapter recyclerAdapter = new ProfileJokesListAdapter(getActivity(), jokes, profile);
        recyclerView.setAdapter(recyclerAdapter);
        return rootView;
    }
}
