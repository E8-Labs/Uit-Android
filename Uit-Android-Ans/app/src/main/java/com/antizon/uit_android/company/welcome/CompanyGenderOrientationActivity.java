package com.antizon.uit_android.company.welcome;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.antizon.uit_android.R;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.models.community.MainResponseModel;
import com.antizon.uit_android.network.GetDataService;
import com.antizon.uit_android.network.RetrofitClientInstance;
import com.antizon.uit_android.utilities.CustomCookieToast;
import com.antizon.uit_android.utilities.Utilities;
import com.google.android.material.slider.Slider;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompanyGenderOrientationActivity extends AppCompatActivity {
    Context context;

    GetDataService service;
    SessionManagement sessionManagement;
    ProgressDialog progressDialog;

    ImageView backIcon, redNoah;
    TextView next, notSure;
    Slider slider_lgbtq;
    String deiStatementValue = "", leadValue = "", ergValue = "", programmingValue = "", trainingValue = "", from;

    int genderProportion = 0, menPercentage=0, womenPercentage = 0, nonBinaryPercentage = 0, lgbtq = 0;

    double companyLgbt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gender_orientation);
        Utilities.setCustomStatusAndNavColor(CompanyGenderOrientationActivity.this, R.color.white_dash, R.color.white_dash);
        context = CompanyGenderOrientationActivity.this;
        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        progressDialog = new ProgressDialog(CompanyGenderOrientationActivity.this);
        sessionManagement = new SessionManagement(CompanyGenderOrientationActivity.this);

        from = getIntent().getStringExtra("from");
        if (from.equals("edit")){
            companyLgbt = getIntent().getDoubleExtra("companyLgbt", 0);
        }else {
            deiStatementValue = getIntent().getStringExtra("deiStatement");
            leadValue = getIntent().getStringExtra("lead");
            ergValue = getIntent().getStringExtra("erg");
            programmingValue = getIntent().getStringExtra("programming");
            trainingValue = getIntent().getStringExtra("training");
            genderProportion = getIntent().getIntExtra("genderProportion", 0);
            menPercentage = getIntent().getIntExtra("menPercentage", 0);
            womenPercentage = getIntent().getIntExtra("womenPercentage", 0);
            nonBinaryPercentage = getIntent().getIntExtra("nonBinaryPercentage", 0);
        }
        initViews();

        if (from.equals("edit")){
            slider_lgbtq.setValue((float) companyLgbt);
        }
    }

    private void initViews() {
        backIcon = findViewById(R.id.backIcon);
        next = findViewById(R.id.next);
        notSure = findViewById(R.id.notSure);
        redNoah = findViewById(R.id.redNoah);
        slider_lgbtq = findViewById(R.id.slider_lgbtq);


        Utilities.loadCircleImage(CompanyGenderOrientationActivity.this, sessionManagement.getProfileImage(), redNoah);
        backIcon.setOnClickListener(v -> onBackPressed());

        slider_lgbtq.setLabelFormatter(value -> (int) value + "%");

        slider_lgbtq.addOnChangeListener((slider, value, fromUser) -> lgbtq = (int) (value));



        next.setOnClickListener(v -> {
            if (from.equals("edit")){
                progressDialog.setMessage("Updating...");
                progressDialog.show();
                requestForUpdateCompanyGenderOrientation(lgbtq);
            }else {
                progressDialog.setMessage("Loading...");
                progressDialog.show();
                requestForAddDEIStatement(deiStatementValue, leadValue, programmingValue, trainingValue, ergValue, menPercentage, womenPercentage, nonBinaryPercentage, lgbtq);
            }

        });

        notSure.setOnClickListener(v -> {
            if (from.equals("edit")){
                onBackPressed();
            }else {
                progressDialog.setMessage("Loading...");
                progressDialog.show();
                requestForAddDEIStatement(deiStatementValue, leadValue, programmingValue, trainingValue, ergValue, menPercentage, womenPercentage, nonBinaryPercentage, lgbtq);
            }

        });
    }

    private void requestForAddDEIStatement(String dei_statement, String team_lead, String programming, String training, String erg, int men, int women, int non_binary, int lgbt) {
        JsonObject mainObject = new JsonObject();

        mainObject.addProperty("dei_statement", dei_statement);
        mainObject.addProperty("team_lead", team_lead.equals("true"));
        mainObject.addProperty("programming", programming.equals("true"));
        mainObject.addProperty("training", training.equals("true"));
        mainObject.addProperty("erg", erg.equals("true"));
        mainObject.addProperty("men", men);
        mainObject.addProperty("women", women);
        mainObject.addProperty("non_binary", non_binary);
        mainObject.addProperty("lgbt", lgbt);


        String jsonString = mainObject.toString();
        RequestBody body = RequestBody.create(jsonString, okhttp3.MediaType.parse("application/json; charset=utf-8"));

        Call<MainResponseModel> call = service.addDeiStatement("Bearer " + sessionManagement.getToken(), body);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NotNull Call<MainResponseModel> call, @NotNull Response<MainResponseModel> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    boolean status = response.body().isStatus();
                    if (status) {
                        openNextScreen();
                    }else {
                        CustomCookieToast.showFailureToast(CompanyGenderOrientationActivity.this, "Failure!", response.body().getMessage());
                    }
                } else {
                    CustomCookieToast.showFailureToast(CompanyGenderOrientationActivity.this, "Failure!", response.message());
                }

            }

            @Override
            public void onFailure(@NotNull Call<MainResponseModel> call, @NotNull Throwable t) {
                progressDialog.dismiss();
                CustomCookieToast.showFailureToast(CompanyGenderOrientationActivity.this, "Failure!", t.getMessage());
            }
        });
    }

    private void openNextScreen() {
        finishAffinity();
        Intent intent = new Intent(CompanyGenderOrientationActivity.this, CompanyInviteTeamMembersActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }

    private void requestForUpdateCompanyGenderOrientation(int lgbt) {
        JsonObject mainObject = new JsonObject();
        mainObject.addProperty("lgbt", lgbt);
        String jsonString = mainObject.toString();
        RequestBody body = RequestBody.create(jsonString, okhttp3.MediaType.parse("application/json; charset=utf-8"));
        Call<MainResponseModel> call = service.updateDeiStatement("Bearer " + sessionManagement.getToken(), body);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NotNull Call<MainResponseModel> call, @NotNull Response<MainResponseModel> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    boolean status = response.body().isStatus();
                    if (status) {
                        Intent intent = new Intent();
                        intent.putExtra("lgbt", lgbt);
                        setResult(RESULT_OK, intent);
                        finish();
                        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                    }else {
                        CustomCookieToast.showFailureToast(CompanyGenderOrientationActivity.this, "Failure!", response.body().getMessage());
                    }
                } else {
                    CustomCookieToast.showFailureToast(CompanyGenderOrientationActivity.this, "Failure!", response.message());
                }

            }

            @Override
            public void onFailure(@NotNull Call<MainResponseModel> call, @NotNull Throwable t) {
                progressDialog.dismiss();
                CustomCookieToast.showFailureToast(CompanyGenderOrientationActivity.this, "Failure!", t.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }
}