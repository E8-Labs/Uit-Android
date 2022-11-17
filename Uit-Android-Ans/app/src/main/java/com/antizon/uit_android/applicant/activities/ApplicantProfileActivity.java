package com.antizon.uit_android.applicant.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.antizon.uit_android.R;
import com.antizon.uit_android.activities.ImageViewerActivity;
import com.antizon.uit_android.activities.PdfViewerActivity;
import com.antizon.uit_android.activities.chat.MessagesActivity;
import com.antizon.uit_android.activities.jobs.FlagJobUserActivity;
import com.antizon.uit_android.activities.jobs.TeamMemberHiredActivity;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.models.community.MainResponseModel;
import com.antizon.uit_android.models.profile.ApplicantProfileResponseModel;
import com.antizon.uit_android.models.profile.ProfileResponseDataModel;
import com.antizon.uit_android.network.GetDataService;
import com.antizon.uit_android.network.RetrofitClientInstance;
import com.antizon.uit_android.utilities.CustomCookieToast;
import com.antizon.uit_android.utilities.Utilities;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApplicantProfileActivity extends AppCompatActivity {
    Context context;
    GetDataService service;
    SessionManagement sessionManagement;
    ProgressDialog progressDialog;

    ImageView ivCover, ivClose, ivSave;
    RoundedImageView ivProfile;
    TextView tvName, tvProfession, tvAbout, tvEmail, tvPhone, tvResume, tvCoverLetter,  btnReject, btnHire;
    View rlProfessional, rlDemographic, tvFlagUser;
    RelativeLayout layout_hiring, layout_resumeView, layout_coverLetter;

    String applicantId, applicationStatus, jobId;
    ProfileResponseDataModel applicantProfileDataModel;
    AlertDialog deleteDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applicant_profile);
        Utilities.setCustomStatusAndNavColor(ApplicantProfileActivity.this, R.color.app_color, R.color.white_dash);
        context = ApplicantProfileActivity.this;
        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        sessionManagement = new SessionManagement(context);
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading...");

        applicantId = getIntent().getStringExtra("applicantId");
        applicationStatus = getIntent().getStringExtra("applicationStatus");
        jobId = getIntent().getStringExtra("jobId");

        ivCover = findViewById(R.id.iv_cover);
        ivProfile = findViewById(R.id.iv_profile);
        ivClose = findViewById(R.id.iv_close);
        ivSave = findViewById(R.id.iv_bookmark);
        tvName = findViewById(R.id.tv_name);
        tvProfession = findViewById(R.id.tv_profession);
        tvAbout = findViewById(R.id.tv_about);
        tvEmail = findViewById(R.id.tv_email);
        tvPhone = findViewById(R.id.tv_phone);
        tvResume = findViewById(R.id.tv_resume);
        tvCoverLetter = findViewById(R.id.tv_cover_letter);
        rlProfessional = findViewById(R.id.rl_professional_information);
        rlDemographic = findViewById(R.id.rl_demographic);
        tvFlagUser = findViewById(R.id.tv_flag);
        layout_hiring = findViewById(R.id.layout_hiring);
        btnHire = findViewById(R.id.btnHire);
        btnReject = findViewById(R.id.btnReject);
        layout_resumeView = findViewById(R.id.layout_resumeView);
        layout_coverLetter = findViewById(R.id.layout_coverLetter);

        requestForUserProfile("Bearer " + sessionManagement.getToken(), applicantId);

        ivClose.setOnClickListener(v -> onBackPressed());
    }


    public void requestForUserProfile(String authToken, String userId) {

        Call<ApplicantProfileResponseModel> call = service.getApplicantProfile(authToken, userId);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ApplicantProfileResponseModel> call, @NonNull Response<ApplicantProfileResponseModel> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().isStatus()) {
                        applicantProfileDataModel = response.body().getData();
                        if (applicantProfileDataModel !=null){
                            Utilities.loadImage(context, applicantProfileDataModel.getProfile_image(), ivProfile);

                            ivProfile.setOnClickListener(v -> {
                                Intent intent = new Intent(context, ImageViewerActivity.class);
                                ViewCompat.setTransitionName(ivProfile, applicantProfileDataModel.getProfile_image());
                                String animationName = ViewCompat.getTransitionName(ivProfile);
                                intent.putExtra("animationName", animationName);
                                intent.putExtra("IMAGE",applicantProfileDataModel.getProfile_image());
                                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(ApplicantProfileActivity.this, ivProfile, Objects.requireNonNull(animationName));
                                startActivity(intent, options.toBundle());
                            });

                            if (applicantProfileDataModel.getName() !=null){
                                tvName.setText(applicantProfileDataModel.getName());
                            }
                            if (applicantProfileDataModel.getJob_title() !=null){
                                tvProfession.setText(applicantProfileDataModel.getJob_title());
                            }

                            if (applicantProfileDataModel.getBio() !=null){
                                tvAbout.setText(applicantProfileDataModel.getBio());
                            }

                            if (applicantProfileDataModel.getEmail() !=null){
                                tvEmail.setText(applicantProfileDataModel.getEmail());
                            }

                            if (applicantProfileDataModel.getPhone() !=null){
                                tvPhone.setText(applicantProfileDataModel.getPhone());
                            }

                            if (applicantProfileDataModel.getProfessionalInfoDataModel() !=null){
                                String resumeUrl = applicantProfileDataModel.getProfessionalInfoDataModel().getResume();
                                String coverLetter = applicantProfileDataModel.getProfessionalInfoDataModel().getCover_letter();
                                if ( resumeUrl!=null){
                                    tvResume.setText(R.string.text_resume);
                                    tvResume.setPaintFlags(tvResume.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                                    tvResume.setTextColor(context.getColor(R.color.codGrey));

                                    layout_resumeView.setOnClickListener(v -> moveToPdfViewerActivity(resumeUrl, applicantProfileDataModel.getName() + " 's Resume"));
                                }else {
                                    tvResume.setText(context.getString(R.string.text_notAvailable));
                                    tvResume.setTextColor(context.getColor(R.color.dark_grey));
                                }

                                if (coverLetter !=null){
                                    tvCoverLetter.setText(context.getString(R.string.text_cover_letter_pdf));
                                    tvCoverLetter.setPaintFlags(tvCoverLetter.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                                    tvCoverLetter.setTextColor(context.getColor(R.color.codGrey));

                                    layout_coverLetter.setOnClickListener(v -> moveToPdfViewerActivity(coverLetter, applicantProfileDataModel.getName() + " 's Cover Letter"));
                                }else {
                                    tvCoverLetter.setText(context.getString(R.string.text_notAvailable));
                                    tvCoverLetter.setTextColor(context.getColor(R.color.dark_grey));
                                }
                            }else {
                                tvResume.setTextColor(context.getColor(R.color.dark_grey));
                                tvCoverLetter.setTextColor(context.getColor(R.color.dark_grey));
                                tvResume.setText(context.getString(R.string.text_notAvailable));
                                tvCoverLetter.setText(context.getString(R.string.text_notAvailable));
                            }

                            tvFlagUser.setOnClickListener(v -> {
                                Intent flagJobIntent = new Intent(context, FlagJobUserActivity.class);
                                flagJobIntent.putExtra("type", "flag_user");
                                flagJobIntent.putExtra("flagImage", applicantProfileDataModel.getProfile_image());
                                flagJobIntent.putExtra("flagTitle", applicantProfileDataModel.getName());
                                flagJobIntent.putExtra("flagId", String.valueOf(applicantProfileDataModel.getUser_id()));
                                startActivity(flagJobIntent);
                                overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
                            });

                            rlProfessional.setOnClickListener(v -> moveToNextScreen(applicantProfileDataModel, ApplicantProfessionalActivity.class));

                            rlDemographic.setOnClickListener(v -> moveToNextScreen(applicantProfileDataModel, ApplicantDemographicActivity.class));


                            if (sessionManagement.getRole().equals("1") || sessionManagement.getRole().equals("3")){
                                rlDemographic.setVisibility(View.VISIBLE);
                                layout_hiring.setVisibility(View.VISIBLE);
                                btnReject.setText(context.getString(R.string.delete));
                                btnHire.setText(context.getString(R.string.message));

                                btnReject.setOnClickListener(v -> showDeleteApplicantPopup(applicantId));

                                btnHire.setOnClickListener(v -> {
                                    Intent intent = new Intent(context, MessagesActivity.class);
                                    intent.putExtra("CHAT_ID", sessionManagement.getKeyId()+"-"+applicantProfileDataModel.getUser_id());
                                    intent.putExtra("second_user_id", String.valueOf(applicantProfileDataModel.getUser_id()));
                                    intent.putExtra("second_user_picture", applicantProfileDataModel.getProfile_image());
                                    intent.putExtra("second_user_name", applicantProfileDataModel.getName());
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
                                });

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
                            }else {
                                if (sessionManagement.getKeyId().equals(String.valueOf(applicantProfileDataModel.getUser_id()))){
                                    tvFlagUser.setVisibility(View.GONE);
                                    rlDemographic.setVisibility(View.VISIBLE);
                                }else {
                                    tvFlagUser.setVisibility(View.VISIBLE);
                                }

                                layout_hiring.setVisibility(View.GONE);
                            }

                        }
                    }else {

                        CustomCookieToast.showFailureToast(ApplicantProfileActivity.this, "Failure!", response.body().getMessage());
                    }
                } else {

                    CustomCookieToast.showFailureToast(ApplicantProfileActivity.this, "Failure!", response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApplicantProfileResponseModel> call, @NonNull Throwable t) {
                CustomCookieToast.showFailureToast(ApplicantProfileActivity.this, "Failure!", t.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }

    private void moveToNextScreen(ProfileResponseDataModel applicantProfileDataModel, Class<?> className){
        Intent intent = new Intent(context, className);
        intent.putExtra("jobId", jobId);
        intent.putExtra("applicantId", applicantId);
        intent.putExtra("applicationStatus", applicationStatus);
        intent.putExtra("data", applicantProfileDataModel);
        onApplicantJobStatusUpdated.launch(intent);
        overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
    }

    private void showHireBottomSheet() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(ApplicantProfileActivity.this, R.style.BottomSheetTheme);
        View sheetView = LayoutInflater.from(ApplicantProfileActivity.this).inflate(R.layout.bottom_sheet_hire_options, findViewById(R.id.bottom_sheet_hire_options));

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

    ActivityResultLauncher<Intent> onApplicantJobStatusUpdated = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
            overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
        }
    });

    private void showRejectApplicantPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ApplicantProfileActivity.this, R.style.CustomDialog);
        final View customLayout = LayoutInflater.from(ApplicantProfileActivity.this).inflate(R.layout.popup_logout_user, null);
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
                                    intent.putExtra("profileImage", applicantProfileDataModel.getProfile_image());
                                    intent.putExtra("applicantName", applicantProfileDataModel.getName());
                                    intent.putExtra("applicantJob", applicantProfileDataModel.getJob_title());
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
                            CustomCookieToast.showFailureToast(ApplicantProfileActivity.this, response.body().getMessage());
                        }
                    }else {
                        progressDialog.dismiss();
                        CustomCookieToast.showFailureToast(ApplicantProfileActivity.this, response.message());
                    }
                }else {
                    progressDialog.dismiss();
                    CustomCookieToast.showFailureToast(ApplicantProfileActivity.this, response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<MainResponseModel> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                CustomCookieToast.showFailureToast(ApplicantProfileActivity.this, t.getLocalizedMessage());
            }
        });
    }

    private void moveToPdfViewerActivity(String pdfUrl, String headerText){
        Intent flagJobIntent = new Intent(context, PdfViewerActivity.class);
        flagJobIntent.putExtra("headerText", headerText);
        flagJobIntent.putExtra("pdfUrl",pdfUrl );
        startActivity(flagJobIntent);
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void showDeleteApplicantPopup(String applicantId) {

        AlertDialog.Builder builder = new AlertDialog.Builder(ApplicantProfileActivity.this, R.style.CustomDialog);
        final View customLayout = LayoutInflater.from(ApplicantProfileActivity.this).inflate(R.layout.popup_logout_user, null);
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
                            CustomCookieToast.showFailureToast(ApplicantProfileActivity.this, response.body().getMessage());
                        }
                    }else {
                        CustomCookieToast.showFailureToast(ApplicantProfileActivity.this, response.message());
                    }
                }else {
                    CustomCookieToast.showFailureToast(ApplicantProfileActivity.this, response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<MainResponseModel> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                CustomCookieToast.showFailureToast(ApplicantProfileActivity.this, t.getLocalizedMessage());
            }
        });
    }


}