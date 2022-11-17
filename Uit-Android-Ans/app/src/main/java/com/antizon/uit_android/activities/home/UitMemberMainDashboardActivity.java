package com.antizon.uit_android.activities.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.antizon.uit_android.R;
import com.antizon.uit_android.adapters.ViewPagerFragmentAdapter;
import com.antizon.uit_android.applicant.fragment.CommunityFragment;
import com.antizon.uit_android.company.fragment.ChatFragment;
import com.antizon.uit_android.generic.activities.SignInActivity;
import com.antizon.uit_android.uit_members.fragment.MemberHomeFragment;
import com.antizon.uit_android.uit_members.fragment.UitTeamMemberListFragment;
import com.antizon.uit_android.utilities.Utilities;
import java.util.ArrayList;
import java.util.List;

public class UitMemberMainDashboardActivity extends AppCompatActivity {

    private static final String TAG = UitMemberMainDashboardActivity.class.getSimpleName();
    Context context;

    LinearLayout homeLayout, communityLayout, chatLayout, teamLayout;
    TextView home, community, chat, team;
    ImageView homeIcon, communityIcon, chatIcon, teamIcon;

    ViewPager2 viewPager2;
    List<Fragment> fragmentsList;
    ViewPagerFragmentAdapter myAdapter;
    int current = 0;

    boolean isAtHomeFragment = false;
    boolean backPressedOnce = false;

    Fragment memberHomeFragment, teamMembersFragment, communityFragment, chatFragment;
    public static boolean touchEnabled = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uit_member_main_dashboard);
        context  = UitMemberMainDashboardActivity.this;
        Utilities.setCustomStatusAndNavColor(UitMemberMainDashboardActivity.this, R.color.white_dash, R.color.white);

        boolean finish = getIntent().getBooleanExtra("finish", false);
        if (finish) {
            startActivity(new Intent(UitMemberMainDashboardActivity.this, SignInActivity.class));
            overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
            finish();
        }
        initViews();
    }

    private void initViews() {
        Log.d(TAG, "setIds: ");
        viewPager2 = findViewById(R.id.viewPager2);

        homeLayout = findViewById(R.id.homeLayout);
        communityLayout = findViewById(R.id.communityLayout);
        chatLayout = findViewById(R.id.chatLayout);
        teamLayout = findViewById(R.id.teamLayout);
        home = findViewById(R.id.home);
        community = findViewById(R.id.community);
        chat = findViewById(R.id.chat);
        team = findViewById(R.id.team);
        homeIcon = findViewById(R.id.homeIcon);
        communityIcon = findViewById(R.id.communityIcon);
        chatIcon = findViewById(R.id.chatIcon);
        teamIcon = findViewById(R.id.teamIcon);

        fragmentsList = new ArrayList<>();
        memberHomeFragment = new MemberHomeFragment();
        teamMembersFragment = new UitTeamMemberListFragment();
        communityFragment = new CommunityFragment();
        chatFragment = new ChatFragment();


        fragmentsList.add(memberHomeFragment);
        fragmentsList.add(teamMembersFragment);
        fragmentsList.add(communityFragment);
        fragmentsList.add(chatFragment);

        myAdapter = new ViewPagerFragmentAdapter(UitMemberMainDashboardActivity.this, fragmentsList);
        viewPager2.setUserInputEnabled(false);
        viewPager2.setAdapter(myAdapter);
        viewPager2.setOffscreenPageLimit(5);
        setDrawableIcon(0);


        homeLayout.setOnClickListener(v -> {
            touchEnabled = true;
            if (current != 0) {
                viewPager2.setCurrentItem(0, false);
                current = 0;
                setDrawableIcon(0);
                isAtHomeFragment = true;
            }
        });

        teamLayout.setOnClickListener(v -> {
            if (current != 1) {
                viewPager2.setCurrentItem(1, false);
                current = 1;
                setDrawableIcon(1);
                isAtHomeFragment = false;
            }

        });

        communityLayout.setOnClickListener(v -> {
            if (current != 2) {
                viewPager2.setCurrentItem(2, false);
                current = 2;
                setDrawableIcon(2);
                isAtHomeFragment = false;
            }
        });

        chatLayout.setOnClickListener(v -> {
            if (current != 3) {
                viewPager2.setCurrentItem(3, false);
                current = 3;
                setDrawableIcon(3);
                isAtHomeFragment = false;
            }
        });

    }



    public void setDrawableIcon(int selected) {

        if (selected == 0) {
            homeIcon.setColorFilter(ContextCompat.getColor(context, R.color.app_color));
            home.setTextColor(ContextCompat.getColor(UitMemberMainDashboardActivity.this, R.color.app_color));
        } else {
            homeIcon.setColorFilter(ContextCompat.getColor(context, R.color.gray));
            home.setTextColor(ContextCompat.getColor(UitMemberMainDashboardActivity.this, R.color.gray));
        }

        if (selected == 1) {
            teamIcon.setColorFilter(ContextCompat.getColor(context, R.color.app_color));
            team.setTextColor(ContextCompat.getColor(UitMemberMainDashboardActivity.this, R.color.app_color));
        } else {
            teamIcon.setColorFilter(ContextCompat.getColor(context, R.color.gray));
            team.setTextColor(ContextCompat.getColor(UitMemberMainDashboardActivity.this, R.color.gray));
        }
        if (selected == 2) {
            communityIcon.setColorFilter(ContextCompat.getColor(context, R.color.app_color));
            community.setTextColor(ContextCompat.getColor(UitMemberMainDashboardActivity.this, R.color.app_color));
        } else {
            communityIcon.setColorFilter(ContextCompat.getColor(context, R.color.gray));
            community.setTextColor(ContextCompat.getColor(UitMemberMainDashboardActivity.this, R.color.gray));
        }

        if (selected == 3) {
            chatIcon.setColorFilter(ContextCompat.getColor(context, R.color.app_color));
            chat.setTextColor(ContextCompat.getColor(UitMemberMainDashboardActivity.this, R.color.app_color));
        } else {
            chatIcon.setColorFilter(ContextCompat.getColor(context, R.color.gray));
            chat.setTextColor(ContextCompat.getColor(UitMemberMainDashboardActivity.this, R.color.gray));
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
            viewPager2.setCurrentItem(0, false);
            current = 0;
            setDrawableIcon(0);
            touchEnabled = true;
            isAtHomeFragment = true;
        }
    }

}