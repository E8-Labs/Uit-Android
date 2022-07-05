package com.antizon.uit_android.applicant.welcome;


import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.antizon.uit_android.generic.adapter.ApplicantDegreeAdapter;
import com.antizon.uit_android.generic.model.ModelApplicantDegree;
import com.antizon.uit_android.generic_utils.AppConstants;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Degree extends BaseActivity {

    private static final String TAG = Degree.class.getSimpleName();
    ProgressDialog progressDialog;
    RecyclerView applicantDegreeRecyclerview;
    ApplicantDegreeAdapter driverAdapter;
    private List<ModelApplicantDegree> list;
    private ModelApplicantDegree dataModel, selectedDataModel;

    String typeHereValue= "", employeeValue = "", educationValue="" , degreeValue="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_degree);

        getIntentData();
        setIds();
        initialize();
        setListener();
        setUpApplicantDegreeRecyclerview();

    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");

        getApplicantDegree();
    }

    void setIds() {
        Log.d(TAG, "setIds: ");

        applicantDegreeRecyclerview = findViewById(R.id.applicant_degree_recyclerview);
    }

    void initialize() {
        Log.d(TAG, "initialize: ");
        progressDialog = new ProgressDialog(Degree.this);
        list = new ArrayList<>();


    }
    void getIntentData() {

        employeeValue = getIntent().getStringExtra("employeStatus");
        typeHereValue = getIntent().getStringExtra("bio");
        educationValue = getIntent().getStringExtra("education");
    }
    void setListener() {
        Log.d(TAG, "setListener: ");

    }


    void setUpApplicantDegreeRecyclerview() {
        Log.d(TAG, "setUpRecyclerViewLinearLayoutForChat: ");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Degree.this);
        applicantDegreeRecyclerview.setLayoutManager(linearLayoutManager);


        driverAdapter = new ApplicantDegreeAdapter(list, Degree.this,

                new ApplicantDegreeAdapter.SelectionListener() {
                    @Override
                    public void selectedapplicantDegree(int position) {
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
                        driverAdapter.notifyDataSetChanged();
                    }
                });

        applicantDegreeRecyclerview.setAdapter(driverAdapter);
    }

    void getApplicantDegree() {
        Log.d(TAG, "getCompanySize: ");
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        RequestQueue requestQueue = Volley.newRequestQueue(Degree.this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConstants.GET_DEGREE_LIST,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse: " + response);
                        try {

                            progressDialog.dismiss();
                            JSONObject jsonObject = new JSONObject(response);

                            boolean status = jsonObject.getBoolean("status");
                            if (status) {
                                JSONArray dataArray = jsonObject.getJSONArray("data");
                                if (dataArray.length() > 0) {
                                    list.clear();

                                    for (int i = 0; i < dataArray.length(); i++) {
                                        JSONObject dataObject = dataArray.getJSONObject(i);

                                        int id;
                                        String name = "", subTitle = "";

                                        id = dataObject.getInt("id");
                                        name = dataObject.getString("name");
//                                        subTitle = dataObject.getString("subTitle");

                                        dataModel = new ModelApplicantDegree();
                                        dataModel.setId(id);
                                        dataModel.setName(name);
//                                        dataModel.setSubTitle(subTitle);

                                        list.add(dataModel);
                                    }
                                }
                            }
                            driverAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                error -> {

                    progressDialog.dismiss();
                    Log.d(TAG, "onErrorResponse: error: " + error);
                });


        stringRequest.setRetryPolicy(new DefaultRetryPolicy((int) TimeUnit.SECONDS.toMillis(120), //After the set time elapses the request will timeout
                0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }


}