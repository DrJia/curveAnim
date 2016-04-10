/**
 * 
 */
package com.tencent.obd.view;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.os.Build;

/**
 * @author robbinjia
 * 
 */
public class VocValueLineViewManager {
    private static final String TAG = "VocValueLineViewManager";

    private VocValueLineView mValueLineView;

    private static final int defaultDuringMilliseconds = 1000;// ms

    private int duringMilliseconds = defaultDuringMilliseconds;

    private ObjectAnimator moveAnimation = null;

    public VocValueLineViewManager(VocValueLineView valueLineView) {
        this.mValueLineView = valueLineView;
    }

    public void setAnimTime(int milliseconds) {
        duringMilliseconds = milliseconds;
    }

    @SuppressLint("NewApi")
    public void showAnim(int ppdX) {

        double scale = mValueLineView.getScale();
        int positionX = (int) (ppdX * scale);

        if (Build.VERSION.SDK_INT < 11) {
            mValueLineView.setPositionX(positionX);
        } else {
            if (moveAnimation != null) {
                moveAnimation.cancel();
            }
            moveAnimation = getAnimator(positionX);
            moveAnimation.start();
        }
    }

    @SuppressLint("NewApi")
    private ObjectAnimator getAnimator(int ppdX) {
        int startX;
        int endX;

        startX = mValueLineView.getPositionX();
        endX = ppdX;

        ObjectAnimator progressAnimation = ObjectAnimator.ofInt(mValueLineView, "positionX", startX, endX);

        progressAnimation.setDuration(duringMilliseconds);

        return progressAnimation;
    }

}
