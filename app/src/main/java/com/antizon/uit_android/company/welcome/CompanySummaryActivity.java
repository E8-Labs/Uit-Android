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
import com.antizon.uit_android.models.company.profile.CompanyDeiStatementModel;
import com.antizon.uit_android.models.company.profile.CompanyProfileDataModel;
import com.antizon.uit_android.models.company.profile.CompanyProfileResponseModel;
import com.antizon.uit_android.network.GetDataService;
import com.antizon.uit_android.network.RetrofitClientInstance;
import com.antizon.uit_android.utilities.CustomCookieToast;
import com.antizon.uit_android.utilities.Utilities;
import com.makeramen.roundedimageview.RoundedImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompanySummaryActivity extends AppCompatActivity {
    Context context;
    SessionManagement sessionManagement;
    GetDataService service;
    ProgressDialog progressDialog;

    RelativeLayout btnBack, btnConfirm;
    TextView text_name, text_companyEmail, text_website, text_companyBioDescription, text_userLocation, text_userState, text_companyWebsite, text_companyStageName, text_companySizeName, text_deiStatementDescription, text_deiLeadStatus, text_deiTrainingStatus, text_employmentResourceStatus, text_deiProgrammingStatus;
    RoundedImageView image_companyProfile;

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
                                text_companyStageName.setText(profileDataModel.getUserStagesList().get(0).getName());
                            }
                            if (profileDataModel.getUserSizesList() !=null){
                                text_companySizeName.setText(profileDataModel.getUserSizesList().get(0).getName());
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