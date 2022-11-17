package com.antizon.uit_android.recruiter.welcome;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.antizon.uit_android.R;
import com.antizon.uit_android.adapters.company.CompanyProfileIndustriesAdapter;
import com.antizon.uit_android.company.utility.BaseActivity;
import com.antizon.uit_android.company.welcome.CompanyValuePropActivity;
import com.antizon.uit_android.recruiter.adapters.RecruiterFindCompanyAdapter;
import com.antizon.uit_android.generic.model.ModelRecruiterFindCompany;
import com.antizon.uit_android.models.profile.NameIdModel;
import com.antizon.uit_android.network.GetDataService;
import com.antizon.uit_android.network.RetrofitClientInstance;
import com.antizon.uit_android.recruiter.models.GetCompaniesResponseModel;
import com.antizon.uit_android.utilities.SimpleScrollListener;
import com.antizon.uit_android.utilities.Utilities;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.makeramen.roundedimageview.RoundedImageView;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecruiterFindCompanyActivity extends BaseActivity implements RecruiterFindCompanyAdapter.RecruiterFindCompanyAdapterCallBack {
    Context context;
    GetDataService service;

    ProgressDialog progressDialog;
    RecyclerView recyclerview_companies;
    RecruiterFindCompanyAdapter recruiterFindCompanyAdapter;
    List<ModelRecruiterFindCompany> companyList, newCompanyList, searchedCompaniesList;

    private boolean isLoading, isLastPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_company);
        Utilities.setCustomStatusAndNavColor(RecruiterFindCompanyActivity.this, R.color.white_dash, R.color.white_dash);
        context = RecruiterFindCompanyActivity.this;
        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        progressDialog = new ProgressDialog(RecruiterFindCompanyActivity.this);

        initViews();

    }

    private void initViews() {
        recyclerview_companies = findViewById(R.id.recyclerview_companies);
        progressDialog.setMessage("Loading....");
        progressDialog.show();
        requestForFindCompanies();
    }

    private void requestForFindCompanies() {
        Call<GetCompaniesResponseModel> call = service.getCompaniesList("0");
        call.enqueue(new Callback<>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NotNull Call<GetCompaniesResponseModel> call, @NotNull Response<GetCompaniesResponseModel> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    if (response.body()!=null){
                        if (response.body().isStatus()){
                            companyList = new ArrayList<>();
                            newCompanyList = new ArrayList<>();

                            companyList = response.body().getCompanyList();
                            showCompaniesListRecyclerView(recyclerview_companies, companyList);

                            if (companyList != null){
                                if (companyList.size() >= 15) {
                                    isLastPage = false;
                                } else {
                                    isLastPage = true;
                                    isLoading = false;
                                }

                                recruiterFindCompanyAdapter.notifyDataSetChanged();
                            }
                        }else {
                            Toast.makeText(context, "Unsuccessful : " + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }

                } else {
                    Toast.makeText(context, "Unsuccessful : " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<GetCompaniesResponseModel> call, @NotNull Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(context, "Failure : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showCompaniesListRecyclerView(RecyclerView recyclerView, List<ModelRecruiterFindCompany> companyList) {
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context,LinearLayoutManager.VERTICAL , false);
        recyclerView.setLayoutManager(layoutManager);
        recruiterFindCompanyAdapter = new RecruiterFindCompanyAdapter(context, companyList, this);
        recyclerView.setAdapter(recruiterFindCompanyAdapter);

        recyclerView.addOnScrollListener(new SimpleScrollListener(layoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                new Handler(Looper.myLooper()).postDelayed(() -> {
                    if (!isLastPage) {
                        loadMoreItemsForPagination(recruiterFindCompanyAdapter);
                    }
                }, 400);
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

        });
    }

    private void loadMoreItemsForPagination(RecruiterFindCompanyAdapter findCompanyAdapter) {
        Call<GetCompaniesResponseModel> call = service.getCompaniesList(String.valueOf(companyList.size()));
        call.enqueue(new Callback<>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NotNull Call<GetCompaniesResponseModel> call, @NotNull Response<GetCompaniesResponseModel> response) {
                isLoading = false;
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    boolean status = response.body().isStatus();
                    if (status) {

                        newCompanyList = new ArrayList<>();

                        newCompanyList = response.body().getCompanyList();
                        companyList.addAll(newCompanyList);

                        if (newCompanyList.size() >= 15) {
                            isLastPage = false;
                        } else {
                            isLastPage = true;
                            isLoading = false;
                        }

                        findCompanyAdapter.notifyDataSetChanged();

                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<GetCompaniesResponseModel> call, @NotNull Throwable t) {
                isLoading = false;
            }
        });

    }

    @Override
    public void onItemClicked(int position) {
        showSendCompanyRequest(position);
    }

    @Override
    public void onSearchFilter(String text) {
        if (text.isEmpty()){
            showCompaniesListRecyclerView(recyclerview_companies, companyList);
        }else {
            searchForCompaniesList(text);
        }
    }


    private void searchForCompaniesList(String searchText) {
        Call<GetCompaniesResponseModel> call = service.searchCompaniesList(searchText);
        call.enqueue(new Callback<>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NotNull Call<GetCompaniesResponseModel> call, @NotNull Response<GetCompaniesResponseModel> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    if (response.body()!=null){
                        if (response.body().isStatus()){
                            searchedCompaniesList = new ArrayList<>();

                            searchedCompaniesList = response.body().getCompanyList();

                            recruiterFindCompanyAdapter.filterList(searchedCompaniesList);

                        }
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<GetCompaniesResponseModel> call, @NotNull Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

    @Override
    public void onCreateCompanyProfileClicked() {
        Intent intent = new Intent(RecruiterFindCompanyActivity.this, CompanyValuePropActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }


    private void showSendCompanyRequest(int position) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context, R.style.BottomSheetTheme);
        View sheetView = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_find_company, findViewById(R.id.bottom_sheet_find_company));

        RoundedImageView company_profileImage = sheetView.findViewById(R.id.company_profileImage);
        TextView text_companyName = sheetView.findViewById(R.id.text_companyName);
        TextView text_companyLocation = sheetView.findViewById(R.id.text_companyLocation);
        TextView btn_SendRequest = sheetView.findViewById(R.id.btn_SendRequest);
        RecyclerView recyclerview_industries = sheetView.findViewById(R.id.recyclerview_industries);

        if (companyList.get(position) != null){
            String location;
            if (!companyList.get(position).getCity().isEmpty()){
                location = companyList.get(position).getCity() + " , " + companyList.get(position).getState();
            }else {
                location = companyList.get(position).getState();
            }

            Utilities.loadImage(context, companyList.get(position).getProfile_image(), company_profileImage);
            text_companyName.setText(companyList.get(position).getName());
            text_companyLocation.setText(location);

            btn_SendRequest.setOnClickListener(v -> {
                bottomSheetDialog.dismiss();
                Intent recruiterProfileIntent = new Intent(context, RecruiterProfilePicActivity.class);
                recruiterProfileIntent.putExtra("companyId", String.valueOf(companyList.get(position).getUser_id()));
                startActivity(recruiterProfileIntent);
                overridePendingTransition(R.anim.right_to_left,R.anim.left_to_right);

            });

            if (companyList.get(position).getIndustriesList() !=null){
                showIndustriesRecyclerView(recyclerview_industries, companyList.get(position).getIndustriesList());
            }
        }
        bottomSheetDialog.setContentView(sheetView);
        bottomSheetDialog.show();
        bottomSheetDialog.setDismissWithAnimation(true);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }

    private void showIndustriesRecyclerView(RecyclerView recyclerView, List<NameIdModel> industriesList){
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        CompanyProfileIndustriesAdapter profileIndustriesAdapter = new CompanyProfileIndustriesAdapter(context, industriesList);
        recyclerView.setAdapter(profileIndustriesAdapter);
    }

}