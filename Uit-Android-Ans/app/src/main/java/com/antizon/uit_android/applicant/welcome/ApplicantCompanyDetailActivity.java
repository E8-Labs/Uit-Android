package com.antizon.uit_android.applicant.welcome;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import com.antizon.uit_android.R;
import com.antizon.uit_android.company.utility.BaseActivity;
import com.antizon.uit_android.generic.model.ApplicantEducationDataModel;
import com.antizon.uit_android.generic.model.ModelApplicantJobs;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.models.applicant.department.ApplicantDepartmentDataModel;
import com.antizon.uit_android.models.applicant.jobs.ApplicantJobDataModel;
import com.antizon.uit_android.utilities.Utilities;
import java.util.ArrayList;

public class ApplicantCompanyDetailActivity extends BaseActivity {
    ProgressDialog progressDialog;
    TextView skip;
    ImageView backIcon;
    SessionManagement sessionManagement;
    ConstraintLayout asianLayout, blackLayout, nativeLayout, whiteLayout, pacificLayout, latinoLayout, preferLayout, internshipLayout;
    CheckBox remote_checkbox, hybrid_checkbox, in_personCheckBox, checkbox_fullTime, checkbox_partTime, contract_checkBox, freelance_checkBox, internship_checkbox;

    ImageView redNoah2;

    ArrayList<ModelApplicantJobs> locationList, interestedJobTypeList;

    ArrayList<ApplicantJobDataModel> selectedJobsList;
    ArrayList<ApplicantEducationDataModel> educationList;
    ArrayList<ApplicantDepartmentDataModel> selectedDepartmentList;

    String employeValue = "", from;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applicant_company_detail);
        Utilities.setWhiteBars(ApplicantCompanyDetailActivity.this);

        initViews();
    }

    private void initViews() {
        skip = findViewById(R.id.skip);
        backIcon = findViewById(R.id.backIcon);
        redNoah2 = findViewById(R.id.redNoah2);
        asianLayout = findViewById(R.id.asianLayout);
        internshipLayout = findViewById(R.id.internshipLayout);
        blackLayout = findViewById(R.id.blackLayout);
        nativeLayout = findViewById(R.id.nativeLayout);
        whiteLayout = findViewById(R.id.whiteLayout);
        pacificLayout = findViewById(R.id.pacificLayout);
        latinoLayout = findViewById(R.id.latinoLayout);
        preferLayout = findViewById(R.id.preferLayout);
        remote_checkbox = findViewById(R.id.remote_checkbox);
        hybrid_checkbox = findViewById(R.id.hybrid_checkbox);
        in_personCheckBox = findViewById(R.id.in_personCheckBox);
        checkbox_fullTime = findViewById(R.id.checkbox_fullTime);
        checkbox_partTime = findViewById(R.id.checkbox_partTime);
        contract_checkBox = findViewById(R.id.contract_checkBox);
        freelance_checkBox = findViewById(R.id.freelance_checkBox);
        internship_checkbox = findViewById(R.id.internship_checkbox);


        from = getIntent().getStringExtra("from");
        employeValue = getIntent().getStringExtra("employeStatus");
        educationList = getIntent().getParcelableArrayListExtra("educationList");
        selectedJobsList = getIntent().getParcelableArrayListExtra("selectedJobsList");
        selectedDepartmentList = getIntent().getParcelableArrayListExtra("selectedDepartmentList");

        sessionManagement = new SessionManagement(ApplicantCompanyDetailActivity.this);
        loadProfile(ApplicantCompanyDetailActivity.this, sessionManagement.getProfileImage(), redNoah2);
        progressDialog = new ProgressDialog(ApplicantCompanyDetailActivity.this);
        locationList = new ArrayList<>();
        interestedJobTypeList = new ArrayList<>();

        skip.setOnClickListener(v -> getLocationList());

        backIcon.setOnClickListener(view -> onBackPressed());

        asianLayout.setOnClickListener(v -> remote_checkbox.setChecked(!remote_checkbox.isChecked()));

        blackLayout.setOnClickListener(v -> hybrid_checkbox.setChecked(!hybrid_checkbox.isChecked()));

        nativeLayout.setOnClickListener(v -> in_personCheckBox.setChecked(!in_personCheckBox.isChecked()));

        whiteLayout.setOnClickListener(v -> checkbox_fullTime.setChecked(!checkbox_fullTime.isChecked()));

        pacificLayout.setOnClickListener(v -> checkbox_partTime.setChecked(!checkbox_partTime.isChecked()));

        latinoLayout.setOnClickListener(v -> contract_checkBox.setChecked(!contract_checkBox.isChecked()));
        preferLayout.setOnClickListener(v -> freelance_checkBox.setChecked(!freelance_checkBox.isChecked()));

        internshipLayout.setOnClickListener(v -> internship_checkbox.setChecked(!internship_checkbox.isChecked()));

    }

    private void getLocationList() {
        locationList = new ArrayList<>();
        if (remote_checkbox.isChecked()) {
            ModelApplicantJobs remoteJobSelected = new ModelApplicantJobs(1, "Remote", true);
            locationList.add(remoteJobSelected);
        }
        if (hybrid_checkbox.isChecked()) {
            ModelApplicantJobs hybridJobSelected =  new ModelApplicantJobs(2, "Hybrid", true);
            locationList.add(hybridJobSelected);
        }
        if (in_personCheckBox.isChecked()) {
            ModelApplicantJobs inPersonJobSelected =  new ModelApplicantJobs(3, "InPerson", true);
            inPersonJobSelected.setName("InPerson");
            locationList.add(inPersonJobSelected);
        }
        getInterestedJobTypeList();
    }

    private void getInterestedJobTypeList() {
        interestedJobTypeList = new ArrayList<>();
        if (checkbox_fullTime.isChecked()) {
            ModelApplicantJobs fullTimeJobSelected = new ModelApplicantJobs(1, "Full Time", true);
            interestedJobTypeList.add(fullTimeJobSelected);
        }
        if (checkbox_partTime.isChecked()) {
            ModelApplicantJobs partTimeJobSelected = new ModelApplicantJobs(2, "Part Time", true);
            interestedJobTypeList.add(partTimeJobSelected);
        }
        if (contract_checkBox.isChecked()) {
            ModelApplicantJobs contractJobSelected = new ModelApplicantJobs(3, "Contract", true);
            interestedJobTypeList.add(contractJobSelected);
        }
        if (freelance_checkBox.isChecked()) {
            ModelApplicantJobs freeLanceJobSelected = new ModelApplicantJobs(4, "FreeLance", true);
            interestedJobTypeList.add(freeLanceJobSelected);
        }
        if (internship_checkbox.isChecked()) {
            ModelApplicantJobs internshipJobSelected = new ModelApplicantJobs(5, "Internship", true);
            interestedJobTypeList.add(internshipJobSelected);
        }
        openNextScreen();
    }

    private void openNextScreen() {

        Intent intent = new Intent(ApplicantCompanyDetailActivity.this, ApplicantInterestInStageActivity.class);
        intent.putExtra("from", from);
        intent.putExtra("employeStatus", employeValue);
        intent.putParcelableArrayListExtra("educationList", educationList);
        intent.putParcelableArrayListExtra("selectedJobsList", selectedJobsList);
        intent.putParcelableArrayListExtra("selectedDepartmentList", selectedDepartmentList);
        intent.putParcelableArrayListExtra("locationList", locationList);
        intent.putParcelableArrayListExtra("interestedJobTypeList", interestedJobTypeList);
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