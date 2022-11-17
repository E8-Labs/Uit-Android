package com.antizon.uit_android.activities.jobs;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.antizon.uit_android.R;
import com.antizon.uit_android.activities.ImageViewerActivity;
import com.antizon.uit_android.adapters.jobs.CompanyJobsAdapter;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.models.jobs.JobsListResponseModel;
import com.antizon.uit_android.models.jobs.SingleJobDataModel;
import com.antizon.uit_android.network.GetDataService;
import com.antizon.uit_android.network.RetrofitClientInstance;
import com.antizon.uit_android.utilities.CustomCookieToast;
import com.antizon.uit_android.utilities.Utilities;
import com.google.android.material.tabs.TabLayout;
import com.makeramen.roundedimageview.RoundedImageView;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompanyAndTeamJobsActivity extends AppCompatActivity implements CompanyJobsAdapter.CompanyJobsAdapterCallBack {
    Context context;
    GetDataService service;
    SessionManagement sessionManagement;
    ProgressDialog progressDialog;

    RelativeLayout btnBack;
    RoundedImageView userProfileImage;
    TextView text_profileName;
    TabLayout tab_layout;
    RecyclerView recyclerview_jobs;
    List<SingleJobDataModel> jobsList;
    CompanyJobsAdapter companyJobsAdapter;

    String personaImage, personaId, personaRole, personaName;

    String jobStatus = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_and_team_jobs);
        Utilities.setCustomStatusAndNavColor(CompanyAndTeamJobsActivity.this, R.color.app_color, R.color.white_dash);
        context = CompanyAndTeamJobsActivity.this;
        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        sessionManagement = new SessionManagement(context);
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading.....");

        personaId = getIntent().getStringExtra("personaId");
        personaRole = getIntent().getStringExtra("personaRole");
        personaImage = getIntent().getStringExtra("personaImage");
        personaName = getIntent().getStringExtra("personaName");

        btnBack = findViewById(R.id.btnBack);
        userProfileImage = findViewById(R.id.userProfileImage);
        text_profileName = findViewById(R.id.text_profileName);
        tab_layout = findViewById(R.id.tab_layout);
        recyclerview_jobs = findViewById(R.id.recyclerview_jobs);
        jobsList = new ArrayList<>();

        Utilities.loadImage(context, personaImage, userProfileImage);
        userProfileImage.setOnClickListener(v -> {
            Intent intent = new Intent(context, ImageViewerActivity.class);
            ViewCompat.setTransitionName(userProfileImage, personaImage);
            String animationName = ViewCompat.getTransitionName(userProfileImage);
            intent.putExtra("animationName", animationName);
            intent.putExtra("IMAGE", personaImage);
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(CompanyAndTeamJobsActivity.this, userProfileImage, Objects.requireNonNull(animationName));
            startActivity(intent, options.toBundle());
        });

        text_profileName.setText(personaName);

        progressDialog.show();
        requestForJobsListList("Bearer " + sessionManagement.getToken(), jobStatus, personaId);

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
                requestForJobsListList("Bearer " + sessionManagement.getToken(), jobStatus, personaId);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        btnBack.setOnClickListener(v -> onBackPressed());
    }

    public void requestForJobsListList(String authToken, String jobStatus, String personaId){
        Call<JobsListResponseModel> call;
        if (personaRole.equals("2")){
            call = service.getCompanyJobsListById(authToken, jobStatus, personaId);
        }else {
            call = service.getRecruiterJobsListById(authToken, jobStatus, personaId);
        }


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
                        showJobsRecyclerView(recyclerview_jobs, jobsList, jobStatus);
                    }
                } else {
                    CustomCookieToast.showFailureToast(CompanyAndTeamJobsActivity.this, response.message());
                }

            }

            @Override
            public void onFailure(@NonNull Call<JobsListResponseModel> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                CustomCookieToast.showFailureToast(CompanyAndTeamJobsActivity.this, t.getMessage());
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
                requestForJobsListList("Bearer " + sessionManagement.getToken(), jobStatus, personaId);
            }
        }
    });


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }
}