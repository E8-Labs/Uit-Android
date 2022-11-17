package com.antizon.uit_android.company.activities;

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
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.antizon.uit_android.R;
import com.antizon.uit_android.activities.ImageViewerActivity;
import com.antizon.uit_android.activities.jobs.CompanyAndTeamJobsActivity;
import com.antizon.uit_android.adapters.company.CompanyProfileIndustriesAdapter;
import com.antizon.uit_android.adapters.company.CompanyTeamMembersProfileAdapter;
import com.antizon.uit_android.generic.activities.WebViewActivity;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.models.applicant.GenericApplicantDataModel;
import com.antizon.uit_android.models.community.MainResponseModel;
import com.antizon.uit_android.models.company.profile.CompanyProfileDataModel;
import com.antizon.uit_android.models.company.profile.CompanyProfileResponseModel;
import com.antizon.uit_android.models.profile.NameIdModel;
import com.antizon.uit_android.network.GetDataService;
import com.antizon.uit_android.network.RetrofitClientInstance;
import com.antizon.uit_android.utilities.CustomCookieToast;
import com.antizon.uit_android.utilities.Utilities;
import com.makeramen.roundedimageview.RoundedImageView;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UitCompanyProfileActivity extends AppCompatActivity implements CompanyTeamMembersProfileAdapter.CompanyTeamMembersProfileAdapterCallBack {
    Context context;
    GetDataService service;
    SessionManagement sessionManagement;
    RelativeLayout rl_loading;

    RelativeLayout btnBack, layout_main, btnDeiInformation, layout_allJobs, btnDemoGraphicInformation, layout_acceptReject;
    RoundedImageView userProfileImage;
    TextView text_profileName, text_userEmail, text_userLocation, text_companyBioDescription, text_companySizeName,
            text_companyStageName,  text_companyWebsite, text_greenHouseConnect, btnReject, btnApprove, text_visitSite, text_totalTeamMembers;
    RecyclerView recyclerview_industries, recyclerview_companyTeamMembers;

    String userId, visitProfileId;
    int accountStatus, detailPosition;
    CompanyProfileDataModel profileDataModel;

    AlertDialog deleteDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uit_company_profile);

        Utilities.setCustomStatusAndNavColor(UitCompanyProfileActivity.this, R.color.app_color, R.color.white_dash);
        context = UitCompanyProfileActivity.this;
        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        sessionManagement = new SessionManagement(context);

        userId = sessionManagement.getKeyId();
        visitProfileId = getIntent().getStringExtra("visitProfileId");
        accountStatus = getIntent().getIntExtra("accountStatus", 0);

        initViews();

        rl_loading.setVisibility(View.VISIBLE);
        layout_main.setVisibility(View.GONE);
        requestForCompanyProfile("Bearer " + sessionManagement.getToken(), visitProfileId);
    }

    private void initViews(){
        rl_loading = findViewById(R.id.rl_loading);
        layout_main = findViewById(R.id.layout_main);

        btnBack = findViewById(R.id.btnBack);
        userProfileImage = findViewById(R.id.userProfileImage);
        text_profileName = findViewById(R.id.text_profileName);
        text_userEmail = findViewById(R.id.text_userEmail);
        text_userLocation = findViewById(R.id.text_userLocation);
        text_companyBioDescription = findViewById(R.id.text_companyBioDescription);
        text_companySizeName = findViewById(R.id.text_companySizeName);
        text_companyWebsite = findViewById(R.id.text_companyWebsite);
        text_greenHouseConnect = findViewById(R.id.text_greenHouseConnect);
        text_companyStageName = findViewById(R.id.text_companyStageName);
        layout_acceptReject = findViewById(R.id.layout_acceptReject);
        btnReject = findViewById(R.id.btnReject);
        btnApprove = findViewById(R.id.btnApprove);
        layout_allJobs = findViewById(R.id.layout_allJobs);
        text_visitSite = findViewById(R.id.text_visitSite);

        text_totalTeamMembers = findViewById(R.id.text_totalTeamMembers);
        recyclerview_companyTeamMembers = findViewById(R.id.recyclerview_companyTeamMembers);
        recyclerview_industries = findViewById(R.id.recyclerview_industries);
        btnDeiInformation = findViewById(R.id.btnDeiInformation);
        btnDemoGraphicInformation = findViewById(R.id.btnDemoGraphicInformation);

        btnBack.setOnClickListener(v -> onBackPressed());
    }

    public void requestForCompanyProfile(String authToken, String userId) {
        Call<CompanyProfileResponseModel> call = service.getCompanyProfile(authToken, userId);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<CompanyProfileResponseModel> call, @NonNull Response<CompanyProfileResponseModel> response) {
                rl_loading.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().isStatus()) {
                         profileDataModel = response.body().getDataModel();
                        if (profileDataModel!=null){
                            layout_main.setVisibility(View.VISIBLE);
                            Utilities.loadImage(context, profileDataModel.getProfile_image(), userProfileImage);
                            userProfileImage.setOnClickListener(v -> {
                                Intent intent = new Intent(context, ImageViewerActivity.class);
                                ViewCompat.setTransitionName(userProfileImage, profileDataModel.getProfile_image());
                                String animationName = ViewCompat.getTransitionName(userProfileImage);
                                intent.putExtra("animationName", animationName);
                                intent.putExtra("IMAGE", profileDataModel.getProfile_image());
                                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(UitCompanyProfileActivity.this, userProfileImage, Objects.requireNonNull(animationName));
                                startActivity(intent, options.toBundle());
                            });

                            text_profileName.setText(profileDataModel.getName());
                            text_userEmail.setText(profileDataModel.getEmail());
                            text_companyWebsite.setText(profileDataModel.getWebsite());
                            text_companyWebsite.setPaintFlags(text_companyWebsite.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

                            text_visitSite.setOnClickListener(v -> {
                                Intent intent = new Intent(context, WebViewActivity.class);
                                intent.putExtra("pageTitle", profileDataModel.getName() + "'s Site");
                                intent.putExtra("pageUrl", profileDataModel.getWebsite());
                                startActivity(intent);
                                overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
                            });

                            text_companyBioDescription.setText(profileDataModel.getBio());
                            text_userLocation.setText(profileDataModel.getCity());


                            if (profileDataModel.getUserStagesList() !=null){
                                if (!profileDataModel.getUserStagesList().isEmpty()){
                                    text_companyStageName.setText(profileDataModel.getUserStagesList().get(0).getName());
                                }else {
                                    text_companyStageName.setText(R.string.notSelected);
                                }
                            }
                            if (profileDataModel.getUserSizesList() !=null){
                                if (!profileDataModel.getUserSizesList().isEmpty()){
                                    text_companySizeName.setText(profileDataModel.getUserSizesList().get(0).getName());
                                }else {
                                    text_companySizeName.setText(R.string.notSelected);
                                }
                            }

                            if (profileDataModel.getIndustries() !=null){
                                showIndustriesRecyclerView(recyclerview_industries, profileDataModel.getIndustries());
                            }


                            if (profileDataModel.getGreenhouse_access_token() !=null){
                                text_greenHouseConnect.setText(R.string.connected);
                            }else {
                                text_greenHouseConnect.setText(R.string.connect);
                            }

                            btnDemoGraphicInformation.setOnClickListener(v -> {
                                Intent demoGraphIntent = new Intent(context, CompanyProfileDemoGraphicInfoActivity.class);
                                demoGraphIntent.putExtra("companyProfileDataModel", profileDataModel);
                                demoGraphIntent.putExtra("visitProfileId", visitProfileId);
                                demoGraphIntent.putExtra("accountStatus", accountStatus);
                                onProfileUpdateLauncher.launch(demoGraphIntent);
                                overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
                            });

                            btnDeiInformation.setOnClickListener(v -> {
                                Intent demoGraphIntent = new Intent(context, CompanyProfileDeiInfoActivity.class);
                                demoGraphIntent.putExtra("companyProfileDataModel", profileDataModel);
                                demoGraphIntent.putExtra("accountStatus", accountStatus);
                                demoGraphIntent.putExtra("visitProfileId", visitProfileId);
                                onProfileUpdateLauncher.launch((demoGraphIntent));
                                overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
                            });


                            layout_allJobs.setOnClickListener(v -> {
                                Intent intent = new Intent(context, CompanyAndTeamJobsActivity.class);
                                intent.putExtra("personaImage", profileDataModel.getProfile_image());
                                intent.putExtra("personaName", profileDataModel.getName());
                                intent.putExtra("personaId", profileDataModel.getUser_id() + "");
                                intent.putExtra("personaRole", "2");
                                startActivity(intent);
                                overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
                            });

                            List<GenericApplicantDataModel> applications = profileDataModel.getCompanyTeamMembers();

                            showCompanyTeamMembersList(recyclerview_companyTeamMembers, applications);

                            if (sessionManagement.getRole().equals("1") || sessionManagement.getRole().equals("3")) {
                                if (accountStatus == 4){
                                    layout_allJobs.setVisibility(View.GONE);
                                    layout_acceptReject.setVisibility(View.VISIBLE);
                                    btnReject.setText(context.getString(R.string.reject));
                                    btnApprove.setText(context.getString(R.string.approve));
                                    btnApprove.setBackgroundResource(R.drawable.app_color_7dp_rounded);

                                    btnReject.setOnClickListener(v -> showAcceptRejectCompanyRequestPopup(visitProfileId, "decline"));

                                    btnApprove.setOnClickListener(v -> showAcceptRejectCompanyRequestPopup(visitProfileId, "approve"));


                                }else if (accountStatus == 1){
                                    layout_allJobs.setVisibility(View.VISIBLE);
                                    layout_acceptReject.setVisibility(View.VISIBLE);
                                    btnReject.setText(context.getString(R.string.delete));
                                    btnApprove.setText(context.getString(R.string.pause));
                                    btnApprove.setBackgroundResource(R.drawable.yellow_color_7dp_rounded);

                                    btnApprove.setOnClickListener(v -> showAcceptRejectCompanyRequestPopup(visitProfileId, "pause"));

                                    btnReject.setOnClickListener(v -> showAcceptRejectCompanyRequestPopup(visitProfileId, "delete"));

                                }else {
                                    layout_allJobs.setVisibility(View.VISIBLE);
                                    layout_acceptReject.setVisibility(View.GONE);
                                }

                            }else {
                                layout_allJobs.setVisibility(View.VISIBLE);
                                layout_acceptReject.setVisibility(View.GONE);
                            }
                        }
                    }else {
                        CustomCookieToast.showFailureToast(UitCompanyProfileActivity.this, "Failure!", response.body().getMessage());
                    }
                } else {
                    CustomCookieToast.showFailureToast(UitCompanyProfileActivity.this, "Failure!", response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<CompanyProfileResponseModel> call, @NonNull Throwable t) {
                rl_loading.setVisibility(View.GONE);
                CustomCookieToast.showFailureToast(UitCompanyProfileActivity.this, "Failure!", t.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }

    private void showCompanyTeamMembersList(RecyclerView recyclerView, List<GenericApplicantDataModel> applications){
        if (applications != null && !applications.isEmpty()) {
            recyclerView.setVisibility(View.VISIBLE);
            text_totalTeamMembers.setVisibility(View.VISIBLE);
            String totalTeamMembers = "Team Members ( " + applications.size() + " )";
            text_totalTeamMembers.setText(totalTeamMembers);

            recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            CompanyTeamMembersProfileAdapter companyTeamMembersProfileAdapter = new CompanyTeamMembersProfileAdapter(context, applications, this);
            recyclerView.setAdapter(companyTeamMembersProfileAdapter);
        } else {
            recyclerView.setVisibility(View.GONE);
            text_totalTeamMembers.setVisibility(View.GONE);
        }
    }

    private void showIndustriesRecyclerView(RecyclerView recyclerView, List<NameIdModel> industriesList){
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        CompanyProfileIndustriesAdapter profileIndustriesAdapter = new CompanyProfileIndustriesAdapter(context, industriesList);
        recyclerView.setAdapter(profileIndustriesAdapter);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void showAcceptRejectCompanyRequestPopup(String companyId, String type) {

        AlertDialog.Builder builder = new AlertDialog.Builder(UitCompanyProfileActivity.this, R.style.CustomDialog);
        final View customLayout = LayoutInflater.from(UitCompanyProfileActivity.this).inflate(R.layout.popup_logout_user, null);
        builder.setView(customLayout);

        TextView text_sure = customLayout.findViewById(R.id.text_sure);
        TextView text_blockThisPerson = customLayout.findViewById(R.id.text_blockThisPerson);
        TextView btn_yes = customLayout.findViewById(R.id.text_Yes);
        TextView btn_no = customLayout.findViewById(R.id.text_No);

        String headerText, areYouSure;
        String noText = "No";
        String yesText = "Yes";

        switch (type) {
            case "approve":
                headerText = "Approve Company";
                areYouSure = "Are you sure you want to approve this company";
                break;
            case "decline":
                headerText = "Decline Company";
                areYouSure = "Are you sure you want to decline this company";
                break;
            case "pause":
                headerText = "Pause Company";
                areYouSure = "Are you sure you want to pause this company";
                break;
            default:
                headerText = "Delete Company";
                areYouSure = "Are you sure you want to delete this company";
                break;
        }

        text_sure.setText(areYouSure);
        text_blockThisPerson.setText(headerText);

        btn_no.setText(noText);
        btn_yes.setText(yesText);


        btn_no.setOnClickListener(view -> deleteDialog.dismiss());

        btn_yes.setOnClickListener(view -> {
            deleteDialog.dismiss();
            rl_loading.setVisibility(View.VISIBLE);
            requestForApproveCompany("Bearer " + sessionManagement.getToken(), companyId, type);
        });

        deleteDialog = builder.create();
        deleteDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        deleteDialog.show();
        deleteDialog.setCancelable(false);
    }

    private void requestForApproveCompany(String authToken, String companyId, String type) {
        Call<MainResponseModel> call;
        switch (type) {
            case "approve":
                call = service.approveCompany(authToken, companyId);
                break;
            case "decline":
                call = service.rejectCompany(authToken, companyId);
                break;
            case "pause":
                call = service.pauseCompany(authToken, companyId);
                break;
            default:
                call = service.deleteUser(authToken, companyId);
                break;
        }
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<MainResponseModel> call, @NonNull Response<MainResponseModel> response) {
                if (response.isSuccessful()){
                    if (response.body()!=null){
                        boolean status = response.body().isStatus();
                        if (status){
                            Intent intent = new Intent();
                            setResult(RESULT_OK, intent);
                            finish();
                            overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                        }else {
                            CustomCookieToast.showFailureToast(UitCompanyProfileActivity.this, response.body().getMessage());
                        }
                    }else {
                        CustomCookieToast.showFailureToast(UitCompanyProfileActivity.this, response.message());
                    }
                }else {
                    CustomCookieToast.showFailureToast(UitCompanyProfileActivity.this, response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<MainResponseModel> call, @NonNull Throwable t) {
                rl_loading.setVisibility(View.GONE);
                CustomCookieToast.showFailureToast(UitCompanyProfileActivity.this, t.getLocalizedMessage());
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    ActivityResultLauncher<Intent> onProfileUpdateLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            if (sessionManagement.getRole().equals("2")){
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
                overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
            }
        }
    });

    @Override
    public void onCompanyTeamMemberClick(int position) {
        detailPosition = position;
        Intent intent = new Intent(context, CompanyTeamMembersActivity.class);
        intent.putExtra("profileDataModel", profileDataModel);
        onCompanyTeamMemberDeleted.launch(intent);
        overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
    }

    @SuppressLint("NotifyDataSetChanged")
    ActivityResultLauncher<Intent> onCompanyTeamMemberDeleted = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
            overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
        }
    });

}