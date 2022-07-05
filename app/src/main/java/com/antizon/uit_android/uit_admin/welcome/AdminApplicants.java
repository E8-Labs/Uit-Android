package com.antizon.uit_android.uit_admin.welcome;

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
import java.util.Locale;

public class AdminApplicants extends BaseActivity {
    private static final String TAG = AdminApplicants.class.getSimpleName();
    TextView welcome;
    EditText searchOffer;

    SessionManagement sessionManagement;
    ProgressDialog progressDialog;
    ImageView profilePicture;
    RecyclerView adminApplicantRecyclerView;
    AdminApplicantsAdapter adminApplicantsAdapter;
    private List<ModelAdminApplicants> list;
    private ModelAdminApplicants dataModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_aplicants);
        setIds();
        initialize();
        setListener();
        setUpAdminApplicantsRecyclerview();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");

//        getAdminApplicantsList();
        Log.d(TAG, "onResume: url: "+AppConstants.ADMIN_APPLICANTS_LIST);
        Log.d(TAG, "onResume: token: "+sessionManagement.getToken());
        sendServerRequestGET(AppConstants.ADMIN_APPLICANTS_LIST, sessionManagement.getToken());
    }
    void setIds() {
        Log.d(TAG, "setIds: ");
        adminApplicantRecyclerView = findViewById(R.id.admin_applicant_recyclerView);
        welcome = findViewById(R.id.welcome);
        searchOffer = findViewById(R.id.searchOffer);
        profilePicture = findViewById(R.id.profilePicture);
    }
    void initialize() {
        Log.d(TAG, "initialize: ");
        progressDialog = new ProgressDialog(this);
        sessionManagement = new SessionManagement(AdminApplicants.this);
        list = new ArrayList<>();
        loadProfile(AdminApplicants.this, sessionManagement.getProfileImage(), profilePicture);
    }
    void setListener() {
        Log.d(TAG, "setListener: ");
        welcome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminApplicants.this, FlaggedApplicantsActivity.class);
                startActivity(intent);
            }
        });
        searchOffer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String searchedString = searchOffer.getText().toString().toLowerCase(Locale.getDefault());
                adminApplicantsAdapter.search(searchedString);
                Log.d(TAG, "onTextChanged: " + searchedString);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
    void setUpAdminApplicantsRecyclerview() {
        Log.d(TAG, "setUpRecyclerViewLinearLayout: ");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        adminApplicantRecyclerView.setLayoutManager(linearLayoutManager);
        adminApplicantsAdapter = new AdminApplicantsAdapter(list, AdminApplicants.this);
        adminApplicantRecyclerView.setAdapter(adminApplicantsAdapter);
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

//                        JSONObject professional_info = dataObject.getJSONObject("professional_info");

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
            adminApplicantsAdapter.setFilterArrayListValue(list);
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