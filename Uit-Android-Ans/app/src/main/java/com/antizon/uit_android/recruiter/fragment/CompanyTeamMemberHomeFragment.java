package com.antizon.uit_android.recruiter.fragment;

import android.annotation.SuppressLint;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.antizon.uit_android.R;
import com.antizon.uit_android.activities.jobs.JobDetailActivity;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.models.jobs.JobsListResponseModel;
import com.antizon.uit_android.models.jobs.SingleJobDataModel;
import com.antizon.uit_android.network.GetDataService;
import com.antizon.uit_android.network.RetrofitClientInstance;
import com.antizon.uit_android.notifications.activities.NotificationsActivity;
import com.antizon.uit_android.recruiter.adapters.CompanyTeamMemberJobSelectionAdapter;
import com.antizon.uit_android.recruiter.adapters.TeamMemberJobsAdapter;
import com.antizon.uit_android.recruiter.models.RecruiterJobTypeSelectionModel;
import com.antizon.uit_android.utilities.CustomCookieToast;
import com.antizon.uit_android.utilities.Utilities;
import com.makeramen.roundedimageview.RoundedImageView;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CompanyTeamMemberHomeFragment extends Fragment implements CompanyTeamMemberJobSelectionAdapter.CompanyTeamMemberJobSelectionAdapterCallBack, TeamMemberJobsAdapter.TeamMemberJobsAdapterCallBack {

    Context context;
    GetDataService service;
    SessionManagement sessionManagement;
    ProgressDialog progressDialog;

    ImageView dashboardNotification;
    RoundedImageView userProfile;
    RecyclerView recyclerview_jobSelection, recyclerview_jobsRecyclerview;
    List<RecruiterJobTypeSelectionModel> jobSelectionList;
    CompanyTeamMemberJobSelectionAdapter companyTeamMemberJobSelectionAdapter;
    List<SingleJobDataModel> jobsList;
    TeamMemberJobsAdapter teamMemberJobsAdapter;

    View rootView;

    String selectedJobType = "All";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_company_team_member_home, container, false);
        context = requireActivity();
        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        sessionManagement = new SessionManagement(context);
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading.....");

        dashboardNotification = rootView.findViewById(R.id.dashboardNotification);
        recyclerview_jobsRecyclerview = rootView.findViewById(R.id.recyclerview_jobsRecyclerview);
        recyclerview_jobSelection = rootView.findViewById(R.id.recyclerview_jobSelection);
        userProfile = rootView.findViewById(R.id.userProfile);

        Utilities.loadImage(context, sessionManagement.getProfileImage(), userProfile);

        jobSelectionList = new ArrayList<>();
        jobSelectionList.add(new RecruiterJobTypeSelectionModel(sessionManagement.getKeyParentId(), "All",  sessionManagement.getKeyParentProfileImage(),  true));
        jobSelectionList.add(new RecruiterJobTypeSelectionModel(sessionManagement.getKeyId(), "My Jobs",  sessionManagement.getProfileImage(),  false));
        showJobSelectionRecyclerView(recyclerview_jobSelection, jobSelectionList);


        progressDialog.show();
        requestForJobsListList("Bearer " + sessionManagement.getToken(), selectedJobType);

        dashboardNotification.setOnClickListener(v -> {
            Intent intent = new Intent(context, NotificationsActivity.class);
            startActivity(intent);
            requireActivity().overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
        });

        return rootView;
    }

    public void requestForJobsListList(String authToken, String type){
        Call<JobsListResponseModel> call;
        if (type.equals("All")){
            call  = service.getCompanyJobsList(authToken, "1");
        }else {
            call= service.getRecruiterJobsList(authToken, "1");
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
                        showJobsRecyclerView(recyclerview_jobsRecyclerview, jobsList);
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

    private void showJobsRecyclerView(RecyclerView recyclerView, List<SingleJobDataModel> jobsList) {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        teamMemberJobsAdapter = new TeamMemberJobsAdapter(context, jobsList ,this);
        recyclerView.setAdapter(teamMemberJobsAdapter);
    }

    private void showJobSelectionRecyclerView(RecyclerView recyclerView, List<RecruiterJobTypeSelectionModel> jobSelectionList) {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        companyTeamMemberJobSelectionAdapter = new CompanyTeamMemberJobSelectionAdapter(context, jobSelectionList ,this);
        recyclerView.setAdapter(companyTeamMemberJobSelectionAdapter);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onItemClick(int position) {
        selectedJobType = jobSelectionList.get(position).getName();
        for (int i = 0; i < jobSelectionList.size(); i++) {
            jobSelectionList.get(i).setSelected(i == position);
        }
        companyTeamMemberJobSelectionAdapter.notifyDataSetChanged();

        progressDialog.show();
        requestForJobsListList("Bearer " + sessionManagement.getToken(), selectedJobType);

    }


    @Override
    public void onJobItemClicked(int position) {
        Intent intent = new Intent(context, JobDetailActivity.class);
        intent.putExtra("jobId", String.valueOf(jobsList.get(position).getId()));
        onJobDetailChangedLauncher.launch(intent);
        requireActivity().overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
    }



    ActivityResultLauncher<Intent> onJobDetailChangedLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            if (result.getData() !=null){
                requestForJobsListList("Bearer " + sessionManagement.getToken(), selectedJobType);
            }
        }
    });


    public void newJobAddedListener() {
        requestForJobsListList("Bearer " + sessionManagement.getToken(), selectedJobType);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void onMyJObsClicked() {
        for (int i = 0; i < jobSelectionList.size(); i++) {
            jobSelectionList.get(i).setSelected(i == 1);
        }

        companyTeamMemberJobSelectionAdapter.notifyDataSetChanged();
        selectedJobType = "My Jobs";
        requestForJobsListList("Bearer " + sessionManagement.getToken(), selectedJobType);
    }





}