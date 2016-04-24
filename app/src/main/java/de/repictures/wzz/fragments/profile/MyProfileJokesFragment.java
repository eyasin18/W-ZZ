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
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.profile_recycler_view);
        String[] jokes = getArguments().getStringArray("jokes");
        if (jokes != null && jokes.length > 0){
            Log.i(TAG, "onCreateView: " + Arrays.toString(jokes));
            recyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(mLayoutManager);
            RecyclerView.Adapter recyclerAdapter = new MyJokesAdapter(jokes, getActivity());
            recyclerView.setAdapter(recyclerAdapter);
        } else {
            rootView.findViewById(R.id.no_jokes_text).setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
        return rootView;
    }
}
