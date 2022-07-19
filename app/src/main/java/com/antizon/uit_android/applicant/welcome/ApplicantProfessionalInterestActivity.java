package com.antizon.uit_android.applicant.welcome;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.antizon.uit_android.R;
import com.antizon.uit_android.company.utility.BaseActivity;
import com.antizon.uit_android.generic.adapter.CompanyStageAdapter;
import com.antizon.uit_android.generic.model.ApplicantEducationDataModel;
import com.antizon.uit_android.generic.model.ModelApplicantJobs;
import com.antizon.uit_android.generic.model.ModelCompanySize;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.models.applicant.department.ApplicantDepartmentDataModel;
import com.antizon.uit_android.models.applicant.interest.ApplicantInStageResponseModel;
import com.antizon.uit_android.models.applicant.jobs.ApplicantJobDataModel;
import com.antizon.uit_android.network.GetDataService;
import com.antizon.uit_android.network.RetrofitClientInstance;
import com.antizon.uit_android.utilities.CustomCookieToast;
import com.antizon.uit_android.utilities.Utilities;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ApplicantProfessionalInterestActivity extends BaseActivity implements CompanyStageAdapter.CompanyStageAdapterCallBack {
    Context context;
    GetDataService service;

    ProgressDialog progressDialog;
    TextView next;
    ImageView backIcon, redNoah2;
    RecyclerView applicantProfessionalInterestRecyclerview;
    SessionManagement sessionManagement;

    String employeValue = "";

    CompanyStageAdapter companyStageAdapter;
    ArrayList<ModelCompanySize> professionInterestList;

    ArrayList<ModelCompanySize> selectedProfessionalInterestList;
    ArrayList<ModelCompanySize> selectedCompanyInterestInSizeList;
    ArrayList<ModelCompanySize> selectedCompanyInterestInStageList;
    ArrayList<ModelApplicantJobs> locationList, interestedJobTypeList;
    ArrayList<ApplicantJobDataModel> selectedJobsList;
    ArrayList<ApplicantEducationDataModel> educationList;
    ArrayList<ApplicantDepartmentDataModel> selectedDepartmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professional_interest);
        Utilities.setWhiteBars(ApplicantProfessionalInterestActivity.this);

        context = ApplicantProfessionalInterestActivity.this;
        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        progressDialog = new ProgressDialog(ApplicantProfessionalInterestActivity.this);

        initViews();

        progressDialog.setMessage("Loading...");
        progressDialog.show();
        requestForGetApplicantProfessionalInterest();
    }

    void initViews() {

        applicantProfessionalInterestRecyclerview = findViewById(R.id.applicant_professional_interest_recyclerview);
        next = findViewById(R.id.next);
        backIcon = findViewById(R.id.backIcon);
        redNoah2 = findViewById(R.id.redNoah2);

        employeValue = getIntent().getStringExtra("employeStatus");

        educationList = getIntent().getParcelableArrayListExtra("educationList");
        selectedJobsList = getIntent().getParcelableArrayListExtra("selectedJobsList");
        selectedDepartmentList = getIntent().getParcelableArrayListExtra("selectedDepartmentList");
        locationList = getIntent().getParcelableArrayListExtra("locationList");
        interestedJobTypeList = getIntent().getParcelableArrayListExtra("interestedJobTypeList");
        selectedCompanyInterestInStageList = getIntent().getParcelableArrayListExtra("selectedCompanyInterestInStageList");
        selectedCompanyInterestInSizeList = getIntent().getParcelableArrayListExtra("selectedCompanyInterestInSizeList");

        sessionManagement = new SessionManagement(ApplicantProfessionalInterestActivity.this);
        loadProfile(ApplicantProfessionalInterestActivity.this, sessionManagement.getProfileImage(), redNoah2);

        next.setOnClickListener(v -> {
            selectedProfessionalInterestList = new ArrayList<>();
            for (int i = 0; i <professionInterestList.size() ; i++) {
                if (professionInterestList.get(i).isSelected()){
                    selectedProfessionalInterestList.add(professionInterestList.get(i));
                }
            }

            if (selectedProfessionalInterestList.size() == 0) {
                CustomCookieToast.showRequiredToast(ApplicantProfessionalInterestActivity.this, "Please select minimum one professional interest");
            }else {
                openNextScreen();
            }
        });

        backIcon.setOnClickListener(v -> onBackPressed());
    }

    private void requestForGetApplicantProfessionalInterest() {
        Call<ApplicantInStageResponseModel> call = service.getProfessionInterestsList();
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NotNull Call<ApplicantInStageResponseModel> call, @NotNull Response<ApplicantInStageResponseModel> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    if (response.body()!=null){
                        if (response.body().isStatus()){
                            professionInterestList = new ArrayList<>();
                            professionInterestList = response.body().getCompanySizesList();
                            setApplicantProfessionalInterestRecyclerView(applicantProfessionalInterestRecyclerview, professionInterestList);
                        }
                        else {
                            Toast.makeText(context, "Unsuccessful : " + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                } else {
                    Toast.makeText(context, "Unsuccessful : " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<ApplicantInStageResponseModel> call, @NotNull Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(context, "Failure : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    void setApplicantProfessionalInterestRecyclerView(RecyclerView recyclerView,  ArrayList<ModelCompanySize> companySizesList) {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context,LinearLayoutManager.VERTICAL , false);
        recyclerView.setLayoutManager(layoutManager);
        companyStageAdapter = new CompanyStageAdapter(context, companySizesList, this);
        recyclerView.setAdapter(companyStageAdapter);
    }

    void openNextScreen() {
        Intent intent = new Intent(ApplicantProfessionalInterestActivity.this, ApplicantLocationPermissionActivity.class);
        intent.putExtra("employeStatus", employeValue);
        intent.putParcelableArrayListExtra("educationList", educationList);
        intent.putParcelableArrayListExtra("selectedJobsList", selectedJobsList);
        intent.putParcelableArrayListExtra("selectedDepartmentList", selectedDepartmentList);
        intent.putParcelableArrayListExtra("locationList", locationList);
        intent.putParcelableArrayListExtra("interestedJobTypeList", interestedJobTypeList);
        intent.putParcelableArrayListExtra("selectedCompanyInterestInStageList", selectedCompanyInterestInStageList);
        intent.putParcelableArrayListExtra("selectedCompanyInterestInSizeList", selectedCompanyInterestInSizeList);
        intent.putParcelableArrayListExtra("selectedProfessionalInterestList", selectedProfessionalInterestList);
        onProfileUpdateLauncher.launch(intent);
        overridePendingTransition(R.anim.left_in,R.anim.left_out);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }

    ActivityResultLauncher<Intent> onProfileUpdateLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
            overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
        }
    });

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onOnSelectCompanySizeListener(boolean isChecked, int position) {
        professionInterestList.get(position).setSelected(!professionInterestList.get(position).isSelected());
        companyStageAdapter.notifyDataSetChanged();
    }
}