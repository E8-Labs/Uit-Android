package com.antizon.uit_android.uit_admin.welcome;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.antizon.uit_android.R;
import com.antizon.uit_android.activities.chat.MessagesActivity;
import com.antizon.uit_android.company.utility.BaseActivity;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.models.admin.GetAdminApplicantsResponseModel;
import com.antizon.uit_android.models.applicant.GenericApplicantDataModel;
import com.antizon.uit_android.network.GetDataService;
import com.antizon.uit_android.network.RetrofitClientInstance;
import com.antizon.uit_android.recruiter.welcome.CompanyTeamMemberProfileActivity;
import com.antizon.uit_android.uit_admin.adapters.AdminAllCompanyMembersAdapter;
import com.antizon.uit_android.utilities.CustomCookieToast;
import com.antizon.uit_android.utilities.SimpleScrollListener;
import com.antizon.uit_android.utilities.Utilities;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminAllCompanyMembersActivity extends BaseActivity implements AdminAllCompanyMembersAdapter.AdminAllCompanyMembersAdapterCallback {
    Context context;
    GetDataService service;
    SessionManagement sessionManagement;
    ProgressDialog progressDialog;

    ImageView profilePicture;
    TextView btnFlaggedApplicants;
    RecyclerView recyclerView_allMembers;
    List<GenericApplicantDataModel> membersList, newMembersList;
    AdminAllCompanyMembersAdapter adminAllCompanyMembersAdapter;
    EditText edSearch;
    RelativeLayout layout_noApplicants;

    private boolean isLoading, isLastPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_company_admin_members);
        Utilities.setCustomStatusAndNavColor(AdminAllCompanyMembersActivity.this, R.color.white_dash, R.color.white_dash);
        context = AdminAllCompanyMembersActivity.this;
        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        sessionManagement = new SessionManagement(context);
        progressDialog = new ProgressDialog(context);

        profilePicture = findViewById(R.id.profilePicture);
        edSearch = findViewById(R.id.edSearch);
        btnFlaggedApplicants = findViewById(R.id.btnFlaggedApplicants);
        recyclerView_allMembers = findViewById(R.id.recyclerView_allMembers);
        layout_noApplicants = findViewById(R.id.layout_noApplicants);

        Utilities.loadCircleImage(AdminAllCompanyMembersActivity.this, sessionManagement.getProfileImage(), profilePicture);
        progressDialog.setMessage("Loading.....");
        progressDialog.show();
        requestForAllApplicants("Bearer " + sessionManagement.getToken());

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

        btnFlaggedApplicants.setOnClickListener(v -> {
            Intent intent = new Intent(AdminAllCompanyMembersActivity.this, FlaggedApplicantsActivity.class);
            intent.putExtra("role", "4");
            startActivity(intent);
            overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
        });

    }

    private void filter(String text) {
        ArrayList<GenericApplicantDataModel> filteredList = new ArrayList<>();
        for (GenericApplicantDataModel item : membersList) {
            if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }

        if (filteredList.size() == 0){
            recyclerView_allMembers.setVisibility(View.GONE);
            layout_noApplicants.setVisibility(View.VISIBLE);
        }else {
            recyclerView_allMembers.setVisibility(View.VISIBLE);
            layout_noApplicants.setVisibility(View.GONE);
            adminAllCompanyMembersAdapter.filterList(filteredList);
        }
    }


    public void requestForAllApplicants(String token){
        Call<GetAdminApplicantsResponseModel> call = service.getAdminAllCompanyMembersList(token, "0");
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<GetAdminApplicantsResponseModel> call, @NonNull Response<GetAdminApplicantsResponseModel> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    boolean status = response.body().isStatus();
                    if (status) {
                        membersList = new ArrayList<>();
                        newMembersList = new ArrayList<>();

                        membersList = response.body().getApplicantsList();

                        recyclerView_allMembers.setVisibility(View.VISIBLE);
                        layout_noApplicants.setVisibility(View.GONE);

                        showCompaniesRecyclerView(recyclerView_allMembers, membersList);

                        if (membersList != null){
                            if (membersList.size() >= 15) {
                                isLastPage = false;
                            } else {
                                isLastPage = true;
                                isLoading = false;
                            }
                        }
                    }
                } else {
                    CustomCookieToast.showFailureToast(AdminAllCompanyMembersActivity.this, response.message());
                }

            }

            @Override
            public void onFailure(@NonNull Call<GetAdminApplicantsResponseModel> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                CustomCookieToast.showFailureToast(AdminAllCompanyMembersActivity.this, t.getMessage());
            }
        });
    }


    private void showCompaniesRecyclerView(RecyclerView recyclerView, List<GenericApplicantDataModel> membersList){
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        adminAllCompanyMembersAdapter = new AdminAllCompanyMembersAdapter(context, membersList, this);
        recyclerView.setAdapter(adminAllCompanyMembersAdapter);

        recyclerView.addOnScrollListener(new SimpleScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                new Handler(Looper.myLooper()).postDelayed(() -> {
                    if (!isLastPage) {
                        loadMoreItemsForPagination(adminAllCompanyMembersAdapter);
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


    private void loadMoreItemsForPagination(AdminAllCompanyMembersAdapter adminAllCompanyMembersAdapter) {
        Call<GetAdminApplicantsResponseModel> call = service.getAdminAllCompanyMembersList("Bearer " + sessionManagement.getToken(), String.valueOf(membersList.size()));
        call.enqueue(new Callback<>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NotNull Call<GetAdminApplicantsResponseModel> call, @NotNull Response<GetAdminApplicantsResponseModel> response) {
                isLoading = false;
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    boolean status = response.body().isStatus();
                    if (status) {

                        newMembersList = new ArrayList<>();

                        newMembersList = response.body().getApplicantsList();
                        membersList.addAll(newMembersList);

                        if (membersList.size() >= 15) {
                            isLastPage = false;
                        } else {
                            isLastPage = true;
                            isLoading = false;
                        }

                        adminAllCompanyMembersAdapter.notifyDataSetChanged();

                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<GetAdminApplicantsResponseModel> call, @NotNull Throwable t) {
                isLoading = false;
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(context, CompanyTeamMemberProfileActivity.class);
        intent.putExtra("applicantDataModel", membersList.get(position));
        startActivity(intent);
        overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
    }

    @Override
    public void onMessageClick(int position) {
        Intent intent = new Intent(context, MessagesActivity.class);
        intent.putExtra("CHAT_ID", sessionManagement.getKeyId()+"-"+membersList.get(position).getUser_id());
        intent.putExtra("second_user_id", String.valueOf(membersList.get(position).getUser_id()));
        intent.putExtra("second_user_picture", membersList.get(position).getProfile_image());
        intent.putExtra("second_user_name", membersList.get(position).getName());
        startActivity(intent);
        overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
    }
}
