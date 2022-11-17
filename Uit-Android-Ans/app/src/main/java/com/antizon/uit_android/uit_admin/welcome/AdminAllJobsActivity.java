package com.antizon.uit_android.uit_admin.welcome;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.antizon.uit_android.R;
import com.antizon.uit_android.activities.jobs.FilledJobDetailActivity;
import com.antizon.uit_android.activities.jobs.JobDetailActivity;
import com.antizon.uit_android.adapters.jobs.CompanyJobsAdapter;
import com.antizon.uit_android.company.welcome.AdminFlaggedJobsActivity;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.models.applicant.GenericApplicantDataModel;
import com.antizon.uit_android.models.jobs.JobsListResponseModel;
import com.antizon.uit_android.models.jobs.SingleJobDataModel;
import com.antizon.uit_android.network.GetDataService;
import com.antizon.uit_android.network.RetrofitClientInstance;
import com.antizon.uit_android.utilities.CustomCookieToast;
import com.antizon.uit_android.utilities.Utilities;
import com.google.android.material.tabs.TabLayout;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminAllJobsActivity extends AppCompatActivity implements CompanyJobsAdapter.CompanyJobsAdapterCallBack {
    Context context;
    GetDataService service;
    SessionManagement sessionManagement;
    ProgressDialog progressDialog;

    TextView btnFlaggedJobs;

    TabLayout tab_layout;
    RecyclerView recyclerview_jobs;
    List<SingleJobDataModel> jobsList;
    CompanyJobsAdapter companyJobsAdapter;

    String jobStatus = "1";

    EditText edSearch;
    RelativeLayout layout_noApplicants;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_all_jobs);
        Utilities.setCustomStatusAndNavColor(AdminAllJobsActivity.this, R.color.white_dash, R.color.white_dash);

        context = AdminAllJobsActivity.this;
        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        sessionManagement = new SessionManagement(context);
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading.....");

        edSearch = findViewById(R.id.edSearch);
        recyclerview_jobs = findViewById(R.id.recyclerview_jobs);
        tab_layout = findViewById(R.id.tab_layout);
        btnFlaggedJobs = findViewById(R.id.btnFlaggedJobs);
        layout_noApplicants = findViewById(R.id.layout_noApplicants);

        progressDialog.show();
        requestForJobsListList("Bearer " + sessionManagement.getToken(), jobStatus);

        btnFlaggedJobs.setOnClickListener(v -> {
            Intent intent = new Intent(context, AdminFlaggedJobsActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
        });

        tab_layout.addTab(tab_layout.newTab().setText("Listed"));
        tab_layout.addTab(tab_layout.newTab().setText("Filled"));
        tab_layout.addTab(tab_layout.newTab().setText("Closed"));

        tab_layout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                if (position == 0){
                    jobStatus = "1";
                }else if (position == 1){
                    jobStatus = "3";
                }else {
                    jobStatus = "2";
                }

                progressDialog.show();
                requestForJobsListList("Bearer " + sessionManagement.getToken(), jobStatus);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        edSearch.addTextChangedListener(new TextWatcher() {
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
        ArrayList<SingleJobDataModel> filteredList = new ArrayList<>();
        for (SingleJobDataModel item : jobsList) {
            if (item.getJobTitle().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        if (filteredList.size() == 0){
            recyclerview_jobs.setVisibility(View.GONE);
            layout_noApplicants.setVisibility(View.VISIBLE);
        }else {
            recyclerview_jobs.setVisibility(View.VISIBLE);
            layout_noApplicants.setVisibility(View.GONE);
            companyJobsAdapter.filterList(filteredList);
        }

    }

    public void requestForJobsListList(String authToken, String jobStatus){
        Call<JobsListResponseModel> call  = service.getAdminJobsList(authToken, jobStatus);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<JobsListResponseModel> call, @NonNull Response<JobsListResponseModel> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    boolean status = response.body().isStatus();
                    if (status) {
                        jobsList = new ArrayList<>();
                        jobsList = response.body().getJobsList();

                        recyclerview_jobs.setVisibility(View.VISIBLE);
                        layout_noApplicants.setVisibility(View.GONE);

                        showJobsRecyclerView(recyclerview_jobs, jobsList, jobStatus);
                    }else {
                        CustomCookieToast.showFailureToast(AdminAllJobsActivity.this, response.body().getMessage());
                    }
                } else {
                    CustomCookieToast.showFailureToast(AdminAllJobsActivity.this, response.message());
                }

            }

            @Override
            public void onFailure(@NonNull Call<JobsListResponseModel> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                CustomCookieToast.showFailureToast(AdminAllJobsActivity.this, t.getMessage());
            }
        });
    }

    private void showJobsRecyclerView(RecyclerView recyclerView, List<SingleJobDataModel> jobsList, String jobStatus) {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        companyJobsAdapter = new CompanyJobsAdapter(context, jobsList ,jobStatus, this);
        recyclerView.setAdapter(companyJobsAdapter);
    }

    @Override
    public void onJobItemClicked(int position) {
        Intent intent;
        if (jobStatus.equals("1")){
            intent = new Intent(context, JobDetailActivity.class);
            intent.putExtra("jobId", String.valueOf(jobsList.get(position).getId()));
        }else{
            intent = new Intent(context, FilledJobDetailActivity.class);
            intent.putExtra("jobDataModel",jobsList.get(position));
        }
        onJobDetailChangedLauncher.launch(intent);
        overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
    }

    ActivityResultLauncher<Intent> onJobDetailChangedLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            if (result.getData() !=null){
                requestForJobsListList("Bearer " + sessionManagement.getToken(), jobStatus);
            }
        }
    });

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }
}