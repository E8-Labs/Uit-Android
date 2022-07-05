package com.antizon.uit_android.uit_admin.welcome;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.antizon.uit_android.R;
import com.antizon.uit_android.company.utility.BaseActivity;
import com.antizon.uit_android.generic.activities.GenericProfile;
import com.antizon.uit_android.generic.model.ModelAdminApplicants;
import com.antizon.uit_android.generic.model.ModelUitAdminApproved;
import com.antizon.uit_android.generic.model.ModelUitAdminPending;
import com.antizon.uit_android.generic_utils.AppConstants;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MemberDetail extends BaseActivity {
    private static final String TAG = MemberDetail.class.getSimpleName();
    ImageView arrow,noahImage,grayBack;
    TextView enterEmail,enterPhone,product,title;

    SessionManagement sessionManagement;
    ProgressDialog progressDialog;
    ModelAdminApplicants dataModel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_detail);

        setIds();
        initialize();
        setListener();
        getIntentData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
        companyProfile();
    }

    void setIds() {
        Log.d(TAG, "setIds: ");
        arrow = findViewById(R.id.arrow);
        noahImage = findViewById(R.id.noahImage);
        enterEmail = findViewById(R.id.enterEmail);
        enterPhone = findViewById(R.id.enterPhone);
        product = findViewById(R.id.product);
        title = findViewById(R.id.title);
        grayBack = findViewById(R.id.grayBack);
    }
    void getIntentData() {
        if (getIntent().getExtras().getSerializable("dataModel") != null) {
            dataModel = (ModelAdminApplicants) getIntent().getExtras().getSerializable("dataModel");
            enterEmail.setText(dataModel.getEmail());
            Glide.with(MemberDetail.this)
                    .load(dataModel.getProfile_image())
                    .circleCrop()
                    .into(noahImage);
            enterPhone.setText(dataModel.getName());

        }
    }
    void initialize() {
        Log.d(TAG, "initialize: ");
        progressDialog = new ProgressDialog(this);
        sessionManagement = new SessionManagement(MemberDetail.this);
//        loadProfile(MemberDetail.this, sessionManagement.getProfileImage(), noahImage);
    }

    void setListener() {
        Log.d(TAG, "setListener: ");


        grayBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MemberDetail.this, AllListedJobs.class);
                startActivity(intent);
                overridePendingTransition(R.anim.left_in, R.anim.left_out);
            }
        });


    }
    void companyProfile() {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("user_id", "6");
        sendServerRequestPOST(AppConstants.GET_PROFILE, params, sessionManagement.getToken());
    }

    @Override
    public void requestStarted() {
        super.requestStarted();
        progressDialog.setMessage("Loading...");
        progressDialog.show();
    }

    @Override
    public void onResponseReceived(String response, String urlCalled) {
        super.onResponseReceived(response, urlCalled);

        Log.d(TAG, "onResponse: " + response);
        try {

            progressDialog.dismiss();
            JSONObject jsonObject = new JSONObject(response);

            Boolean status = jsonObject.getBoolean("status");
            String message = jsonObject.getString("message");
            Log.d(TAG, "onResponse: " + message);
            if (status) {
                JSONObject dataObject = jsonObject.getJSONObject("data");
                Log.d(TAG, "onResponse: data: size: " + dataObject.length());

                JSONArray team_membersArray = dataObject.getJSONArray("team_members");
                Log.d(TAG, "onResponse: team_members: size: " + team_membersArray.length());
                for (int i = 0; i < team_membersArray.length(); i++) {

                    JSONObject jsonObject1 = team_membersArray.getJSONObject(i);
                    String profile_image = jsonObject1.getString("profile_image");
                    Log.d(TAG, "onResponse: profile_image " + profile_image);
                    String name = jsonObject1.getString("name");
                    Log.d(TAG, "onResponse: name " + name);
                    String email = jsonObject1.getString("email");
                    Log.d(TAG, "onResponse: email " + email);
                    String job_title = jsonObject1.getString("job_title");
                    Log.d(TAG, "onResponse: job_title " + job_title);

                    title.setText(name);
                    enterEmail.setText(email);
                    product.setText(job_title);

                    Glide.with(MemberDetail.this)
                            .load(profile_image)
                            .circleCrop()
                            .into(noahImage);


                }


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void requestEndedWithError(VolleyError error) {
        super.requestEndedWithError(error);
        progressDialog.dismiss();
        Log.d(TAG, "onErrorResponse: error: " + error);
    }
}