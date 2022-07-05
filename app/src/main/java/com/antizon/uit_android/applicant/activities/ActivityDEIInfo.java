package com.antizon.uit_android.applicant.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.antizon.uit_android.R;
import com.antizon.uit_android.company.utility.BaseActivity;
import com.antizon.uit_android.utilities.Utilities;

public class ActivityDEIInfo extends BaseActivity {
    private static final String TAG = ActivityDEIInfo.class.getSimpleName();
    ImageView grayBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deiinfo);
        Utilities.setCustomStatusAndNavColor(ActivityDEIInfo.this, R.color.app_color, R.color.white);
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
        grayBack=findViewById(R.id.grayBack);

    }

    void initialize() {
        Log.d(TAG, "initialize: ");


    }

    void setListener() {
        Log.d(TAG, "setListener: ");

        grayBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();

                overridePendingTransition(R.anim.right_in, R.anim.right_out);
            }
        });

    }


}