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
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.models.applicant.degree.ApplicantDegreeDataModel;
import com.antizon.uit_android.models.applicant.interest.ApplicantInStageResponseModel;
import com.antizon.uit_android.models.community.MainResponseModel;
import com.antizon.uit_android.models.profile.NameIdModel;
import com.antizon.uit_android.network.GetDataService;
import com.antizon.uit_android.network.RetrofitClientInstance;
import com.antizon.uit_android.utilities.CustomCookieToast;
import com.antizon.uit_android.utilities.Utilities;
import com.makeramen.roundedimageview.RoundedImageView;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectCompanyStageActivity extends AppCompatActivity implements CompanyStageAdapter.CompanyStageAdapterCallBack {
    Context context;
    GetDataService service;
    SessionManagement sessionManagement;
    ImageView backIcon;
    RoundedImageView redNoah2;
    TextView skip;
    ProgressDialog progressDialog;

    RecyclerView companyStageRecyclerView;
    CompanyStageAdapter companyStageAdapter;
    ArrayList<ModelCompanySize> companyStagesList;

    String typeHereValue = "", companyNameHintValue = "", encodedImageData = "", from;

    ArrayList<ModelCompanySize> selectedCompanyInterestInStageList;
    ArrayList<ApplicantDegreeDataModel> selectedCompanyIndustries;

    NameIdModel companyInStageModel;
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
    private void initViews() {
        selectedCompanyIndustries = new ArrayList<>();
        selectedCompanyInterestInStageList = new ArrayList<>();
        companyStagesList = new ArrayList<>();

        if (getIntent() != null) {
            from = getIntent().getStringExtra("from");
            if (from.equals("add")){
                encodedImageData = getIntent().getStringExtra("profilePic");
                companyNameHintValue = getIntent().getStringExtra("companyName");
                typeHereValue = getIntent().getStringExtra("bio");
                selectedCompanyIndustries = getIntent().getParcelableArrayListExtra("selectedCompanyIndustries");
            }else {
                companyInStageModel = getIntent().getParcelableExtra("companyInStageModel");
            }
        }

        skip = findViewById(R.id.skip);
        redNoah2 = findViewById(R.id.redNoah2);
        backIcon = findViewById(R.id.backIcon);
        companyStageRecyclerView = findViewById(R.id.company_stage_recyclerview);

        skip.setText(R.string.text_next);

        if (from.equals("edit")){
            sessionManagement = new SessionManagement(context);
            Utilities.loadImage(context, sessionManagement.getProfileImage(), redNoah2);

        }else {
            Utilities.loadImage(context, encodedImageData, redNoah2);
        }


        backIcon.setOnClickListener(v -> onBackPressed());

    }


    private void requestForGetApplicantInStage() {
        Call<ApplicantInStageResponseModel> call = service.getCompanyStagesList();
        call.enqueue(new Callback<>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NotNull Call<ApplicantInStageResponseModel> call, @NotNull Response<ApplicantInStageResponseModel> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    if (response.body()!=null){
                        if (response.body().isStatus()){
                            companyStagesList = new ArrayList<>();
                            companyStagesList = response.body().getCompanySizesList();
                            setUpCompanyStageRecyclerView(companyStageRecyclerView, companyStagesList);

                            if (from.equals("edit")){
                                for (int i = 0; i <companyStagesList.size() ; i++) {
                                    if (companyInStageModel.getId() == companyStagesList.get(i).getId()){
                                        companyStagesList.get(i).setSelected(true);
                                        companyStageAdapter.notifyDataSetChanged();
                                        break;
                                    }
                                }
                            }

                            skip.setOnClickListener(v -> openNextScreen());

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
        intent.putExtra("from", from);
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

            if (from.equals("edit")){
                progressDialog.setMessage("Updating...");
                progressDialog.show();
                requestForUpdateCompanyStage("Bearer " + sessionManagement.getToken(), companyStagesList);
            }else {
                openNextScreen();
            }

        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }

    private void requestForUpdateCompanyStage(String authToken, List<ModelCompanySize> companyStagesList){

        List<String> selectedStages = new ArrayList<>();
        for (int i = 0; i< companyStagesList.size() ; i++){
            if (companyStagesList.get(i).isSelected()){
                companyInStageModel = new NameIdModel(companyStagesList.get(i).getId(), companyStagesList.get(i).getName());
                selectedStages.add(String.valueOf(companyStagesList.get(i).getId()));
                break;
            }
        }

        Call<MainResponseModel> call = service.updateCompanyStage(authToken, selectedStages);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NotNull Call<MainResponseModel> call, @NotNull Response<MainResponseModel> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()){
                    assert response.body() != null;
                    boolean status = response.body().isStatus();
                    if (status) {
                        Intent intent = new Intent();
                        intent.putExtra("companyInStageModel", companyInStageModel);
                        setResult(RESULT_OK, intent);
                        finish();
                        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                    } else {
                        CustomCookieToast.showFailureToast(SelectCompanyStageActivity.this, "Error!", response.body().getMessage());
                    }
                }else {
                    CustomCookieToast.showFailureToast(SelectCompanyStageActivity.this, "Error!", response.message());
                }
            }

            @Override
            public void onFailure(@NotNull Call<MainResponseModel> call, @NotNull Throwable t) {
                progressDialog.dismiss();
                CustomCookieToast.showFailureToast(SelectCompanyStageActivity.this, "Error!", t.getMessage());
            }
        });
    }

}