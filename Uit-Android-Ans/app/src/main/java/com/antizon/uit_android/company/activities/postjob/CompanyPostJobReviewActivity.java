package com.antizon.uit_android.company.activities.postjob;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.antizon.uit_android.R;
import com.antizon.uit_android.adapters.company.CompanyPostJobExperienceAdapter;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.models.applicant.degree.ApplicantDegreeDataModel;
import com.antizon.uit_android.models.applicant.filter.MultiSelectionModel;
import com.antizon.uit_android.models.applicant.skills.SkillDataModel;
import com.antizon.uit_android.models.community.MainResponseModel;
import com.antizon.uit_android.models.company.JobExperienceModel;
import com.antizon.uit_android.models.jobs.JobDetailsDataModel;
import com.antizon.uit_android.network.GetDataService;
import com.antizon.uit_android.network.RetrofitClientInstance;
import com.antizon.uit_android.utilities.CustomCookieToast;
import com.antizon.uit_android.utilities.Utilities;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.makeramen.roundedimageview.RoundedImageView;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import co.lujun.androidtagview.TagContainerLayout;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompanyPostJobReviewActivity extends AppCompatActivity implements CompanyPostJobExperienceAdapter.CompanyPostJobExperienceAdapterCallBack {
    Context context;
    GetDataService service;
    SessionManagement sessionManagement;
    ProgressDialog progressDialog;

    RelativeLayout btnBack;
    RoundedImageView company_profileImage;
    TextView btnCreate, text_companyName, text_jobTitle, text_jobLocation, text_jobIndustry, text_jobDepartment, text_jobEmploymentStatus, text_salaryRange, text_minEducation, text_vaccinationRequired, text_languageName, text_descriptionResponsibility, text_applicationDeadline;
    TabLayout tab_layout;
    RecyclerView recyclerview_experience;
    TagContainerLayout tagContainerSkills;

    ArrayList<SkillDataModel> skillsList;

    ArrayList<JobExperienceModel> experiencesList;
    CompanyPostJobExperienceAdapter experienceAdapter;
    MultiSelectionModel selectedLocation, selectedEmployment;
    ApplicantDegreeDataModel selectedIndustryModel, selectedDepartmentModel, selectedDegreeModel, selectedLanguageModel;
    double latitude, longitude;
    boolean isEducationRequired, isLanguageRequired, isVaccinationRequired;
    String jobTitle, jobLocation, city, state, jobRole, jobResponsibilities, applicationDeadLineDate = "", from;
    AlertDialog successDialog;
    int minSalary, maxSalary;

    JobDetailsDataModel jobDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_post_job_review);
        Utilities.setCustomStatusAndNavColor(CompanyPostJobReviewActivity.this, R.color.white_dash, R.color.white_dash);
        context = CompanyPostJobReviewActivity.this;
        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        sessionManagement = new SessionManagement(context);
        progressDialog = new ProgressDialog(context);

        initViews();
    }

    private void initViews(){
        getIntentData();
        btnBack = findViewById(R.id.btnBack);
        btnCreate = findViewById(R.id.btnCreate);
        company_profileImage = findViewById(R.id.company_profileImage);
        text_companyName = findViewById(R.id.text_companyName);
        text_jobTitle = findViewById(R.id.text_jobTitle);
        text_jobLocation = findViewById(R.id.text_jobLocation);
        text_jobIndustry = findViewById(R.id.text_jobIndustry);
        text_jobDepartment = findViewById(R.id.text_jobDepartment);
        text_jobEmploymentStatus = findViewById(R.id.text_jobEmploymentStatus);
        text_salaryRange = findViewById(R.id.text_salaryRange);
        text_minEducation = findViewById(R.id.text_minEducation);
        text_vaccinationRequired = findViewById(R.id.text_vaccinationRequired);
        text_languageName = findViewById(R.id.text_languageName);
        text_descriptionResponsibility = findViewById(R.id.text_descriptionResponsibility);
        text_applicationDeadline = findViewById(R.id.text_applicationDeadline);
        tab_layout = findViewById(R.id.tab_layout);
        recyclerview_experience = findViewById(R.id.recyclerview_experience);
        tagContainerSkills = findViewById(R.id.tag_container_skills);

        Utilities.loadImage(context, sessionManagement.getProfileImage(), company_profileImage);

        String salaryRange = "$" + minSalary+ "k - $" + maxSalary + "k";
        text_companyName.setText(sessionManagement.getUserName());
        text_salaryRange.setText(salaryRange);
        text_jobLocation.setText(jobLocation);
        text_jobTitle.setText(jobTitle);
        text_jobIndustry.setText(selectedIndustryModel.getName());
        text_jobDepartment.setText(selectedDepartmentModel.getName());
        text_jobEmploymentStatus.setText(selectedEmployment.getTitle());
        text_minEducation.setText(selectedDegreeModel.getName());
        text_languageName.setText(selectedLanguageModel.getName());
        if (isVaccinationRequired){
            text_vaccinationRequired.setText(context.getString(R.string.required));
        }else {
            text_vaccinationRequired.setText(context.getString(R.string.notRequired));
        }


        text_descriptionResponsibility.setText(jobRole);
        tab_layout.addTab(tab_layout.newTab().setText("Job Description"), true);
        tab_layout.addTab(tab_layout.newTab().setText("Responsibilities"));
        tab_layout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        text_descriptionResponsibility.setText(jobRole);
                        break;
                    case 1:
                        text_descriptionResponsibility.setText(jobResponsibilities);
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
        text_applicationDeadline.setText(applicationDeadLineDate);

        showAllExperiencesRecyclerview(recyclerview_experience, experiencesList);
        showAllSkillsRecyclerview(tagContainerSkills, skillsList);

        btnCreate.setOnClickListener(v -> {
            if (from.equals("edit")){
                progressDialog.setMessage("Updating Job.......");
                progressDialog.show();
                requestForUpdateJob();
            }else {
                progressDialog.setMessage("Creating Job.......");
                progressDialog.show();
                requestForAddJob();
            }

        });
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
            jobRole = getIntent().getStringExtra("jobRole");
            jobResponsibilities = getIntent().getStringExtra("jobResponsibilities");
            applicationDeadLineDate = getIntent().getStringExtra("applicationDeadLineDate");
            experiencesList = getIntent().getParcelableArrayListExtra("experiencesList");
            skillsList = getIntent().getParcelableArrayListExtra("skillsList");

            if (from.equals("edit")){
                jobDetails = getIntent().getParcelableExtra("jobDetail");
            }
        }
    }

    private void showAllSkillsRecyclerview(TagContainerLayout tagContainerLayout, ArrayList<SkillDataModel> skillsList) {
        for (int i = 0; i < skillsList.size(); i++) {
            tagContainerLayout.addTag(skillsList.get(i).getName());
        }
    }

    private void showAllExperiencesRecyclerview(RecyclerView recyclerView, ArrayList<JobExperienceModel> experiencesList) {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager =  new LinearLayoutManager(this ,LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        experienceAdapter = new CompanyPostJobExperienceAdapter(context, experiencesList , "detail",this);
        recyclerView.setAdapter(experienceAdapter);
    }

    @Override
    public void onRemoveClicked(int position) {
    }

    private void requestForAddJob() {

        JsonArray experienceArray = new JsonArray();
        JsonArray skillsArray = new JsonArray();

        for (int i = 0; i < experiencesList.size(); i++) {
            JsonObject experienceObject = new JsonObject();
            // add the exercise category to Json
            experienceObject.addProperty("industry_id", experiencesList.get(i).getSelectedIndustryModel().getId());
            experienceObject.addProperty("years", Integer.parseInt(experiencesList.get(i).getTotalExperience()));
            if (experiencesList.get(i).isRequired()){
                experienceObject.addProperty("requirement_status", 1);
            }else {
                experienceObject.addProperty("requirement_status", 2);
            }

            experienceArray.add(experienceObject);
        }

        for (int i = 0; i < skillsList.size(); i++) {
            JsonObject skillsObject = new JsonObject();
            // add the exercise category to Json
            skillsObject.addProperty("skill_id", skillsList.get(i).getId());
            skillsArray.add(skillsObject);
        }

        JsonObject mainObject = new JsonObject();

        mainObject.addProperty("city", city);
        mainObject.addProperty("state", state);
        mainObject.addProperty("degree_id", selectedDegreeModel.getId());
        if (isEducationRequired){
            mainObject.addProperty("degree_required", 1);
        }else{
            mainObject.addProperty("degree_required", 0);
        }
        mainObject.addProperty("department_id", selectedDepartmentModel.getId());
        mainObject.addProperty("employment_type", selectedEmployment.getValue());
        mainObject.add("experience", experienceArray);

        mainObject.addProperty("industry_id", selectedIndustryModel.getId());
        mainObject.addProperty("job_title", jobTitle);
        mainObject.addProperty("lat", latitude);
        mainObject.addProperty("lang", longitude);
        mainObject.addProperty("language_id", selectedLanguageModel.getId());
        if (isLanguageRequired){
            mainObject.addProperty("language_required", 1);
        }else{
            mainObject.addProperty("language_required", 0);
        }
        mainObject.addProperty("location_status", selectedLocation.getValue());
        mainObject.addProperty("min_salary", minSalary*1000);
        mainObject.addProperty("max_salary", maxSalary*1000);
        mainObject.addProperty("responsibilities", jobResponsibilities);
        mainObject.addProperty("roles", jobRole);
        mainObject.add("skills", skillsArray);

        if (isVaccinationRequired){
            mainObject.addProperty("vaccination_status", 1);
        }else {
            mainObject.addProperty("vaccination_status", 0);
        }

        String jsonString = mainObject.toString();
        RequestBody body = RequestBody.create(jsonString, okhttp3.MediaType.parse("application/json; charset=utf-8"));

        Call<MainResponseModel> call = service.addJob("Bearer " + sessionManagement.getToken(), body);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NotNull Call<MainResponseModel> call, @NotNull Response<MainResponseModel> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    boolean status = response.body().isStatus();
                    if (status) {
                        showJobSuccessFullyCreatedPopup();
                    }else {
                        CustomCookieToast.showFailureToast(CompanyPostJobReviewActivity.this, "Failure!", response.body().getMessage());
                    }
                } else {
                    CustomCookieToast.showFailureToast(CompanyPostJobReviewActivity.this, "Failure!", response.message());
                }

            }

            @Override
            public void onFailure(@NotNull Call<MainResponseModel> call, @NotNull Throwable t) {
                progressDialog.dismiss();
                CustomCookieToast.showFailureToast(CompanyPostJobReviewActivity.this, "Failure!", t.getMessage());
            }
        });
    }

    private void showJobSuccessFullyCreatedPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(CompanyPostJobReviewActivity.this, R.style.CustomDialog);
        final View customLayout = LayoutInflater.from(CompanyPostJobReviewActivity.this).inflate(R.layout.popup_job_posted_successfully, null);
        builder.setView(customLayout);

        TextView btnGotIt = customLayout.findViewById(R.id.btnGotIt);

        btnGotIt.setOnClickListener(view -> {
            successDialog.dismiss();
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
            overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
        });

        successDialog = builder.create();
        successDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        successDialog.show();
        successDialog.setCancelable(false);
    }


    private void requestForUpdateJob() {

        JsonArray experienceArray = new JsonArray();
        JsonArray skillsArray = new JsonArray();

        for (int i = 0; i < experiencesList.size(); i++) {
            JsonObject experienceObject = new JsonObject();
            // add the exercise category to Json
            experienceObject.addProperty("industry_id", experiencesList.get(i).getSelectedIndustryModel().getId());
            experienceObject.addProperty("years", Integer.parseInt(experiencesList.get(i).getTotalExperience()));
            if (experiencesList.get(i).isRequired()){
                experienceObject.addProperty("requirement_status", 1);
            }else {
                experienceObject.addProperty("requirement_status", 2);
            }

            experienceArray.add(experienceObject);
        }

        for (int i = 0; i < skillsList.size(); i++) {
            JsonObject skillsObject = new JsonObject();
            // add the exercise category to Json
            skillsObject.addProperty("skill_id", skillsList.get(i).getId());
            skillsArray.add(skillsObject);
        }

        JsonObject mainObject = new JsonObject();

        mainObject.addProperty("job_id", jobDetails.getId());
        mainObject.addProperty("city", city);
        mainObject.addProperty("state", state);
        mainObject.addProperty("degree_id", selectedDegreeModel.getId());
        if (isEducationRequired){
            mainObject.addProperty("degree_required", 1);
        }else{
            mainObject.addProperty("degree_required", 0);
        }
        mainObject.addProperty("department_id", selectedDepartmentModel.getId());
        mainObject.addProperty("employment_type", selectedEmployment.getValue());
        mainObject.add("experience", experienceArray);

        mainObject.addProperty("industry_id", selectedIndustryModel.getId());
        mainObject.addProperty("job_title", jobTitle);
        mainObject.addProperty("lat", latitude);
        mainObject.addProperty("lang", longitude);
        mainObject.addProperty("language_id", selectedLanguageModel.getId());
        if (isLanguageRequired){
            mainObject.addProperty("language_required", 1);
        }else{
            mainObject.addProperty("language_required", 0);
        }
        mainObject.addProperty("location_status", selectedLocation.getValue());
        mainObject.addProperty("min_salary", minSalary*1000);
        mainObject.addProperty("max_salary", maxSalary*1000);
        mainObject.addProperty("responsibilities", jobResponsibilities);
        mainObject.addProperty("roles", jobRole);
        mainObject.add("skills", skillsArray);

        if (isVaccinationRequired){
            mainObject.addProperty("vaccination_status", 1);
        }else {
            mainObject.addProperty("vaccination_status", 0);
        }

        String jsonString = mainObject.toString();
        RequestBody body = RequestBody.create(jsonString, okhttp3.MediaType.parse("application/json; charset=utf-8"));

        Call<MainResponseModel> call = service.editJob("Bearer " + sessionManagement.getToken(), body);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NotNull Call<MainResponseModel> call, @NotNull Response<MainResponseModel> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    boolean status = response.body().isStatus();
                    if (status) {
                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
                        finish();
                        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                    }else {
                        CustomCookieToast.showFailureToast(CompanyPostJobReviewActivity.this, "Failure!", response.body().getMessage());
                    }
                } else {
                    CustomCookieToast.showFailureToast(CompanyPostJobReviewActivity.this, "Failure!", response.message());
                }

            }

            @Override
            public void onFailure(@NotNull Call<MainResponseModel> call, @NotNull Throwable t) {
                progressDialog.dismiss();
                CustomCookieToast.showFailureToast(CompanyPostJobReviewActivity.this, "Failure!", t.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }


}