package com.antizon.uit_android.company.welcome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
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

public class CompanyAddGreenHouseAccountActivity extends AppCompatActivity {
    Context context;
    GetDataService service;
    SessionManagement sessionManagement;
    ProgressDialog progressDialog;

    ImageView backIcon,redNoah2;
    TextView next;
    EditText edit_greenHouseAccountToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_add_green_house_account);
        Utilities.setWhiteBars(CompanyAddGreenHouseAccountActivity.this);
        context = CompanyAddGreenHouseAccountActivity.this;

        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        progressDialog = new ProgressDialog(context);
        sessionManagement = new SessionManagement(context);

        backIcon = findViewById(R.id.backIcon);
        redNoah2 = findViewById(R.id.redNoah2);
        next = findViewById(R.id.next);
        edit_greenHouseAccountToken = findViewById(R.id.edit_greenHouseAccountToken);

        Utilities.loadCircleImage(context, sessionManagement.getProfileImage(), redNoah2);

        backIcon.setOnClickListener(v -> onBackPressed());

        next.setOnClickListener(v -> {
            String greenHouseAccountToken = edit_greenHouseAccountToken.getText().toString();

            if (greenHouseAccountToken.isEmpty()){
                CustomCookieToast.showRequiredToast(CompanyAddGreenHouseAccountActivity.this, "Please enter greenhouse token");
            }else {
                Utilities.hideKeyboard(edit_greenHouseAccountToken, context);
                progressDialog.setMessage("Loading...");
                progressDialog.show();
                requestForUpdateGreenHouseToken("Bearer " + sessionManagement.getToken(), greenHouseAccountToken);
            }

        });
    }

    private void requestForUpdateGreenHouseToken(String authToken, String greenHouseAccountToken) {
        Call<MainResponseModel> call = service.updateGreenHouseToken(authToken, greenHouseAccountToken);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<MainResponseModel> call, @NonNull Response<MainResponseModel> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()){
                    if (response.body()!=null){
                        if (response.body().isStatus()){
                            Intent connectIntent = new Intent(context, CompanyDEIStatementActivity.class);
                            connectIntent.putExtra("from", "add");
                            startActivity(connectIntent);
                            overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
                        }else {
                            CustomCookieToast.showFailureToast(CompanyAddGreenHouseAccountActivity.this, response.body().getMessage());
                        }
                    }else {
                        CustomCookieToast.showFailureToast(CompanyAddGreenHouseAccountActivity.this, response.message());
                    }
                }else {
                    CustomCookieToast.showFailureToast(CompanyAddGreenHouseAccountActivity.this, response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<MainResponseModel> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                CustomCookieToast.showFailureToast(CompanyAddGreenHouseAccountActivity.this, t.getLocalizedMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }
}