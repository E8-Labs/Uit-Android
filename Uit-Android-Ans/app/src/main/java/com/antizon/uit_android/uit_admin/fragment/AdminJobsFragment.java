package com.antizon.uit_android.uit_admin.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.antizon.uit_android.R;
import com.antizon.uit_android.activities.jobs.FilledJobDetailActivity;
import com.antizon.uit_android.activities.jobs.JobDetailActivity;
import com.antizon.uit_android.adapters.jobs.CompanyJobsAdapter;
import com.antizon.uit_android.company.activities.postjob.CompanyPostJobActivity;
import com.antizon.uit_android.company.welcome.AdminFlaggedJobsActivity;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.models.jobs.JobsListResponseModel;
import com.antizon.uit_android.models.jobs.SingleJobDataModel;
import com.antizon.uit_android.network.GetDataService;
import com.antizon.uit_android.network.RetrofitClientInstance;
import com.antizon.uit_android.notifications.activities.NotificationsActivity;
import com.antizon.uit_android.utilities.CustomCookieToast;
import com.google.android.material.tabs.TabLayout;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AdminJobsFragment extends Fragment implements CompanyJobsAdapter.CompanyJobsAdapterCallBack {

    Context context;
    GetDataService service;
    SessionManagement sessionManagement;
    ProgressDialog progressDialog;

    RelativeLayout btnAddJob;
    TextView btnFlaggedJobs;
    EditText edSearch;
    TabLayout tab_layout;
    RecyclerView recyclerview_jobs;
    List<SingleJobDataModel> jobsList;
    CompanyJobsAdapter companyJobsAdapter;
    ImageView dashboardNotification;
    String jobStatus = "1";

    RelativeLayout layout_noApplicants;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_admin_jobs, container, false);
        context = requireActivity();
        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        sessionManagement = new SessionManagement(context);
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading.....");

        edSearch = rootView.findViewById(R.id.edSearch);
        recyclerview_jobs = rootView.findViewById(R.id.recyclerview_jobs);
        tab_layout = rootView.findViewById(R.id.tab_layout);
        btnAddJob = rootView.findViewById(R.id.btnAddJob);
        btnFlaggedJobs = rootView.findViewById(R.id.btnFlaggedJobs);
        dashboardNotification = rootView.findViewById(R.id.dashboardNotification);
        layout_noApplicants = rootView.findViewById(R.id.layout_noApplicants);

        requestForJobsListList("Bearer " + sessionManagement.getToken(), jobStatus);

        dashboardNotification.setOnClickListener(v -> {
            Intent intent = new Intent(context, NotificationsActivity.class);
            startActivity(intent);
            requireActivity().overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
        });

        btnFlaggedJobs.setOnClickListener(v -> {
            Intent intent = new Intent(context, AdminFlaggedJobsActivity.class);
            startActivity(intent);
            requireActivity().overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
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

        btnAddJob.setOnClickListener(v -> {
            Intent postJobIntent = new Intent(context, CompanyPostJobActivity.class);
            postJobIntent.putExtra("from", "add");
            onJobPostedLauncher.launch(postJobIntent);
            requireActivity().overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
        });


        if (sessionManagement.getKeyUnreadNotifications().equals("0")){
            dashboardNotification.setImageResource(R.drawable.notification_all_read_ic);
        }else {
            dashboardNotification.setImageResource(R.drawable.notifications_ic);
        }

        return rootView;
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
                    }
                } else {
                    CustomCookieToast.showFailureToast(requireActivity(), response.message());
                }

            }

            @Override
            public void onFailure(@NonNull Call<JobsListResponseModel> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                CustomCookieToast.showFailureToast(requireActivity(), t.getMessage());
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
        requireActivity().overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
    }

    ActivityResultLauncher<Intent> onJobDetailChangedLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            if (result.getData() !=null){
                requestForJobsListList("Bearer " + sessionManagement.getToken(), jobStatus);
            }
        }
    });

    ActivityResultLauncher<Intent> onJobPostedLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            if (jobStatus.equals("1")){
                requestForJobsListList("Bearer " + sessionManagement.getToken(), jobStatus);
            }
        }
    });

}