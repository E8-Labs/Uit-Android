package com.antizon.uit_android.company.welcome;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.antizon.uit_android.R;
import com.antizon.uit_android.company.utility.BaseActivity;
import com.antizon.uit_android.generic.model.ModelCompanySize;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.models.applicant.degree.ApplicantDegreeDataModel;
import com.antizon.uit_android.models.company.signup.CompanyAccessTokenModel;
import com.antizon.uit_android.models.company.signup.CompanyProfileDataModel;
import com.antizon.uit_android.models.company.signup.CompanySignupResponseModel;
import com.antizon.uit_android.network.GetDataService;
import com.antizon.uit_android.network.RetrofitClientInstance;
import com.antizon.uit_android.utilities.CustomCookieToast;
import com.antizon.uit_android.utilities.Utilities;
import org.jetbrains.annotations.NotNull;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompanyPasswordActivity extends BaseActivity {
    Context context;
    GetDataService service;
    ProgressDialog progressDialog;
    SessionManagement sessionManagement;

    ImageView backIcon, redNoah2;
    TextView next;
    EditText passwordEditText;

    String typeHereValue = "", companyNameHintValue = "", encodedImageData = "", headquarterValue = "", websiteValue, emailValue = "", verificationValue = "", passwordValue = "";
    double latitude, longitude;
    ArrayList<ModelCompanySize> selectedCompanyInterestInStageList, selectedCompanyInSizeList;

    ArrayList<ApplicantDegreeDataModel> selectedCompanyIndustries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_password);
        Utilities.setWhiteBars(CompanyPasswordActivity.this);
        context = CompanyPasswordActivity.this;
        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        progressDialog = new ProgressDialog(CompanyPasswordActivity.this);
        sessionManagement = new SessionManagement(context);


        initViews();
        setListener();
    }


    void initViews() {
        redNoah2 = findViewById(R.id.redNoah2);
        backIcon = findViewById(R.id.backIcon);
        next = findViewById(R.id.next);
        passwordEditText = findViewById(R.id.passwordEditText);

        if (getIntent() != null) {
            encodedImageData = getIntent().getStringExtra("profilePic");
            companyNameHintValue = getIntent().getStringExtra("companyName");
            typeHereValue = getIntent().getStringExtra("bio");
            headquarterValue = getIntent().getStringExtra("headquarter");
            latitude = getIntent().getDoubleExtra("latitude", latitude);
            longitude = getIntent().getDoubleExtra("longitude", longitude);

            websiteValue = getIntent().getStringExtra("website");
            emailValue = getIntent().getStringExtra("email");
            verificationValue = getIntent().getStringExtra("verification");

            selectedCompanyIndustries = getIntent().getParcelableArrayListExtra("selectedCompanyIndustries");
            selectedCompanyInterestInStageList = getIntent().getParcelableArrayListExtra("selectedCompanyInterestInStageList");
            selectedCompanyInSizeList = getIntent().getParcelableArrayListExtra("selectedCompanyInSizeList");
        }

        loadProfile(CompanyPasswordActivity.this, encodedImageData, redNoah2);
    }

    private void setListener() {
        backIcon.setOnClickListener(v -> onBackPressed());
        next.setOnClickListener(v -> {
            passwordValue = passwordEditText.getText().toString().trim();
            if (passwordValue.isEmpty()) {
                CustomCookieToast.showRequiredToast(CompanyPasswordActivity.this, "Please enter password");
            } else {
                hideSoftKeyboard(CompanyPasswordActivity.this, passwordEditText);
                progressDialog.setMessage("Creating Account...");
                progressDialog.show();
                requestForCreateCompanyAccount(companyNameHintValue, emailValue, passwordValue, selectedCompanyInterestInStageList, selectedCompanyInSizeList, typeHereValue, selectedCompanyIndustries, headquarterValue, latitude, longitude, getRealPathFromURI(Uri.parse(encodedImageData)), websiteValue);
            }
        });
    }


    private void requestForCreateCompanyAccount(String name, String email, String password, List<ModelCompanySize> companyStagesList, List<ModelCompanySize> companySizesList, String bio, List<ApplicantDegreeDataModel> companyIndustryList, String city, double latitude, double longitude, String profileImage, String websiteValue) {

        String stage = "", size = "";

        if (companyStagesList.size() == 1){
            stage = String.valueOf(companyStagesList.get(0).getId());
        }

        if (companySizesList.size() == 1){
            size = String.valueOf(companySizesList.get(0).getId());
        }

        RequestBody[] industriesRequestBody = new RequestBody[companyIndustryList.size()];
        for (int i = 0; i < companyIndustryList.size(); i++) {
            industriesRequestBody[i] = RequestBody.create(String.valueOf(companyIndustryList.get(i).getId()), MediaType.parse("text/plain"));
        }

        RequestBody requestBodyName = RequestBody.create(name, MediaType.parse("text/plain"));
        RequestBody requestBodyEmail = RequestBody.create(email, MediaType.parse("text/plain"));
        RequestBody requestBodyPassword = RequestBody.create(password, MediaType.parse("text/plain"));
        RequestBody requestBodyStage = RequestBody.create(stage, MediaType.parse("text/plain"));
        RequestBody requestBodySize = RequestBody.create(size, MediaType.parse("text/plain"));
        RequestBody requestBodyBio = RequestBody.create(bio, MediaType.parse("text/plain"));
        RequestBody requestBodyLatitude = RequestBody.create(String.valueOf(latitude), MediaType.parse("text/plain"));
        RequestBody requestBodyLongitude = RequestBody.create(String.valueOf(longitude), MediaType.parse("text/plain"));
        RequestBody requestBodyCity = RequestBody.create(city, MediaType.parse("text/plain"));
        RequestBody requestBodyState = RequestBody.create("CA", MediaType.parse("text/plain"));
        RequestBody requestBodyWebsite = RequestBody.create(websiteValue, MediaType.parse("text/plain"));

        File image = new File(profileImage);
        RequestBody requestBodyPostMedia = RequestBody.create(image, MediaType.parse("*/*"));
        MultipartBody.Part profileImageMultiPart = MultipartBody.Part.createFormData("profile_image", image.getName(), requestBodyPostMedia);

        Call<CompanySignupResponseModel> call = service.registerCompany(requestBodyName, requestBodyEmail, requestBodyPassword, requestBodyStage, requestBodySize, requestBodyBio, industriesRequestBody, requestBodyLatitude, requestBodyLongitude, requestBodyCity, requestBodyState, requestBodyWebsite, profileImageMultiPart);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NotNull Call<CompanySignupResponseModel> call, @NotNull Response<CompanySignupResponseModel> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().isStatus()) {
                        CompanyProfileDataModel profileDataModel = response.body().getDataModel().getProfileDataModel();
                        CompanyAccessTokenModel companyAccessTokenModel = response.body().getDataModel().getAccessTokenModel();

                        sessionManagement.createLoginSession("" + profileDataModel.getUser_id(), email, name, passwordValue, websiteValue, profileDataModel.getProfile_image(),
                                "", city, "", "", "" , bio, "", "", "" + 2,
                                companyAccessTokenModel.getToken(), "" + 1);
                        openNextScreen();
                    } else {
                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(context, "un successful : " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<CompanySignupResponseModel> call, @NotNull Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(context, "failure : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openNextScreen() {
        finishAffinity();
        Intent intent = new Intent(CompanyPasswordActivity.this, CompanyConnectGreenHouseActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }
}

