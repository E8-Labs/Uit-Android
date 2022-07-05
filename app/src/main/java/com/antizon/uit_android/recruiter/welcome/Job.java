package com.antizon.uit_android.recruiter.welcome;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.antizon.uit_android.R;
import com.antizon.uit_android.company.utility.BaseActivity;

import com.antizon.uit_android.generic.adapter.ApplicantJobsAdapter;

import com.antizon.uit_android.generic.model.ModelApplicantJobs;
import com.antizon.uit_android.generic_utils.AppConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class Job extends BaseActivity {
    private static final String TAG = Job.class.getSimpleName();

    ProgressDialog progressDialog;
    ImageView backIcon,menYellow;
    TextView next;
    EditText search;
    RecyclerView applicantJobRecyclerview;
    ApplicantJobsAdapter applicantJobAdapter;
    private List<ModelApplicantJobs> list;
    private ModelApplicantJobs dataModel, selectedDataModel;
    String fullNameEditTextValue = "", encodedImageData = "", jobTitleValue = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job);


        setIds();
        getIntentData();
        initialize();
        setListener();

    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");

        getApplicantJob();
    }

    void setIds() {
        Log.d(TAG, "setIds: ");
        backIcon = findViewById(R.id.backIcon);
        applicantJobRecyclerview = findViewById(R.id.applicant_jobs_recyclerview);
        search = findViewById(R.id.searchJob);
        next = findViewById(R.id.next);
        menYellow = findViewById(R.id.menYellow);
    }
    void getIntentData() {

        if (getIntent() != null) {
            encodedImageData = getIntent().getStringExtra("profilePic");
            fullNameEditTextValue = getIntent().getStringExtra("fullName");
            Log.d(TAG, "getIntentData: image: "+encodedImageData);
            Log.d(TAG, "getIntentData: fullName: "+fullNameEditTextValue);
        }
    }

    void initialize() {
        Log.d(TAG, "initialize: ");
        progressDialog = new ProgressDialog(Job.this);
        list = new ArrayList<>();
        loadProfile(Job.this, encodedImageData, menYellow);

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

                openNextScreen();
            }
        });


        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String searchedString = search.getText().toString().toLowerCase(Locale.getDefault());
                applicantJobAdapter.search(searchedString);
                Log.d(TAG, "onTextChanged: " + searchedString);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }



    void getApplicantJob() {
        Log.d(TAG, "getCompanySize: ");
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        RequestQueue requestQueue = Volley.newRequestQueue(Job.this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConstants.GET_APPLICANT_JOBS,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse: " + response);
                        try {

                            progressDialog.dismiss();
                            JSONObject jsonObject = new JSONObject(response);

                            Boolean status = jsonObject.getBoolean("status");
                            if (status) {
                                JSONArray dataArray = jsonObject.getJSONArray("data");
                                if (dataArray.length() > 0) {
                                    list.clear();

                                    for (int i = 0; i < dataArray.length(); i++) {
                                        JSONObject dataObject = dataArray.getJSONObject(i);

                                        int id;
                                        String name = "";

                                        id = dataObject.getInt("id");
                                        name = dataObject.getString("name");

                                        dataModel = new ModelApplicantJobs();
                                        dataModel.setId(id)
                                        ;
                                        dataModel.setName(name);


                                        list.add(dataModel);
                                    }
                                }
                            }
                            applicantJobAdapter.notifyDataSetChanged();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        progressDialog.dismiss();
                        Log.d(TAG, "onErrorResponse: error: " + error);
                    }
                });


        stringRequest.setRetryPolicy(new DefaultRetryPolicy((int) TimeUnit.SECONDS.toMillis(120), //After the set time elapses the request will timeout
                0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    void openNextScreen() {

        Bundle bundle = new Bundle();
        bundle.putSerializable("Job", (Serializable) list);

        Intent intent = new Intent(Job.this, EmailAddress.class);
        intent.putExtras(bundle);
        intent.putExtra("fullName", fullNameEditTextValue);
        intent.putExtra("profilePic", encodedImageData);
        startActivity(intent);
    }
}