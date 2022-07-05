package com.antizon.uit_android.generic.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.antizon.uit_android.R;
import com.antizon.uit_android.company.utility.BaseActivity;
import com.antizon.uit_android.generic.adapter.AdminApplicantsAdapter;
import com.antizon.uit_android.generic.adapter.HiredApplicantAdapter;
import com.antizon.uit_android.generic.model.ModelAdminApplicants;
import com.antizon.uit_android.generic.model.ModelAllJobs;
import com.antizon.uit_android.generic.model.ModelUitAdminPending;
import com.antizon.uit_android.generic_utils.AppConstants;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.uit_admin.welcome.CompanyTeam;
import com.antizon.uit_android.uit_admin.welcome.PendingProfile;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AdminJobDetail extends BaseActivity {
    private static final String TAG = AdminJobDetail.class.getSimpleName();

    SessionManagement sessionManagement;
    ProgressDialog progressDialog;
    RecyclerView hiredApplicantsRecyclerView;
    ImageView reminder, backIcon;
    TextView senior, airBnb, sanFrancisco, product, design, dollar, fullTime;
    HiredApplicantAdapter hiredApplicantAdapter;
    private List<ModelAdminApplicants> list;
    private ModelAdminApplicants dataModel;
    ModelAllJobs modelAllJobs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_job_detail);
        setIds();
        initialize();
        setListener();
        getIntentData();
        setUpAdminApplicantsRecyclerview();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");

//        getAdminApplicantsList();
        Log.d(TAG, "onResume: url: " + AppConstants.ADMIN_APPLICANTS_LIST);
        Log.d(TAG, "onResume: token: " + sessionManagement.getToken());
        sendServerRequestGET(AppConstants.ADMIN_APPLICANTS_LIST, sessionManagement.getToken());
    }

    void setIds() {
        Log.d(TAG, "setIds: ");
        hiredApplicantsRecyclerView = findViewById(R.id.hired_applicants_recyclerview);
        reminder = findViewById(R.id.reminder);
        senior = findViewById(R.id.senior);
        airBnb = findViewById(R.id.airBnb);
        sanFrancisco = findViewById(R.id.sanFrancisco);
        product = findViewById(R.id.product);
        design = findViewById(R.id.design);
        dollar = findViewById(R.id.dollar);
        fullTime = findViewById(R.id.fullTime);
        backIcon = findViewById(R.id.backIcon);

    }

    void initialize() {
        Log.d(TAG, "initialize: ");
        progressDialog = new ProgressDialog(this);
        sessionManagement = new SessionManagement(AdminJobDetail.this);
        list = new ArrayList<>();
    }

    void getIntentData() {
        if (getIntent().getExtras().getParcelable("dataModel") != null) {
            modelAllJobs = (ModelAllJobs) getIntent().getExtras().getParcelable("dataModel");

            sanFrancisco.setText(modelAllJobs.getCity() + ", " + modelAllJobs.getState());
            Glide.with(AdminJobDetail.this)
                    .load(modelAllJobs.getUserDataModel().getProfile_image())
                    .into(reminder);
            dollar.setText("$" + modelAllJobs.getMin_salary() + " - $" + modelAllJobs.getMax_salary());



            airBnb.setText( modelAllJobs.getUserDataModel().getName());
            product.setText("By " +modelAllJobs.getIndustryDataModel().getName());
//            airBnb.setText(modelAllJobs.getJob_title());
            design.setText(modelAllJobs.getJob_title());


        }

    }

    void setListener() {
        Log.d(TAG, "setListener: ");

        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    void setUpAdminApplicantsRecyclerview() {
        Log.d(TAG, "setUpRecyclerViewLinearLayout: ");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        hiredApplicantsRecyclerView.setLayoutManager(linearLayoutManager);
        hiredApplicantAdapter = new HiredApplicantAdapter(list, AdminJobDetail.this);
        hiredApplicantsRecyclerView.setAdapter(hiredApplicantAdapter);
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
            hiredApplicantAdapter.notifyDataSetChanged();
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