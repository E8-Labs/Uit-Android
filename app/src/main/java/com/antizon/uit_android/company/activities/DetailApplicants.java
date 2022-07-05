package com.antizon.uit_android.company.activities;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.antizon.uit_android.R;
import com.antizon.uit_android.company.utility.BaseActivity;
import com.antizon.uit_android.generic.adapter.AdminApplicantsAdapter;
import com.antizon.uit_android.generic.model.ModelAdminApplicants;
import com.antizon.uit_android.generic_utils.AppConstants;
import com.antizon.uit_android.generic_utils.SessionManagement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DetailApplicants extends BaseActivity {


    private static final String TAG = DetailApplicants.class.getSimpleName();
    ProgressDialog progressDialog;
    SessionManagement sessionManagement;
    TextView pending, approved, paused;
    ConstraintLayout pendingLayout, approvedLayout, pausedLayout;
    RecyclerView recentApplicantsRecyclerview, oldestApplicantsRecyclerview, savedApplicantsRecyclerview;

    AdminApplicantsAdapter adminApplicantsAdapter;
    private List<ModelAdminApplicants> list;
    private ModelAdminApplicants dataModel;

    String progressMessage = "", accessToken = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_applicants);

        setIds();
        initialize();
        setListener();
        setUpRecentApplicantRecyclerview();
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
        Log.d(TAG, "onResume: url: "+ AppConstants.ADMIN_APPLICANTS_LIST);
        Log.d(TAG, "onResume: token: "+sessionManagement.getToken());
        sendServerRequestGET(AppConstants.ADMIN_APPLICANTS_LIST, sessionManagement.getToken());

    }

    void setIds() {
        Log.d(TAG, "setIds: ");
        pending = findViewById(R.id.pending);
        approved = findViewById(R.id.approved);
        paused = findViewById(R.id.paused);
        pendingLayout = findViewById(R.id.pendingLayout);
        approvedLayout = findViewById(R.id.approvedLayout);
        pausedLayout = findViewById(R.id.pausedLayout);
        recentApplicantsRecyclerview = findViewById(R.id.recent_companies_recyclerview);
        oldestApplicantsRecyclerview = findViewById(R.id.oldest_companies_recyclerview);
        savedApplicantsRecyclerview = findViewById(R.id.saved_companies_recyclerview);
    }

    void initialize() {
        Log.d(TAG, "initialize: ");
        pending.setText("Recent");
        approved.setText("Oldest");
        paused.setText("Saved");
        list = new ArrayList<>();
        progressDialog = new ProgressDialog(DetailApplicants.this);
//        pendingList = new ArrayList<>();
//        approvedList = new ArrayList<>();
        sessionManagement = new SessionManagement(DetailApplicants.this);
        accessToken = sessionManagement.getUserDetails().get("access_token");

    }

    void setListener() {
        Log.d(TAG, "setListener: ");

        pendingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: pending: ");

                setPendingLayout();
//                getUitAdminPending("");
            }
        });
        approvedLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: approved: ");

                setApprovedLayout();
//                getUitAdminApproved("");
            }
        });
        pausedLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: approved: ");

                setPausedLayout();
            }
        });
    }
    void setUpRecentApplicantRecyclerview() {
        Log.d(TAG, "setUpRecyclerViewLinearLayout: ");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recentApplicantsRecyclerview.setLayoutManager(linearLayoutManager);
        adminApplicantsAdapter = new AdminApplicantsAdapter(list, DetailApplicants.this);
        recentApplicantsRecyclerview.setAdapter(adminApplicantsAdapter);
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

                        int id = dataObject.getInt("id");
                        int user_id = dataObject.getInt("user_id");
                        String name = dataObject.getString("name");
                        String email = dataObject.getString("email");
                        String job_title = dataObject.getString("job_title");
                        String profile_image = dataObject.getString("profile_image");
                        String city = dataObject.getString("city");
                        String state = dataObject.getString("state");
                        String professional_info = dataObject.getString("professional_info");
                        String industries = dataObject.getString("industries");

                        dataModel = new ModelAdminApplicants();
                        dataModel.setId(id);
                        dataModel.setUser_id(user_id);
                        dataModel.setName(name);
                        dataModel.setEmail(email);
                        dataModel.setJob_title(job_title);
                        dataModel.setProfile_image(profile_image);
                        dataModel.setCity(city);
                        dataModel.setState(state);
                        dataModel.setProfessional_info(professional_info);
                        dataModel.setIndustries(industries);

                        list.add(dataModel);
                    }
                }
            }
            adminApplicantsAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void requestEndedWithError(VolleyError error) {
        super.requestEndedWithError(error);
        progressDialog.dismiss();
        Log.d(TAG, "onErrorResponse: error: " + error);
    }
    void setPendingLayout() {
        recentApplicantsRecyclerview.setVisibility(View.VISIBLE);
        oldestApplicantsRecyclerview.setVisibility(View.GONE);
        savedApplicantsRecyclerview.setVisibility(View.GONE);

        pendingLayout.setBackground(getResources().getDrawable(R.drawable.app_color_curved_background));
        approvedLayout.setBackground(getResources().getDrawable(R.drawable.white_curved_background));
        pausedLayout.setBackground(getResources().getDrawable(R.drawable.white_curved_background));

        pending.setTextColor(getResources().getColor(R.color.white));
        approved.setTextColor(getResources().getColor(R.color.black));
        paused.setTextColor(getResources().getColor(R.color.black));
    }

    void setApprovedLayout() {
        recentApplicantsRecyclerview.setVisibility(View.GONE);
        oldestApplicantsRecyclerview.setVisibility(View.VISIBLE);
        savedApplicantsRecyclerview.setVisibility(View.VISIBLE);
        pendingLayout.setBackground(getResources().getDrawable(R.drawable.white_curved_background));
        approvedLayout.setBackground(getResources().getDrawable(R.drawable.app_color_curved_background));
        pausedLayout.setBackground(getResources().getDrawable(R.drawable.white_curved_background));
        pending.setTextColor(getResources().getColor(R.color.black));
        approved.setTextColor(getResources().getColor(R.color.white));
        paused.setTextColor(getResources().getColor(R.color.black));
    }
    void setPausedLayout() {
        recentApplicantsRecyclerview.setVisibility(View.GONE);
        oldestApplicantsRecyclerview.setVisibility(View.GONE);
        savedApplicantsRecyclerview.setVisibility(View.VISIBLE);

        pendingLayout.setBackground(getResources().getDrawable(R.drawable.white_curved_background));
        approvedLayout.setBackground(getResources().getDrawable(R.drawable.white_curved_background));
        pausedLayout.setBackground(getResources().getDrawable(R.drawable.app_color_curved_background));

        pending.setTextColor(getResources().getColor(R.color.black));
        approved.setTextColor(getResources().getColor(R.color.black));
        paused.setTextColor(getResources().getColor(R.color.white));

    }
}