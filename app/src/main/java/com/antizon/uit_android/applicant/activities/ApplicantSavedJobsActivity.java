package com.antizon.uit_android.applicant.activities;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.volley.VolleyError;
import com.antizon.uit_android.R;
import com.antizon.uit_android.company.utility.BaseActivity;
import com.antizon.uit_android.generic.adapter.SavedJobsAdapter;
import com.antizon.uit_android.generic.model.ModelAllJobs;
import com.antizon.uit_android.generic_utils.AppConstants;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.utilities.Utilities;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class ApplicantSavedJobsActivity extends BaseActivity {
    private static final String TAG = ApplicantSavedJobsActivity.class.getSimpleName();

    TextView pending, approved;
    ConstraintLayout pendingLayout, approvedLayout;
    SessionManagement sessionManagement;
    ProgressDialog progressDialog;
    ImageView backIcon;
    RecyclerView applied_jobs_recyclerview, saved_jobs_recyclerview;


    SavedJobsAdapter savedJobsAdapter;
    List<ModelAllJobs> savedList;
    ModelAllJobs savedDataModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applicant_saved_jobs);
        Utilities.setWhiteBars(ApplicantSavedJobsActivity.this);
        setIds();
        initialize();
        setListener();
        setUpAppliedJobRecyclerview();
        sendServerRequestGET(AppConstants.GET_APPLIED_JOBS + "?job_status=" + 1 + "&off_set=0", sessionManagement.getToken());
    }

    void setIds() {

        Log.d(TAG, "setIds: ");
        applied_jobs_recyclerview = findViewById(R.id.applied_jobs_recyclerview);
        saved_jobs_recyclerview = findViewById(R.id.saved_jobs_recyclerview);
        pending = findViewById(R.id.pending);
        approved = findViewById(R.id.approved);
        pendingLayout = findViewById(R.id.pendingLayout);
        approvedLayout = findViewById(R.id.approvedLayout);
        backIcon=findViewById(R.id.backIcon);

    }

    void initialize() {
        Log.d(TAG, "initialize: ");
        progressDialog = new ProgressDialog(this);
        sessionManagement = new SessionManagement(ApplicantSavedJobsActivity.this);
//        list = new ArrayList<>();
        savedList = new ArrayList<>();
    }

    void setListener() {
        Log.d(TAG, "setListener: ");

        backIcon.setOnClickListener(view -> onBackPressed());

        pendingLayout.setOnClickListener(view -> {
            Log.d(TAG, "onClick: applied: ");
            setPendingLayout();
        });

        approvedLayout.setOnClickListener(view -> {
            Log.d(TAG, "onClick: saved: ");
            setApprovedLayout();
        });
    }

    void setUpAppliedJobRecyclerview() {
        Log.d(TAG, "setUpRecyclerViewLinearLayout: ");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        applied_jobs_recyclerview.setLayoutManager(linearLayoutManager);
        savedJobsAdapter = new SavedJobsAdapter(savedList, ApplicantSavedJobsActivity.this);
        applied_jobs_recyclerview.setAdapter(savedJobsAdapter);
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
            savedList.clear();

            JSONObject jsonObject = new JSONObject(response);
            boolean status = jsonObject.getBoolean("status");

            if (status) {
                JSONArray dataArray = jsonObject.getJSONArray("data");
                Log.d(TAG, "onResponse: dataArray: " + dataArray.length());

                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject dataObject = dataArray.getJSONObject(i);
                    int id, user_id, location_status, employment_type, job_status, min_salary, max_salary, total_applications, role;
                    String name, city, profile_image, job_title, state,
                            created_at, reason_to_close, greenhouse_access_token, match;

                    created_at = dataObject.getString("created_at");
                    id = dataObject.getInt("id");
                    job_title = dataObject.getString("job_title");
                    city = dataObject.getString("city");
                    reason_to_close = dataObject.getString("reason_to_close");
                    match = dataObject.getString("match");
                    state = dataObject.getString("state");
                    location_status = dataObject.getInt("location_status");
                    employment_type = dataObject.getInt("employment_type");
                    job_status = dataObject.getInt("job_status");
                    min_salary = dataObject.getInt("min_salary");
                    max_salary = dataObject.getInt("max_salary");
                    total_applications = dataObject.getInt("total_applications");

                    savedDataModel = new ModelAllJobs();
                    savedDataModel.setId(id);
                    savedDataModel.setJob_title(job_title);
                    savedDataModel.setMatch(match);
                    savedDataModel.setCity(city);
                    savedDataModel.setReason_to_close(reason_to_close);
                    savedDataModel.setState(state);
                    savedDataModel.setLocation_status(location_status);
                    savedDataModel.setEmployment_type(employment_type);
                    savedDataModel.setJob_status(job_status);
                    savedDataModel.setMin_salary(min_salary);
                    savedDataModel.setMax_salary(max_salary);
                    savedDataModel.setTotal_applications(total_applications);
                    savedDataModel.setCreated_at(created_at);
                    savedList.add(savedDataModel);

                    JSONObject industryObject = dataObject.getJSONObject("industry");
                    id = industryObject.getInt("id");
                    name = industryObject.getString("name");
                    savedDataModel.setId(id);
                    savedDataModel.setName(name);


                    JSONObject userObject = dataObject.getJSONObject("user");
                    id = userObject.getInt("id");
                    name = userObject.getString("name");
                    role = userObject.getInt("role");
                    user_id = userObject.getInt("user_id");
                    profile_image = userObject.getString("profile_image");
                    job_title = userObject.getString("job_title");
                    greenhouse_access_token = userObject.getString("greenhouse_access_token");

                    savedDataModel.setUser_id(user_id);
                    savedDataModel.setRole(role);
                    savedDataModel.setName(name);
                    savedDataModel.setGreenhouse_access_token(greenhouse_access_token);
                    savedDataModel.setProfile_image(profile_image);
                    savedDataModel.setJob_title(job_title);
                    savedDataModel.setId(id);

                    JSONObject companyObject = dataObject.getJSONObject("company");
                    id = companyObject.getInt("id");
                    name = companyObject.getString("name");
                    role = companyObject.getInt("role");
                    user_id = companyObject.getInt("user_id");
                    profile_image = companyObject.getString("profile_image");
                    job_title = companyObject.getString("job_title");
                    greenhouse_access_token = companyObject.getString("greenhouse_access_token");

                    savedDataModel.setUser_id(user_id);
                    savedDataModel.setRole(role);
                    savedDataModel.setName(name);
                    savedDataModel.setGreenhouse_access_token(greenhouse_access_token);
                    savedDataModel.setProfile_image(profile_image);
                    savedDataModel.setJob_title(job_title);
                    savedDataModel.setId(id);

                    JSONArray applicationsArray = dataObject.getJSONArray("applications");
                    Log.d(TAG, "onResponse: applications: size: " + applicationsArray.length());

                    for (int j = 0; j < applicationsArray.length(); j++) {
                        JSONObject jsonObject1 = applicationsArray.getJSONObject(j);
                        Log.d(TAG, "onResponse: "+jsonObject1);


                    }

                }
                Log.d(TAG, "onResponse: pendingList: " + savedList.size());
                savedJobsAdapter.notifyDataSetChanged();
            }

        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void requestEndedWithError(VolleyError error) {
        super.requestEndedWithError(error);
        progressDialog.dismiss();
        Log.d(TAG, "onErrorResponse: error: " + error);
    }


    private void setPendingLayout() {
        pendingLayout.setBackgroundResource(R.drawable.app_color_curved_background);
        approvedLayout.setBackgroundResource(R.drawable.white_curved_background);
        pending.setTextColor(getColor(R.color.white));
        approved.setTextColor(getColor(R.color.black));
        sendServerRequestGET(AppConstants.GET_APPLIED_JOBS + "?job_status=" + 1 + "&off_set=0", sessionManagement.getToken());
    }

    private void setApprovedLayout() {
        pendingLayout.setBackgroundResource(R.drawable.white_curved_background);
        approvedLayout.setBackgroundResource(R.drawable.app_color_curved_background);
        pending.setTextColor(getColor(R.color.black));
        approved.setTextColor(getColor(R.color.white));

        sendServerRequestGET(AppConstants.GET_SAVED_JOBS, sessionManagement.getToken());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }
}