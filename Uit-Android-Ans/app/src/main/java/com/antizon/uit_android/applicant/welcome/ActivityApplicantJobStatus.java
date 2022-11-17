package com.antizon.uit_android.applicant.welcome;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import com.antizon.uit_android.R;
import com.antizon.uit_android.company.utility.BaseActivity;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.models.community.MainResponseModel;
import com.antizon.uit_android.network.GetDataService;
import com.antizon.uit_android.network.RetrofitClientInstance;
import com.antizon.uit_android.utilities.CustomCookieToast;
import com.antizon.uit_android.utilities.Utilities;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityApplicantJobStatus extends BaseActivity {
    GetDataService service;
    Context context;
    ProgressDialog progressDialog;
    SessionManagement sessionManagement;

    ImageView backIcon, menYellow;
    TextView unEmployed, employed, notLooking, next;
    ConstraintLayout approvedLayout, pausedLayout, pendingLayout;
    String employeValue = "", employmentStatus = "", from;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applicant_job_status);
        Utilities.setWhiteBars(ActivityApplicantJobStatus.this);
        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        context = ActivityApplicantJobStatus.this;
        sessionManagement = new SessionManagement(ActivityApplicantJobStatus.this);
        progressDialog = new ProgressDialog(ActivityApplicantJobStatus.this);

        initViews();

        from = getIntent().getStringExtra("from");
        if (from.equals("edit")){
            employmentStatus = getIntent().getStringExtra("employmentStatus");

            if (employmentStatus !=null ){
                switch (Integer.parseInt(employmentStatus)) {
                    case 0:
                        setApprovedOneLayout();
                        break;
                    case 1:
                        setPausedOneLayout();
                        break;
                    case 2:
                        setPendingOneLayout();
                        break;
                }
            }
        }

    }

    private void initViews() {

        backIcon = findViewById(R.id.backIcon);
        next = findViewById(R.id.next);
        unEmployed = findViewById(R.id.unEmployed);
        menYellow = findViewById(R.id.menYellow);
        employed = findViewById(R.id.employed);
        notLooking = findViewById(R.id.notLooking);
        pausedLayout = findViewById(R.id.pausedLayout);
        pendingLayout = findViewById(R.id.pendingLayout);
        approvedLayout = findViewById(R.id.approvedLayout);


        loadProfile(ActivityApplicantJobStatus.this, sessionManagement.getProfileImage(), menYellow);

        next.setOnClickListener(v -> openNextScreen());

        approvedLayout.setOnClickListener(v -> {
            setApprovedOneLayout();
            if (from.equals("edit")){
                progressDialog.setMessage("Updating...");
                progressDialog.show();
                updateApplicantJobStatus(employeValue);
            }
        });
        pausedLayout.setOnClickListener(v -> {
            setPausedOneLayout();
            if (from.equals("edit")){
                progressDialog.setMessage("Updating...");
                progressDialog.show();
                updateApplicantJobStatus(employeValue);
            }
        });
        pendingLayout.setOnClickListener(v -> {
            setPendingOneLayout();
            if (from.equals("edit")){
                progressDialog.setMessage("Updating...");
                progressDialog.show();
                updateApplicantJobStatus(employeValue);
            }
        });

        backIcon.setOnClickListener(v -> onBackPressed());
    }



    private void setApprovedOneLayout() {
        approvedLayout.setBackgroundResource(R.drawable.login_background);
        pausedLayout.setBackgroundResource(R.drawable.text_here_border);
        pendingLayout.setBackgroundResource(R.drawable.text_here_border);
        unEmployed.setTextColor(getColor(R.color.white));
        employed.setTextColor(getColor(R.color.black));
        notLooking.setTextColor(getColor(R.color.black));
        employeValue = "0";
    }

    private void setPausedOneLayout() {

        approvedLayout.setBackgroundResource(R.drawable.text_here_border);
        pausedLayout.setBackgroundResource(R.drawable.login_background);
        pendingLayout.setBackgroundResource(R.drawable.text_here_border);
        unEmployed.setTextColor(getColor(R.color.black));
        employed.setTextColor(getColor(R.color.white));
        notLooking.setTextColor(getColor(R.color.black));
        employeValue = "1";
    }

    private void setPendingOneLayout() {

        approvedLayout.setBackgroundResource(R.drawable.text_here_border);
        pausedLayout.setBackgroundResource(R.drawable.text_here_border);
        pendingLayout.setBackgroundResource(R.drawable.login_background);

        unEmployed.setTextColor(getColor(R.color.black));
        employed.setTextColor(getColor(R.color.black));
        notLooking.setTextColor(getColor(R.color.white));
        employeValue = "2";

    }

    private void openNextScreen() {
        if (from.equals("edit")){
           onBackPressed();
        }else {
            if (employeValue.isEmpty()) {
                CustomCookieToast.showRequiredToast(ActivityApplicantJobStatus.this, "Please select Employment Status");
            } else {
                Intent intent = new Intent(ActivityApplicantJobStatus.this, ApplicantEducationActivity.class);
                intent.putExtra("from", from);
                intent.putExtra("employeStatus", employeValue);
                onProfileUpdateLauncher.launch(intent);
                overridePendingTransition(R.anim.left_in,R.anim.left_out);
            }
        }

    }


    ActivityResultLauncher<Intent> onProfileUpdateLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
            overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
        }
    });

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }


    private void updateApplicantJobStatus(String employmentStatus) {
        JsonObject mainObject = new JsonObject();
        mainObject.addProperty("employment_status", Integer.parseInt(employmentStatus));
        String jsonString = mainObject.toString();
        RequestBody body = RequestBody.create(jsonString, okhttp3.MediaType.parse("application/json; charset=utf-8"));
        Call<MainResponseModel> call = service.updateProfessionalInfo("Bearer " + sessionManagement.getToken(), body);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NotNull Call<MainResponseModel> call, @NotNull Response<MainResponseModel> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    boolean status = response.body().isStatus();
                    if (status) {
                        Intent intent = new Intent();
                        intent.putExtra("employmentStatus", employmentStatus);
                        setResult(RESULT_OK, intent);
                        finish();
                        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                    }else {
                        CustomCookieToast.showFailureToast(ActivityApplicantJobStatus.this, "Failure!", response.body().getMessage());
                    }
                } else {
                    CustomCookieToast.showFailureToast(ActivityApplicantJobStatus.this, "Failure!", response.message());
                }

            }

            @Override
            public void onFailure(@NotNull Call<MainResponseModel> call, @NotNull Throwable t) {
                progressDialog.dismiss();
                CustomCookieToast.showFailureToast(ActivityApplicantJobStatus.this, "Failure!", t.getMessage());
            }
        });
    }
}