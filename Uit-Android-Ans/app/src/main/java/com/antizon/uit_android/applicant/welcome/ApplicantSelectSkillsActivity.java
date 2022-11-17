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
import com.antizon.uit_android.adapters.applicant.ApplicantSelectSkillAdapter;
import com.antizon.uit_android.models.applicant.skills.ApplicantSkillsResponseModel;
import com.antizon.uit_android.models.applicant.skills.SkillDataModel;
import com.antizon.uit_android.network.GetDataService;
import com.antizon.uit_android.network.RetrofitClientInstance;
import com.antizon.uit_android.utilities.Utilities;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApplicantSelectSkillsActivity extends AppCompatActivity implements ApplicantSelectSkillAdapter.ApplicantSelectSkillAdapterCallBack {
    Context context;
    GetDataService service;

    RelativeLayout btnBack;
    RecyclerView recyclerview_skills;
    List<SkillDataModel> skillsList;
    ApplicantSelectSkillAdapter applicantSelectSkillAdapter;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applicant_select_skills);

        Utilities.setCustomStatusAndNavColor(ApplicantSelectSkillsActivity.this, R.color.white_dash, R.color.white_dash);
        context = ApplicantSelectSkillsActivity.this;
        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        progressDialog = new ProgressDialog(ApplicantSelectSkillsActivity.this);

        btnBack = findViewById(R.id.btnBack);
        recyclerview_skills = findViewById(R.id.recyclerview_skills);

        btnBack.setOnClickListener(v -> onBackPressed());

        progressDialog.setMessage("Loading...");
        progressDialog.show();
        requestForGetSkillsList();
    }
    private void requestForGetSkillsList() {
        Call<ApplicantSkillsResponseModel> call = service.getApplicantSkills();
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NotNull Call<ApplicantSkillsResponseModel> call, @NotNull Response<ApplicantSkillsResponseModel> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    if (response.body()!=null){
                        if (response.body().isStatus()){
                            skillsList = new ArrayList<>();
                            skillsList = response.body().getSkillsList();
                            setSkillsToRecyclerView(recyclerview_skills, skillsList);
                        }else {
                            Toast.makeText(context, "Unsuccessful : " + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }

                } else {
                    Toast.makeText(context, "Unsuccessful : " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<ApplicantSkillsResponseModel> call, @NotNull Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(context, "Failure : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setSkillsToRecyclerView(RecyclerView recyclerView, List<SkillDataModel> skillsList) {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context,LinearLayoutManager.VERTICAL , false);
        recyclerView.setLayoutManager(layoutManager);
        applicantSelectSkillAdapter = new ApplicantSelectSkillAdapter(context, skillsList, this);
        recyclerView.setAdapter(applicantSelectSkillAdapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_out_up, R.anim.slide_bottom);
    }

    @Override
    public void onItemClick(SkillDataModel skillDataModel) {
        Intent intent = new Intent();
        intent.putExtra("skillDataModel", skillDataModel);
        setResult(RESULT_OK, intent);
        finish();
        overridePendingTransition(R.anim.slide_out_up, R.anim.slide_bottom);
    }
}