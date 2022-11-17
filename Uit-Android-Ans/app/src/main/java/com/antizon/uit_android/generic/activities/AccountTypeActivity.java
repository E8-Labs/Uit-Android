package com.antizon.uit_android.generic.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import com.antizon.uit_android.R;
import com.antizon.uit_android.applicant.welcome.ApplicantValuePropActivity;
import com.antizon.uit_android.company.utility.BaseActivity;
import com.antizon.uit_android.company.welcome.CompanyValuePropActivity;
import com.antizon.uit_android.recruiter.welcome.RecruiterFindCompanyActivity;
import com.antizon.uit_android.uit_members.welcome.ProfilePictureActivity;
import com.antizon.uit_android.utilities.Utilities;
import com.google.android.material.bottomsheet.BottomSheetDialog;
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

    private void setListener() {
        layoutApplicant.setOnClickListener(view -> {
            setMembersLayout();
            openNextActivity("Members");
        });


        layoutCompany.setOnClickListener(view -> {
            setCompanyLayout();
            selectUserTypeActivity();
        });

        signUpHere.setOnClickListener(v -> {
            Intent intent = new Intent(AccountTypeActivity.this, ProfilePictureActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.left_in, R.anim.left_out);
        });

    }

    private void setMembersLayout() {
        layoutApplicant.setBackgroundResource(R.drawable.full_time_border);
        layoutCompany.setBackgroundResource(R.drawable.text_here_border);
        applicantText.setTextColor(getColor(R.color.white));
        dreamJob.setTextColor(getColor(R.color.white));
        applicantImage.setColorFilter(ContextCompat.getColor(this, R.color.white));
        companyText.setTextColor(getColor(R.color.black));
        registerCompany.setTextColor(getColor(R.color.black));
        companyImage.setColorFilter(ContextCompat.getColor(this, R.color.black));

    }

    private void setCompanyLayout() {

        layoutApplicant.setBackgroundResource(R.drawable.text_here_border);
        layoutCompany.setBackgroundResource(R.drawable.full_time_border);
        applicantText.setTextColor(getColor(R.color.black));
        dreamJob.setTextColor(getColor(R.color.black));
        applicantImage.setColorFilter(ContextCompat.getColor(this, R.color.black));
        companyText.setTextColor(getColor(R.color.white));
        registerCompany.setTextColor(getColor(R.color.white));
        companyImage.setColorFilter(ContextCompat.getColor(this, R.color.white));

    }

    private void selectUserTypeActivity() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(AccountTypeActivity.this, R.style.BottomSheetTheme);
        View sheetView = LayoutInflater.from(AccountTypeActivity.this).inflate(R.layout.bottom_sheet_select_user_type, findViewById(R.id.bottom_sheet_select_user_type));

        TextView btn_selectTeamMember = sheetView.findViewById(R.id.btn_selectTeamMember);
        TextView btn_selectCompany = sheetView.findViewById(R.id.btn_selectCompany);

        btn_selectTeamMember.setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            openNextActivity("Recruiter");
        });
        btn_selectCompany.setOnClickListener(view -> {
            bottomSheetDialog.dismiss();
            openNextActivity("Company");
        });

        bottomSheetDialog.setContentView(sheetView);
        bottomSheetDialog.show();
        bottomSheetDialog.setDismissWithAnimation(true);
    }

    private void openNextActivity(String nextScreen) {

        Intent intent;
        if (nextScreen.equalsIgnoreCase("Members")) {
            intent = new Intent(AccountTypeActivity.this, ApplicantValuePropActivity.class);
        } else if (nextScreen.equalsIgnoreCase("Team Members")) {
            intent = new Intent(AccountTypeActivity.this, ApplicantValuePropActivity.class);
        } else if (nextScreen.equalsIgnoreCase("Company")){
            intent = new Intent(AccountTypeActivity.this, CompanyValuePropActivity.class);
        } else {
            intent = new Intent(AccountTypeActivity.this, RecruiterFindCompanyActivity.class);
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