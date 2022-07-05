package com.antizon.uit_android.uit_members.welcome;

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

public class UitMemberEmailAddressActivity extends BaseActivity {

    private static final String TAG = UitMemberEmailAddressActivity.class.getSimpleName();
    ProgressDialog progressDialog;
    ImageView menYellow, backIcon;
    TextView next;
    EditText emailAddressEditText;
    String emailAddressEditTextValue = "", fullNameEditTextValue = "", encodedImageData = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uit_member_email_address);
        Utilities.setWhiteBars(UitMemberEmailAddressActivity.this);

        setIds();
        getIntentData();
        initialize();
        setListener();
    }

    void setIds() {
        backIcon = findViewById(R.id.backIcon);
        next = findViewById(R.id.next);
        menYellow = findViewById(R.id.menYellow);
        emailAddressEditText = findViewById(R.id.emailAddressEditText);
    }

    void initialize() {
        progressDialog = new ProgressDialog(this);
        loadProfile(UitMemberEmailAddressActivity.this, encodedImageData, menYellow);
    }

    void getIntentData() {
        if (getIntent() != null) {
            encodedImageData = getIntent().getStringExtra("profilePic");
            fullNameEditTextValue = getIntent().getStringExtra("fullName");
        }
    }

    void setListener() {

        backIcon.setOnClickListener(v -> {
            onBackPressed();
            overridePendingTransition(R.anim.right_in, R.anim.right_out);
        });

        next.setOnClickListener(v -> {
            hideSoftKeyboard(UitMemberEmailAddressActivity.this, emailAddressEditText);
            emailAddressEditTextValue = emailAddressEditText.getText().toString().trim();
            if (fullNameEditTextValue.isEmpty()) {
                CustomCookieToast.showRequiredToast(UitMemberEmailAddressActivity.this, "Please enter your email");
            }else if (!Patterns.EMAIL_ADDRESS.matcher(emailAddressEditTextValue).matches()) {
                CustomCookieToast.showRequiredToast(UitMemberEmailAddressActivity.this, "Please enter valid email");
            } else {
                sendVerificationCode();
            }
        });
    }

    public boolean validate() {
        emailAddressEditTextValue = emailAddressEditText.getText().toString().trim();
        boolean valid = true;
        if (emailAddressEditTextValue.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(emailAddressEditTextValue).matches()) {
            emailAddressEditText.setError("Please enter valid email");
            valid = false;
        }
        return valid;
    }

    void sendVerificationCode() {
        HashMap<String, String> params = new HashMap<>();
        params.put("email", emailAddressEditTextValue);
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
        Log.d(TAG, "requestEndedWithError: error: " + error);
        Toast.makeText(this, "error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
    }

    void openNextScreen() {
        Intent intent = new Intent(UitMemberEmailAddressActivity.this, UitMemberVerificationActivity.class);
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