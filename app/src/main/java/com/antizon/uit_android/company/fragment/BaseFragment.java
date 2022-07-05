package com.antizon.uit_android.company.fragment;

import android.content.Context;
import android.util.Log;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.VolleyError;
import com.antizon.uit_android.company.utility.BaseActivity;
import com.antizon.uit_android.company.utility.VolleyRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;

public class BaseFragment extends Fragment implements VolleyRequest.VolleyResponseListener {

    Gson gson;
    VolleyRequest volleyRequest;
    Context context = getContext();
    private static final String TAG = "BaseFragment";


    public void sendServerRequestGET(String data, String accessToken) {
        // This is to initialize gson object.
        // We will use this gson to get Json object in child classes

        try {

            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.setDateFormat("M/d/yy hh:mm a");
            gson = gsonBuilder.create();

            volleyRequest = new VolleyRequest();
            volleyRequest.mVolleyResponse = BaseFragment.this;
            data = data.replaceAll(" ", "+");
            volleyRequest.getRequest(getContext(), data, false, accessToken);
            Log.d(TAG, "sendServerRequest: complete url:" + data);

        } catch (Exception e) {

            e.printStackTrace();
        }


    }

    public void sendServerRequestPOST(String url, HashMap<String, String> hashMapParams) {
        // This is to initialze gson object.
        // We will use this gson to get Json object in child classes

        try {

            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.setDateFormat("M/d/yy hh:mm a");
            gson = gsonBuilder.create();

            volleyRequest = new VolleyRequest();
            volleyRequest.mVolleyResponse = BaseFragment.this;
            url = url.replaceAll(" ", "+");
            volleyRequest.postRequest(getContext(), url, hashMapParams, false);
            Log.d(TAG, "sendServerRequest: complete url:" + url);
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public void sendServerRequestPOST(String url, HashMap<String, String> hashMapParams, String accessToken) {
        // This is to initialze gson object.
        // We will use this gson to get Json object in child classes

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();

        volleyRequest = new VolleyRequest();
        volleyRequest.mVolleyResponse = BaseFragment.this;
        url = url.replaceAll(" ", "+");
        volleyRequest.postRequest(context, url, hashMapParams, false, accessToken);
        Log.d(TAG, "sendServerRequest: complete url:" + url);
    }


    @Override
    public void requestStarted() {

    }

    @Override
    public void onResponseReceived(String response, String urlCalled) {

    }

    @Override
    public void requestEndedWithError(VolleyError error) {

    }


    public String getFrequencyId(String frequencyValue) {
        if (frequencyValue.equalsIgnoreCase("Monthly")) {
            return "1";
        } else if (frequencyValue.equalsIgnoreCase("Weekly")) {
            return "2";
        } else if (frequencyValue.equalsIgnoreCase("BiWeekly")) {
            return "3";
        } else if (frequencyValue.equalsIgnoreCase("Daily")) {
            return "4";
        }
        return "";
    }

    public String getFrequencyName(String frequencyId) {
        if (frequencyId.equalsIgnoreCase("1")) {
            return "Monthly";
        } else if (frequencyId.equalsIgnoreCase("2")) {
            return "Weekly";
        } else if (frequencyId.equalsIgnoreCase("3")) {
            return "BiWeekly";
        } else if (frequencyId.equalsIgnoreCase("4")) {
            return "Daily";
        }
        return "";
    }


    int getMonthDays(String monthName) {
        if (monthName.equalsIgnoreCase("January")
                || monthName.equalsIgnoreCase("March")
                || monthName.equalsIgnoreCase("May")
                || monthName.equalsIgnoreCase("July")
                || monthName.equalsIgnoreCase("August")
                || monthName.equalsIgnoreCase("October")
                || monthName.equalsIgnoreCase("December")) {
            return 31;
        } else if (monthName.equalsIgnoreCase("April")
                || monthName.equalsIgnoreCase("June")
                || monthName.equalsIgnoreCase("September")
                || monthName.equalsIgnoreCase("November")) {
            return 30;
        } else if (monthName.equalsIgnoreCase("February")) {
            return 28;
        }
        return 0;
    }

    int getMonthId(String monthName) {
        if (monthName.equalsIgnoreCase("January")) {
            return 1;
        } else if (monthName.equalsIgnoreCase("February")) {
            return 2;
        } else if (monthName.equalsIgnoreCase("March")) {
            return 3;
        } else if (monthName.equalsIgnoreCase("April")) {
            return 4;
        } else if (monthName.equalsIgnoreCase("May")) {
            return 5;
        } else if (monthName.equalsIgnoreCase("June")) {
            return 6;
        } else if (monthName.equalsIgnoreCase("July")) {
            return 7;
        } else if (monthName.equalsIgnoreCase("August")) {
            return 8;
        } else if (monthName.equalsIgnoreCase("September")) {
            return 9;
        } else if (monthName.equalsIgnoreCase("October")) {
            return 10;
        } else if (monthName.equalsIgnoreCase("November")) {
            return 11;
        } else if (monthName.equalsIgnoreCase("December")) {
            return 12;
        }
        return 1;
    }

    public String getMonthName(int monthId) {
        if (monthId == 1) {
            return "January";
        } else if (monthId == 2) {
            return "February";
        } else if (monthId == 3) {
            return "March";
        } else if (monthId == 4) {
            return "April";
        } else if (monthId == 5) {
            return "May";
        } else if (monthId == 6) {
            return "June";
        } else if (monthId == 7) {
            return "July";
        } else if (monthId == 8) {
            return "August";
        } else if (monthId == 9) {
            return "September";
        } else if (monthId == 10) {
            return "October";
        } else if (monthId == 11) {
            return "November";
        } else if (monthId == 12) {
            return "December";
        }
        return "January";
    }

    int getYearIndex(int year) {
        if (year == 2020) {
            return 0;
        } else if (year == 2021) {
            return 1;
        } else if (year == 2022) {
            return 2;
        } else if (year == 2023) {
            return 3;
        } else if (year == 2024) {
            return 4;
        } else if (year == 2025) {
            return 5;
        } else if (year == 2026) {
            return 6;
        } else if (year == 2027) {
            return 7;
        } else if (year == 2028) {
            return 8;
        } else if (year == 2029) {
            return 9;
        } else if (year == 2030) {
            return 10;
        }
        return 1;
    }
    public void hideWindowsKeyboard() {

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    public void hideSoftKeyboard(AppCompatEditText editText) {

        editText.clearFocus();
        InputMethodManager in = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    public void hideSoftKeyboard(EditText editText) {

        editText.clearFocus();
        InputMethodManager in = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    public void loadProfile(Context context, String url, ImageView imageView) {
        Log.d(TAG, "Image cache path: " + url);

        Glide.with(context)
                .load(url)
                .apply(new RequestOptions()
                        .circleCrop())
                .into(imageView);
        imageView.setColorFilter(ContextCompat.getColor(getContext(), android.R.color.transparent));
    }

}
