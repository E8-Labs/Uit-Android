package com.antizon.uit_android.models;

import android.graphics.Paint;

/********** Developed by ArfiDeveloper **********
 * Created by : Muhammad Ans on 7/21/2022 at 12:40 AM
 ******************************************************/


public class PercentageData {
    public float percentage;
    public int color;
    public float start, end;
    public Paint paint;

    public PercentageData(float percentage, int color) {
        this.percentage = percentage;
        this.color = color;
    }
}
