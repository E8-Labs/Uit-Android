package com.antizon.uit_android.applicant.welcome;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

public class ActivityContactUs extends AppCompatActivity {
    GetDataService service;
    Context context;
    SessionManagement sessionManagement;
    ProgressDialog progressDialog;

    ImageView btnBack;
    TextView btnSend, text_fullName;
    EditText editMessageTitle, editMessageDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        Utilities.setCustomStatusAndNavColor(ActivityContactUs.this, R.color.white_dash, R.color.white_dash);
        context = ActivityContactUs.this;
        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        sessionManagement = new SessionManagement(context);
        progressDialog  = new ProgressDialog(context);

        initViews();
    }


    private void initViews() {

        btnBack = findViewById(R.id.btnBack);
        btnSend = findViewById(R.id.btnSend);
        text_fullName = findViewById(R.id.text_fullName);
        editMessageTitle = findViewById(R.id.editMessageTitle);
        editMessageDescription = findViewById(R.id.editMessageDescription);

        text_fullName.setText(sessionManagement.getUserName());

        btnSend.setOnClickListener(v -> {
            String messageTitle = editMessageTitle.getText().toString().trim();
            String messageDescription = editMessageDescription.getText().toString().trim();

            if (messageTitle.trim().isEmpty()) {
                CustomCookieToast.showRequiredToast(ActivityContactUs.this, "Please enter message title");
            }else if (messageDescription.trim().isEmpty()) {
                CustomCookieToast.showRequiredToast(ActivityContactUs.this, "Please enter message description");
            }else {
               Utilities.hideKeyboard(editMessageDescription, context);
                progressDialog.setMessage("Loading...");
                progressDialog.show();
                requestForContactUit("Bearer " + sessionManagement.getToken(), messageTitle, messageDescription);
            }
        });

        btnBack.setOnClickListener(v -> onBackPressed());

    }


    private void requestForContactUit(String authToken, String messageTitle, String messageDescription) {
        Call<MainResponseModel> call = service.contactUit(authToken, messageTitle, messageDescription);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<MainResponseModel> call, @NonNull Response<MainResponseModel> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().isStatus()) {
                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
                        finish();
                        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                    } else {
                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Unsuccessful : " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<MainResponseModel> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(context, "Failure : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }
}