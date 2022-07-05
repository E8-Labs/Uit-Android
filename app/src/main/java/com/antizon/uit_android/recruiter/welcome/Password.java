package com.antizon.uit_android.recruiter.welcome;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

public class Password extends BaseActivity {
    private static final String TAG = Password.class.getSimpleName();

    ProgressDialog progressDialog;
    ImageView backIcon,menYellow;
    TextView next;
    EditText password;
    String encodedImageData = "", fullNameEditTextValue = "", jobTitleValue = "", emailValue = "", codeValue="",
            phoneValue="", passwordValue = "";
    private List<ModelApplicantJobs> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password4);

        setIds();
        getIntentData();
        initialize();

        setListener();
    }


    void setIds() {
        Log.d(TAG, "setIds: ");

        backIcon = findViewById(R.id.backIcon);
        next = findViewById(R.id.next);
        password = findViewById(R.id.password);
        menYellow = findViewById(R.id.menYellow);

    }
    void getIntentData() {

        if (getIntent() != null) {

            encodedImageData = getIntent().getStringExtra("profilePic");
            fullNameEditTextValue = getIntent().getStringExtra("fullName");
            emailValue = getIntent().getStringExtra("emailAddress");
            codeValue = getIntent().getStringExtra("verification");
            phoneValue = getIntent().getStringExtra("memberPhone");

            Bundle b = getIntent().getExtras();
            list = b.getParcelableArrayList("jobs");
        }
    }
    void initialize() {
        Log.d(TAG, "initialize: ");
        progressDialog = new ProgressDialog(Password.this);
        loadProfile(Password.this, encodedImageData, menYellow);
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
                hideSoftKeyboard(Password.this, password);
                if (!validate()) {

                } else {
                    openNextScreen();
                    registerCompanyTeam();
                }
            }
        });
    }

    public boolean validate() {
        Log.d(TAG, "validate: ");
        passwordValue = password.getText().toString().trim();

        boolean valid = true;
        if (passwordValue.isEmpty()) {
            password.setError("Please enter your password");
            valid = false;
        }
        return valid;
    }

    void registerCompanyTeam() {
        HashMap<String, String> params = new HashMap<String, String>();


        Log.d(TAG, "getDocumentData: params: " + params);
        sendServerRequestPOST(AppConstants.GET_PROFILE, params);
    }

    @Override
    public void requestStarted() {
        super.requestStarted();
        Log.d(TAG, "requestStarted: running");
        progressDialog.setMessage(" get profile...");
        progressDialog.show();
    }

    @Override
    public void onResponseReceived(String response, String urlCalled) {
        super.onResponseReceived(response, urlCalled);
        progressDialog.dismiss();
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(response);
            String message = jsonObject.getString("message");
            Log.d(TAG, "onResponse: status: " + message);
            String status = jsonObject.getString("status");
            Log.d(TAG, "onResponse: status: " + status);
            JSONObject dataObject = jsonObject.getJSONObject("data");
            Log.d(TAG, "onResponse: data: size: " + dataObject.length());

            String email = dataObject.getString("email");
            Log.d(TAG, "onResponse: email " + email);

            String name = dataObject.getString("name");
            Log.d(TAG, "onResponse: name " + name);


            String profile_image = dataObject.getString("profile_image");
            Log.d(TAG, "onResponse: profile_image " + profile_image);

            String phone = dataObject.getString("phone");
            Log.d(TAG, "onResponse: phone " + phone);

            String job_title = dataObject.getString("job_title");
            Log.d(TAG, "onResponse: job_title " + job_title);

            Log.d(TAG, "onResponseReceived: response: " + response);
            Log.d(TAG, "onResponseReceived: urlCalled: " + urlCalled);



        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void requestEndedWithError(VolleyError error) {
        super.requestEndedWithError(error);
        progressDialog.dismiss();
        Log.d(TAG, "requestEndedWithError: error: " + error);
    }


    void openNextScreen() {

        Bundle bundle = new Bundle();
        bundle.putSerializable("Job", (Serializable) list);

        Intent intent = new Intent(Password.this, Congratulations.class);
        intent.putExtra("profilePic", encodedImageData);
        intent.putExtra("fullName", fullNameEditTextValue);
        intent.putExtra("emailAddress", emailValue);
        intent.putExtra("job", jobTitleValue);
        intent.putExtra("verification", codeValue);
        intent.putExtra("memberPhone", phoneValue);
        intent.putExtra("password", passwordValue);
        startActivity(intent);
    }

}