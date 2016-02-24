package de.repictures.wzz.uiHelper;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.widget.TextView;

public class updateNumber {

    float abstand;
    float counterY, newCounterY;

    public updateNumber(final TextView counter, final TextView newCounter, final int newNumber, int number) {

        if (number < newNumber){
            counterY= counter.getY();
            newCounterY = newCounter.getY();
            abstand = counter.getY() - newCounter.getY();
        } else if (number > newNumber){
            counterY= counter.getY();
            newCounterY = newCounter.getY();
            abstand = counter.getY() - newCounter.getY();
            newCounter.setY(counter.getY() + abstand);
            abstand = counter.getY() - newCounter.getY();
        } else {
            return;
        }

        newCounter.setText(String.valueOf(newNumber));
        counter.animate().translationYBy(abstand).setDuration(200).start();
        counter.animate().alpha(0).setDuration(200).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                counter.setY(counterY);
                counter.setAlpha(1);
                counter.setText(String.valueOf(newNumber));
            }
        }).start();
        newCounter.animate().translationYBy(abstand).setDuration(200).start();
        newCounter.animate().alpha(1).setDuration(200).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                newCounter.setY(newCounterY);
                newCounter.setAlpha(0);
            }
        }).start();
    }
}
