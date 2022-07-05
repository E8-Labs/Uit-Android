package com.antizon.uit_android.uit_admin.welcome;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
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
import com.antizon.uit_android.generic.adapter.UitAdminApprovedAdapter;
import com.antizon.uit_android.generic.adapter.UitAdminPendingAdapter;
import com.antizon.uit_android.generic.model.ModelUitAdminApproved;
import com.antizon.uit_android.generic.model.ModelUitAdminPending;
import com.antizon.uit_android.generic_utils.AppConstants;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.utilities.Utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;


public class PendingCompaniesActivity extends BaseActivity {

    private static final String TAG = PendingCompaniesActivity.class.getSimpleName();
    ProgressDialog progressDialog;
    SessionManagement sessionManagement;
    TextView pending, approved, paused;
    ImageView profilePicture;

    ConstraintLayout pendingLayout, approvedLayout, pausedLayout;
    RecyclerView pendingCompaniesRecyclerview, approvedCompaniesRecyclerview, pausedCompaniesRecyclerview;

    UitAdminPendingAdapter pendingAdapter;
    private List<ModelUitAdminPending> pendingList;
    private ModelUitAdminPending pendingDataModel;

    UitAdminApprovedAdapter approvedAdapter;
    private List<ModelUitAdminApproved> approvedList;
    private ModelUitAdminApproved approvedDataModel;

