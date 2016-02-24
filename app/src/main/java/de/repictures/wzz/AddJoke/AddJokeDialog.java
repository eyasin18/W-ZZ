package de.repictures.wzz.AddJoke;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.transition.ArcMotion;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.repictures.wzz.MainJokes;
import de.repictures.wzz.R;
import de.repictures.wzz.internet.postJokes;
import de.repictures.wzz.internet.postWitze;

public class AddJokeDialog extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {

    private ViewGroup container;
    @Bind(R.id.add_joke) EditText writeWizzle;
    @Bind(R.id.jokesdialog_framlayout) ViewGroup frameLayout;
    @Bind(R.id.send) Button send;
    @Bind(R.id.cancel) Button cancel;
    @Bind(R.id.progressBar) ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_joke_dialog);
        ButterKnife.bind(this);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        container = (ViewGroup) findViewById(R.id.container);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            setupSharedEelementTransitions1();
        }
        //frameLayout.setOnTouchListener(this);
        //container.setOnTouchListener(this);
        View.OnClickListener dismissListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        };
        send.setOnClickListener(this);
        cancel.setOnClickListener(dismissListener);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void setupSharedEelementTransitions1() {
        ArcMotion arcMotion = new ArcMotion();
        arcMotion.setMinimumHorizontalAngle(50f);
        arcMotion.setMinimumVerticalAngle(50f);

        Interpolator easeInOut = AnimationUtils.loadInterpolator(this, android.R.interpolator.fast_out_slow_in);

        MorphFabToDialog sharedEnter = new MorphFabToDialog();
        sharedEnter.setPathMotion(arcMotion);
        sharedEnter.setInterpolator(easeInOut);

        MorphDialogToFab sharedReturn = new MorphDialogToFab();
        sharedReturn.setPathMotion(arcMotion);
        sharedReturn.setInterpolator(easeInOut);

        if (container != null) {
            sharedEnter.addTarget(container);
            sharedReturn.addTarget(container);
        }
        getWindow().setSharedElementEnterTransition(sharedEnter);
        getWindow().setSharedElementReturnTransition(sharedReturn);
    }

    @Override
    public void onBackPressed() {
        dismiss();
    }

    public void dismiss() {
        setResult(Activity.RESULT_CANCELED);
        writeWizzle.setVisibility(View.GONE);
        send.setVisibility(View.GONE);
        cancel.setVisibility(View.GONE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            finishAfterTransition();
        } else {
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        postJokes mAuthTask = new postJokes(MainJokes.katego,
                changeForbiddenCharacters(writeWizzle.getText().toString()),
                liesKey(), this, this, //writeWizzle.getText().toString()
                send, cancel, writeWizzle, progressBar);
        mAuthTask.execute((Void) null);
        Log.v("postInfos", MainJokes.katego + "~" + liesUserName() + "~"
                + writeWizzle.getText().toString());
    }

    private String liesUserName(){
        SharedPreferences pref = getSharedPreferences("UserInformations", 0);
        return pref.getString("UserName", "");
    }

    private String liesKey(){
        SharedPreferences pref = getSharedPreferences("Account", 0);
        return pref.getString("Key", "");
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v != container){
            dismiss();
            return true;
        } else {
            return false;
        }
    }

    private String changeForbiddenCharacters(String text) {
        String[] forbidden = getResources().getStringArray(R.array.forbidden_characters);
        String[] replacers = getResources().getStringArray(R.array.replacing_characters);
        for (int i = 0; i < forbidden.length-1; i++){
            text = text.replace(forbidden[i], replacers[i]);
        }
        return text;
    }
}

