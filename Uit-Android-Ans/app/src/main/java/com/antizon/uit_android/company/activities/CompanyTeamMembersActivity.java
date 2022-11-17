package com.antizon.uit_android.company.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.antizon.uit_android.R;
import com.antizon.uit_android.activities.ImageViewerActivity;
import com.antizon.uit_android.activities.chat.MessagesActivity;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.models.applicant.GenericApplicantDataModel;
import com.antizon.uit_android.models.company.profile.CompanyProfileDataModel;
import com.antizon.uit_android.recruiter.welcome.CompanyTeamMemberProfileActivity;
import com.antizon.uit_android.uit_admin.adapters.AdminUitMembersAdapter;
import com.antizon.uit_android.utilities.Utilities;
import com.makeramen.roundedimageview.RoundedImageView;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CompanyTeamMembersActivity extends AppCompatActivity implements AdminUitMembersAdapter.AdminUitMembersAdapterCallBack {
    Context context;
    SessionManagement sessionManagement;

    RelativeLayout btnBack;
    RoundedImageView userProfileImage;
    TextView text_profileName;
    RecyclerView recyclerview_companyTeamMembers;
    AdminUitMembersAdapter companyMembersAdapter;
    List<GenericApplicantDataModel> companyTeamMembersList;

    CompanyProfileDataModel profileDataModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_team_members);
        Utilities.setCustomStatusAndNavColor(CompanyTeamMembersActivity.this, R.color.app_color, R.color.white_dash);
        context = CompanyTeamMembersActivity.this;
        sessionManagement = new SessionManagement(context);
        profileDataModel = getIntent().getParcelableExtra("profileDataModel");

        btnBack = findViewById(R.id.btnBack);
        userProfileImage = findViewById(R.id.userProfileImage);
        text_profileName = findViewById(R.id.text_profileName);
        recyclerview_companyTeamMembers = findViewById(R.id.recyclerview_companyTeamMembers);

        companyTeamMembersList = new ArrayList<>();

        if (profileDataModel !=null){
            Utilities.loadImage(context, profileDataModel.getProfile_image(), userProfileImage);
            text_profileName.setText(profileDataModel.getName());
            companyTeamMembersList = profileDataModel.getCompanyTeamMembers();
            showCompanyTeamMembersList(recyclerview_companyTeamMembers, companyTeamMembersList);

            userProfileImage.setOnClickListener(v -> {
                Intent intent = new Intent(context, ImageViewerActivity.class);
                ViewCompat.setTransitionName(userProfileImage, profileDataModel.getProfile_image());
                String animationName = ViewCompat.getTransitionName(userProfileImage);
                intent.putExtra("animationName", animationName);
                intent.putExtra("IMAGE", profileDataModel.getProfile_image());
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(CompanyTeamMembersActivity.this, userProfileImage, Objects.requireNonNull(animationName));
                startActivity(intent, options.toBundle());
            });

        }


        btnBack.setOnClickListener(v -> onBackPressed());

    }


    private void showCompanyTeamMembersList(RecyclerView recyclerView, List<GenericApplicantDataModel> companyTeamMembers){
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        companyMembersAdapter = new AdminUitMembersAdapter(context, companyTeamMembers, 1,this);
        recyclerView.setAdapter(companyMembersAdapter);
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(context, CompanyTeamMemberProfileActivity.class);
        intent.putExtra("applicantDataModel", companyTeamMembersList.get(position));
        startActivity(intent);
        overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
    }

    @Override
    public void onAcceptClicked(int position) {

    }

    @Override
    public void onDeclineClicked(int position) {

    }

    @Override
    public void onMessageClicked(int position) {
        Intent intent = new Intent(context, MessagesActivity.class);
        intent.putExtra("CHAT_ID", sessionManagement.getKeyId()+"-"+companyTeamMembersList.get(position).getUser_id());
        intent.putExtra("second_user_id", String.valueOf(companyTeamMembersList.get(position).getUser_id()));
        intent.putExtra("second_user_picture", companyTeamMembersList.get(position).getProfile_image());
        intent.putExtra("second_user_name", companyTeamMembersList.get(position).getName());
        startActivity(intent);
        overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }
}