package com.antizon.uit_android.applicant.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;

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
import com.antizon.uit_android.applicant.welcome.ActivityApplicantSelectGenderIdentity;
import com.antizon.uit_android.applicant.welcome.ActivityApplicantSelectRace;
import com.antizon.uit_android.applicant.welcome.ActivityApplicantSexualOrientation;
import com.antizon.uit_android.applicant.welcome.ActivityApplicantVeteranStatus;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.models.community.MainResponseModel;
import com.antizon.uit_android.models.profile.NameIdModel;
import com.antizon.uit_android.models.profile.ProfileDemoGraphicInfoModel;
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

public class ApplicantDemographicActivity extends AppCompatActivity {
    Context context;
    GetDataService service;
    SessionManagement sessionManagement;
    ProgressDialog progressDialog;

    // ---------------------------------------- V I E W S ------------------------------------------
    ImageView ivCover, ivProfile, ivClose, ivSave;
    TextView tvName, tvProfession, tvGender, tvSexOrientation, tvVeteran, btnReject, btnHire,
            btnEditRacialIdentity,btnEditGenderIdentity, btnEditSexualOrientation, btnEditVeteranStatus;
    TagContainerLayout tagContainerLayout;
    RelativeLayout layout_hiring;

    // -------------------------------------- V A R I A B L E S ------------------------------------
    String applicantId, jobId, applicationStatus;
    ProfileResponseDataModel data;

