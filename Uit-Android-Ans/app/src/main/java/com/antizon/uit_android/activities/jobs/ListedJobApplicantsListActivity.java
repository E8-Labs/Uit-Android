package com.antizon.uit_android.activities.jobs;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import com.antizon.uit_android.R;
import com.antizon.uit_android.activities.chat.MessagesActivity;
import com.antizon.uit_android.adapters.applicant.CompanyHiredApplicantAdapter;
import com.antizon.uit_android.adapters.jobs.CompanyAppliedJobApplicantAdapter;
import com.antizon.uit_android.adapters.jobs.JobTagAdapter;
import com.antizon.uit_android.applicant.activities.ApplicantProfileActivity;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.models.AppliedJobApplicantDataModel;
import com.antizon.uit_android.models.AppliedJobApplicantsResponseModel;
import com.antizon.uit_android.models.applicant.GenericApplicantDataModel;
import com.antizon.uit_android.models.jobs.JobCompanyDataModel;
import com.antizon.uit_android.models.jobs.JobDetailsDataModel;
import com.antizon.uit_android.models.jobs.TagModel;
import com.antizon.uit_android.network.GetDataService;
import com.antizon.uit_android.network.RetrofitClientInstance;
import com.antizon.uit_android.utilities.ArfiDeveloperTime;
import com.antizon.uit_android.utilities.CustomCookieToast;
import com.antizon.uit_android.utilities.Utilities;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListedJobApplicantsListActivity extends AppCompatActivity implements CompanyHiredApplicantAdapter.HiredApplicantAdapterCallBack, CompanyAppliedJobApplicantAdapter.CompanyAppliedJobApplicantAdapterCallBack {
    Context context;
    GetDataService service;
    SessionManagement sessionManagement;

    ImageView ivClose, ivProfile;
    TextView tvTitle, tvCompany, tvAddress, postedByName, postedByJobTitle, tvTime;

    RecyclerView recyclerview_allApplicants, recyclerViewTags;
    List<AppliedJobApplicantDataModel> appliedJobApplicantsList;
    CompanyAppliedJobApplicantAdapter companyAppliedJobApplicantAdapter;

    JobDetailsDataModel jobDataModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listed_job_applicants_list);

        Utilities.setCustomStatusAndNavColor(ListedJobApplicantsListActivity.this, R.color.white, R.color.white_dash);
        context = ListedJobApplicantsListActivity.this;
        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        sessionManagement = new SessionManagement(context);

        jobDataModel = getIntent().getParcelableExtra("jobDataModel");

        ivClose = findViewById(R.id.iv_close);
        ivProfile = findViewById(R.id.iv_profile);
        tvTitle = findViewById(R.id.tv_description);
        tvAddress = findViewById(R.id.tv_address);
        tvCompany = findViewById(R.id.tv_company);
        postedByName = findViewById(R.id.postedByName);
        postedByJobTitle = findViewById(R.id.postedByJobTitle);
        tvTime = findViewById(R.id.tv_time);
        recyclerViewTags = findViewById(R.id.rv_tags);
        recyclerview_allApplicants = findViewById(R.id.recyclerview_allApplicants);

        showFilledJobDetail();

        requestForAppliedJob("Bearer " + sessionManagement.getToken(), jobDataModel.getId() + "");

        ivClose.setOnClickListener(v -> onBackPressed());
    }

    public void requestForAppliedJob(String authToken, String jobId){
        Call<AppliedJobApplicantsResponseModel> call  = service.getFilledJobApplicantsList(authToken, jobId);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<AppliedJobApplicantsResponseModel> call, @NonNull Response<AppliedJobApplicantsResponseModel> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    boolean status = response.body().isStatus();
                    if (status) {
                        appliedJobApplicantsList = new ArrayList<>();
                        appliedJobApplicantsList = response.body().getApplicantsLists();
                        showFilledApplicantsRecyclerView(recyclerview_allApplicants, appliedJobApplicantsList);
                    }
                } else {
                    CustomCookieToast.showFailureToast(ListedJobApplicantsListActivity.this, response.message());
                }

            }

            @Override
            public void onFailure(@NonNull Call<AppliedJobApplicantsResponseModel> call, @NonNull Throwable t) {
                CustomCookieToast.showFailureToast(ListedJobApplicantsListActivity.this, t.getMessage());
            }
        });
    }

    private void showFilledJobDetail() {
        final GenericApplicantDataModel user = jobDataModel.getUser();
        final JobCompanyDataModel company = jobDataModel.getCompany();
        if (company != null) {
            String companyLocation =  company.getCity() + ", " + company.getState();
            Utilities.loadImage(context, company.getProfileImage(), ivProfile);
            tvCompany.setText(company.getName());
            tvAddress.setText(companyLocation);

        }
        if (user != null) {
            String postedBy = "Posted by " + user.getName();
            postedByName.setText(postedBy);
            postedByJobTitle.setText(user.getJob_title());
        }

        tvTitle.setText(jobDataModel.getJobTitle());
        tvTime.setText(new ArfiDeveloperTime().getPrettyTimeFromCreatedAt(jobDataModel.getCreatedAt()));

        // set tags
        final List<TagModel> tags = new ArrayList<>();
        // salary range
        tags.add(new TagModel("$" + Utilities.getShortAmount(jobDataModel.getMinSalary()) + " - $" + Utilities.getShortAmount(jobDataModel.getMaxSalary())));
        // location status
        tags.add(new TagModel(Utilities.getLocationStatus(jobDataModel.getLocationStatus())));
        // Employment type
        tags.add(new TagModel(Utilities.getEmploymentType(jobDataModel.getEmploymentType())));

        recyclerViewTags.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        JobTagAdapter jobTagAdapter = new JobTagAdapter(context, tags);
        recyclerViewTags.setAdapter(jobTagAdapter);

    }

    private void showFilledApplicantsRecyclerView(RecyclerView recyclerView, List<AppliedJobApplicantDataModel> uitMembersList){
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        companyAppliedJobApplicantAdapter = new CompanyAppliedJobApplicantAdapter(context, uitMembersList, this);
        recyclerView.setAdapter(companyAppliedJobApplicantAdapter);
    }

    @Override
    public void onItemClick(int position) {
        GenericApplicantDataModel applicantDataModel = appliedJobApplicantsList.get(position).getGenericApplicantDataModel();
        if (applicantDataModel !=null){
            Intent intent = new Intent(context, ApplicantProfileActivity.class);
            intent.putExtra("jobId", String.valueOf(jobDataModel.getId()));
            intent.putExtra("applicantId", String.valueOf(applicantDataModel.getUser_id()));
            intent.putExtra("applicationStatus", String.valueOf(appliedJobApplicantsList.get(position).getApplication_status()));
            onApplicantJobStatusUpdated.launch(intent);
            overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
        }
    }

    @Override
    public void onMessageClicked(int position) {
        GenericApplicantDataModel applicantDataModel = appliedJobApplicantsList.get(position).getGenericApplicantDataModel();
        if (applicantDataModel !=null){
            Intent intent = new Intent(context, MessagesActivity.class);
            intent.putExtra("CHAT_ID", sessionManagement.getKeyId()+"-"+applicantDataModel.getUser_id());
            intent.putExtra("second_user_id", String.valueOf(applicantDataModel.getUser_id()));
            intent.putExtra("second_user_picture", applicantDataModel.getProfile_image());
            intent.putExtra("second_user_name", applicantDataModel.getName());
            startActivity(intent);
            overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
        }
       
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }

    ActivityResultLauncher<Intent> onApplicantJobStatusUpdated = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
            overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
        }
    });
}