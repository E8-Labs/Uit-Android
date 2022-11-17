package com.antizon.uit_android.applicant.welcome;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.antizon.uit_android.R;
import com.antizon.uit_android.adapters.applicant.ApplicantSelectDegreeAdapter;
import com.antizon.uit_android.models.applicant.degree.ApplicantDegreeDataModel;
import com.antizon.uit_android.models.applicant.degree.ApplicantDegreesListResponseModel;
import com.antizon.uit_android.network.GetDataService;
import com.antizon.uit_android.network.RetrofitClientInstance;
import com.antizon.uit_android.utilities.Utilities;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApplicantSelectLanguageActivity extends AppCompatActivity implements ApplicantSelectDegreeAdapter.ApplicantSelectDegreeAdapterCallBack {
    Context context;
    GetDataService service;

    RelativeLayout btnBack;
    RecyclerView recyclerview_languages;
    List<ApplicantDegreeDataModel> degreesList;
    ApplicantSelectDegreeAdapter applicantSelectDegreeAdapter;

    ProgressDialog progressDialog;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applicant_select_language);
        Utilities.setCustomStatusAndNavColor(ApplicantSelectLanguageActivity.this, R.color.white_dash, R.color.white_dash);
        context = ApplicantSelectLanguageActivity.this;
        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        progressDialog = new ProgressDialog(ApplicantSelectLanguageActivity.this);

        btnBack = findViewById(R.id.btnBack);
        recyclerview_languages = findViewById(R.id.recyclerview_languages);

        btnBack.setOnClickListener(v -> onBackPressed());

        progressDialog.setMessage("Loading...");
        progressDialog.show();
        requestForGetDegreesList();
    }

    private void requestForGetDegreesList() {
        Call<ApplicantDegreesListResponseModel> call = service.getLanguagesList();
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NotNull Call<ApplicantDegreesListResponseModel> call, @NotNull Response<ApplicantDegreesListResponseModel> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    if (response.body()!=null){
                        if (response.body().isStatus()){
                            degreesList = new ArrayList<>();
                            degreesList = response.body().getApplicantDegreesList();
                            setDegreesToRecyclerView(recyclerview_languages, degreesList);
                        }else {
                            Toast.makeText(context, "Unsuccessful : " + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }

                } else {
                    Toast.makeText(context, "Unsuccessful : " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<ApplicantDegreesListResponseModel> call, @NotNull Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(context, "Failure : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setDegreesToRecyclerView(RecyclerView recyclerView, List<ApplicantDegreeDataModel> degreesList) {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context,LinearLayoutManager.VERTICAL , false);
        recyclerView.setLayoutManager(layoutManager);
        applicantSelectDegreeAdapter = new ApplicantSelectDegreeAdapter(context, degreesList, this);
        recyclerView.setAdapter(applicantSelectDegreeAdapter);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_out_up, R.anim.slide_bottom);
    }

    @Override
    public void onItemClick(ApplicantDegreeDataModel degreeDataModel) {
        Intent intent = new Intent();
        intent.putExtra("degreeDataModel", degreeDataModel);
        setResult(RESULT_OK, intent);
        finish();
        overridePendingTransition(R.anim.slide_out_up, R.anim.slide_bottom);
    }
}