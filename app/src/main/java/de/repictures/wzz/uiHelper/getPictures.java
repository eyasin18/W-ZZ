package de.repictures.wzz.uiHelper;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.squareup.picasso.Transformation;

import java.util.Arrays;
import java.util.List;

public class getPictures implements Runnable{

    //private static final String TAG = "getPictures";
    String url;
    ImageView imageView;
    private Drawable drawable;
    Activity activity;
    Boolean transfrom;
    private boolean transition;
    private boolean blur;

    public getPictures(String url, ImageView imageView, Drawable drawable, Activity activity,
                       boolean circle, boolean transition, boolean blur) {
        this.drawable = drawable;
        this.activity = activity;
        this.imageView = imageView;
        this.url = url;
        this.transfrom = circle;
        this.transition = transition;
        this.blur = blur;
        //Log.i(TAG, "getPictures: started with: " + url);
    }

    @Override
    public void run() {
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
        final Target target;
        if (imageView != null) {
            target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    Drawable d = new BitmapDrawable(activity.getResources(), bitmap);
                    if (transition) {
                        TransitionDrawable td = new TransitionDrawable(new Drawable[]{
                                new ColorDrawable(activity.getResources().getColor(android.R.color.transparent)),
                                d
                        });
                        imageView.setImageDrawable(td);
                        td.startTransition(110);
                    } else {
                        imageView.setImageDrawable(d);
                    }
                    //Log.d(TAG, "ImageView success");
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                    imageView.setImageDrawable(errorDrawable);
                    //Log.e(TAG, "ImageView onBitmapFailed: drawingImageFailed");
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {
                    //Log.i(TAG, "ImageView onPrepareLoad: prepared");
                }
            };
            imageView.setTag(target);
        } else {
            target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    drawable = new BitmapDrawable(activity.getResources(), bitmap);
                    //Log.d(TAG, "Drawable success");
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                    drawable = errorDrawable;
                    //Log.e(TAG, "Drawable onBitmapFailed: drawingImageFailed");
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {
                    //Log.i(TAG, "Drawable onPrepareLoad: prepared");
                }
            };
        }
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (transfrom && !blur) {
                    Picasso.with(activity).load(url).transform(new CircleTransform()).into(target);
                } else if (blur && !transfrom){
                    Picasso.with(activity).load(url).transform(new BlurTransform(activity, 25, 1)).into(target);
                } else if (blur && transfrom){
                    List<Transformation> transformations = Arrays.asList(new CircleTransform(),
                            new BlurTransform(activity, 25, 1));
                    Picasso.with(activity).load(url).transform(transformations).into(target);
                } else {
                    Picasso.with(activity).load(url).into(target);
                }
            }
        });

    }
}
