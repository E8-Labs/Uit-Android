package com.antizon.uit_android.applicant.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.antizon.uit_android.R;
import com.antizon.uit_android.activities.ImageViewerActivity;
import com.antizon.uit_android.activities.chat.MessagesActivity;
import com.antizon.uit_android.activities.jobs.FlagJobUserActivity;
import com.antizon.uit_android.activities.jobs.TeamMemberHiredActivity;
import com.antizon.uit_android.adapters.applicant.ApplicantProfessionalEducationAdapter;
import com.antizon.uit_android.adapters.applicant.ApplicantProfileExperienceAdapter;
import com.antizon.uit_android.applicant.welcome.ActivityApplicantJobStatus;
import com.antizon.uit_android.applicant.welcome.ApplicantEducationActivity;
import com.antizon.uit_android.applicant.welcome.ApplicantProfessionalInterestActivity;
import com.antizon.uit_android.applicant.welcome.ApplicantWorkExperienceActivity;
import com.antizon.uit_android.applicant.welcome.SelectApplicantJobsActivity;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.models.community.MainResponseModel;
import com.antizon.uit_android.models.profile.ApplicantProfileEducationDataModel;
import com.antizon.uit_android.models.profile.ApplicantProfileExperienceDataModel;
import com.antizon.uit_android.models.profile.ApplicantProfileProfessionalInfoDataModel;
import com.antizon.uit_android.models.profile.NameIdModel;
import com.antizon.uit_android.models.profile.ProfileResponseDataModel;
import com.antizon.uit_android.network.GetDataService;
import com.antizon.uit_android.network.RetrofitClientInstance;
import com.antizon.uit_android.utilities.CustomCookieToast;
import com.antizon.uit_android.utilities.Utilities;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import co.lujun.androidtagview.TagContainerLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApplicantProfessionalActivity extends AppCompatActivity {
    Context context;
    GetDataService service;
    SessionManagement sessionManagement;
    ProgressDialog progressDialog;


    // ---------------------------------------- V I E W S ------------------------------------------
    ImageView ivCover, ivProfile, ivClose, ivSave;
    TextView tvName, tvProfession, tvEmploymentStatus, btnReject, btnHire, btnEditEmployment, btnEditEducation, btnEditJobRole, btnEditInterest, btnEditExperience, tvFlagUser;
    TagContainerLayout tagJobs, tagInterests;
    RecyclerView rvEducation, rvExperience;
    RelativeLayout layout_hiring;

    // -------------------------------------- V A R I A B L E S ------------------------------------
    String applicantId, applicationStatus, jobId;
    ProfileResponseDataModel data;

    AlertDialog deleteDialog;

    List<ApplicantProfileEducationDataModel> applicantEducationList;
    List<NameIdModel> applicantJobsList, applicantInterestsList;
    List<ApplicantProfileExperienceDataModel> applicantExperiencesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applicant_professional);
        Utilities.setCustomStatusAndNavColor(ApplicantProfessionalActivity.this, R.color.app_color, R.color.white_dash);
        context = ApplicantProfessionalActivity.this;
        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        sessionManagement = new SessionManagement(context);
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading...");


        //--------------------------------- I N I T   V I E W S ------------------------------------
        ivCover = findViewById(R.id.iv_cover);
        ivProfile = findViewById(R.id.iv_profile);
        ivClose = findViewById(R.id.iv_close);
        ivSave = findViewById(R.id.iv_bookmark);
        tvName = findViewById(R.id.tv_name);
        tvProfession = findViewById(R.id.tv_profession);
        tagJobs = findViewById(R.id.tag_container_jobs);
        tagInterests = findViewById(R.id.tag_container_interest);
        tvEmploymentStatus = findViewById(R.id.tv_employment_status);
        tvFlagUser = findViewById(R.id.tv_flag);
        rvEducation = findViewById(R.id.rv_education);
        rvExperience = findViewById(R.id.rv_experience);
        layout_hiring = findViewById(R.id.layout_hiring);
        btnHire = findViewById(R.id.btnHire);
        btnReject = findViewById(R.id.btnReject);

        btnEditEmployment = findViewById(R.id.btnEditEmployment);
        btnEditEducation = findViewById(R.id.btnEditEducation);
        btnEditJobRole = findViewById(R.id.btnEditJobRole);
        btnEditInterest = findViewById(R.id.btnEditInterest);
        btnEditExperience = findViewById(R.id.btnEditExperience);


        //------------------------------  S E T U P  &   L O A D  ----------------------------------

        applicantId = getIntent().getStringExtra("applicantId");
        jobId = getIntent().getStringExtra("jobId");
        applicationStatus = getIntent().getStringExtra("applicationStatus");
        data = getIntent().getParcelableExtra("data");


        applicantEducationList = new ArrayList<>();
        applicantJobsList = new ArrayList<>();
        applicantInterestsList = new ArrayList<>();
        applicantExperiencesList = new ArrayList<>();
        setDataToViews();


        //------------------------------------ A C T I O N S ---------------------------------------
        ivClose.setOnClickListener(v -> onBackPressed());

        tvFlagUser.setOnClickListener(v -> {
            Intent flagJobIntent = new Intent(context, FlagJobUserActivity.class);
            flagJobIntent.putExtra("type", "flag_user");
            flagJobIntent.putExtra("flagImage", data.getProfile_image());
            flagJobIntent.putExtra("flagTitle", data.getName());
            flagJobIntent.putExtra("flagId", String.valueOf(data.getUser_id()));
            startActivity(flagJobIntent);
            overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
        });

    }


    private void setDataToViews() {
        if (data == null) return;

        Utilities.loadImage(context, data.getProfile_image(), ivProfile);
        tvName.setText(data.getName());
        tvProfession.setText(data.getJob_title());

        ivProfile.setOnClickListener(v -> {
            Intent intent = new Intent(context, ImageViewerActivity.class);
            ViewCompat.setTransitionName(ivProfile, data.getProfile_image());
            String animationName = ViewCompat.getTransitionName(ivProfile);
            intent.putExtra("animationName", animationName);
            intent.putExtra("IMAGE", data.getProfile_image());
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(ApplicantProfessionalActivity.this, ivProfile, Objects.requireNonNull(animationName));
            startActivity(intent, options.toBundle());
        });

        // employment status
        final ApplicantProfileProfessionalInfoDataModel professionalInfo = data.getProfessionalInfoDataModel();
        if (professionalInfo != null) {
            final int employment_status = professionalInfo.getEmployment_status();
            tvEmploymentStatus.setText(Utilities.getApplicantEmploymentStatus(employment_status));

            btnEditEmployment.setOnClickListener(v -> {
                Intent intent = new Intent(context, ActivityApplicantJobStatus.class);
                intent.putExtra("from", "edit");
                intent.putExtra("employmentStatus", String.valueOf(employment_status));
                onApplicantEmploymentStatusUpdateLauncher.launch(intent);
                overridePendingTransition(R.anim.right_to_left,R.anim.left_to_right);
            });
        }


        // Education
        if (data.getEducationList() !=null){
            applicantEducationList = data.getEducationList();
            showEducationRecyclerView(rvEducation, applicantEducationList);
            btnEditEducation.setOnClickListener(v -> {
                Intent intent = new Intent(context, ApplicantEducationActivity.class);
                intent.putExtra("from", "edit");
                intent.putParcelableArrayListExtra("applicantEducationList", (ArrayList<? extends Parcelable>) applicantEducationList);
                onApplicantEducationUpdateLauncher.launch(intent);
                overridePendingTransition(R.anim.right_to_left,R.anim.left_to_right);
            });
        }


        // jobs
        if (data.getJobsList() !=null){
            applicantJobsList = data.getJobsList();
            setJobTags(applicantJobsList);

            btnEditJobRole.setOnClickListener(v -> {
                Intent intent = new Intent(context, SelectApplicantJobsActivity.class);
                intent.putExtra("from", "edit");
                intent.putParcelableArrayListExtra("applicantJobsList", (ArrayList<? extends Parcelable>) applicantJobsList);
                onApplicantJobsUpdateLauncher.launch(intent);
                overridePendingTransition(R.anim.right_to_left,R.anim.left_to_right);
            });
        }


        // interests
        if (data.getProfessionalInterestsList() !=null){
            applicantInterestsList = data.getProfessionalInterestsList();
            setInterestTags(applicantInterestsList);

            btnEditInterest.setOnClickListener(v -> {
                Intent intent = new Intent(context, ApplicantProfessionalInterestActivity.class);
                intent.putExtra("from", "edit");
                intent.putParcelableArrayListExtra("applicantInterestsList", (ArrayList<? extends Parcelable>) applicantInterestsList);
                onApplicantInterestsUpdateLauncher.launch(intent);
                overridePendingTransition(R.anim.right_to_left,R.anim.left_to_right);
            });
        }

        // Experience
        if (data.getExperienceList() !=null){
            applicantExperiencesList = data.getExperienceList();
            showExperiencesRecyclerView(rvExperience, applicantExperiencesList);

            btnEditExperience.setOnClickListener(v -> {
                Intent intent = new Intent(context, ApplicantWorkExperienceActivity.class);
                intent.putExtra("from", "edit");
                intent.putParcelableArrayListExtra("applicantExperiencesList", (ArrayList<? extends Parcelable>) applicantExperiencesList);
                onApplicantWorkExperienceUpdateLauncher.launch(intent);
                overridePendingTransition(R.anim.right_to_left,R.anim.left_to_right);
            });


        }


        if (sessionManagement.getRole().equals("1")  || sessionManagement.getRole().equals("3")){
            layout_hiring.setVisibility(View.VISIBLE);
            btnReject.setText(context.getString(R.string.delete));
            btnHire.setText(context.getString(R.string.message));

            btnReject.setOnClickListener(v -> showDeleteApplicantPopup(applicantId));

            btnHire.setOnClickListener(v -> {
                Intent intent = new Intent(context, MessagesActivity.class);
                intent.putExtra("CHAT_ID", sessionManagement.getKeyId()+"-"+data.getUser_id());
                intent.putExtra("second_user_id", String.valueOf(data.getUser_id()));
                intent.putExtra("second_user_picture", data.getProfile_image());
                intent.putExtra("second_user_name", data.getName());
                startActivity(intent);
                overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
            });

            btnEditEmployment.setVisibility(View.GONE);
            btnEditEducation.setVisibility(View.GONE);
            btnEditJobRole.setVisibility(View.GONE);
            btnEditInterest.setVisibility(View.GONE);
            btnEditExperience.setVisibility(View.GONE);

        } else if (sessionManagement.getRole().equals("2")){
            if (applicationStatus.equals("1")){
                layout_hiring.setVisibility(View.VISIBLE);
                btnHire.setText(context.getString(R.string.hire));
                btnReject.setText(context.getString(R.string.reject));

                btnHire.setOnClickListener(v -> showHireBottomSheet());

                btnReject.setOnClickListener(v -> showRejectApplicantPopup());
            }else {
                layout_hiring.setVisibility(View.GONE);
            }

            tvFlagUser.setVisibility(View.VISIBLE);

            btnEditEmployment.setVisibility(View.GONE);
            btnEditEducation.setVisibility(View.GONE);
            btnEditJobRole.setVisibility(View.GONE);
            btnEditInterest.setVisibility(View.GONE);
            btnEditExperience.setVisibility(View.GONE);
        }else {
            if (sessionManagement.getKeyId().equals(String.valueOf(data.getUser_id()))){
                tvFlagUser.setVisibility(View.GONE);

                btnEditEmployment.setVisibility(View.VISIBLE);
                btnEditEducation.setVisibility(View.VISIBLE);
                btnEditJobRole.setVisibility(View.VISIBLE);
                btnEditInterest.setVisibility(View.VISIBLE);
                btnEditExperience.setVisibility(View.VISIBLE);

            }else {
                tvFlagUser.setVisibility(View.VISIBLE);
                btnEditEmployment.setVisibility(View.GONE);
                btnEditEducation.setVisibility(View.GONE);
                btnEditJobRole.setVisibility(View.GONE);
                btnEditInterest.setVisibility(View.GONE);
                btnEditExperience.setVisibility(View.GONE);
            }

            layout_hiring.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        if (sessionManagement.getKeyId().equals(String.valueOf(data.getUser_id()))){
            Intent intent = new Intent();
            intent.putExtra("data", data);
            setResult(RESULT_OK, intent);
            finish();
        }else {
            super.onBackPressed();
        }
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);


    }

    private void showHireBottomSheet() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(ApplicantProfessionalActivity.this, R.style.BottomSheetTheme);
        View sheetView = LayoutInflater.from(ApplicantProfessionalActivity.this).inflate(R.layout.bottom_sheet_hire_options, findViewById(R.id.bottom_sheet_hire_options));

        RelativeLayout btnHireAndContinue = sheetView.findViewById(R.id.btnHireAndContinue);
        RelativeLayout btnHireAndClose = sheetView.findViewById(R.id.btnHireAndClose);
        RelativeLayout btn_cancel = sheetView.findViewById(R.id.btn_cancel);

        btnHireAndContinue.setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            progressDialog.show();
            requestForRejectApplicant("Bearer " + sessionManagement.getToken(), jobId, applicantId, "accept");
        });

        btnHireAndClose.setOnClickListener(view -> {
            bottomSheetDialog.dismiss();
            progressDialog.show();
            requestForRejectApplicant("Bearer " + sessionManagement.getToken(), jobId, applicantId, "close");
        });

        btn_cancel.setOnClickListener(view -> bottomSheetDialog.dismiss());

        bottomSheetDialog.setContentView(sheetView);
        bottomSheetDialog.show();
        bottomSheetDialog.setDismissWithAnimation(true);
    }

    private void showRejectApplicantPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ApplicantProfessionalActivity.this, R.style.CustomDialog);
        final View customLayout = LayoutInflater.from(ApplicantProfessionalActivity.this).inflate(R.layout.popup_logout_user, null);
        builder.setView(customLayout);

        TextView text_sure = customLayout.findViewById(R.id.text_sure);
        TextView text_blockThisPerson = customLayout.findViewById(R.id.text_blockThisPerson);
        TextView btn_yes = customLayout.findViewById(R.id.text_Yes);
        TextView btn_no = customLayout.findViewById(R.id.text_No);


        String deleteTest = "Reject Applicant";
        String areYouSure = "Are you sure you want to reject this applicant";
        String noText = "No";
        String yesText = "Yes";

        text_sure.setText(areYouSure);
        text_blockThisPerson.setText(deleteTest);

        btn_no.setText(noText);
        btn_yes.setText(yesText);


        btn_no.setOnClickListener(view -> deleteDialog.dismiss());

        btn_yes.setOnClickListener(view -> {
            deleteDialog.dismiss();
            progressDialog.show();
            requestForRejectApplicant("Bearer " + sessionManagement.getToken(), jobId, applicantId, "reject");
        });

        deleteDialog = builder.create();
        deleteDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        deleteDialog.show();
        deleteDialog.setCancelable(false);
    }

    private void requestForRejectApplicant(String authToken, String jobId, String applicantId, String type) {
        Call<MainResponseModel> call;
        if (type.equals("accept")){
            call = service.hireApplicant(authToken, jobId, applicantId);
        }else if (type.equals("reject")){
            call = service.rejectApplicant(authToken, jobId, applicantId);
        }else {
            call = service.hireAndCloseApplicant(authToken, jobId, applicantId, "true");
        }

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<MainResponseModel> call, @NonNull Response<MainResponseModel> response) {
                if (response.isSuccessful()){
                    if (response.body()!=null){
                        boolean status = response.body().isStatus();
                        if (status){
                            if (type.equals("accept") || type.equals("close")){
                                Intent intent = new Intent(context, TeamMemberHiredActivity.class);
                                intent.putExtra("profileImage", data.getProfile_image());
                                intent.putExtra("applicantName", data.getName());
                                intent.putExtra("applicantJob", data.getJob_title());
                                onApplicantJobStatusUpdated.launch(intent);
                                overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
                            }else {
                                progressDialog.dismiss();
                                Intent intent = new Intent();
                                setResult(RESULT_OK, intent);
                                finish();
                                overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                            }

                        }else {
                            progressDialog.dismiss();
                            CustomCookieToast.showFailureToast(ApplicantProfessionalActivity.this, response.body().getMessage());
                        }
                    }else {
                        progressDialog.dismiss();
                        CustomCookieToast.showFailureToast(ApplicantProfessionalActivity.this, response.message());
                    }
                }else {
                    progressDialog.dismiss();
                    CustomCookieToast.showFailureToast(ApplicantProfessionalActivity.this, response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<MainResponseModel> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                CustomCookieToast.showFailureToast(ApplicantProfessionalActivity.this, t.getLocalizedMessage());
            }
        });
    }

    ActivityResultLauncher<Intent> onApplicantJobStatusUpdated = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
            overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
        }
    });


    @SuppressLint("NotifyDataSetChanged")
    private void showDeleteApplicantPopup(String applicantId) {

        AlertDialog.Builder builder = new AlertDialog.Builder(ApplicantProfessionalActivity.this, R.style.CustomDialog);
        final View customLayout = LayoutInflater.from(ApplicantProfessionalActivity.this).inflate(R.layout.popup_logout_user, null);
        builder.setView(customLayout);

        TextView text_sure = customLayout.findViewById(R.id.text_sure);
        TextView text_blockThisPerson = customLayout.findViewById(R.id.text_blockThisPerson);
        TextView btn_yes = customLayout.findViewById(R.id.text_Yes);
        TextView btn_no = customLayout.findViewById(R.id.text_No);

        String headerText, areYouSure;
        String noText = "No";
        String yesText = "Yes";

        headerText = "Delete Applicant";
        areYouSure = "Are you sure you want to delete this applicant";

        text_sure.setText(areYouSure);
        text_blockThisPerson.setText(headerText);

        btn_no.setText(noText);
        btn_yes.setText(yesText);


        btn_no.setOnClickListener(view -> deleteDialog.dismiss());

        btn_yes.setOnClickListener(view -> {
            deleteDialog.dismiss();
            progressDialog.show();
            requestForDeleteUser("Bearer " + sessionManagement.getToken(), applicantId);
        });

        deleteDialog = builder.create();
        deleteDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        deleteDialog.show();
        deleteDialog.setCancelable(false);
    }

    private void requestForDeleteUser(String authToken, String userId) {
        Call<MainResponseModel> call = service.deleteUser(authToken, userId);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<MainResponseModel> call, @NonNull Response<MainResponseModel> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()){
                    if (response.body()!=null){
                        boolean status = response.body().isStatus();
                        if (status){
                            Intent intent = new Intent();
                            setResult(RESULT_OK, intent);
                            finish();
                            overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                        }else {
                            CustomCookieToast.showFailureToast(ApplicantProfessionalActivity.this, response.body().getMessage());
                        }
                    }else {
                        CustomCookieToast.showFailureToast(ApplicantProfessionalActivity.this, response.message());
                    }
                }else {
                    CustomCookieToast.showFailureToast(ApplicantProfessionalActivity.this, response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<MainResponseModel> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                CustomCookieToast.showFailureToast(ApplicantProfessionalActivity.this, t.getLocalizedMessage());
            }
        });
    }

    private void showEducationRecyclerView(RecyclerView recyclerView, List<ApplicantProfileEducationDataModel> educationList){
        recyclerView.setHasFixedSize(true);
        ApplicantProfessionalEducationAdapter applicantEducationAdapter = new ApplicantProfessionalEducationAdapter(context, educationList);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(applicantEducationAdapter);
    }

    private void showExperiencesRecyclerView(RecyclerView recyclerView, List<ApplicantProfileExperienceDataModel> applicantExperiencesList){
        recyclerView.setHasFixedSize(true);
        ApplicantProfileExperienceAdapter experienceAdapter = new ApplicantProfileExperienceAdapter(context, applicantExperiencesList);
        rvExperience.setLayoutManager(new LinearLayoutManager(context));
        rvExperience.setAdapter(experienceAdapter);
    }

    ActivityResultLauncher<Intent> onApplicantEmploymentStatusUpdateLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent resultIntent = result.getData();
            if (resultIntent !=null){
                String employmentStatus = resultIntent.getStringExtra("employmentStatus");
                tvEmploymentStatus.setText(Utilities.getApplicantEmploymentStatus(Integer.parseInt(employmentStatus)));
                data.getProfessionalInfoDataModel().setEmployment_status(Integer.parseInt(employmentStatus));
            }

        }
    });

    ActivityResultLauncher<Intent> onApplicantEducationUpdateLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent resultIntent = result.getData();
            if (resultIntent !=null){
                applicantEducationList = resultIntent.getParcelableArrayListExtra("applicantEducationList");
                data.setEducationList(applicantEducationList);
                showEducationRecyclerView(rvEducation, applicantEducationList);
            }

        }
    });

    ActivityResultLauncher<Intent> onApplicantJobsUpdateLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent resultIntent = result.getData();
            if (resultIntent !=null){
                applicantJobsList = resultIntent.getParcelableArrayListExtra("applicantJobsList");
                data.setJobsList(applicantJobsList);
                setJobTags(applicantJobsList);
            }

        }
    });

    ActivityResultLauncher<Intent> onApplicantInterestsUpdateLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent resultIntent = result.getData();
            if (resultIntent !=null){
                applicantInterestsList = resultIntent.getParcelableArrayListExtra("applicantInterestsList");
                data.setProfessionalInterestsList(applicantInterestsList);
                setInterestTags(applicantInterestsList);
            }

        }
    });

    ActivityResultLauncher<Intent> onApplicantWorkExperienceUpdateLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent resultIntent = result.getData();
            if (resultIntent !=null){
                applicantExperiencesList = resultIntent.getParcelableArrayListExtra("applicantExperiencesList");
                data.setExperienceList(applicantExperiencesList);
                showExperiencesRecyclerView(rvExperience, applicantExperiencesList);
            }

        }
    });

    private void setJobTags(List<NameIdModel> applicantJobsList){
        List<String> jobs = new ArrayList<>();
        for (NameIdModel job : applicantJobsList) {
            jobs.add(job.getName());
        }
        tagJobs.setTags(jobs);
    }

    private void setInterestTags(List<NameIdModel> applicantInterestsList){
        List<String> interests = new ArrayList<>();
        for (NameIdModel applicantInterest : applicantInterestsList) {
            interests.add(applicantInterest.getName());
        }
        tagInterests.setTags(interests);
    }
}