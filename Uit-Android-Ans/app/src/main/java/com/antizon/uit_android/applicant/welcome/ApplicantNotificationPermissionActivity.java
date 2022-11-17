package com.antizon.uit_android.applicant.welcome;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.antizon.uit_android.R;
import com.antizon.uit_android.company.utility.BaseActivity;
import com.antizon.uit_android.generic.model.ApplicantEducationDataModel;
import com.antizon.uit_android.generic.model.ModelApplicantJobs;
import com.antizon.uit_android.generic.model.ModelCompanySize;
import com.antizon.uit_android.models.applicant.department.ApplicantDepartmentDataModel;
import com.antizon.uit_android.models.applicant.jobs.ApplicantJobDataModel;
import com.antizon.uit_android.utilities.Utilities;
import java.util.ArrayList;

public class ApplicantNotificationPermissionActivity extends BaseActivity {

    ImageView backIcon;
    TextView allowNotificationPermissions;

    ArrayList<ModelCompanySize> selectedProfessionalInterestList;
    ArrayList<ModelCompanySize> selectedCompanyInterestInSizeList;
    ArrayList<ModelCompanySize> selectedCompanyInterestInStageList;
    ArrayList<ModelApplicantJobs> locationList, interestedJobTypeList;
    ArrayList<ApplicantJobDataModel> selectedJobsList;
    ArrayList<ApplicantEducationDataModel> educationList;
    ArrayList<ApplicantDepartmentDataModel> selectedDepartmentList;

    String employeValue = "", from;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_permission);
        Utilities.setWhiteBars(ApplicantNotificationPermissionActivity.this);
        initViews();
    }

    private void initViews(){

        allowNotificationPermissions = findViewById(R.id.allowLocation);
        backIcon = findViewById(R.id.backIcon);

        from = getIntent().getStringExtra("from");
        employeValue = getIntent().getStringExtra("employeStatus");
        educationList = getIntent().getParcelableArrayListExtra("educationList");
        selectedJobsList = getIntent().getParcelableArrayListExtra("selectedJobsList");
        selectedDepartmentList = getIntent().getParcelableArrayListExtra("selectedDepartmentList");
        locationList = getIntent().getParcelableArrayListExtra("locationList");
        interestedJobTypeList = getIntent().getParcelableArrayListExtra("interestedJobTypeList");
        selectedCompanyInterestInStageList = getIntent().getParcelableArrayListExtra("selectedCompanyInterestInStageList");
        selectedCompanyInterestInSizeList = getIntent().getParcelableArrayListExtra("selectedCompanyInterestInSizeList");
        selectedProfessionalInterestList = getIntent().getParcelableArrayListExtra("selectedProfessionalInterestList");

        backIcon.setOnClickListener(v -> onBackPressed());

        allowNotificationPermissions.setOnClickListener(v -> openNextActivity());
    }


    private void openNextActivity() {
        Intent intent = new Intent(ApplicantNotificationPermissionActivity.this, ApplicantWorkExperienceActivity.class);
        intent.putExtra("from", from);
        intent.putExtra("employeStatus", employeValue);
        intent.putParcelableArrayListExtra("educationList", educationList);
        intent.putParcelableArrayListExtra("selectedJobsList", selectedJobsList);
        intent.putParcelableArrayListExtra("selectedDepartmentList", selectedDepartmentList);
        intent.putParcelableArrayListExtra("locationList", locationList);
        intent.putParcelableArrayListExtra("interestedJobTypeList", interestedJobTypeList);
        intent.putParcelableArrayListExtra("selectedCompanyInterestInStageList", selectedCompanyInterestInStageList);
        intent.putParcelableArrayListExtra("selectedCompanyInterestInSizeList", selectedCompanyInterestInSizeList);
        intent.putParcelableArrayListExtra("selectedProfessionalInterestList", selectedProfessionalInterestList);
        onProfileUpdateLauncher.launch(intent);
        overridePendingTransition(R.anim.left_in,R.anim.left_out);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }


    ActivityResultLauncher<Intent> onProfileUpdateLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
            overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
        }
    });
}