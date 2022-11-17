package com.antizon.uit_android.company.welcome;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.antizon.uit_android.R;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.models.PercentageData;
import com.antizon.uit_android.models.company.profile.CompanyDeiStatementModel;
import com.antizon.uit_android.models.company.profile.CompanyProfileDataModel;
import com.antizon.uit_android.models.company.profile.CompanyProfileResponseModel;
import com.antizon.uit_android.network.GetDataService;
import com.antizon.uit_android.network.RetrofitClientInstance;
import com.antizon.uit_android.utilities.CustomCookieToast;
import com.antizon.uit_android.utilities.PercentageGraph;
import com.antizon.uit_android.utilities.Utilities;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompanySummaryActivity extends AppCompatActivity {
    Context context;
    SessionManagement sessionManagement;
    GetDataService service;
    ProgressDialog progressDialog;

    RelativeLayout btnBack, btnConfirm, gender_proportionGraph, lgbtq_populationGraph;
    TextView text_name, text_companyEmail, text_website, text_companyBioDescription, text_userLocation, text_userState, text_companyWebsite, text_companyStageName, text_companySizeName, text_deiStatementDescription, text_deiLeadStatus, text_deiTrainingStatus, text_employmentResourceStatus, text_deiProgrammingStatus;
    RoundedImageView image_companyProfile;

    PercentageGraph proportionGraph, populationGraph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_summary);
        Utilities.setWhiteBars(CompanySummaryActivity.this);
        context = CompanySummaryActivity.this;
        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        sessionManagement = new SessionManagement(context);
        progressDialog = new ProgressDialog(context);

        initViews();
    }


    private void initViews(){
        btnBack = findViewById(R.id.btnBack);
        btnConfirm = findViewById(R.id.btnConfirm);
        text_name = findViewById(R.id.text_name);
        image_companyProfile = findViewById(R.id.image_companyProfile);
        text_companyEmail = findViewById(R.id.text_companyEmail);
        text_website = findViewById(R.id.text_website);
        text_companyBioDescription = findViewById(R.id.text_companyBioDescription);
        text_userLocation = findViewById(R.id.text_userLocation);
        text_userState = findViewById(R.id.text_userState);
        text_companyWebsite = findViewById(R.id.text_companyWebsite);
        text_companyStageName = findViewById(R.id.text_companyStageName);
        text_companySizeName = findViewById(R.id.text_companySizeName);
        text_deiStatementDescription = findViewById(R.id.text_deiStatementDescription);
        text_deiLeadStatus = findViewById(R.id.text_deiLeadStatus);
        text_deiTrainingStatus = findViewById(R.id.text_deiTrainingStatus);
        text_employmentResourceStatus = findViewById(R.id.text_employmentResourceStatus);
        text_deiProgrammingStatus = findViewById(R.id.text_deiProgrammingStatus);
        gender_proportionGraph = findViewById(R.id.gender_proportionGraph);
        lgbtq_populationGraph = findViewById(R.id.lgbtq_populationGraph);

        btnConfirm.setOnClickListener(v -> openNextScreen());

        progressDialog.setMessage("Loading.....");
        progressDialog.show();
        requestForCompanyProfile("Bearer " + sessionManagement.getToken(), sessionManagement.getKeyId());


    }


    public void requestForCompanyProfile(String authToken, String userId) {

        Call<CompanyProfileResponseModel> call = service.getCompanyProfile(authToken, userId);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<CompanyProfileResponseModel> call, @NonNull Response<CompanyProfileResponseModel> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().isStatus()) {
                        CompanyProfileDataModel profileDataModel = response.body().getDataModel();
                        if (profileDataModel!=null){

                            Utilities.loadImage(context, profileDataModel.getProfile_image(), image_companyProfile);
                            text_name.setText(profileDataModel.getName());
                            text_companyEmail.setText(profileDataModel.getEmail());
                            text_website.setText(profileDataModel.getWebsite());
                            text_companyBioDescription.setText(profileDataModel.getBio());
                            text_userLocation.setText(profileDataModel.getCity());
                            text_userState.setText(profileDataModel.getState());
                            text_companyWebsite.setText(profileDataModel.getWebsite());

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

                            CompanyDeiStatementModel companyDeiStatementModel = profileDataModel.getCompanyDeiStatement();
                            if (companyDeiStatementModel != null){
                                text_deiStatementDescription.setText(companyDeiStatementModel.getDeistatement());
                                if (companyDeiStatementModel.getTeam_lead() == 1){
                                    text_deiLeadStatus.setText(R.string.yes);
                                }else {
                                    text_deiLeadStatus.setText(R.string.no);
                                }

                                if (companyDeiStatementModel.getTraining() == 1){
                                    text_deiTrainingStatus.setText(R.string.yes);
                                }else {
                                    text_deiTrainingStatus.setText(R.string.no);
                                }

                                if (companyDeiStatementModel.getErg() == 1){
                                    text_employmentResourceStatus.setText(R.string.yes);
                                }else {
                                    text_employmentResourceStatus.setText(R.string.no);
                                }

                                if (companyDeiStatementModel.getProgramming() == 1){
                                    text_deiProgrammingStatus.setText(R.string.yes);
                                }else {
                                    text_deiProgrammingStatus.setText(R.string.no);
                                }


                                proportionGraph = new PercentageGraph(context);
                                proportionGraph.setTextColor(R.color.white);
                                proportionGraph.setTextSize(30);
                                proportionGraph.setFontFamily(R.font.poppins_medium);
                                gender_proportionGraph.addView(proportionGraph);

                                if (profileDataModel.getCompanyDeiStatement() !=null) {
                                    proportionGraph.setOnInitializedListener(() -> {
                                        // set data after initialization
                                        List<PercentageData> proportionList = new ArrayList<>();

                                        if (profileDataModel.getCompanyDeiStatement().getMen() == 100){
                                            proportionList.add(new PercentageData(profileDataModel.getCompanyDeiStatement().getMen(), R.color.app_color));
                                        }else if (profileDataModel.getCompanyDeiStatement().getWomen() == 100){
                                            proportionList.add(new PercentageData(profileDataModel.getCompanyDeiStatement().getWomen(), R.color.yellow));
                                        }else if (profileDataModel.getCompanyDeiStatement().getNon_binary() == 100){
                                            proportionList.add(new PercentageData(profileDataModel.getCompanyDeiStatement().getNon_binary(), R.color.gray));
                                        }else {
                                            proportionList.add(new PercentageData(profileDataModel.getCompanyDeiStatement().getMen(), R.color.app_color));
                                            proportionList.add(new PercentageData(profileDataModel.getCompanyDeiStatement().getWomen(), R.color.yellow));
                                            proportionList.add(new PercentageData(profileDataModel.getCompanyDeiStatement().getNon_binary(), R.color.gray));
                                        }
                                        proportionGraph.setData(proportionList);
                                    });
                                }else {
                                    proportionGraph.setOnInitializedListener(() -> {
                                        // set data after initialization
                                        List<PercentageData> data = new ArrayList<>();
                                        data.add(new PercentageData(0, R.color.grayCircle));
                                        proportionGraph.setData(data);
                                    });
                                }


                            }

                            populationGraph = new PercentageGraph(context);
                            populationGraph.setTextColor(R.color.white);
                            populationGraph.setTextSize(30);
                            populationGraph.setFontFamily(R.font.poppins_medium);
                            lgbtq_populationGraph.addView(populationGraph);


                            if (profileDataModel.getDemoGraphicInfoModel() !=null){
                                populationGraph.setOnInitializedListener(() -> {
                                    // set data after initialization
                                    List<PercentageData> data = new ArrayList<>();
                                    data.add(new PercentageData((int)profileDataModel.getCompanyDeiStatement().getLgbt(), R.color.app_color));
                                    populationGraph.setData(data);
                                });
                            }else {
                                populationGraph.setOnInitializedListener(() -> {
                                    // set data after initialization
                                    List<PercentageData> data = new ArrayList<>();
                                    data.add(new PercentageData(0, R.color.app_color));
                                    populationGraph.setData(data);
                                });
                            }


                        }
                    }else {
                        CustomCookieToast.showFailureToast(CompanySummaryActivity.this, "Failure!", response.body().getMessage());
                    }
                } else {
                    CustomCookieToast.showFailureToast(CompanySummaryActivity.this, "Failure!", response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<CompanyProfileResponseModel> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                CustomCookieToast.showFailureToast(CompanySummaryActivity.this, "Failure!", t.getMessage());
            }
        });

    }

    private void openNextScreen() {
        Intent intent = new Intent(CompanySummaryActivity.this, CompanyCreationCongratulationActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }

    @Override
    public void onBackPressed() {

    }
}