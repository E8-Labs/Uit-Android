package com.antizon.uit_android.activities.jobs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.antizon.uit_android.R;
import com.antizon.uit_android.utilities.Utilities;
import com.makeramen.roundedimageview.RoundedImageView;

public class TeamMemberHiredActivity extends AppCompatActivity {
    Context context;

    RelativeLayout layout_backArrow, btn_goBack;
    RoundedImageView user_ProfileImage;
    TextView text_userName, text_jobTitle;

    String profileImage, applicantName, applicantJob;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_member_hired);
        Utilities.setWhiteBars(TeamMemberHiredActivity.this);
        context  = TeamMemberHiredActivity.this;

        profileImage = getIntent().getStringExtra("profileImage");
        applicantName = getIntent().getStringExtra("applicantName");
        applicantJob = getIntent().getStringExtra("applicantJob");

        layout_backArrow = findViewById(R.id.layout_backArrow);
        user_ProfileImage = findViewById(R.id.user_ProfileImage);
        text_userName = findViewById(R.id.text_userName);
        text_jobTitle = findViewById(R.id.text_jobTitle);
        btn_goBack = findViewById(R.id.btn_goBack);

        Utilities.loadImage(context, profileImage, user_ProfileImage);
        if (applicantName!=null){
            text_userName.setText(applicantName);
        }
        if (applicantJob !=null){
            text_jobTitle.setText(applicantJob);
        }

        layout_backArrow.setOnClickListener(v -> goBackAndFinishAllStacks());
        btn_goBack.setOnClickListener(v -> goBackAndFinishAllStacks());
    }

    @Override
    public void onBackPressed() {
        goBackAndFinishAllStacks();
    }

    private void goBackAndFinishAllStacks(){
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }
}