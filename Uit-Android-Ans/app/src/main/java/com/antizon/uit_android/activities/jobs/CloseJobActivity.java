package com.antizon.uit_android.activities.jobs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.antizon.uit_android.R;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.models.community.MainResponseModel;
import com.antizon.uit_android.network.GetDataService;
import com.antizon.uit_android.network.RetrofitClientInstance;
import com.antizon.uit_android.utilities.CustomCookieToast;
import com.antizon.uit_android.utilities.Utilities;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CloseJobActivity extends AppCompatActivity {
    Context context;
    GetDataService service;
    SessionManagement sessionManagement;
    ProgressDialog progressDialog;

    RelativeLayout btnBack;
    EditText editTextReason;
    TextView btnCloseJob, text_jobTitle;
    String jobId, jobTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_close_job);
        Utilities.setWhiteBars(CloseJobActivity.this);
        context = CloseJobActivity.this;
        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        progressDialog = new ProgressDialog(context);
        sessionManagement = new SessionManagement(context);

        jobId = getIntent().getStringExtra("jobId");
        jobTitle = getIntent().getStringExtra("jobTitle");

        btnBack = findViewById(R.id.btnBack);
        text_jobTitle = findViewById(R.id.text_jobTitle);
        editTextReason = findViewById(R.id.editTextReason);
        btnCloseJob = findViewById(R.id.btnCloseJob);

        text_jobTitle.setText(jobTitle);
        btnCloseJob.setOnClickListener(v -> {
            String reason = editTextReason.getText().toString();
            if (reason.isEmpty()){
                CustomCookieToast.showRequiredToast(CloseJobActivity.this, "Please enter the job closing reason");
            }else {
                progressDialog.setMessage("Loading....");
                progressDialog.show();
                requestForCloseJob("Bearer " + sessionManagement.getToken(), jobId);
            }
        });

        btnBack.setOnClickListener(v -> onBackPressed());
    }

    private void requestForCloseJob(String authToken, String jobId) {
        Call<MainResponseModel> call = service.closeJob(authToken, jobId);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<MainResponseModel> call, @NonNull Response<MainResponseModel> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()){
                    if (response.body()!=null){
                        boolean status = response.body().isStatus();
                        if (status){
                            Intent intent = new Intent();
                            setResult(RESULT_OK, intent);
                            finish();
                            overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                        }else {
                            CustomCookieToast.showFailureToast(CloseJobActivity.this, response.body().getMessage());
                        }
                    }else {
                        CustomCookieToast.showFailureToast(CloseJobActivity.this, response.message());
                    }
                }else {
                    CustomCookieToast.showFailureToast(CloseJobActivity.this, response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<MainResponseModel> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                CustomCookieToast.showFailureToast(CloseJobActivity.this, t.getLocalizedMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_out_up, R.anim.slide_bottom);
    }
}