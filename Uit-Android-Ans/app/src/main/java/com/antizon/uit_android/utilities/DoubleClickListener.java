package com.antizon.uit_android.utilities;

import android.view.View;

public abstract class DoubleClickListener implements View.OnClickListener {

    private static final long DOUBLE_CLICK_TIME_DELTA = 300;//milliseconds
    long lastClickTime = 0;

    @Override
    public void onClick(View v) {
        long clickTime = System.currentTimeMillis();
        if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA){
            lastClickTime = 0;
        } else {
            onSingleClick(v);
        }
        lastClickTime = clickTime;
    }

    public abstract void onSingleClick(View v);
}