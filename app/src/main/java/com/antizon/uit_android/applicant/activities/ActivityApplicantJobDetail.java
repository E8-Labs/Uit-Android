package com.antizon.uit_android.applicant.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.volley.VolleyError;
import com.antizon.uit_android.R;
import com.antizon.uit_android.activities.jobs.FlagJobUserActivity;
import com.antizon.uit_android.company.utility.BaseActivity;
import com.antizon.uit_android.generic_utils.AppConstants;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.models.applicant.home.ApplicantHomeJobDataModel;
import com.antizon.uit_android.utilities.Utilities;
import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ActivityApplicantJobDetail extends BaseActivity {
    Context context;
    private static final String TAG = ActivityApplicantJobDetail.class.getSimpleName();
    RoundedImageView reminder;
    TextView text_locationType, text_employmentStatus, pending, approved, paused, applyNow, detail, responseDetail, educationDetail, threeYear, sales, market, aboutDetail, web, jobTitle, address, dollar, twoDays, job, airBnb, btnFlagThisUser;
    ConstraintLayout firstLayout, secondLayout, thirdLayout;
    ConstraintLayout pendingLayout, approvedLayout, pausedLayout;
    ImageView btnFlag;

    SessionManagement sessionManagement;
    ProgressDialog progressDialog;
    int currentlyViewedLayout = 1;

    ApplicantHomeJobDataModel jobDataModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applicant_job_detail);
        Utilities.setCustomStatusAndNavColor(ActivityApplicantJobDetail.this, R.color.white_dash, R.color.white_dash);
        context = ActivityApplicantJobDetail.this;

        initViews();

        sendServerRequestGET(AppConstants.GET_JOB_DETAIL + jobDataModel.getId(), sessionManagement.getToken());
    }
    
    void initViews() {
        jobDataModel = getIntent().getParcelableExtra("jobDataModel");

        twoDays = findViewById(R.id.twoDays);
        job = findViewById(R.id.job);
        pending = findViewById(R.id.pending);
        approved = findViewById(R.id.approved);
        applyNow = findViewById(R.id.applyNow);
        paused = findViewById(R.id.paused);
        pendingLayout = findViewById(R.id.pendingLayout);
        approvedLayout = findViewById(R.id.approvedLayout);
        pausedLayout = findViewById(R.id.pausedLayout);
        firstLayout = findViewById(R.id.firstLayout);
        secondLayout = findViewById(R.id.secondLayout);
        thirdLayout = findViewById(R.id.thirdLayout);
        detail = findViewById(R.id.detail);
        responseDetail = findViewById(R.id.responseDetail);
        educationDetail = findViewById(R.id.educationDetail);
        threeYear = findViewById(R.id.threeYear);
        sales = findViewById(R.id.sales);
        market = findViewById(R.id.market);
        aboutDetail = findViewById(R.id.aboutDetail);
        web = findViewById(R.id.web);
        address = findViewById(R.id.address);
        jobTitle = findViewById(R.id.jobTitle);
        reminder = findViewById(R.id.reminder);
        dollar = findViewById(R.id.dollar);
        airBnb = findViewById(R.id.airBnb);
        btnFlag = findViewById(R.id.btnFlag);
        btnFlagThisUser = findViewById(R.id.btnFlagThisUser);
        text_employmentStatus = findViewById(R.id.text_employmentStatus);
        text_locationType = findViewById(R.id.text_locationType);

        pending.setText("Description");
        approved.setText("Requirement");
        paused.setText("Company");
        progressDialog = new ProgressDialog(this);
        sessionManagement = new SessionManagement(ActivityApplicantJobDetail.this);

        if (currentlyViewedLayout == 1) {
            firstLayout.setVisibility(View.VISIBLE);
            secondLayout.setVisibility(View.GONE);
            thirdLayout.setVisibility(View.GONE);
        }

        applyNow.setOnClickListener(v -> {
            Intent intent = new Intent(ActivityApplicantJobDetail.this, ActivityApplicantApplyJob.class);
            intent.putExtra("jobDataModel", jobDataModel);
            onApplyJobLauncher.launch(intent);
            overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
        });

        pendingLayout.setOnClickListener(view -> {

            firstLayout.setVisibility(View.VISIBLE);
            secondLayout.setVisibility(View.GONE);
            thirdLayout.setVisibility(View.GONE);
            setPendingLayout();
        });
        approvedLayout.setOnClickListener(view -> {
            Log.d(TAG, "onClick: approved: ");
            firstLayout.setVisibility(View.GONE);
            secondLayout.setVisibility(View.VISIBLE);
            thirdLayout.setVisibility(View.GONE);
            setApprovedLayout();
        });

        pausedLayout.setOnClickListener(view -> {
            Log.d(TAG, "onClick: paused: ");
            firstLayout.setVisibility(View.GONE);
            secondLayout.setVisibility(View.GONE);
            thirdLayout.setVisibility(View.VISIBLE);
            setPausedLayout();
        });

        btnFlag.setOnClickListener(v -> {
            Intent flagJobIntent = new Intent(context, FlagJobUserActivity.class);
            flagJobIntent.putExtra("type", "flag_job");
            flagJobIntent.putExtra("flagDataModel", jobDataModel.getCompanyDataModel());
            flagJobIntent.putExtra("jobTitle", jobDataModel.getJob_title());
            flagJobIntent.putExtra("jobId", String.valueOf(jobDataModel.getId()));
            startActivity(flagJobIntent);
            overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
        });

        btnFlagThisUser.setOnClickListener(v -> {
            Intent flagJobIntent = new Intent(context, FlagJobUserActivity.class);
            flagJobIntent.putExtra("type", "flag_user");
            flagJobIntent.putExtra("flagDataModel", jobDataModel.getUserDataModel());
            startActivity(flagJobIntent);
            overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
        });

        String locationStatus = "";
        if (jobDataModel.getLocation_status() == 1){
            locationStatus = "Remote";
        }else if (jobDataModel.getLocation_status() == 2){
            locationStatus = "Hybrid";
        }else if (jobDataModel.getLocation_status() == 3) {
            locationStatus = "InPerson";
        }
        text_locationType.setText(locationStatus);

        String employmentStatus = "";
        if (jobDataModel.getEmployment_type() == 1){
            employmentStatus = "FullTime";
        }else if (jobDataModel.getEmployment_type() == 2){
            employmentStatus = "PartTime";
        }else if (jobDataModel.getEmployment_type() == 3) {
            employmentStatus = "Contract";
        }else if (jobDataModel.getEmployment_type() == 4) {
            employmentStatus = "Freelance";
        }else if (jobDataModel.getEmployment_type() == 5) {
            employmentStatus = "Internship";
        }
        text_employmentStatus.setText(employmentStatus);

        String location =  jobDataModel.getCity() + ", " + jobDataModel.getState();
        String salaryRange = "$" +  jobDataModel.getMin_salary() + "-" + "$" +  jobDataModel.getMax_salary();
        String total_applications =  jobDataModel.getTotal_applications() + " Applied";

        address.setText(location);
        dollar.setText(salaryRange);
        job.setText(total_applications);

    }

    @Override
    public void requestStarted() {
        super.requestStarted();
        progressDialog.setMessage("Loading...");
        progressDialog.show();
    }

    @Override
    public void onResponseReceived(String response, String urlCalled) {
        super.onResponseReceived(response, urlCalled);

        Log.d(TAG, "onResponse: " + response);
        try {

            progressDialog.dismiss();
            JSONObject jsonObject = new JSONObject(response);

            boolean status = jsonObject.getBoolean("status");
            String message = jsonObject.getString("message");
            Log.d(TAG, "onResponse: " + message);
            if (status) {
                JSONObject dataObject = jsonObject.getJSONObject("data");
                Log.d(TAG, "onResponse: data: size: " + dataObject.length());
                String roles = dataObject.getString("roles");
                Log.d(TAG, "onResponse: roles " + roles);
                String job_title = dataObject.getString("job_title");
                Log.d(TAG, "onResponse: job_title " + job_title);
                String city = dataObject.getString("city");
                Log.d(TAG, "onResponse: city " + city);
                String state = dataObject.getString("state");
                Log.d(TAG, "onResponse: state " + state);
                String min_salary = dataObject.getString("min_salary");
                Log.d(TAG, "onResponse: min_salary " + min_salary);
                String total_applications = dataObject.getString("total_applications");
                Log.d(TAG, "onResponse: total_applications " + total_applications);
                String created_at = dataObject.getString("created_at");
                Log.d(TAG, "onResponse: created_at " + created_at);

                detail.setText(roles);
                jobTitle.setText(job_title);


                String responsibilities = dataObject.getString("responsibilities");
                Log.d(TAG, "onResponse: responsibilities " + responsibilities);
                responseDetail.setText(responsibilities);

                JSONObject degreeObject = dataObject.getJSONObject("degree");
                Log.d(TAG, "onResponse: degree: size: " + degreeObject.length());
                String name = degreeObject.getString("name");
                Log.d(TAG, "onResponse: name " + name);
                educationDetail.setText(name);

                JSONObject industryObject = dataObject.getJSONObject("industry");
                Log.d(TAG, "onResponse: industry: size: " + industryObject.length());
                name = industryObject.getString("name");
                Log.d(TAG, "onResponse: name " + name);
                airBnb.setText(name);

                JSONArray experienceArray = dataObject.getJSONArray("experience");
                Log.d(TAG, "onResponse: experience: size: " + experienceArray.length());


                for (int i = 0; i < experienceArray.length(); i++) {

                    JSONObject jsonObject1 = experienceArray.getJSONObject(i);
                    String years = jsonObject1.getString("years");
                    Log.d(TAG, "onResponse: years " + years);

                    threeYear.setText(years + " +years");
                }


                JSONArray skillsArray = dataObject.getJSONArray("skills");
                Log.d(TAG, "onResponse: skills: size: " + skillsArray.length());

                for (int i = 0; i < skillsArray.length(); i++) {

                    JSONObject jsonObject1 = skillsArray.getJSONObject(i);
                    name = jsonObject1.getString("name");
                    Log.d(TAG, "onResponse: years " + name);
                    sales.setText(name);

                }

                JSONObject userObject = dataObject.getJSONObject("user");
                Log.d(TAG, "onResponse: user: size: " + userObject.length());
                String bio = userObject.getString("bio");
                Log.d(TAG, "onResponse: bio " + bio);
                String website = userObject.getString("website");
                Log.d(TAG, "onResponse: website " + website);
                String address = userObject.getString("address");
                Log.d(TAG, "onResponse: address " + address);

                String profile_image = userObject.getString("profile_image");
                Log.d(TAG, "onResponse: profile_image " + profile_image);
                aboutDetail.setText(bio);

                web.setText(website);


                Glide.with(ActivityApplicantJobDetail.this)
                        .load(profile_image)
                        .into(reminder);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void requestEndedWithError(VolleyError error) {
        super.requestEndedWithError(error);
        progressDialog.dismiss();
        Log.d(TAG, "onErrorResponse: error: " + error);
    }

    void setPendingLayout() {
        pendingLayout.setBackgroundResource(R.drawable.app_color_curved_background);
        approvedLayout.setBackgroundResource(R.drawable.white_curved_background);
        pausedLayout.setBackgroundResource(R.drawable.white_curved_background);
        pending.setTextColor(getColor(R.color.white));
        approved.setTextColor(getColor(R.color.black));
        paused.setTextColor(getColor(R.color.black));
    }

    void setApprovedLayout() {
        pendingLayout.setBackgroundResource(R.drawable.white_curved_background);
        approvedLayout.setBackgroundResource(R.drawable.app_color_curved_background);
        pausedLayout.setBackgroundResource(R.drawable.white_curved_background);
        pending.setTextColor(getColor(R.color.black));
        approved.setTextColor(getColor(R.color.white));
        paused.setTextColor(getColor(R.color.black));
    }

    void setPausedLayout() {
        pendingLayout.setBackgroundResource(R.drawable.white_curved_background);
        approvedLayout.setBackgroundResource(R.drawable.white_curved_background);
        pausedLayout.setBackgroundResource(R.drawable.app_color_curved_background);
        pending.setTextColor(getColor(R.color.black));
        approved.setTextColor(getColor(R.color.black));
        paused.setTextColor(getColor(R.color.white));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }

    ActivityResultLauncher<Intent> onApplyJobLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
            overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
        }
    });
}