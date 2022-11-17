package com.antizon.uit_android.company.welcome;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.antizon.uit_android.R;
import com.antizon.uit_android.company.utility.BaseActivity;
import com.antizon.uit_android.generic.activities.SignInActivity;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.utilities.Utilities;

public class CompanyCreationCongratulationActivity extends BaseActivity {
    Context context;
    SessionManagement sessionManagement;

    TextView logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_creation_congratulation);
        Utilities.setWhiteBars(CompanyCreationCongratulationActivity.this);
        context = CompanyCreationCongratulationActivity.this;
        sessionManagement = new SessionManagement(context);
        logout = findViewById(R.id.logout);

        logout.setOnClickListener(v -> logoutUser());

    }


    private void logoutUser(){
        sessionManagement.logoutUser();
        finishAffinity();
        Intent intent = new Intent(CompanyCreationCongratulationActivity.this, SignInActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        logoutUser();
    }
}
