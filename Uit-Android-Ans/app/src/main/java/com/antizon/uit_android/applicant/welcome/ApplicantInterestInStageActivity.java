package com.antizon.uit_android.applicant.welcome;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.antizon.uit_android.R;
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
import com.antizon.uit_android.utilities.Utilities;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApplicantInterestInStageActivity extends AppCompatActivity implements CompanyStageAdapter.CompanyStageAdapterCallBack {
    Context context;
    GetDataService service;

    ImageView backIcon, redNoah2;
    TextView skip;
    ProgressDialog progressDialog;
    SessionManagement sessionManagement;
    RecyclerView companyStageRecyclerView;
    CompanyStageAdapter companyStageAdapter;
    ArrayList<ModelCompanySize> companyStagesList;

    String employeValue = "", from;

    ArrayList<ModelCompanySize> selectedCompanyInterestInStageList;
    ArrayList<ModelApplicantJobs> locationList, interestedJobTypeList;
    ArrayList<ApplicantJobDataModel> selectedJobsList;
    ArrayList<ApplicantEducationDataModel> educationList;
    ArrayList<ApplicantDepartmentDataModel> selectedDepartmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utilities.setWhiteBars(ApplicantInterestInStageActivity.this);
        setContentView(R.layout.activity_applicant_interestin_stage);
        context = ApplicantInterestInStageActivity.this;
        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        progressDialog = new ProgressDialog(ApplicantInterestInStageActivity.this);

        initViews();

        progressDialog.setMessage("Loading...");
        progressDialog.show();
        requestForGetApplicantInStage();
    }


    private void initViews() {

        skip = findViewById(R.id.skip);
        backIcon = findViewById(R.id.backIcon);
        companyStageRecyclerView = findViewById(R.id.company_stage_recyclerview);
        redNoah2 = findViewById(R.id.redNoah2);

        from = getIntent().getStringExtra("from");
        employeValue = getIntent().getStringExtra("employeStatus");
        educationList = getIntent().getParcelableArrayListExtra("educationList");
        selectedJobsList = getIntent().getParcelableArrayListExtra("selectedJobsList");
        selectedDepartmentList = getIntent().getParcelableArrayListExtra("selectedDepartmentList");
        locationList = getIntent().getParcelableArrayListExtra("locationList");
        interestedJobTypeList = getIntent().getParcelableArrayListExtra("interestedJobTypeList");

        sessionManagement = new SessionManagement(ApplicantInterestInStageActivity.this);
        Utilities.loadImage(ApplicantInterestInStageActivity.this, sessionManagement.getProfileImage(), redNoah2);

        backIcon.setOnClickListener(v -> onBackPressed());
        skip.setOnClickListener(v -> openNextScreen());


    }

    private void requestForGetApplicantInStage() {
        Call<ApplicantInStageResponseModel> call = service.getCompanyStagesList();
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NotNull Call<ApplicantInStageResponseModel> call, @NotNull Response<ApplicantInStageResponseModel> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    if (response.body()!=null){
                        if (response.body().isStatus()){
                            companyStagesList = new ArrayList<>();
                            companyStagesList = response.body().getCompanySizesList();
                            setUpCompanyStageRecyclerView(companyStageRecyclerView, companyStagesList);
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

    void setUpCompanyStageRecyclerView(RecyclerView recyclerView,  ArrayList<ModelCompanySize> companyStagesList) {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context,LinearLayoutManager.VERTICAL , false);
        recyclerView.setLayoutManager(layoutManager);
        companyStageAdapter = new CompanyStageAdapter(context, companyStagesList, this);
        recyclerView.setAdapter(companyStageAdapter);
    }



    private void openNextScreen() {
        selectedCompanyInterestInStageList = new ArrayList<>();
        for (int i = 0; i <companyStagesList.size() ; i++) {
            if (companyStagesList.get(i).isSelected()){
                selectedCompanyInterestInStageList.add(companyStagesList.get(i));
            }
        }

        Intent intent = new Intent(ApplicantInterestInStageActivity.this, ApplicantInterestInSizeActivity.class);
        intent.putExtra("from", from);
        intent.putExtra("employeStatus", employeValue);
        intent.putParcelableArrayListExtra("educationList", educationList);
        intent.putParcelableArrayListExtra("selectedJobsList", selectedJobsList);
        intent.putParcelableArrayListExtra("selectedDepartmentList", selectedDepartmentList);
        intent.putParcelableArrayListExtra("locationList", locationList);
        intent.putParcelableArrayListExtra("interestedJobTypeList", interestedJobTypeList);
        intent.putParcelableArrayListExtra("selectedCompanyInterestInStageList", selectedCompanyInterestInStageList);
        onProfileUpdateLauncher.launch(intent);
        overridePendingTransition(R.anim.left_in,R.anim.left_out);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onOnSelectCompanySizeListener(boolean isChecked, int position) {
        boolean isSelected = false;
        for (ModelCompanySize companySize : companyStagesList){
            if (companySize.isSelected()){
                isSelected = true;
                break;
            }
        }
        if (isSelected){
            skip.setText(R.string.text_next);
        }else {
            skip.setText(R.string.text_skip);
        }

        companyStagesList.get(position).setSelected(!companyStagesList.get(position).isSelected());
        companyStageAdapter.notifyDataSetChanged();
    }

    ActivityResultLauncher<Intent> onProfileUpdateLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
            overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
        }
    });
}