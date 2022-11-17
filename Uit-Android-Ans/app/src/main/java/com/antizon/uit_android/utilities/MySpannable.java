package com.antizon.uit_android.utilities;

import android.graphics.Color;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

public class MySpannable extends ClickableSpan {

    private final boolean isUnderline;
    /**
     * Constructor
     */
    public MySpannable(boolean isUnderline) {
        this.isUnderline = isUnderline;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setUnderlineText(isUnderline);
        ds.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        ds.setColor(Color.parseColor("#19999A"));
    }

    @Override
    public void onClick(View widget) {

    }
}
