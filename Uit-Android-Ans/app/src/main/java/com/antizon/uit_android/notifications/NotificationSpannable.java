package com.antizon.uit_android.notifications;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import androidx.core.content.ContextCompat;

public class NotificationSpannable extends ClickableSpan {

    Context context;
    boolean isUnderline, isBold;
    private int color = -1;

    public NotificationSpannable(Context context, boolean isUnderline, boolean isBold, int color) {
        this.context = context;
        this.isUnderline = isUnderline;
        this.isBold = isBold;
        this.color = color;
    }

    public NotificationSpannable(boolean isUnderline) {
        this.isUnderline = isUnderline;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setUnderlineText(isUnderline);
        if (isBold){
            ds.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        }
        if (context != null && color != -1){
            ds.setColor(ContextCompat.getColor(context, color));
        }

    }

    @Override
    public void onClick(View widget) {

    }
}
