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
import com.antizon.uit_android.company.welcome.CompanyGenderOrientationActivity;
import com.antizon.uit_android.company.welcome.CompanyGenderProportionActivity;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.models.PercentageData;
import com.antizon.uit_android.models.community.MainResponseModel;
import com.antizon.uit_android.models.company.profile.CompanyProfileDataModel;
import com.antizon.uit_android.network.GetDataService;
import com.antizon.uit_android.network.RetrofitClientInstance;
import com.antizon.uit_android.utilities.CustomCookieToast;
import com.antizon.uit_android.utilities.PercentageGraph;
import com.antizon.uit_android.utilities.Utilities;
import com.makeramen.roundedimageview.RoundedImageView;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompanyProfileDemoGraphicInfoActivity extends AppCompatActivity {
    Context context;
    GetDataService service;
    SessionManagement sessionManagement;
    ProgressDialog progressDialog;

    RelativeLayout btnBack, gender_proportionGraph, lgbtq_populationGraph, layout_acceptReject;
    TextView text_profileName, text_editGenderProportion, text_editPopulation, btnReject, btnApprove;
    RoundedImageView company_profileImage;
    PercentageGraph proportionGraph, populationGraph;


    CompanyProfileDataModel companyProfileDataModel;
    String visitProfileId;
    int accountStatus;
    AlertDialog deleteDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_profile_demo_graphic_info);
        Utilities.setCustomStatusAndNavColor(CompanyProfileDemoGraphicInfoActivity.this, R.color.app_color, R.color.white_dash);
        context = CompanyProfileDemoGraphicInfoActivity.this;
        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        sessionManagement = new SessionManagement(context);
        progressDialog = new ProgressDialog(context);

        companyProfileDataModel = getIntent().getParcelableExtra("companyProfileDataModel");
        accountStatus = getIntent().getIntExtra("accountStatus", accountStatus);
        visitProfileId = getIntent().getStringExtra("visitProfileId");

        btnBack = findViewById(R.id.btnBack);
        text_profileName = findViewById(R.id.text_profileName);
        company_profileImage = findViewById(R.id.company_profileImage);
        gender_proportionGraph = findViewById(R.id.gender_proportionGraph);
        lgbtq_populationGraph = findViewById(R.id.lgbtq_populationGraph);
        text_editPopulation = findViewById(R.id.text_editPopulation);
        text_editGenderProportion = findViewById(R.id.text_editGenderProportion);
        layout_acceptReject = findViewById(R.id.layout_acceptReject);
        btnReject = findViewById(R.id.btnReject);
        btnApprove = findViewById(R.id.btnApprove);

        btnBack.setOnClickListener(v -> onBackPressed());

        if (!sessionManagement.getKeyId().equals(String.valueOf(companyProfileDataModel.getUser_id()))){
            text_editGenderProportion.setVisibility(View.GONE);
            text_editPopulation.setVisibility(View.GONE);
        }

        Utilities.loadImage(context, companyProfileDataModel.getProfile_image(), company_profileImage);
        text_profileName.setText(companyProfileDataModel.getName());

        company_profileImage.setOnClickListener(v -> {
            Intent intent = new Intent(context, ImageViewerActivity.class);
            ViewCompat.setTransitionName(company_profileImage, companyProfileDataModel.getProfile_image());
            String animationName = ViewCompat.getTransitionName(company_profileImage);
            intent.putExtra("animationName", animationName);
            intent.putExtra("IMAGE", companyProfileDataModel.getProfile_image());
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(CompanyProfileDemoGraphicInfoActivity.this, company_profileImage, Objects.requireNonNull(animationName));
            startActivity(intent, options.toBundle());
        });

        if (companyProfileDataModel.getCompanyDeiStatement()!=null){
            proportionGraph = new PercentageGraph(context);
            proportionGraph.setTextColor(R.color.white);
            proportionGraph.setTextSize(30);
            proportionGraph.setFontFamily(R.font.poppins_medium);
            gender_proportionGraph.addView(proportionGraph);

            proportionGraph.setOnInitializedListener(() -> {
                // set data after initialization
                List<PercentageData> proportionList = new ArrayList<>();
                if (companyProfileDataModel.getCompanyDeiStatement().getMen() == 100){
                    proportionList.add(new PercentageData(companyProfileDataModel.getCompanyDeiStatement().getMen(), R.color.app_color));
                }else if (companyProfileDataModel.getCompanyDeiStatement().getWomen() == 100){
                    proportionList.add(new PercentageData(companyProfileDataModel.getCompanyDeiStatement().getWomen(), R.color.yellow));
                }else if (companyProfileDataModel.getCompanyDeiStatement().getNon_binary() == 100){
                    proportionList.add(new PercentageData(companyProfileDataModel.getCompanyDeiStatement().getNon_binary(), R.color.gray));
                }else {
                    proportionList.add(new PercentageData(companyProfileDataModel.getCompanyDeiStatement().getMen(), R.color.app_color));
                    proportionList.add(new PercentageData(companyProfileDataModel.getCompanyDeiStatement().getWomen(), R.color.yellow));
                    proportionList.add(new PercentageData(companyProfileDataModel.getCompanyDeiStatement().getNon_binary(), R.color.gray));
                }

                proportionGraph.setData(proportionList);
            });

            text_editGenderProportion.setOnClickListener(v -> {
                Intent genderProportionIntent = new Intent(context, CompanyGenderProportionActivity.class);
                genderProportionIntent.putExtra("from", "edit");
                genderProportionIntent.putExtra("men", companyProfileDataModel.getCompanyDeiStatement().getMen());
                genderProportionIntent.putExtra("women", companyProfileDataModel.getCompanyDeiStatement().getWomen());
                genderProportionIntent.putExtra("nonBinary", companyProfileDataModel.getCompanyDeiStatement().getNon_binary());
                onCompanyGenderProportionUpdateLauncher.launch(genderProportionIntent);
                overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);

            });

            populationGraph = new PercentageGraph(context);
            populationGraph.setTextColor(R.color.white);
            populationGraph.setTextSize(30);
            populationGraph.setFontFamily(R.font.poppins_medium);
            lgbtq_populationGraph.addView(populationGraph);

            populationGraph.setOnInitializedListener(() -> {
                // set data after initialization
                List<PercentageData> data = new ArrayList<>();
                data.add(new PercentageData((int) companyProfileDataModel.getCompanyDeiStatement().getLgbt(), R.color.app_color));
                populationGraph.setData(data);
            });


            text_editPopulation.setOnClickListener(v -> {
                Intent genderProportionIntent = new Intent(context, CompanyGenderOrientationActivity.class);
                genderProportionIntent.putExtra("from", "edit");
                genderProportionIntent.putExtra("companyLgbt", companyProfileDataModel.getCompanyDeiStatement().getLgbt());
                onCompanyGenderOrientationUpdateLauncher.launch(genderProportionIntent);
                overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);

            });
        }


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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("companyProfileDataModel", companyProfileDataModel);
        setResult(RESULT_OK, intent);
        finish();
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void showAcceptRejectCompanyRequestPopup(String companyId, String type) {

        AlertDialog.Builder builder = new AlertDialog.Builder(CompanyProfileDemoGraphicInfoActivity.this, R.style.CustomDialog);
        final View customLayout = LayoutInflater.from(CompanyProfileDemoGraphicInfoActivity.this).inflate(R.layout.popup_logout_user, null);
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
                            CustomCookieToast.showFailureToast(CompanyProfileDemoGraphicInfoActivity.this, response.body().getMessage());
                        }
                    }else {
                        CustomCookieToast.showFailureToast(CompanyProfileDemoGraphicInfoActivity.this, response.message());
                    }
                }else {
                    CustomCookieToast.showFailureToast(CompanyProfileDemoGraphicInfoActivity.this, response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<MainResponseModel> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                CustomCookieToast.showFailureToast(CompanyProfileDemoGraphicInfoActivity.this, t.getLocalizedMessage());
            }
        });
    }

    ActivityResultLauncher<Intent> onCompanyGenderProportionUpdateLauncher= registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent resultIntent = result.getData();
            if (resultIntent !=null){
                int men = resultIntent.getIntExtra("men", 0);
                int women = resultIntent.getIntExtra("women", 0);
                int nonBinary = resultIntent.getIntExtra("nonBinary", 0);

                companyProfileDataModel.getCompanyDeiStatement().setMen(men);
                companyProfileDataModel.getCompanyDeiStatement().setWomen(women);
                companyProfileDataModel.getCompanyDeiStatement().setNon_binary(nonBinary);

                if (companyProfileDataModel.getCompanyDeiStatement()!=null){
                    proportionGraph = new PercentageGraph(context);
                    proportionGraph.setTextColor(R.color.white);
                    proportionGraph.setTextSize(30);
                    proportionGraph.setFontFamily(R.font.poppins_medium);
                    gender_proportionGraph.addView(proportionGraph);

                    proportionGraph.setOnInitializedListener(() -> {
                        // set data after initialization
                        List<PercentageData> proportionList = new ArrayList<>();
                        if (companyProfileDataModel.getCompanyDeiStatement().getMen() == 100){
                            proportionList.add(new PercentageData(companyProfileDataModel.getCompanyDeiStatement().getMen(), R.color.app_color));
                        }else if (companyProfileDataModel.getCompanyDeiStatement().getWomen() == 100){
                            proportionList.add(new PercentageData(companyProfileDataModel.getCompanyDeiStatement().getWomen(), R.color.yellow));
                        }else if (companyProfileDataModel.getCompanyDeiStatement().getNon_binary() == 100){
                            proportionList.add(new PercentageData(companyProfileDataModel.getCompanyDeiStatement().getNon_binary(), R.color.gray));
                        }else {
                            proportionList.add(new PercentageData(companyProfileDataModel.getCompanyDeiStatement().getMen(), R.color.app_color));
                            proportionList.add(new PercentageData(companyProfileDataModel.getCompanyDeiStatement().getWomen(), R.color.yellow));
                            proportionList.add(new PercentageData(companyProfileDataModel.getCompanyDeiStatement().getNon_binary(), R.color.gray));
                        }

                        proportionGraph.setData(proportionList);
                    });

                }

            }

        }
    });

    ActivityResultLauncher<Intent> onCompanyGenderOrientationUpdateLauncher= registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent resultIntent = result.getData();
            if (resultIntent !=null){
                int lgbt = resultIntent.getIntExtra("lgbt", 0);

                companyProfileDataModel.getCompanyDeiStatement().setLgbt(lgbt);

                populationGraph = new PercentageGraph(context);
                populationGraph.setTextColor(R.color.white);
                populationGraph.setTextSize(30);
                populationGraph.setFontFamily(R.font.poppins_medium);
                lgbtq_populationGraph.addView(populationGraph);

                populationGraph.setOnInitializedListener(() -> {
                    // set data after initialization
                    List<PercentageData> data = new ArrayList<>();
                    data.add(new PercentageData((int) companyProfileDataModel.getCompanyDeiStatement().getLgbt(), R.color.app_color));
                    populationGraph.setData(data);
                });

            }

        }
    });

}