package com.antizon.uit_android.company.activities;

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
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.antizon.uit_android.R;
import com.antizon.uit_android.activities.ImageViewerActivity;
import com.antizon.uit_android.company.welcome.CompanyDEIProgrammingActivity;
import com.antizon.uit_android.company.welcome.CompanyDEIStatementActivity;
import com.antizon.uit_android.company.welcome.CompanyDEITrainingActivity;
import com.antizon.uit_android.company.welcome.CompanyErgActivity;
import com.antizon.uit_android.company.welcome.CompanyLeadActivity;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.models.community.MainResponseModel;
import com.antizon.uit_android.models.company.profile.CompanyDeiStatementModel;
import com.antizon.uit_android.models.company.profile.CompanyProfileDataModel;
import com.antizon.uit_android.network.GetDataService;
import com.antizon.uit_android.network.RetrofitClientInstance;
import com.antizon.uit_android.utilities.CustomCookieToast;
import com.antizon.uit_android.utilities.Utilities;
import com.makeramen.roundedimageview.RoundedImageView;
import java.util.Objects;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompanyProfileDeiInfoActivity extends AppCompatActivity {
    Context context;
    GetDataService service;
    SessionManagement sessionManagement;
    ProgressDialog progressDialog;

    RelativeLayout btnBack, layout_acceptReject;
    TextView text_profileName, text_deiStatementDescription, text_deiLeadStatus, text_deiTrainingStatus, text_employmentResourceStatus,
            text_deiProgrammingStatus, text_editDeiStatement, text_editDeiLead, text_editDeiTraining, text_editEmployResource,
            text_editDeiProgramming, btnReject, btnApprove;
    RoundedImageView company_profileImage;

    CompanyProfileDataModel companyProfileDataModel;
    String visitProfileId;
    int accountStatus;
    AlertDialog deleteDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_profile_dei_info);
        Utilities.setCustomStatusAndNavColor(CompanyProfileDeiInfoActivity.this, R.color.app_color, R.color.white_dash);
        context = CompanyProfileDeiInfoActivity.this;
        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        sessionManagement = new SessionManagement(context);
        progressDialog = new ProgressDialog(context);

        companyProfileDataModel = getIntent().getParcelableExtra("companyProfileDataModel");
        accountStatus = getIntent().getIntExtra("accountStatus", accountStatus);
        visitProfileId = getIntent().getStringExtra("visitProfileId");

        btnBack = findViewById(R.id.btnBack);
        text_profileName = findViewById(R.id.text_profileName);
        company_profileImage = findViewById(R.id.company_profileImage);
        text_deiStatementDescription = findViewById(R.id.text_deiStatementDescription);
        text_deiLeadStatus = findViewById(R.id.text_deiLeadStatus);
        text_deiTrainingStatus = findViewById(R.id.text_deiTrainingStatus);
        text_employmentResourceStatus = findViewById(R.id.text_employmentResourceStatus);
        text_deiProgrammingStatus = findViewById(R.id.text_deiProgrammingStatus);
        text_editDeiStatement = findViewById(R.id.text_editDeiStatement);
        text_editDeiLead = findViewById(R.id.text_editDeiLead);
        text_editDeiTraining = findViewById(R.id.text_editDeiTraining);
        text_editEmployResource = findViewById(R.id.text_editEmployResource);
        text_editDeiProgramming = findViewById(R.id.text_editDeiProgramming);
        layout_acceptReject = findViewById(R.id.layout_acceptReject);
        btnReject = findViewById(R.id.btnReject);
        btnApprove = findViewById(R.id.btnApprove);

        if (!sessionManagement.getKeyId().equals(String.valueOf(companyProfileDataModel.getUser_id()))){
            text_editDeiStatement.setVisibility(View.GONE);
            text_editDeiLead.setVisibility(View.GONE);
            text_editDeiTraining.setVisibility(View.GONE);
            text_editEmployResource.setVisibility(View.GONE);
            text_editDeiProgramming.setVisibility(View.GONE);
        }

        Utilities.loadImage(context, companyProfileDataModel.getProfile_image(), company_profileImage);
        text_profileName.setText(companyProfileDataModel.getName());

        company_profileImage.setOnClickListener(v -> {
            Intent intent = new Intent(context, ImageViewerActivity.class);
            ViewCompat.setTransitionName(company_profileImage, companyProfileDataModel.getProfile_image());
            String animationName = ViewCompat.getTransitionName(company_profileImage);
            intent.putExtra("animationName", animationName);
            intent.putExtra("IMAGE", companyProfileDataModel.getProfile_image());
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(CompanyProfileDeiInfoActivity.this, company_profileImage, Objects.requireNonNull(animationName));
            startActivity(intent, options.toBundle());
        });

        CompanyDeiStatementModel companyDeiStatementModel = companyProfileDataModel.getCompanyDeiStatement();

        if (companyDeiStatementModel != null){
            text_deiStatementDescription.setText(companyDeiStatementModel.getDeistatement());

            text_editDeiStatement.setOnClickListener(v -> {
                Intent deiStatementIntent = new Intent(context, CompanyDEIStatementActivity.class);
                deiStatementIntent.putExtra("from", "edit");
                deiStatementIntent.putExtra("companyDeiStatement", companyDeiStatementModel.getDeistatement());
                onCompanyDeiStatementUpdateLauncher.launch(deiStatementIntent);
                overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
            });

            if (companyDeiStatementModel.getTeam_lead() == 1){
                text_deiLeadStatus.setText(R.string.yes);
            }else {
                text_deiLeadStatus.setText(R.string.no);
            }

            text_editDeiLead.setOnClickListener(v -> {
                Intent demoGraphIntent = new Intent(context, CompanyLeadActivity.class);
                demoGraphIntent.putExtra("from", "edit");
                demoGraphIntent.putExtra("companyTeamLead", companyDeiStatementModel.getTeam_lead());
                onCompanyLeadUpdateLauncher.launch(demoGraphIntent);
                overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
            });

            if (companyDeiStatementModel.getTraining() == 1){
                text_deiTrainingStatus.setText(R.string.yes);
            }else {
                text_deiTrainingStatus.setText(R.string.no);
            }

            text_editDeiTraining.setOnClickListener(v -> {
                Intent demoGraphIntent = new Intent(context, CompanyDEITrainingActivity.class);
                demoGraphIntent.putExtra("from", "edit");
                demoGraphIntent.putExtra("companyTraining", companyDeiStatementModel.getTraining());
                onCompanyTrainingUpdateLauncher.launch(demoGraphIntent);
                overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
            });


            if (companyDeiStatementModel.getErg() == 1){
                text_employmentResourceStatus.setText(R.string.yes);
            }else {
                text_employmentResourceStatus.setText(R.string.no);
            }

            text_editEmployResource.setOnClickListener(v -> {
                Intent demoGraphIntent = new Intent(context, CompanyErgActivity.class);
                demoGraphIntent.putExtra("from", "edit");
                demoGraphIntent.putExtra("companyErg", companyDeiStatementModel.getErg());
                onCompanyErgUpdateLauncher.launch(demoGraphIntent);
                overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
            });


            if (companyDeiStatementModel.getProgramming() == 1){
                text_deiProgrammingStatus.setText(R.string.yes);
            }else {
                text_deiProgrammingStatus.setText(R.string.no);
            }

            text_editDeiProgramming.setOnClickListener(v -> {
                Intent demoGraphIntent = new Intent(context, CompanyDEIProgrammingActivity.class);
                demoGraphIntent.putExtra("from", "edit");
                demoGraphIntent.putExtra("companyProgramming", companyDeiStatementModel.getProgramming());
                onCompanyProgrammingUpdateLauncher.launch(demoGraphIntent);
                overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
            });


        }

        btnBack.setOnClickListener(v -> onBackPressed());

        if (sessionManagement.getRole().equals("1") || sessionManagement.getRole().equals("3")) {
            if (accountStatus == 4){
                layout_acceptReject.setVisibility(View.VISIBLE);
                btnReject.setText(context.getString(R.string.reject));
                btnApprove.setText(context.getString(R.string.approve));
                btnApprove.setBackgroundResource(R.drawable.app_color_7dp_rounded);

                btnReject.setOnClickListener(v -> showAcceptRejectCompanyRequestPopup(visitProfileId, "decline"));

                btnApprove.setOnClickListener(v -> showAcceptRejectCompanyRequestPopup(visitProfileId, "approve"));


            }else if (accountStatus == 1){
                layout_acceptReject.setVisibility(View.VISIBLE);
                btnReject.setText(context.getString(R.string.delete));
                btnApprove.setText(context.getString(R.string.pause));
                btnApprove.setBackgroundResource(R.drawable.yellow_color_7dp_rounded);

                btnApprove.setOnClickListener(v -> showAcceptRejectCompanyRequestPopup(visitProfileId, "pause"));

                btnReject.setOnClickListener(v -> showAcceptRejectCompanyRequestPopup(visitProfileId, "delete"));

            }else {
                layout_acceptReject.setVisibility(View.GONE);
            }

        }else {
            layout_acceptReject.setVisibility(View.GONE);
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    private void showAcceptRejectCompanyRequestPopup(String companyId, String type) {

        AlertDialog.Builder builder = new AlertDialog.Builder(CompanyProfileDeiInfoActivity.this, R.style.CustomDialog);
        final View customLayout = LayoutInflater.from(CompanyProfileDeiInfoActivity.this).inflate(R.layout.popup_logout_user, null);
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
                progressDialog.setMessage("Approving...");
                break;
            case "decline":
                headerText = "Reject Company";
                areYouSure = "Are you sure you want to reject this company";
                progressDialog.setMessage("Rejecting...");
                break;
            case "pause":
                headerText = "Pause Company";
                areYouSure = "Are you sure you want to pause this company";
                progressDialog.setMessage("Pausing...");
                break;
            default:
                headerText = "Delete Company";
                areYouSure = "Are you sure you want to delete this company";
                progressDialog.setMessage("Deleting...");
                break;
        }

        text_sure.setText(areYouSure);
        text_blockThisPerson.setText(headerText);

        btn_no.setText(noText);
        btn_yes.setText(yesText);


        btn_no.setOnClickListener(view -> deleteDialog.dismiss());

        btn_yes.setOnClickListener(view -> {
            deleteDialog.dismiss();
            progressDialog.show();
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
                            CustomCookieToast.showFailureToast(CompanyProfileDeiInfoActivity.this, response.body().getMessage());
                        }
                    }else {
                        CustomCookieToast.showFailureToast(CompanyProfileDeiInfoActivity.this, response.message());
                    }
                }else {
                    CustomCookieToast.showFailureToast(CompanyProfileDeiInfoActivity.this, response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<MainResponseModel> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                CustomCookieToast.showFailureToast(CompanyProfileDeiInfoActivity.this, t.getLocalizedMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("companyProfileDataModel", companyProfileDataModel);
        setResult(RESULT_OK, intent);
        finish();
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }


    ActivityResultLauncher<Intent> onCompanyDeiStatementUpdateLauncher= registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent resultIntent = result.getData();
            if (resultIntent !=null){
                String companyDeiStatement = resultIntent.getStringExtra("companyDeiStatement");
                text_deiStatementDescription.setText(companyDeiStatement);
                companyProfileDataModel.getCompanyDeiStatement().setDeistatement(companyDeiStatement);
            }

        }
    });

    ActivityResultLauncher<Intent> onCompanyLeadUpdateLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent resultIntent = result.getData();
            if (resultIntent !=null){
                int companyTeamLead = resultIntent.getIntExtra("companyTeamLead", 0);

                if (companyTeamLead == 1){
                    text_deiLeadStatus.setText(R.string.yes);
                }else {
                    text_deiLeadStatus.setText(R.string.no);
                }

                companyProfileDataModel.getCompanyDeiStatement().setTeam_lead(companyTeamLead);
            }

        }
    });

    ActivityResultLauncher<Intent> onCompanyErgUpdateLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent resultIntent = result.getData();
            if (resultIntent !=null){
                int companyErg = resultIntent.getIntExtra("companyErg", 0);

                if (companyErg == 1){
                    text_employmentResourceStatus.setText(R.string.yes);
                }else {
                    text_employmentResourceStatus.setText(R.string.no);
                }

                companyProfileDataModel.getCompanyDeiStatement().setErg(companyErg);
            }

        }
    });

    ActivityResultLauncher<Intent> onCompanyProgrammingUpdateLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent resultIntent = result.getData();
            if (resultIntent !=null){
                int companyProgramming = resultIntent.getIntExtra("companyProgramming", 0);

                if (companyProgramming == 1){
                    text_deiProgrammingStatus.setText(R.string.yes);
                }else {
                    text_deiProgrammingStatus.setText(R.string.no);
                }
                companyProfileDataModel.getCompanyDeiStatement().setProgramming(companyProgramming);
            }

        }
    });

    ActivityResultLauncher<Intent> onCompanyTrainingUpdateLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent resultIntent = result.getData();
            if (resultIntent !=null){
                int companyTraining = resultIntent.getIntExtra("companyTraining", 0);

                if (companyTraining == 1){
                    text_deiTrainingStatus.setText(R.string.yes);
                }else {
                    text_deiTrainingStatus.setText(R.string.no);
                }
                companyProfileDataModel.getCompanyDeiStatement().setTeam_lead(companyTraining);
            }

        }
    });

}