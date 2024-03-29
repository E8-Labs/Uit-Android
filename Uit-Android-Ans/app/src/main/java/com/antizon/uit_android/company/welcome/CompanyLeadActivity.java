package com.antizon.uit_android.company.welcome;

import androidx.constraintlayout.widget.ConstraintLayout;

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

public class CompanyLeadActivity extends BaseActivity {
    Context context;
    GetDataService service;
    SessionManagement sessionManagement;
    ProgressDialog progressDialog;

    ImageView backIcon, redNoah;
    TextView yes, no, skip;
    ConstraintLayout yesLayout, noLayout;

    String deiStatementValue = "", leadValue = "", from;
    int companyTeamLead;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_lead);
        Utilities.setWhiteBars(CompanyLeadActivity.this);
        context = CompanyLeadActivity.this;
        sessionManagement = new SessionManagement(context);
        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        progressDialog = new ProgressDialog(context);
        from = getIntent().getStringExtra("from");
        if (from.equals("edit")){
            companyTeamLead = getIntent().getIntExtra("companyTeamLead", 0);
        }else {
            deiStatementValue = getIntent().getStringExtra("deiStatement");
        }


        initViews();
        setListener();

        if (from.equals("edit")){
            if (companyTeamLead == 1){
                setYesLayout();
            }else {
                setNoLayout();
            }
        }

    }

    private void initViews() {

        backIcon = findViewById(R.id.backIcon);
        yes = findViewById(R.id.yes);
        no = findViewById(R.id.no);
        skip = findViewById(R.id.skip);
        redNoah = findViewById(R.id.redNoah);
        yesLayout = findViewById(R.id.yesLayout);
        noLayout = findViewById(R.id.noLayout);

        loadProfile(CompanyLeadActivity.this, sessionManagement.getProfileImage(), redNoah);

    }



    private void setListener() {
        backIcon.setOnClickListener(v -> onBackPressed());
        yesLayout.setOnClickListener(v -> setYesLayout());
        noLayout.setOnClickListener(v -> setNoLayout());

        skip.setOnClickListener(v -> {
            if (from.equals("edit")){
                progressDialog.setMessage("Updating...");
                progressDialog.show();
                requestForUpdateCompanyLead(deiStatementValue);
            }else {
                openNextScreen();
            }

        });
    }

    private void openNextScreen() {
        Intent intent = new Intent(CompanyLeadActivity.this, CompanyErgActivity.class);
        intent.putExtra("from", from);
        intent.putExtra("deiStatement", deiStatementValue);
        intent.putExtra("lead", leadValue);
        startActivity(intent);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }

    private void setYesLayout() {
        leadValue = "true";
        yesLayout.setBackgroundResource(R.drawable.login_background);
        noLayout.setBackgroundResource(R.drawable.text_here_border);

        yes.setTextColor(getColor(R.color.white));
        no.setTextColor(getColor(R.color.black));
    }

    private void setNoLayout() {
        leadValue = "false";
        yesLayout.setBackgroundResource(R.drawable.text_here_border);
        noLayout.setBackgroundResource(R.drawable.login_background);
        yes.setTextColor(getColor(R.color.black));
        no.setTextColor(getColor(R.color.white));

    }

    private void requestForUpdateCompanyLead(String leadValue) {
        JsonObject mainObject = new JsonObject();
        mainObject.addProperty("team_lead", leadValue.equals("true"));
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
                        if (leadValue.equals("true")){
                            intent.putExtra("companyTeamLead", 1);
                        }
                        else{
                            intent.putExtra("companyTeamLead", 0);
                        }
                        setResult(RESULT_OK, intent);
                        finish();
                        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                    }else {
                        CustomCookieToast.showFailureToast(CompanyLeadActivity.this, "Failure!", response.body().getMessage());
                    }
                } else {
                    CustomCookieToast.showFailureToast(CompanyLeadActivity.this, "Failure!", response.message());
                }

            }

            @Override
            public void onFailure(@NotNull Call<MainResponseModel> call, @NotNull Throwable t) {
                progressDialog.dismiss();
                CustomCookieToast.showFailureToast(CompanyLeadActivity.this, "Failure!", t.getMessage());
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }
}