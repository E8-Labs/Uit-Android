package com.antizon.uit_android.recruiter.welcome;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;

import com.antizon.uit_android.R;
import com.antizon.uit_android.company.utility.BaseActivity;
import com.antizon.uit_android.generic.adapter.ApplicantJobSearchAdapter;
import com.antizon.uit_android.generic.model.ModelApplicantJobSearch;
import com.antizon.uit_android.generic_utils.AppConstants;
import com.antizon.uit_android.generic_utils.SessionManagement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RecruiterListedJobs extends BaseActivity {
    private static final String TAG = RecruiterListedJobs.class.getSimpleName();
    SessionManagement sessionManagement;
    ProgressDialog progressDialog;
    RecyclerView listed_recruiter_recyclerview;
    ApplicantJobSearchAdapter applicantJobSearchAdapter;
    private List<ModelApplicantJobSearch> list;
    private ModelApplicantJobSearch dataModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recruiter_listed_jobs);
        setIds();
        initialize();
        setListener();
        setUpListedJobRecyclerview();
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
        Log.d(TAG, "onResume: url: " + AppConstants.RECRUITER_JOBS_LIST);
        Log.d(TAG, "onResume: token: " + sessionManagement.getToken());
        sendServerRequestGET(AppConstants.RECRUITER_JOBS_LIST, sessionManagement.getToken());
    }
    void setIds() {
        Log.d(TAG, "setIds: ");
        listed_recruiter_recyclerview = findViewById(R.id.listed_recruiter_recyclerview);
    }

    void initialize() {
        Log.d(TAG, "initialize: ");
        progressDialog = new ProgressDialog(this);
        sessionManagement = new SessionManagement(RecruiterListedJobs.this);
        list = new ArrayList<>();
    }

    void setListener() {
        Log.d(TAG, "setListener: ");

    }
    void setUpListedJobRecyclerview() {
        Log.d(TAG, "setUpRecyclerViewLinearLayout: ");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        listed_recruiter_recyclerview.setLayoutManager(linearLayoutManager);
        applicantJobSearchAdapter = new ApplicantJobSearchAdapter(list, RecruiterListedJobs.this);
        listed_recruiter_recyclerview.setAdapter(applicantJobSearchAdapter);
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
                JSONArray dataArray = jsonObject.getJSONArray("data");
                if (dataArray.length() > 0) {
                    list.clear();

                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject dataObject = dataArray.getJSONObject(i);
                        Log.d(TAG, "onResponseReceived: "+dataObject);


                        list.add(dataModel);
                    }
                }
            }
            applicantJobSearchAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}