package com.antizon.uit_android.company.welcome;

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
import com.antizon.uit_android.generic.model.ModelCompanySize;
import com.antizon.uit_android.generic_utils.AppConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class EmailAddress extends BaseActivity {
    private static final String TAG = EmailAddress.class.getSimpleName();
    ProgressDialog progressDialog;
    ImageView backIcon, redNoah2;
    TextView next;
    EditText emailEditText;

    String selectedStageId = "", selectedStageName = "", typeHereValue = "", companyNameHintValue = "", encodedImageData = "",
            sizeValue = "", headquarterValue = "", websiteValue="", emailValue = "";
    ArrayList<ModelCompanySize> industriesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_address2);
        setIds();
        getIntentData();
        initialize();
        setListener();

    }


    @Override
    protected void onResume() {
        super.onResume();

//        sendCode();
    }

    void setIds() {
        Log.d(TAG, "setIds: ");

        backIcon = findViewById(R.id.backIcon);
        next = findViewById(R.id.next);
        redNoah2 = findViewById(R.id.redNoah2);
        emailEditText = findViewById(R.id.emailEditText);

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

            Log.d(TAG, "getIntentData: image: "+encodedImageData);
            Log.d(TAG, "getIntentData: companyName: "+companyNameHintValue);
            Log.d(TAG, "getIntentData: bio: "+typeHereValue);
            Log.d(TAG, "getIntentData: stageName: "+selectedStageName);
            Log.d(TAG, "getIntentData: stageId: "+selectedStageId);
            Log.d(TAG, "getIntentData: headquarter: "+headquarterValue);
            Log.d(TAG, "getIntentData: website: "+websiteValue);

            Bundle bundle = getIntent().getExtras();

                industriesList = bundle.getParcelableArrayList("industries");
            Log.d(TAG, "getIntentData: industriesList: " + industriesList.size());

        }
    }

    void initialize() {
        Log.d(TAG, "initialize: ");
        progressDialog = new ProgressDialog(this);
        loadProfile(EmailAddress.this, encodedImageData, redNoah2);
        industriesList = new ArrayList<>();
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
                hideSoftKeyboard(EmailAddress.this, emailEditText);
                if (!validate()) {

                } else {
                    sendVerificationCode();
                }
            }
        });
    }

    public boolean validate() {
        Log.d(TAG, "validate: ");
        emailValue = emailEditText.getText().toString().trim();

        boolean valid = true;
        if (emailValue.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(emailValue).matches()) {

            emailEditText.setError("Please enter valid email");
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
        bundle.putParcelableArrayList("industries", industriesList);

        Intent intent = new Intent(EmailAddress.this, VerificationCode.class);
        intent.putExtra("profilePic", encodedImageData);
        intent.putExtra("companyName", companyNameHintValue);
        intent.putExtra("bio", typeHereValue);
        intent.putExtra("stageId", "" +selectedStageId);
        intent.putExtra("stageName", selectedStageName);
        intent.putExtra("size", sizeValue);
        intent.putExtra("headquarter", headquarterValue);
        intent.putExtra("website", websiteValue);
        intent.putExtra("email", emailValue);
        intent.putExtras(bundle);
        startActivity(intent);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }
}