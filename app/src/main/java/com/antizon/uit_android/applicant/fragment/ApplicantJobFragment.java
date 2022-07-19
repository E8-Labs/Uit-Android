package com.antizon.uit_android.applicant.fragment;


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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.antizon.uit_android.R;
import com.antizon.uit_android.activities.jobs.ApplicantJobFilterActivity;
import com.antizon.uit_android.applicant.activities.ActivityApplicantJobDetail;
import com.antizon.uit_android.applicant.activities.ApplicantSavedJobsActivity;
import com.antizon.uit_android.applicant.welcome.ActivityApplicantJobStatus;
import com.antizon.uit_android.applicant.welcome.ApplicantJobSearchActivity;
import com.antizon.uit_android.applicant.welcome.ActivityApplicantSelectRace;
import com.antizon.uit_android.generic.adapter.FeatureJobAdapter;
import com.antizon.uit_android.generic.adapter.LatestJobAdapter;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.models.applicant.home.ApplicantHomeJobDataModel;
import com.antizon.uit_android.models.applicant.home.ApplicantHomeResponseDataModel;
import com.antizon.uit_android.models.applicant.home.ApplicantHomeResponseModel;
import com.antizon.uit_android.network.GetDataService;
import com.antizon.uit_android.network.RetrofitClientInstance;
import com.antizon.uit_android.utilities.CustomCookieToast;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApplicantJobFragment extends Fragment implements FeatureJobAdapter.FeatureJobAdapterCallBack, LatestJobAdapter.LatestJobAdapterCallBack {
    Context context;
    GetDataService service;

    TextView name, myJobs;
    EditText searchOffer;

    SessionManagement sessionManagement;
    ProgressDialog progressDialog;
    RecyclerView feature_recyclerview, recommended_recyclerview, latest_jobs_recyclerview;
    String userName;

    FeatureJobAdapter featureJobAdapter;
    LatestJobAdapter latestJobAdapter;
    LatestJobAdapter recommendedJobAdapter;

    List<ApplicantHomeJobDataModel> latestList, featuredList, recommendedList;

    ImageView dashboardNotification, dashboardMen;

    View rootView;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_applicant_job, container, false);
        context = requireActivity();
        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        progressDialog = new ProgressDialog(context);
        sessionManagement = new SessionManagement(context);
        initViews(rootView);

        if (!sessionManagement.getKeyApplicationStatus().equals("3")){
            completeProfileBottomSheet();
        }
        return rootView;
    }

    public void initViews(View view) {

        dashboardNotification = view.findViewById(R.id.dashboardNotification);
        myJobs = view.findViewById(R.id.myJobs);
        dashboardMen = view.findViewById(R.id.dashboardMen);

        searchOffer = view.findViewById(R.id.searchOffer);
        name = view.findViewById(R.id.name);

        feature_recyclerview = view.findViewById(R.id.feature_recyclerview);
        recommended_recyclerview = view.findViewById(R.id.recommended_recyclerview);
        latest_jobs_recyclerview = view.findViewById(R.id.latest_jobs_recyclerview);


        userName = sessionManagement.getUserName();
        name.setText(userName);

        myJobs.setOnClickListener(v -> {
            Intent intent = new Intent(context, ApplicantSavedJobsActivity.class);
            startActivity(intent);
            requireActivity().overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
        });

        searchOffer.setOnClickListener(v -> {
            Intent intent = new Intent(context, ApplicantJobSearchActivity.class);
            intent.putExtra("filterApplied", false);
            startActivity(intent);
            requireActivity().overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
        });

        dashboardMen.setOnClickListener(v -> {
            Intent intent = new Intent(context, ApplicantJobFilterActivity.class);
            onFilterAppliedLauncher.launch(intent);
            requireActivity().overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
        });
        
        
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        getApplicantHomeData("Bearer " + sessionManagement.getToken(), "10", "10");

    }

    private void getApplicantHomeData(String token, String latitude, String longitude){
        Call<ApplicantHomeResponseModel> call = service.getApplicantHome(token, latitude, longitude);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ApplicantHomeResponseModel> call, @NonNull Response<ApplicantHomeResponseModel> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    boolean status = response.body().isStatus();
                    if (status) {
                        ApplicantHomeResponseDataModel  applicantHomeResponseDataModel = response.body().getApplicantHomeResponseDataModel();

                        if (applicantHomeResponseDataModel !=null){
                            latestList = new ArrayList<>();
                            featuredList = new ArrayList<>();
                            recommendedList = new ArrayList<>();

                            latestList = applicantHomeResponseDataModel.getLatestJobsList();
                            featuredList = applicantHomeResponseDataModel.getFeaturedJobList();
                            recommendedList = applicantHomeResponseDataModel.getRecommendedJobsList();

                            setUpFeatureJobRecyclerview(feature_recyclerview, featuredList);
                            setUpLatestJobRecyclerview(latest_jobs_recyclerview, latestList);
                            setRecommendedJobRecyclerview(recommended_recyclerview, recommendedList);
                        }


                    } else {
                        CustomCookieToast.showFailureToast( requireActivity(), "Failure!", response.body().getMessage());
                    }
                } else {
                    CustomCookieToast.showFailureToast( requireActivity(), "Failure!", response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApplicantHomeResponseModel> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                CustomCookieToast.showFailureToast( requireActivity(), "Failure!",  t.getMessage());
            }
        });
    }

    public void setUpFeatureJobRecyclerview(RecyclerView recyclerView, List<ApplicantHomeJobDataModel> featuredList) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, true);
        recyclerView.setLayoutManager(layoutManager);
        featureJobAdapter = new FeatureJobAdapter(context, featuredList, this);
        recyclerView.setAdapter(featureJobAdapter);
    }


    void setUpLatestJobRecyclerview(RecyclerView recyclerView, List<ApplicantHomeJobDataModel> latestList) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        latestJobAdapter = new LatestJobAdapter(context, latestList, this);
        recyclerView.setAdapter(latestJobAdapter);
    }

    void setRecommendedJobRecyclerview(RecyclerView recyclerView, List<ApplicantHomeJobDataModel> latestList) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        recommendedJobAdapter = new LatestJobAdapter(context, latestList, this);
        recyclerView.setAdapter(recommendedJobAdapter);
    }


    void completeProfileBottomSheet() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context, R.style.BottomSheetTheme);
        View sheetView = LayoutInflater.from(context).inflate(R.layout.complete_profile_bottomsheet, rootView.findViewById(R.id.completeYourProfileLayout));

        String profileCompletion = "Your Profile is " + sessionManagement.getKeyApplicationStatus() + "/3 complete";

        TextView btnNotNow = sheetView.findViewById(R.id.notNow);
        TextView btnCompleteProfile = sheetView.findViewById(R.id.completeProfile);
        TextView text_profileCompletion = sheetView.findViewById(R.id.text_profileCompletion);

        text_profileCompletion.setText(profileCompletion);

        btnNotNow.setOnClickListener(v -> bottomSheetDialog.dismiss());
        btnCompleteProfile.setOnClickListener(view -> {
            bottomSheetDialog.dismiss();
            if (sessionManagement.getKeyApplicationStatus().equals("1")){
                Intent intent = new Intent(context, ActivityApplicantSelectRace.class);
                startActivity(intent);
                requireActivity().overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
            }else if (sessionManagement.getKeyApplicationStatus().equals("2")){
                Intent intent = new Intent(context, ActivityApplicantJobStatus.class);
                startActivity(intent);
                requireActivity().overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
            }

        });

        bottomSheetDialog.setContentView(sheetView);
        bottomSheetDialog.show();
        bottomSheetDialog.setDismissWithAnimation(true);
    }

    @Override
    public void onItemClick(ApplicantHomeJobDataModel jobDataModel) {
        Intent intent = new Intent(context, ActivityApplicantJobDetail.class);
        intent.putExtra("jobDataModel", jobDataModel);
        onApplyJobLauncher.launch(intent);
        requireActivity().overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
    }

    ActivityResultLauncher<Intent> onApplyJobLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            getApplicantHomeData("Bearer " + sessionManagement.getToken(), "10", "10");
        }
    });

    ActivityResultLauncher<Intent> onFilterAppliedLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent resultIntent = result.getData();
            if (resultIntent !=null){
                ArrayList<String> filtersList = resultIntent.getStringArrayListExtra("filtersList");

                Intent intent = new Intent(context, ApplicantJobSearchActivity.class);
                intent.putExtra("filterApplied", true);
                intent.putStringArrayListExtra("filtersList", filtersList);
                startActivity(intent);
                requireActivity().overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
            }

        }
    });
}
