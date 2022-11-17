package com.antizon.uit_android.company.welcome;

import android.app.ProgressDialog;
import android.content.Context;
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
import com.antizon.uit_android.models.community.MainResponseModel;
import com.antizon.uit_android.network.GetDataService;
import com.antizon.uit_android.network.RetrofitClientInstance;
import com.antizon.uit_android.utilities.Utilities;
import com.makeramen.roundedimageview.RoundedImageView;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CompanyVerificationCodeActivity extends BaseActivity {
    Context context;
    GetDataService service;
    private static final String TAG = CompanyVerificationCodeActivity.class.getSimpleName();
    SessionManagement sessionManagement;
    ProgressDialog progressDialog;
    ImageView backIcon;
    RoundedImageView logoImage;
    TextView next, resendCode,verificationEmail;
    EditText firstDigit, secondDigit, thirdDigit, fourthDigit;
    String message="";
    String typeHereValue = "", companyNameHintValue = "", encodedImageData = "", city="", state = "", websiteValue="",emailValue="",verificationValue="", from;
    double latitude, longitude;

    ArrayList<ModelCompanySize> selectedCompanyInterestInStageList, selectedCompanyInSizeList;

    ArrayList<ApplicantDegreeDataModel> selectedCompanyIndustries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_verification_code);
        Utilities.setWhiteBars(CompanyVerificationCodeActivity.this);
        context = CompanyVerificationCodeActivity.this;
        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        progressDialog = new ProgressDialog(context);

        hideWindowsKeyboard();
        initViews();
        setListener();

    }

    private void initViews() {
        if (getIntent() != null) {
            from = getIntent().getStringExtra("from");
            emailValue = getIntent().getStringExtra("email");
            if (!from.equals("edit")){
                encodedImageData = getIntent().getStringExtra("profilePic");
                companyNameHintValue = getIntent().getStringExtra("companyName");
                typeHereValue = getIntent().getStringExtra("bio");
                city = getIntent().getStringExtra("city");
                state = getIntent().getStringExtra("state");
                websiteValue = getIntent().getStringExtra("website");
                latitude = getIntent().getDoubleExtra("latitude", latitude);
                longitude = getIntent().getDoubleExtra("latitude", longitude);
                selectedCompanyIndustries = getIntent().getParcelableArrayListExtra("selectedCompanyIndustries");
                selectedCompanyInterestInStageList = getIntent().getParcelableArrayListExtra("selectedCompanyInterestInStageList");
                selectedCompanyInSizeList = getIntent().getParcelableArrayListExtra("selectedCompanyInSizeList");
            }

        }



        verificationEmail = findViewById(R.id.verificationEmail);
        backIcon = findViewById(R.id.backIcon);
        next = findViewById(R.id.next);
        resendCode = findViewById(R.id.resend);
        logoImage = findViewById(R.id.logoImage);
        firstDigit = findViewById(R.id.boxesText1);
        secondDigit = findViewById(R.id.boxesText2);
        thirdDigit = findViewById(R.id.boxesText3);
        fourthDigit = findViewById(R.id.boxesText4);



        verificationEmail.setText(emailValue);
        if (from.equals("edit")){
            sessionManagement = new SessionManagement(context);
            Utilities.loadImage(CompanyVerificationCodeActivity.this, sessionManagement.getProfileImage(), logoImage);
        }else {
            loadProfile(CompanyVerificationCodeActivity.this, encodedImageData, logoImage);
        }
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

    private void resendVerification() {
        message = "sending code...";
        HashMap<String, String> params = new HashMap<>();
        params.put("email", emailValue);
        sendServerRequestPOST(AppConstants.SEND_CODE, params);
    }

    private void sendAPIToVerifyCode(String code) {
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
                if (from.equals("edit")){
                    progressDialog.show();
                    requestForUpdateCompanyEmail("Bearer " + sessionManagement.getToken(), emailValue);
                }else {
                    Toast.makeText(this, "Code Verified.", Toast.LENGTH_SHORT).show();
                    openNextScreen();
                }
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
        intent.putExtra("website", websiteValue);
        intent.putExtra("email", emailValue);
        intent.putExtra("verification", verificationValue);
        intent.putExtra("city", city);
        intent.putExtra("state", state);
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

    private void requestForUpdateCompanyEmail(String authToken,String companyEmail) {
        Call<MainResponseModel> call = service.updateApplicantProfileEmail(authToken, companyEmail);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NotNull Call<MainResponseModel> call, @NotNull Response<MainResponseModel> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().isStatus()) {
                        Intent intent = new Intent();
                        intent.putExtra("companyEmail", companyEmail);
                        setResult(RESULT_OK, intent);
                        finish();
                        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                    }else {
                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(context, "un successful : " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<MainResponseModel> call, @NotNull Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(context, "failure : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
