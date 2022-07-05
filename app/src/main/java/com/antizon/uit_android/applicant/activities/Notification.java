package com.antizon.uit_android.applicant.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.antizon.uit_android.R;
import com.antizon.uit_android.company.utility.BaseActivity;

public class Notification extends BaseActivity {
    private static final String TAG = Notification.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        setIds();
        initialize();
        setListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");

    }

    void setIds() {
        Log.d(TAG, "setIds: ");

    }

    void initialize() {
        Log.d(TAG, "initialize: ");


    }

    void setListener() {
        Log.d(TAG, "setListener: ");
    }


}