package com.antizon.uit_android.uit_admin.welcome;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.antizon.uit_android.R;
import com.antizon.uit_android.activities.chat.MessagesActivity;
import com.antizon.uit_android.applicant.activities.ApplicantProfileActivity;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.models.admin.GetAdminApplicantsResponseModel;
import com.antizon.uit_android.models.applicant.GenericApplicantDataModel;
import com.antizon.uit_android.network.GetDataService;
import com.antizon.uit_android.network.RetrofitClientInstance;
import com.antizon.uit_android.uit_admin.adapters.AdminApplicantsAdapter;
import com.antizon.uit_android.utilities.CustomCookieToast;
import com.antizon.uit_android.utilities.SimpleScrollListener;
import com.antizon.uit_android.utilities.Utilities;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminApplicantsListActivity extends AppCompatActivity implements AdminApplicantsAdapter.AdminApplicantsAdapterCallback {
    Context context;
    GetDataService service;
    SessionManagement sessionManagement;
    ProgressDialog progressDialog;


    TextView btnFlaggedApplicants;
    RecyclerView recyclerView_applicants;
    List<GenericApplicantDataModel> applicantsList, newApplicantsList;
    AdminApplicantsAdapter adminApplicantsAdapter;
    EditText edSearch;
    RelativeLayout layout_noApplicants;

    private boolean isLoading, isLastPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_all_aplicants_list);
        Utilities.setCustomStatusAndNavColor(AdminApplicantsListActivity.this, R.color.white_dash, R.color.white_dash);
        context = AdminApplicantsListActivity.this;
        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        sessionManagement = new SessionManagement(context);
        progressDialog = new ProgressDialog(context);

        edSearch = findViewById(R.id.edSearch);
        btnFlaggedApplicants = findViewById(R.id.btnFlaggedApplicants);
        recyclerView_applicants = findViewById(R.id.recyclerView_applicants);
        layout_noApplicants = findViewById(R.id.layout_noApplicants);

        btnFlaggedApplicants.setOnClickListener(v -> {
            Intent intent = new Intent(AdminApplicantsListActivity.this, FlaggedApplicantsActivity.class);
            intent.putExtra("role", "5");
            startActivity(intent);
            overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
        });

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
    }

    private void filter(String text) {
        ArrayList<GenericApplicantDataModel> filteredList = new ArrayList<>();
        for (GenericApplicantDataModel item : applicantsList) {
            if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }

        if (filteredList.size() == 0){
            recyclerView_applicants.setVisibility(View.GONE);
            layout_noApplicants.setVisibility(View.VISIBLE);
        }else {
            recyclerView_applicants.setVisibility(View.VISIBLE);
            layout_noApplicants.setVisibility(View.GONE);
            adminApplicantsAdapter.filterList(filteredList);
        }
    }

    public void requestForAllApplicants(String token){
        Call<GetAdminApplicantsResponseModel> call = service.getAdminApplicantsList(token, "0");
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<GetAdminApplicantsResponseModel> call, @NonNull Response<GetAdminApplicantsResponseModel> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    boolean status = response.body().isStatus();
                    if (status) {
                        applicantsList = new ArrayList<>();
                        newApplicantsList = new ArrayList<>();
                        applicantsList = response.body().getApplicantsList();


                        recyclerView_applicants.setVisibility(View.VISIBLE);
                        layout_noApplicants.setVisibility(View.GONE);

                        showAllApplicantsRecyclerView(recyclerView_applicants, applicantsList);

                        if (applicantsList != null){
                            if (applicantsList.size() >= 15) {
                                isLastPage = false;
                            } else {
                                isLastPage = true;
                                isLoading = false;
                            }
                        }
                    }
                } else {
                    CustomCookieToast.showFailureToast(AdminApplicantsListActivity.this, response.message());
                }

            }

            @Override
            public void onFailure(@NonNull Call<GetAdminApplicantsResponseModel> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                CustomCookieToast.showFailureToast(AdminApplicantsListActivity.this, t.getMessage());
            }
        });
    }

    private void showAllApplicantsRecyclerView(RecyclerView recyclerView, List<GenericApplicantDataModel> applicantsList){
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        adminApplicantsAdapter = new AdminApplicantsAdapter(context, applicantsList, this);
        recyclerView.setAdapter(adminApplicantsAdapter);

        recyclerView.addOnScrollListener(new SimpleScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                if (!isLastPage) {
                    loadMoreItemsForPagination(adminApplicantsAdapter);
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


    private void loadMoreItemsForPagination(AdminApplicantsAdapter adminApplicantsAdapter) {
        Call<GetAdminApplicantsResponseModel> call = service.getAdminApplicantsList("Bearer " + sessionManagement.getToken(), String.valueOf(applicantsList.size()));
        call.enqueue(new Callback<>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NotNull Call<GetAdminApplicantsResponseModel> call, @NotNull Response<GetAdminApplicantsResponseModel> response) {
                isLoading = false;
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    boolean status = response.body().isStatus();
                    if (status) {

                        newApplicantsList = new ArrayList<>();

                        newApplicantsList = response.body().getApplicantsList();
                        applicantsList.addAll(newApplicantsList);

                        if (newApplicantsList.size() >= 15) {
                            isLastPage = false;
                        } else {
                            isLastPage = true;
                            isLoading = false;
                        }

                        adminApplicantsAdapter.notifyDataSetChanged();

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
        GenericApplicantDataModel applicantDataModel = applicantsList.get(position);
        if (applicantDataModel !=null){
            Intent intent = new Intent(context, ApplicantProfileActivity.class);
            intent.putExtra("jobId", "");
            intent.putExtra("applicantId", String.valueOf(applicantDataModel.getUser_id()));
            intent.putExtra("applicationStatus", "");
            onApplicantJobStatusUpdated.launch(intent);
            overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
        }
    }

    @Override
    public void onMessageClick(int position) {
        Intent intent = new Intent(context, MessagesActivity.class);
        intent.putExtra("CHAT_ID", sessionManagement.getKeyId()+"-"+applicantsList.get(position).getUser_id());
        intent.putExtra("second_user_id", String.valueOf(applicantsList.get(position).getUser_id()));
        intent.putExtra("second_user_picture", applicantsList.get(position).getProfile_image());
        intent.putExtra("second_user_name", applicantsList.get(position).getName());
        startActivity(intent);
        overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
    }

    ActivityResultLauncher<Intent> onApplicantJobStatusUpdated = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
            overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
        }
    });
}