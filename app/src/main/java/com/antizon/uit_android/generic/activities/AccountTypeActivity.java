package com.antizon.uit_android.generic.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import com.antizon.uit_android.R;
import com.antizon.uit_android.applicant.welcome.ApplicantValueProp;
import com.antizon.uit_android.company.utility.BaseActivity;
import com.antizon.uit_android.company.welcome.CompanyValueProp;
import com.antizon.uit_android.uit_members.welcome.ProfilePictureActivity;
import com.antizon.uit_android.utilities.Utilities;
import com.google.android.material.card.MaterialCardView;

public class AccountTypeActivity extends BaseActivity {

    private static final String TAG = AccountTypeActivity.class.getSimpleName();
    MaterialCardView cardViewApplicant, cardViewCompany, teamMembers;
    ProgressDialog progressDialog;

    ConstraintLayout layoutApplicant, layoutTeamMembers, layoutCompany;
    ImageView applicantImage, teamMemberImage, companyImage;
    TextView applicantText, dreamJob, teamMemberText, joinCompany, companyText, registerCompany, signUpHere;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_type);
        Utilities.setWhiteBars(AccountTypeActivity.this);

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
        signUpHere = findViewById(R.id.signUpHere);
        cardViewApplicant = findViewById(R.id.cardViewApplicant);
        cardViewCompany = findViewById(R.id.cardViewCompany);
        teamMembers = findViewById(R.id.teamMembers);
        layoutApplicant = findViewById(R.id.layoutApplicant);
        layoutTeamMembers = findViewById(R.id.layoutTeamMembers);
        layoutCompany = findViewById(R.id.layoutCompany);
        applicantText = findViewById(R.id.applicantText);
        dreamJob = findViewById(R.id.dreamJob);
        teamMemberText = findViewById(R.id.teamMemberText);
        joinCompany = findViewById(R.id.joinCompany);
        companyText = findViewById(R.id.companyText);
        registerCompany = findViewById(R.id.registerCompany);

        applicantImage = findViewById(R.id.applicantImage);
        teamMemberImage = findViewById(R.id.teamMemberImage);
        companyImage = findViewById(R.id.companyImage);

    }

    void initialize() {
        Log.d(TAG, "initialize: ");
        progressDialog = new ProgressDialog(AccountTypeActivity.this);
    }

    void setListener() {
        Log.d(TAG, "setListener: ");

        cardViewApplicant.setOnClickListener(view -> {

        });

        cardViewCompany.setOnLongClickListener(view -> {
            Intent intent = new Intent(AccountTypeActivity.this, CompanyValueProp.class);
            startActivity(intent);
            overridePendingTransition(R.anim.left_in, R.anim.left_out);
            return false;
        });

        cardViewApplicant.setOnLongClickListener(view -> {
            Intent intent = new Intent(AccountTypeActivity.this, ApplicantValueProp.class);
            startActivity(intent);
            overridePendingTransition(R.anim.left_in, R.anim.left_out);
            return false;
        });

        layoutApplicant.setOnClickListener(view -> {
            Log.d(TAG, "onClick: pending: ");
            setMembersLayout();
            openNextActivity("Members");
        });


        layoutCompany.setOnClickListener(view -> {
            Log.d(TAG, "onClick: pending: ");
            setCompanyLayout();
            openNextActivity("Company");
        });

        signUpHere.setOnClickListener(v -> {
            Intent intent = new Intent(AccountTypeActivity.this, ProfilePictureActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.left_in, R.anim.left_out);
        });

    }

    void setMembersLayout() {
        layoutApplicant.setBackgroundResource(R.drawable.full_time_border);
        layoutCompany.setBackgroundResource(R.drawable.text_here_border);
        applicantText.setTextColor(getColor(R.color.white));
        dreamJob.setTextColor(getColor(R.color.white));
//        applicantImage.setImageAlpha(getResources().getColor(R.color.white));
        applicantImage.setColorFilter(ContextCompat.getColor(this, R.color.white));
        companyText.setTextColor(getColor(R.color.black));
        registerCompany.setTextColor(getColor(R.color.black));
        companyImage.setColorFilter(ContextCompat.getColor(this, R.color.black));

    }


    void setCompanyLayout() {

        layoutApplicant.setBackgroundResource(R.drawable.text_here_border);
        layoutCompany.setBackgroundResource(R.drawable.full_time_border);
        applicantText.setTextColor(getColor(R.color.black));
        dreamJob.setTextColor(getColor(R.color.black));
        applicantImage.setColorFilter(ContextCompat.getColor(this, R.color.black));
        companyText.setTextColor(getColor(R.color.white));
        registerCompany.setTextColor(getColor(R.color.white));
//        companyImage.setImageAlpha(getResources().getColor(R.color.white));
        companyImage.setColorFilter(ContextCompat.getColor(this, R.color.white));

    }

    void openNextActivity(String nextScreen) {

        Intent intent;
        if (nextScreen.equalsIgnoreCase("Members")) {
            intent = new Intent(AccountTypeActivity.this, ApplicantValueProp.class);
        } else if (nextScreen.equalsIgnoreCase("Team Members")) {
            intent = new Intent(AccountTypeActivity.this, ApplicantValueProp.class);
        } else {
            intent = new Intent(AccountTypeActivity.this, CompanyValueProp.class);
        }
        startActivity(intent);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }
}