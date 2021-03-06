package de.repictures.wzz;

import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.repictures.wzz.AsyncTasks.PassData;

public class ApplyActivity extends AppCompatActivity {

    private static final String TAG = "StartActivity";
    @Bind(R.id.apply_progressBar) ProgressBar progressbar;
    @Bind(R.id.apply_check) ImageView check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply);
        ButterKnife.bind(this);
        int crazyValue = 50;
        int[] likes = getIntent().getIntArrayExtra("likes");
        for (int i = 0; i < likes.length; i++){
            if (likes[i] == 0){
                switch (i){
                    case 0:
                        crazyValue = crazyValue + 7;
                        break;
                    case 1:
                        crazyValue = crazyValue - 7;
                        break;
                    case 2:
                        crazyValue = crazyValue - 8;
                        break;
                    case 3:
                        crazyValue = crazyValue + 15;
                        break;
                    case 4:
                        crazyValue = crazyValue + 23;
                        break;
                }
            } else if (likes[i] == 1){
                switch (i){
                    case 0:
                        crazyValue = crazyValue - 7;
                        break;
                    case 1:
                        crazyValue = crazyValue + 7;
                        break;
                    case 2:
                        crazyValue = crazyValue + 8;
                        break;
                    case 3:
                        crazyValue = crazyValue - 15;
                        break;
                    case 4:
                        crazyValue = crazyValue - 23;
                        break;
                }
            }
            Log.d(TAG, "onCreate: " + crazyValue);
        }
        String[] data = getIntent().getStringArrayExtra("data");
        final PassData mAuthTask = new PassData(data[0], data[1],
                Integer.parseInt(data[2]), data[3], data[4],
                this, Boolean.parseBoolean(data[5]), data[8],
                progressbar, check, crazyValue, data[9], null, Boolean.parseBoolean(data[7]));
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mAuthTask.execute((Void) null);
            }
        }, 1000);
    }
}
