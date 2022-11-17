package com.antizon.uit_android.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.antizon.uit_android.R;
import com.antizon.uit_android.models.community.MainResponseModel;
import com.antizon.uit_android.network.GetDataService;
import com.antizon.uit_android.network.RetrofitClientInstance;
import com.antizon.uit_android.utilities.CustomCookieToast;
import com.antizon.uit_android.utilities.Utilities;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends AppCompatActivity {
    Context context;
    GetDataService service;
    ProgressDialog progressDialog;

    ImageView backIcon;
    EditText editTextEmail;
    TextView btnResetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        Utilities.setWhiteBars(ForgotPasswordActivity.this);
        context = ForgotPasswordActivity.this;
        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        progressDialog = new ProgressDialog(context);

        backIcon = findViewById(R.id.backIcon);
        editTextEmail = findViewById(R.id.editTextEmail);
        btnResetPassword = findViewById(R.id.btnResetPassword);

        btnResetPassword.setOnClickListener(v -> {
            String userEmail = editTextEmail.getText().toString().trim();

            if (userEmail.isEmpty()) {
                CustomCookieToast.showRequiredToast(ForgotPasswordActivity.this, "Please enter your email");
            } else if (!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
                CustomCookieToast.showRequiredToast(ForgotPasswordActivity.this, "Please enter valid email");
            }else {
                Utilities.hideKeyboard(editTextEmail, context);
                progressDialog.setMessage("Loading...");
                progressDialog.show();
                requestForForgotPassword(userEmail);
            }
        });

        backIcon.setOnClickListener(v -> onBackPressed());
    }


    private void requestForForgotPassword(String userEmail){
        Call<MainResponseModel> call = service.forgotPassword(userEmail);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<MainResponseModel> call, @NonNull Response<MainResponseModel> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    boolean status = response.body().isStatus();
                    if (status) {
                        CustomCookieToast.showSuccessToast( ForgotPasswordActivity.this, "Success!", "Reset password email has sent to your email");
                    } else {
                        CustomCookieToast.showFailureToast( ForgotPasswordActivity.this, "Failure!", response.body().getMessage());
                    }
                } else {
                    CustomCookieToast.showFailureToast( ForgotPasswordActivity.this, "Failure!", response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<MainResponseModel> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                CustomCookieToast.showFailureToast( ForgotPasswordActivity.this, "Failure!",  t.getMessage());
            }
        });
    }

    
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }
}