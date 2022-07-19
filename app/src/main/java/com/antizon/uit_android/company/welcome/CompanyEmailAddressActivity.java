package com.antizon.uit_android.company.welcome;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.VolleyError;
import com.antizon.uit_android.R;
import com.antizon.uit_android.company.utility.BaseActivity;
import com.antizon.uit_android.generic.model.ModelCompanySize;
import com.antizon.uit_android.generic_utils.AppConstants;
import com.antizon.uit_android.models.applicant.degree.ApplicantDegreeDataModel;
import com.antizon.uit_android.utilities.CustomCookieToast;
import com.antizon.uit_android.utilities.Utilities;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;

public class CompanyEmailAddressActivity extends BaseActivity {

    ProgressDialog progressDialog;
    ImageView backIcon, redNoah2;
    TextView next;
    EditText emailEditText;

    String typeHereValue = "", companyNameHintValue = "", encodedImageData = "", headquarterValue = "", websiteValue="", emailValue = "";
    double latitude, longitude;

    ArrayList<ModelCompanySize>  selectedCompanyInterestInStageList, selectedCompanyInSizeList;
    ArrayList<ApplicantDegreeDataModel> selectedCompanyIndustries;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_email_address);
        Utilities.setWhiteBars(CompanyEmailAddressActivity.this);
        initViews();
    }

    void initViews() {
        progressDialog = new ProgressDialog(this);
        backIcon = findViewById(R.id.backIcon);
        next = findViewById(R.id.next);
        redNoah2 = findViewById(R.id.redNoah2);
        emailEditText = findViewById(R.id.emailEditText);

        if (getIntent() != null) {
            encodedImageData = getIntent().getStringExtra("profilePic");
            companyNameHintValue = getIntent().getStringExtra("companyName");
            typeHereValue = getIntent().getStringExtra("bio");
            headquarterValue = getIntent().getStringExtra("headquarter");
            websiteValue = getIntent().getStringExtra("website");
            latitude = getIntent().getDoubleExtra("latitude", latitude);
            longitude = getIntent().getDoubleExtra("latitude", longitude);
            selectedCompanyIndustries = getIntent().getParcelableArrayListExtra("selectedCompanyIndustries");
            selectedCompanyInterestInStageList = getIntent().getParcelableArrayListExtra("selectedCompanyInterestInStageList");
            selectedCompanyInSizeList = getIntent().getParcelableArrayListExtra("selectedCompanyInSizeList");
        }
        loadProfile(CompanyEmailAddressActivity.this, encodedImageData, redNoah2);

        backIcon.setOnClickListener(v -> onBackPressed());

        next.setOnClickListener(v -> {
            emailValue = emailEditText.getText().toString().trim();
            if (emailValue.isEmpty()) {
                CustomCookieToast.showRequiredToast(CompanyEmailAddressActivity.this, "Please enter email address");
            } else if (!Patterns.EMAIL_ADDRESS.matcher(emailValue).matches()){
                CustomCookieToast.showRequiredToast(CompanyEmailAddressActivity.this, "Please enter valid email address");
            }else {
                Utilities.hideKeyboard(emailEditText, CompanyEmailAddressActivity.this);
                sendVerificationCode();
            }
        });
    }


    private void sendVerificationCode() {
        HashMap<String, String> params = new HashMap<>();
        params.put("email", emailValue);
        sendServerRequestPOST(AppConstants.SEND_CODE, params);
    }

    @Override
    public void requestStarted() {
        super.requestStarted();
        progressDialog.setMessage("sending code...");
        progressDialog.show();
    }

    @Override
    public void onResponseReceived(String response, String urlCalled) {
        super.onResponseReceived(response, urlCalled);
        progressDialog.dismiss();
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(response);
            boolean status = jsonObject.getBoolean("status");
            String message = jsonObject.getString("message");
            if (status) {
                Toast.makeText(this, "Code Sent", Toast.LENGTH_SHORT).show();
                openNextScreen();
            } else {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void requestEndedWithError(VolleyError error) {
        super.requestEndedWithError(error);
        progressDialog.dismiss();
        Toast.makeText(this, "error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
    }


    private void openNextScreen() {
        Intent intent = new Intent(CompanyEmailAddressActivity.this, CompanyVerificationCodeActivity.class);
        intent.putExtra("profilePic", encodedImageData);
        intent.putExtra("companyName", companyNameHintValue);
        intent.putExtra("bio", typeHereValue);
        intent.putExtra("headquarter", headquarterValue);
        intent.putExtra("website", websiteValue);
        intent.putExtra("email", emailValue);
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