package de.repictures.wzz.uiHelper;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Path;
import android.graphics.Point;
import android.os.Build;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;

import de.repictures.wzz.MainJokes;
import de.repictures.wzz.internet.postWitze;

public class MainJokesFab {

    public static Thread t;

    public MainJokesFab(View v, Display display, FloatingActionButton fabbutton, ImageButton sendButton,
                        EditText writeWizzle, InputMethodManager imm, int initialRadius, Handler mHandler,
                        String katego, String s, String email, View dataView2, View dataView, Context context,
                        String key) {

        final FloatingActionButton fabbutton1 = fabbutton;
        final EditText writeWizzle1 = writeWizzle;
        final ImageButton sendeButton1 = sendButton;
        Point size = new Point();
        display.getSize(size);
        int screenWidth = size.x;
        int x, y, yr, xr;
        xr = writeWizzle.getWidth() - fabbutton.getWidth()*3;
        yr = writeWizzle.getHeight() - fabbutton.getHeight()/2;
        x = screenWidth - fabbutton.getWidth()*3;
        y = writeWizzle.getHeight();

        if (v == fabbutton) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                float x1 = dataView2.getX();
                float y1 = dataView2.getY();
                final float x2 = x1;
                final float y2 = dataView.getY();
                float x3 = dataView.getX();
                float y3 = y2;

                final AlphaAnimation alphaAnimation = new AlphaAnimation(1f, 0f);
                alphaAnimation.setDuration(100);
                final AlphaAnimation reverseAlphaAnimation = new AlphaAnimation(0f, 1f);
                reverseAlphaAnimation.setDuration(100);

                //ending x co-ordinates

                final Path path = new Path();
                path.moveTo(x1, y1);
                path.cubicTo(x1, y1, x2, y2, x3, y3);

                ObjectAnimator anim = ObjectAnimator.ofFloat(fabbutton, View.X, View.Y, path);
                anim.setDuration(100);
                anim.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        fabbutton1.setVisibility(View.INVISIBLE);
                    }
                });
                anim.start();

                final Animator animator = ViewAnimationUtils.createCircularReveal(
                        writeWizzle,
                        xr,
                        yr,
                        0,
                        (float) Math.hypot(writeWizzle.getWidth(), writeWizzle.getHeight()));
                animator.setInterpolator(new AccelerateDecelerateInterpolator());
                animator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        sendeButton1.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationStart(Animator animator1) {
                        super.onAnimationStart(animator1);
                        writeWizzle1.setVisibility(View.VISIBLE);
                    }
                });
                animator.setStartDelay(anim.getDuration()-30);
                animator.start();
            }
        } else if (v == sendButton){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                final AlphaAnimation alphaAnimation2 = new AlphaAnimation(0f, 1f);
                alphaAnimation2.setDuration(200);
                final AlphaAnimation reverseAlphaAnimation2 = new AlphaAnimation(1f, 0f);
                String inhalt = writeWizzle.getText().toString();
                if (inhalt!=null && !inhalt.isEmpty()){
                    Log.v("postInfos", katego + "~" + s + "~"
                            + inhalt);
                    t = new Thread(new postWitze(katego, inhalt, key, null, context, false));
                    t.start();
                }

                float x1 = dataView.getX();
                float y1 = dataView.getY();
                float y2 = y1;
                float x2 = dataView2.getX();
                float x3 = x2;
                float y3 = dataView2.getY();

                final Path path2 = new Path();
                path2.moveTo(x1, y1);
                path2.cubicTo(x1, y1, x2, y2, x3, y3);

                final ObjectAnimator anim2 = ObjectAnimator.ofFloat(fabbutton, View.X, View.Y, path2);
                anim2.setDuration(100);
                anim2.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        super.onAnimationStart(animation);
                        fabbutton1.setVisibility(View.VISIBLE);
                    }
                });
                Animator anim =
                        ViewAnimationUtils.createCircularReveal(writeWizzle, xr, yr, initialRadius, 0);
                anim.setInterpolator(new AccelerateDecelerateInterpolator());
                anim.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        MainJokes.animationEnd();
                    }
                });
                imm.hideSoftInputFromWindow(writeWizzle.getWindowToken(), 0);
                sendButton.setVisibility(View.GONE);
                anim2.setStartDelay(anim.getDuration()-50);
                anim2.start();
                anim.start();
            }
        }
    }
}