    AlertDialog deleteDialog;
    List<NameIdModel> applicantRacesList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applicant_demographic);
        Utilities.setCustomStatusAndNavColor(ApplicantDemographicActivity.this, R.color.app_color, R.color.white_dash);
        context = ApplicantDemographicActivity.this;
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
        tagContainerLayout = findViewById(R.id.tag_container_races);
        tvGender = findViewById(R.id.tv_gender);
        tvSexOrientation = findViewById(R.id.tv_sexual_orientation);
        tvVeteran = findViewById(R.id.tv_veteran_status);
        layout_hiring = findViewById(R.id.layout_hiring);
        btnReject = findViewById(R.id.btnReject);
        btnHire = findViewById(R.id.btnHire);

        btnEditRacialIdentity = findViewById(R.id.btnEditRacialIdentity);
        btnEditGenderIdentity = findViewById(R.id.btnEditGenderIdentity);
        btnEditSexualOrientation = findViewById(R.id.btnEditSexualOrientation);
        btnEditVeteranStatus = findViewById(R.id.btnEditVeteranStatus);

        //------------------------------  S E T U P  &   L O A D  ----------------------------------

        jobId = getIntent().getStringExtra("jobId");
        applicantId = getIntent().getStringExtra("applicantId");
        applicationStatus = getIntent().getStringExtra("applicationStatus");
        data = getIntent().getParcelableExtra("data");

        setDataToViews();


        //------------------------------------ A C T I O N S ---------------------------------------
        ivClose.setOnClickListener(v -> onBackPressed());

        if (sessionManagement.getRole().equals("1")  || sessionManagement.getRole().equals("3")){
            layout_hiring.setVisibility(View.VISIBLE);
            btnReject.setText(context.getString(R.string.delete));
            btnHire.setText(context.getString(R.string.message));

            btnReject.setOnClickListener(v -> {
                //
            });

            btnHire.setOnClickListener(v -> {
                if (data !=null){
                    Intent intent = new Intent(context, MessagesActivity.class);
                    intent.putExtra("CHAT_ID", sessionManagement.getKeyId()+"-"+data.getUser_id());
                    intent.putExtra("second_user_id", String.valueOf(data.getUser_id()));
                    intent.putExtra("second_user_picture", data.getProfile_image());
                    intent.putExtra("second_user_name", data.getName());
                    startActivity(intent);
                    overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
                }
            });


            btnEditRacialIdentity.setVisibility(View.GONE);
            btnEditGenderIdentity.setVisibility(View.GONE);
            btnEditSexualOrientation.setVisibility(View.GONE);
            btnEditVeteranStatus.setVisibility(View.GONE);

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

            btnEditRacialIdentity.setVisibility(View.GONE);
            btnEditGenderIdentity.setVisibility(View.GONE);
            btnEditSexualOrientation.setVisibility(View.GONE);
            btnEditVeteranStatus.setVisibility(View.GONE);
        }else {
            if (sessionManagement.getKeyId().equals(String.valueOf(data.getUser_id()))){
                btnEditRacialIdentity.setVisibility(View.VISIBLE);
                btnEditGenderIdentity.setVisibility(View.VISIBLE);
                btnEditSexualOrientation.setVisibility(View.VISIBLE);
                btnEditVeteranStatus.setVisibility(View.VISIBLE);
            }else {
                btnEditRacialIdentity.setVisibility(View.GONE);
                btnEditGenderIdentity.setVisibility(View.GONE);
                btnEditSexualOrientation.setVisibility(View.GONE);
                btnEditVeteranStatus.setVisibility(View.GONE);
            }
            layout_hiring.setVisibility(View.GONE);
        }

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
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(ApplicantDemographicActivity.this, ivProfile, Objects.requireNonNull(animationName));
            startActivity(intent, options.toBundle());
        });

        final ProfileDemoGraphicInfoModel demographicInfo = data.getDemographicInfo();
        if (demographicInfo != null) {
            applicantRacesList = new ArrayList<>();

            applicantRacesList = demographicInfo.getRacesList();
            if (applicantRacesList != null) {
                setRacesTags(applicantRacesList);

                btnEditRacialIdentity.setOnClickListener(v -> {
                    Intent intent = new Intent(context, ActivityApplicantSelectRace.class);
                    intent.putExtra("from", "edit");
                    intent.putParcelableArrayListExtra("applicantRacesList", (ArrayList<? extends Parcelable>) applicantRacesList);
                    onApplicantRacialIdentitiesUpdateLauncher.launch(intent);
                    overridePendingTransition(R.anim.right_to_left,R.anim.left_to_right);
                });
            }

            tvGender.setText(Utilities.getGender(demographicInfo.getGender()));

            btnEditGenderIdentity.setOnClickListener(v -> {
                Intent intent = new Intent(context, ActivityApplicantSelectGenderIdentity.class);
                intent.putExtra("from", "edit");
                intent.putExtra("applicantGender", String.valueOf(demographicInfo.getGender()));
                onApplicantGenderUpdateLauncher.launch(intent);
                overridePendingTransition(R.anim.right_to_left,R.anim.left_to_right);
            });

            tvSexOrientation.setText(Utilities.getLgbt(demographicInfo.getLgbt()));

            btnEditSexualOrientation.setOnClickListener(v -> {
                Intent intent = new Intent(context, ActivityApplicantSexualOrientation.class);
                intent.putExtra("from", "edit");
                intent.putExtra("applicantLgbt", String.valueOf(demographicInfo.getLgbt()));
                onApplicantSexualOrientationUpdateLauncher.launch(intent);
                overridePendingTransition(R.anim.right_to_left,R.anim.left_to_right);
            });

            tvVeteran.setText(Utilities.getVeteranType(demographicInfo.getVeteran()));

            btnEditVeteranStatus.setOnClickListener(v -> {
                Intent intent = new Intent(context, ActivityApplicantVeteranStatus.class);
                intent.putExtra("from", "edit");
                intent.putExtra("applicantVeteranStatus", demographicInfo.getVeteran());
                onApplicantVeteranStatusUpdateLauncher.launch(intent);
                overridePendingTransition(R.anim.right_to_left,R.anim.left_to_right);
            });
        }
    }

    ActivityResultLauncher<Intent> onApplicantRacialIdentitiesUpdateLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent resultIntent = result.getData();
            if (resultIntent !=null){
                applicantRacesList = resultIntent.getParcelableArrayListExtra("applicantRacesList");
                data.getDemographicInfo().setRacesList(applicantRacesList);
                setRacesTags(applicantRacesList);
            }

        }
    });

    ActivityResultLauncher<Intent> onApplicantGenderUpdateLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent resultIntent = result.getData();
            if (resultIntent !=null){
                String applicantGender = resultIntent.getStringExtra("applicantGender");
                tvGender.setText(Utilities.getGender(Integer.parseInt(applicantGender)));
                data.getDemographicInfo().setGender(Integer.parseInt(applicantGender));
            }

        }
    });

    ActivityResultLauncher<Intent> onApplicantSexualOrientationUpdateLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent resultIntent = result.getData();
            if (resultIntent !=null){
                String applicantLgbt = resultIntent.getStringExtra("applicantLgbt");
                tvSexOrientation.setText(Utilities.getLgbt(Integer.parseInt(applicantLgbt)));
                data.getDemographicInfo().setLgbt(Integer.parseInt(applicantLgbt));
            }

        }
    });

    ActivityResultLauncher<Intent> onApplicantVeteranStatusUpdateLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent resultIntent = result.getData();
            if (resultIntent !=null){
                Integer applicantVeteranStatus = resultIntent.getIntExtra("applicantVeteranStatus", -1);
                tvVeteran.setText(Utilities.getVeteranType(applicantVeteranStatus));
                data.getDemographicInfo().setVeteran(applicantVeteranStatus);
            }

        }
    });

    private void setRacesTags(List<NameIdModel> applicantRacesList){
        List<String> races = new ArrayList<>();
        for (NameIdModel race : applicantRacesList) {
            races.add(race.getName());
        }
        tagContainerLayout.setTags(races);
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
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(ApplicantDemographicActivity.this, R.style.BottomSheetTheme);
        View sheetView = LayoutInflater.from(ApplicantDemographicActivity.this).inflate(R.layout.bottom_sheet_hire_options, findViewById(R.id.bottom_sheet_hire_options));

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
        AlertDialog.Builder builder = new AlertDialog.Builder(ApplicantDemographicActivity.this, R.style.CustomDialog);
        final View customLayout = LayoutInflater.from(ApplicantDemographicActivity.this).inflate(R.layout.popup_logout_user, null);
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

    private void requestForRejectApplicant(String authToken, String jobId, String applicantId,  String type) {
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
                            CustomCookieToast.showFailureToast(ApplicantDemographicActivity.this, response.body().getMessage());
                        }
                    }else {
                        CustomCookieToast.showFailureToast(ApplicantDemographicActivity.this, response.message());
                    }
                }else {
                    CustomCookieToast.showFailureToast(ApplicantDemographicActivity.this, response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<MainResponseModel> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                CustomCookieToast.showFailureToast(ApplicantDemographicActivity.this, t.getLocalizedMessage());
            }
        });
    }
}