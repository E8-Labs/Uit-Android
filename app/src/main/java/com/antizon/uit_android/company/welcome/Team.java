package com.antizon.uit_android.company.welcome;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class Team extends BaseActivity {
    private static final String TAG = Team.class.getSimpleName();
    ProgressDialog progressDialog;
    SessionManagement sessionManagement;
    TextView pending, approved, paused;
    ConstraintLayout pendingLayout, approvedLayout, pausedLayout;
    RecyclerView accepted_companies_recyclerview, pending_companies_recyclerview, invited_companies_recyclerview;

    UitAdminApprovedAdapter companyTeamAdapter;
    private List<ModelUitAdminApproved> approvedList;
    private ModelUitAdminApproved approvedDataModel;

    UitAdminPendingAdapter pendingAdapter;
    private List<ModelUitAdminPending> pendingList;
    private ModelUitAdminPending pendingDataModel;

    String progressMessage = "", accessToken = "";
    Boolean isAcceptedSelected = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);

        setIds();
        initialize();
        setListener();

        setUpAcceptedMemberRecyclerview();
        setUpPendingMemberRecyclerview();
        setUpInvitedMemberRecyclerview();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d(TAG, "onResume: ");
        Log.d(TAG, "onResume: url: " + AppConstants.COMPANY_TEAM_MEMBERS);
        Log.d(TAG, "onResume: token: " + sessionManagement.getToken());
        sendServerRequestGET(AppConstants.COMPANY_TEAM_MEMBERS, sessionManagement.getToken());

    }

    void setIds() {
        Log.d(TAG, "setIds: ");

        pending = findViewById(R.id.pending);
        approved = findViewById(R.id.approved);
        paused = findViewById(R.id.paused);
        pendingLayout = findViewById(R.id.pendingLayout);
        approvedLayout = findViewById(R.id.approvedLayout);
        pausedLayout = findViewById(R.id.pausedLayout);
        accepted_companies_recyclerview = findViewById(R.id.accepted_companies_recyclerview);
        pending_companies_recyclerview = findViewById(R.id.pending_companies_recyclerview);
        invited_companies_recyclerview = findViewById(R.id.invited_companies_recyclerview);
    }

    void initialize() {
        Log.d(TAG, "initialize: ");

        progressDialog = new ProgressDialog(Team.this);
        pending.setText("Accepted");
        approved.setText("Pending");
        paused.setText("Invited");
        approvedList = new ArrayList<>();
        pendingList = new ArrayList<>();
        sessionManagement = new SessionManagement(Team.this);
        accessToken = sessionManagement.getUserDetails().get("access_token");
    }

    void setListener() {
        Log.d(TAG, "setListener: ");
        pendingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: pending: ");

                setAcceptedLayout();
                getUitAdminAccepted();
            }
        });

        approvedLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: approved: ");

                setPendingLayout();
                sendServerRequestGET(AppConstants.COMPANY_TEAM_MEMBERS, sessionManagement.getToken());
                getUitAdminPending();

            }
        });

        pausedLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: approved: ");

                setInvitedLayout();
                sendServerRequestGET(AppConstants.INVITED_MEMBERS, sessionManagement.getToken());
                getInvitedMember();


            }
        });

    }


    void setUpPendingMemberRecyclerview() {
        Log.d(TAG, "setUpRecyclerViewLinearLayoutForChat: ");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Team.this);
        pending_companies_recyclerview.setLayoutManager(linearLayoutManager);

        pendingAdapter = new UitAdminPendingAdapter(pendingList, Team.this,
                new UitAdminPendingAdapter.CompanyStatusInterface() {
                    @Override
                    public void accept(ModelUitAdminPending dataModel) {
                        Log.d(TAG, "accept: Accept Company Members");
                        progressMessage = "Accepting...";

                        HashMap<String, String> params = new HashMap<String, String>();
                        params.put("team_id", "" + dataModel.getUserId());

                        Log.d(TAG, "getDocumentData: params: " + params);
//                        sendServerRequestPOST(AppConstants.ACCEPT_COMPANY, params, sessionManagement.getUserDetails().get("access_token"));
                        sendServerRequestPOST(AppConstants.APPROVE_COMPANY_MEMBER, params, accessToken);
                    }

                    @Override
                    public void decline(ModelUitAdminPending dataModel) {
                        Log.d(TAG, "decline: Decline Company Members");
                        progressMessage = "Declining...";

                        HashMap<String, String> params = new HashMap<String, String>();
                        params.put("team_id", "" + dataModel.getUserId());

                        Log.d(TAG, "getDocumentData: params: " + params);
//                        sendServerRequestPOST(AppConstants.ACCEPT_COMPANY, params, sessionManagement.getUserDetails().get("access_token"));
                        sendServerRequestPOST(AppConstants.REJECT_COMPANY_MEMBER, params, accessToken);
                    }
                });
        pending_companies_recyclerview.setAdapter(pendingAdapter);
    }

    void setUpAcceptedMemberRecyclerview() {
        Log.d(TAG, "setUpRecyclerViewLinearLayoutForChat: ");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Team.this);
        accepted_companies_recyclerview.setLayoutManager(linearLayoutManager);
        companyTeamAdapter = new UitAdminApprovedAdapter(approvedList, Team.this);
        accepted_companies_recyclerview.setAdapter(companyTeamAdapter);
    }

    void setUpInvitedMemberRecyclerview() {
        Log.d(TAG, "setUpRecyclerViewLinearLayoutForChat: ");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Team.this);
        invited_companies_recyclerview.setLayoutManager(linearLayoutManager);
        companyTeamAdapter = new UitAdminApprovedAdapter(approvedList, Team.this);
        invited_companies_recyclerview.setAdapter(companyTeamAdapter);
    }


    void getUitAdminPending() {
        Log.d(TAG, "pendingCompanies: ");

        isAcceptedSelected = false;
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        RequestQueue requestQueue = Volley.newRequestQueue(Team.this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConstants.COMPANY_TEAM_MEMBERS,
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
                                    Log.d(TAG, "onResponse: " + dataObject);


//                                    pendingDataModel = new ModelUitAdminPending();
//                                    pendingDataModel.setId(id);
//

                                    pendingList.add(pendingDataModel);
                                }
                                Log.d(TAG, "onResponse: pendingList: " + pendingList.size());
                                pendingAdapter.notifyDataSetChanged();
                            }

                        }
                        catch (JSONException e) {
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


    void getUitAdminAccepted() {
        Log.d(TAG, "acceptedCompanies: ");

        isAcceptedSelected = false;
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        RequestQueue requestQueue = Volley.newRequestQueue(Team.this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConstants.COMPANY_TEAM_MEMBERS,
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
                                        Log.d(TAG, "onResponse: " + dataObject);

//                                        approvedDataModel = new ModelUitAdminApproved();
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
                            companyTeamAdapter.notifyDataSetChanged();
                        }
                        catch (JSONException e) {
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


    void getInvitedMember() {
        Log.d(TAG, "acceptedCompanies: ");

        isAcceptedSelected = false;
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        RequestQueue requestQueue = Volley.newRequestQueue(Team.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConstants.INVITED_MEMBERS,
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
                                        Log.d(TAG, "onResponse: " + dataObject);

//                                        approvedDataModel = new ModelUitAdminApproved();
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
                            companyTeamAdapter.notifyDataSetChanged();
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


    @Override
    public void requestStarted() {
        super.requestStarted();
        Log.d(TAG, "requestStarted: running");
        progressDialog.setMessage(" register company member...");
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

    void setAcceptedLayout() {
        accepted_companies_recyclerview.setVisibility(View.VISIBLE);
        pending_companies_recyclerview.setVisibility(View.GONE);
        invited_companies_recyclerview.setVisibility(View.GONE);

        pendingLayout.setBackground(getResources().getDrawable(R.drawable.app_color_curved_background));
        approvedLayout.setBackground(getResources().getDrawable(R.drawable.white_curved_background));
        pausedLayout.setBackground(getResources().getDrawable(R.drawable.white_curved_background));

        pending.setTextColor(getResources().getColor(R.color.white));
        approved.setTextColor(getResources().getColor(R.color.black));
        paused.setTextColor(getResources().getColor(R.color.black));
    }

    void setPendingLayout() {
        accepted_companies_recyclerview.setVisibility(View.GONE);
        pending_companies_recyclerview.setVisibility(View.VISIBLE);
        invited_companies_recyclerview.setVisibility(View.VISIBLE);
        pendingLayout.setBackground(getResources().getDrawable(R.drawable.white_curved_background));
        approvedLayout.setBackground(getResources().getDrawable(R.drawable.app_color_curved_background));
        pausedLayout.setBackground(getResources().getDrawable(R.drawable.white_curved_background));
        pending.setTextColor(getResources().getColor(R.color.black));
        approved.setTextColor(getResources().getColor(R.color.white));
        paused.setTextColor(getResources().getColor(R.color.black));
    }


    void setInvitedLayout() {

        accepted_companies_recyclerview.setVisibility(View.GONE);
        pending_companies_recyclerview.setVisibility(View.GONE);
        invited_companies_recyclerview.setVisibility(View.VISIBLE);

        pendingLayout.setBackground(getResources().getDrawable(R.drawable.white_curved_background));
        approvedLayout.setBackground(getResources().getDrawable(R.drawable.white_curved_background));
        pausedLayout.setBackground(getResources().getDrawable(R.drawable.app_color_curved_background));

        pending.setTextColor(getResources().getColor(R.color.black));
        approved.setTextColor(getResources().getColor(R.color.black));
        paused.setTextColor(getResources().getColor(R.color.white));

    }


}