package com.antizon.uit_android.uit_admin.welcome;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.antizon.uit_android.R;
import com.antizon.uit_android.company.utility.BaseActivity;
import com.antizon.uit_android.generic.adapter.UitAdminApprovedAdapter;
import com.antizon.uit_android.generic.adapter.UitAdminPendingAdapter;
import com.antizon.uit_android.generic.model.ModelUitAdminApproved;
import com.antizon.uit_android.generic.model.ModelUitAdminPending;
import com.antizon.uit_android.generic_utils.AppConstants;
import com.antizon.uit_android.generic_utils.SessionManagement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class UitMembers extends BaseActivity {


    private static final String TAG = UitMembers.class.getSimpleName();
    ProgressDialog progressDialog;
    SessionManagement sessionManagement;
    TextView pending, approved;
    EditText searchOffer;
    ConstraintLayout pendingLayout, approvedLayout;
    RecyclerView pendingMemberRecyclerview, approvedMemberRecyclerview;


    UitAdminPendingAdapter pendingAdapter;
    private List<ModelUitAdminPending> pendingList;
    private ModelUitAdminPending pendingDataModel;

    UitAdminApprovedAdapter approvedAdapter;
    private List<ModelUitAdminApproved> approvedList;
    private ModelUitAdminApproved approvedDataModel;

    String progressMessage = "", accessToken = "";
    EditText search;
    Boolean isPendingSelected = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uit_members);

        setIds();
        initialize();
        setListener();
        setUpUitAdminPendingRecyclerview();
        setUpUitAdminApprovedRecyclerview();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");

        getUitAdminPending("");
    }

    void setIds() {
        Log.d(TAG, "setIds: ");

        searchOffer = findViewById(R.id.searchOffer);
        pending = findViewById(R.id.pending);
        approved = findViewById(R.id.approved);
        pendingLayout = findViewById(R.id.pendingLayout);
        approvedLayout = findViewById(R.id.approvedLayout);
        search=findViewById(R.id.search);

        pendingMemberRecyclerview = findViewById(R.id.pending_member_recyclerview);
        approvedMemberRecyclerview = findViewById(R.id.approved_member_recyclerview);

    }

    void initialize() {
        Log.d(TAG, "initialize: ");
        progressDialog = new ProgressDialog(UitMembers.this);
        pendingList = new ArrayList<>();
        approvedList = new ArrayList<>();
        sessionManagement = new SessionManagement(UitMembers.this);
        accessToken = sessionManagement.getUserDetails().get("access_token");
        //accessToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOlwvXC96b3Jyb2FwcC50ZWNoXC91aXRcL2FwaVwvbG9naW4iLCJpYXQiOjE2NDU3OTg1ODcsImV4cCI6MTY3NjkwMjU4NywibmJmIjoxNjQ1Nzk4NTg3LCJqdGkiOiJnZFJOVkJPb1lpOUdwcjJaIiwic3ViIjo1NSwicHJ2IjoiMjNiZDVjODk0OWY2MDBhZGIzOWU3MDFjNDAwODcyZGI3YTU5NzZmNyJ9.4Faf58wc_G6xdhHaqThDTWn3trLzXpkOtpSOH0oTUuA";
    }

    void setListener() {
        Log.d(TAG, "setListener: ");

        pendingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: pending: ");

                setPendingLayout();
                getUitAdminPending("");
            }
        });

        approvedLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: approved: ");

                setApprovedLayout();
                getUitAdminApproved("");
            }
        });
    }
    void setUpUitAdminPendingRecyclerview() {
        Log.d(TAG, "setUpRecyclerViewLinearLayoutForChat: ");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(UitMembers.this);
        pendingMemberRecyclerview.setLayoutManager(linearLayoutManager);

        pendingAdapter = new UitAdminPendingAdapter(pendingList, UitMembers.this,
                new UitAdminPendingAdapter.CompanyStatusInterface() {
                    @Override
                    public void accept(ModelUitAdminPending dataModel) {
                        Log.d(TAG, "accept: Accept Company Member");
                        progressMessage = "Accepting...";

                        HashMap<String, String> params = new HashMap<String, String>();
                        params.put("team_id", "12");

                        Log.d(TAG, "getDocumentData: params: " + params);
//                        sendServerRequestPOST(AppConstants.ACCEPT_COMPANY, params, sessionManagement.getUserDetails().get("access_token"));
                        sendServerRequestPOST(AppConstants.APPROVE_COMPANY_MEMBER, params, accessToken);
                    }

                    @Override
                    public void decline(ModelUitAdminPending dataModel) {
                        Log.d(TAG, "decline: Decline Company Member");
                        progressMessage = "Declining...";

                        HashMap<String, String> params = new HashMap<String, String>();
                        params.put("team_id", "12");

                        Log.d(TAG, "getDocumentData: params: " + params);
//                        sendServerRequestPOST(AppConstants.ACCEPT_COMPANY, params, sessionManagement.getUserDetails().get("access_token"));
                        sendServerRequestPOST(AppConstants.REJECT_COMPANY_MEMBER, params, accessToken);
                    }
                });
        pendingMemberRecyclerview.setAdapter(pendingAdapter);
    }

    void setUpUitAdminApprovedRecyclerview() {
        Log.d(TAG, "setUpRecyclerViewLinearLayoutForChat: ");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(UitMembers.this);
        approvedMemberRecyclerview.setLayoutManager(linearLayoutManager);

        approvedAdapter = new UitAdminApprovedAdapter(approvedList, UitMembers.this);
        approvedMemberRecyclerview.setAdapter(approvedAdapter);
    }


    void getUitAdminPending(String search) {
        Log.d(TAG, "pendingCompanies: ");

        isPendingSelected = true;
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        RequestQueue requestQueue = Volley.newRequestQueue(UitMembers.this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConstants.COMPANY_TEAM_MEMBERS+"off_set=0"+"account_status=1",
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse: " + response);
                        try {
                            progressDialog.dismiss();
                            pendingList.clear();

                            JSONObject jsonObject = new JSONObject(response);
                            Boolean status = jsonObject.getBoolean("status");
                            String message = jsonObject.getString("message");

                            if (status) {
                                JSONArray dataArray = jsonObject.getJSONArray("data");
                                Log.d(TAG, "onResponse: dataArray: " + dataArray.length());

                                for (int i = 0; i < dataArray.length(); i++) {
                                    JSONObject dataObject = dataArray.getJSONObject(i);
                                    Log.d(TAG, "onResponse: "+dataObject);


//                                    pendingDataModel = new ModelUitAdminPending();
//                                    pendingDataModel.setId(id);
//

                                    pendingList.add(pendingDataModel);
                                }
                                Log.d(TAG, "onResponse: pendingList: " + pendingList.size());
                                pendingAdapter.notifyDataSetChanged();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d(TAG, "onResponse: JSONException: " + e.getMessage());
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


    void getUitAdminApproved(String search) {
        Log.d(TAG, "AllCompanies: ");

        isPendingSelected = false;
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        RequestQueue requestQueue = Volley.newRequestQueue(UitMembers.this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConstants.COMPANY_TEAM_MEMBERS+"off_set=0"+"account_status=2",
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Log.d(TAG, "onResponse: " + response);
                        try {

                            progressDialog.dismiss();
                            JSONObject jsonObject = new JSONObject(response);

                            Boolean status = jsonObject.getBoolean("status");
                            String message = jsonObject.getString("message");

                            if (status) {
                                JSONArray dataArray = jsonObject.getJSONArray("data");
                                Log.d(TAG, "onResponse: 'dataArray: size: " + dataArray.length());
                                if (dataArray.length() > 0) {
                                    approvedList.clear();

                                    for (int i = 0; i < dataArray.length(); i++) {
                                        JSONObject dataObject = dataArray.getJSONObject(i);
                                        Log.d(TAG, "onResponse: "+dataObject);

//                                        acc = new ModelUitAdminApproved();
//                                        approvedDataModel.setId(id);
//                                        approvedDataModel.setUserId(userId);
//                                        approvedDataModel.setName(name);
//                                        approvedDataModel.setEmail(email);
//                                        approvedDataModel.setCity(city);
//                                        approvedDataModel.setState(state);
//                                        approvedDataModel.setProfile_image(profile_image);
//                                        approvedDataModel.setActive_members(active_members);
                                        approvedList.add(approvedDataModel);
                                    }
                                }
                            }
//                            Log.d(TAG, "onResponse: list: size: " + approvedList.size());
                            approvedAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            Log.d(TAG, "onResponse: JSONException: " + e.getMessage());
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

    void setPendingLayout() {
        pendingMemberRecyclerview.setVisibility(View.GONE);
        approvedMemberRecyclerview.setVisibility(View.VISIBLE);

        pendingLayout.setBackground(getResources().getDrawable(R.drawable.app_color_curved_background));
        approvedLayout.setBackground(getResources().getDrawable(R.drawable.white_curved_background));
        pending.setTextColor(getResources().getColor(R.color.white));
        approved.setTextColor(getResources().getColor(R.color.black));

    }

    void setApprovedLayout() {
        pendingMemberRecyclerview.setVisibility(View.VISIBLE);
        approvedMemberRecyclerview.setVisibility(View.GONE);
        pendingLayout.setBackground(getResources().getDrawable(R.drawable.white_curved_background));
        approvedLayout.setBackground(getResources().getDrawable(R.drawable.app_color_curved_background));

        pending.setTextColor(getResources().getColor(R.color.black));
        approved.setTextColor(getResources().getColor(R.color.white));

    }


    @Override
    public void requestStarted() {
        super.requestStarted();
        Log.d(TAG, "requestStarted: ");
        progressDialog.setMessage(progressMessage);
        progressDialog.show();
    }

    @Override
    public void onResponseReceived(String response, String urlCalled) {
        super.onResponseReceived(response, urlCalled);
        progressDialog.dismiss();
        Log.d(TAG, "onResponseReceived: response: " + response);

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(response);
            Boolean status = jsonObject.getBoolean("status");
            if (status) {
//                Toast.makeText(this, "Done.", Toast.LENGTH_SHORT).show();
                getUitAdminPending("");
            } else {
                Toast.makeText(this, "Failed, please try again.", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            Log.d(TAG, "onResponseReceived: JSONException: " + e.getMessage());
        }
    }

    @Override
    public void requestEndedWithError(VolleyError error) {
        super.requestEndedWithError(error);
        progressDialog.dismiss();
        Log.d(TAG, "requestEndedWithError: error: " + error);
    }
}