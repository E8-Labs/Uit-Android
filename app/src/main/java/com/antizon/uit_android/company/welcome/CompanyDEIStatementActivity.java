package com.antizon.uit_android.company.welcome;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.antizon.uit_android.R;
import com.antizon.uit_android.company.utility.BaseActivity;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.utilities.CustomCookieToast;
import com.antizon.uit_android.utilities.Utilities;

public class CompanyDEIStatementActivity extends BaseActivity {
    Context context;
    SessionManagement sessionManagement;

    ImageView backIcon, redNoah;
    TextView next;
    EditText deiDetail;

    String  deiStatementValue = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_deistatement);
        Utilities.setWhiteBars(CompanyDEIStatementActivity.this);
        context = CompanyDEIStatementActivity.this;
        sessionManagement = new SessionManagement(context);

        initViews();
        setListener();
    }

    private void initViews() {
        backIcon = findViewById(R.id.backIcon);
        next = findViewById(R.id.next);
        redNoah = findViewById(R.id.redNoah);
        deiDetail = findViewById(R.id.deiDetail);

        loadProfile(CompanyDEIStatementActivity.this, sessionManagement.getProfileImage(), redNoah);

    }


    private void setListener() {
        backIcon.setOnClickListener(v -> onBackPressed());

        next.setOnClickListener(v -> {
            deiStatementValue = deiDetail.getText().toString().trim();
            if (deiStatementValue.isEmpty()) {
                CustomCookieToast.showRequiredToast(CompanyDEIStatementActivity.this, "Please enter company DEI statement");
            } else {
                hideSoftKeyboard(CompanyDEIStatementActivity.this, deiDetail);
                openNextScreen();
            }
        });


    }

    private void openNextScreen() {
        Intent intent = new Intent(context, CompanyLeadActivity.class);
        intent.putExtra("deiStatement", deiStatementValue);
        startActivity(intent);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }
}