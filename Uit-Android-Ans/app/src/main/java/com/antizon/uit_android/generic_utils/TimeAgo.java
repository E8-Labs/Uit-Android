package com.antizon.uit_android.generic_utils;

import android.util.Log;

public class TimeAgo {
    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    public static String getTimeAgo(long time) {
        if (time < 1000000000000L) {
            time *= 1000;
        }

        long now = System.currentTimeMillis();
        if (time > now || time <= 0) {

            Log.d("TimeAgo", "validate: startDateTimestamp: getTimeAgo: return  null");
            return null;
        }

        final long diff = now - time;

        if (diff < MINUTE_MILLIS) {
            Log.d("TimeAgo", "validate: startDateTimestamp: getTimeAgo: return  MINUTE_MILLIS");
            return "just now difference by " + diff;
        } else if (diff < 2 * MINUTE_MILLIS) {
            Log.d("TimeAgo", "validate: startDateTimestamp: getTimeAgo: return  2 * MINUTE_MILLIS");
            return "a minute ago";
        } else if (diff < 50 * MINUTE_MILLIS) {
            Log.d("TimeAgo", "validate: startDateTimestamp: getTimeAgo: return  50 * MINUTE_MILLIS");
            return diff / MINUTE_MILLIS + " minutes ago";
        } else if (diff < 90 * MINUTE_MILLIS) {
            Log.d("TimeAgo", "validate: startDateTimestamp: getTimeAgo: return  90 * MINUTE_MILLIS");
            return "an hour ago";
        } else if (diff < 24 * HOUR_MILLIS) {
            Log.d("TimeAgo", "validate: startDateTimestamp: getTimeAgo: return  24 * MINUTE_MILLIS");
            return diff / HOUR_MILLIS + " hours ago";
        } else if (diff < 48 * HOUR_MILLIS) {
            Log.d("TimeAgo", "validate: startDateTimestamp: getTimeAgo: return  48 * MINUTE_MILLIS");
            return "yesterday";
        } else {
            Log.d("TimeAgo", "validate: startDateTimestamp: getTimeAgo: return  Days Ago");
            return diff / DAY_MILLIS + " days ago";
        }
    }

    public static long getTimeAgoInDays(long time) {
        if (time < 1000000000000L) {
            time *= 1000;
        }

        long now = System.currentTimeMillis();
        if (time <= now) {

            Log.d("TimeAgo", "validate: startDateTimestamp: getTimeAgo: return  null");
            return -1;
        }

//        final long diff = now - time;
        final long diff = time - now;

        if (diff < 48 * HOUR_MILLIS) {
            Log.d("TimeAgo", "validate: startDateTimestamp: getTimeAgo: return  48 * MINUTE_MILLIS");
            return 1;
        } else {
            Log.d("TimeAgo", "validate: startDateTimestamp: getTimeAgo: return  Days Ago");
            return diff / DAY_MILLIS;
        }
    }
}