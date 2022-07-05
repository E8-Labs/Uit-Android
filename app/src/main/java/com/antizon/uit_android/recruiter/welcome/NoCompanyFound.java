package com.antizon.uit_android.recruiter.welcome;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.antizon.uit_android.R;
import com.antizon.uit_android.company.utility.BaseActivity;

import java.util.ArrayList;

public class NoCompanyFound extends BaseActivity {

    private static final String TAG = NoCompanyFound.class.getSimpleName();
    ProgressDialog progressDialog;
    TextView createCompanyProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_company_found);
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
        createCompanyProfile= findViewById(R.id.createCompanyProfile);


    }

    void initialize() {
        Log.d(TAG, "initialize: ");
        progressDialog = new ProgressDialog(NoCompanyFound.this);


    }

    void setListener() {
        Log.d(TAG, "setListener: ");


        createCompanyProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NoCompanyFound.this, ProfilePic.class);
                startActivity(intent);
            }
        });
    }
}