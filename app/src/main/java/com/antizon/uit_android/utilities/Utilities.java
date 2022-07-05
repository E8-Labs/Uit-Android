package com.antizon.uit_android.utilities;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.antizon.uit_android.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.signature.MediaStoreSignature;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class Utilities {

    public static void setWhiteBars(Activity activity) {
        Window window = activity.getWindow();
        View view = window.getDecorView();
        view.setSystemUiVisibility(view.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        view.setSystemUiVisibility(view.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        window.setNavigationBarColor(activity.getResources().getColor(android.R.color.white, activity.getTheme()));
        window.setStatusBarColor(activity.getResources().getColor(android.R.color.white, activity.getTheme()));
    }

    public static void setBlackBars(Activity activity) {
        Window window = activity.getWindow();
        View view = window.getDecorView();
        view.setSystemUiVisibility(view.getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        view.setSystemUiVisibility(view.getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        window.setNavigationBarColor(activity.getResources().getColor(android.R.color.black, activity.getTheme()));
        window.setStatusBarColor(activity.getResources().getColor(android.R.color.black, activity.getTheme()));
    }

    public static void setAppColorBars(Activity activity) {
            Window window = activity.getWindow();
            View view = window.getDecorView();
            view.setSystemUiVisibility(view.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            view.setSystemUiVisibility(view.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
            window.setNavigationBarColor(activity.getResources().getColor(R.color.app_color, activity.getTheme()));
            window.setStatusBarColor(activity.getResources().getColor(R.color.app_color, activity.getTheme()));
    }

    public static void setCustomStatusBarColor(Activity activity, int color) {
        Window window = activity.getWindow();
        View view = window.getDecorView();
        view.setSystemUiVisibility(view.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        view.setSystemUiVisibility(view.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        window.setStatusBarColor(activity.getResources().getColor(color, activity.getTheme()));
        window.setNavigationBarColor(activity.getResources().getColor(R.color.white, activity.getTheme()));
    }

    public static void setCustomStatusAndNavColor(Activity activity, int statusColor, int navColor) {
        Window window = activity.getWindow();
        View view = window.getDecorView();
        view.setSystemUiVisibility(view.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        view.setSystemUiVisibility(view.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        window.setNavigationBarColor(activity.getResources().getColor(navColor, activity.getTheme()));
        window.setStatusBarColor(activity.getResources().getColor(statusColor, activity.getTheme()));
    }

    public static void hideNavAndStatusBar(Activity activity) {
        Window window = activity.getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    public static String loadJSONFromAsset(Activity activity) {
        String json;
        try {
            InputStream is = activity.getAssets().open("countries.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public static String getCurrentTimeStamp() {
        long tsLong = System.currentTimeMillis() / 1000;
        return Long.toString(tsLong);
    }


    public static long getTimeFromPattern(String date, String pattern) {
        long output = System.currentTimeMillis();
        try {
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            output = simpleDateFormat.parse(date).getTime();
        } catch (Exception e) {
            Log.e("YAM", "Exception : " + e.getMessage());
            e.printStackTrace();
        }
        return output;
    }

    public static void loadImage(Context context, String image, ImageView imageView, View loadingImage) {
        try {
            loadingImage.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(image)
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .placeholder(R.drawable.post_placeholder_ic)
                    .error(R.drawable.post_placeholder_ic)
                    .signature(new MediaStoreSignature("*/*", Calendar.DATE,0))
                    .listener(new RequestListener<>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            loadingImage.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            loadingImage.setVisibility(View.GONE);
                            return false;
                        }
                    }).into(imageView);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void loadImage(Context context, String image, ImageView imageView) {
        try {
            Glide.with(context)
                    .load(image)
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .placeholder(R.drawable.post_placeholder_ic)
                    .error(R.drawable.post_placeholder_ic)
                    .signature(new MediaStoreSignature("*/*", Calendar.DATE,0))
                    .into(imageView);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void loadThumbnailViaGlide(Context context, String videoUrl, ImageView imageView) {
        if (videoUrl.isEmpty()){
            imageView.setImageResource(R.drawable.ic_image_add_line);
        }
        else {
            try {
                RequestOptions requestOptions = new RequestOptions();

                Glide.with(context).setDefaultRequestOptions(requestOptions).load(videoUrl).diskCacheStrategy(DiskCacheStrategy.DATA).skipMemoryCache(false).dontAnimate().into(imageView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void hideKeyboard(View view, Context context) {
        InputMethodManager in = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(),0);
    }

    public static List<String> getListFromCommaSeparatedString(String string) {
        return Arrays.asList(string.split(","));
    }

    public static String getMonthNameByNumber(int month) {
        String monthName  = "";
        switch (month){
            case 1:
                monthName = "Jan";
                break;
            case 2:
                monthName = "Feb";
                break;
            case 3:
                monthName = "Mar";
                break;
            case 4:
                monthName = "Apr";
                break;
            case 5:
                monthName = "May";
                break;
            case 6:
                monthName = "Jun";
                break;
            case 7:
                monthName = "Jul";
                break;
            case 8:
                monthName = "Aug";
                break;
            case 9:
                monthName = "Sep";
                break;
            case 10:
                monthName = "Oct";
                break;
            case 11:
                monthName = "Nov";
                break;
            case 12:
                monthName = "Dec";
                break;
        }

        return monthName;
    }

    public static String getCommaSeparatedString(ArrayList<String> list) {
        String result = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            result = String.join(",", list);
        } else {
            StringBuilder csvBuilder = new StringBuilder();
            for (String string : list) {
                csvBuilder.append(string);
                csvBuilder.append(",");
            }
            result = csvBuilder.toString();
        }
        return result;
    }
    public static String getCommaSeparatedString(List<String> list) {
        String result = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            result = String.join(",", list);
        } else {
            StringBuilder csvBuilder = new StringBuilder();
            for (String string : list) {
                csvBuilder.append(string);
                csvBuilder.append(",");
            }
            result = csvBuilder.toString();
        }
        return result;
    }


}
