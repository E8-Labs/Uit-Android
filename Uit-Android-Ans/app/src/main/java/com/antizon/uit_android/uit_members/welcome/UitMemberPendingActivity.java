package com.antizon.uit_android.uit_members.welcome;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import com.antizon.uit_android.R;
import com.antizon.uit_android.company.utility.BaseActivity;
import com.antizon.uit_android.generic.activities.SignInActivity;
import com.antizon.uit_android.utilities.Utilities;

public class UitMemberPendingActivity extends BaseActivity {

    TextView logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uit_member_pending);
        Utilities.setWhiteBars(UitMemberPendingActivity.this);

        logout = findViewById(R.id.logout);
        logout.setOnClickListener(v -> onBackPressed());
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
        Intent intent = new Intent(UitMemberPendingActivity.this, SignInActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }
}