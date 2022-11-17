package com.antizon.uit_android.applicant.activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.antizon.uit_android.R;
import com.antizon.uit_android.company.utility.BaseActivity;
import com.antizon.uit_android.generic.adapter.LatestJobAdapter;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.models.applicant.home.ApplicantHomeJobDataModel;
import com.antizon.uit_android.models.applicant.jobs.ApplicantJobFilterResponseModel;
import com.antizon.uit_android.network.GetDataService;
import com.antizon.uit_android.network.RetrofitClientInstance;
import com.antizon.uit_android.utilities.CustomCookieToast;
import com.antizon.uit_android.utilities.SimpleScrollListener;
import com.antizon.uit_android.utilities.Utilities;
import com.google.android.material.tabs.TabLayout;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApplicantSavedJobsActivity extends BaseActivity implements LatestJobAdapter.LatestJobAdapterCallBack {
    Context context;
    GetDataService service;
    SessionManagement sessionManagement;
    ProgressDialog progressDialog;

    ImageView backIcon;

    TabLayout tab_layout;
    RecyclerView recyclerviewJobs;
    LatestJobAdapter latestJobAdapter;
    List<ApplicantHomeJobDataModel> jobsList, newJobsList;


    private boolean isLoading, isLastPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applicant_saved_jobs);
        Utilities.setCustomStatusAndNavColor(ApplicantSavedJobsActivity.this, R.color.white_dash, R.color.white_dash);


        context = ApplicantSavedJobsActivity.this;
        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        sessionManagement = new SessionManagement(context);
        progressDialog = new ProgressDialog(context);

        tab_layout = findViewById(R.id.tab_layout);
        recyclerviewJobs = findViewById(R.id.recyclerviewJobs);
        backIcon = findViewById(R.id.backIcon);

        backIcon.setOnClickListener(v -> onBackPressed());


        tab_layout.addTab(tab_layout.newTab().setText("Applied"), true);
        tab_layout.addTab(tab_layout.newTab().setText("Saved"));
        setMarginBetweenTabs(tab_layout);

        progressDialog.setMessage("Loading.....");
        progressDialog.show();
        requestForApplicantJobsList("Bearer " + sessionManagement.getToken(), "applied");


        tab_layout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                progressDialog.show();
                if (position == 0){
                    requestForApplicantJobsList("Bearer " + sessionManagement.getToken(), "applied");
                }else if (position == 1){
                    requestForApplicantJobsList("Bearer " + sessionManagement.getToken(), "saved");
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setMarginBetweenTabs(TabLayout tabLayout) {
        ViewGroup tabs = (ViewGroup) tabLayout.getChildAt(0);
        for (int i = 0; i < tabs.getChildCount() - 1; i++) {
            View tab = tabs.getChildAt(i);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) tab.getLayoutParams();
            layoutParams.setMarginEnd(50);
            tab.setLayoutParams(layoutParams);
            tabLayout.requestLayout();
        }
    }


    public void requestForApplicantJobsList(String authToken, String type){
        Call<ApplicantJobFilterResponseModel> call;
        if (type.equals("applied")){
            call = service.getAppliedJobs(authToken, "0");
        }else {
            call = service.getSavedJobsList(authToken, "0");
        }


        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ApplicantJobFilterResponseModel> call, @NonNull Response<ApplicantJobFilterResponseModel> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    boolean status = response.body().isStatus();
                    if (status) {
                        jobsList = new ArrayList<>();
                        newJobsList = new ArrayList<>();

                        jobsList = response.body().getJobsList();

                        showUitMembersRecyclerView(recyclerviewJobs, jobsList, type);

                        if (jobsList != null){
                            if (jobsList.size() >= 15) {
                                isLastPage = false;
                            } else {
                                isLastPage = true;
                                isLoading = false;
                            }
                        }
                    }
                } else {
                    CustomCookieToast.showFailureToast(ApplicantSavedJobsActivity.this, response.message());
                }

            }

            @Override
            public void onFailure(@NonNull Call<ApplicantJobFilterResponseModel> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                CustomCookieToast.showFailureToast(ApplicantSavedJobsActivity.this, t.getMessage());
            }
        });
    }

    private void showUitMembersRecyclerView(RecyclerView recyclerView, List<ApplicantHomeJobDataModel> jobsList, String  type){
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        latestJobAdapter = new LatestJobAdapter(context, jobsList, this);
        recyclerView.setAdapter(latestJobAdapter);


        recyclerView.addOnScrollListener(new SimpleScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                if (!isLastPage) {
                    loadMoreItemsForPagination(latestJobAdapter, type);
                }
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

        });
    }

    private void loadMoreItemsForPagination(LatestJobAdapter latestJobAdapter, String type) {
        Call<ApplicantJobFilterResponseModel> call;
        if (type.equals("applied")){
            call = service.getAppliedJobs("Bearer " + sessionManagement.getToken(), String.valueOf(jobsList.size()));
        }else {
            call = service.getSavedJobsList("Bearer " + sessionManagement.getToken(), String.valueOf(jobsList.size()));
        }
        call.enqueue(new Callback<>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NotNull Call<ApplicantJobFilterResponseModel> call, @NotNull Response<ApplicantJobFilterResponseModel> response) {
                isLoading = false;
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    boolean status = response.body().isStatus();
                    if (status) {

                        newJobsList = new ArrayList<>();

                        newJobsList = response.body().getJobsList();

                        jobsList.addAll(newJobsList);

                        if (newJobsList.size() >= 15) {
                            isLastPage = false;
                        } else {
                            isLastPage = true;
                            isLoading = false;
                        }

                        latestJobAdapter.notifyDataSetChanged();

                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<ApplicantJobFilterResponseModel> call, @NotNull Throwable t) {
                isLoading = false;
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }

    @Override
    public void onItemClick(ApplicantHomeJobDataModel jobDataModel) {
        Intent intent = new Intent(context, ActivityApplicantJobDetail.class);
        intent.putExtra("jobDataModel", jobDataModel);
        startActivity(intent);
        overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
    }
}