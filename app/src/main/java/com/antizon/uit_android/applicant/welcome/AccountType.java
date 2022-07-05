package com.antizon.uit_android.applicant.welcome;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.antizon.uit_android.R;
import com.antizon.uit_android.company.utility.BaseActivity;
import com.antizon.uit_android.company.welcome.CompanyValueProp;
import com.antizon.uit_android.generic.activities.AccountTypeActivity;
import com.google.android.material.card.MaterialCardView;

public class AccountType extends BaseActivity {

    private static final String TAG = AccountTypeActivity.class.getSimpleName();
    MaterialCardView cardViewApplicant;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_type2);

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
        cardViewApplicant = findViewById(R.id.cardViewApplicant);

    }
    void initialize(){
        Log.d(TAG, "initialize: ");

        progressDialog = new ProgressDialog(AccountType.this);
    }
    void setListener()
    {
        Log.d(TAG, "setListener: ");

        cardViewApplicant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AccountType.this, CompanyValueProp.class);
                startActivity(intent);
                overridePendingTransition(R.anim.left_in,R.anim.left_out);
            }
        });
    }

}