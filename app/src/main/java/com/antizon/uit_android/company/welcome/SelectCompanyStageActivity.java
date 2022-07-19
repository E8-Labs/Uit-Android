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

public class SelectCompanyStageActivity extends AppCompatActivity implements CompanyStageAdapter.CompanyStageAdapterCallBack {
    Context context;
    GetDataService service;

    ImageView backIcon;
    RoundedImageView redNoah2;
    TextView skip;
    ProgressDialog progressDialog;

    RecyclerView companyStageRecyclerView;
    CompanyStageAdapter companyStageAdapter;
    ArrayList<ModelCompanySize> companyStagesList;

    String typeHereValue = "", companyNameHintValue = "", encodedImageData = "";

    ArrayList<ModelCompanySize> selectedCompanyInterestInStageList;
    ArrayList<ApplicantDegreeDataModel> selectedCompanyIndustries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stage);
        Utilities.setWhiteBars(SelectCompanyStageActivity.this);

        context = SelectCompanyStageActivity.this;
        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        progressDialog = new ProgressDialog(SelectCompanyStageActivity.this);

        initViews();

        progressDialog.setMessage("Loading...");
        progressDialog.show();
        requestForGetApplicantInStage();
    }
    void initViews() {

        selectedCompanyIndustries = new ArrayList<>();
        selectedCompanyInterestInStageList = new ArrayList<>();
        companyStagesList = new ArrayList<>();

        skip = findViewById(R.id.skip);
        redNoah2 = findViewById(R.id.redNoah2);
        backIcon = findViewById(R.id.backIcon);
        companyStageRecyclerView = findViewById(R.id.company_stage_recyclerview);

        if (getIntent() != null) {
            encodedImageData = getIntent().getStringExtra("profilePic");
            companyNameHintValue = getIntent().getStringExtra("companyName");
            typeHereValue = getIntent().getStringExtra("bio");
            selectedCompanyIndustries = getIntent().getParcelableArrayListExtra("selectedCompanyIndustries");
        }
        Utilities.loadImage(SelectCompanyStageActivity.this, encodedImageData, redNoah2);

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

    private void setUpCompanyStageRecyclerView(RecyclerView recyclerView,  ArrayList<ModelCompanySize> companyStagesList) {
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

        Intent intent = new Intent(SelectCompanyStageActivity.this, CompanyInSizeActivity.class);
        intent.putExtra("profilePic", encodedImageData);
        intent.putExtra("companyName", companyNameHintValue);
        intent.putExtra("bio", typeHereValue);
        intent.putParcelableArrayListExtra("selectedCompanyIndustries", selectedCompanyIndustries);
        intent.putParcelableArrayListExtra("selectedCompanyInterestInStageList", selectedCompanyInterestInStageList);
        startActivity(intent);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onOnSelectCompanySizeListener(boolean isChecked, int position) {
        if (!isChecked){
            for (int i = 0; i < companyStagesList.size(); i++) {
                companyStagesList.get(i).setSelected(i == position);
            }
            companyStageAdapter.notifyDataSetChanged();
        }

    }
}