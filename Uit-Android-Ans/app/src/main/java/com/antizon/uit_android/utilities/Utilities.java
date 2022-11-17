package com.antizon.uit_android.utilities;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.antizon.uit_android.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.signature.MediaStoreSignature;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

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

    public static void hideNavigationAndStatusBar(Activity activity) {
        Window window = activity.getWindow();
        View view = window.getDecorView();
        view.setSystemUiVisibility(view.getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        view.setSystemUiVisibility(view.getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
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

    public static void loadCircleImage(Context context, String image, ImageView imageView) {
        try {
            Glide.with(context)
                    .load(image)
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .placeholder(R.drawable.post_placeholder_ic)
                    .error(R.drawable.post_placeholder_ic)
                    .apply(new RequestOptions().circleCrop())
                    .signature(new MediaStoreSignature("*/*", Calendar.DATE,0))
                    .into(imageView);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void loadThumbnailViaGlide(Context context, String videoUrl, ImageView imageView) {
        if (videoUrl.isEmpty()){
            imageView.setImageResource(R.drawable.post_placeholder_ic);
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


    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public static void shareAppLink(Context context, String url) {
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Swiit");
            //String shareMessage = "Let me recommend you this application\n";
            //+ BuildConfig.APPLICATION_ID
            shareIntent.putExtra(Intent.EXTRA_TEXT, url);
            context.startActivity(Intent.createChooser(shareIntent, "choose one"));
        } catch (Exception e) {
            //e.toString();
        }
    }

    public static boolean isValidUrl(String text) {
        String WebUrl = "^(www.)?(?!.*(ftp|http|https|www.))[a-zA-Z0-9_-]+(\\.[a-zA-Z]+)+((\\/)[\\w#]+)*(\\/\\w+\\?[a-zA-Z0-9_]+=\\w+(&[a-zA-Z0-9_]+=\\w+)*)?$";
        if (text != null && text.trim().length() > 0) {
            //validation msg
            return text.matches(WebUrl);
        }
        return false;
    }



    public static String getLocationStatus(int locationStatus) {
        switch (locationStatus) {
            case 1:
                return "Remote";
            case 2:
                return "Hybrid";
            case 3:
                return "InPerson";

            default:
                return "";
        }
    }

    public static String getEmploymentType(int employmentType) {
        switch (employmentType) {
            case 1:
                return "FullTime";
            case 2:
                return "PartTime";
            case 3:
                return "Contract";
            case 4:
                return "Freelance";
            case 5:
                return "Internship";

            default:
                return "";
        }
    }

    public static String getApplicantEmploymentStatus(int employmentType) {
        switch (employmentType) {
            case 0:
                return "Unemployed";
            case 1:
                return "Employed And Looking";
            case 2:
                return "Employed And Not Looking";
            default:
                return "Not Available";
        }
    }

    public static String getPersonaType(int role) {
        switch (role) {
            case 1:
                return "Admin";
            case 2:
                return "Company";
            case 3:
                return "Uit Member";
            case 4:
                return "Company Member";
            case 5:
                return "Applicant";

            default:
                return "";
        }
    }

    public static String getVeteranType(Integer type) {
        if (type == null) {
            return "Prefer not to say";
        }else{
            if (type == 0){
                return "Not Veteran";
            }else {
                return "Veteran";
            }
        }
    }

    public static String getGender(int type) {
        switch (type) {
            case 1:
                return "Male";
            case 2:
                return "Female";
            case 3:
                return "Non-binary";
            case 4:
                return "Prefer not to say";
            default:
                return "Not Provided";
        }
    }

    public static String getLgbt(int type) {
        switch (type) {
            case 0:
                return "No";
            case 1:
                return "Yes";
            case -1:
            default:
                return "Prefer not to say";
        }
    }


    public static String getAppliedJobStatus(int jobStatus) {
        switch (jobStatus) {
            case 1:
                return "Submitted";
            case 2:
                return "Rejected";
            case 3:
                return "Hired";
            default:
                return "";
        }
    }

    public static String getJobStatus(int jobStatus) {
        switch (jobStatus) {
            case 1:
                return "Opened";
            case 2:
                return "Closed";
            case 3:
                return "Filled";
            case 4:
                return "Paused";
            default:
                return "";
        }
    }


    public static String getShortAmount(int intAmount) {
        try {
            long amount = Long.parseLong(intAmount + "");
            String result = String.valueOf(amount);

            if (amount > 1000000000000000000L) {
                result = String.format(Locale.US,"%.1f", amount / 1000000000000000000.0d).concat("Qui");
            } else if (amount > 1000000000000000L) {
                result = String.format(Locale.US,"%.1f", amount / 1000000000000000.0d).concat("Qua");
            } else if (amount > 1000000000000L) {
                result = String.format(Locale.US,"%.1f", amount / 1000000000000.0d).concat("T");
            } else if (amount > 1000000000) {
                result = String.format(Locale.US,"%.1f", amount / 1000000000.0).concat("B");
            } else if (amount > 1000000) {
                result = String.format(Locale.US,"%.1f", amount / 1000000.0).concat("M");
            } else if (amount > 1000) {
                result = String.format(Locale.US,"%.1f", amount / 1000.0).concat("K");
            }
            return result;
        } catch (Exception e) {
            return "0.0";
        }
    }

    public static String getShortAmount(long amount) {
        try {
            String result = String.valueOf(amount);
            if (amount > 1000000000000000000L) {
                result = String.format(Locale.US,"%.1f", amount / 1000000000000000000.0d).concat("Qui");
            } else if (amount > 1000000000000000L) {
                result = String.format(Locale.US,"%.1f", amount / 1000000000000000.0d).concat("Qua");
            } else if (amount > 1000000000000L) {
                result = String.format(Locale.US,"%.1f", amount / 1000000000000.0d).concat("T");
            } else if (amount > 1000000000) {
                result = String.format(Locale.US,"%.1f", amount / 1000000000.0).concat("B");
            } else if (amount > 1000000) {
                result = String.format(Locale.US,"%.1f", amount / 1000000.0).concat("M");
            } else if (amount > 1000) {
                result = String.format(Locale.US,"%.1f", amount / 1000.0).concat("K");
            }
            return result;
        } catch (Exception e) {
            return "0.0";
        }
    }

    public static String getShortAmount(String amount) {
        try {
            String result = amount.replace(",", "");
            long amountNumber = Long.parseLong(result);
            if (amountNumber > 1000000000000000000L) {
                result = String.format(Locale.US,"%.1f", amountNumber / 1000000000000000000.0d).concat("Qui");
            } else if (amountNumber > 1000000000000000L) {
                result = String.format(Locale.US,"%.1f", amountNumber / 1000000000000000.0d).concat("Qua");
            } else if (amountNumber > 1000000000000L) {
                result = String.format(Locale.US,"%.1f", amountNumber / 1000000000000.0d).concat("T");
            } else if (amountNumber > 1000000000) {
                result = String.format(Locale.US,"%.1f", amountNumber / 1000000000.0).concat("B");
            } else if (amountNumber > 1000000) {
                result = String.format(Locale.US,"%.1f", amountNumber / 1000000.0).concat("M");
            } else if (amountNumber > 1000) {
                result = String.format(Locale.US,"%.1f", amountNumber / 1000.0).concat("K");
            }
            return result;
        } catch (Exception e) {
            return "0.0";
        }
    }


    public static void startNavigationIntent(Activity context, String desLatLng) {
        try {
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?q=" + desLatLng));
            context.startActivity(intent);
            context.overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
        } catch (Exception e) {
            Log.d("PlaceDetails", "ShareException" + e.toString());
        }
    }


    public static Bitmap getBitmapFromUri(Context context, Uri uri) {
        ParcelFileDescriptor parcelFileDescriptor = null;
        try {
            parcelFileDescriptor = context.getContentResolver().openFileDescriptor(uri, "r");
        } catch (FileNotFoundException e) {
            Toast.makeText(context, "File Not Found Exception!", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        assert parcelFileDescriptor != null;
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        try {
            parcelFileDescriptor.close();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "IOException!", Toast.LENGTH_LONG).show();
        }
        return image;
    }

}
