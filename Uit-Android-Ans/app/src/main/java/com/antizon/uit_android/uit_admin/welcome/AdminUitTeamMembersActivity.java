package com.antizon.uit_android.uit_admin.welcome;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.antizon.uit_android.R;
import com.antizon.uit_android.activities.chat.MessagesActivity;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.models.admin.GetAdminApplicantsResponseModel;
import com.antizon.uit_android.models.applicant.GenericApplicantDataModel;
import com.antizon.uit_android.models.community.MainResponseModel;
import com.antizon.uit_android.network.GetDataService;
import com.antizon.uit_android.network.RetrofitClientInstance;
import com.antizon.uit_android.uit_admin.adapters.AdminUitMembersAdapter;
import com.antizon.uit_android.uit_members.welcome.UitTeamMemberProfileActivity;
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

public class AdminUitTeamMembersActivity extends AppCompatActivity implements AdminUitMembersAdapter.AdminUitMembersAdapterCallBack {
    Context context;
    GetDataService service;
    SessionManagement sessionManagement;
    ProgressDialog progressDialog;

    TabLayout tab_layout;
    RecyclerView recyclerview_uitTeamMembers;
    List<GenericApplicantDataModel> uitMembersList, newUitMembersList;
    AdminUitMembersAdapter adminUitMembersAdapter;
    EditText edSearch;
    RelativeLayout layout_noApplicants;

    int accountStatus = 4;
    AlertDialog deleteDialog;

