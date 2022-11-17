package com.antizon.uit_android.utilities;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.antizon.uit_android.models.PercentageData;

import java.util.List;
import java.util.Locale;

/********** Developed by ArfiDeveloper **********
 * Created by : Muhammad Ans on 7/21/2022 at 12:21 AM
 ******************************************************/


public class PercentageGraph extends View {

    Context context;
    float totalSum = 100, width, height, widthRatio;
    private Paint textPaint;

    private List<PercentageData> list;

    private OnInitializedListener onInitializedListener;

    public PercentageGraph(Context context) {
        super(context);
        this.context = context;
    }

    public PercentageGraph(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public PercentageGraph(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    public PercentageGraph(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
    }

    public void setTotalSum(float totalSum) {
        this.totalSum = totalSum;
        init();
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        init();
    }

    private void init() {
        widthRatio = width / totalSum;

        textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(32);

        if (onInitializedListener != null)
            onInitializedListener.onInitialized();
    }

    public void setData(List<PercentageData> data) {
        if (widthRatio == 0) {
            return;
        }


        this.list = data;
        float consumed = 0;
        for (PercentageData item : list) {
            float w = widthRatio * item.percentage;
            item.start = consumed;
            item.end = consumed + w;
            consumed = item.end;

            // init the paint
            item.paint = new Paint();
            item.paint.setColor(ContextCompat.getColor(context, item.color));
            item.paint.setAntiAlias(true);
            item.paint.setStyle(Paint.Style.FILL);
        }
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (list == null) {
            return;
        }

        for (PercentageData item : list) {
            canvas.drawRect(item.start, 0, item.end, item.end + height, item.paint);
            canvas.drawText( (int)item.percentage+ "%", ((item.start + item.end) / 2) -30, height / 2 + 10, textPaint);
        }
    }

    public void setTextColor(int color) {
        if (textPaint == null) textPaint = new Paint();
        textPaint.setColor(ContextCompat.getColor(context, color));
    }

    public void setTextSize(int size) {
        if (textPaint == null) textPaint = new Paint();
        textPaint.setTextSize(size);
    }

    public void setFontFamily(int font) {
        if (textPaint == null) textPaint = new Paint();
        textPaint.setTypeface(ResourcesCompat.getFont(context, font));
    }

    public void setOnInitializedListener(OnInitializedListener onInitializedListener) {
        this.onInitializedListener = onInitializedListener;
    }

    public interface OnInitializedListener {
        void onInitialized();
    }
}
