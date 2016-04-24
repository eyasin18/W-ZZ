package de.repictures.wzz.fragments.profile;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Locale;

import de.repictures.wzz.AsyncTasks.PassData;
import de.repictures.wzz.MainKatego;
import de.repictures.wzz.R;
import de.repictures.wzz.SplashActivity;
import de.repictures.wzz.fragments.home.MyJokesFragment;
import de.repictures.wzz.internet.getProfile;

public class MyProfileAboutFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "AboutFragment";
    private EditText statusEdit, aboutEdit, vnameEdit;
    private TextInputLayout statusLayout, aboutLayout, vnameLayout;
    private TextView nameText;

    public MyProfileAboutFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_profileabout_list, container, false);

        String[] profile = getArguments().getStringArray("profile");
        nameText = (TextView) rootView.findViewById(R.id.profile_name);
        statusEdit = (EditText) rootView.findViewById(R.id.profile_status_edit);
        statusLayout = (TextInputLayout) rootView.findViewById(R.id.profile_status_edit_label);
        aboutEdit = (EditText) rootView.findViewById(R.id.profile_about_edit);
        aboutLayout = (TextInputLayout) rootView.findViewById(R.id.profile_about_edit_label);
        vnameEdit = (EditText) rootView.findViewById(R.id.profile_vname_edit);
        vnameLayout = (TextInputLayout) rootView.findViewById(R.id.profile_vname_edit_label);
        try {
            nameText.setText(URLDecoder.decode(profile[0], "UTF-8"));
            if (profile[6].length() > 0)statusEdit.setText(URLDecoder.decode(profile[6], "UTF-8"));
            if (profile[1].length() > 0) vnameEdit.setText(URLDecoder.decode(profile[1], "UTF-8"));
            if (profile[7].length() > 0)aboutEdit.setText(URLDecoder.decode(profile[7], "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Button applyButton = (Button) rootView.findViewById(R.id.profile_apply_button);
        applyButton.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        statusLayout.setErrorEnabled(false);
        statusLayout.setError("");
        aboutLayout.setErrorEnabled(false);
        aboutLayout.setError("");
        vnameLayout.setErrorEnabled(false);
        vnameLayout.setError("");
        if (checkInputs()){} else {
            PassData mAuthTask = new PassData(getProfile.infos[2], vnameEdit.getText().toString(),
                    Integer.parseInt(getProfile.infos[3]), SplashActivity.picUrl, SplashActivity.coverUrl,
                    getActivity(), false, statusEdit.getText().toString(), null, null, 0, aboutEdit.getText().toString(),
                    nameText.getText().toString(), null);
            mAuthTask.execute((Void) null);
            Snackbar
                    .make(MyJokesFragment.myJokesCL, R.string.saved, Snackbar.LENGTH_LONG)
                    .show();
        }
    }

    private boolean checkInputs() {if (statusEdit.getText().toString().length() < 5){
            statusLayout.setErrorEnabled(true);
            statusLayout.setError(getActivity().getResources().getString(R.string.status_too_short));
            return true;
        } else if (vnameEdit.getText().toString().length() < 5){
            vnameLayout.setErrorEnabled(true);
            vnameLayout.setError(getActivity().getResources().getString(R.string.vname_too_short));
            return true;
        } else if (checkIllegalCharacters(vnameEdit.getText().toString())){
            vnameLayout.setErrorEnabled(true);
            vnameLayout.setError(illegalCharacters(getActivity().getResources().getString(R.string.vname_illegal)));
            return true;
        } else if (checkIllegalCharacters(statusEdit.getText().toString())){
            statusLayout.setErrorEnabled(true);
            statusLayout.setError(illegalCharacters(getActivity().getResources().getString(R.string.status_illegal)));
            return true;
        } else if(aboutEdit.getText().toString().length() < 5){
            aboutLayout.setErrorEnabled(true);
            aboutLayout.setError(getActivity().getResources().getString(R.string.about_too_short));
            return true;
        } else if (checkIllegalCharacters(aboutEdit.getText().toString())){
            aboutLayout.setErrorEnabled(true);
            aboutLayout.setError(illegalCharacters(getActivity().getResources().getString(R.string.about_illegal)));
            return true;
        } else {
            return false;
        }
    }

    private CharSequence illegalCharacters(String string) {
        if (Locale.getDefault().getLanguage().contentEquals(Locale.GERMAN.getLanguage())){
            return string + getIllegalCharacters() + " " + getActivity().getResources().getString(R.string.contain);
        } else {
            return string + getIllegalCharacters();
        }
    }

    private String getIllegalCharacters() {
        String[] illegals = getActivity().getResources().getStringArray(R.array.forbidden_characters);
        String illegal = "";
        for (int i = 0; i < illegals.length; i++){
            if (i == illegals.length - 1) illegal += " " + getActivity().getResources().getString(R.string.or) + " ";
            else if (i != 0) illegal += ", ";
            else illegal += " ";
            illegal += illegals[i];
        }
        return illegal;
    }

    private boolean checkIllegalCharacters(String string) {
        String[] illegals = getActivity().getResources().getStringArray(R.array.forbidden_characters);
        for (String illegal : illegals) {
            if (string.contains(illegal)) {
                return true;
            }
        }
        return false;
    }
}
