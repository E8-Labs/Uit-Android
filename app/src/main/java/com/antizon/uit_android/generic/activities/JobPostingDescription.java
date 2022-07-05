package com.antizon.uit_android.generic.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.antizon.uit_android.R;
import com.antizon.uit_android.company.utility.BaseActivity;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.utilities.CustomCookieToast;

public class JobPostingDescription extends BaseActivity {
    private static final String TAG = JobPostingDescription.class.getSimpleName();
    SessionManagement sessionManagement;
    ProgressDialog progressDialog;
    ImageView companyIV;
    TextView next, companyTitle, salaryRange, cityState, editJobPost;
    EditText roleET, responsibilitiesET;
    String jobTitleValue = "", industryValue = "", departmentValue = "", cityStateValue = "",
            locationStatusValue = "", employmentStatusValue = "", minSalaryValue = "",
            maxSalaryValue = "", salaryRangeValue = "", minEducationRequiredValue = "", languageRequirementValue = "",
            vaccinationRequirementValue = "", roleValue = "", responsibilitiesValue = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_posting_description);

        setIds();
        getIntentData();
        initialize();
        setListener();
    }

    void setIds() {
        Log.d(TAG, "setIds: ");
        next = findViewById(R.id.next);
        companyIV = findViewById(R.id.companyIV);
        companyTitle = findViewById(R.id.companyTitle);
        salaryRange = findViewById(R.id.salaryRange);
        cityState = findViewById(R.id.cityState);
        editJobPost = findViewById(R.id.editJobPost);

        roleET = findViewById(R.id.roleET);
        responsibilitiesET = findViewById(R.id.responsibilitiesET);
    }

    void getIntentData() {

        jobTitleValue = getIntent().getStringExtra("jobTitle");
        industryValue = getIntent().getStringExtra("industryTitle");
        departmentValue = getIntent().getStringExtra("department");
        cityStateValue = getIntent().getStringExtra("cityState");
        locationStatusValue = getIntent().getStringExtra("locationStatus");
        employmentStatusValue = getIntent().getStringExtra("employmentType");
        minSalaryValue = getIntent().getStringExtra("minSalary");
        maxSalaryValue = getIntent().getStringExtra("maxSalary");
        salaryRangeValue = getIntent().getStringExtra("salaryRange");
        minEducationRequiredValue = getIntent().getStringExtra("minEducationRequired");
        languageRequirementValue = getIntent().getStringExtra("languageRequirement");
        vaccinationRequirementValue = getIntent().getStringExtra("vaccinationRequirement");

    }

    void initialize() {
        Log.d(TAG, "initialize: ");

        sessionManagement = new SessionManagement(JobPostingDescription.this);
        progressDialog = new ProgressDialog(JobPostingDescription.this);

        companyTitle.setText(jobTitleValue);
        salaryRange.setText(salaryRangeValue);
        cityState.setText(cityStateValue);

        showImageUsingGlide(JobPostingDescription.this, companyIV, sessionManagement.getProfileImage());
    }

    void setListener() {
        Log.d(TAG, "setListener: ");
        next.setOnClickListener(v -> {
            roleValue = roleET.getText().toString().trim();
            responsibilitiesValue = responsibilitiesET.getText().toString().trim();

            if (roleValue.isEmpty()) {
                CustomCookieToast.showRequiredToast(JobPostingDescription.this, "Please enter role.");
            }
            if (responsibilitiesValue.isEmpty()) {
                CustomCookieToast.showRequiredToast(JobPostingDescription.this, "Please enter responsibilities.");
            } else {
                openNextActivity();
            }
        });

        editJobPost.setOnClickListener(view -> Toast.makeText(JobPostingDescription.this, "Working...", Toast.LENGTH_SHORT).show());
    }


    void openNextActivity() {

        Intent intent = new Intent(JobPostingDescription.this, JobPostingExperience.class);
        intent.putExtra("jobTitle", jobTitleValue);
        intent.putExtra("industryTitle", industryValue);
        intent.putExtra("department", departmentValue);
        intent.putExtra("cityState", cityStateValue);
        intent.putExtra("locationStatus", locationStatusValue);
        intent.putExtra("employmentType", employmentStatusValue);
        intent.putExtra("minSalary", minSalaryValue);
        intent.putExtra("maxSalary", maxSalaryValue);
        intent.putExtra("salaryRange", salaryRangeValue);
        intent.putExtra("minEducationRequired", minEducationRequiredValue);
        intent.putExtra("languageRequirement", languageRequirementValue);
        intent.putExtra("vaccinationRequirement", vaccinationRequirementValue);
        intent.putExtra("role", roleValue);
        intent.putExtra("responsibilities", responsibilitiesValue);
        startActivity(intent);
    }


}