package com.antizon.uit_android.uit_admin.welcome;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.antizon.uit_android.R;
import com.antizon.uit_android.applicant.activities.ActivityDemoInfo;
import com.antizon.uit_android.company.utility.BaseActivity;
import com.antizon.uit_android.generic.model.ModelUitAdminPending;
import com.antizon.uit_android.generic_utils.AppConstants;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class PendingProfile extends BaseActivity {
    private static final String TAG = PendingProfile.class.getSimpleName();
    SessionManagement sessionManagement;
    ProgressDialog progressDialog;
    ImageView noahImage,arrowTwo,arrow,grayBack;
    TextView title,product,noahEmail,userAddress,roleText,employ,phoneNumber,series2;
    ModelUitAdminPending pendingModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_profile);
        setIds();
        getIntentData();
        initialize();
        setListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
        companyProfile();
    }

    void setIds() {
        Log.d(TAG, "setIds: ");
        noahImage = findViewById(R.id.noahImage);
        noahImage = findViewById(R.id.noahImage);
        arrowTwo = findViewById(R.id.arrowTwo);
        arrow = findViewById(R.id.arrow);
        noahEmail = findViewById(R.id.noahEmail);
        userAddress = findViewById(R.id.userAddress);
        roleText = findViewById(R.id.roleText);
        employ = findViewById(R.id.employ);
        phoneNumber = findViewById(R.id.phoneNumber);
        series2 = findViewById(R.id.series2);
        product = findViewById(R.id.product);
        title = findViewById(R.id.title);
        grayBack = findViewById(R.id.grayBack);

    }

    void getIntentData() {
        if (getIntent().getExtras().getSerializable("dataModel") != null) {
            pendingModel = (ModelUitAdminPending) getIntent().getExtras().getSerializable("dataModel");

            noahEmail.setText(pendingModel.getEmail());
            Glide.with(PendingProfile.this)
                    .load(pendingModel.getProfile_image())
                    .circleCrop()
                    .into(noahImage);
            title.setText(pendingModel.getName());
            product.setText(pendingModel.getJob_title());
            roleText.setText(pendingModel.getBio());
        }

    }


    void initialize() {
        Log.d(TAG, "initialize: ");
        progressDialog = new ProgressDialog(this);
        sessionManagement = new SessionManagement(PendingProfile.this);

    }

    void setListener() {
        Log.d(TAG, "setListener: ");

        grayBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        arrowTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(PendingProfile. this, ActivityDemoInfo.class);
                intent.putExtra("pendingModel", pendingModel);
                startActivity(intent);
            }
        });
        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(PendingProfile. this, DeiInformation.class);
                intent.putExtra("pendingModel", pendingModel);
                startActivity(intent);
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
                String bio = dataObject.getString("bio");
                Log.d(TAG, "onResponse: bio " + bio);
                String job_title = dataObject.getString("job_title");
                Log.d(TAG, "onResponse: job_title " + job_title);
                String phone = dataObject.getString("phone");
                Log.d(TAG, "onResponse: phone " + phone);
                String address = dataObject.getString("address");

                String website = dataObject.getString("website");
                Log.d(TAG, "onResponse: website " + website);

                phoneNumber.setText(website);
                roleText.setText(bio);
                product.setText(job_title);
                userAddress.setText(address);

                JSONArray industriesArray = dataObject.getJSONArray("industries");
                Log.d(TAG, "onResponse: industries: size: " + industriesArray.length());
                for (int i = 0; i < industriesArray.length(); i++) {

                    JSONObject jsonObject1 = industriesArray.getJSONObject(i);
                    String name = jsonObject1.getString("name");
                    Log.d(TAG, "onResponse: name " + name);

                    product.setText(name);
                    title.setText(name);
                }
                JSONArray user_sizesArray = dataObject.getJSONArray("user_sizes");
                Log.d(TAG, "onResponse: user_sizes: size: " + user_sizesArray.length());
                for (int i = 0; i < user_sizesArray.length(); i++) {

                    JSONObject jsonObject1 = user_sizesArray.getJSONObject(i);
                    String name = jsonObject1.getString("name");
                    Log.d(TAG, "onResponse: name " + name);

                    employ.setText(name);
                }


                JSONArray user_stagesArray = dataObject.getJSONArray("user_stages");
                Log.d(TAG, "onResponse: user_stages: size: " + user_stagesArray.length());
                for (int i = 0; i < user_stagesArray.length(); i++) {

                    JSONObject jsonObject1 = user_stagesArray.getJSONObject(i);
                    String name = jsonObject1.getString("name");
                    Log.d(TAG, "onResponse: name " + name);

                    series2.setText(name);
                }

                JSONObject dei_statementObject = dataObject.getJSONObject("dei_statement");
                Log.d(TAG, "onResponse: dei_statement: size: " + dei_statementObject.length());
                String deistatement = dei_statementObject.getString("deistatement");
                int team_lead = dei_statementObject.getInt("team_lead");

                int programming = dei_statementObject.getInt("programming");
                int training = dei_statementObject.getInt("training");
                int men = dei_statementObject.getInt("men");
                int women = dei_statementObject.getInt("women");
                int lgbt = dei_statementObject.getInt("lgbt");
                int erg = dei_statementObject.getInt("erg");

                pendingModel.setDeistatement(deistatement);
                pendingModel.setTeam_lead(team_lead);
                pendingModel.setProgramming(programming);
                pendingModel.setTraining(training);
                pendingModel.setMen(men);
                pendingModel.setWomen(women);
                pendingModel.setLgbt(lgbt);
                pendingModel.setErg(erg);

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