package com.antizon.uit_android.applicant.welcome;


import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import com.antizon.uit_android.R;
import com.antizon.uit_android.company.utility.BaseActivity;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.utilities.CustomCookieToast;
import com.antizon.uit_android.utilities.Utilities;

public class ActivityApplicantJobStatus extends BaseActivity {
    ImageView backIcon, menYellow;
    SessionManagement sessionManagement;
    TextView unEmployed, employed, notLooking, next;
    ConstraintLayout approvedLayout, pausedLayout, pendingLayout;
    String employeValue = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applicant_job_status);
        Utilities.setWhiteBars(ActivityApplicantJobStatus.this);
        initViews();
    }

    void initViews() {
        backIcon = findViewById(R.id.backIcon);
        next = findViewById(R.id.next);
        unEmployed = findViewById(R.id.unEmployed);
        menYellow = findViewById(R.id.menYellow);
        employed = findViewById(R.id.employed);
        notLooking = findViewById(R.id.notLooking);
        pausedLayout = findViewById(R.id.pausedLayout);
        pendingLayout = findViewById(R.id.pendingLayout);
        approvedLayout = findViewById(R.id.approvedLayout);

        sessionManagement = new SessionManagement(ActivityApplicantJobStatus.this);
        loadProfile(ActivityApplicantJobStatus.this, sessionManagement.getProfileImage(), menYellow);

        next.setOnClickListener(v -> openNextScreen());
        approvedLayout.setOnClickListener(v -> setApprovedOneLayout());
        pausedLayout.setOnClickListener(v -> setPausedOneLayout());
        pendingLayout.setOnClickListener(v -> setPendingOneLayout());

        backIcon.setOnClickListener(v -> onBackPressed());
    }



    void setApprovedOneLayout() {
        approvedLayout.setBackgroundResource(R.drawable.login_background);
        pausedLayout.setBackgroundResource(R.drawable.text_here_border);
        pendingLayout.setBackgroundResource(R.drawable.text_here_border);
        unEmployed.setTextColor(getColor(R.color.white));
        employed.setTextColor(getColor(R.color.black));
        notLooking.setTextColor(getColor(R.color.black));
        employeValue = "0";
    }

    void setPausedOneLayout() {

        approvedLayout.setBackgroundResource(R.drawable.text_here_border);
        pausedLayout.setBackgroundResource(R.drawable.login_background);
        pendingLayout.setBackgroundResource(R.drawable.text_here_border);
        unEmployed.setTextColor(getColor(R.color.black));
        employed.setTextColor(getColor(R.color.white));
        notLooking.setTextColor(getColor(R.color.black));
        employeValue = "1";
    }

    void setPendingOneLayout() {

        approvedLayout.setBackgroundResource(R.drawable.text_here_border);
        pausedLayout.setBackgroundResource(R.drawable.text_here_border);
        pendingLayout.setBackgroundResource(R.drawable.login_background);

        unEmployed.setTextColor(getColor(R.color.black));
        employed.setTextColor(getColor(R.color.black));
        notLooking.setTextColor(getColor(R.color.white));
        employeValue = "2";

    }

    void openNextScreen() {

        if (employeValue.isEmpty()) {
            CustomCookieToast.showRequiredToast(ActivityApplicantJobStatus.this, "Please select Employment Status");
        } else {
            Intent intent = new Intent(ActivityApplicantJobStatus.this, ApplicantEducationActivity.class);
            intent.putExtra("employeStatus", employeValue);
            onProfileUpdateLauncher.launch(intent);
            overridePendingTransition(R.anim.left_in,R.anim.left_out);
        }
    }


    ActivityResultLauncher<Intent> onProfileUpdateLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
            overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
        }
    });

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }
}