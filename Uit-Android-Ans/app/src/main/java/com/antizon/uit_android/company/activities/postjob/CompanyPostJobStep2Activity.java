package com.antizon.uit_android.company.activities.postjob;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.antizon.uit_android.R;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.models.applicant.degree.ApplicantDegreeDataModel;
import com.antizon.uit_android.models.applicant.filter.MultiSelectionModel;
import com.antizon.uit_android.models.jobs.JobDetailsDataModel;
import com.antizon.uit_android.utilities.CustomCookieToast;
import com.antizon.uit_android.utilities.Utilities;
import com.makeramen.roundedimageview.RoundedImageView;

public class CompanyPostJobStep2Activity extends AppCompatActivity {
    Context context;
    SessionManagement sessionManagement;

    RelativeLayout btnBack;
    RoundedImageView company_profileImage;
    TextView btnNext, text_companyName, text_salaryRange, text_location;
    EditText editText_jobRole, editText_jobResponsibilities;


    MultiSelectionModel selectedLocation, selectedEmployment;
    ApplicantDegreeDataModel selectedIndustryModel, selectedDepartmentModel, selectedDegreeModel, selectedLanguageModel;
    double latitude, longitude;
    boolean isEducationRequired, isLanguageRequired, isVaccinationRequired;
    String jobTitle, jobLocation, city, state, from;
    int minSalary, maxSalary;

    JobDetailsDataModel jobDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_post_job_step2);
        Utilities.setWhiteBars(CompanyPostJobStep2Activity.this);
        context = CompanyPostJobStep2Activity.this;
        sessionManagement = new SessionManagement(context);

        initViews();
    }

    private void initViews(){
        getIntentData();
        btnNext = findViewById(R.id.btnNext);
        btnBack = findViewById(R.id.btnBack);
        company_profileImage = findViewById(R.id.company_profileImage);
        text_companyName = findViewById(R.id.text_companyName);
        text_salaryRange = findViewById(R.id.text_salaryRange);
        text_location = findViewById(R.id.text_location);
        editText_jobRole = findViewById(R.id.editText_jobRole);
        editText_jobResponsibilities = findViewById(R.id.editText_jobResponsibilities);

        btnBack.setOnClickListener(v -> onBackPressed());
        Utilities.loadImage(context, sessionManagement.getProfileImage(), company_profileImage);

        String salaryRange = "$" + minSalary + "k - $" + maxSalary + "k";
        text_companyName.setText(sessionManagement.getUserName());
        text_salaryRange.setText(salaryRange);
        text_location.setText(jobLocation);

        btnNext.setOnClickListener(v -> {
            String jobRole = editText_jobRole.getText().toString();
            String jobResponsibilities = editText_jobResponsibilities.getText().toString();
            if (jobRole.isEmpty()){
                CustomCookieToast.showRequiredToast(CompanyPostJobStep2Activity.this, "Please enter job role");
            }else if (jobResponsibilities.isEmpty()){
                CustomCookieToast.showRequiredToast(CompanyPostJobStep2Activity.this, "Please enter job responsibilities");
            }else {
                Utilities.hideKeyboard(editText_jobResponsibilities, context);
                moveToNextScreen(jobRole, jobResponsibilities);
            }
        });

        if (from.equals("edit")){
            editText_jobRole.setText(jobDetails.getRoles());
            editText_jobResponsibilities.setText(jobDetails.getResponsibilities());
        }
    }

    private void getIntentData(){
        if (getIntent() !=null){
            from = getIntent().getStringExtra("from");

            jobTitle = getIntent().getStringExtra("jobTitle");
            selectedIndustryModel = getIntent().getParcelableExtra("jobIndustry");
            selectedDepartmentModel = getIntent().getParcelableExtra("jobDepartment");
            jobLocation = getIntent().getStringExtra("jobLocation");
            city = getIntent().getStringExtra("city");
            state = getIntent().getStringExtra("state");
            latitude = getIntent().getDoubleExtra("latitude", 0);
            longitude = getIntent().getDoubleExtra("longitude", 0);
            selectedLocation = getIntent().getParcelableExtra("selectedLocation");
            selectedEmployment = getIntent().getParcelableExtra("selectedEmployment");
            minSalary = getIntent().getIntExtra("minSalary", 0);
            maxSalary = getIntent().getIntExtra("maxSalary", 0);
            selectedDegreeModel = getIntent().getParcelableExtra("jobMinEducation");
            isEducationRequired = getIntent().getBooleanExtra("isEducationRequired", false);
            selectedLanguageModel = getIntent().getParcelableExtra("selectedLanguageModel");
            isLanguageRequired = getIntent().getBooleanExtra("isLanguageRequired", false);
            isVaccinationRequired = getIntent().getBooleanExtra("isVaccinationRequired", false);

            if (from.equals("edit")){
                jobDetails = getIntent().getParcelableExtra("jobDetail");
            }
        }
    }

    private void moveToNextScreen(String jobRole, String jobResponsibilities){
        Intent jobPostIntent = new Intent(context, CompanyPostJobStep3Activity.class);
        jobPostIntent.putExtra("from", from);
        jobPostIntent.putExtra("jobTitle", jobTitle);
        jobPostIntent.putExtra("jobIndustry", selectedIndustryModel);
        jobPostIntent.putExtra("jobDepartment", selectedDepartmentModel);
        jobPostIntent.putExtra("jobLocation", jobLocation);
        jobPostIntent.putExtra("city", city);
        jobPostIntent.putExtra("state", state);
        jobPostIntent.putExtra("latitude", latitude);
        jobPostIntent.putExtra("longitude", longitude);
        jobPostIntent.putExtra("selectedLocation", selectedLocation);
        jobPostIntent.putExtra("selectedEmployment", selectedEmployment);
        jobPostIntent.putExtra("minSalary", minSalary);
        jobPostIntent.putExtra("maxSalary", maxSalary);
        jobPostIntent.putExtra("jobMinEducation", selectedDegreeModel);
        jobPostIntent.putExtra("isEducationRequired", isEducationRequired);
        jobPostIntent.putExtra("selectedLanguageModel", selectedLanguageModel);
        jobPostIntent.putExtra("isLanguageRequired", isLanguageRequired);
        jobPostIntent.putExtra("isVaccinationRequired", isVaccinationRequired);
        jobPostIntent.putExtra("jobRole", jobRole);
        jobPostIntent.putExtra("jobResponsibilities", jobResponsibilities);

        if (from.equals("edit")){
            jobPostIntent.putExtra("jobDetail", jobDetails);
        }

        onJobPostedLauncher.launch(jobPostIntent);
        overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
    }

    ActivityResultLauncher<Intent> onJobPostedLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
            overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
        }
    });

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }
}