    private boolean isLoading, isLastPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_uit_team_members);
        Utilities.setCustomStatusAndNavColor(AdminUitTeamMembersActivity.this, R.color.white_dash, R.color.white_dash);

        context = AdminUitTeamMembersActivity.this;
        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        sessionManagement = new SessionManagement(context);
        progressDialog = new ProgressDialog(context);

        edSearch = findViewById(R.id.edSearch);
        layout_noApplicants = findViewById(R.id.layout_noApplicants);
        tab_layout = findViewById(R.id.tab_layout);
        recyclerview_uitTeamMembers = findViewById(R.id.recyclerview_uitTeamMembers);

        tab_layout.addTab(tab_layout.newTab().setText("Approved"));
        tab_layout.addTab(tab_layout.newTab().setText("Pending"), true);


        setMarginBetweenTabs(tab_layout);

        progressDialog.setMessage("Loading.....");
        progressDialog.show();
        requestForUitMembersList("Bearer " + sessionManagement.getToken(), "4");

        tab_layout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                progressDialog.show();
                if (position == 0){
                    accountStatus = 1;
                    requestForUitMembersList("Bearer " + sessionManagement.getToken(),"1");
                }else if (position == 1){
                    accountStatus = 4;
                    requestForUitMembersList("Bearer " + sessionManagement.getToken(),"4");
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
        ArrayList<GenericApplicantDataModel> filteredList = new ArrayList<>();
        for (GenericApplicantDataModel item : uitMembersList) {
            if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }

        if (filteredList.size() == 0){
            recyclerview_uitTeamMembers.setVisibility(View.GONE);
            layout_noApplicants.setVisibility(View.VISIBLE);
        }else {
            recyclerview_uitTeamMembers.setVisibility(View.VISIBLE);
            layout_noApplicants.setVisibility(View.GONE);
            adminUitMembersAdapter.filterList(filteredList);
        }
    }

    public void requestForUitMembersList(String authToken, String account_status){
        Call<GetAdminApplicantsResponseModel> call = service.getUitMembersList(authToken, account_status, "0");

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<GetAdminApplicantsResponseModel> call, @NonNull Response<GetAdminApplicantsResponseModel> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    boolean status = response.body().isStatus();
                    if (status) {
                        uitMembersList = new ArrayList<>();
                        newUitMembersList = new ArrayList<>();

                        uitMembersList = response.body().getApplicantsList();

                        recyclerview_uitTeamMembers.setVisibility(View.VISIBLE);
                        layout_noApplicants.setVisibility(View.GONE);

                        showUitMembersRecyclerView(recyclerview_uitTeamMembers, uitMembersList, accountStatus);

                        if (uitMembersList != null){
                            if (uitMembersList.size() >= 15) {
                                isLastPage = false;
                            } else {
                                isLastPage = true;
                                isLoading = false;
                            }
                        }
                    }
                } else {
                    CustomCookieToast.showFailureToast(AdminUitTeamMembersActivity.this, response.message());
                }

            }

            @Override
            public void onFailure(@NonNull Call<GetAdminApplicantsResponseModel> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                CustomCookieToast.showFailureToast(AdminUitTeamMembersActivity.this, t.getMessage());
            }
        });
    }

    private void showUitMembersRecyclerView(RecyclerView recyclerView, List<GenericApplicantDataModel> uitMembersList, int accountStatus){
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        adminUitMembersAdapter = new AdminUitMembersAdapter(context, uitMembersList, accountStatus, this);
        recyclerView.setAdapter(adminUitMembersAdapter);


        recyclerView.addOnScrollListener(new SimpleScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                if (!isLastPage) {
                    loadMoreItemsForPagination(adminUitMembersAdapter);
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


    private void loadMoreItemsForPagination(AdminUitMembersAdapter adminUitMembersAdapter) {
        Call<GetAdminApplicantsResponseModel> call = service.getUitMembersList("Bearer " + sessionManagement.getToken(), accountStatus+"",String.valueOf(uitMembersList.size()));

        call.enqueue(new Callback<>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NotNull Call<GetAdminApplicantsResponseModel> call, @NotNull Response<GetAdminApplicantsResponseModel> response) {
                isLoading = false;
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    boolean status = response.body().isStatus();
                    if (status) {

                        newUitMembersList = new ArrayList<>();

                        newUitMembersList = response.body().getApplicantsList();

                        uitMembersList.addAll(newUitMembersList);

                        if (newUitMembersList.size() >= 15) {
                            isLastPage = false;
                        } else {
                            isLastPage = true;
                            isLoading = false;
                        }

                        adminUitMembersAdapter.notifyDataSetChanged();

                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<GetAdminApplicantsResponseModel> call, @NotNull Throwable t) {
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
        GenericApplicantDataModel applicantDataModel = uitMembersList.get(position);
        if (applicantDataModel !=null){
            Intent intent = new Intent(context, UitTeamMemberProfileActivity.class);
            intent.putExtra("applicantDataModel", applicantDataModel);
            startActivity(intent);
            overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
        }
    }

    @Override
    public void onAcceptClicked(int position) {
        showAcceptRejectCompanyRequestPopup(position, String.valueOf(uitMembersList.get(position).getUser_id()), "approve");
    }

    @Override
    public void onDeclineClicked(int position) {
        showAcceptRejectCompanyRequestPopup(position, String.valueOf(uitMembersList.get(position).getUser_id()), "decline");
    }

    @Override
    public void onMessageClicked(int position) {
        Intent intent = new Intent(context, MessagesActivity.class);
        intent.putExtra("CHAT_ID", sessionManagement.getKeyId()+"-"+uitMembersList.get(position).getUser_id());
        intent.putExtra("second_user_id", String.valueOf(uitMembersList.get(position).getUser_id()));
        intent.putExtra("second_user_picture", uitMembersList.get(position).getProfile_image());
        intent.putExtra("second_user_name", uitMembersList.get(position).getName());
        startActivity(intent);
        overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
    }


    @SuppressLint("NotifyDataSetChanged")
    private void showAcceptRejectCompanyRequestPopup(int position, String companyId, String type) {

        AlertDialog.Builder builder = new AlertDialog.Builder(AdminUitTeamMembersActivity.this, R.style.CustomDialog);
        final View customLayout = LayoutInflater.from(AdminUitTeamMembersActivity.this).inflate(R.layout.popup_logout_user, null);
        builder.setView(customLayout);

        TextView text_sure = customLayout.findViewById(R.id.text_sure);
        TextView text_blockThisPerson = customLayout.findViewById(R.id.text_blockThisPerson);
        TextView btn_yes = customLayout.findViewById(R.id.text_Yes);
        TextView btn_no = customLayout.findViewById(R.id.text_No);

        String headerText, areYouSure;
        String noText = "No";
        String yesText = "Yes";

        if (type.equals("approve")){
            headerText =   "Approve Uit Member";
            areYouSure = "Are you sure you want to approve this uit member";
        }else {
            headerText =  "Decline Uit Member";
            areYouSure = "Are you sure you want to decline this uit member";
        }

        text_sure.setText(areYouSure);
        text_blockThisPerson.setText(headerText);

        btn_no.setText(noText);
        btn_yes.setText(yesText);


        btn_no.setOnClickListener(view -> deleteDialog.dismiss());

        btn_yes.setOnClickListener(view -> {
            deleteDialog.dismiss();
            requestForApproveCompany("Bearer " + sessionManagement.getToken(), companyId, type);
            uitMembersList.remove(position);
            adminUitMembersAdapter.notifyDataSetChanged();
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
                            CustomCookieToast.showFailureToast(AdminUitTeamMembersActivity.this, response.body().getMessage());
                        }
                    }else {
                        CustomCookieToast.showFailureToast(AdminUitTeamMembersActivity.this, response.message());
                    }
                }else {
                    CustomCookieToast.showFailureToast(AdminUitTeamMembersActivity.this, response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<MainResponseModel> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                CustomCookieToast.showFailureToast(AdminUitTeamMembersActivity.this, t.getLocalizedMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }
}
