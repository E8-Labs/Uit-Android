package com.antizon.uit_android.uit_members.welcome;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import com.antizon.uit_android.R;
import com.antizon.uit_android.company.utility.BaseActivity;
import com.antizon.uit_android.generic.activities.SignInActivity;
import com.antizon.uit_android.utilities.Utilities;

public class UitMemberPendingActivity extends BaseActivity {
    private static final String TAG = UitMemberPendingActivity.class.getSimpleName();

    TextView logout;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uit_member_pending);
        Utilities.setWhiteBars(UitMemberPendingActivity.this);

        setIds();
        initialize();
        setListener();
    }

    void setIds() {
        logout = findViewById(R.id.logout);
    }

    void initialize() {
        Log.d(TAG, "initialize: ");
        progressDialog = new ProgressDialog(UitMemberPendingActivity.this);
    }

    void setListener() {
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