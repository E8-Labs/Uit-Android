package com.antizon.uit_android.activities.home;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.antizon.uit_android.R;
import com.antizon.uit_android.company.fragment.ChatFragment;
import com.antizon.uit_android.company.fragment.CompanyJobsFragment;
import com.antizon.uit_android.company.fragment.PostJob;
import com.antizon.uit_android.company.fragment.ProfileFragment;
import com.antizon.uit_android.company.fragment.Team;
import com.antizon.uit_android.company.utility.BaseActivity;
import com.antizon.uit_android.generic_utils.SessionManagement;

public class CompanyBottomNavigationActivity extends BaseActivity {
    private static final String TAG = CompanyBottomNavigationActivity.class.getSimpleName();
    ProgressDialog progressDialog;
    SessionManagement sessionManagement;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    LinearLayout jobLayout, teamLayout, postJobLayout, chatLayout, profileLayout;
    TextView job, team, postJob, chat, profile;
    ImageView jobIcon, teamIcon, postJobIcon, chatIcon, profileIcon;
    String lastOpenedFragment = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_bottom_navigation_activity);

        setIds();
        initialize();
        setListener();
    }

    @Override
    protected void onResume() {
        super.onResume();

        setBottomBar("Job");
        setJobFragment();
    }

    void setIds() {
        Log.d(TAG, "setIds: ");

        jobIcon = findViewById(R.id.jobIcon);
        teamIcon = findViewById(R.id.teamIcon);
        postJobIcon = findViewById(R.id.postJobIcon);
        chatIcon = findViewById(R.id.chatIcon);
        profileIcon = findViewById(R.id.profileIcon);
        job = findViewById(R.id.job);
        team = findViewById(R.id.team);
        postJob = findViewById(R.id.postJob);
        chat = findViewById(R.id.chat);
        profile = findViewById(R.id.profile);
        jobLayout = findViewById(R.id.jobLayout);
        teamLayout = findViewById(R.id.teamLayout);
        postJobLayout = findViewById(R.id.postJobLayout);
        chatLayout = findViewById(R.id.chatLayout);
        profileLayout = findViewById(R.id.profileLayout);
    }

    void initialize() {
        Log.d(TAG, "initialize: ");

        progressDialog = new ProgressDialog(CompanyBottomNavigationActivity.this);
        sessionManagement = new SessionManagement(CompanyBottomNavigationActivity.this);
        loadProfile(CompanyBottomNavigationActivity.this, sessionManagement.getProfileImage(), profileIcon);
    }

    void setListener() {
        Log.d(TAG, "setListener: ");

        jobLayout.setOnClickListener(view -> {
            setBottomBar("Job");
            setJobFragment();
        });
        teamLayout.setOnClickListener(view -> {
            setBottomBar("Team");
            setTeamFragment();
        });

        postJobLayout.setOnClickListener(view -> {
            setBottomBar("PostJob");
            setPostJobFragment();
        });

        chatLayout.setOnClickListener(view -> {
            setBottomBar("Chat");
            setChatFragment();
        });

        profileLayout.setOnClickListener(view -> {
            setBottomBar("Profile");
            setProfileFragment();
        });
    }

    void setLastOpenedFragment(String lastOpenedFragment) {
        this.lastOpenedFragment = lastOpenedFragment;

    }


    void setJobFragment() {
        Log.d(TAG, "setHomeFragment: ");
        setLastOpenedFragment("Job");
        if (fragmentManager == null)
            fragmentManager = getSupportFragmentManager();

        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, new CompanyJobsFragment());
        fragmentTransaction.commit();
    }

    void setTeamFragment() {
        Log.d(TAG, "setHomeFragment: ");
        setLastOpenedFragment("Team");
        if (fragmentManager == null)
            fragmentManager = getSupportFragmentManager();

        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, new Team());
        fragmentTransaction.commit();
    }

    void setChatFragment() {
        Log.d(TAG, "setHomeFragment: ");
        setLastOpenedFragment("Chat");

        if (fragmentManager == null)
            fragmentManager = getSupportFragmentManager();

        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, new ChatFragment());
        fragmentTransaction.commit();
    }

    void setProfileFragment() {
        Log.d(TAG, "setProfileFragment: ");
        setLastOpenedFragment("Profile");
        if (fragmentManager == null)
            fragmentManager = getSupportFragmentManager();

        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, new ProfileFragment());
        fragmentTransaction.commit();
    }

    void setPostJobFragment() {
        Log.d(TAG, "setProfileFragment: ");
        setLastOpenedFragment("PostJob");
        if (fragmentManager == null)
            fragmentManager = getSupportFragmentManager();

        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, new PostJob());
        fragmentTransaction.commit();
    }


    void setBottomBar(String selectedFragment) {

        job.setTextColor(getColor(R.color.grayShadeThree));
        team.setTextColor(getResources().getColor(R.color.grayShadeThree));
        postJob.setTextColor(getResources().getColor(R.color.grayShadeThree));
        chat.setTextColor(getResources().getColor(R.color.grayShadeThree));
        profile.setTextColor(getResources().getColor(R.color.grayShadeThree));

        jobIcon.setColorFilter(ContextCompat.getColor(this, R.color.grayShadeThree));
        teamIcon.setColorFilter(ContextCompat.getColor(this, R.color.grayShadeThree));
        postJobIcon.setColorFilter(ContextCompat.getColor(this, R.color.grayShadeThree));
        chatIcon.setColorFilter(ContextCompat.getColor(this, R.color.grayShadeThree));


        if (selectedFragment.equalsIgnoreCase("Job")) {
            job.setTextColor(getColor(R.color.app_color));
            jobIcon.setColorFilter(ContextCompat.getColor(this, R.color.app_color));
        } else if (selectedFragment.equalsIgnoreCase("Team")) {
            team.setTextColor(getColor(R.color.app_color));
            teamIcon.setColorFilter(ContextCompat.getColor(this, R.color.app_color));
        } else if (selectedFragment.equalsIgnoreCase("PostJob")) {
            postJob.setTextColor(getColor(R.color.app_color));
            postJobIcon.setColorFilter(ContextCompat.getColor(this, R.color.app_color));
        } else if (selectedFragment.equalsIgnoreCase("Chat")) {
            chat.setTextColor(getColor(R.color.app_color));
            chatIcon.setColorFilter(ContextCompat.getColor(this, R.color.app_color));
        } else if (selectedFragment.equalsIgnoreCase("Profile")) {
            profile.setTextColor(getColor(R.color.app_color));
        }
    }
}