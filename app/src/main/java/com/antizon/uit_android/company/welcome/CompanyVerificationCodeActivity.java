package com.antizon.uit_android.company.welcome;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.VolleyError;
import com.antizon.uit_android.R;
import com.antizon.uit_android.company.utility.BaseActivity;
import com.antizon.uit_android.generic.model.ModelCompanySize;
import com.antizon.uit_android.generic_utils.AppConstants;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.models.applicant.degree.ApplicantDegreeDataModel;
import com.antizon.uit_android.utilities.Utilities;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;


public class CompanyVerificationCodeActivity extends BaseActivity {

    private static final String TAG = CompanyVerificationCodeActivity.class.getSimpleName();
    SessionManagement sessionManagement;
    ProgressDialog progressDialog;
    ImageView backIcon,logoImage;
    TextView next, resendCode,verificationEmail;
    EditText firstDigit, secondDigit, thirdDigit, fourthDigit;
    String message="";
    String typeHereValue = "", companyNameHintValue = "", encodedImageData = "",headquarterValue="",websiteValue="",emailValue="",verificationValue="";
    double latitude, longitude;

    ArrayList<ModelCompanySize> selectedCompanyInterestInStageList, selectedCompanyInSizeList;

    ArrayList<ApplicantDegreeDataModel> selectedCompanyIndustries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_verification_code);
        Utilities.setWhiteBars(CompanyVerificationCodeActivity.this);
        hideWindowsKeyboard();
        initViews();
        setListener();

    }

    private void initViews() {

        verificationEmail = findViewById(R.id.verificationEmail);
        backIcon = findViewById(R.id.backIcon);
        next = findViewById(R.id.next);
        resendCode = findViewById(R.id.resend);
        logoImage = findViewById(R.id.logoImage);
        firstDigit = findViewById(R.id.boxesText1);
        secondDigit = findViewById(R.id.boxesText2);
        thirdDigit = findViewById(R.id.boxesText3);
        fourthDigit = findViewById(R.id.boxesText4);

        if (getIntent() != null) {
            encodedImageData = getIntent().getStringExtra("profilePic");
            companyNameHintValue = getIntent().getStringExtra("companyName");
            typeHereValue = getIntent().getStringExtra("bio");
            headquarterValue = getIntent().getStringExtra("headquarter");
            websiteValue = getIntent().getStringExtra("website");
            emailValue = getIntent().getStringExtra("email");
            latitude = getIntent().getDoubleExtra("latitude", latitude);
            longitude = getIntent().getDoubleExtra("latitude", longitude);
            selectedCompanyIndustries = getIntent().getParcelableArrayListExtra("selectedCompanyIndustries");
            selectedCompanyInterestInStageList = getIntent().getParcelableArrayListExtra("selectedCompanyInterestInStageList");
            selectedCompanyInSizeList = getIntent().getParcelableArrayListExtra("selectedCompanyInSizeList");

            verificationEmail.setText(emailValue);
        }

        progressDialog = new ProgressDialog(this);
        sessionManagement = new SessionManagement(CompanyVerificationCodeActivity.this);
        loadProfile(CompanyVerificationCodeActivity.this, encodedImageData, logoImage);
    }

    private void setListener() {

        backIcon.setOnClickListener(view -> onBackPressed());

        resendCode.setOnClickListener(view -> resendVerification());

        next.setOnClickListener(v -> {

            String digit1 = firstDigit.getText().toString();
            String digit2 = secondDigit.getText().toString();
            String digit3 = thirdDigit.getText().toString();
            String digit4 = fourthDigit.getText().toString();

            String pinCodeIs = digit1 + digit2 + digit3 + digit4;

            if (pinCodeIs.length() < 4) {
                Toast.makeText(CompanyVerificationCodeActivity.this, "Please write 4 digits code", Toast.LENGTH_SHORT).show();
            } else {
                sendAPIToVerifyCode(pinCodeIs);
            }
        });

        firstDigit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (!firstDigit.getText().toString().isEmpty()) {
                    secondDigit.requestFocus();
                }
            }
        });

        secondDigit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (secondDigit.getText().toString().isEmpty()) {
                    firstDigit.requestFocus();
                } else {
                    thirdDigit.requestFocus();
                }


            }
        });

        thirdDigit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (thirdDigit.getText().toString().isEmpty()) {
                    secondDigit.requestFocus();
                } else {
                    fourthDigit.requestFocus();
                }
            }
        });

        fourthDigit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (fourthDigit.getText().toString().isEmpty()) {

                    thirdDigit.requestFocus();
                }
            }
        });
    }

    void resendVerification() {
        message = "sending code...";
        HashMap<String, String> params = new HashMap<>();
        params.put("email", emailValue);
        sendServerRequestPOST(AppConstants.SEND_CODE, params);
    }

    void sendAPIToVerifyCode(String code) {
        message = "Verifying...";
        HashMap<String, String> params = new HashMap<>();
        params.put("email", emailValue);
        params.put("code", code);
        sendServerRequestPOST(AppConstants.VERIFY_EMAIL, params);
    }

    @Override
    public void requestStarted() {
        super.requestStarted();
        progressDialog.setMessage(message);
        progressDialog.show();
    }

    @Override
    public void onResponseReceived(String response, String urlCalled) {
        super.onResponseReceived(response, urlCalled);
        progressDialog.dismiss();

        if (urlCalled.contains(AppConstants.SEND_CODE)) {
            checkResponseForResendCode(response);
        }

        if (urlCalled.contains(AppConstants.VERIFY_EMAIL)) {
            checkResponseForCodeVerification(response);
        }
    }

    @Override
    public void requestEndedWithError(VolleyError error) {
        super.requestEndedWithError(error);
        progressDialog.dismiss();
        Toast.makeText(this, "error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
    }

    private void checkResponseForResendCode(String response) {

        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(response);
            boolean status = jsonObject.getBoolean("status");
            String message = jsonObject.getString("message");
            if (status) {

                Toast.makeText(this, "Code resent.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            Log.d(TAG, "onResponseReceived: JSONException: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private void checkResponseForCodeVerification(String response) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(response);
            boolean status = jsonObject.getBoolean("status");
            if (status) {
                Toast.makeText(this, "Code Verified.", Toast.LENGTH_SHORT).show();
                openNextScreen();
            } else {
                Toast.makeText(this, "Wrong Code.", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            Log.d(TAG, "onResponseReceived: JSONException: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void openNextScreen() {
        Intent intent = new Intent(CompanyVerificationCodeActivity.this, CompanyPasswordActivity.class);
        intent.putExtra("profilePic", encodedImageData);
        intent.putExtra("companyName", companyNameHintValue);
        intent.putExtra("bio", typeHereValue);
        intent.putExtra("headquarter", headquarterValue);
        intent.putExtra("website", websiteValue);
        intent.putExtra("email", emailValue);
        intent.putExtra("verification", verificationValue);
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
