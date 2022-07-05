package com.antizon.uit_android.recruiter.welcome;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.VolleyError;
import com.antizon.uit_android.R;
import com.antizon.uit_android.company.utility.BaseActivity;
import com.antizon.uit_android.generic.adapter.AdminMembersAdapter;
import com.antizon.uit_android.generic.model.ModelAdminApplicants;
import com.antizon.uit_android.generic_utils.AppConstants;
import com.antizon.uit_android.generic_utils.SessionManagement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TeamMember extends BaseActivity {

    private static final String TAG = TeamMember.class.getSimpleName();

    SessionManagement sessionManagement;
    ProgressDialog progressDialog;
    RecyclerView adminMemberRecyclerView;
    AdminMembersAdapter adminMembersAdapter;
    private List<ModelAdminApplicants> list;
    private ModelAdminApplicants dataModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_member);
        setIds();
        initialize();
        setListener();
        setUpAdminMembersRecyclerview();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");

//        getAdminApplicantsList();
        Log.d(TAG, "onResume: url: "+ AppConstants.ADMIN_APPLICANTS_LIST);
        Log.d(TAG, "onResume: token: "+sessionManagement.getToken());
        sendServerRequestGET(AppConstants.ADMIN_MEMBERS_LIST, sessionManagement.getToken());
    }
    void setIds() {
        Log.d(TAG, "setIds: ");
        adminMemberRecyclerView = findViewById(R.id.admin_members_recyclerView);
    }
    void initialize() {
        Log.d(TAG, "initialize: ");
        progressDialog = new ProgressDialog(this);
        sessionManagement = new SessionManagement(TeamMember.this);
        list = new ArrayList<>();
    }
    void setListener() {
        Log.d(TAG, "setListener: ");

    }
    void setUpAdminMembersRecyclerview() {
        Log.d(TAG, "setUpRecyclerViewLinearLayout: ");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        adminMemberRecyclerView.setLayoutManager(linearLayoutManager);
        adminMembersAdapter = new AdminMembersAdapter(list, TeamMember.this);
        adminMemberRecyclerView.setAdapter(adminMembersAdapter);
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
            adminMembersAdapter.notifyDataSetChanged();
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
}