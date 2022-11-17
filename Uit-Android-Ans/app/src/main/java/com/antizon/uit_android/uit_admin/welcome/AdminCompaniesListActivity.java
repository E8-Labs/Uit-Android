package com.antizon.uit_android.uit_admin.welcome;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.antizon.uit_android.R;
import com.antizon.uit_android.activities.chat.MessagesActivity;
import com.antizon.uit_android.company.activities.UitCompanyProfileActivity;
import com.antizon.uit_android.company.utility.BaseActivity;
import com.antizon.uit_android.generic.model.ModelRecruiterFindCompany;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.models.community.MainResponseModel;
import com.antizon.uit_android.network.GetDataService;
import com.antizon.uit_android.network.RetrofitClientInstance;
import com.antizon.uit_android.recruiter.models.GetCompaniesResponseModel;
import com.antizon.uit_android.uit_admin.adapters.AdminCompaniesAdapter;
import com.antizon.uit_android.utilities.CustomCookieToast;
import com.antizon.uit_android.utilities.SimpleScrollListener;
import com.antizon.uit_android.utilities.Utilities;
import com.google.android.material.tabs.TabLayout;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AdminCompaniesListActivity extends BaseActivity implements AdminCompaniesAdapter.AdminCompaniesAdapterCallback {
    Context context;
    GetDataService service;
    SessionManagement sessionManagement;
    ProgressDialog progressDialog;

    TabLayout tab_layout;
    RecyclerView recyclerview_companies;
    List<ModelRecruiterFindCompany> companiesList, newCompaniesList;
    AdminCompaniesAdapter adminCompaniesAdapter;
    EditText edSearch;
    RelativeLayout layout_noApplicants;

    int accountStatus = 4, detailPosition;
    AlertDialog deleteDialog;

    private boolean isLoading, isLastPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_companies_list);
        Utilities.setCustomStatusAndNavColor(AdminCompaniesListActivity.this, R.color.white_dash, R.color.white_dash);
        context = AdminCompaniesListActivity.this;
        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        sessionManagement = new SessionManagement(context);
        progressDialog = new ProgressDialog(context);

        edSearch = findViewById(R.id.edSearch);
        tab_layout = findViewById(R.id.tab_layout);
        recyclerview_companies = findViewById(R.id.recyclerview_companies);
        layout_noApplicants = findViewById(R.id.layout_noApplicants);

        tab_layout.addTab(tab_layout.newTab().setText("Pending"));
        tab_layout.addTab(tab_layout.newTab().setText("Approved"));

        if (sessionManagement.getRole().equals("1")){
            tab_layout.addTab(tab_layout.newTab().setText("Paused"));
        }


        setMarginBetweenTabs(tab_layout);

        progressDialog.setMessage("Loading.....");
        progressDialog.show();
        requestForCompanyList("4");

        tab_layout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                progressDialog.show();
                if (position == 0){
                    accountStatus = 4;
                    requestForCompanyList("4");
                }else if (position == 1){
                    accountStatus = 1;
                    requestForCompanyList("1");
                }else{
                    accountStatus = 5;
                    requestForCompanyList("5");
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        edSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });
    }

    private void filter(String text) {
        ArrayList<ModelRecruiterFindCompany> filteredList = new ArrayList<>();
        for (ModelRecruiterFindCompany item : companiesList) {
            if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }

        if (filteredList.size() == 0){
            recyclerview_companies.setVisibility(View.GONE);
            layout_noApplicants.setVisibility(View.VISIBLE);
        }else {
            recyclerview_companies.setVisibility(View.VISIBLE);
            layout_noApplicants.setVisibility(View.GONE);
            adminCompaniesAdapter.filterList(filteredList);
        }

    }

    public void requestForCompanyList(String account_status){
        Call<GetCompaniesResponseModel> call = service.getAdminCompanies(account_status, "0");
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<GetCompaniesResponseModel> call, @NonNull Response<GetCompaniesResponseModel> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    boolean status = response.body().isStatus();
                    if (status) {
                        companiesList = new ArrayList<>();
                        newCompaniesList = new ArrayList<>();

                        companiesList = response.body().getCompanyList();

                        recyclerview_companies.setVisibility(View.VISIBLE);
                        layout_noApplicants.setVisibility(View.GONE);
                        showCompaniesRecyclerView(recyclerview_companies, companiesList, accountStatus);

                        if (companiesList != null){
                            if (companiesList.size() >= 15) {
                                isLastPage = false;
                            } else {
                                isLastPage = true;
                                isLoading = false;
                            }
                        }
                    }
                } else {
                    CustomCookieToast.showFailureToast(AdminCompaniesListActivity.this, response.message());
                }

            }

            @Override
            public void onFailure(@NonNull Call<GetCompaniesResponseModel> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                CustomCookieToast.showFailureToast(AdminCompaniesListActivity.this, t.getMessage());
            }
        });
    }


    private void showCompaniesRecyclerView(RecyclerView recyclerView, List<ModelRecruiterFindCompany> companiesList, int accountStatus){
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        adminCompaniesAdapter = new AdminCompaniesAdapter(context, companiesList, accountStatus, this);
        recyclerView.setAdapter(adminCompaniesAdapter);

        recyclerView.addOnScrollListener(new SimpleScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                if (!isLastPage) {
                    loadMoreItemsForPagination(adminCompaniesAdapter);
                }
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


    private void loadMoreItemsForPagination(AdminCompaniesAdapter adminCompaniesAdapter) {
        Call<GetCompaniesResponseModel> call = service.getAdminCompanies(accountStatus+"", String.valueOf(companiesList.size()));
        call.enqueue(new Callback<>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NotNull Call<GetCompaniesResponseModel> call, @NotNull Response<GetCompaniesResponseModel> response) {
                isLoading = false;
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    boolean status = response.body().isStatus();
                    if (status) {

                        newCompaniesList = new ArrayList<>();

                        newCompaniesList = response.body().getCompanyList();
                        companiesList.addAll(newCompaniesList);

                        if (newCompaniesList.size() >= 15) {
                            isLastPage = false;
                        } else {
                            isLastPage = true;
                            isLoading = false;
                        }

                        adminCompaniesAdapter.notifyDataSetChanged();

                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<GetCompaniesResponseModel> call, @NotNull Throwable t) {
                isLoading = false;
            }
        });

    }

    private void setMarginBetweenTabs(TabLayout tabLayout) {
        ViewGroup tabs = (ViewGroup) tabLayout.getChildAt(0);
        for (int i = 0; i < tabs.getChildCount() - 1; i++) {
            View tab = tabs.getChildAt(i);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) tab.getLayoutParams();
            layoutParams.setMarginEnd(50);
            tab.setLayoutParams(layoutParams);
            tabLayout.requestLayout();
        }
    }

    @Override
    public void onItemClick(int position) {
        detailPosition = position;
        Intent intent = new Intent(context, UitCompanyProfileActivity.class);
        intent.putExtra("visitProfileId", String.valueOf(companiesList.get(position).getUser_id()));
        intent.putExtra("accountStatus", accountStatus);
        onProfileUpdateLauncher.launch(intent);
        overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
    }

    @Override
    public void onAcceptClicked(int position) {
        showAcceptRejectCompanyRequestPopup(position, String.valueOf(companiesList.get(position).getUser_id()), "approve");
    }

    @Override
    public void onDeclineClicked(int position) {
        showAcceptRejectCompanyRequestPopup(position, String.valueOf(companiesList.get(position).getUser_id()), "decline");
    }

    @Override
    public void onMessageClicked(int position) {
        Intent intent = new Intent(context, MessagesActivity.class);
        intent.putExtra("CHAT_ID", sessionManagement.getKeyId()+"-"+companiesList.get(position).getUser_id());
        intent.putExtra("second_user_id", String.valueOf(companiesList.get(position).getUser_id()));
        intent.putExtra("second_user_picture", companiesList.get(position).getProfile_image());
        intent.putExtra("second_user_name", companiesList.get(position).getName());
        startActivity(intent);
        overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
    }


    @SuppressLint("NotifyDataSetChanged")
    private void showAcceptRejectCompanyRequestPopup(int position, String companyId, String type) {

        AlertDialog.Builder builder = new AlertDialog.Builder(AdminCompaniesListActivity.this, R.style.CustomDialog);
        final View customLayout = LayoutInflater.from(AdminCompaniesListActivity.this).inflate(R.layout.popup_logout_user, null);
        builder.setView(customLayout);

        TextView text_sure = customLayout.findViewById(R.id.text_sure);
        TextView text_blockThisPerson = customLayout.findViewById(R.id.text_blockThisPerson);
        TextView btn_yes = customLayout.findViewById(R.id.text_Yes);
        TextView btn_no = customLayout.findViewById(R.id.text_No);

        String headerText, areYouSure;
        String noText = "No";
        String yesText = "Yes";

        if (type.equals("approve")){
            headerText =   "Approve Company";
            areYouSure = "Are you sure you want to approve this company";
        }else {
            headerText =  "Decline Company";
            areYouSure = "Are you sure you want to decline this company";
        }

        text_sure.setText(areYouSure);
        text_blockThisPerson.setText(headerText);

        btn_no.setText(noText);
        btn_yes.setText(yesText);


        btn_no.setOnClickListener(view -> deleteDialog.dismiss());

        btn_yes.setOnClickListener(view -> {
            deleteDialog.dismiss();
            requestForApproveCompany("Bearer " + sessionManagement.getToken(), companyId, type);
            companiesList.remove(position);
            adminCompaniesAdapter.notifyDataSetChanged();
        });

        deleteDialog = builder.create();
        deleteDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        deleteDialog.show();
        deleteDialog.setCancelable(false);
    }


    private void requestForApproveCompany(String authToken, String companyId, String type) {
        Call<MainResponseModel> call;
        if (type.equals("approve")){
            call = service.approveCompany(authToken, companyId);
        }else {
            call = service.rejectCompany(authToken, companyId);
        }
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<MainResponseModel> call, @NonNull Response<MainResponseModel> response) {
                if (response.isSuccessful()){
                    if (response.body()!=null){
                        boolean status = response.body().isStatus();
                        if (!status){
                            CustomCookieToast.showFailureToast(AdminCompaniesListActivity.this, response.body().getMessage());
                        }
                    }else {
                        CustomCookieToast.showFailureToast(AdminCompaniesListActivity.this, response.message());
                    }
                }else {
                    CustomCookieToast.showFailureToast(AdminCompaniesListActivity.this, response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<MainResponseModel> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                CustomCookieToast.showFailureToast(AdminCompaniesListActivity.this, t.getLocalizedMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }

    @SuppressLint("NotifyDataSetChanged")
    ActivityResultLauncher<Intent> onProfileUpdateLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            companiesList.remove(detailPosition);
            adminCompaniesAdapter.notifyDataSetChanged();
        }
    });
}
