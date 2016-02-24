package de.repictures.wzz;

import android.content.pm.ActivityInfo;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import de.repictures.wzz.internet.postReport;
import de.repictures.wzz.uiHelper.getPictures;

public class ReportActivity extends AppCompatActivity {

    RadioGroup reportGroup;
    String[] extras;
    TextView userText, deviseText, inhaltText, likesNum, reportsNum;
    ImageView profilepic, likeView, reportView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        if(getResources().getBoolean(R.bool.portrait_only)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        findViewById(R.id.load_comments).setVisibility(View.GONE);

        extras = getIntent().getExtras().getStringArray("detailExtras");
        initCard();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String art = null;
                String[] arts = getResources().getStringArray(R.array.report_server_codes);
                switch (reportGroup.getCheckedRadioButtonId()) {
                    case R.id.misuseButton:
                        art = arts[0];
                        break;
                    case R.id.offensiveButton:
                        art = arts[1];
                        break;
                    case R.id.sexualButton:
                        art = arts[2];
                        break;
                    case R.id.violentButton:
                        art = arts[3];
                        break;
                    case R.id.spamButton:
                        art = arts[4];
                        break;
                    case R.id.otherButton:
                        art = arts[5];
                        break;
                }
                new Thread(new postReport(art, extras[7], ReportActivity.this)).start();
            }
        });
        reportGroup = (RadioGroup) findViewById(R.id.reportRadioGroup);
    }

    private void initCard() {
        userText = (TextView) findViewById(R.id.detail_username);
        userText.setText(extras[2]);
        deviseText = (TextView) findViewById(R.id.detail_devise);
        deviseText.setText(extras[4]);
        inhaltText = (TextView) findViewById(R.id.detail_joke);
        inhaltText.setText(extras[3]);
        reportsNum = (TextView) findViewById(R.id.detail_number_report);
        reportsNum.setText(extras[6]);
        likesNum = (TextView) findViewById(R.id.detail_number_like);
        likesNum.setText(extras[5]);
        likeView = (ImageView) findViewById(R.id.detail_thumb_up);
        likeView.setImageDrawable(colorImage(R.drawable.ic_thumb_up_white_18dp, R.color.like_blue));
        reportView = (ImageView) findViewById(R.id.detail_report);
        reportView.setImageDrawable(colorImage(R.drawable.ic_flag_variant_white_18dp, R.color.report_red));
        profilepic = (ImageView) findViewById(R.id.detail_profile_pic);
        new Thread(new getPictures(extras[1], profilepic, null, this, true, true, false)).start();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public Drawable colorImage(int viewId, int colorId){
        Drawable view = getResources().getDrawable(viewId);
        int cyan = getResources().getColor(colorId);
        ColorFilter filter = new PorterDuffColorFilter(cyan, PorterDuff.Mode.MULTIPLY);
        if (view != null) {
            view.setColorFilter(filter);
        }
        return view;
    }

}
