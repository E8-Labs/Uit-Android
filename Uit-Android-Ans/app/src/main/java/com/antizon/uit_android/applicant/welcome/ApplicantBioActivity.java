package com.antizon.uit_android.applicant.welcome;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.antizon.uit_android.R;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.models.community.MainResponseModel;
import com.antizon.uit_android.network.GetDataService;
import com.antizon.uit_android.network.RetrofitClientInstance;
import com.antizon.uit_android.utilities.CustomCookieToast;
import com.antizon.uit_android.utilities.Utilities;
import com.makeramen.roundedimageview.RoundedImageView;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApplicantBioActivity extends AppCompatActivity {

    Context context;
    GetDataService service;
    SessionManagement sessionManagement;
    ProgressDialog progressDialog;

    ImageView backIcon;
    TextView next;
    RoundedImageView menYellow;
    EditText edit_applicantBio;

    String applicantBio;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applicant_bio);
        Utilities.setWhiteBars(ApplicantBioActivity.this);
        context =  ApplicantBioActivity.this;
        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        sessionManagement = new SessionManagement(context);
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading....");

        applicantBio = getIntent().getStringExtra("applicantBio");

        backIcon = findViewById(R.id.backIcon);
        next = findViewById(R.id.next);
        menYellow = findViewById(R.id.menYellow);
        edit_applicantBio = findViewById(R.id.edit_applicantBio);

        Utilities.loadImage(context, sessionManagement.getProfileImage(), menYellow);

        if (applicantBio !=null){
            edit_applicantBio.setText(applicantBio);
        }

        next.setOnClickListener(v -> {
            String bio = edit_applicantBio.getText().toString();
            if (bio.isEmpty()){
                CustomCookieToast.showRequiredToast(ApplicantBioActivity.this, "Please enter applicant bio");
            }else{
                progressDialog.show();
                requestForApplicantUpdateApplicantBio("Bearer " + sessionManagement.getToken(), bio);
            }
        });

        backIcon.setOnClickListener(v -> onBackPressed());
    }

    private void requestForApplicantUpdateApplicantBio(String authToken,String applicantBio) {
        Call<MainResponseModel> call = service.updateApplicantProfileBio(authToken, applicantBio);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NotNull Call<MainResponseModel> call, @NotNull Response<MainResponseModel> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().isStatus()) {
                        Intent intent = new Intent();
                        intent.putExtra("applicantBio", applicantBio);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }
}