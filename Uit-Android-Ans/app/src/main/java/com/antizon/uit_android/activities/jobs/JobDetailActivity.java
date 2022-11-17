package com.antizon.uit_android.activities.jobs;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.antizon.uit_android.R;
import com.antizon.uit_android.activities.chat.MessagesActivity;
import com.antizon.uit_android.adapters.jobs.JobPeopleAdapter;
import com.antizon.uit_android.adapters.jobs.JobRequiredExperienceAdapter;
import com.antizon.uit_android.adapters.jobs.JobTagAdapter;
import com.antizon.uit_android.company.activities.postjob.CompanyPostJobActivity;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.models.applicant.GenericApplicantDataModel;
import com.antizon.uit_android.models.community.MainResponseModel;
import com.antizon.uit_android.models.jobs.JobCompanyDataModel;
import com.antizon.uit_android.models.jobs.JobDetailsDataModel;
import com.antizon.uit_android.models.jobs.JobDetailsResponseModel;
import com.antizon.uit_android.models.jobs.JobExperienceDataModel;
import com.antizon.uit_android.models.jobs.TagModel;
import com.antizon.uit_android.models.profile.NameIdModel;
import com.antizon.uit_android.network.GetDataService;
import com.antizon.uit_android.network.RetrofitClientInstance;
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

public class JobDetailActivity extends AppCompatActivity implements JobPeopleAdapter.JobPeopleAdapterCallBack {
    Context context;
    GetDataService service;
    SessionManagement sessionManagement;
    ProgressDialog progressDialog;

    ImageView ivClose, ivProfile, btnFeatureJob;
    RoundedImageView postedByProfile;
    View llDescription, llRequirements, llCompany;
    TextView tvTitle, tvCompany, tvAddress, postedByName, postedByJobTitle, tvTime, btnCloseJob, btnEditJob, tvNoPeople, tvJobDescription, tvResponsibility, tvEducation, tvAbout, tvWebsite, tvEquity;
    TagContainerLayout tagContainerSkills;
    TabLayout tabLayout;
    RecyclerView recyclerview_jobExperience, rvTags, rvPeople;

    JobDetailsDataModel job;
    String jobId;

