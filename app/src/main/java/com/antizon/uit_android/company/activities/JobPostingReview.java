package com.antizon.uit_android.company.activities;

import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.antizon.uit_android.R;
import com.antizon.uit_android.company.utility.BaseActivity;
import com.antizon.uit_android.generic_utils.AppConstants;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class JobPostingReview extends BaseActivity {
    private static final String TAG = JobPostingReview.class.getSimpleName();

    SessionManagement sessionManagement;
    ProgressDialog progressDialog;
    ImageView flag,companyLogo;
    TextView post,congrats,nowLive,postJob;
    TextView companyInfo ,companyName,job,industry,department,address,employment,salary,education,vaccination,language,deadline,role,
    jobDescription,responsibilitiesTitle,responsibilities,experienceYear,adobe,sales;

    ConstraintLayout jobPostSuccessLayout;
    String jobTitleValue = "", industryValue = "", departmentValue = "", cityStateValue = "",
            locationStatusValue = "", employmentStatusValue = "", minSalaryValue = "",
            maxSalaryValue = "", salaryRangeValue = "", minEducationRequiredValue = "",
            languageRequirementValue = "", vaccinationRequirementValue = "", roleValue = "",
            responsibilitiesValue = "", experienceValue = "", yearsValue = ""
            , priorityValue = "Required", skillValue = "",deadLineValue="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_posting_review);
        setIds();
        getIntentData();
        initialize();
        setListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
        uploadCoverLetter();
    }

    void setIds() {
        Log.d(TAG, "setIds: ");

        experienceYear=findViewById(R.id.experienceYear);
        adobe=findViewById(R.id.adobe);
        jobDescription=findViewById(R.id.jobDescription);
        responsibilitiesTitle=findViewById(R.id.responsibilitiesTitle);
        responsibilities=findViewById(R.id.responsibilities);
        flag=findViewById(R.id.flag);
        post=findViewById(R.id.post);
        role=findViewById(R.id.role);
        companyLogo=findViewById(R.id.companyLogo);
        companyInfo = findViewById(R.id.companyInfo);
        companyName = findViewById(R.id.companyName);
        job = findViewById(R.id.job);
        industry = findViewById(R.id.industry);
        department = findViewById(R.id.department);
        address = findViewById(R.id.address);
        employment = findViewById(R.id.employment);
        education = findViewById(R.id.education);
        salary = findViewById(R.id.salary);
        vaccination = findViewById(R.id.vaccination);
        language = findViewById(R.id.language);
        deadline = findViewById(R.id.deadline);
        sales = findViewById(R.id.sales);


    }
    void getIntentData() {

        if (getIntent() != null) {
            jobTitleValue = getIntent().getStringExtra("jobTitle");
            job.setText(jobTitleValue);

            industryValue = getIntent().getStringExtra("industryTitle");
            industry.setText(industryValue);

            departmentValue = getIntent().getStringExtra("department");
            department.setText(departmentValue);

            cityStateValue = getIntent().getStringExtra("cityState");
            address.setText(cityStateValue);

            locationStatusValue = getIntent().getStringExtra("locationStatus");

            employmentStatusValue = getIntent().getStringExtra("employmentType");
            employment.setText(employmentStatusValue);

            minSalaryValue = getIntent().getStringExtra("minSalary");
            maxSalaryValue = getIntent().getStringExtra("maxSalary");
            salaryRangeValue = getIntent().getStringExtra("salaryRange");
            salary.setText(salaryRangeValue);

            minEducationRequiredValue = getIntent().getStringExtra("minEducationRequired");
            education.setText(minEducationRequiredValue);

            languageRequirementValue = getIntent().getStringExtra("languageRequirement");
            language.setText(languageRequirementValue);

            vaccinationRequirementValue = getIntent().getStringExtra("vaccinationRequirement");
            vaccination.setText(vaccinationRequirementValue);

            roleValue = getIntent().getStringExtra("role");
            responsibilitiesValue = getIntent().getStringExtra("responsibilities");

            deadLineValue = getIntent().getStringExtra("applicationDeadLine");
            deadline.setText(deadLineValue);

            roleValue = getIntent().getStringExtra("role");
            role.setText(roleValue);

            responsibilitiesValue = getIntent().getStringExtra("responsibilities");
            responsibilities.setText(responsibilitiesValue);

            experienceValue = getIntent().getStringExtra("experienceET");
            experienceYear.setText(experienceValue);

            yearsValue = getIntent().getStringExtra("experienceET");
            adobe.setText(yearsValue);

            skillValue = getIntent().getStringExtra("skillET");
            sales.setText(skillValue);

        }
    }

    void initialize() {
        Log.d(TAG, "initialize: ");
        progressDialog = new ProgressDialog(JobPostingReview.this);
        sessionManagement = new SessionManagement(JobPostingReview.this);

        if (getIntent() != null) {
            companyName.setText(jobTitleValue);
//            salaryRange.setText(salaryRangeValue);
//            cityState.setText(cityStateValue);

            showImageUsingGlide(JobPostingReview.this, companyLogo, sessionManagement.getProfileImage());
        }

    }

    void setListener() {
        Log.d(TAG, "setListener: ");
        flag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                jobPostSuccessBottomSheet();
            }
        });

        jobDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                role.setVisibility(View.VISIBLE);
                responsibilities.setVisibility(View.GONE);
            }
        });
        responsibilitiesTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                role.setVisibility(View.GONE);
                responsibilities.setVisibility(View.VISIBLE);
            }
        });
    }


    void uploadCoverLetter() {
        HashMap<String, String> params = new HashMap<String, String>();
//        params.put("cover_letter", coverLetterValue);

        Log.d(TAG, "getDocumentData: params: " + params);
        sendServerRequestPOST(AppConstants.ADD_JOB, params, sessionManagement.getToken());
    }

    @Override
    public void requestStarted() {
        super.requestStarted();
        Log.d(TAG, "requestStarted: running");
        progressDialog.setMessage(" Adding Jobs...");
        progressDialog.show();
    }

    @Override
    public void onResponseReceived(String response, String urlCalled) {
        super.onResponseReceived(response, urlCalled);
        progressDialog.dismiss();

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(response);
            Boolean status = jsonObject.getBoolean("status");
            String message = jsonObject.getString("message");
            Log.d(TAG, "onResponse: status: " + message);
            Log.d(TAG, "onResponse: status: " + status);
            Log.d(TAG, "onResponseReceived: response: " + response);

            Toast.makeText(this, "" + message, Toast.LENGTH_SHORT).show();
            if (status) {

                JSONObject dataObject = jsonObject.getJSONObject("data");
                Log.d(TAG, "onResponse: data: size: " + dataObject.length());

                int id = dataObject.getInt("id");
                Log.d(TAG, "onResponse: id " + id);

                Log.d(TAG, "onResponseReceived: response: " + response);
                Log.d(TAG, "onResponseReceived: urlCalled: " + urlCalled);


            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void requestEndedWithError(VolleyError error) {
        super.requestEndedWithError(error);
        progressDialog.dismiss();
        Log.d(TAG, "requestEndedWithError: error: " + error);
    }



    void jobPostSuccessBottomSheet() {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.job_post_success_layout);
        congrats = bottomSheetDialog.findViewById(R.id.congrats);
        nowLive = bottomSheetDialog.findViewById(R.id.nowLive);
        postJob = bottomSheetDialog.findViewById(R.id.postJob);
        jobPostSuccessLayout = bottomSheetDialog.findViewById(R.id.jobPostSuccessLayout);


        postJob.setOnClickListener(v -> bottomSheetDialog.dismiss());

        bottomSheetDialog.show();
    }

}