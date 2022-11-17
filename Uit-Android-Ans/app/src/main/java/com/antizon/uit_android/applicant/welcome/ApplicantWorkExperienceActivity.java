package com.antizon.uit_android.applicant.welcome;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.antizon.uit_android.R;
import com.antizon.uit_android.generic.adapter.ApplicantWorkExperienceAdapter;
import com.antizon.uit_android.generic.model.ApplicantEducationDataModel;
import com.antizon.uit_android.generic.model.ModelApplicantJobs;
import com.antizon.uit_android.generic.model.ModelCompanySize;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.models.applicant.degree.ApplicantDegreeDataModel;
import com.antizon.uit_android.models.applicant.department.ApplicantDepartmentDataModel;
import com.antizon.uit_android.models.applicant.jobs.ApplicantJobDataModel;
import com.antizon.uit_android.models.applicant.skills.SkillDataModel;
import com.antizon.uit_android.models.community.MainResponseModel;
import com.antizon.uit_android.models.profile.ApplicantPorfessionalExperienceRefrenceModel;
import com.antizon.uit_android.models.profile.ApplicantProfileExperienceDataModel;
import com.antizon.uit_android.models.profile.NameIdModel;
import com.antizon.uit_android.models.work.ApplicantReferenceModel;
import com.antizon.uit_android.models.work.ApplicantWorkExperienceModel;
import com.antizon.uit_android.network.GetDataService;
import com.antizon.uit_android.network.RetrofitClientInstance;
import com.antizon.uit_android.utilities.CustomCookieToast;
import com.antizon.uit_android.utilities.CustomSwipeHelper;
import com.antizon.uit_android.utilities.Utilities;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApplicantWorkExperienceActivity extends AppCompatActivity implements ApplicantWorkExperienceAdapter.ApplicantWorkExperienceAdapterCallBack {
    GetDataService service;
    Context context;

    ProgressDialog progressDialog;
    SessionManagement sessionManagement;
    ImageView plus, backIcon,menYellow;

    TextView next;

    RecyclerView experienceRecyclerView;
    ArrayList<ApplicantWorkExperienceModel> workExperiencesList;
    ApplicantWorkExperienceAdapter workExperienceAdapter;

    ArrayList<ModelCompanySize> selectedProfessionalInterestList, selectedCompanyInterestInSizeList, selectedCompanyInterestInStageList;
    ArrayList<ModelApplicantJobs> locationList, interestedJobTypeList;
    ArrayList<ApplicantJobDataModel> selectedJobsList;
    ArrayList<ApplicantEducationDataModel> educationList;
    ArrayList<ApplicantDepartmentDataModel> selectedDepartmentList;

    AlertDialog deleteDialog;

    int updatedPosition;

    String employeValue = "", from;

    List<ApplicantProfileExperienceDataModel> applicantExperiencesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_experience);
        Utilities.setWhiteBars(ApplicantWorkExperienceActivity.this);
        context = ApplicantWorkExperienceActivity.this;
        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        sessionManagement = new SessionManagement(context);
        progressDialog = new ProgressDialog(ApplicantWorkExperienceActivity.this);


        initViews();
    }


    private void initViews() {
        workExperiencesList = new ArrayList<>();

        plus = findViewById(R.id.plus);
        next = findViewById(R.id.next);
        backIcon = findViewById(R.id.backIcon);
        menYellow = findViewById(R.id.menYellow);
        experienceRecyclerView = findViewById(R.id.experienceRecyclerView);


        from = getIntent().getStringExtra("from");
        if (from.equals("edit")){
            applicantExperiencesList = new ArrayList<>();
            applicantExperiencesList = getIntent().getParcelableArrayListExtra("applicantExperiencesList");
        }else {
            employeValue = getIntent().getStringExtra("employeStatus");
            educationList = getIntent().getParcelableArrayListExtra("educationList");
            selectedJobsList = getIntent().getParcelableArrayListExtra("selectedJobsList");
            selectedDepartmentList = getIntent().getParcelableArrayListExtra("selectedDepartmentList");
            locationList = getIntent().getParcelableArrayListExtra("locationList");
            interestedJobTypeList = getIntent().getParcelableArrayListExtra("interestedJobTypeList");
            selectedCompanyInterestInStageList = getIntent().getParcelableArrayListExtra("selectedCompanyInterestInStageList");
            selectedCompanyInterestInSizeList = getIntent().getParcelableArrayListExtra("selectedCompanyInterestInSizeList");
            selectedProfessionalInterestList = getIntent().getParcelableArrayListExtra("selectedProfessionalInterestList");
        }


        Utilities.loadImage(ApplicantWorkExperienceActivity.this, sessionManagement.getProfileImage(), menYellow);

        backIcon.setOnClickListener(view -> onBackPressed());

        next.setOnClickListener(v -> {
            if (workExperiencesList.size() == 0) {
                CustomCookieToast.showRequiredToast(ApplicantWorkExperienceActivity.this, "Please add minimum one work experience");
            } else {
              if (from.equals("edit")){
                  progressDialog.setMessage("Updating...");
                  progressDialog.show();
                  requestForUpdateApplicantWorkExperience(workExperiencesList);
              }else {
                  progressDialog.setMessage("Loading...");
                  progressDialog.show();
                  requestForAddProfessionalInformation(locationList, interestedJobTypeList, educationList, selectedDepartmentList,  selectedJobsList, selectedCompanyInterestInStageList,  selectedCompanyInterestInSizeList,  selectedProfessionalInterestList, workExperiencesList, employeValue);
              }

            }

        });


        plus.setOnClickListener(v -> {
            Intent addEducationIntent = new Intent(context, ActivityAddApplicantWorkExperience.class);
            addEducationIntent.putExtra("type", "add");
            onWorkExperienceAddedLauncher.launch(addEducationIntent);
            overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
        });


        if (from.equals("edit")){
            for (int i = 0; i < applicantExperiencesList.size(); i++) {
                ApplicantProfileExperienceDataModel applicantProfileExperienceDataModel = applicantExperiencesList.get(i);
                List<SkillDataModel> skillsList = new ArrayList<>();

                if (applicantExperiencesList.get(i).getSkillsList() != null) {
                    for (int j = 0; j < applicantExperiencesList.get(i).getSkillsList().size(); j++) {
                        skillsList.add(new SkillDataModel(applicantExperiencesList.get(i).getSkillsList().get(j).getId(),applicantExperiencesList.get(i).getSkillsList().get(j).getName()));
                    }
                }

                List<ApplicantReferenceModel> referencesList = new ArrayList<>();

                if (applicantExperiencesList.get(i).getReferencesList() != null) {
                    for (int j = 0; j < applicantExperiencesList.get(i).getReferencesList().size(); j++) {
                        ApplicantPorfessionalExperienceRefrenceModel referenceModel = applicantExperiencesList.get(i).getReferencesList().get(j);

                        String[] phoneNumbers = referenceModel.getPhone().split(" ");
                        String selectedCountryCode = "", selectedPhoneNumber = "";

                        if (phoneNumbers.length >= 2){
                            selectedCountryCode = phoneNumbers[0];
                            selectedPhoneNumber = phoneNumbers[1].replace("-","");
                        }

                        referencesList.add(new ApplicantReferenceModel(referenceModel.getName(), selectedCountryCode, selectedPhoneNumber, new ApplicantJobDataModel(referenceModel.getId(), referenceModel.getJob_title())));
                    }
                }

                ApplicantWorkExperienceModel applicantWorkExperienceModel = new ApplicantWorkExperienceModel(applicantProfileExperienceDataModel.getCompany(), applicantProfileExperienceDataModel.getJob_title(), applicantProfileExperienceDataModel.getStart_date(), applicantProfileExperienceDataModel.getEnd_date(), applicantProfileExperienceDataModel.getRoles(), new ApplicantDegreeDataModel(applicantProfileExperienceDataModel.getDepartment().getId(), applicantProfileExperienceDataModel.getDepartment().getName() , "", "", "") , referencesList, skillsList);
                workExperiencesList.add(applicantWorkExperienceModel);
            }
        }

        showWorkExperienceRecyclerview(experienceRecyclerView, workExperiencesList);

        new CustomSwipeHelper(this, experienceRecyclerView) {
            @Override
            public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {
                underlayButtons.add(new CustomSwipeHelper.UnderlayButton(ApplicantWorkExperienceActivity.this,
                        "delete",
                        R.drawable.delete_job_ic,
                        Color.parseColor("#00FFFFFF"),
                        pos -> showDeleteWorkExperiencePopup(pos)
                ));
            }

        };
    }

    @SuppressLint("NotifyDataSetChanged")
    private void showDeleteWorkExperiencePopup(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ApplicantWorkExperienceActivity.this, R.style.CustomDialog);
        final View customLayout = LayoutInflater.from(ApplicantWorkExperienceActivity.this).inflate(R.layout.popup_logout_user, null);
        builder.setView(customLayout);

        TextView text_sure = customLayout.findViewById(R.id.text_sure);
        TextView text_blockThisPerson = customLayout.findViewById(R.id.text_blockThisPerson);
        TextView btn_yes = customLayout.findViewById(R.id.text_Yes);
        TextView btn_no = customLayout.findViewById(R.id.text_No);


        String deleteTest = "Delete Work Experience?";
        String areYouSure = "Are you sure you want to delete this work experience";
        String noText = "No";
        String yesText = "Yes";

        text_sure.setText(areYouSure);
        text_blockThisPerson.setText(deleteTest);

        btn_no.setText(noText);
        btn_yes.setText(yesText);


        btn_no.setOnClickListener(view -> deleteDialog.dismiss());

        btn_yes.setOnClickListener(view -> {
            deleteDialog.dismiss();
            workExperiencesList.remove(position);
            workExperienceAdapter.notifyDataSetChanged();
        });

        deleteDialog = builder.create();
        deleteDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        deleteDialog.show();
        deleteDialog.setCancelable(false);
    }

    private void showWorkExperienceRecyclerview(RecyclerView recyclerView, ArrayList<ApplicantWorkExperienceModel> workExperiencesList) {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager =  new LinearLayoutManager(this ,LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        workExperienceAdapter = new ApplicantWorkExperienceAdapter(context, workExperiencesList , this);
        recyclerView.setAdapter(workExperienceAdapter);
    }



    ActivityResultLauncher<Intent> onProfileUpdateLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
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

    @SuppressLint("NotifyDataSetChanged")
    ActivityResultLauncher<Intent> onWorkExperienceAddedLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent intent = result.getData();
            if (intent != null) {
                ApplicantWorkExperienceModel applicantWorkExperienceModel = intent.getParcelableExtra("applicantWorkExperience");
                workExperiencesList.add(applicantWorkExperienceModel);
                workExperienceAdapter.notifyDataSetChanged();
            }
        }
    });

    @Override
    public void onEditBtnClicked(int position) {
        updatedPosition = position;
        Intent addEducationIntent = new Intent(context,  ActivityAddApplicantWorkExperience.class);
        addEducationIntent.putExtra("type", "edit");
        addEducationIntent.putExtra("applicantWorkExperience", workExperiencesList.get(position));
        onUpdateWorkExperienceItemLauncher.launch(addEducationIntent);
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
    }

    @SuppressLint("NotifyDataSetChanged")
    ActivityResultLauncher<Intent> onUpdateWorkExperienceItemLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent intent = result.getData();
            if (intent != null) {
                ApplicantWorkExperienceModel applicantWorkExperienceModel = intent.getParcelableExtra("applicantWorkExperience");
                workExperiencesList.set(updatedPosition, applicantWorkExperienceModel);
                workExperienceAdapter.notifyDataSetChanged();
            }
        }
    });

    private void requestForAddProfessionalInformation(ArrayList<ModelApplicantJobs> locationList, ArrayList<ModelApplicantJobs> interestedJobTypeList, ArrayList<ApplicantEducationDataModel> educationList,  ArrayList<ApplicantDepartmentDataModel> selectedDepartmentList, ArrayList<ApplicantJobDataModel> selectedJobsList, ArrayList<ModelCompanySize> selectedCompanyInterestInStageList, ArrayList<ModelCompanySize> selectedCompanyInterestInSizeList, ArrayList<ModelCompanySize> selectedProfessionalInterestList, ArrayList<ApplicantWorkExperienceModel> workExperiencesList, String employeValue) {

        JsonArray locationInterest = new JsonArray();
        JsonArray interestedEmploymentList = new JsonArray();
        JsonArray educationJsonList = new JsonArray();
        JsonArray departmentsList = new JsonArray();
        JsonArray jobsList = new JsonArray();
        JsonArray companyStagesList = new JsonArray();
        JsonArray companySizesList = new JsonArray();
        JsonArray professionalInterestList = new JsonArray();
        JsonArray experiencesList = new JsonArray();


        for (int i = 0; i < locationList.size(); i++) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("id", locationList.get(i).getId());
            locationInterest.add(jsonObject);
        }

        for (int i = 0; i < interestedJobTypeList.size(); i++) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("id", interestedJobTypeList.get(i).getId());
            interestedEmploymentList.add(jsonObject);
        }

        for (int i = 0; i < educationList.size(); i++) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("field", educationList.get(i).getFieldOfStudy());
            jsonObject.addProperty("school_name", educationList.get(i).getSchoolName());
            jsonObject.addProperty("start_date", educationList.get(i).getStartDate());
            jsonObject.addProperty("end_date", educationList.get(i).getEndDate());

            jsonObject.addProperty("degree", educationList.get(i).getApplicantDegreeData().getId());
            educationJsonList.add(jsonObject);
        }

        for (int i = 0; i < selectedDepartmentList.size(); i++) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("department_id", selectedDepartmentList.get(i).getId());
            departmentsList.add(jsonObject);
        }

        for (int i = 0; i < selectedJobsList.size(); i++) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("job_id", selectedJobsList.get(i).getId());
            jobsList.add(jsonObject);
        }

        for (int i = 0; i < selectedCompanyInterestInStageList.size(); i++) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("stage_id", selectedCompanyInterestInStageList.get(i).getId());
            companyStagesList.add(jsonObject);
        }

        for (int i = 0; i < selectedCompanyInterestInSizeList.size(); i++) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("size_id", selectedCompanyInterestInSizeList.get(i).getId());
            companySizesList.add(jsonObject);
        }

        for (int i = 0; i < selectedProfessionalInterestList.size(); i++) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("interest_id", selectedProfessionalInterestList.get(i).getId());
            professionalInterestList.add(jsonObject);
        }

        for (int i = 0; i < workExperiencesList.size(); i++) {
            JsonArray referencesList = new JsonArray();
            JsonArray skillsList = new JsonArray();

            List<ApplicantReferenceModel> selectedReferencesList= workExperiencesList.get(i).getReferencesList();
            List<SkillDataModel> selectedSkillsList= workExperiencesList.get(i).getSkillsList();

            for (int j = 0; j < selectedReferencesList.size(); j++) {
                JsonObject referenceObject = new JsonObject();
                referenceObject.addProperty("name", selectedReferencesList.get(j).getName());
                referenceObject.addProperty("job_title", selectedReferencesList.get(j).getApplicantJobDataModel().getName());
                referenceObject.addProperty("phone", selectedReferencesList.get(j).getCountryCode() + " " + selectedReferencesList.get(j).getPhoneNumber());
                referencesList.add(referenceObject);
            }

            for (int j = 0; j < selectedSkillsList.size(); j++) {
                JsonObject skillObject = new JsonObject();
                skillObject.addProperty("skill_id", selectedSkillsList.get(j).getId());
                skillsList.add(skillObject);
            }

            JsonObject experienceObject = new JsonObject();
            experienceObject.addProperty("company", workExperiencesList.get(i).getCompanyName());
            experienceObject.addProperty("job_title", workExperiencesList.get(i).getJobTitle());
            experienceObject.addProperty("start_date", workExperiencesList.get(i).getStartDate());
            experienceObject.addProperty("end_date", workExperiencesList.get(i).getEndDate());
            experienceObject.addProperty("roles", workExperiencesList.get(i).getResponsibilities());
            experienceObject.addProperty("department", workExperiencesList.get(i).getDegreeDataModel().getId());
            experienceObject.add("references", referencesList);
            experienceObject.add("skills", skillsList);

            experiencesList.add(experienceObject);
        }

        JsonObject mainObject = new JsonObject();

        mainObject.add("location_interests", locationInterest);
        mainObject.add("employment_interests", interestedEmploymentList);
        mainObject.add("educations", educationJsonList);
        mainObject.add("departments", departmentsList);
        mainObject.add("jobs", jobsList);
        mainObject.add("stages", companyStagesList);
        mainObject.add("professional_interests", professionalInterestList);
        mainObject.add("experiences", experiencesList);
        mainObject.addProperty("employment_status", Integer.parseInt(employeValue));

        String jsonString = mainObject.toString();
        RequestBody body = RequestBody.create(jsonString, okhttp3.MediaType.parse("application/json; charset=utf-8"));


        Call<MainResponseModel> call = service.addProfessionalInfo("Bearer " + sessionManagement.getToken(), body);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NotNull Call<MainResponseModel> call, @NotNull Response<MainResponseModel> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    boolean status = response.body().isStatus();
                    if (status) {
                        sessionManagement.setApplicationStatus("3");
                        sessionManagement.setKeyProfileUpdated("true");
                        Intent intent = new Intent(ApplicantWorkExperienceActivity.this, ApplicantResumeActivity.class);
                        intent.putExtra("type", "add");
                        onProfileUpdateLauncher.launch(intent);
                        overridePendingTransition(R.anim.right_to_left,R.anim.left_to_right);
                    }else {
                        CustomCookieToast.showFailureToast(ApplicantWorkExperienceActivity.this, "Failure!", response.body().getMessage());
                    }
                } else {
                    CustomCookieToast.showFailureToast(ApplicantWorkExperienceActivity.this, "Failure!", response.message());
                }

            }

            @Override
            public void onFailure(@NotNull Call<MainResponseModel> call, @NotNull Throwable t) {
                progressDialog.dismiss();
                CustomCookieToast.showFailureToast(ApplicantWorkExperienceActivity.this, "Failure!", t.getMessage());
            }
        });
    }


    private void requestForUpdateApplicantWorkExperience(ArrayList<ApplicantWorkExperienceModel> workExperiencesList) {

        JsonArray experiencesList = new JsonArray();
        applicantExperiencesList = new ArrayList<>();

        for (int i = 0; i < workExperiencesList.size(); i++) {

            JsonArray referencesList = new JsonArray();
            JsonArray skillsList = new JsonArray();
            List<NameIdModel> applicantProfileSkills = new ArrayList<>();
            List<ApplicantPorfessionalExperienceRefrenceModel> applicantProfileRefrenceList = new ArrayList<>();

            List<ApplicantReferenceModel> selectedReferencesList= workExperiencesList.get(i).getReferencesList();
            List<SkillDataModel> selectedSkillsList= workExperiencesList.get(i).getSkillsList();

            for (int j = 0; j < selectedReferencesList.size(); j++) {
                JsonObject referenceObject = new JsonObject();
                referenceObject.addProperty("name", selectedReferencesList.get(j).getName());
                referenceObject.addProperty("job_title", selectedReferencesList.get(j).getApplicantJobDataModel().getName());
                referenceObject.addProperty("phone", selectedReferencesList.get(j).getCountryCode() + " " + selectedReferencesList.get(j).getPhoneNumber());
                referencesList.add(referenceObject);

                applicantProfileRefrenceList.add(new ApplicantPorfessionalExperienceRefrenceModel(j, selectedReferencesList.get(j).getName(), selectedReferencesList.get(j).getApplicantJobDataModel().getName(), selectedReferencesList.get(j).getCountryCode() + " " + selectedReferencesList.get(j).getPhoneNumber(), j, "", ""));
            }

            for (int j = 0; j < selectedSkillsList.size(); j++) {
                JsonObject skillObject = new JsonObject();
                skillObject.addProperty("skill_id", selectedSkillsList.get(j).getId());
                skillsList.add(skillObject);
                applicantProfileSkills.add(new NameIdModel(selectedSkillsList.get(j).getId(),selectedSkillsList.get(j).getName()));
            }

            JsonObject experienceObject = new JsonObject();
            experienceObject.addProperty("company", workExperiencesList.get(i).getCompanyName());
            experienceObject.addProperty("job_title", workExperiencesList.get(i).getJobTitle());
            experienceObject.addProperty("start_date", workExperiencesList.get(i).getStartDate());
            experienceObject.addProperty("end_date", workExperiencesList.get(i).getEndDate());
            experienceObject.addProperty("roles", workExperiencesList.get(i).getResponsibilities());
            experienceObject.addProperty("department", workExperiencesList.get(i).getDegreeDataModel().getId());
            experienceObject.add("references", referencesList);
            experienceObject.add("skills", skillsList);
            experiencesList.add(experienceObject);

            ApplicantProfileExperienceDataModel applicantProfileExperienceDataModel = new ApplicantProfileExperienceDataModel(i, workExperiencesList.get(i).getCompanyName(),
                    new NameIdModel(workExperiencesList.get(i).getDegreeDataModel().getId(),workExperiencesList.get(i).getDegreeDataModel().getName()), workExperiencesList.get(i).getJobTitle(),
                    workExperiencesList.get(i).getResponsibilities(), Integer.parseInt(sessionManagement.getKeyId()),  workExperiencesList.get(i).getStartDate(),  workExperiencesList.get(i).getEndDate(), applicantProfileSkills, applicantProfileRefrenceList);

            applicantExperiencesList.add(applicantProfileExperienceDataModel);

        }

        JsonObject mainObject = new JsonObject();
        mainObject.add("experiences", experiencesList);

        String jsonString = mainObject.toString();
        RequestBody body = RequestBody.create(jsonString, okhttp3.MediaType.parse("application/json; charset=utf-8"));
        Call<MainResponseModel> call = service.updateProfessionalInfo("Bearer " + sessionManagement.getToken(), body);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NotNull Call<MainResponseModel> call, @NotNull Response<MainResponseModel> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    boolean status = response.body().isStatus();
                    if (status) {
                        Intent intent = new Intent();
                        intent.putParcelableArrayListExtra("applicantExperiencesList", (ArrayList<? extends Parcelable>) applicantExperiencesList);
                        setResult(RESULT_OK, intent);
                        finish();
                        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                    }else {
                        CustomCookieToast.showFailureToast(ApplicantWorkExperienceActivity.this, "Failure!", response.body().getMessage());
                    }
                } else {
                    CustomCookieToast.showFailureToast(ApplicantWorkExperienceActivity.this, "Failure!", response.message());
                }

            }

            @Override
            public void onFailure(@NotNull Call<MainResponseModel> call, @NotNull Throwable t) {
                progressDialog.dismiss();
                CustomCookieToast.showFailureToast(ApplicantWorkExperienceActivity.this, "Failure!", t.getMessage());
            }
        });
    }


}