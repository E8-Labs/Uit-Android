package com.antizon.uit_android.activities.home;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.antizon.uit_android.R;
import com.antizon.uit_android.adapters.ViewPagerFragmentAdapter;
import com.antizon.uit_android.company.activities.postjob.CompanyPostJobActivity;
import com.antizon.uit_android.company.fragment.ChatFragment;
import com.antizon.uit_android.generic.activities.SignInActivity;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.recruiter.fragment.CompanyTeamMemberHomeFragment;
import com.antizon.uit_android.recruiter.fragment.CompanyTeamMemberListFragment;
import com.antizon.uit_android.recruiter.fragment.CompanyTeamMemberProfileFragment;
import com.antizon.uit_android.utilities.Utilities;
import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import java.util.ArrayList;
import java.util.List;

public class CompanyTeamMemberBottomNavigationActivity extends AppCompatActivity implements CompanyTeamMemberProfileFragment.CompanyTeamMemberProfileFragmentCallBacks {
    Context context;
    ViewPager2 viewPager2;
    RelativeLayout layout_jobs, layout_team, layout_chat, layout_profile, layout_postJob;
    TextView text_jobs, text_team, text_chat, text_profile;
    ImageView jobs_ic, team_ic, chat_ic;
    RoundedImageView profileIcon;

    SessionManagement sessionManagement;

    List<Fragment> fragmentsList;
    ViewPagerFragmentAdapter myAdapter;
    int current = 0;

    boolean isAtHomeFragment = false;
    boolean backPressedOnce = false;


    Fragment jobsFrag, teamFragment, chatFragment, profileFragment;

