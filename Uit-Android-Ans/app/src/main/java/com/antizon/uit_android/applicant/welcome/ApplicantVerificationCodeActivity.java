package com.antizon.uit_android.applicant.welcome;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import com.android.volley.VolleyError;
import com.antizon.uit_android.R;
import com.antizon.uit_android.company.utility.BaseActivity;
import com.antizon.uit_android.generic_utils.AppConstants;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.models.community.MainResponseModel;
import com.antizon.uit_android.network.GetDataService;
import com.antizon.uit_android.network.RetrofitClientInstance;
import com.antizon.uit_android.utilities.CustomCookieToast;
import com.antizon.uit_android.utilities.Utilities;
import com.makeramen.roundedimageview.RoundedImageView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ApplicantVerificationCodeActivity extends BaseActivity {
    Context context;
    GetDataService service;

    SessionManagement sessionManagement;
    ProgressDialog progressDialog;
    ImageView backIcon;
    RoundedImageView logoImage;
    TextView next, resendCode,verificationEmail;
    EditText firstDigit, secondDigit, thirdDigit, fourthDigit;

    String emailAddressEditTextValue = "", applicantNameValue="", encodedImageData = "", message="", from;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application_verification_code);
        Utilities.setWhiteBars(ApplicantVerificationCodeActivity.this);
        context = ApplicantVerificationCodeActivity.this;
        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        progressDialog = new ProgressDialog(context);
        sessionManagement = new SessionManagement(context);

        hideWindowsKeyboard();
        initViews();
        setListener();
    }

    private void initViews() {

        from = getIntent().getStringExtra("from");
        emailAddressEditTextValue = getIntent().getStringExtra("email");
        if (from.equals("add")){
            encodedImageData = getIntent().getStringExtra("profilePic");
            applicantNameValue = getIntent().getStringExtra("applicantName");
        }


        backIcon = findViewById(R.id.backIcon);
        next = findViewById(R.id.next);
        resendCode = findViewById(R.id.resend);
        logoImage = findViewById(R.id.logoImage);
        firstDigit = findViewById(R.id.boxesText1);
        secondDigit = findViewById(R.id.boxesText2);
        thirdDigit = findViewById(R.id.boxesText3);
        fourthDigit = findViewById(R.id.boxesText4);
        verificationEmail = findViewById(R.id.verificationEmail);

        verificationEmail.setText(emailAddressEditTextValue);

        if (from.equals("edit")){
            Utilities.loadImage(context, sessionManagement.getProfileImage(), logoImage);
        }else {
            loadProfile(ApplicantVerificationCodeActivity.this, encodedImageData, logoImage);
        }


    }

    private void setListener() {
        backIcon.setOnClickListener(view -> onBackPressed());

        resendCode.setOnClickListener(view -> doYouWantToResendCode());

        next.setOnClickListener(v -> {

            String digit1 = firstDigit.getText().toString();
            String digit2 = secondDigit.getText().toString();
            String digit3 = thirdDigit.getText().toString();
            String digit4 = fourthDigit.getText().toString();

            String pinCodeIs = digit1 + digit2 + digit3 + digit4;

            if (pinCodeIs.isEmpty()){
                CustomCookieToast.showRequiredToast(ApplicantVerificationCodeActivity.this, "Please enter verification code");
            }
            else if (pinCodeIs.length() < 4) {
                CustomCookieToast.showRequiredToast(ApplicantVerificationCodeActivity.this, "Please enter four digit verification code");
            } else {
                Utilities.hideKeyboard(fourthDigit, context);
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

    private void doYouWantToResendCode(){

        AlertDialog reportPostPopup;
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.CustomDialog);
        final View customLayout =  LayoutInflater.from(context).inflate(R.layout.popup_yes_no, null);
        builder.setView(customLayout);
        String sure = "Are you sure you want to resend code ?";

        TextView btn_yes = customLayout.findViewById(R.id.text_Yes);
        TextView btn_cancel = customLayout.findViewById(R.id.text_No);
        TextView text_sure = customLayout.findViewById(R.id.text_sure);

        text_sure.setText(sure);

        reportPostPopup = builder.create();
        reportPostPopup.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        reportPostPopup.show();
        reportPostPopup.setCancelable(false);
        btn_cancel.setOnClickListener(v -> reportPostPopup.dismiss());

        btn_yes.setOnClickListener(v -> {
            // Clear SharedPref and send to login
            reportPostPopup.dismiss();
            Utilities.hideKeyboard(v, context);
            resendVerification();
        });
    }

    void resendVerification() {
        message = "sending code...";
        HashMap<String, String> params = new HashMap<>();
        params.put("email", emailAddressEditTextValue);
        sendServerRequestPOST(AppConstants.SEND_CODE, params);
    }

    private void sendAPIToVerifyCode(String code) {
        message = "Verifying...";
        HashMap<String, String> params;
        params = new HashMap<>();
        params.put("email", emailAddressEditTextValue);
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
                progressDialog.setMessage("Updating...");
                progressDialog.show();
                CustomCookieToast.showSuccessToast(ApplicantVerificationCodeActivity.this, "New Verification code has been sent to your mail");
            } else {
                CustomCookieToast.showFailureToast(ApplicantVerificationCodeActivity.this, message);
            }
        } catch (JSONException e) {

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
                    requestForApplicantUpdateApplicantEmail("Bearer " + sessionManagement.getToken(), emailAddressEditTextValue);
                }else {
                    Toast.makeText(this, "Code Verified.", Toast.LENGTH_SHORT).show();
                    openNextScreen();
                }

            } else {
                CustomCookieToast.showFailureToast(ApplicantVerificationCodeActivity.this, "Your verification code is inCorrect.");
            }
        } catch (JSONException e) {

            e.printStackTrace();
        }
    }


    private void requestForApplicantUpdateApplicantEmail(String authToken,String applicantEmail) {
        Call<MainResponseModel> call = service.updateApplicantProfileEmail(authToken, applicantEmail);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NotNull Call<MainResponseModel> call, @NotNull Response<MainResponseModel> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().isStatus()) {
                        Intent intent = new Intent();
                        intent.putExtra("applicantEmail", applicantEmail);
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

    private void openNextScreen() {
        Intent intent = new Intent(ApplicantVerificationCodeActivity.this, ApplicantPhoneNumberActivity.class);
        intent.putExtra("profilePic", encodedImageData);
        intent.putExtra("applicantName", applicantNameValue);
        intent.putExtra("from", from);
        intent.putExtra("email", emailAddressEditTextValue);
        startActivity(intent);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }
}


