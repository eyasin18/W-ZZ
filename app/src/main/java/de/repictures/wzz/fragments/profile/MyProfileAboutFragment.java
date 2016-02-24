package de.repictures.wzz.fragments.profile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import de.repictures.wzz.MainKatego;
import de.repictures.wzz.R;
import de.repictures.wzz.SplashActivity;
import de.repictures.wzz.AsyncTasks.PassData;
import de.repictures.wzz.internet.getProfile;

public class MyProfileAboutFragment extends Fragment implements View.OnClickListener {

    private EditText nameEdit, statusEdit;
    private Button applyButton;

    public MyProfileAboutFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_profileabout_list, container, false);

        nameEdit = (EditText) rootView.findViewById(R.id.profile_name_edit);
        statusEdit = (EditText) rootView.findViewById(R.id.profile_status_edit);
        nameEdit.setText(MainKatego.drawerName.getText());
        statusEdit.setText(getProfile.infos[5]);
        applyButton = (Button) rootView.findViewById(R.id.profile_apply_button);
        applyButton.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        PassData mAuthTask = new PassData(getProfile.infos[2], nameEdit.getText().toString(),
                Integer.parseInt(getProfile.infos[3]), SplashActivity.picUrl, SplashActivity.coverUrl,
                getActivity(), false, statusEdit.getText().toString());
        mAuthTask.execute((Void) null);
    }
}
