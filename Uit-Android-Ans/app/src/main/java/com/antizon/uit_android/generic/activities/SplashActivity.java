package com.antizon.uit_android.generic.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import com.antizon.uit_android.R;
import com.antizon.uit_android.activities.home.ApplicantBottomNavigationActivity;
import com.antizon.uit_android.activities.home.CompanyTeamMemberBottomNavigationActivity;
import com.antizon.uit_android.company.utility.BaseActivity;
import com.antizon.uit_android.activities.home.CompanyBottomNavigationActivity;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.activities.home.UitAdminDashboardActivity;
import com.antizon.uit_android.activities.home.UitMemberMainDashboardActivity;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends BaseActivity {
    SessionManagement sessionManagement;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initialize();
        runThreadForFewSeconds();
    }


    void initialize() {
        sessionManagement = new SessionManagement(SplashActivity.this);
    }

    void runThreadForFewSeconds() {
        if (sessionManagement.isLoggedIn()) {
            new Handler(Looper.getMainLooper()).postDelayed(() -> openNextActivity(Integer.parseInt(sessionManagement.getRole())), 500);
        }else{
            new Handler(Looper.getMainLooper()).postDelayed(() -> moveToNextScreen(SignUpActivity.class), 500);
        }
    }

    void openNextActivity(int role) {
        if (role == 1) {
            moveToNextScreen(UitAdminDashboardActivity.class);
        } else if (role == 2) {
            moveToNextScreen(CompanyBottomNavigationActivity.class);
        } else if (role == 3) {
            moveToNextScreen(UitMemberMainDashboardActivity.class);
        } else if (role == 4) {
            moveToNextScreen(CompanyTeamMemberBottomNavigationActivity.class);
        } else if (role == 5) {
            moveToNextScreen(ApplicantBottomNavigationActivity.class);
        }
    }

    private void moveToNextScreen(Class<?> className){
        Intent intent = new Intent(SplashActivity.this, className);
        startActivity(intent);
        overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
        finish();
    }


}