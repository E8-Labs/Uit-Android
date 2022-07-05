package com.antizon.uit_android.applicant.welcome;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.VolleyError;
import com.antizon.uit_android.R;
import com.antizon.uit_android.company.utility.BaseActivity;
import com.antizon.uit_android.generic_utils.AppConstants;
import com.antizon.uit_android.utilities.CustomCookieToast;
import com.antizon.uit_android.utilities.Utilities;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;

public class ApplicantEmailAddressActivity extends BaseActivity {
    private static final String TAG = ApplicantEmailAddressActivity.class.getSimpleName();
    ProgressDialog progressDialog;
    ImageView backIcon, menYellow;
    TextView next;
    EditText emailAddressEditText;
    String emailAddressEditTextValue = "", applicantNameValue = "", encodedImageData = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applicant_email_address);
        Utilities.setWhiteBars(ApplicantEmailAddressActivity.this);
        setIds();
        getIntentData();
        initialize();
        setListener();

    }


    void setIds() {
        Log.d(TAG, "setIds: ");
        menYellow = findViewById(R.id.menYellow);
        backIcon = findViewById(R.id.backIcon);
        next = findViewById(R.id.next);
        emailAddressEditText = findViewById(R.id.emailAddressEditText);

    }

    void getIntentData() {

        if (getIntent() != null) {
            encodedImageData = getIntent().getStringExtra("profilePic");
            applicantNameValue = getIntent().getStringExtra("applicantName");
            Log.d(TAG, "getIntentData: image: " + encodedImageData);
            Log.d(TAG, "getIntentData: fullName: " + applicantNameValue);
        }
    }

    void initialize() {
        Log.d(TAG, "initialize: ");
        progressDialog = new ProgressDialog(this);
        loadProfile(ApplicantEmailAddressActivity.this, encodedImageData, menYellow);
    }


    void setListener() {
        Log.d(TAG, "setListener: ");

        backIcon.setOnClickListener(v -> onBackPressed());

        next.setOnClickListener(v -> {
            hideSoftKeyboard(ApplicantEmailAddressActivity.this, emailAddressEditText);
            emailAddressEditTextValue = emailAddressEditText.getText().toString().trim();

            if (emailAddressEditTextValue.isEmpty()) {
                CustomCookieToast.showRequiredToast(ApplicantEmailAddressActivity.this, "Please enter your email");
            }else if (!Patterns.EMAIL_ADDRESS.matcher(emailAddressEditTextValue).matches()) {
                CustomCookieToast.showRequiredToast(ApplicantEmailAddressActivity.this, "Please enter valid email");
            } else {
                sendVerificationCode();
            }
        });

    }

    public boolean validate() {
        Log.d(TAG, "validate: ");


        boolean valid = true;
        if (emailAddressEditTextValue.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(emailAddressEditTextValue).matches()) {

            emailAddressEditText.setError("Please enter valid email");
            valid = false;
        }
        return valid;
    }

    void sendVerificationCode() {


        HashMap<String, String> params = new HashMap<String, String>();
        params.put("email", emailAddressEditTextValue);

        Log.d(TAG, "sendVerificationCode: params: " + params);
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
        JSONObject jsonObject = null;
        try {
            Log.d(TAG, "onResponseReceived: response: " + response);
            Log.d(TAG, "onResponseReceived: urlCalled: " + urlCalled);

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
            Log.d(TAG, "onResponseReceived: JSONException: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void requestEndedWithError(VolleyError error) {
        super.requestEndedWithError(error);
        progressDialog.dismiss();
        Log.d(TAG, "requestEndedWithError: error: " + error);
        Toast.makeText(this, "error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
    }


    void openNextScreen() {
        Intent intent = new Intent(ApplicantEmailAddressActivity.this, ApplicantVerificationCodeActivity.class);
        intent.putExtra("profilePic", encodedImageData);
        intent.putExtra("applicantName", applicantNameValue);
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