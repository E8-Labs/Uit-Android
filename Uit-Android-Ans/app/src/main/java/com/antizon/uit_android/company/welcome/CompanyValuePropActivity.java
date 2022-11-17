package com.antizon.uit_android.company.welcome;

import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.antizon.uit_android.R;
import com.antizon.uit_android.company.utility.BaseActivity;

public class CompanyValuePropActivity extends BaseActivity {
    private static final String TAG = CompanyValuePropActivity.class.getSimpleName();

    ConstraintLayout firstLayout, secondLayout, thirdLayout  , next , getStarted , next2;
    int currentlyViewedLayout = 1;
    TextView skip,skip2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_value_prop);

        setIds();
        initialize();
        setListener();
    }
    void setIds() {
        Log.d(TAG, "setIds: ");
        next = findViewById(R.id.next);
        next2 = findViewById(R.id.next2);
        skip = findViewById(R.id.skip);
        skip2 = findViewById(R.id.skip2);
        getStarted = findViewById(R.id.getStarted);
        firstLayout = findViewById(R.id.firstLayout);
        secondLayout = findViewById(R.id.secondLayout);
        thirdLayout = findViewById(R.id.thirdLayout);
    }
    void initialize() {
        Log.d(TAG, "initialize: ");
        if (currentlyViewedLayout == 1) {
            firstLayout.setVisibility(View.VISIBLE);
            secondLayout.setVisibility(View.GONE);
            thirdLayout.setVisibility(View.GONE);
        }

    }
    void setListener() {
        Log.d(TAG, "setListener: ");

        next.setOnClickListener(v -> {
            setNextLayout();
            overridePendingTransition(R.anim.left_in,R.anim.left_out);

        });
        next2.setOnClickListener(v -> {
            overridePendingTransition(R.anim.left_in,R.anim.left_out);
            setNextLayout();

        });

        getStarted.setOnClickListener(v -> {
            Intent intent = new Intent(CompanyValuePropActivity.this, UploadCompanyLogoActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.left_in,R.anim.left_out);
        });

        skip.setOnClickListener(view -> {
            Intent intent = new Intent(CompanyValuePropActivity.this, UploadCompanyLogoActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.left_in,R.anim.left_out);
        });
        skip2.setOnClickListener(view -> {
            Intent intent = new Intent(CompanyValuePropActivity.this, UploadCompanyLogoActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.left_in,R.anim.left_out);
        });
    }

    void setNextLayout() {

        Log.d(TAG, "setNextLayout: currentlyViewedLayout: " + currentlyViewedLayout);
        if (currentlyViewedLayout == 1) {
            firstLayout.setVisibility(View.GONE);
            secondLayout.setVisibility(View.VISIBLE);
            thirdLayout.setVisibility(View.GONE);

            currentlyViewedLayout = 2;
        } else if (currentlyViewedLayout == 2) {
            firstLayout.setVisibility(View.GONE);
            secondLayout.setVisibility(View.GONE);
            thirdLayout.setVisibility(View.VISIBLE);
            currentlyViewedLayout = 3;
        } else if (currentlyViewedLayout == 3) {

            Toast.makeText(this, "Net", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }
}


