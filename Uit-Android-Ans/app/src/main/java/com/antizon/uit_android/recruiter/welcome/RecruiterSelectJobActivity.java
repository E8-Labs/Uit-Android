package com.antizon.uit_android.recruiter.welcome;


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
import com.antizon.uit_android.R;
import com.antizon.uit_android.activities.jobs.ApplicantSelectJobActivity;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.models.applicant.jobs.ApplicantJobDataModel;
import com.antizon.uit_android.models.community.MainResponseModel;
import com.antizon.uit_android.network.GetDataService;
import com.antizon.uit_android.network.RetrofitClientInstance;
import com.antizon.uit_android.utilities.CustomCookieToast;
import com.antizon.uit_android.utilities.Utilities;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecruiterSelectJobActivity extends AppCompatActivity {
    Context context;
    SessionManagement sessionManagement;
    GetDataService service;
    ProgressDialog progressDialog;

    ImageView backIcon,menYellow;
    TextView next;
    TextView text_jobTitle;
    String selectedJobTitle = "", fullNameEditTextValue = "", encodedImageData = "", companyId = "", from = "";

    ApplicantJobDataModel recruiterJobDataModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recruiter_select_job);
        Utilities.setWhiteBars(RecruiterSelectJobActivity.this);
        context= RecruiterSelectJobActivity.this;
        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        progressDialog = new ProgressDialog(context);
        initViews();

    }

    private void initViews() {
        from = getIntent().getStringExtra("from");
        if (from.equals("add")){
            encodedImageData = getIntent().getStringExtra("profilePic");
            companyId = getIntent().getStringExtra("companyId");
            fullNameEditTextValue = getIntent().getStringExtra("fullName");
        }
        backIcon = findViewById(R.id.backIcon);
        next = findViewById(R.id.next);
        menYellow = findViewById(R.id.menYellow);
        text_jobTitle = findViewById(R.id.text_jobTitle);



        backIcon.setOnClickListener(v -> onBackPressed());

        next.setOnClickListener(v -> {
            if (selectedJobTitle.isEmpty()){
                CustomCookieToast.showRequiredToast(RecruiterSelectJobActivity.this, "Please select your job");
            }else {
                if (from.equals("edit")){
                    if (selectedJobTitle.equals(sessionManagement.getKeyJobTitle())){
                        finish();
                        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                    }else {
                        progressDialog.setMessage("Updating...");
                        progressDialog.show();
                        requestForUpdateRecruiterJobTitle("Bearer " + sessionManagement.getToken(), selectedJobTitle);
                    }
                }else {
                    openNextScreen();
                }

            }
        });

        text_jobTitle.setOnClickListener(v -> {
            Intent selectJobIntent = new Intent(context, ApplicantSelectJobActivity.class);
            onSelectedJobTitleLauncher.launch(selectJobIntent);
            overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
        });

        if (from.equals("add")){
            Utilities.loadCircleImage(RecruiterSelectJobActivity.this, encodedImageData, menYellow);
        }else {
            sessionManagement = new SessionManagement(context);
            Utilities.loadCircleImage(RecruiterSelectJobActivity.this, sessionManagement.getProfileImage(), menYellow);
            selectedJobTitle = sessionManagement.getKeyJobTitle();
            text_jobTitle.setText(selectedJobTitle);
        }
    }

    private void openNextScreen() {
        Intent intent = new Intent(context, RecruiterEmailAddressActivity.class);
        intent.putExtra("fullName", fullNameEditTextValue);
        intent.putExtra("companyId", companyId);
        intent.putExtra("profilePic", encodedImageData);
        intent.putExtra("recruiterJobDataModel", recruiterJobDataModel);
        intent.putExtra("from", from);
        startActivity(intent);
        overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
    }

    ActivityResultLauncher<Intent> onSelectedJobTitleLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent intent = result.getData();
            if (intent != null) {
                recruiterJobDataModel= intent.getParcelableExtra("applicantJobDataModel");
                selectedJobTitle = recruiterJobDataModel.getName();
                text_jobTitle.setText(selectedJobTitle);
            }
        }
    });

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }

    private void requestForUpdateRecruiterJobTitle(String authToken,String companyTeamMemberJobTitle) {
        Call<MainResponseModel> call = service.updateUserJobTitle(authToken, companyTeamMemberJobTitle);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NotNull Call<MainResponseModel> call, @NotNull Response<MainResponseModel> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().isStatus()) {
                        Intent intent = new Intent();
                        intent.putExtra("companyTeamMemberJobTitle", companyTeamMemberJobTitle);
                        setResult(RESULT_OK, intent);
                        finish();
                        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                    }else {
                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(context, "un successful : " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<MainResponseModel> call, @NotNull Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(context, "failure : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}