    RelativeLayout layout_ownJob;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_detail);
        Utilities.setCustomStatusAndNavColor(JobDetailActivity.this, R.color.white, R.color.white_dash);
        context = JobDetailActivity.this;
        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        progressDialog = new ProgressDialog(context);
        sessionManagement = new SessionManagement(context);

        //--------------------------------- I N I T   V I E W S ------------------------------------

        btnFeatureJob = findViewById(R.id.btnFeatureJob);
        ivClose = findViewById(R.id.iv_close);
        ivProfile = findViewById(R.id.iv_profile);
        tvTitle = findViewById(R.id.tv_description);
        tvAddress = findViewById(R.id.tv_address);
        tvCompany = findViewById(R.id.tv_company);

        postedByProfile = findViewById(R.id.postedByProfile);
        postedByName = findViewById(R.id.postedByName);
        postedByJobTitle = findViewById(R.id.postedByJobTitle);

        tvTime = findViewById(R.id.tv_time);
        rvTags = findViewById(R.id.rv_tags);
        rvPeople = findViewById(R.id.rv_people);
        tvNoPeople = findViewById(R.id.tv_no_people);
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
        layout_ownJob = findViewById(R.id.layout_ownJob);
        btnCloseJob = findViewById(R.id.btnCloseJob);
        btnEditJob = findViewById(R.id.btnEditJob);

        //------------------------------  S E T U P  &   L O A D  ----------------------------------
        jobId = getIntent().getStringExtra("jobId");

        setupTabLayout();

        progressDialog.setMessage("Loading....");
        progressDialog.show();
        getJobDetails("Bearer " + sessionManagement.getToken(), jobId);


        //------------------------------------ A C T I O N S ---------------------------------------

        ivClose.setOnClickListener(v -> back());
    }


    private void requestForFeatureJob(String authToken, String jobId) {
        Call<MainResponseModel> call = service.featureThisJob(authToken, jobId);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<MainResponseModel> call, @NonNull Response<MainResponseModel> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()){
                    if (response.body()!=null){
                        if (!response.body().isStatus()){
                            CustomCookieToast.showFailureToast(JobDetailActivity.this, response.body().getMessage());
                        }
                    }else {
                        CustomCookieToast.showFailureToast(JobDetailActivity.this, response.message());
                    }
                }else {
                    CustomCookieToast.showFailureToast(JobDetailActivity.this, response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<MainResponseModel> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                CustomCookieToast.showFailureToast(JobDetailActivity.this, t.getLocalizedMessage());
            }
        });
    }

    private void back() {
        finish();
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
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
                        CustomCookieToast.showFailureToast(JobDetailActivity.this, response.body().getMessage());
                    }
                } else {
                    CustomCookieToast.showFailureToast(JobDetailActivity.this, "Unsuccessful : " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<JobDetailsResponseModel> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                CustomCookieToast.showFailureToast(JobDetailActivity.this,"Failure : " + t.getMessage());
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void setDataToViews(JobDetailsDataModel job) {

        final GenericApplicantDataModel user = job.getUser();
        final JobCompanyDataModel company = job.getCompany();
        if (company != null) {
            Utilities.loadImage(context, company.getProfileImage(), ivProfile);
            tvCompany.setText(company.getName());
            tvAddress.setText(company.getCity() + ", " + company.getState());

        }

        if (user != null) {
            String postedBy = "Posted by " + user.getName();
            Utilities.loadImage(context, user.getProfile_image(), postedByProfile);
            postedByName.setText(postedBy);
            postedByJobTitle.setText(user.getJob_title());

            if (user.getRole() == 1 || user.getRole() == 2){
                postedByProfile.setVisibility(View.GONE);
                postedByName.setVisibility(View.GONE);
                postedByJobTitle.setVisibility(View.GONE);

            }else {
                postedByProfile.setVisibility(View.VISIBLE);
                postedByName.setVisibility(View.VISIBLE);
                postedByJobTitle.setVisibility(View.VISIBLE);
            }
        }

        if (sessionManagement.getRole().equals("1") || sessionManagement.getRole().equals("3")){
            layout_ownJob.setVisibility(View.VISIBLE);
            btnEditJob.setText("Message");

            btnCloseJob.setOnClickListener(v -> {
                Intent intent = new Intent(context, CloseJobActivity.class);
                intent.putExtra("jobId", jobId);
                intent.putExtra("jobTitle", job.getJobTitle());
                onCloseJobLauncher.launch(intent);
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
            });

            btnEditJob.setOnClickListener(v -> {
                if (company !=null){
                    Intent intent = new Intent(context, MessagesActivity.class);
                    intent.putExtra("CHAT_ID", sessionManagement.getKeyId()+"-"+company.getUserId());
                    intent.putExtra("second_user_id", String.valueOf(company.getUserId()));
                    intent.putExtra("second_user_picture", company.getProfileImage());
                    intent.putExtra("second_user_name", company.getName());
                    startActivity(intent);
                    overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
                }
            });

        }
        else if (sessionManagement.getRole().equals("2")){
            if (company !=null){
                if (sessionManagement.getKeyId().equals(String.valueOf(company.getUserId()))){
                    layout_ownJob.setVisibility(View.VISIBLE);
                    btnCloseJob.setOnClickListener(v -> {
                        Intent intent = new Intent(context, CloseJobActivity.class);
                        intent.putExtra("jobId", jobId);
                        intent.putExtra("jobTitle", job.getJobTitle());
                        onCloseJobLauncher.launch(intent);
                        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                    });

                    btnEditJob.setOnClickListener(v -> {
                        Intent postJobIntent = new Intent(context, CompanyPostJobActivity.class);
                        postJobIntent.putExtra("from", "edit");
                        postJobIntent.putExtra("jobDetail", job);
                        onJobUpdateLauncher.launch(postJobIntent);
                        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                    });
                }else {
                    layout_ownJob.setVisibility(View.GONE);
                }
            }
        }
        else {
            if (user !=null){
                if (sessionManagement.getKeyId().equals(String.valueOf(user.getUser_id()))){
                    layout_ownJob.setVisibility(View.VISIBLE);
                    btnEditJob.setVisibility(View.VISIBLE);
                    btnCloseJob.setVisibility(View.GONE);
                }else {
                    layout_ownJob.setVisibility(View.GONE);
                }
            }
        }




        tvTitle.setText(job.getJobTitle());
        tvTime.setText(new ArfiDeveloperTime().getPrettyTimeFromCreatedAt(job.getCreatedAt()));

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

        // set people
        final List<GenericApplicantDataModel> applications = job.getApplicantsList();
        if (applications != null && !applications.isEmpty()) {
            rvPeople.setVisibility(View.VISIBLE);
            tvNoPeople.setVisibility(View.GONE);
            rvPeople.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            JobPeopleAdapter jobPeopleAdapter = new JobPeopleAdapter(context, applications, this);
            rvPeople.setAdapter(jobPeopleAdapter);
        } else {
            rvPeople.setVisibility(View.GONE);
            tvNoPeople.setVisibility(View.VISIBLE);
        }

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
        }

        if (sessionManagement.getRole().equals("1")){
            btnFeatureJob.setVisibility(View.VISIBLE);

            if (job.getFeatured() == 1){
                btnFeatureJob.setImageResource(R.drawable.job_featured_ic);
            }else {
                btnFeatureJob.setImageResource(R.drawable.job_not_featured_ic);
            }

            btnFeatureJob.setOnClickListener(v -> {
                if (job.getFeatured() == 1){
                    job.setFeatured(0);
                    btnFeatureJob.setImageResource(R.drawable.job_not_featured_ic);
                }else {
                    job.setFeatured(1);
                    btnFeatureJob.setImageResource(R.drawable.job_featured_ic);
                }
                requestForFeatureJob("Bearer " + sessionManagement.getToken(), jobId);
            });
        }else {
            btnFeatureJob.setVisibility(View.GONE);
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
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }

    ActivityResultLauncher<Intent> onCloseJobLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
            overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
        }
    });

    @Override
    public void onJobPeopleItemClicked(int position) {
        Intent intent = new Intent(context, ListedJobApplicantsListActivity.class);
        intent.putExtra("jobDataModel", job);
        onJobDetailChangedLauncher.launch(intent);
        overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
    }

    ActivityResultLauncher<Intent> onJobDetailChangedLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            if (result.getData() !=null){
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
                overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
            }
        }
    });

    ActivityResultLauncher<Intent> onJobUpdateLauncher= registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            getJobDetails("Bearer " + sessionManagement.getToken(), jobId);
        }
    });
}