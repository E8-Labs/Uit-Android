package com.antizon.uit_android.recruiter.welcome;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.antizon.uit_android.R;
import com.antizon.uit_android.company.utility.BaseActivity;
import com.antizon.uit_android.generic.activities.SignInActivity;

public class Congratulations extends BaseActivity {
    private static final String TAG = Congratulations.class.getSimpleName();

    ProgressDialog progressDialog;
    TextView logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_congratulations);

        setIds();
        initialize();
        setListener();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    void setIds() {
        Log.d(TAG, "setIds: ");
        logout=findViewById(R.id.logout);

    }

    void initialize() {
        Log.d(TAG, "initialize: ");

        progressDialog = new ProgressDialog(Congratulations.this);
    }

    void setListener() {
        Log.d(TAG, "setListener: ");

        logout.setOnClickListener(view -> {
            Intent intent = new Intent(Congratulations.this, SignInActivity.class);
            startActivity(intent);
            progressDialog.setMessage("Logging out");
            progressDialog.show();
        });

    }


}