package com.antizon.uit_android.recruiter.welcome;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
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
import com.antizon.uit_android.generic.adapter.RecruiterFindCompanyAdapter;
import com.antizon.uit_android.generic.model.ModelApplicantDepartment;
import com.antizon.uit_android.generic.model.ModelRecruiterFindCompany;
import com.antizon.uit_android.generic_utils.AppConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class FindCompany extends BaseActivity {
    private static final String TAG = FindCompany.class.getSimpleName();
    ProgressDialog progressDialog;
    RecyclerView applicantFindCompanyRecyclerview;
    RecruiterFindCompanyAdapter driverAdapter;
    private List<ModelRecruiterFindCompany> list;
    private ModelRecruiterFindCompany dataModel;
    EditText searchOffer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_company);

        setIds();
        initialize();
        setListener();
        setUpFindCompanyRecyclerview();
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");

        getApplicantFindCompany();
    }

    void setIds() {
        Log.d(TAG, "setIds: ");
        searchOffer = findViewById(R.id.searchOffer);

        applicantFindCompanyRecyclerview = findViewById(R.id.recruiter_find_company_recyclerview);

    }

    void initialize() {
        Log.d(TAG, "initialize: ");
        progressDialog = new ProgressDialog(FindCompany.this);
        list = new ArrayList<>();


    }

    void setListener() {
        Log.d(TAG, "setListener: ");


        searchOffer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String searchedString = searchOffer.getText().toString().toLowerCase(Locale.getDefault());
                driverAdapter.search(searchedString);
                Log.d(TAG, "onTextChanged: " + searchedString);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    void setUpFindCompanyRecyclerview() {
        Log.d(TAG, "setUpRecyclerViewLinearLayoutForChat: ");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(FindCompany.this);
        applicantFindCompanyRecyclerview.setLayoutManager(linearLayoutManager);

        driverAdapter = new RecruiterFindCompanyAdapter(list, FindCompany.this);
        applicantFindCompanyRecyclerview.setAdapter(driverAdapter);
    }

    void getApplicantFindCompany() {
        Log.d(TAG, "getCompanySize: ");
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        RequestQueue requestQueue = Volley.newRequestQueue(FindCompany.this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConstants.GET_RECRUITER_FIND_COMPANY,
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

                                        JSONArray industriesArray = dataObject.getJSONArray("industries");
                                        Log.d(TAG, "onResponse: industries: size: " + industriesArray.length());

                                        List<ModelApplicantDepartment> modelApplicantDepartmentList = new ArrayList<>();
                                        for (int j = 0; j < industriesArray.length(); j++) {
                                            JSONObject jsonObject1 = industriesArray.getJSONObject(j);


                                            ModelApplicantDepartment modelApplicantDepartment = new ModelApplicantDepartment();
                                            modelApplicantDepartment.setId(jsonObject1.getInt("id"));
                                            modelApplicantDepartment.setName(jsonObject1.getString("name"));
                                            modelApplicantDepartmentList.add(modelApplicantDepartment);

                                        }


                                        int id;
                                        String name = "", email, profileImage;
                                        id = dataObject.getInt("id");
                                        name = dataObject.getString("name");
                                        email = dataObject.getString("email");
                                        profileImage = dataObject.getString("profile_image");
                                        dataModel = new ModelRecruiterFindCompany();
                                        dataModel.setId(id);
                                        dataModel.setName(name);
                                        dataModel.setEmail(email);
                                        dataModel.setProfileImage(profileImage);
//                                        dataModel.setIndustryModel(modelApplicantDepartmentList);

                                        list.add(dataModel);
                                    }


                                }
                            }
                            driverAdapter.notifyDataSetChanged();
                            driverAdapter.setFilterArrayListValue(list);
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

}