package com.antizon.uit_android.applicant.welcome;


import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.antizon.uit_android.R;
import com.antizon.uit_android.company.utility.BaseActivity;
import com.antizon.uit_android.generic.adapter.ApplicantIndustryAdapter;
import com.antizon.uit_android.generic.model.ModelCompanySize;
import com.antizon.uit_android.generic_utils.AppConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class Industry extends BaseActivity {
    private static final String TAG = Industry.class.getSimpleName();

    ProgressDialog progressDialog;
    ImageView backIcon;
    TextView next;
    EditText search;

    RecyclerView companyIndustryRecyclerView;
    ApplicantIndustryAdapter applicantIndustryAdapter;
    private List<ModelCompanySize> list;
    private ModelCompanySize dataModel, selectedDataModel;

    String typeHereValue= "", employeeValue = "", educationValue="", industryValue="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role);
        setIds();
        initialize();
        setListener();
        getIntentData();
        setUpCompanyIndustryRecyclerView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");

        getCompanyIndustry();
    }
    void setIds() {
        Log.d(TAG, "setIds: ");
        backIcon = findViewById(R.id.backIcon);
        companyIndustryRecyclerView = findViewById(R.id.company_industry_recyclerview);
        next = findViewById(R.id.next);

        search = findViewById(R.id.searchIndustry);
    }
    void initialize(){
        Log.d(TAG, "initialize: ");

        progressDialog = new ProgressDialog(Industry.this);
        list = new ArrayList<>();

    }
    void getIntentData() {

        employeeValue = getIntent().getStringExtra("employeStatus");
        typeHereValue = getIntent().getStringExtra("bio");
        educationValue = getIntent().getStringExtra("education");
    }
    void setListener()
    {
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

                Bundle bundle = new Bundle();
                bundle.putSerializable("industries", (Serializable) list);
                openNextScreen();
                Intent intent = new Intent(Industry.this, ApplicantDepartmentActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.left_in, R.anim.left_out);

            }
        });

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String searchedString = search.getText().toString().toLowerCase(Locale.getDefault());
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

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Industry.this);
        companyIndustryRecyclerView.setLayoutManager(linearLayoutManager);


        applicantIndustryAdapter = new ApplicantIndustryAdapter(list, Industry.this,

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

    void getCompanyIndustry() {
        Log.d(TAG, "getCompanySize: ");
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        RequestQueue requestQueue = Volley.newRequestQueue(Industry.this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConstants.GET_COMPANY_INDUSTRY,
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

                                        dataModel = new ModelCompanySize();
                                        dataModel.setId(id);
                                        dataModel.setName(name);

                                        list.add(dataModel);
                                    }
                                }
                            }
                            applicantIndustryAdapter.notifyDataSetChanged();
                            applicantIndustryAdapter.setFilterArrayListValue(list);
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

        Intent intent = new Intent(Industry.this, ApplicantDepartmentActivity.class);

        intent.putExtra("employeStatus", employeeValue);
        intent.putExtra("bio", typeHereValue);
        intent.putExtra("industry", industryValue);

        startActivity(intent);
    }
}