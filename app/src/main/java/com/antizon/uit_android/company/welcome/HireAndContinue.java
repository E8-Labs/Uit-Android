package com.antizon.uit_android.company.welcome;


import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.antizon.uit_android.R;
import com.antizon.uit_android.company.utility.BaseActivity;

public class HireAndContinue extends BaseActivity {
    private static final String TAG = HireAndContinue.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hire_and_continue);
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