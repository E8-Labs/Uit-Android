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
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.antizon.uit_android.R;
import com.antizon.uit_android.activities.jobs.ApplicantJobFilterActivity;
import com.antizon.uit_android.applicant.activities.ActivityApplicantJobDetail;
import com.antizon.uit_android.company.utility.BaseActivity;
import com.antizon.uit_android.generic.adapter.LatestJobAdapter;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.models.applicant.home.ApplicantHomeJobDataModel;
import com.antizon.uit_android.models.applicant.home.ApplicantHomeResponseDataModel;
import com.antizon.uit_android.models.applicant.home.ApplicantHomeResponseModel;
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
    private static final String TAG = ApplicantJobSearchActivity.class.getSimpleName();
    Context context;
    GetDataService service;

    SessionManagement sessionManagement;
    ImageView backWhite,filterIcon;
    ProgressDialog progressDialog;
    RecyclerView listed_companies_recyclerview;
    LatestJobAdapter latestJobAdapter;
    List<ApplicantHomeJobDataModel> latestList;

    TextView jobFound;
    EditText searchOffer;

    RelativeLayout layout_noJobs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_search);
        Utilities.setCustomStatusAndNavColor(ApplicantJobSearchActivity.this, R.color.white_dash, R.color.white_dash);

        context = ApplicantJobSearchActivity.this;
        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        progressDialog = new ProgressDialog(context);
        sessionManagement = new SessionManagement(context);

        initViews();

        progressDialog.setMessage("Loading...");
        progressDialog.show();
        getApplicantHomeData("Bearer " + sessionManagement.getToken(), "10", "10");
    }

    void initViews() {
        Log.d(TAG, "setIds: ");
        listed_companies_recyclerview = findViewById(R.id.listed_companies_recyclerview);
        backWhite = findViewById(R.id.backWhite);
        filterIcon = findViewById(R.id.filterIcon);
        searchOffer = findViewById(R.id.searchOffer);
        layout_noJobs = findViewById(R.id.layout_noJobs);
        jobFound = findViewById(R.id.job);

        backWhite.setOnClickListener(view -> onBackPressed());

        filterIcon.setOnClickListener(v -> {
            Intent intent = new Intent(context, ApplicantJobFilterActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
        });

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
        ArrayList<ApplicantHomeJobDataModel> filteredList = new ArrayList<>();
        for (ApplicantHomeJobDataModel item : latestList) {
            if (item.getJob_title().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        if (filteredList.size() == 0){
            jobFound.setVisibility(View.GONE);
            listed_companies_recyclerview.setVisibility(View.GONE);
            layout_noJobs.setVisibility(View.VISIBLE);
        }else {
            String jobsFound = filteredList.size() + " jobs found";

            jobFound.setVisibility(View.VISIBLE);
            listed_companies_recyclerview.setVisibility(View.VISIBLE);
            layout_noJobs.setVisibility(View.GONE);
            jobFound.setText(jobsFound);
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
                            jobFound.setText(jobsFound);
                            setUpLatestJobRecyclerview(listed_companies_recyclerview, latestList);
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

    void setUpLatestJobRecyclerview(RecyclerView recyclerView, List<ApplicantHomeJobDataModel> latestList) {
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

        }
    });

}