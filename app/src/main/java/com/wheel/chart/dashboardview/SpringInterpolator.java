package com.wheel.chart.dashboardview;

import android.view.animation.Interpolator;

/**
 * ================================================
 * Description: MPAndroidChart v3.1.0
 * <p>
 * Created by MPAndroidChartProject on 10/10/2019 14:12
 * <a href="mailto:yuanw9@sina.com">Contact me</a>
 * ================================================
 */
public class SpringInterpolator implements Interpolator {
    private final float mTension;
    public SpringInterpolator() {
        mTension = 0.4f;

    }
    public SpringInterpolator(float tension) {
        mTension = tension;
    }

    @Override
    public float getInterpolation(float input) {
        float result = (float) (Math.pow(2,-10 * input) *
                Math.sin((input - mTension / 4) * (2 * Math.PI)/mTension) + 1);
        return result;
    }
}
