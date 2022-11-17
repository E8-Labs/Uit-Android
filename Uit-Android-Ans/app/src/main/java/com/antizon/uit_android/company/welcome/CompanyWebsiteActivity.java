package com.antizon.uit_android.company.welcome;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.antizon.uit_android.R;
import com.antizon.uit_android.generic.model.ModelCompanySize;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.models.applicant.degree.ApplicantDegreeDataModel;
import com.antizon.uit_android.models.community.MainResponseModel;
import com.antizon.uit_android.network.GetDataService;
import com.antizon.uit_android.network.RetrofitClientInstance;
import com.antizon.uit_android.utilities.CustomCookieToast;
import com.antizon.uit_android.utilities.Utilities;
import com.makeramen.roundedimageview.RoundedImageView;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompanyWebsiteActivity extends AppCompatActivity {
    Context context;
    GetDataService service;
    SessionManagement sessionManagement;
    ProgressDialog progressDialog;
    ImageView backIcon;
    RoundedImageView redNoah2;
    TextView next;
    EditText editText_companyWebsite;
    String  typeHereValue = "", companyNameHintValue = "", encodedImageData = "", city="", state = "" ,websiteValue="", from, companyWebsite;
    double latitude, longitude;

    ArrayList<ModelCompanySize>  selectedCompanyInterestInStageList, selectedCompanyInSizeList;
    ArrayList<ApplicantDegreeDataModel> selectedCompanyIndustries;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_website);
        Utilities.setWhiteBars(CompanyWebsiteActivity.this);
        context = CompanyWebsiteActivity.this;
        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Updating...");

        initViews();


    }


    private void initViews() {
        if (getIntent() != null) {
            from = getIntent().getStringExtra("from");
            if (from.equals("add")){
                selectedCompanyIndustries = new ArrayList<>();
                selectedCompanyInterestInStageList = new ArrayList<>();
                selectedCompanyInSizeList = new ArrayList<>();

                encodedImageData = getIntent().getStringExtra("profilePic");
                companyNameHintValue = getIntent().getStringExtra("companyName");
                typeHereValue = getIntent().getStringExtra("bio");
                latitude = getIntent().getDoubleExtra("latitude", latitude);
                longitude = getIntent().getDoubleExtra("latitude", longitude);
                selectedCompanyIndustries = getIntent().getParcelableArrayListExtra("selectedCompanyIndustries");
                selectedCompanyInterestInStageList = getIntent().getParcelableArrayListExtra("selectedCompanyInterestInStageList");
                selectedCompanyInSizeList = getIntent().getParcelableArrayListExtra("selectedCompanyInSizeList");
                city = getIntent().getStringExtra("city");
                state = getIntent().getStringExtra("state");
            }else {
                companyWebsite = getIntent().getStringExtra("companyWebsite");
            }
        }

        redNoah2 = findViewById(R.id.redNoah2);
        backIcon = findViewById(R.id.backIcon);
        next = findViewById(R.id.next);
        editText_companyWebsite = findViewById(R.id.editText_companyWebsite);


        if (from.equals("edit")){
            sessionManagement = new SessionManagement(context);
            Utilities.loadImage(context, sessionManagement.getProfileImage(), redNoah2);
            editText_companyWebsite.setText(companyWebsite);
        }else {
            Utilities.loadImage(CompanyWebsiteActivity.this, encodedImageData, redNoah2);
        }



        backIcon.setOnClickListener(v -> onBackPressed());

        next.setOnClickListener(v -> {
            websiteValue = editText_companyWebsite.getText().toString();
            if (websiteValue.isEmpty()) {
                CustomCookieToast.showRequiredToast(CompanyWebsiteActivity.this, "Please enter company website");
            }else if (!Utilities.isValidUrl(websiteValue)) {
                CustomCookieToast.showRequiredToast(CompanyWebsiteActivity.this, "Please enter proper company website url");
            } else {
                Utilities.hideKeyboard(editText_companyWebsite, context);

                if (from.equals("edit")){
                    progressDialog.show();
                    requestForUpdateCompanyWebsite("Bearer " + sessionManagement.getToken(), websiteValue);
                }else {
                    openNextScreen();
                }

            }
        });

    }

    private void openNextScreen() {
        Intent intent = new Intent(CompanyWebsiteActivity.this, CompanyEmailAddressActivity.class);
        intent.putExtra("profilePic", encodedImageData);
        intent.putExtra("companyName", companyNameHintValue);
        intent.putExtra("bio", typeHereValue);
        intent.putExtra("city", city);
        intent.putExtra("state", state);
        intent.putExtra("latitude", latitude);
        intent.putExtra("longitude", longitude);
        intent.putExtra("website", websiteValue);
        intent.putParcelableArrayListExtra("selectedCompanyIndustries", selectedCompanyIndustries);
        intent.putParcelableArrayListExtra("selectedCompanyInterestInStageList", selectedCompanyInterestInStageList);
        intent.putParcelableArrayListExtra("selectedCompanyInSizeList", selectedCompanyInSizeList);
        intent.putExtra("from", "add");
        startActivity(intent);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }

    private void requestForUpdateCompanyWebsite(String authToken, String companyWebsite){
        Call<MainResponseModel> call = service.updateCompanyWebsite(authToken, companyWebsite);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NotNull Call<MainResponseModel> call, @NotNull Response<MainResponseModel> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()){
                    assert response.body() != null;
                    boolean status = response.body().isStatus();
                    if (status) {
                        Intent intent = new Intent();
                        intent.putExtra("companyWebsite", companyWebsite);
                        setResult(RESULT_OK, intent);
                        finish();
                        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                    } else {
                        CustomCookieToast.showFailureToast(CompanyWebsiteActivity.this, "Error!", response.body().getMessage());
                    }
                }else {
                    CustomCookieToast.showFailureToast(CompanyWebsiteActivity.this, "Error!", response.message());
                }
            }

            @Override
            public void onFailure(@NotNull Call<MainResponseModel> call, @NotNull Throwable t) {
                progressDialog.dismiss();
                CustomCookieToast.showFailureToast(CompanyWebsiteActivity.this, "Error!", t.getMessage());
            }
        });
    }
}