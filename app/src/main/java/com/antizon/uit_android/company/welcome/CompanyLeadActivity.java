package com.antizon.uit_android.company.welcome;

import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.antizon.uit_android.R;
import com.antizon.uit_android.company.utility.BaseActivity;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.utilities.Utilities;

public class CompanyLeadActivity extends BaseActivity {
    Context context;
    SessionManagement sessionManagement;

    ImageView backIcon, redNoah;
    TextView yes, no, skip;
    ConstraintLayout yesLayout, noLayout;

    String deiStatementValue = "", leadValue = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_lead);
        Utilities.setWhiteBars(CompanyLeadActivity.this);
        context = CompanyLeadActivity.this;
        sessionManagement = new SessionManagement(context);


        initViews();
        setListener();
    }

    private void initViews() {

        backIcon = findViewById(R.id.backIcon);
        yes = findViewById(R.id.yes);
        no = findViewById(R.id.no);
        skip = findViewById(R.id.skip);
        redNoah = findViewById(R.id.redNoah);
        yesLayout = findViewById(R.id.yesLayout);
        noLayout = findViewById(R.id.noLayout);

        loadProfile(CompanyLeadActivity.this, sessionManagement.getProfileImage(), redNoah);
        deiStatementValue = getIntent().getStringExtra("deiStatement");

    }



    private void setListener() {
        backIcon.setOnClickListener(v -> onBackPressed());
        yesLayout.setOnClickListener(v -> setYesLayout());
        noLayout.setOnClickListener(v -> setNoLayout());
        skip.setOnClickListener(v -> openNextScreen());
    }

    private void openNextScreen() {
        Intent intent = new Intent(CompanyLeadActivity.this, CompanyErgActivity.class);
        intent.putExtra("deiStatement", deiStatementValue);
        intent.putExtra("lead", leadValue);
        startActivity(intent);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }

    private void setYesLayout() {
        leadValue = "true";
        yesLayout.setBackgroundResource(R.drawable.login_background);
        noLayout.setBackgroundResource(R.drawable.text_here_border);

        yes.setTextColor(getColor(R.color.white));
        no.setTextColor(getColor(R.color.black));
    }

    private void setNoLayout() {
        leadValue = "false";
        yesLayout.setBackgroundResource(R.drawable.text_here_border);
        noLayout.setBackgroundResource(R.drawable.login_background);
        yes.setTextColor(getColor(R.color.black));
        no.setTextColor(getColor(R.color.white));

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }
}