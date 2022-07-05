package com.antizon.uit_android.company.welcome;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class VerificationCode extends BaseActivity {

    private static final String TAG = VerificationCode.class.getSimpleName();
    SessionManagement sessionManagement;
    ProgressDialog progressDialog;
    ImageView backIcon,logoImage;
    TextView next, resendCode,verificationEmail;
    EditText firstDigit, secondDigit, thirdDigit, fourthDigit;
    String message="";
    ArrayList<ModelCompanySize> industriesList;


    String selectedStageId = "", selectedStageName = "", typeHereValue = "", companyNameHintValue = "", encodedImageData = "", sizeValue = ""
            ,headquarterValue="",websiteValue="",emailValue="",verificationValue="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_code2);
        hideWindowsKeyboard();

        setIds();
        getIntentData();
        initialize();
        setListener();

    }

    void setIds() {
        verificationEmail = findViewById(R.id.verificationEmail);
        backIcon = findViewById(R.id.backIcon);
        next = findViewById(R.id.next);
        resendCode = findViewById(R.id.resend);
        logoImage = findViewById(R.id.logoImage);
        firstDigit = findViewById(R.id.boxesText1);
        secondDigit = findViewById(R.id.boxesText2);
        thirdDigit = findViewById(R.id.boxesText3);
        fourthDigit = findViewById(R.id.boxesText4);
    }
    void getIntentData() {

        if (getIntent() != null) {

            encodedImageData = getIntent().getStringExtra("profilePic");
            companyNameHintValue = getIntent().getStringExtra("companyName");
            typeHereValue = getIntent().getStringExtra("bio");
            selectedStageId = getIntent().getStringExtra("stageId");
            selectedStageName = getIntent().getStringExtra("stageName");
            sizeValue = getIntent().getStringExtra("size");
            headquarterValue = getIntent().getStringExtra("headquarter");
            websiteValue = getIntent().getStringExtra("website");
            emailValue = getIntent().getStringExtra("email");
            verificationEmail.setText(emailValue);


            Log.d(TAG, "getIntentData: image: "+encodedImageData);
            Log.d(TAG, "getIntentData: companyName: "+companyNameHintValue);
            Log.d(TAG, "getIntentData: bio: "+typeHereValue);
            Log.d(TAG, "getIntentData: stageName: "+selectedStageName);
            Log.d(TAG, "getIntentData: stageId: "+selectedStageId);
            Log.d(TAG, "getIntentData: headquarter: "+headquarterValue);
            Log.d(TAG, "getIntentData: website: "+websiteValue);
            Log.d(TAG, "getIntentData: email: " + emailValue);

            Bundle bundle = getIntent().getExtras();

                industriesList = bundle.getParcelableArrayList("industries");
            Log.d(TAG, "getIntentData: industriesList: " + industriesList.size());

        }
    }
    void initialize() {

        progressDialog = new ProgressDialog(this);
        sessionManagement = new SessionManagement(VerificationCode.this);
        loadProfile(VerificationCode.this, encodedImageData, logoImage);
        industriesList = new ArrayList<>();
    }



    void setListener() {

        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                overridePendingTransition(R.anim.right_in, R.anim.right_out);
            }
        });

        resendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                resendVerification();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String digit1 = firstDigit.getText().toString();
                String digit2 = secondDigit.getText().toString();
                String digit3 = thirdDigit.getText().toString();
                String digit4 = fourthDigit.getText().toString();

                String pinCodeIs = digit1 + digit2 + digit3 + digit4;

                if (pinCodeIs.length() < 4) {
                    Toast.makeText(VerificationCode.this, "Please write 4 digits code", Toast.LENGTH_SHORT).show();
                } else {
                    sendAPIToVerifyCode(pinCodeIs);
                }
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

                if (firstDigit.getText().toString().isEmpty()) {

                } else {
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
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("email", emailValue);
        sendServerRequestPOST(AppConstants.SEND_CODE, params);
    }

    void sendAPIToVerifyCode(String code) {
        message = "Verifying...";
        HashMap<String, String> params = new HashMap<String, String>();
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
            checkResponseForResendCode(response, urlCalled);
        }

        if (urlCalled.contains(AppConstants.VERIFY_EMAIL)) {
            checkResponseForCodeVerification(response, urlCalled);
        }
    }

    @Override
    public void requestEndedWithError(VolleyError error) {
        super.requestEndedWithError(error);
        progressDialog.dismiss();
        Log.d(TAG, "requestEndedWithError: error: " + error);
        Toast.makeText(this, "error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
    }

    void checkResponseForResendCode(String response, String urlCalled) {

        JSONObject jsonObject = null;
        try {
            Log.d(TAG, "onResponseReceived: response: " + response);
            Log.d(TAG, "onResponseReceived: urlCalled: " + urlCalled);

//                    "status": true,
//                        "message": "Code sent",
//                        "data": null

            jsonObject = new JSONObject(response);
            Boolean status = jsonObject.getBoolean("status");
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


    void checkResponseForCodeVerification(String response, String urlCalled) {
        JSONObject jsonObject = null;
        try {
            Log.d(TAG, "onResponseReceived: response: " + response);
            Log.d(TAG, "onResponseReceived: urlCalled: " + urlCalled);

//                    "status": true,
//                        "message": "Code sent",
//                        "data": null

            jsonObject = new JSONObject(response);
            Boolean status = jsonObject.getBoolean("status");
            String message = jsonObject.getString("message");
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

    void openNextScreen() {

        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("industries", industriesList);

        Intent intent = new Intent(VerificationCode.this, Password.class);
        intent.putExtra("profilePic", encodedImageData);
        intent.putExtra("companyName", companyNameHintValue);
        intent.putExtra("bio", typeHereValue);
        intent.putExtra("stageId", "" +selectedStageId);
        intent.putExtra("stageName", selectedStageName);
        intent.putExtra("size", sizeValue);
        intent.putExtra("headquarter", headquarterValue);
        intent.putExtra("website", websiteValue);
        intent.putExtra("email", emailValue);
        intent.putExtra("verification", verificationValue);
        intent.putExtras(bundle);
        startActivity(intent);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }
}
