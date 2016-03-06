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

import java.util.Locale;

import de.repictures.wzz.AsyncTasks.PassData;
import de.repictures.wzz.MainKatego;
import de.repictures.wzz.R;
import de.repictures.wzz.SplashActivity;
import de.repictures.wzz.fragments.home.MyJokesFragment;
import de.repictures.wzz.internet.getProfile;

public class MyProfileAboutFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "AboutFragment";
    private EditText nameEdit, statusEdit, aboutEdit, vnameEdit;
    private TextInputLayout nameLayout, statusLayout, aboutLayout, vnameLayout;
    private Button applyButton;

    public MyProfileAboutFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_profileabout_list, container, false);

        nameEdit = (EditText) rootView.findViewById(R.id.profile_name_edit);
        nameLayout = (TextInputLayout) rootView.findViewById(R.id.profile_name_edit_label);
        statusEdit = (EditText) rootView.findViewById(R.id.profile_status_edit);
        statusLayout = (TextInputLayout) rootView.findViewById(R.id.profile_status_edit_label);
        aboutEdit = (EditText) rootView.findViewById(R.id.profile_about_edit);
        aboutLayout = (TextInputLayout) rootView.findViewById(R.id.profile_about_edit_label);
        vnameEdit = (EditText) rootView.findViewById(R.id.profile_vname_edit);
        vnameLayout = (TextInputLayout) rootView.findViewById(R.id.profile_vname_edit_label);
        nameEdit.setText(MainKatego.drawerName.getText());
        if (getProfile.infos[5].length() > 4)statusEdit.setText(getProfile.infos[5]);
        if (getProfile.infos[9].length() > 4)vnameEdit.setText(getProfile.infos[9]);
        if (getProfile.infos[8].length() > 4)aboutEdit.setText(getProfile.infos[8]);
        applyButton = (Button) rootView.findViewById(R.id.profile_apply_button);
        applyButton.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        nameLayout.setErrorEnabled(false);
        nameLayout.setError("");
        statusLayout.setErrorEnabled(false);
        statusLayout.setError("");
        aboutLayout.setErrorEnabled(false);
        aboutLayout.setError("");
        vnameLayout.setErrorEnabled(false);
        vnameLayout.setError("");
        if (checkInputs()){} else {
            PassData mAuthTask = new PassData(getProfile.infos[2], nameEdit.getText().toString(),
                    Integer.parseInt(getProfile.infos[3]), SplashActivity.picUrl, SplashActivity.coverUrl,
                    getActivity(), false, statusEdit.getText().toString(), null, null, 0, aboutEdit.getText().toString(),
                    vnameEdit.getText().toString());
            mAuthTask.execute((Void) null);
            Snackbar
                    .make(MyJokesFragment.myJokesCL, R.string.saved, Snackbar.LENGTH_LONG)
                    .show();
        }
    }

    private boolean checkInputs() {
        if (nameEdit.getText().toString().length() < 5){
            nameLayout.setErrorEnabled(true);
            nameLayout.setError(getActivity().getResources().getString(R.string.name_too_short));
            return true;
        } else if (checkIllegalCharacters(nameEdit.getText().toString())){
            nameLayout.setErrorEnabled(true);
            nameLayout.setError(illegalCharacters(getActivity().getResources().getString(R.string.name_illegal)));
            return true;
        } else if (statusEdit.getText().toString().length() < 5){
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
        } else if (nameEdit.getText().toString().contentEquals(vnameEdit.getText().toString())){
            nameLayout.setErrorEnabled(true);
            nameLayout.setError(getActivity().getResources().getString(R.string.names_equal));
            vnameLayout.setErrorEnabled(true);
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
