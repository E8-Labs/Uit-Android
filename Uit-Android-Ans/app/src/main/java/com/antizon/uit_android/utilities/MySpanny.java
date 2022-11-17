package com.antizon.uit_android.utilities;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;

import androidx.annotation.NonNull;

import com.antizon.uit_android.notifications.NotificationSpannable;

public class MySpanny extends SpannableStringBuilder {

    private int flag = Spannable.SPAN_EXCLUSIVE_EXCLUSIVE;

    public MySpanny() {
        super("");
    }

    public MySpanny(CharSequence text) {
        super(text);
    }

    public MySpanny(CharSequence text, MySpannable... spans) {
        super(text);
        for (MySpannable span : spans) {
            setSpan(span, 0, length());
        }
    }

    public MySpanny(CharSequence text, MySpannable span) {
        super(text);
        setSpan(span, 0, text.length());
    }


    public MySpanny append(CharSequence text, MySpannable... spans) {
        append(text);
        for (MySpannable span : spans) {
            setSpan(span, length() - text.length(), length());
        }
        return this;
    }

    public MySpanny append(CharSequence text, NotificationSpannable span) {
        append(text);
        setSpan(span, length() - text.length(), length());
        return this;
    }

    public MySpanny append(CharSequence text, MySpannable span) {
        append(text);
        setSpan(span, length() - text.length(), length());
        return this;
    }

    @NonNull
    @Override
    public MySpanny append(CharSequence text) {
        super.append(text);
        return this;
    }


    @Deprecated
    public MySpanny appendText(CharSequence text) {
        append(text);
        return this;
    }


    public void setFlag(int flag) {
        this.flag = flag;
    }


    private void setSpan(MySpannable span, int start, int end) {
        setSpan(span, start, end, flag);
    }

    private void setSpan(NotificationSpannable span, int start, int end) {
        setSpan(span, start, end, flag);
    }


    public MySpanny findAndSpan(CharSequence textToSpan, GetSpan getSpan) {
        int lastIndex = 0;
        while (lastIndex != -1) {
            lastIndex = toString().indexOf(textToSpan.toString(), lastIndex);
            if (lastIndex != -1) {
                setSpan(getSpan.getSpan(), lastIndex, lastIndex + textToSpan.length());
                lastIndex += textToSpan.length();
            }
        }
        return this;
    }

    public interface GetSpan {
        /**
         * @return A new span object should be returned.
         */
        MySpannable getSpan();
    }

    public static SpannableString spanText(CharSequence text, Object... spans) {
        SpannableString spannableString = new SpannableString(text);
        for (Object span : spans) {
            spannableString.setSpan(span, 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spannableString;
    }

    public static SpannableString spanText(CharSequence text, Object span) {
        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(span, 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }
}