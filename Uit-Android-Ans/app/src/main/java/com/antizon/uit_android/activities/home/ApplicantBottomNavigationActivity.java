package com.antizon.uit_android.activities.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import com.antizon.uit_android.R;
import com.antizon.uit_android.adapters.ViewPagerFragmentAdapter;
import com.antizon.uit_android.applicant.fragment.ApplicantJobFragment;
import com.antizon.uit_android.applicant.fragment.ApplicantProfileFragment;
import com.antizon.uit_android.applicant.fragment.CommunityFragment;
import com.antizon.uit_android.company.fragment.ChatFragment;
import com.antizon.uit_android.generic.activities.SignInActivity;
import com.antizon.uit_android.generic_utils.SessionManagement;
import com.antizon.uit_android.utilities.Utilities;
import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;


public class ApplicantBottomNavigationActivity extends AppCompatActivity {

    Context context;
    ViewPager2 viewPager2;
    RelativeLayout layout_jobs, layout_community, layout_chat, layout_profile;
    TextView text_jobs, text_community, text_chat, text_profile;
    ImageView jobs_ic, community_ic, chat_ic;
    RoundedImageView profileIcon;

    SessionManagement sessionManagement;

    List<Fragment> fragmentsList;
    ViewPagerFragmentAdapter myAdapter;
    int current = 0;

    boolean isAtHomeFragment = false;
    boolean backPressedOnce = false;


    Fragment jobsFrag, communityFragment, chatFragment, profileFragment;

    public static boolean touchEnabled = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applicant_bottom_navigation);

        Utilities.setCustomStatusAndNavColor(ApplicantBottomNavigationActivity.this, R.color.white_dash, R.color.white);
        context = ApplicantBottomNavigationActivity.this;

        boolean finish = getIntent().getBooleanExtra("finish", false);
        if (finish) {
            startActivity(new Intent(ApplicantBottomNavigationActivity.this, SignInActivity.class));
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
        layout_community = findViewById(R.id.layout_community);
        text_community = findViewById(R.id.text_community);
        community_ic = findViewById(R.id.community_ic);

        layout_chat = findViewById(R.id.layout_chat);
        text_chat = findViewById(R.id.text_chat);
        chat_ic = findViewById(R.id.chat_ic);

        layout_profile = findViewById(R.id.layout_profile);
        profileIcon = findViewById(R.id.profileIcon);
        text_profile = findViewById(R.id.text_profile);

        sessionManagement = new SessionManagement(ApplicantBottomNavigationActivity.this);
        Glide.with(ApplicantBottomNavigationActivity.this).load(sessionManagement.getProfileImage()).placeholder(R.drawable.uit_app_icon_for_background).into(profileIcon);

        fragmentsList = new ArrayList<>();

        jobsFrag = new ApplicantJobFragment();
        communityFragment = new CommunityFragment();
        chatFragment = new ChatFragment();
        profileFragment = new ApplicantProfileFragment();

        fragmentsList.add(jobsFrag);
        fragmentsList.add(communityFragment);
        fragmentsList.add(chatFragment);
        fragmentsList.add(profileFragment);


        myAdapter = new ViewPagerFragmentAdapter(ApplicantBottomNavigationActivity.this, fragmentsList);
        viewPager2.setUserInputEnabled(false);
        viewPager2.setAdapter(myAdapter);
        viewPager2.setOffscreenPageLimit(5);
        setDrawableIcon(0);

        layout_jobs.setOnClickListener(v -> {
            touchEnabled = true;
            if (current != 0) {
                Utilities.setCustomStatusAndNavColor(ApplicantBottomNavigationActivity.this, R.color.white_dash, R.color.white);
                viewPager2.setCurrentItem(0, false);
                current = 0;
                setDrawableIcon(0);
                isAtHomeFragment = true;
            }
        });

        layout_community.setOnClickListener(v -> {
            if (current != 1) {
                Utilities.setCustomStatusAndNavColor(ApplicantBottomNavigationActivity.this, R.color.white_dash, R.color.white);
                viewPager2.setCurrentItem(1, false);
                current = 1;
                setDrawableIcon(1);
                isAtHomeFragment = false;
            }

        });

        layout_chat.setOnClickListener(v -> {
            if (current != 2) {
                Utilities.setCustomStatusAndNavColor(ApplicantBottomNavigationActivity.this, R.color.white_dash, R.color.white);
                viewPager2.setCurrentItem(2, false);
                current = 2;
                setDrawableIcon(2);
                isAtHomeFragment = false;
            }
        });

        layout_profile.setOnClickListener(v -> {
            if (current != 3) {
                Utilities.setCustomStatusAndNavColor(ApplicantBottomNavigationActivity.this, R.color.app_color, R.color.white);
                viewPager2.setCurrentItem(3, false);
                current = 3;
                setDrawableIcon(3);
                isAtHomeFragment = false;
            }
        });

    }

    public void setDrawableIcon(int selected) {

        if (selected == 0) {
            jobs_ic.setColorFilter(ContextCompat.getColor(context, R.color.app_color));
            text_jobs.setTextColor(ContextCompat.getColor(ApplicantBottomNavigationActivity.this, R.color.app_color));
        } else {
            jobs_ic.setColorFilter(ContextCompat.getColor(context, R.color.gray));
            text_jobs.setTextColor(ContextCompat.getColor(ApplicantBottomNavigationActivity.this, R.color.gray));
        }

        if (selected == 1) {
            community_ic.setColorFilter(ContextCompat.getColor(context, R.color.app_color));
            text_community.setTextColor(ContextCompat.getColor(ApplicantBottomNavigationActivity.this, R.color.app_color));
        } else {
            community_ic.setColorFilter(ContextCompat.getColor(context, R.color.gray));
            text_community.setTextColor(ContextCompat.getColor(ApplicantBottomNavigationActivity.this, R.color.gray));
        }
        if (selected == 2) {
            chat_ic.setColorFilter(ContextCompat.getColor(context, R.color.app_color));
            text_chat.setTextColor(ContextCompat.getColor(ApplicantBottomNavigationActivity.this, R.color.app_color));
        } else {
            chat_ic.setColorFilter(ContextCompat.getColor(context, R.color.gray));
            text_chat.setTextColor(ContextCompat.getColor(ApplicantBottomNavigationActivity.this, R.color.gray));
        }

        if (selected == 3) {
            text_profile.setTextColor(ContextCompat.getColor(ApplicantBottomNavigationActivity.this, R.color.app_color));
        } else {
            text_profile.setTextColor(ContextCompat.getColor(ApplicantBottomNavigationActivity.this, R.color.gray));
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
            Utilities.setCustomStatusAndNavColor(ApplicantBottomNavigationActivity.this, R.color.white_dash, R.color.white);
            viewPager2.setCurrentItem(0, false);
            current = 0;
            setDrawableIcon(0);
            touchEnabled = true;
            isAtHomeFragment = true;
        }
    }

}