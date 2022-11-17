package com.antizon.uit_android.company.welcome;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
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

public class CompanyDEIStatementActivity extends BaseActivity {
    Context context;
    GetDataService service;
    SessionManagement sessionManagement;
    ProgressDialog progressDialog;

    ImageView backIcon, redNoah;
    TextView next, tvCounter;
    EditText deiDetail;

    String  deiStatementValue = "", from, companyDeiStatement;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_deistatement);
        Utilities.setWhiteBars(CompanyDEIStatementActivity.this);
        context = CompanyDEIStatementActivity.this;
        sessionManagement = new SessionManagement(context);
        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        progressDialog = new ProgressDialog(context);

        initViews();
        setListener();

        from = getIntent().getStringExtra("from");
        if (from.equals("edit")){
            companyDeiStatement = getIntent().getStringExtra("companyDeiStatement");
            deiDetail.setText(companyDeiStatement);
            String maxChar = companyDeiStatement.length() + "/500";
            tvCounter.setText(maxChar);
        }

    }

    private void initViews() {
        backIcon = findViewById(R.id.backIcon);
        next = findViewById(R.id.next);
        redNoah = findViewById(R.id.redNoah);
        deiDetail = findViewById(R.id.deiDetail);
        tvCounter = findViewById(R.id.tvCounter);

        loadProfile(CompanyDEIStatementActivity.this, sessionManagement.getProfileImage(), redNoah);

    }


    private void setListener() {
        backIcon.setOnClickListener(v -> onBackPressed());

        next.setOnClickListener(v -> {
            deiStatementValue = deiDetail.getText().toString().trim();
            if (deiStatementValue.isEmpty()) {
                CustomCookieToast.showRequiredToast(CompanyDEIStatementActivity.this, "Please enter company DEI statement");
            } else {
                hideSoftKeyboard(CompanyDEIStatementActivity.this, deiDetail);
                if (from.equals("edit")){
                    progressDialog.setMessage("Updating...");
                    progressDialog.show();
                    requestForUpdateDEIStatement(deiStatementValue);
                }else {
                    openNextScreen();
                }

            }
        });

        deiDetail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void afterTextChanged(Editable s) {
                String maxChar = String.valueOf(s.toString().length());
                tvCounter.setText(maxChar+ "/500");
            }
        });
    }

    private void openNextScreen() {
        Intent intent = new Intent(context, CompanyLeadActivity.class);
        intent.putExtra("deiStatement", deiStatementValue);
        intent.putExtra("from", from);
        startActivity(intent);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }

    private void requestForUpdateDEIStatement(String deiStatementValue) {
        JsonObject mainObject = new JsonObject();
        mainObject.addProperty("dei_statement", deiStatementValue);
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
                        intent.putExtra("companyDeiStatement", deiStatementValue);
                        setResult(RESULT_OK, intent);
                        finish();
                        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                    }else {
                        CustomCookieToast.showFailureToast(CompanyDEIStatementActivity.this, "Failure!", response.body().getMessage());
                    }
                } else {
                    CustomCookieToast.showFailureToast(CompanyDEIStatementActivity.this, "Failure!", response.message());
                }

            }

            @Override
            public void onFailure(@NotNull Call<MainResponseModel> call, @NotNull Throwable t) {
                progressDialog.dismiss();
                CustomCookieToast.showFailureToast(CompanyDEIStatementActivity.this, "Failure!", t.getMessage());
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }
}