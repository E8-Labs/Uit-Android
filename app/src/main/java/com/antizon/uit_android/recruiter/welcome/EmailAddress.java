package com.antizon.uit_android.recruiter.welcome;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.antizon.uit_android.R;
import com.antizon.uit_android.company.utility.BaseActivity;
import com.antizon.uit_android.generic.model.ModelApplicantJobs;
import com.antizon.uit_android.generic_utils.AppConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class EmailAddress extends BaseActivity {

    private static final String TAG = EmailAddress.class.getSimpleName();
    ProgressDialog progressDialog;
    ImageView backIcon,menYellow;
    TextView next;
    EditText email;
    private List<ModelApplicantJobs> list;
    String encodedImageData = "", fullNameEditTextValue = "", jobTitleEditTextValue = "", emailValue = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_address4);

        setIds();
        getIntentData();
        initialize();

        setListener();
    }


    void setIds() {
        Log.d(TAG, "setIds: ");

        backIcon = findViewById(R.id.backIcon);
        menYellow = findViewById(R.id.menYellow);
        next = findViewById(R.id.next);
        email = findViewById(R.id.email);

    }
    void getIntentData() {

        if (getIntent() != null) {

            encodedImageData = getIntent().getStringExtra("profilePic");
            fullNameEditTextValue = getIntent().getStringExtra("fullName");


            Bundle b = getIntent().getExtras();
//            ArrayList<ModelApplicantJobs> list = (ArrayList<ModelApplicantJobs>) b.getSerializable("jobs");
//            Log.d(TAG, "getIntentData: list: " + list.size());
        }
    }

    void initialize() {
        Log.d(TAG, "initialize: ");
        progressDialog = new ProgressDialog(this);
        loadProfile(EmailAddress.this, encodedImageData, menYellow);
        list = new ArrayList<>();
    }





    void setListener() {
        Log.d(TAG, "setListener: ");

        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
                overridePendingTransition(R.anim.right_in, R.anim.right_out);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard(EmailAddress.this, email);
                if (!validate()) {

                } else {
                    sendVerificationCode();
                }
            }
        });
    }

    public boolean validate() {
        Log.d(TAG, "validate: ");
        emailValue = email.getText().toString().trim();

        boolean valid = true;
        if (emailValue.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(emailValue).matches()) {

            email.setError("Please enter valid email");
            valid = false;
        }
        return valid;
    }

    void sendVerificationCode() {

        HashMap<String, String> params = new HashMap<String, String>();
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

        Bundle bundle = new Bundle();
        bundle.putSerializable("Job", (Serializable) list);

        Intent intent = new Intent(EmailAddress.this, VerificationMember.class);
        intent.putExtra("profilePic", encodedImageData);
        intent.putExtra("fullName", fullNameEditTextValue);
        intent.putExtra("email", emailValue);
        startActivity(intent);
    }


}