package com.antizon.uit_android.applicant.activities;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import com.android.volley.VolleyError;
import com.antizon.uit_android.R;
import com.antizon.uit_android.company.utility.BaseActivity;
import com.antizon.uit_android.generic.adapter.ApplicantIndustryAdapter;
import com.antizon.uit_android.generic.model.ModelCompanySize;
import com.antizon.uit_android.generic_utils.AppConstants;
import com.antizon.uit_android.generic_utils.SessionManagement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class JobFilterActivity extends BaseActivity {

    private static final String TAG = JobFilterActivity.class.getSimpleName();

    ProgressDialog progressDialog;
    EditText industryText;
    SessionManagement sessionManagement;
    RecyclerView companyIndustryRecyclerView;
    ApplicantIndustryAdapter applicantIndustryAdapter;
    private List<ModelCompanySize> list;
    private ModelCompanySize dataModel, selectedDataModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_filter);

        setIds();
        initialize();
        setListener();
        setUpCompanyIndustryRecyclerView();

    }
        @Override
        protected void onResume() {
            super.onResume();
            Log.d(TAG, "onResume: ");

//            getCompanyIndustry();

            Log.d(TAG, "onResume: url: "+ AppConstants.GET_COMPANY_INDUSTRY);
            Log.d(TAG, "onResume: token: "+sessionManagement.getToken());
            sendServerRequestGET(AppConstants.GET_COMPANY_INDUSTRY, sessionManagement.getToken());
        }
        void setIds() {
            Log.d(TAG, "setIds: ");
            companyIndustryRecyclerView = findViewById(R.id.company_industry_recyclerview);
            industryText = findViewById(R.id.industryText);
        }
        void initialize(){
            Log.d(TAG, "initialize: ");
            sessionManagement = new SessionManagement(JobFilterActivity.this);
            progressDialog = new ProgressDialog(JobFilterActivity.this);
            list = new ArrayList<>();

        }

        void setListener()
        {
            Log.d(TAG, "setListener: ");

            industryText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    String searchedString = industryText.getText().toString().toLowerCase(Locale.getDefault());
                    applicantIndustryAdapter.search(searchedString);
                    Log.d(TAG, "onTextChanged: " + searchedString);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
        void setUpCompanyIndustryRecyclerView() {
            Log.d(TAG, "setUpRecyclerViewLinearLayoutForChat: ");

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(JobFilterActivity.this);
            companyIndustryRecyclerView.setLayoutManager(linearLayoutManager);
            applicantIndustryAdapter = new ApplicantIndustryAdapter(list, JobFilterActivity.this,

                    new ApplicantIndustryAdapter.SelectionListener() {
                        @Override
                        public void selectedcompanyIndustry(int position) {
//                        ModelCompanySize modelCompanySize,
                            Log.d(TAG, "selectedApplicantRace: position: " + position);
                            for (int i = 0; i < list.size(); i++) {
                                if (i == position) {
                                    list.get(i).setSelected(true);
                                    selectedDataModel = list.get(i);
                                } else {
                                    list.get(i).setSelected(false);
                                }
                            }
                            applicantIndustryAdapter.notifyDataSetChanged();
                        }
                    });

            companyIndustryRecyclerView.setAdapter(applicantIndustryAdapter);
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
            String message = jsonObject.getString("message");
            Log.d(TAG, "onResponse: " + message);
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

                        dataModel = new ModelCompanySize();
                        dataModel.setId(id);
                        dataModel.setName(name);

                        list.add(dataModel);
                    }
                }
            }
            applicantIndustryAdapter.notifyDataSetChanged();
            applicantIndustryAdapter.setFilterArrayListValue(list);
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




//        void getCompanyIndustry() {
//            Log.d(TAG, "getCompanySize: ");
//            progressDialog.setMessage("Loading...");
//            progressDialog.show();
//
//            RequestQueue requestQueue = Volley.newRequestQueue(JobFilter.this);
//
//            StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConstants.GET_COMPANY_INDUSTRY,
//                    new com.android.volley.Response.Listener<String>() {
//                        @Override
//                        public void onResponse(String response) {
//                            Log.d(TAG, "onResponse: " + response);
//                            try {
//
//                                progressDialog.dismiss();
//                                JSONObject jsonObject = new JSONObject(response);
//
//                                Boolean status = jsonObject.getBoolean("status");
//                                if (status) {
//                                    JSONArray dataArray = jsonObject.getJSONArray("data");
//                                    if (dataArray.length() > 0) {
//                                        list.clear();
//
//                                        for (int i = 0; i < dataArray.length(); i++) {
//                                            JSONObject dataObject = dataArray.getJSONObject(i);
//
//                                            int id;
//                                            String name = "";
//                                            id = dataObject.getInt("id");
//                                            name = dataObject.getString("name");
//
//                                            dataModel = new ModelCompanySize();
//                                            dataModel.setId(id);
//                                            dataModel.setName(name);
//
//                                            list.add(dataModel);
//                                        }
//                                    }
//                                }
//                                applicantIndustryAdapter.notifyDataSetChanged();
//                                applicantIndustryAdapter.setFilterArrayListValue(list);
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//
//                        }
//                    },
//                    new com.android.volley.Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//
//                            progressDialog.dismiss();
//                            Log.d(TAG, "onErrorResponse: error: " + error);
//                        }
//                    });
//
//            stringRequest.setRetryPolicy(new DefaultRetryPolicy((int) TimeUnit.SECONDS.toMillis(120), //After the set time elapses the request will timeout
//                    0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//            requestQueue.add(stringRequest);
//        }

    }
