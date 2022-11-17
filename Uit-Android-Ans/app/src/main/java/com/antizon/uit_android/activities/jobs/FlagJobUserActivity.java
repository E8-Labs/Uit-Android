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
import com.makeramen.roundedimageview.RoundedImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FlagJobUserActivity extends AppCompatActivity {
    Context context;
    GetDataService service;
    SessionManagement sessionManagement;
    ProgressDialog progressDialog;

    RelativeLayout btnBack;
    TextView btnSend, text_profileName;
    RoundedImageView profileImageView;
    EditText edit_description;

    String type, flagTitle, flagId, flagImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flag_job_user);
        Utilities.setCustomStatusAndNavColor(FlagJobUserActivity.this, R.color.white_dash, R.color.white_dash);
        context = FlagJobUserActivity.this;

        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        progressDialog = new ProgressDialog(context);
        sessionManagement = new SessionManagement(context);

        type = getIntent().getStringExtra("type");
        flagImage = getIntent().getStringExtra("flagImage");
        flagTitle = getIntent().getStringExtra("flagTitle");
        flagId = getIntent().getStringExtra("flagId");

        btnBack = findViewById(R.id.btnBack);
        btnSend = findViewById(R.id.btnSend);
        text_profileName = findViewById(R.id.text_profileName);
        profileImageView = findViewById(R.id.profileImageView);
        edit_description = findViewById(R.id.edit_description);

        btnBack.setOnClickListener(v -> onBackPressed());

        text_profileName.setText(flagTitle);
        Utilities.loadImage(context, flagImage, profileImageView);

        btnSend.setOnClickListener(v -> {
            String description= edit_description.getText().toString();
            if (description.isEmpty()) {
                CustomCookieToast.showRequiredToast(FlagJobUserActivity.this, "Please tell us why you're flagging this user");
            } else {
                Utilities.hideKeyboard(edit_description, context);
                progressDialog.setMessage("Loading...");
                progressDialog.show();
                requestForFlagJobOrUser("Bearer " + sessionManagement.getToken(), flagId, description);

            }
        });
    }

    private void requestForFlagJobOrUser(String authToken, String flagId, String comment) {
        Call<MainResponseModel> call;
        if (type.equals("flag_job")){
            call = service.flagJob(authToken, flagId, comment);
        }else if (type.equals("flag_post")){
            call = service.flagPost(authToken, flagId);
        }else {
            call = service.flagUser(authToken, flagId, comment);
        }
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<MainResponseModel> call, @NonNull Response<MainResponseModel> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()){
                    if (response.body()!=null){
                        if (response.body().isStatus()){
                            Intent intent = new Intent();
                            setResult(RESULT_OK, intent);
                            finish();
                            overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                        }else {
                            CustomCookieToast.showFailureToast(FlagJobUserActivity.this, response.body().getMessage());
                        }
                    }else {
                        CustomCookieToast.showFailureToast(FlagJobUserActivity.this, response.message());
                    }
                }else {
                    CustomCookieToast.showFailureToast(FlagJobUserActivity.this, response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<MainResponseModel> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                CustomCookieToast.showFailureToast(FlagJobUserActivity.this, t.getLocalizedMessage());
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }
}