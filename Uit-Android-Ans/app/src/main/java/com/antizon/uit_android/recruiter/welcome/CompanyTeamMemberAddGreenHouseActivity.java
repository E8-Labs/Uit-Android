package com.antizon.uit_android.recruiter.welcome;

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
import com.antizon.uit_android.activities.home.CompanyTeamMemberBottomNavigationActivity;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.models.community.MainResponseModel;
import com.antizon.uit_android.network.GetDataService;
import com.antizon.uit_android.network.RetrofitClientInstance;
import com.antizon.uit_android.utilities.CustomCookieToast;
import com.antizon.uit_android.utilities.Utilities;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompanyTeamMemberAddGreenHouseActivity extends AppCompatActivity {
    Context context;
    GetDataService service;
    SessionManagement sessionManagement;
    ProgressDialog progressDialog;

    ImageView backIcon,redNoah2;
    TextView next;
    EditText edit_greenHouseAccountToken;

    String id, email, name, passwordValue, website, profile_image, address, city, state, phone, account_status
            , bio, dob, job_title, role, access_token, application_status, parentId
            , parentName, parentProfileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_team_member_add_green_house);
        Utilities.setWhiteBars(CompanyTeamMemberAddGreenHouseActivity.this);
        context = CompanyTeamMemberAddGreenHouseActivity.this;

        getAllIntentData();
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
                CustomCookieToast.showRequiredToast(CompanyTeamMemberAddGreenHouseActivity.this, "Please enter greenhouse token");
            }else {
                Utilities.hideKeyboard(edit_greenHouseAccountToken, context);
                progressDialog.setMessage("Loading...");
                progressDialog.show();
                requestForUpdateGreenHouseToken("Bearer " + access_token, greenHouseAccountToken);
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
                            sessionManagement.createLoginSession("" + id, email, name, passwordValue, website, profile_image, address, city, state, phone, "" + account_status, bio, dob, job_title, "" + role, access_token, "" + application_status, "0", "0");
                            sessionManagement.setKeyParentId("" + parentId);
                            sessionManagement.setKeyParentProfileName(parentName);
                            sessionManagement.setKeyParentProfileImage(parentProfileImage);
                            moveToNextScreen();
                        }else {
                            CustomCookieToast.showFailureToast(CompanyTeamMemberAddGreenHouseActivity.this, response.body().getMessage());
                        }
                    }else {
                        CustomCookieToast.showFailureToast(CompanyTeamMemberAddGreenHouseActivity.this, response.message());
                    }
                }else {
                    CustomCookieToast.showFailureToast(CompanyTeamMemberAddGreenHouseActivity.this, response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<MainResponseModel> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                CustomCookieToast.showFailureToast(CompanyTeamMemberAddGreenHouseActivity.this, t.getLocalizedMessage());
            }
        });
    }

    private void moveToNextScreen(){
        finishAffinity();
        Intent intent = new Intent(context, CompanyTeamMemberBottomNavigationActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }

    private void getAllIntentData(){
        id = getIntent().getStringExtra("id");
        email = getIntent().getStringExtra("email");
        name = getIntent().getStringExtra("name");
        passwordValue = getIntent().getStringExtra("passwordValue");
        website = getIntent().getStringExtra("website");
        profile_image = getIntent().getStringExtra("profile_image");
        address = getIntent().getStringExtra("address");
        city = getIntent().getStringExtra("city");
        state = getIntent().getStringExtra("state");
        phone = getIntent().getStringExtra("phone");
        account_status = getIntent().getStringExtra("account_status");
        bio = getIntent().getStringExtra("bio");
        dob = getIntent().getStringExtra("dob");
        job_title = getIntent().getStringExtra("job_title");
        role = getIntent().getStringExtra("role");
        access_token = getIntent().getStringExtra("access_token");
        application_status = getIntent().getStringExtra("application_status");
        parentId = getIntent().getStringExtra("parentId");
        parentName = getIntent().getStringExtra("parentName");
        parentProfileImage = getIntent().getStringExtra("parentProfileImage");
    }
}