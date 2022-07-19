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

public class CompanyDEITrainingActivity extends BaseActivity {
    Context context;
    SessionManagement sessionManagement;

    ImageView backIcon, redNoah;
    TextView yes, no, skip;
    String deiStatementValue = "", leadValue = "", ergValue = "", programmingValue = "", trainingValue = "";
    ConstraintLayout yesLayout, noLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deitraining);
        Utilities.setWhiteBars(CompanyDEITrainingActivity.this);
        context = CompanyDEITrainingActivity.this;
        sessionManagement = new SessionManagement(context);

        initViews();
        setListener();
    }


    private void initViews() {
        backIcon = findViewById(R.id.backIcon);
        yes = findViewById(R.id.yes);
        skip = findViewById(R.id.skip);
        yesLayout = findViewById(R.id.yesLayout);
        noLayout = findViewById(R.id.noLayout);
        no = findViewById(R.id.no);
        redNoah = findViewById(R.id.redNoah);

        deiStatementValue = getIntent().getStringExtra("deiStatement");
        leadValue = getIntent().getStringExtra("lead");
        ergValue = getIntent().getStringExtra("erg");
        programmingValue = getIntent().getStringExtra("programming");

        loadProfile(CompanyDEITrainingActivity.this, sessionManagement.getProfileImage() , redNoah);
    }
    private void setListener() {
        backIcon.setOnClickListener(v -> onBackPressed());

        yesLayout.setOnClickListener(v -> setYesLayout());
        noLayout.setOnClickListener(v -> setNoLayout());
        skip.setOnClickListener(v -> openNextScreen());
    }


    private void openNextScreen() {

        Intent intent = new Intent(CompanyDEITrainingActivity.this, CompanyGenderProportionActivity.class);
        intent.putExtra("deiStatement", deiStatementValue);
        intent.putExtra("lead", leadValue);
        intent.putExtra("erg", ergValue);
        intent.putExtra("programming", programmingValue);
        intent.putExtra("training", trainingValue);
        startActivity(intent);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }

    private void setYesLayout() {
        trainingValue = "true";
        yesLayout.setBackgroundResource(R.drawable.login_background);
        noLayout.setBackgroundResource(R.drawable.text_here_border);

        yes.setTextColor(getColor(R.color.white));
        no.setTextColor(getColor(R.color.black));


    }

    private void setNoLayout() {
        trainingValue = "false";
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