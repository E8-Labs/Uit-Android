package com.antizon.uit_android.applicant.welcome;

import androidx.constraintlayout.widget.ConstraintLayout;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.antizon.uit_android.R;
import com.antizon.uit_android.company.utility.BaseActivity;


public class ApplicantValuePropActivity extends BaseActivity {
    private static final String TAG = ApplicantValuePropActivity.class.getSimpleName();
    ConstraintLayout firstLayout, secondLayout, thirdLayout, next, getStarted, next2;
    TextView skip,skipTwo;
    int currentlyViewedLayout = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applicant_value_prop);

        setIds();
        initialize();
        setListener();
    }

    void setIds() {
        Log.d(TAG, "setIds: ");
        next = findViewById(R.id.next);
        skip = findViewById(R.id.skip);
        skipTwo = findViewById(R.id.skipTwo);
        next2 = findViewById(R.id.next2);
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

        next.setOnClickListener(v -> setNextLayout());
        next2.setOnClickListener(v -> setNextLayout());

        getStarted.setOnClickListener(v -> goToNextScreen());
        skip.setOnClickListener(v -> goToNextScreen());
        skipTwo.setOnClickListener(v -> goToNextScreen());
    }

    void setNextLayout() {

        Log.d(TAG, "setNextLayout: currentlyViewedLayout: " + currentlyViewedLayout);
        if (currentlyViewedLayout == 1) {
            firstLayout.setVisibility(View.GONE);
            secondLayout.setVisibility(View.VISIBLE);
            thirdLayout.setVisibility(View.GONE);
            overridePendingTransition(R.anim.left_in, R.anim.left_out);
            currentlyViewedLayout = 2;
        } else if (currentlyViewedLayout == 2) {
            firstLayout.setVisibility(View.GONE);
            secondLayout.setVisibility(View.GONE);
            thirdLayout.setVisibility(View.VISIBLE);
            currentlyViewedLayout = 3;
        } else if (currentlyViewedLayout == 3) {

            Intent intent = new Intent(ApplicantValuePropActivity.this, ApplicantPersonalInfoActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }

    private void goToNextScreen(){
        Intent intent = new Intent(ApplicantValuePropActivity.this, ApplicantPersonalInfoActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }
}