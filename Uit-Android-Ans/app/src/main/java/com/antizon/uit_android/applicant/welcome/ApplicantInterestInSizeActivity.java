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
import com.antizon.uit_android.utilities.CustomCookieToast;
import com.antizon.uit_android.utilities.Utilities;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApplicantInterestInSizeActivity extends AppCompatActivity implements CompanyStageAdapter.CompanyStageAdapterCallBack {
    Context context;
    GetDataService service;

    ImageView backIcon, redNoah2;
    TextView skip;
    ProgressDialog progressDialog;
    SessionManagement sessionManagement;
    RecyclerView companySizeRecyclerView;

    CompanyStageAdapter companyStageAdapter;
    ArrayList<ModelCompanySize> companyInterestInSizeList;

    ArrayList<ModelCompanySize> selectedCompanyInterestInSizeList;
    ArrayList<ModelCompanySize> selectedCompanyInterestInStageList;
    ArrayList<ModelApplicantJobs> locationList, interestedJobTypeList;
    ArrayList<ApplicantJobDataModel> selectedJobsList;
    ArrayList<ApplicantEducationDataModel> educationList;
    ArrayList<ApplicantDepartmentDataModel> selectedDepartmentList;

    String employeValue = "", from;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applicant_interestin_size);
        Utilities.setWhiteBars(ApplicantInterestInSizeActivity.this);
        context = ApplicantInterestInSizeActivity.this;
        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        progressDialog = new ProgressDialog(ApplicantInterestInSizeActivity.this);

        initViews();

    }

    private void initViews() {

        companySizeRecyclerView = findViewById(R.id.company_size_recyclerview);
        backIcon = findViewById(R.id.backIcon);
        skip = findViewById(R.id.skip);
        redNoah2 = findViewById(R.id.redNoah2);

        from = getIntent().getStringExtra("from");
        employeValue = getIntent().getStringExtra("employeStatus");
        educationList = getIntent().getParcelableArrayListExtra("educationList");
        selectedJobsList = getIntent().getParcelableArrayListExtra("selectedJobsList");
        selectedDepartmentList = getIntent().getParcelableArrayListExtra("selectedDepartmentList");
        locationList = getIntent().getParcelableArrayListExtra("locationList");
        interestedJobTypeList = getIntent().getParcelableArrayListExtra("interestedJobTypeList");
        selectedCompanyInterestInStageList = getIntent().getParcelableArrayListExtra("selectedCompanyInterestInStageList");

        sessionManagement = new SessionManagement(ApplicantInterestInSizeActivity.this);
        Utilities.loadImage(ApplicantInterestInSizeActivity.this, sessionManagement.getProfileImage(), redNoah2);

        backIcon.setOnClickListener(v -> onBackPressed());

        skip.setOnClickListener(v -> {
            selectedCompanyInterestInSizeList = new ArrayList<>();
            for (int i = 0; i <companyInterestInSizeList.size() ; i++) {
                if (companyInterestInSizeList.get(i).isSelected()){
                    selectedCompanyInterestInSizeList.add(companyInterestInSizeList.get(i));
                }
            }

//            if (selectedCompanyInterestInSizeList.size() == 0) {
//                CustomCookieToast.showRequiredToast(ApplicantInterestInSizeActivity.this, "Please select minimum one size");
//            }else {
//
//            }

            openNextScreen();

        });

        progressDialog.setMessage("Loading...");
        progressDialog.show();
        requestForGetApplicantInSize();
    }


    private void requestForGetApplicantInSize() {
        Call<ApplicantInStageResponseModel> call = service.getCompanySizesList();
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NotNull Call<ApplicantInStageResponseModel> call, @NotNull Response<ApplicantInStageResponseModel> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    if (response.body()!=null){
                        if (response.body().isStatus()){
                            companyInterestInSizeList = new ArrayList<>();
                            companyInterestInSizeList = response.body().getCompanySizesList();
                            setUpCompanySizeRecyclerView(companySizeRecyclerView, companyInterestInSizeList);
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

    private void setUpCompanySizeRecyclerView(RecyclerView recyclerView,  ArrayList<ModelCompanySize> companySizesList) {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context,LinearLayoutManager.VERTICAL , false);
        recyclerView.setLayoutManager(layoutManager);
        companyStageAdapter = new CompanyStageAdapter(context, companySizesList, this);
        recyclerView.setAdapter(companyStageAdapter);
    }

    private void openNextScreen() {
        Intent intent = new Intent(ApplicantInterestInSizeActivity.this, ApplicantProfessionalInterestActivity.class);
        intent.putExtra("from", from);
        intent.putExtra("employeStatus", employeValue);
        intent.putParcelableArrayListExtra("educationList", educationList);
        intent.putParcelableArrayListExtra("selectedJobsList", selectedJobsList);
        intent.putParcelableArrayListExtra("selectedDepartmentList", selectedDepartmentList);
        intent.putParcelableArrayListExtra("locationList", locationList);
        intent.putParcelableArrayListExtra("interestedJobTypeList", interestedJobTypeList);
        intent.putParcelableArrayListExtra("selectedCompanyInterestInStageList", selectedCompanyInterestInStageList);
        intent.putParcelableArrayListExtra("selectedCompanyInterestInSizeList", selectedCompanyInterestInSizeList);
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

        boolean isSelected = false;
        for (ModelCompanySize companySize : companyInterestInSizeList){
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

        companyInterestInSizeList.get(position).setSelected(!companyInterestInSizeList.get(position).isSelected());
        companyStageAdapter.notifyDataSetChanged();
    }
}