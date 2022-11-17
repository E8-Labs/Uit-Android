package com.antizon.uit_android.applicant.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.antizon.uit_android.R;
import com.antizon.uit_android.activities.jobs.FlagJobUserActivity;
import com.antizon.uit_android.adapters.jobs.JobRequiredExperienceAdapter;
import com.antizon.uit_android.adapters.jobs.JobTagAdapter;
import com.antizon.uit_android.company.activities.UitCompanyProfileActivity;
import com.antizon.uit_android.company.utility.BaseActivity;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.models.applicant.GenericApplicantDataModel;
import com.antizon.uit_android.models.applicant.home.ApplicantHomeJobDataModel;
import com.antizon.uit_android.models.community.MainResponseModel;
import com.antizon.uit_android.models.jobs.JobCompanyDataModel;
import com.antizon.uit_android.models.jobs.JobDetailsDataModel;
import com.antizon.uit_android.models.jobs.JobDetailsResponseModel;
import com.antizon.uit_android.models.jobs.JobExperienceDataModel;
import com.antizon.uit_android.models.jobs.TagModel;
import com.antizon.uit_android.models.profile.NameIdModel;
import com.antizon.uit_android.network.GetDataService;
import com.antizon.uit_android.network.RetrofitClientInstance;
import com.antizon.uit_android.recruiter.welcome.CompanyTeamMemberProfileActivity;
import com.antizon.uit_android.utilities.ArfiDeveloperTime;
import com.antizon.uit_android.utilities.CustomCookieToast;
import com.antizon.uit_android.utilities.Utilities;
import com.google.android.material.tabs.TabLayout;
import com.makeramen.roundedimageview.RoundedImageView;
import java.util.ArrayList;
import java.util.List;
import co.lujun.androidtagview.TagContainerLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ActivityApplicantJobDetail extends BaseActivity {
    Context context;
    GetDataService service;
    SessionManagement sessionManagement;
    ProgressDialog progressDialog;

    ImageView ivClose, ivProfile, btnFlagJob, btnSaveJob;
    RoundedImageView postedByProfile;
    View llDescription, llRequirements, llCompany;
    TextView tvTitle, tvCompany, tvAddress, postedByName, postedByJobTitle, tvTime, tvJobDescription, tvResponsibility, tvEducation, tvAbout, tvWebsite, tvEquity, text_appliedCounts, btnApplyNow;
    TagContainerLayout tagContainerSkills;
    TabLayout tabLayout;
    RecyclerView recyclerview_jobExperience, rvTags;
    LinearLayout layout_companyDetail, btnCompanyTeamMemberDetail;

    JobDetailsDataModel job;
    ApplicantHomeJobDataModel jobDataModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applicant_job_detail);
        Utilities.setCustomStatusAndNavColor(ActivityApplicantJobDetail.this, R.color.white_dash, R.color.white_dash);

        context = ActivityApplicantJobDetail.this;
        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        progressDialog = new ProgressDialog(context);
        sessionManagement = new SessionManagement(context);

        //--------------------------------- I N I T   V I E W S ------------------------------------

        ivClose = findViewById(R.id.iv_close);
        ivProfile = findViewById(R.id.iv_profile);
        tvTitle = findViewById(R.id.tv_description);
        tvAddress = findViewById(R.id.tv_address);
        tvCompany = findViewById(R.id.tv_company);
        text_appliedCounts = findViewById(R.id.text_appliedCounts);
        btnFlagJob = findViewById(R.id.btnFlagJob);
        btnSaveJob = findViewById(R.id.btnSaveJob);
        btnApplyNow = findViewById(R.id.btnApplyNow);

        postedByProfile = findViewById(R.id.postedByProfile);
        postedByName = findViewById(R.id.postedByName);
        postedByJobTitle = findViewById(R.id.postedByJobTitle);

        tvTime = findViewById(R.id.tv_time);
        rvTags = findViewById(R.id.rv_tags);
        tabLayout = findViewById(R.id.tabLayout_tab);
        tvJobDescription = findViewById(R.id.tv_job_description);
        tvResponsibility = findViewById(R.id.tv_responsibilities);
        tvEducation = findViewById(R.id.tv_education);

        tvAbout = findViewById(R.id.tv_about);
        tvWebsite = findViewById(R.id.tv_website);
        tvEquity = findViewById(R.id.tv_equity);
        llDescription = findViewById(R.id.ll_description);
        llRequirements = findViewById(R.id.ll_Requirements);
        llCompany = findViewById(R.id.ll_company);
        tagContainerSkills = findViewById(R.id.tag_container_skills);
        recyclerview_jobExperience = findViewById(R.id.recyclerview_jobExperience);
        layout_companyDetail = findViewById(R.id.layout_companyDetail);
        btnCompanyTeamMemberDetail = findViewById(R.id.btnCompanyTeamMemberDetail);


        //------------------------------  S E T U P  &   L O A D  ----------------------------------
        jobDataModel = getIntent().getParcelableExtra("jobDataModel");

        setupTabLayout();

        progressDialog.setMessage("Loading....");
        progressDialog.show();
        getJobDetails("Bearer " + sessionManagement.getToken(), jobDataModel.getId()+"");


        //------------------------------------ A C T I O N S ---------------------------------------

        ivClose.setOnClickListener(v -> onBackPressed());


    }


    private void setupTabLayout() {
        // In Start the Description will be shown only

        llDescription.setVisibility(View.VISIBLE);
        llRequirements.setVisibility(View.GONE);
        llCompany.setVisibility(View.GONE);

        tabLayout.addTab(tabLayout.newTab().setText("Description"));
        tabLayout.addTab(tabLayout.newTab().setText("Requirements"));
        tabLayout.addTab(tabLayout.newTab().setText("Company"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        llDescription.setVisibility(View.VISIBLE);
                        llRequirements.setVisibility(View.GONE);
                        llCompany.setVisibility(View.GONE);
                        break;
                    case 1:
                        llDescription.setVisibility(View.GONE);
                        llRequirements.setVisibility(View.VISIBLE);
                        llCompany.setVisibility(View.GONE);
                        break;
                    case 2:
                        llDescription.setVisibility(View.GONE);
                        llRequirements.setVisibility(View.GONE);
                        llCompany.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        setMarginBetweenTabs(tabLayout);
    }

    private void getJobDetails(String authToken, String jobId) {
        Call<JobDetailsResponseModel> call = service.getJobDetails(authToken, jobId);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<JobDetailsResponseModel> call, @NonNull Response<JobDetailsResponseModel> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().isStatus()) {
                        job = response.body().getData();
                        setDataToViews(job);
                    } else {
                        CustomCookieToast.showFailureToast(ActivityApplicantJobDetail.this, response.body().getMessage());
                    }
                } else {
                    CustomCookieToast.showFailureToast(ActivityApplicantJobDetail.this, "Unsuccessful : " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<JobDetailsResponseModel> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                CustomCookieToast.showFailureToast(ActivityApplicantJobDetail.this,"Failure : " + t.getMessage());
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void setDataToViews(JobDetailsDataModel job) {
        if (job != null) {

            if (job.isFlagged()){
                btnFlagJob.setImageResource(R.drawable.job_flagged_ic);
            }else {
                btnFlagJob.setImageResource(R.drawable.job_not_flagged_ic);
            }

            btnFlagJob.setOnClickListener(v -> {
                if (!job.isFlagged()){
                    Intent flagJobIntent = new Intent(context, FlagJobUserActivity.class);
                    flagJobIntent.putExtra("type", "flag_job");
                    flagJobIntent.putExtra("flagImage", jobDataModel.getCompanyDataModel().getProfile_image());
                    flagJobIntent.putExtra("flagTitle", jobDataModel.getJob_title());
                    flagJobIntent.putExtra("flagId", String.valueOf(jobDataModel.getId()));
                    onJobFlaggedLauncher.launch(flagJobIntent);
                    overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
                }else {
                    CustomCookieToast.showFailureToast(ActivityApplicantJobDetail.this, "Already Flagged", "This job has already been flagged.");
                }
            });

            if (job.isSaved()){
                btnSaveJob.setImageResource(R.drawable.job_saved_ic);
            }else {
                btnSaveJob.setImageResource(R.drawable.job_not_saved_ic);
            }

            btnSaveJob.setOnClickListener(v -> {
                if (job.isSaved()){
                    job.setSaved(false);
                    btnSaveJob.setImageResource(R.drawable.job_not_saved_ic);
                }else {
                    job.setSaved(true);
                    btnSaveJob.setImageResource(R.drawable.job_saved_ic);
                }
                requestForSaveJob("Bearer " + sessionManagement.getToken(), String.valueOf(jobDataModel.getId()));
            });

            final GenericApplicantDataModel user = job.getUser();
            final JobCompanyDataModel company = job.getCompany();
            if (company != null) {
                Utilities.loadImage(context, company.getProfileImage(), ivProfile);
                tvCompany.setText(company.getName());
                tvAddress.setText(company.getCity() + ", " + company.getState());

                layout_companyDetail.setOnClickListener(v -> {
                    Intent intent = new Intent(context, UitCompanyProfileActivity.class);
                    intent.putExtra("visitProfileId", String.valueOf(company.getUserId()));
                    intent.putExtra("accountStatus", "4");
                    startActivity(intent);
                    overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
                });
            }

            if (user != null) {
                String postedBy = "Posted by " + user.getName();
                Utilities.loadImage(context, user.getProfile_image(), postedByProfile);
                postedByName.setText(postedBy);
                postedByJobTitle.setText(user.getJob_title());

                btnCompanyTeamMemberDetail.setOnClickListener(v -> {
                    Intent intent = new Intent(context, CompanyTeamMemberProfileActivity.class);
                    intent.putExtra("applicantDataModel", user);
                    startActivity(intent);
                    overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
                });

                if (user.getRole() == 1 || user.getRole() == 2){
                    postedByProfile.setVisibility(View.GONE);
                    postedByName.setVisibility(View.GONE);
                    postedByJobTitle.setVisibility(View.GONE);
                    btnCompanyTeamMemberDetail.setVisibility(View.VISIBLE);

                }else {
                    btnCompanyTeamMemberDetail.setVisibility(View.VISIBLE);
                    postedByProfile.setVisibility(View.VISIBLE);
                    postedByName.setVisibility(View.VISIBLE);
                    postedByJobTitle.setVisibility(View.VISIBLE);
                }
            }else {
                btnCompanyTeamMemberDetail.setVisibility(View.VISIBLE);
                postedByProfile.setVisibility(View.VISIBLE);
                postedByName.setVisibility(View.VISIBLE);
                postedByJobTitle.setVisibility(View.VISIBLE);
            }


            tvTitle.setText(job.getJobTitle());
            tvTime.setText(new ArfiDeveloperTime().getPrettyTimeFromCreatedAt(job.getCreatedAt()));

            String total_applications =  jobDataModel.getTotal_applications() + " Applied";

            text_appliedCounts.setText(total_applications);


            // set tags
            final List<TagModel> tags = new ArrayList<>();
            // salary range
            tags.add(new TagModel("$" + Utilities.getShortAmount(job.getMinSalary()) + " - $" + Utilities.getShortAmount(job.getMaxSalary())));
            // location status
            tags.add(new TagModel(Utilities.getLocationStatus(job.getLocationStatus())));
            // Employment type
            tags.add(new TagModel(Utilities.getEmploymentType(job.getEmploymentType())));

            rvTags.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            JobTagAdapter jobTagAdapter = new JobTagAdapter(context, tags);
            rvTags.setAdapter(jobTagAdapter);

            // set description data
            tvJobDescription.setText(job.getRoles());
            tvResponsibility.setText(job.getResponsibilities());

            // set Requirements data
            final NameIdModel degree = job.getDegree();
            if (degree != null) tvEducation.setText(degree.getName());

            // experience
            final List<JobExperienceDataModel> experience = job.getExperience();

            if (experience !=null){
                showAllExperiencesRecyclerview(recyclerview_jobExperience, experience);
            }


            // skills required
            final List<NameIdModel> skills = job.getSkills();

            if (skills != null && !skills.isEmpty()) {
                showAllSkillsRecyclerview(tagContainerSkills, skills);
            }

            // set company data
            if (company != null) {
                tvAbout.setText(company.getName());
                tvWebsite.setText(company.getWebsite());
                tvEquity.setText(company.getBio());
            }


            btnApplyNow.setOnClickListener(v -> {
                if (job.isApplied()){
                    CustomCookieToast.showFailureToast(ActivityApplicantJobDetail.this, "Already Applied!", "You have already applied for this job.");
                }else {
                    Intent intent = new Intent(ActivityApplicantJobDetail.this, ActivityApplicantApplyJob.class);
                    intent.putExtra("jobDataModel", jobDataModel);
                    onApplyJobLauncher.launch(intent);
                    overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
                }

            });
        }
    }

    private void showAllExperiencesRecyclerview(RecyclerView recyclerView, List<JobExperienceDataModel> experiencesList) {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager =  new LinearLayoutManager(this ,LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        JobRequiredExperienceAdapter jobRequiredExperienceAdapter = new JobRequiredExperienceAdapter(context, experiencesList);
        recyclerView.setAdapter(jobRequiredExperienceAdapter);
    }

    private void showAllSkillsRecyclerview(TagContainerLayout tagContainerLayout, List<NameIdModel> skillsList) {
        for (int i = 0; i < skillsList.size(); i++) {
            tagContainerLayout.addTag(skillsList.get(i).getName());
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_out_up, R.anim.slide_bottom);
    }

    ActivityResultLauncher<Intent> onJobFlaggedLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            if (job.isFlagged()){
                job.setFlagged(false);
                btnFlagJob.setImageResource(R.drawable.job_not_flagged_ic);
            }else {
                job.setFlagged(true);
                btnFlagJob.setImageResource(R.drawable.job_flagged_ic);
            }
        }
    });

    private void requestForSaveJob(String authToken, String jobId) {
        Call<MainResponseModel> call = service.saveThisJob(authToken, jobId);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<MainResponseModel> call, @NonNull Response<MainResponseModel> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()){
                    if (response.body()!=null){
                        if (!response.body().isStatus()){
                            CustomCookieToast.showFailureToast(ActivityApplicantJobDetail.this, response.body().getMessage());
                        }
                    }else {
                        CustomCookieToast.showFailureToast(ActivityApplicantJobDetail.this, response.message());
                    }
                }else {
                    CustomCookieToast.showFailureToast(ActivityApplicantJobDetail.this, response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<MainResponseModel> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                CustomCookieToast.showFailureToast(ActivityApplicantJobDetail.this, t.getLocalizedMessage());
            }
        });
    }


    ActivityResultLauncher<Intent> onApplyJobLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
            overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
        }
    });


    private void setMarginBetweenTabs(TabLayout tabLayout) {
        ViewGroup tabs = (ViewGroup) tabLayout.getChildAt(0);
        for (int i = 0; i < tabs.getChildCount() - 1; i++) {
            View tab = tabs.getChildAt(i);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) tab.getLayoutParams();
            layoutParams.setMarginEnd(25);
            tab.setLayoutParams(layoutParams);
            tabLayout.requestLayout();
        }
    }

}