    public static boolean touchEnabled = true;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_team_member_bottom_navigation);
        Utilities.setCustomStatusAndNavColor(CompanyTeamMemberBottomNavigationActivity.this, R.color.white_dash, R.color.white);
        context = CompanyTeamMemberBottomNavigationActivity.this;

        boolean finish = getIntent().getBooleanExtra("finish", false);
        if (finish) {
            startActivity(new Intent(CompanyTeamMemberBottomNavigationActivity.this, SignInActivity.class));
            overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
            finish();
        }

        initViews();
    }

    void initViews() {

        viewPager2 = findViewById(R.id.viewPager2);
        layout_jobs = findViewById(R.id.layout_jobs);
        text_jobs = findViewById(R.id.text_jobs);
        jobs_ic = findViewById(R.id.jobs_ic);

        layout_team = findViewById(R.id.layout_team);
        text_team = findViewById(R.id.text_team);
        team_ic = findViewById(R.id.team_ic);

        layout_chat = findViewById(R.id.layout_chat);
        text_chat = findViewById(R.id.text_chat);
        chat_ic = findViewById(R.id.chat_ic);

        layout_profile = findViewById(R.id.layout_profile);
        profileIcon = findViewById(R.id.profileIcon);
        text_profile = findViewById(R.id.text_profile);

        layout_postJob = findViewById(R.id.layout_postJob);

        sessionManagement = new SessionManagement(CompanyTeamMemberBottomNavigationActivity.this);
        Glide.with(CompanyTeamMemberBottomNavigationActivity.this).load(sessionManagement.getProfileImage()).placeholder(R.drawable.uit_app_icon_for_background).into(profileIcon);

        fragmentsList = new ArrayList<>();

        jobsFrag = new CompanyTeamMemberHomeFragment();
        teamFragment = new CompanyTeamMemberListFragment();
        chatFragment = new ChatFragment();
        profileFragment = new CompanyTeamMemberProfileFragment(this);

        fragmentsList.add(jobsFrag);
        fragmentsList.add(teamFragment);
        fragmentsList.add(chatFragment);
        fragmentsList.add(profileFragment);



        myAdapter = new ViewPagerFragmentAdapter(CompanyTeamMemberBottomNavigationActivity.this, fragmentsList);
        viewPager2.setUserInputEnabled(false);
        viewPager2.setAdapter(myAdapter);
        viewPager2.setOffscreenPageLimit(5);
        setDrawableIcon(0);

        layout_jobs.setOnClickListener(v -> {
            touchEnabled = true;
            if (current != 0) {
                Utilities.setCustomStatusAndNavColor(CompanyTeamMemberBottomNavigationActivity.this, R.color.white_dash, R.color.white);
                viewPager2.setCurrentItem(0, false);
                current = 0;
                setDrawableIcon(0);
                isAtHomeFragment = true;
            }
        });

        layout_team.setOnClickListener(v -> {
            if (current != 1) {
                Utilities.setCustomStatusAndNavColor(CompanyTeamMemberBottomNavigationActivity.this, R.color.white_dash, R.color.white);
                viewPager2.setCurrentItem(1, false);
                current = 1;
                setDrawableIcon(1);
                isAtHomeFragment = false;
            }

        });

        layout_chat.setOnClickListener(v -> {
            if (current != 2) {
                Utilities.setCustomStatusAndNavColor(CompanyTeamMemberBottomNavigationActivity.this, R.color.white_dash, R.color.white);
                viewPager2.setCurrentItem(2, false);
                current = 2;
                setDrawableIcon(2);
                isAtHomeFragment = false;
            }
        });

        layout_profile.setOnClickListener(v -> {
            if (current != 3) {
                Utilities.setCustomStatusAndNavColor(CompanyTeamMemberBottomNavigationActivity.this, R.color.app_color, R.color.white);
                viewPager2.setCurrentItem(3, false);
                current = 3;
                setDrawableIcon(3);
                isAtHomeFragment = false;
            }
        });

        layout_postJob.setOnClickListener(v -> {
            Intent postJobIntent = new Intent(context, CompanyPostJobActivity.class);
            postJobIntent.putExtra("from", "add");
            onJobPostedLauncher.launch(postJobIntent);
            overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
        });
    }

    public void setDrawableIcon(int selected) {

        if (selected == 0) {
            jobs_ic.setColorFilter(ContextCompat.getColor(context, R.color.app_color));
            text_jobs.setTextColor(ContextCompat.getColor(CompanyTeamMemberBottomNavigationActivity.this, R.color.app_color));
        } else {
            jobs_ic.setColorFilter(ContextCompat.getColor(context, R.color.gray));
            text_jobs.setTextColor(ContextCompat.getColor(CompanyTeamMemberBottomNavigationActivity.this, R.color.gray));
        }

        if (selected == 1) {
            team_ic.setColorFilter(ContextCompat.getColor(context, R.color.app_color));
            text_team.setTextColor(ContextCompat.getColor(CompanyTeamMemberBottomNavigationActivity.this, R.color.app_color));
        } else {
            team_ic.setColorFilter(ContextCompat.getColor(context, R.color.gray));
            text_team.setTextColor(ContextCompat.getColor(CompanyTeamMemberBottomNavigationActivity.this, R.color.gray));
        }
        if (selected == 2) {
            chat_ic.setColorFilter(ContextCompat.getColor(context, R.color.app_color));
            text_chat.setTextColor(ContextCompat.getColor(CompanyTeamMemberBottomNavigationActivity.this, R.color.app_color));
        } else {
            chat_ic.setColorFilter(ContextCompat.getColor(context, R.color.gray));
            text_chat.setTextColor(ContextCompat.getColor(CompanyTeamMemberBottomNavigationActivity.this, R.color.gray));
        }

        if (selected == 3) {
            text_profile.setTextColor(ContextCompat.getColor(CompanyTeamMemberBottomNavigationActivity.this, R.color.app_color));
        } else {
            text_profile.setTextColor(ContextCompat.getColor(CompanyTeamMemberBottomNavigationActivity.this, R.color.gray));
        }

    }


    @Override
    public void onBackPressed() {
        if (isAtHomeFragment) {
            if (backPressedOnce) {
                overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                finish();
            }
            Toast.makeText(context, "Back press again to exit", Toast.LENGTH_SHORT).show();
            backPressedOnce = true;
            new Handler(Looper.getMainLooper()).postDelayed(() -> backPressedOnce = false, 2000);
        } else {
            moveBackToHome();
        }
    }

    @Override
    public void onMyJobsClicked() {
        moveBackToHome();
        ((CompanyTeamMemberHomeFragment) fragmentsList.get(0)).onMyJObsClicked();
    }


    private void moveBackToHome(){
        Utilities.setCustomStatusAndNavColor(CompanyTeamMemberBottomNavigationActivity.this, R.color.white_dash, R.color.white);
        viewPager2.setCurrentItem(0, false);
        current = 0;
        setDrawableIcon(0);
        touchEnabled = true;
        isAtHomeFragment = true;
    }


    ActivityResultLauncher<Intent> onJobPostedLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            ((CompanyTeamMemberHomeFragment) fragmentsList.get(0)).newJobAddedListener();
        }
    });

}