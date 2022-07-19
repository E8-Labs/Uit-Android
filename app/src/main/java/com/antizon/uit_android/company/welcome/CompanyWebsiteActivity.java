package com.antizon.uit_android.company.welcome;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.antizon.uit_android.R;
import com.antizon.uit_android.generic.model.ModelCompanySize;
import com.antizon.uit_android.models.applicant.degree.ApplicantDegreeDataModel;
import com.antizon.uit_android.utilities.CustomCookieToast;
import com.antizon.uit_android.utilities.Utilities;
import com.makeramen.roundedimageview.RoundedImageView;
import java.util.ArrayList;

public class CompanyWebsiteActivity extends AppCompatActivity {
    Context context;

    ImageView backIcon;
    RoundedImageView redNoah2;
    TextView next;
    EditText urlText;
    String urlTextValue;
    String  typeHereValue = "", companyNameHintValue = "", encodedImageData = "",headquarterValue="",websiteValue="www.google.com";
    double latitude, longitude;

    ArrayList<ModelCompanySize>  selectedCompanyInterestInStageList, selectedCompanyInSizeList;
    ArrayList<ApplicantDegreeDataModel> selectedCompanyIndustries;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_website);
        context = CompanyWebsiteActivity.this;
        Utilities.setWhiteBars(CompanyWebsiteActivity.this);
        initViews();
    }


    void initViews() {
        redNoah2 = findViewById(R.id.redNoah2);
        backIcon = findViewById(R.id.backIcon);
        next = findViewById(R.id.next);
        urlText = findViewById(R.id.urlText);

        if (getIntent() != null) {
            encodedImageData = getIntent().getStringExtra("profilePic");
            companyNameHintValue = getIntent().getStringExtra("companyName");
            typeHereValue = getIntent().getStringExtra("bio");
            latitude = getIntent().getDoubleExtra("latitude", latitude);
            longitude = getIntent().getDoubleExtra("latitude", longitude);
            selectedCompanyIndustries = getIntent().getParcelableArrayListExtra("selectedCompanyIndustries");
            selectedCompanyInterestInStageList = getIntent().getParcelableArrayListExtra("selectedCompanyInterestInStageList");
            selectedCompanyInSizeList = getIntent().getParcelableArrayListExtra("selectedCompanyInSizeList");
            headquarterValue = getIntent().getStringExtra("headquarter");
        }

        Utilities.loadImage(CompanyWebsiteActivity.this, encodedImageData, redNoah2);

        backIcon.setOnClickListener(v -> onBackPressed());

        next.setOnClickListener(v -> {
            urlTextValue = urlText.getText().toString();
            if (urlTextValue.isEmpty()) {
                CustomCookieToast.showRequiredToast(CompanyWebsiteActivity.this, "Please enter company website");
            }else if (!Utilities.isValidUrl(urlTextValue)) {
                CustomCookieToast.showRequiredToast(CompanyWebsiteActivity.this, "Please enter proper company website url");
            } else {
                Utilities.hideKeyboard(urlText, context);
                openNextScreen();
            }
        });
    }

    void openNextScreen() {
        Intent intent = new Intent(CompanyWebsiteActivity.this, CompanyEmailAddressActivity.class);
        intent.putExtra("profilePic", encodedImageData);
        intent.putExtra("companyName", companyNameHintValue);
        intent.putExtra("bio", typeHereValue);
        intent.putExtra("headquarter", headquarterValue);
        intent.putExtra("website", websiteValue);
        intent.putExtra("latitude", latitude);
        intent.putExtra("longitude", longitude);
        intent.putParcelableArrayListExtra("selectedCompanyIndustries", selectedCompanyIndustries);
        intent.putParcelableArrayListExtra("selectedCompanyInterestInStageList", selectedCompanyInterestInStageList);
        intent.putParcelableArrayListExtra("selectedCompanyInSizeList", selectedCompanyInSizeList);
        startActivity(intent);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }
}