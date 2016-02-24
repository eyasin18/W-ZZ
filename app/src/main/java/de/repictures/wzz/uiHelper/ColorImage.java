package de.repictures.wzz.uiHelper;

import android.app.Activity;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

public class ColorImage {
    public ColorImage(int pictureID, int colorID, ImageView image, Activity activity) {

        Drawable thumbUp_cyan = activity.getResources().getDrawable(pictureID);
        int cyan = activity.getResources().getColor(colorID);
        ColorFilter filter = new PorterDuffColorFilter(cyan, PorterDuff.Mode.MULTIPLY);
        if (thumbUp_cyan != null) {
            thumbUp_cyan.setColorFilter(filter);
        }
        image.setImageDrawable(thumbUp_cyan);
    }
}
