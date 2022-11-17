package com.antizon.uit_android.applicant.welcome;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.antizon.uit_android.R;
import com.antizon.uit_android.generic.adapter.ApplicantDepartmentAdapter;
import com.antizon.uit_android.generic.model.ApplicantEducationDataModel;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.models.applicant.department.ApplicantDepartmentDataModel;
import com.antizon.uit_android.models.applicant.department.ApplicantDepartmentsListResponseModel;
import com.antizon.uit_android.network.GetDataService;
import com.antizon.uit_android.network.RetrofitClientInstance;
import com.antizon.uit_android.utilities.CustomCookieToast;
import com.antizon.uit_android.utilities.Utilities;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApplicantDepartmentActivity extends AppCompatActivity implements ApplicantDepartmentAdapter.ApplicantDepartmentAdapterCallBack {
    Context context;
    GetDataService service;

    ImageView backIcon, menYellow;
    TextView next;
    String employeStatus = "", from;

    ProgressDialog progressDialog;
    SessionManagement sessionManagement;
    RecyclerView applicantDepartmentRecyclerview;

    ApplicantDepartmentAdapter applicantDepartmentAdapter;
    ArrayList<ApplicantDepartmentDataModel> departmentsList;
    ArrayList<ApplicantDepartmentDataModel> selectedDepartmentList;

    ArrayList<ApplicantEducationDataModel> educationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applicant_department);
        context = ApplicantDepartmentActivity.this;
        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        progressDialog = new ProgressDialog(ApplicantDepartmentActivity.this);
        Utilities.setWhiteBars(ApplicantDepartmentActivity.this);
        initViews();

        progressDialog.setMessage("Loading...");
        progressDialog.show();
        requestForGetApplicantDepartments();
    }


    void initViews() {
        applicantDepartmentRecyclerview = findViewById(R.id.applicant_department_recyclerview);
        backIcon = findViewById(R.id.backIcon);
        next = findViewById(R.id.next);
        menYellow = findViewById(R.id.menYellow);


        from = getIntent().getStringExtra("from");
        employeStatus = getIntent().getStringExtra("employeStatus");
        educationList = getIntent().getParcelableArrayListExtra("educationList");

        sessionManagement = new SessionManagement(ApplicantDepartmentActivity.this);
        Utilities.loadImage(context, sessionManagement.getProfileImage(), menYellow);

        backIcon.setOnClickListener(v -> onBackPressed());

        next.setOnClickListener(v -> {
            selectedDepartmentList = new ArrayList<>();
            for (int i = 0; i< departmentsList.size(); i++){
                if (departmentsList.get(i).isSelected()){
                    selectedDepartmentList.add(departmentsList.get(i));
                }
            }

            if (selectedDepartmentList.size() > 0) {
                moveToSelectApplicantJobs();
            }else {
                CustomCookieToast.showRequiredToast(ApplicantDepartmentActivity.this, "Please select minimum one Department.");
            }
        });

    }



    private void moveToSelectApplicantJobs() {
        Intent intent = new Intent(ApplicantDepartmentActivity.this, SelectApplicantJobsActivity.class);
        intent.putExtra("from", from);
        intent.putExtra("employeStatus", employeStatus);
        intent.putParcelableArrayListExtra("educationList", educationList);
        intent.putParcelableArrayListExtra("selectedDepartmentList", selectedDepartmentList);
        onProfileUpdateLauncher.launch(intent);
        overridePendingTransition(R.anim.left_in,R.anim.left_out);
    }


    private void requestForGetApplicantDepartments() {
        Call<ApplicantDepartmentsListResponseModel> call = service.getDepartmentsList();
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NotNull Call<ApplicantDepartmentsListResponseModel> call, @NotNull Response<ApplicantDepartmentsListResponseModel> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    if (response.body()!=null){
                        if (response.body().isStatus()){
                            departmentsList = new ArrayList<>();
                            departmentsList = response.body().getDepartmentsList();
                            setDepartmentsToRecyclerView(applicantDepartmentRecyclerview, departmentsList);
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
            public void onFailure(@NotNull Call<ApplicantDepartmentsListResponseModel> call, @NotNull Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(context, "Failure : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setDepartmentsToRecyclerView(RecyclerView recyclerView, List<ApplicantDepartmentDataModel> departmentsList) {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context,LinearLayoutManager.VERTICAL , false);
        recyclerView.setLayoutManager(layoutManager);
        applicantDepartmentAdapter = new ApplicantDepartmentAdapter(context, departmentsList, this);
        recyclerView.setAdapter(applicantDepartmentAdapter);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void selectedApplicantDepartment(boolean isChecked, int position) {
        departmentsList.get(position).setSelected(!departmentsList.get(position).isSelected());
        applicantDepartmentAdapter.notifyDataSetChanged();
    }

    ActivityResultLauncher<Intent> onProfileUpdateLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
            overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
        }
    });
}