package com.antizon.uit_android.uit_members.welcome;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.antizon.uit_android.R;
import com.antizon.uit_android.company.utility.BaseActivity;
import com.antizon.uit_android.generic.adapter.AdminApplicantsAdapter;
import com.antizon.uit_android.generic.model.ModelAdminApplicants;
import com.antizon.uit_android.generic_utils.AppConstants;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.uit_admin.welcome.FlaggedApplicantsActivity;
import com.antizon.uit_android.utilities.Utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ActivityApplicants extends BaseActivity {
    private static final String TAG = ActivityApplicants.class.getSimpleName();

    ImageView userProfile;
    TextView flaggedApplicant;

    SessionManagement sessionManagement;
    ProgressDialog progressDialog;
    RecyclerView adminApplicantRecyclerView;
    AdminApplicantsAdapter adminApplicantsAdapter;
    List<ModelAdminApplicants> list;
    ModelAdminApplicants dataModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applicants);
        Utilities.setCustomStatusAndNavColor(ActivityApplicants.this, R.color.white_dash, R.color.white_dash);
        setIds();
        initialize();
        setListener();
        setUpAdminApplicantsRecyclerview();
        sendServerRequestGET(AppConstants.ADMIN_APPLICANTS_LIST, sessionManagement.getToken());
    }


    void setIds() {
        Log.d(TAG, "setIds: ");
        adminApplicantRecyclerView = findViewById(R.id.admin_applicant_recyclerView);
        userProfile = findViewById(R.id.userProfile);
        flaggedApplicant = findViewById(R.id.flaggedApplicant);
    }
    void initialize() {
        Log.d(TAG, "initialize: ");
        progressDialog = new ProgressDialog(this);
        sessionManagement = new SessionManagement(ActivityApplicants.this);
        list = new ArrayList<>();
        loadProfile(ActivityApplicants.this, sessionManagement.getProfileImage(), userProfile);
    }
    void setListener() {
        Log.d(TAG, "setListener: ");
        flaggedApplicant.setOnClickListener(view -> {
            Intent intent = new Intent(ActivityApplicants.this, FlaggedApplicantsActivity.class);
            startActivity(intent);
        });


    }
    void setUpAdminApplicantsRecyclerview() {
        Log.d(TAG, "setUpRecyclerViewLinearLayout: ");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        adminApplicantRecyclerView.setLayoutManager(linearLayoutManager);
        adminApplicantsAdapter = new AdminApplicantsAdapter(list, ActivityApplicants.this);
        adminApplicantRecyclerView.setAdapter(adminApplicantsAdapter);
    }

    @Override
    public void requestStarted() {
        super.requestStarted();
        progressDialog.setMessage("Loading...");
        progressDialog.show();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onResponseReceived(String response, String urlCalled) {
        super.onResponseReceived(response, urlCalled);

        Log.d(TAG, "onResponse: " + response);
        try {

            progressDialog.dismiss();
            JSONObject jsonObject = new JSONObject(response);

            boolean status = jsonObject.getBoolean("status");
            String message = jsonObject.getString("message");
            Log.d(TAG, "onResponse: " + message);
            if (status) {
                JSONArray dataArray = jsonObject.getJSONArray("data");
                if (dataArray.length() > 0) {
                    list.clear();

                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject dataObject = dataArray.getJSONObject(i);

                        int id = dataObject.getInt("id");
                        int user_id = dataObject.getInt("user_id");
                        String name = dataObject.getString("name");
                        String email = dataObject.getString("email");
                        String job_title = dataObject.getString("job_title");
                        String profile_image = dataObject.getString("profile_image");
                        String city = dataObject.getString("city");
                        String state = dataObject.getString("state");
                        String professional_info = dataObject.getString("professional_info");
                        String industries = dataObject.getString("industries");

                        dataModel = new ModelAdminApplicants();
                        dataModel.setId(id);
                        dataModel.setUser_id(user_id);
                        dataModel.setName(name);
                        dataModel.setEmail(email);
                        dataModel.setJob_title(job_title);
                        dataModel.setProfile_image(profile_image);
                        dataModel.setCity(city);
                        dataModel.setState(state);
                        dataModel.setProfessional_info(professional_info);
                        dataModel.setIndustries(industries);

                        list.add(dataModel);
                    }
                }
            }
            adminApplicantsAdapter.notifyDataSetChanged();
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