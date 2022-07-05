package com.antizon.uit_android.uit_admin.welcome;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.antizon.uit_android.R;
import com.antizon.uit_android.company.utility.BaseActivity;
import com.antizon.uit_android.generic.adapter.AdminMembersAdapter;
import com.antizon.uit_android.generic.model.ModelAdminApplicants;
import com.antizon.uit_android.generic_utils.AppConstants;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.utilities.Utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AdminMembersActivity extends BaseActivity {
    private static final String TAG = AdminMembersActivity.class.getSimpleName();


    EditText searchOffer;
    SessionManagement sessionManagement;
    ProgressDialog progressDialog;
    TextView welcome;
    ImageView profilePicture;
    RecyclerView adminMemberRecyclerView;
    AdminMembersAdapter adminMembersAdapter;
    List<ModelAdminApplicants> list;
    ModelAdminApplicants dataModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_members);
        Utilities.setCustomStatusAndNavColor(AdminMembersActivity.this, R.color.white_dash, R.color.white_dash);
        setIds();
        initialize();
        setListener();
        setUpAdminMembersRecyclerview();

        sendServerRequestGET(AppConstants.ADMIN_MEMBERS_LIST, sessionManagement.getToken());
    }

    void setIds() {
        adminMemberRecyclerView = findViewById(R.id.admin_members_recyclerView);
        searchOffer = findViewById(R.id.searchOffer);
        welcome = findViewById(R.id.welcome);
        profilePicture = findViewById(R.id.profilePicture);
    }
    void initialize() {
        progressDialog = new ProgressDialog(this);
        sessionManagement = new SessionManagement(AdminMembersActivity.this);
        list = new ArrayList<>();
        loadProfile(AdminMembersActivity.this, sessionManagement.getProfileImage(), profilePicture);
    }
    void setListener() {
        Log.d(TAG, "setListener: ");

        welcome.setOnClickListener(view -> {
            Intent intent = new Intent(AdminMembersActivity.this, FlaggedApplicantsActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
        });

        searchOffer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String searchedString = searchOffer.getText().toString().toLowerCase(Locale.getDefault());
                adminMembersAdapter.search(searchedString);
                Log.d(TAG, "onTextChanged: " + searchedString);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
    void setUpAdminMembersRecyclerview() {
        Log.d(TAG, "setUpRecyclerViewLinearLayout: ");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        adminMemberRecyclerView.setLayoutManager(linearLayoutManager);
        adminMembersAdapter = new AdminMembersAdapter(list, AdminMembersActivity.this);
        adminMemberRecyclerView.setAdapter(adminMembersAdapter);
    }

    @Override
    public void requestStarted() {
        super.requestStarted();
        progressDialog.setMessage("Loading...");
        progressDialog.show();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onResponseReceived(String response, String urlCalled) {
        super.onResponseReceived(response, urlCalled);

        Log.d(TAG, "onResponse: " + response);
        try {

            progressDialog.dismiss();
            JSONObject jsonObject = new JSONObject(response);

            boolean status = jsonObject.getBoolean("status");
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
            adminMembersAdapter.setFilterArrayListValue(list);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }
}