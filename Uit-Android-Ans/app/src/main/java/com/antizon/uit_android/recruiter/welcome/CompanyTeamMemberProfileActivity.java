package com.antizon.uit_android.recruiter.welcome;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.antizon.uit_android.R;
import com.antizon.uit_android.activities.ImageViewerActivity;
import com.antizon.uit_android.activities.jobs.CompanyAndTeamJobsActivity;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.models.applicant.GenericApplicantDataModel;
import com.antizon.uit_android.network.GetDataService;
import com.antizon.uit_android.network.RetrofitClientInstance;
import com.antizon.uit_android.utilities.Utilities;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.Objects;

public class CompanyTeamMemberProfileActivity extends AppCompatActivity {
    GetDataService service;
    Context context;
    SessionManagement sessionManagement;

    RelativeLayout layout_myJobs, btnBack;
    TextView text_profileName, text_jobTitle, text_userEmail, text_userPhoneNumber;
    RoundedImageView userProfileImage;

    GenericApplicantDataModel applicantDataModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_team_member_profile);
        Utilities.setCustomStatusAndNavColor(CompanyTeamMemberProfileActivity.this, R.color.app_color, R.color.white_dash);
        context = CompanyTeamMemberProfileActivity.this;
        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        sessionManagement = new SessionManagement(context);

        applicantDataModel = getIntent().getParcelableExtra("applicantDataModel");

        text_profileName = findViewById(R.id.text_profileName);
        text_jobTitle = findViewById(R.id.text_jobTitle);
        text_userEmail = findViewById(R.id.text_userEmail);
        text_userPhoneNumber = findViewById(R.id.text_userPhoneNumber);
        userProfileImage = findViewById(R.id.userProfileImage);
        layout_myJobs = findViewById(R.id.layout_myJobs);
        btnBack = findViewById(R.id.btnBack);


        Utilities.loadImage(context, applicantDataModel.getProfile_image(), userProfileImage);

        userProfileImage.setOnClickListener(v -> {
            Intent intent = new Intent(context, ImageViewerActivity.class);
            ViewCompat.setTransitionName(userProfileImage, applicantDataModel.getProfile_image());
            String animationName = ViewCompat.getTransitionName(userProfileImage);
            intent.putExtra("animationName", animationName);
            intent.putExtra("IMAGE",applicantDataModel.getProfile_image());
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(CompanyTeamMemberProfileActivity.this, userProfileImage, Objects.requireNonNull(animationName));
            startActivity(intent, options.toBundle());
        });

        text_profileName.setText(applicantDataModel.getName());
        text_jobTitle.setText(applicantDataModel.getJob_title());
        text_userEmail.setText(applicantDataModel.getEmail());
        text_userPhoneNumber.setText(applicantDataModel.getPhone());

        btnBack.setOnClickListener(v -> onBackPressed());

        layout_myJobs.setOnClickListener(v -> {
            Intent intent = new Intent(context, CompanyAndTeamJobsActivity.class);
            intent.putExtra("personaImage", applicantDataModel.getProfile_image());
            intent.putExtra("personaName", applicantDataModel.getName());
            intent.putExtra("personaId", applicantDataModel.getUser_id() + "");
            intent.putExtra("personaRole", "4");
            startActivity(intent);
            overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }
}