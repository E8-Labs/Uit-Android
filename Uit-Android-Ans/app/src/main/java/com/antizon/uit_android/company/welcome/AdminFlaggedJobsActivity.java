package com.antizon.uit_android.company.welcome;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.android.volley.VolleyError;
import com.antizon.uit_android.R;
import com.antizon.uit_android.company.utility.BaseActivity;
import com.antizon.uit_android.generic.adapter.FlaggedJobAdapter;
import com.antizon.uit_android.generic.model.ModelFlaggedJob;
import com.antizon.uit_android.generic_utils.AppConstants;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.utilities.Utilities;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class AdminFlaggedJobsActivity extends BaseActivity {

    Context context;

    SessionManagement sessionManagement;
    ProgressDialog progressDialog;
    RecyclerView flaggedJobRecyclerView;
    ImageView backIcon;
    EditText searchOffer;
    RelativeLayout layout_noJobs;

    FlaggedJobAdapter flaggedJobAdapter;
    List<ModelFlaggedJob> flaggedJobList;
    ModelFlaggedJob dataModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flagged_jobs);
        Utilities.setCustomStatusAndNavColor(AdminFlaggedJobsActivity.this, R.color.white_dash, R.color.white_dash);
        context = AdminFlaggedJobsActivity.this;
        progressDialog = new ProgressDialog(context);
        sessionManagement = new SessionManagement(context);

        initViews();

        sendServerRequestGET(AppConstants.GET_FLAGGED_JOB, sessionManagement.getToken());
    }


    private void initViews() {
        flaggedJobRecyclerView = findViewById(R.id.flagged_job_recyclerView);
        backIcon = findViewById(R.id.backIcon);
        searchOffer = findViewById(R.id.searchOffer);
        layout_noJobs = findViewById(R.id.layout_noJobs);

        backIcon.setOnClickListener(view -> onBackPressed());

        searchOffer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });
    }

    private void filter(String text) {
        ArrayList<ModelFlaggedJob> filteredList = new ArrayList<>();
        for (ModelFlaggedJob item : flaggedJobList) {
            if (item.getJob_title().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        if (filteredList.size() == 0){
            flaggedJobRecyclerView.setVisibility(View.GONE);
            layout_noJobs.setVisibility(View.VISIBLE);
        }else {
            flaggedJobRecyclerView.setVisibility(View.VISIBLE);
            layout_noJobs.setVisibility(View.GONE);
            flaggedJobAdapter.filterList(filteredList);
        }

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
        try {

            progressDialog.dismiss();
            JSONObject jsonObject = new JSONObject(response);

            boolean status = jsonObject.getBoolean("status");
            String message = jsonObject.getString("message");
            if (status) {
                JSONArray dataArray = jsonObject.getJSONArray("data");
                if (dataArray.length() > 0) {
                    flaggedJobList = new ArrayList<>();
                    for (int i = 0; i < dataArray.length(); i++) {

                        JSONObject dataObject = dataArray.getJSONObject(i);
                        int id, user_id, location_status, employment_type, job_status, min_salary, max_salary, total_applications, role;
                        String name, city, profile_image, job_title, state,
                                created_at, reason_to_close, greenhouse_access_token, comment;

                        comment = dataObject.getString("comment");
                        created_at = dataObject.getString("created_at");

                        dataModel = new ModelFlaggedJob();
                        dataModel.setComment(comment);
                        dataModel.setCreated_at(created_at);

                        flaggedJobList.add(dataModel);

                        JSONObject jobObject = dataObject.getJSONObject("job");

                        id = jobObject.getInt("id");
                        job_title = jobObject.getString("job_title");

                        city = jobObject.getString("city");
                        reason_to_close = jobObject.getString("reason_to_close");
                        state = jobObject.getString("state");
                        location_status = jobObject.getInt("location_status");
                        employment_type = jobObject.getInt("employment_type");
                        job_status = jobObject.getInt("job_status");
                        min_salary = jobObject.getInt("min_salary");
                        max_salary = jobObject.getInt("max_salary");
                        total_applications = jobObject.getInt("total_applications");


                        dataModel.setId(id);
                        dataModel.setJob_title(job_title);
                        dataModel.setCity(city);
                        dataModel.setReason_to_close(reason_to_close);
                        dataModel.setState(state);
                        dataModel.setLocation_status(location_status);
                        dataModel.setEmployment_type(employment_type);
                        dataModel.setJob_status(job_status);
                        dataModel.setMin_salary(min_salary);
                        dataModel.setMax_salary(max_salary);
                        dataModel.setTotal_applications(total_applications);


                        JSONObject industryObject = jobObject.getJSONObject("industry");
                        id = industryObject.getInt("id");
                        name = industryObject.getString("name");
                        dataModel.setId(id);
                        dataModel.setName(name);


                        JSONObject userObject = jobObject.getJSONObject("user");
                        id = userObject.getInt("id");
                        name = userObject.getString("name");
                        role = userObject.getInt("role");
                        user_id = userObject.getInt("user_id");
                        profile_image = userObject.getString("profile_image");
                        job_title = userObject.getString("job_title");
                        greenhouse_access_token = userObject.getString("greenhouse_access_token");

                        dataModel.setUser_id(user_id);
                        dataModel.setRole(role);
                        dataModel.setName(name);
                        dataModel.setGreenhouse_access_token(greenhouse_access_token);
                        dataModel.setProfile_image(profile_image);
                        dataModel.setJob_title(job_title);
                        dataModel.setId(id);

                        JSONObject companyObject = jobObject.getJSONObject("company");
                        id = companyObject.getInt("id");
                        name = companyObject.getString("name");
                        role = companyObject.getInt("role");
                        user_id = companyObject.getInt("user_id");
                        profile_image = companyObject.getString("profile_image");
                        job_title = companyObject.getString("job_title");
                        greenhouse_access_token = companyObject.getString("greenhouse_access_token");

                        dataModel.setUser_id(user_id);
                        dataModel.setRole(role);
                        dataModel.setName(name);
                        dataModel.setGreenhouse_access_token(greenhouse_access_token);
                        dataModel.setProfile_image(profile_image);
                        dataModel.setJob_title(job_title);
                        dataModel.setId(id);

                        JSONArray applicationsArray = jobObject.getJSONArray("applications");

                        for (int j = 0; j < applicationsArray.length(); j++) {
                            JSONObject jsonObject1 = applicationsArray.getJSONObject(j);

                            id = jsonObject1.getInt("id");
                            name = jsonObject1.getString("name");
                            role = jsonObject1.getInt("role");
                            user_id = jsonObject1.getInt("user_id");
                            profile_image = jsonObject1.getString("profile_image");
                            job_title = jsonObject1.getString("job_title");
                            greenhouse_access_token = jsonObject1.getString("greenhouse_access_token");
                            dataModel.setUser_id(user_id);
                            dataModel.setRole(role);
                            dataModel.setName(name);
                            dataModel.setGreenhouse_access_token(greenhouse_access_token);
                            dataModel.setProfile_image(profile_image);
                            dataModel.setJob_title(job_title);
                            dataModel.setId(id);
                        }

                        JSONObject flagged_byObject = dataObject.getJSONObject("flagged_by");

                        id = flagged_byObject.getInt("id");
                        name = flagged_byObject.getString("name");
                        role = flagged_byObject.getInt("role");
                        user_id = flagged_byObject.getInt("user_id");
                        profile_image = flagged_byObject.getString("profile_image");
                        job_title = flagged_byObject.getString("job_title");
                        greenhouse_access_token = flagged_byObject.getString("greenhouse_access_token");

                        dataModel.setId(id);
                        dataModel.setUser_id(user_id);
                        dataModel.setRole(role);
                        dataModel.setName(name);
                        dataModel.setGreenhouse_access_token(greenhouse_access_token);
                        dataModel.setProfile_image(profile_image);
                        dataModel.setJob_title(job_title);
                    }

                    setUpFlaggedJobRecyclerview(flaggedJobRecyclerView, flaggedJobList);
                }


            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void requestEndedWithError(VolleyError error) {
        super.requestEndedWithError(error);
        progressDialog.dismiss();
    }

    private void setUpFlaggedJobRecyclerview(RecyclerView recyclerView, List<ModelFlaggedJob> list) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(AdminFlaggedJobsActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        flaggedJobAdapter = new FlaggedJobAdapter(list, AdminFlaggedJobsActivity.this);
        recyclerView.setAdapter(flaggedJobAdapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }
}