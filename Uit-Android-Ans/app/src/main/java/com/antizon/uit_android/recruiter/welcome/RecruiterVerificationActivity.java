package com.antizon.uit_android.recruiter.welcome;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import com.antizon.uit_android.models.applicant.jobs.ApplicantJobDataModel;
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


public class RecruiterVerificationActivity extends BaseActivity {
    private static final String TAG = RecruiterVerificationActivity.class.getSimpleName();
    Context context;
    GetDataService service;
    SessionManagement sessionManagement;
    ProgressDialog progressDialog;
    ImageView backIcon;
    RoundedImageView logoImage;
    TextView next, resendCode,verificationEmail;
    EditText firstDigit, secondDigit, thirdDigit, fourthDigit;

    String profilePic = "", fullName = "", recruiterEmail = "", companyId = "", codeValue = "", message = "", from;
    ApplicantJobDataModel recruiterJobDataModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recruiter_verification);
        Utilities.setWhiteBars(RecruiterVerificationActivity.this);
        context = RecruiterVerificationActivity.this;
        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        progressDialog = new ProgressDialog(context);
        sessionManagement = new SessionManagement(context);
        initViews();
        setListener();
    }

    private void initViews() {
        from = getIntent().getStringExtra("from");
        recruiterEmail=getIntent().getStringExtra("recruiterEmail");
        if (from.equals("add")){
            companyId = getIntent().getStringExtra("companyId");
            profilePic = getIntent().getStringExtra("profilePic");
            fullName = getIntent().getStringExtra("fullName");
            recruiterJobDataModel= getIntent().getParcelableExtra("recruiterJobDataModel");
        }


        logoImage = findViewById(R.id.logoImage);
        backIcon = findViewById(R.id.backIcon);
        next = findViewById(R.id.next);
        verificationEmail = findViewById(R.id.verificationEmail);
        resendCode = findViewById(R.id.resend);
        firstDigit = findViewById(R.id.boxesText1);
        secondDigit = findViewById(R.id.boxesText2);
        thirdDigit = findViewById(R.id.boxesText3);
        fourthDigit = findViewById(R.id.boxesText4);

        verificationEmail.setText(recruiterEmail);


        if (from.equals("edit")){
            Utilities.loadImage(context, sessionManagement.getProfileImage(), logoImage);
        }else {
            loadProfile(RecruiterVerificationActivity.this, profilePic, logoImage);
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
                CustomCookieToast.showRequiredToast(RecruiterVerificationActivity.this, "Please enter verification code");
            }
            else if (pinCodeIs.length() < 4) {
                CustomCookieToast.showRequiredToast(RecruiterVerificationActivity.this, "Please enter four digit verification code");
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
            reportPostPopup.dismiss();
            Utilities.hideKeyboard(v, context);
            resendVerification();
        });
    }

    private void resendVerification() {
        message = "sending code...";
        HashMap<String, String> params = new HashMap<>();
        params.put("email", recruiterEmail);
        sendServerRequestPOST(AppConstants.SEND_CODE, params);
    }

    private void sendAPIToVerifyCode(String code) {
        message = "Verifying...";
        HashMap<String, String> params = new HashMap<>();
        params.put("email", recruiterEmail);
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
                CustomCookieToast.showSuccessToast(RecruiterVerificationActivity.this, "New Verification code has been sent to your mail");
            } else {
                CustomCookieToast.showFailureToast(RecruiterVerificationActivity.this, message);
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
                if (from.equals("edit")){
                    progressDialog.show();
                    requestForUpdateCompanyTeamMemberEmail("Bearer " + sessionManagement.getToken(), recruiterEmail);
                }else {
                    openNextScreen();
                }

            } else {
                CustomCookieToast.showFailureToast(RecruiterVerificationActivity.this, "Your verification code is inCorrect.");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void openNextScreen() {
        Intent intent = new Intent(RecruiterVerificationActivity.this, RecruiterPhoneNumberActivity.class);
        intent.putExtra("companyId", companyId);
        intent.putExtra("profilePic", profilePic);
        intent.putExtra("fullName", fullName);
        intent.putExtra("recruiterEmail", recruiterEmail);
        intent.putExtra("verification", codeValue);
        intent.putExtra("recruiterJobDataModel", recruiterJobDataModel);
        intent.putExtra("from", from);
        startActivity(intent);
        overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }

    private void requestForUpdateCompanyTeamMemberEmail(String authToken,String recruiterEmail) {
        Call<MainResponseModel> call = service.updateApplicantProfileEmail(authToken, recruiterEmail);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NotNull Call<MainResponseModel> call, @NotNull Response<MainResponseModel> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().isStatus()) {
                        Intent intent = new Intent();
                        intent.putExtra("companyTeamMemberEmail", recruiterEmail);
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