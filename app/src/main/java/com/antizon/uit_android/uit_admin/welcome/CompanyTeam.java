package com.antizon.uit_android.uit_admin.welcome;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

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
import java.util.HashMap;
import java.util.List;

public class CompanyTeam extends BaseActivity {
    private static final String TAG = CompanyTeam.class.getSimpleName();
    SessionManagement sessionManagement;
    ProgressDialog progressDialog;
    ImageView grayBack,redNoah;
    RecyclerView teamMemberRecyclerView;
    AdminApplicantsAdapter adminApplicantsAdapter;
    private List<ModelAdminApplicants> list;
    private ModelAdminApplicants dataModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_team);
        setIds();
        initialize();
        setListener();
        setUpAdminApplicantsRecyclerview();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");

        companyProfile();
    }
    void setIds() {

        Log.d(TAG, "setIds: ");
        teamMemberRecyclerView = findViewById(R.id.team_member_recyclerview);
        grayBack= findViewById(R.id.grayBack);
        redNoah= findViewById(R.id.redNoah);
    }
    void initialize() {
        Log.d(TAG, "initialize: ");
        progressDialog = new ProgressDialog(this);
        sessionManagement = new SessionManagement(CompanyTeam.this);
        list = new ArrayList<>();
        loadProfile(CompanyTeam.this, sessionManagement.getProfileImage(), redNoah);
    }
    void setListener() {
        Log.d(TAG, "setListener: ");
        grayBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
    void setUpAdminApplicantsRecyclerview() {
        Log.d(TAG, "setUpRecyclerViewLinearLayout: ");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        teamMemberRecyclerView.setLayoutManager(linearLayoutManager);
        adminApplicantsAdapter = new AdminApplicantsAdapter(list, CompanyTeam.this);
        teamMemberRecyclerView.setAdapter(adminApplicantsAdapter);
    }

    void companyProfile() {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("user_id", "126");
        sendServerRequestPOST(AppConstants.GET_PROFILE, params, sessionManagement.getToken());
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
                JSONObject dataObject = jsonObject.getJSONObject("data");
                Log.d(TAG, "onResponse: data: size: " + dataObject.length());

                        int id = dataObject.getInt("id");
                        int user_id = dataObject.getInt("user_id");
                        String name = dataObject.getString("name");
                        String email = dataObject.getString("email");
                        String job_title = dataObject.getString("job_title");
                        String profile_image = dataObject.getString("profile_image");
                        String city = dataObject.getString("city");
                        String state = dataObject.getString("state");
                        String professional_info = dataObject.getString("professional_info");
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

                        list.add(dataModel);


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
}