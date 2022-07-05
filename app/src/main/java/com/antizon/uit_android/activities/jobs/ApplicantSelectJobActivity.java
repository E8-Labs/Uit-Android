package com.antizon.uit_android.activities.jobs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.antizon.uit_android.R;
import com.antizon.uit_android.adapters.applicant.ApplicantSelectJobAdapter;
import com.antizon.uit_android.models.applicant.jobs.ApplicantJobDataModel;
import com.antizon.uit_android.models.applicant.jobs.ApplicantJobsListResponseModel;
import com.antizon.uit_android.network.GetDataService;
import com.antizon.uit_android.network.RetrofitClientInstance;
import com.antizon.uit_android.utilities.Utilities;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApplicantSelectJobActivity extends AppCompatActivity implements ApplicantSelectJobAdapter.ApplicantSelectJobAdapterCallBack {
    Context context;
    GetDataService service;

    RelativeLayout btnBack;
    RecyclerView recyclerview_jobs;
    List<ApplicantJobDataModel> jobsList;
    ApplicantSelectJobAdapter applicantSelectJobAdapter;

    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applicant_select_job);
        Utilities.setCustomStatusAndNavColor(ApplicantSelectJobActivity.this, R.color.white_dash, R.color.white_dash);
        context = ApplicantSelectJobActivity.this;
        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        progressDialog = new ProgressDialog(ApplicantSelectJobActivity.this);

        btnBack = findViewById(R.id.btnBack);
        recyclerview_jobs = findViewById(R.id.recyclerview_jobs);

        btnBack.setOnClickListener(v -> onBackPressed());

        progressDialog.setMessage("Loading...");
        progressDialog.show();
        requestForGetJobsList();
    }

    private void requestForGetJobsList() {
        Call<ApplicantJobsListResponseModel> call = service.getApplicantJobsList();
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NotNull Call<ApplicantJobsListResponseModel> call, @NotNull Response<ApplicantJobsListResponseModel> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    if (response.body()!=null){
                        if (response.body().isStatus()){
                            jobsList = new ArrayList<>();
                            jobsList = response.body().getApplicantJobsList();
                            setDegreesToRecyclerView(recyclerview_jobs, jobsList);
                        }else {
                            Toast.makeText(context, "Unsuccessful : " + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }

                } else {
                    Toast.makeText(context, "Unsuccessful : " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<ApplicantJobsListResponseModel> call, @NotNull Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(context, "Failure : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setDegreesToRecyclerView(RecyclerView recyclerView, List<ApplicantJobDataModel> jobsList) {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context,LinearLayoutManager.VERTICAL , false);
        recyclerView.setLayoutManager(layoutManager);
        applicantSelectJobAdapter = new ApplicantSelectJobAdapter(context, jobsList, this);
        recyclerView.setAdapter(applicantSelectJobAdapter);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_out_up, R.anim.slide_bottom);
    }

    @Override
    public void onItemClick(ApplicantJobDataModel applicantJobDataModel) {
        Intent intent = new Intent();
        intent.putExtra("applicantJobDataModel", applicantJobDataModel);
        setResult(RESULT_OK, intent);
        finish();
        overridePendingTransition(R.anim.slide_out_up, R.anim.slide_bottom);
    }
}