package com.antizon.uit_android.applicant.welcome;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.antizon.uit_android.R;
import com.antizon.uit_android.activities.jobs.ApplicantJobFilterActivity;
import com.antizon.uit_android.adapters.filter.JobFilterAdapter;
import com.antizon.uit_android.applicant.activities.ActivityApplicantJobDetail;
import com.antizon.uit_android.company.utility.BaseActivity;
import com.antizon.uit_android.generic.adapter.LatestJobAdapter;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.models.applicant.home.ApplicantHomeJobDataModel;
import com.antizon.uit_android.models.applicant.home.ApplicantHomeResponseDataModel;
import com.antizon.uit_android.models.applicant.home.ApplicantHomeResponseModel;
import com.antizon.uit_android.models.applicant.jobs.ApplicantJobFilterResponseModel;
import com.antizon.uit_android.network.GetDataService;
import com.antizon.uit_android.network.RetrofitClientInstance;
import com.antizon.uit_android.utilities.CustomCookieToast;
import com.antizon.uit_android.utilities.Utilities;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApplicantJobSearchActivity extends BaseActivity implements LatestJobAdapter.LatestJobAdapterCallBack {

    Context context;
    GetDataService service;

    SessionManagement sessionManagement;
    ImageView backWhite,filterIcon;
    ProgressDialog progressDialog;
    RecyclerView listed_companies_recyclerview;
    LatestJobAdapter latestJobAdapter;
    List<ApplicantHomeJobDataModel> latestList;
    TextView text_jobsFound;
    EditText searchOffer;
    RelativeLayout layout_noJobs;

    RecyclerView recyclerview_appliedFilter;
    ArrayList<String> filtersList, industries, employment, work_location;
    JobFilterAdapter jobFilterAdapter;

    String jobTitle, location;
    int minSalary, maxSalary;
    double latitude, longitude;
    boolean filterApplied;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_search);
        Utilities.setCustomStatusAndNavColor(ApplicantJobSearchActivity.this, R.color.white_dash, R.color.white_dash);

        context = ApplicantJobSearchActivity.this;
        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        progressDialog = new ProgressDialog(context);
        sessionManagement = new SessionManagement(context);

        filterApplied = getIntent().getBooleanExtra("filterApplied", false);
        latitude = getIntent().getDoubleExtra("latitude",31.5204);
        longitude = getIntent().getDoubleExtra("longitude",74.3587);


        initViews();

        if (filterApplied){
            industries = getIntent().getStringArrayListExtra("industries");
            employment = getIntent().getStringArrayListExtra("employment");
            work_location = getIntent().getStringArrayListExtra("work_location");
            minSalary = getIntent().getIntExtra("min_salary", 0);
            maxSalary = getIntent().getIntExtra("max_salary", 0);
            jobTitle = getIntent().getStringExtra("jobTitle");
            location = getIntent().getStringExtra("location");
            latitude = getIntent().getDoubleExtra("latitude", 31.5204);
            longitude = getIntent().getDoubleExtra("longitude", 74.3587);

            filtersList = getIntent().getStringArrayListExtra("filtersList");

            showAppliedFilterRecyclerview(recyclerview_appliedFilter, filtersList);

            if (jobTitle == null) {
                jobTitle = "";
            }
            progressDialog.setMessage("Loading...");
            progressDialog.show();
            requestForApplyJobFilter("Bearer " + sessionManagement.getToken(), jobTitle, minSalary*1000+"", maxSalary*1000+"", work_location, employment, latitude+"", longitude+"");

        }else {
            progressDialog.setMessage("Loading...");
            progressDialog.show();
            getApplicantHomeData("Bearer " + sessionManagement.getToken(), latitude+"", longitude+"");

        }

    }

    private void initViews() {
        filtersList = new ArrayList<>();
        listed_companies_recyclerview = findViewById(R.id.listed_companies_recyclerview);
        backWhite = findViewById(R.id.backWhite);
        filterIcon = findViewById(R.id.filterIcon);
        searchOffer = findViewById(R.id.searchOffer);
        layout_noJobs = findViewById(R.id.layout_noJobs);
        text_jobsFound = findViewById(R.id.text_jobsFound);
        recyclerview_appliedFilter = findViewById(R.id.recyclerview_appliedFilter);

        backWhite.setOnClickListener(view -> onBackPressed());

        filterIcon.setOnClickListener(v -> {
            Utilities.hideKeyboard(filterIcon, context);
            Intent intent = new Intent(context, ApplicantJobFilterActivity.class);
            onFilterAppliedLauncher.launch(intent);
            overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
        });

    }


    private void filter(String text) {
        ArrayList<ApplicantHomeJobDataModel> filteredList = new ArrayList<>();
        for (ApplicantHomeJobDataModel item : latestList) {
            if (item.getJob_title().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        if (filteredList.size() == 0){
            text_jobsFound.setVisibility(View.GONE);
            listed_companies_recyclerview.setVisibility(View.GONE);
            layout_noJobs.setVisibility(View.VISIBLE);
        }else {
            String jobsFound = filteredList.size() + " jobs found";
            text_jobsFound.setVisibility(View.VISIBLE);
            listed_companies_recyclerview.setVisibility(View.VISIBLE);
            layout_noJobs.setVisibility(View.GONE);
            text_jobsFound.setText(jobsFound);
            latestJobAdapter.filterList(filteredList);
        }

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
                        ApplicantHomeResponseDataModel applicantHomeResponseDataModel = response.body().getApplicantHomeResponseDataModel();
                        if (applicantHomeResponseDataModel !=null){
                            latestList = new ArrayList<>();
                            latestList = applicantHomeResponseDataModel.getLatestJobsList();
                            String jobsFound = latestList.size() + " jobs found";
                            text_jobsFound.setText(jobsFound);
                            setUpLatestJobRecyclerview(listed_companies_recyclerview, latestList);

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


                    } else {
                        CustomCookieToast.showFailureToast( ApplicantJobSearchActivity.this, "Failure!", response.body().getMessage());
                    }
                } else {
                    CustomCookieToast.showFailureToast( ApplicantJobSearchActivity.this, "Failure!", response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApplicantHomeResponseModel> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                CustomCookieToast.showFailureToast(ApplicantJobSearchActivity.this, "Failure!",  t.getMessage());
            }
        });
    }

    private void requestForApplyJobFilter(String token, String job_title, String min_salary, String  maxSalary, ArrayList<String> work_locations, ArrayList<String> employment_types, String latitude, String longitude){

        Call<ApplicantJobFilterResponseModel> call = service.filterJobs(token, job_title, min_salary, maxSalary, work_locations.toArray(new String[0]), employment_types.toArray(new String[0]), latitude, longitude);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ApplicantJobFilterResponseModel> call, @NonNull Response<ApplicantJobFilterResponseModel> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    boolean status = response.body().isStatus();
                    if (status) {
                        latestList = new ArrayList<>();
                        latestList =  response.body().getJobsList();
                        String jobsFound = latestList.size() + " jobs found";
                        text_jobsFound.setText(jobsFound);
                        setUpLatestJobRecyclerview(listed_companies_recyclerview, latestList);

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
                    } else {
                        CustomCookieToast.showFailureToast( ApplicantJobSearchActivity.this, "Failure!", response.body().getMessage());
                    }
                } else {
                    CustomCookieToast.showFailureToast( ApplicantJobSearchActivity.this, "Failure!", response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApplicantJobFilterResponseModel> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                CustomCookieToast.showFailureToast(ApplicantJobSearchActivity.this, "Failure!",  t.getMessage());
            }
        });
    }


    private void setUpLatestJobRecyclerview(RecyclerView recyclerView, List<ApplicantHomeJobDataModel> latestList) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        latestJobAdapter = new LatestJobAdapter(context, latestList, this);
        recyclerView.setAdapter(latestJobAdapter);
    }


    @Override
    public void onItemClick(ApplicantHomeJobDataModel jobDataModel) {
        Intent intent = new Intent(context, ActivityApplicantJobDetail.class);
        intent.putExtra("jobDataModel", jobDataModel);
        onApplyJobLauncher.launch(intent);
        overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }

    ActivityResultLauncher<Intent> onApplyJobLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            if (!filterApplied){
                getApplicantHomeData("Bearer " + sessionManagement.getToken(), latitude+"", longitude+"");
            }else{
                requestForApplyJobFilter("Bearer " + sessionManagement.getToken(), jobTitle, minSalary*1000+"", maxSalary*1000+"", work_location, employment, latitude+"", longitude+"");
            }
        }
    });

    private void showAppliedFilterRecyclerview(RecyclerView recyclerView, List<String> filtersList){
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        jobFilterAdapter = new JobFilterAdapter(context, filtersList);
        recyclerview_appliedFilter.setAdapter(jobFilterAdapter);
    }

    ActivityResultLauncher<Intent> onFilterAppliedLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent resultIntent = result.getData();
            if (resultIntent !=null){
                filtersList = resultIntent.getStringArrayListExtra("filtersList");
                industries = resultIntent.getStringArrayListExtra("industries");
                employment = resultIntent.getStringArrayListExtra("employment");
                work_location = resultIntent.getStringArrayListExtra("work_location");
                minSalary = resultIntent.getIntExtra("min_salary", 0);
                maxSalary = resultIntent.getIntExtra("max_salary", 0);
                jobTitle = resultIntent.getStringExtra("jobTitle");
                location = resultIntent.getStringExtra("location");
                latitude = resultIntent.getDoubleExtra("latitude", 31.5204);
                longitude = resultIntent.getDoubleExtra("longitude", 74.3587);

                showAppliedFilterRecyclerview(recyclerview_appliedFilter, filtersList);

                if (jobTitle == null) {
                    jobTitle = "";
                }

                progressDialog.setMessage("Loading...");
                progressDialog.show();
                requestForApplyJobFilter("Bearer " + sessionManagement.getToken(), jobTitle, minSalary*1000+"", maxSalary*1000+"", work_location, employment, latitude+"", longitude+"");

            }

        }
    });
}