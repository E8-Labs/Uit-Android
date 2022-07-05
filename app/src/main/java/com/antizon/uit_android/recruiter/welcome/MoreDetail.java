package com.antizon.uit_android.recruiter.welcome;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.antizon.uit_android.R;
import com.antizon.uit_android.applicant.activities.FlagUser;
import com.antizon.uit_android.company.utility.BaseActivity;

public class MoreDetail extends BaseActivity {
    private static final String TAG = MoreDetail.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_detail);
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