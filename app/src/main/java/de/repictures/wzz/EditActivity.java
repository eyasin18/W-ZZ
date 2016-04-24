package de.repictures.wzz;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.Arrays;
import java.util.Locale;
import java.util.Random;

public class EditActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private static final String TAG = "EditActivity";
    TextInputEditText nameEdit, statusEdit, aboutEdit;
    TextInputLayout nameLayout, statusLayout, aboutLayout;
    FloatingActionButton fab;
    Boolean gender = null;
    String[] data;
    Random rnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        Spinner spinner = (Spinner) findViewById(R.id.edit_gender_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.genders, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        data = getIntent().getStringArrayExtra("data");
        nameEdit = (TextInputEditText) findViewById(R.id.edit_vname_edit);
        nameEdit.setText(data[1]);
        nameLayout = (TextInputLayout) findViewById(R.id.edit_vname_edit_label);
        statusEdit = (TextInputEditText) findViewById(R.id.edit_status_edit);
        statusLayout = (TextInputLayout) findViewById(R.id.edit_status_edit_label);
        aboutEdit = (TextInputEditText) findViewById(R.id.edit_about_edit);
        aboutLayout = (TextInputLayout) findViewById(R.id.edit_about_edit_label) ;
        fab = (FloatingActionButton) findViewById(R.id.edit_fab);
        fab.setOnClickListener(this);
        rnd = new Random();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.edit_fab:
                getDataAndFinish();
                break;
        }
    }

    private void getDataAndFinish() {
        nameLayout.setErrorEnabled(false);
        aboutLayout.setErrorEnabled(false);
        statusLayout.setErrorEnabled(false);
        if (nameEdit.getText().length() == 0){
            nameLayout.setErrorEnabled(true);
            nameLayout.setError(getResources().getString(R.string.name_required));
        } else if (!checkInputs()){
            data[1] = nameEdit.getText().toString();
            if (gender != null) data[7] = String.valueOf(gender);
            else data[7] = String.valueOf(getRandomGender());
            if (statusEdit.getText().length() == 0) data[8] = null;
            else data[8] = statusEdit.getText().toString();
            if (aboutEdit.getText().length() == 0) data[9] = null;
            else data[9] = aboutEdit.getText().toString();
            Intent i = new Intent(this, ApplyActivity.class);
            i.putExtra("likes", getIntent().getIntArrayExtra("likes"));
            i.putExtra("data", data);
            startActivity(i);
            finish();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position == 1){
            gender = true;
        } else if (position == 2){
            gender = false;
        } else {
            gender = getRandomGender();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //Do nothing
    }

    public boolean getRandomGender() {
        return rnd.nextBoolean();
    }

    private boolean checkInputs() {
        if (nameEdit.getText().toString().length() < 1){
            nameLayout.setErrorEnabled(true);
            nameLayout.setError(getResources().getString(R.string.vname_too_short));
            return true;
        } else if (checkIllegalCharacters(nameEdit.getText().toString())){
            nameLayout.setErrorEnabled(true);
            nameLayout.setError(illegalCharacters(getResources().getString(R.string.vname_illegal)));
            return true;
        } else if (checkIllegalCharacters(statusEdit.getText().toString())){
            statusLayout.setErrorEnabled(true);
            statusLayout.setError(illegalCharacters(getResources().getString(R.string.status_illegal)));
            return true;
        } else if (checkIllegalCharacters(aboutEdit.getText().toString())){
            aboutLayout.setErrorEnabled(true);
            aboutLayout.setError(illegalCharacters(getResources().getString(R.string.about_illegal)));
            return true;
        } else {
            return false;
        }
    }

    private CharSequence illegalCharacters(String string) {
        if (Locale.getDefault().getLanguage().contentEquals(Locale.GERMAN.getLanguage())){
            return string + getIllegalCharacters() + " " + getResources().getString(R.string.contain);
        } else {
            return string + getIllegalCharacters();
        }
    }

    private String getIllegalCharacters() {
        String[] illegals = getResources().getStringArray(R.array.forbidden_characters);
        String illegal = "";
        for (int i = 0; i < illegals.length; i++){
            if (i == illegals.length - 1) illegal += " " + getResources().getString(R.string.or) + " ";
            else if (i != 0) illegal += ", ";
            else illegal += " ";
            illegal += illegals[i];
        }
        return illegal;
    }

    private boolean checkIllegalCharacters(String string) {
        String[] illegals = getResources().getStringArray(R.array.forbidden_characters);
        for (String illegal : illegals) {
            if (string.contains(illegal)) {
                return true;
            }
        }
        return false;
    }
}