    String progressMessage = "", accessToken = "";
    String profile_image = "";
    EditText search;
    Boolean isPendingSelected = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_companies);
        Log.d(TAG, "onCreate: ");
        Utilities.setCustomStatusAndNavColor(PendingCompaniesActivity.this, R.color.white_dash, R.color.white_dash);
        setIds();
        initialize();
        setListener();
        setUpUitAdminPendingRecyclerview();
        setUpUitAdminApprovedRecyclerview();

        getUitAdminPending("");
    }


    void setIds() {
        Log.d(TAG, "setIds: ");

        search = findViewById(R.id.search);
        pending = findViewById(R.id.pending);
        approved = findViewById(R.id.approved);
        paused = findViewById(R.id.paused);
        profilePicture = findViewById(R.id.profilePicture);
        pendingLayout = findViewById(R.id.pendingLayout);
        approvedLayout = findViewById(R.id.approvedLayout);
        pausedLayout = findViewById(R.id.pausedLayout);
        pendingCompaniesRecyclerview = findViewById(R.id.pending_companies_recyclerview);
        approvedCompaniesRecyclerview = findViewById(R.id.approved_companies_recyclerview);
        pausedCompaniesRecyclerview = findViewById(R.id.paused_companies_recyclerview);
    }

    void initialize() {
        Log.d(TAG, "initialize: ");
        progressDialog = new ProgressDialog(PendingCompaniesActivity.this);
        pendingList = new ArrayList<>();
        approvedList = new ArrayList<>();
        sessionManagement = new SessionManagement(PendingCompaniesActivity.this);
        accessToken = sessionManagement.getToken();

    }

    void setListener() {
        Log.d(TAG, "setListener: ");

        pendingLayout.setOnClickListener(view -> {
            Log.d(TAG, "onClick: pending: ");

            setPendingLayout();
            getUitAdminPending("");
        });

        approvedLayout.setOnClickListener(view -> {
            Log.d(TAG, "onClick: approved: ");

            setApprovedLayout();
            getUitAdminApproved("");
        });
        pausedLayout.setOnClickListener(view -> {
            Log.d(TAG, "onClick: approved: ");
            setPausedLayout();
        });

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String searchedString = search.getText().toString().toLowerCase(Locale.getDefault());
                Log.d(TAG, "onTextChanged: " + searchedString);
                if (searchedString.isEmpty()) {


                    if (isPendingSelected) {
                        getUitAdminPending("");
                    } else {
                        getUitAdminApproved("");
                    }
                } else {

                    if (isPendingSelected) {
                        getUitAdminPending(searchedString);
                    } else {
                        getUitAdminApproved(searchedString);
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    void setUpUitAdminPendingRecyclerview() {
        Log.d(TAG, "setUpRecyclerViewLinearLayoutForChat: ");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(PendingCompaniesActivity.this);
        pendingCompaniesRecyclerview.setLayoutManager(linearLayoutManager);

        pendingAdapter = new UitAdminPendingAdapter(pendingList, PendingCompaniesActivity.this,
                new UitAdminPendingAdapter.CompanyStatusInterface() {
                    @Override
                    public void accept(ModelUitAdminPending dataModel) {
                        Log.d(TAG, "accept: Accept Company");
                        progressMessage = "Accepting...";

                        HashMap<String, String> params = new HashMap<>();
                        params.put("company_id", "" + dataModel.getUserId());

                        Log.d(TAG, "getDocumentData: params: " + params);
//                        sendServerRequestPOST(AppConstants.ACCEPT_COMPANY, params, sessionManagement.getUserDetails().get("access_token"));
                        sendServerRequestPOST(AppConstants.ACCEPT_COMPANY, params, accessToken);
                    }

                    @Override
                    public void decline(ModelUitAdminPending dataModel) {
                        Log.d(TAG, "decline: Decline Company");
                        progressMessage = "Declining...";

                        HashMap<String, String> params = new HashMap<>();
                        params.put("company_id", "" + dataModel.getUserId());

                        Log.d(TAG, "getDocumentData: params: " + params);
//                        sendServerRequestPOST(AppConstants.ACCEPT_COMPANY, params, sessionManagement.getUserDetails().get("access_token"));
                        sendServerRequestPOST(AppConstants.REJECT_COMPANY, params, accessToken);
                    }
                });
        pendingCompaniesRecyclerview.setAdapter(pendingAdapter);
    }

    void setUpUitAdminApprovedRecyclerview() {
        Log.d(TAG, "setUpRecyclerViewLinearLayoutForChat: ");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(PendingCompaniesActivity.this);
        approvedCompaniesRecyclerview.setLayoutManager(linearLayoutManager);

        approvedAdapter = new UitAdminApprovedAdapter(approvedList, PendingCompaniesActivity.this);
        approvedCompaniesRecyclerview.setAdapter(approvedAdapter);
    }


    void getUitAdminPending(String search) {
        Log.d(TAG, "pendingCompanies: ");

        isPendingSelected = true;
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        RequestQueue requestQueue = Volley.newRequestQueue(PendingCompaniesActivity.this);

        Log.d(TAG, "getUitAdminPending: url: "+AppConstants.GET_RECRUITER_FIND_COMPANY);
        @SuppressLint("NotifyDataSetChanged") StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConstants.GET_RECRUITER_FIND_COMPANY ,
                response -> {
                    Log.d(TAG, "onResponse: " + response);
                    try {
                        progressDialog.dismiss();
                        pendingList.clear();

                        JSONObject jsonObject = new JSONObject(response);
                        boolean status = jsonObject.getBoolean("status");

                        if (status) {
                            JSONArray dataArray = jsonObject.getJSONArray("data");
                            Log.d(TAG, "onResponse: dataArray: " + dataArray.length());

                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject dataObject = dataArray.getJSONObject(i);

                                int id, userId, active_members;
                                String name = "", email = "", city = "", state = "", profile_image = "";
                                id = dataObject.getInt("id");
                                userId = dataObject.getInt("user_id");
                                name = dataObject.getString("name");
                                email = dataObject.getString("email");
                                city = dataObject.getString("city");
                                state = dataObject.getString("state");
                                profile_image = dataObject.getString("profile_image");
                                active_members = dataObject.getInt("active_members");

                                Log.d(TAG, "onResponse: active_members: " + active_members);

                                pendingDataModel = new ModelUitAdminPending();
                                pendingDataModel.setId(id);
                                pendingDataModel.setUserId(userId);
                                pendingDataModel.setName(name);
                                pendingDataModel.setEmail(email);
                                pendingDataModel.setCity(city);
                                pendingDataModel.setState(state);
                                pendingDataModel.setProfile_image(profile_image);
                                pendingDataModel.setActive_members(active_members);

                                pendingList.add(pendingDataModel);
                            }
                            Log.d(TAG, "onResponse: pendingList: " + pendingList.size());
                            pendingAdapter.notifyDataSetChanged();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d(TAG, "onResponse: JSONException: " + e.getMessage());
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

    void getUitAdminApproved(String search) {
        Log.d(TAG, "pendingCompanies: ");

        isPendingSelected = false;
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        RequestQueue requestQueue = Volley.newRequestQueue(PendingCompaniesActivity.this);

        @SuppressLint("NotifyDataSetChanged")
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConstants.GET_RECRUITER_FIND_COMPANY,
                response -> {
                    try {

                        progressDialog.dismiss();
                        JSONObject jsonObject = new JSONObject(response);

                        boolean status = jsonObject.getBoolean("status");
                        String message = jsonObject.getString("message");
                        if (status) {
                            JSONArray dataArray = jsonObject.getJSONArray("data");
                            Log.d(TAG, "onResponse: 'dataArray: size: " + dataArray.length());
                            if (dataArray.length() > 0) {
                                approvedList.clear();

                                for (int i = 0; i < dataArray.length(); i++) {
                                    JSONObject dataObject = dataArray.getJSONObject(i);

                                    int id, userId, active_members;
                                    String name = "", email = "", city = "", state = "", profile_image = "";

                                    id = dataObject.getInt("id");
                                    userId = dataObject.getInt("user_id");
                                    name = dataObject.getString("name");
                                    email = dataObject.getString("email");
                                    city = dataObject.getString("city");
                                    state = dataObject.getString("state");
                                    profile_image = dataObject.getString("profile_image");
                                    active_members = dataObject.getInt("active_members");
                                    Log.d(TAG, "onResponse: active_members: " + active_members);

                                    approvedDataModel = new ModelUitAdminApproved();
                                    approvedDataModel.setId(id);
                                    approvedDataModel.setUserId(userId);
                                    approvedDataModel.setName(name);
                                    approvedDataModel.setEmail(email);
                                    approvedDataModel.setCity(city);
                                    approvedDataModel.setState(state);
                                    approvedDataModel.setProfile_image(profile_image);
                                    approvedDataModel.setActive_members(active_members);
                                    approvedList.add(approvedDataModel);
                                }
                            }
                        }
                        approvedAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        Log.d(TAG, "onResponse: JSONException: " + e.getMessage());
                        e.printStackTrace();
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

    void setPendingLayout() {
        pendingCompaniesRecyclerview.setVisibility(View.VISIBLE);
        approvedCompaniesRecyclerview.setVisibility(View.GONE);
        pausedCompaniesRecyclerview.setVisibility(View.GONE);

        pendingLayout.setBackgroundResource(R.drawable.app_color_curved_background);
        approvedLayout.setBackgroundResource(R.drawable.white_curved_background);
        pausedLayout.setBackgroundResource(R.drawable.white_curved_background);

        pending.setTextColor(getColor(R.color.white));
        approved.setTextColor(getColor(R.color.black));
        paused.setTextColor(getColor(R.color.black));
    }

    void setApprovedLayout() {
        pendingCompaniesRecyclerview.setVisibility(View.GONE);
        approvedCompaniesRecyclerview.setVisibility(View.VISIBLE);
        pausedCompaniesRecyclerview.setVisibility(View.VISIBLE);
        pendingLayout.setBackgroundResource(R.drawable.white_curved_background);
        approvedLayout.setBackgroundResource(R.drawable.app_color_curved_background);
        pausedLayout.setBackgroundResource(R.drawable.white_curved_background);
        pending.setTextColor(getColor(R.color.black));
        approved.setTextColor(getColor(R.color.white));
        paused.setTextColor(getColor(R.color.black));
    }


    void setPausedLayout() {

        pendingCompaniesRecyclerview.setVisibility(View.GONE);
        approvedCompaniesRecyclerview.setVisibility(View.GONE);
        pausedCompaniesRecyclerview.setVisibility(View.VISIBLE);

        pendingLayout.setBackgroundResource(R.drawable.white_curved_background);
        approvedLayout.setBackgroundResource(R.drawable.white_curved_background);
        pausedLayout.setBackgroundResource(R.drawable.app_color_curved_background);

        pending.setTextColor(getColor(R.color.black));
        approved.setTextColor(getColor(R.color.black));
        paused.setTextColor(getColor(R.color.white));

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

        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(response);
            boolean status = jsonObject.getBoolean("status");
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
