package com.antizon.uit_android.company.welcome;

import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import com.antizon.uit_android.R;
import com.antizon.uit_android.company.utility.BaseActivity;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.utilities.Utilities;

public class CompanyErgActivity extends BaseActivity {
    Context context;
    SessionManagement sessionManagement;

    ImageView backIcon, redNoah;
    TextView yes, no, skip;
    ConstraintLayout yesLayout, noLayout;

    String  deiStatementValue = "", leadValue = "", ergValue = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_erg);
        Utilities.setWhiteBars(CompanyErgActivity.this);
        context = CompanyErgActivity.this;
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

        loadProfile(CompanyErgActivity.this, sessionManagement.getProfileImage(), redNoah);
        deiStatementValue = getIntent().getStringExtra("deiStatement");
        leadValue = getIntent().getStringExtra("lead");

    }

    private void setListener() {
        backIcon.setOnClickListener(v -> onBackPressed());

        yesLayout.setOnClickListener(v -> setYesLayout());
        noLayout.setOnClickListener(v -> setNoLayout());

        skip.setOnClickListener(v -> openNextScreen());
    }

    private void setYesLayout() {
        ergValue = "true";
        yesLayout.setBackgroundResource(R.drawable.login_background);
        noLayout.setBackgroundResource(R.drawable.text_here_border);
        yes.setTextColor(getColor(R.color.white));
        no.setTextColor(getColor(R.color.black));
    }

    private void setNoLayout() {
        ergValue = "false";
        yesLayout.setBackgroundResource(R.drawable.text_here_border);
        noLayout.setBackgroundResource(R.drawable.login_background);
        yes.setTextColor(getColor(R.color.black));
        no.setTextColor(getColor(R.color.white));

    }


    private void openNextScreen() {
        Intent intent = new Intent(CompanyErgActivity.this, CompanyDEIProgrammingActivity.class);
        intent.putExtra("deiStatement", deiStatementValue);
        intent.putExtra("lead", leadValue);
        intent.putExtra("erg", ergValue);
        startActivity(intent);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }
}