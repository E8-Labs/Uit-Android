package com.antizon.uit_android.company.welcome;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.antizon.uit_android.R;
import com.antizon.uit_android.generic.adapter.CompanyStageAdapter;
import com.antizon.uit_android.generic.model.ModelCompanySize;
import com.antizon.uit_android.models.applicant.degree.ApplicantDegreeDataModel;
import com.antizon.uit_android.models.applicant.interest.ApplicantInStageResponseModel;
import com.antizon.uit_android.network.GetDataService;
import com.antizon.uit_android.network.RetrofitClientInstance;
import com.antizon.uit_android.utilities.Utilities;
import com.makeramen.roundedimageview.RoundedImageView;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompanyInSizeActivity extends AppCompatActivity implements CompanyStageAdapter.CompanyStageAdapterCallBack {
    Context context;
    GetDataService service;

    ImageView backIcon;
    RoundedImageView redNoah2;
    TextView skip;
    ProgressDialog progressDialog;
    RecyclerView companySizeRecyclerView;

    CompanyStageAdapter companySizeAdapter;
    ArrayList<ApplicantDegreeDataModel> selectedCompanyIndustries;
    ArrayList<ModelCompanySize> selectedCompanyInterestInStageList, companyInSizeList, selectedCompanyInSizeList;

    String companyBio = "", companyNameHintValue = "", encodedImageData = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_in_size);
        Utilities.setWhiteBars(CompanyInSizeActivity.this);
        context = CompanyInSizeActivity.this;
        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        progressDialog = new ProgressDialog(CompanyInSizeActivity.this);

        initViews();

        progressDialog.setMessage("Loading...");
        progressDialog.show();
        requestForGetApplicantInSize();
    }

    private void initViews() {
        selectedCompanyIndustries = new ArrayList<>();
        selectedCompanyInterestInStageList = new ArrayList<>();
        companyInSizeList = new ArrayList<>();
        selectedCompanyInSizeList = new ArrayList<>();


        companySizeRecyclerView = findViewById(R.id.company_size_recyclerview);
        backIcon = findViewById(R.id.backIcon);
        redNoah2 = findViewById(R.id.redNoah2);
        skip = findViewById(R.id.skip);

        if (getIntent() != null) {
            encodedImageData = getIntent().getStringExtra("profilePic");
            companyNameHintValue = getIntent().getStringExtra("companyName");
            companyBio = getIntent().getStringExtra("bio");
            selectedCompanyIndustries = getIntent().getParcelableArrayListExtra("selectedCompanyIndustries");
            selectedCompanyInterestInStageList = getIntent().getParcelableArrayListExtra("selectedCompanyInterestInStageList");
        }

        Utilities.loadImage(CompanyInSizeActivity.this, encodedImageData, redNoah2);

        backIcon.setOnClickListener(v -> onBackPressed());
        skip.setOnClickListener(v -> openNextScreen());

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
                            companyInSizeList = new ArrayList<>();
                            companyInSizeList = response.body().getCompanySizesList();
                            setUpCompanySizeRecyclerView(companySizeRecyclerView, companyInSizeList);
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

    private void setUpCompanySizeRecyclerView(RecyclerView recyclerView,  ArrayList<ModelCompanySize> companyStagesList) {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context,LinearLayoutManager.VERTICAL , false);
        recyclerView.setLayoutManager(layoutManager);
        companySizeAdapter = new CompanyStageAdapter(context, companyStagesList, this);
        recyclerView.setAdapter(companySizeAdapter);
    }


    private void openNextScreen() {
        selectedCompanyInSizeList = new ArrayList<>();
        for (int i = 0; i <companyInSizeList.size() ; i++) {
            if (companyInSizeList.get(i).isSelected()){
                selectedCompanyInSizeList.add(companyInSizeList.get(i));
            }
        }

        Intent intent = new Intent(CompanyInSizeActivity.this, CompanyHeadquatersActivity.class);
        intent.putExtra("profilePic", encodedImageData);
        intent.putExtra("companyName", companyNameHintValue);
        intent.putExtra("bio", companyBio);
        intent.putParcelableArrayListExtra("selectedCompanyIndustries", selectedCompanyIndustries);
        intent.putParcelableArrayListExtra("selectedCompanyInterestInStageList", selectedCompanyInterestInStageList);
        intent.putParcelableArrayListExtra("selectedCompanyInSizeList", selectedCompanyInSizeList);

        startActivity(intent);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onOnSelectCompanySizeListener(boolean isChecked, int position) {
        if (!isChecked){
            for (int i = 0; i < companyInSizeList.size(); i++) {
                companyInSizeList.get(i).setSelected(i == position);
            }
            companySizeAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }
}