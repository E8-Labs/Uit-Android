package com.antizon.uit_android.recruiter.welcome;


import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import com.antizon.uit_android.R;
import com.antizon.uit_android.company.utility.BaseActivity;
import com.antizon.uit_android.generic.activities.SignInActivity;
import com.antizon.uit_android.utilities.Utilities;

public class RecruiterCongratulationsActivity extends BaseActivity {
    TextView logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recruiter_congratulations);

        Utilities.setWhiteBars(RecruiterCongratulationsActivity.this);
        logout=findViewById(R.id.logout);

        logout.setOnClickListener(view -> {
            finishAffinity();
            Intent intent = new Intent(RecruiterCongratulationsActivity.this, SignInActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
        });
    }


    @Override
    public void onBackPressed() {

    }
}