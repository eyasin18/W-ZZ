package de.repictures.wzz.fragments.profile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import de.repictures.wzz.R;

public class ProfileAboutFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_profile_about, container, false);
        TextView aboutText = (TextView) rootView.findViewById(R.id.about_text);
        String about = getArguments().getString("about");
        aboutText.setText(about);
        return rootView;
    }

}
