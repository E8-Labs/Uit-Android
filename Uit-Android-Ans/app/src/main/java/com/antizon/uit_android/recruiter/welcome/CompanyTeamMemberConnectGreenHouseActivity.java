package com.antizon.uit_android.recruiter.welcome;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.antizon.uit_android.R;
import com.antizon.uit_android.activities.home.CompanyTeamMemberBottomNavigationActivity;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.utilities.Utilities;

public class CompanyTeamMemberConnectGreenHouseActivity extends AppCompatActivity {
    Context context;
    String id, email, name, passwordValue, website, profile_image, address, city, state, phone, account_status
            , bio, dob, job_title, role, access_token, application_status, parentId
            , parentName, parentProfileImage;

    RelativeLayout btnConnect;
    TextView skip;
    ImageView redNoah2;

    SessionManagement sessionManagement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_team_member_connect_green_house);
        Utilities.setWhiteBars(CompanyTeamMemberConnectGreenHouseActivity.this);
        context = CompanyTeamMemberConnectGreenHouseActivity.this;
        sessionManagement = new SessionManagement(context);

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

        btnConnect = findViewById(R.id.btnConnect);
        skip = findViewById(R.id.skip);
        redNoah2 = findViewById(R.id.redNoah2);

        skip.setOnClickListener(v -> {
            sessionManagement.createLoginSession("" + id, email, name, passwordValue, website, profile_image, address, city, state, phone, "" + account_status, bio, dob, job_title, "" + role, access_token, "" + application_status, "0", "0");
            sessionManagement.setKeyParentId("" + parentId);
            sessionManagement.setKeyParentProfileName(parentName);
            sessionManagement.setKeyParentProfileImage(parentProfileImage);
            moveToNextScreen();
        });

        btnConnect.setOnClickListener(v -> {
            Intent intent = new Intent(context, CompanyTeamMemberAddGreenHouseActivity.class);
            intent.putExtra("id", "" + id);
            intent.putExtra("email", email);
            intent.putExtra("name", name);
            intent.putExtra("passwordValue", passwordValue);
            intent.putExtra("website", website);
            intent.putExtra("profile_image", profile_image);
            intent.putExtra("address", address);
            intent.putExtra("city", city);
            intent.putExtra("state", state);
            intent.putExtra("phone", phone);
            intent.putExtra("account_status", "" + account_status);
            intent.putExtra("bio",   bio);
            intent.putExtra("dob",   dob);
            intent.putExtra("job_title",   job_title);
            intent.putExtra("role",   ""+role);
            intent.putExtra("access_token",   access_token);
            intent.putExtra("application_status",   "" + application_status);
            intent.putExtra("parentId",   "" + parentId);
            intent.putExtra("parentName",   parentName);
            intent.putExtra("parentProfileImage",   parentProfileImage);
            startActivity(intent);
            overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
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
}