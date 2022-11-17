package com.antizon.uit_android.uit_members.welcome;

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
import com.antizon.uit_android.utilities.CustomCookieToast;
import com.antizon.uit_android.utilities.Utilities;
import com.makeramen.roundedimageview.RoundedImageView;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;

public class UitMemberVerificationActivity extends BaseActivity {

    Context context;

    private static final String TAG = UitMemberVerificationActivity.class.getSimpleName();
    SessionManagement sessionManagement;
    ProgressDialog progressDialog;
    ImageView backIcon;
    RoundedImageView menYellow;
    TextView next, resendCode,verificationEmail;
    EditText firstDigit, secondDigit, thirdDigit, fourthDigit;
    String emailAddressEditTextValue = "", fullNameEditTextValue = "", encodedImageData = "", message = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uit_member_verification);
        Utilities.setWhiteBars(UitMemberVerificationActivity.this);
        context = UitMemberVerificationActivity.this;

        hideWindowsKeyboard();
        setIds();
        getIntentData();
        initialize();
        setListener();
    }

    void setIds() {

        backIcon = findViewById(R.id.backIcon);
        next = findViewById(R.id.next);
        menYellow = findViewById(R.id.logoImage);
        resendCode = findViewById(R.id.resend);
        verificationEmail = findViewById(R.id.verificationEmail);
        firstDigit = findViewById(R.id.boxesText1);
        secondDigit = findViewById(R.id.boxesText2);
        thirdDigit = findViewById(R.id.boxesText3);
        fourthDigit = findViewById(R.id.boxesText4);
    }

    void initialize() {

        progressDialog = new ProgressDialog(this);
        sessionManagement = new SessionManagement(UitMemberVerificationActivity.this);
        loadProfile(UitMemberVerificationActivity.this, encodedImageData, menYellow);
    }

    void getIntentData() {

        if (getIntent() != null) {
            encodedImageData = getIntent().getStringExtra("profilePic");
            fullNameEditTextValue = getIntent().getStringExtra("fullName");
            emailAddressEditTextValue = getIntent().getStringExtra("email");
            verificationEmail.setText(emailAddressEditTextValue);
            Log.d(TAG, "getIntentData: image: "+encodedImageData);
            Log.d(TAG, "getIntentData: fullName: "+fullNameEditTextValue);
            Log.d(TAG, "getIntentData: email: "+emailAddressEditTextValue);
        }
    }

    void setListener() {

        backIcon.setOnClickListener(view -> {
            onBackPressed();
            overridePendingTransition(R.anim.right_in, R.anim.right_out);
        });

        resendCode.setOnClickListener(v -> doYouWantToResendCode());

        next.setOnClickListener(v -> {

            String digit1 = firstDigit.getText().toString();
            String digit2 = secondDigit.getText().toString();
            String digit3 = thirdDigit.getText().toString();
            String digit4 = fourthDigit.getText().toString();

            String pinCodeIs = digit1 + digit2 + digit3 + digit4+"";
            if (pinCodeIs.isEmpty()){
                CustomCookieToast.showRequiredToast(UitMemberVerificationActivity.this, "Please enter verification code");
            }
            else if (pinCodeIs.length() < 4) {
                CustomCookieToast.showRequiredToast(UitMemberVerificationActivity.this, "Please enter four digit verification code");
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
            resendVerification();
        });
    }

    void resendVerification() {
        message = "sending code...";
        HashMap<String, String> params = new HashMap<>();
        params.put("email", emailAddressEditTextValue);
        sendServerRequestPOST(AppConstants.SEND_CODE, params);
    }

    void sendAPIToVerifyCode(String code) {
        message = "Verifying...";
        HashMap<String, String> params = new HashMap<>();
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
        Log.d(TAG, "requestEndedWithError: error: " + error);
        Toast.makeText(this, "error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
    }

    void checkResponseForResendCode(String response) {

        JSONObject jsonObject;
        try {

            jsonObject = new JSONObject(response);
            boolean status = jsonObject.getBoolean("status");
            String message = jsonObject.getString("message");
            if (status) {
                CustomCookieToast.showSuccessToast(UitMemberVerificationActivity.this, "New Verification code has been sent to your mail");
            } else {
                CustomCookieToast.showFailureToast(UitMemberVerificationActivity.this, message);
            }
        } catch (JSONException e) {
            Log.d(TAG, "onResponseReceived: JSONException: " + e.getMessage());
            e.printStackTrace();
        }
    }


    void checkResponseForCodeVerification(String response) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(response);
            boolean status = jsonObject.getBoolean("status");
            if (status) {
                Toast.makeText(this, "Code Verified.", Toast.LENGTH_SHORT).show();
                openNextScreen();
            } else {
                CustomCookieToast.showFailureToast(UitMemberVerificationActivity.this, "Your verification code is inCorrect.");
            }
        } catch (JSONException e) {
            Log.d(TAG, "onResponseReceived: JSONException: " + e.getMessage());
            e.printStackTrace();
        }
    }

    void openNextScreen() {
        Intent intent = new Intent(UitMemberVerificationActivity.this, UitMemberPhoneNumberActivity.class);
        intent.putExtra("profilePic", encodedImageData);
        intent.putExtra("fullName", fullNameEditTextValue);
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