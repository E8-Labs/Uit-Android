package com.antizon.uit_android.generic.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.antizon.uit_android.R;
import com.antizon.uit_android.company.utility.BaseActivity;

public class CommunityVideoOpen extends BaseActivity {
    private static final String TAG = CommunityVideoOpen.class.getSimpleName();
    TextView overview,comments,visitWebsite;
    ImageView backIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_video_open);
        setIds();
        initialize();
        setListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");

    }

    void setIds() {
        Log.d(TAG, "setIds: ");
        overview=findViewById(R.id.overview);
        comments=findViewById(R.id.comments);
        visitWebsite=findViewById(R.id.visitWebsite);
        backIcon=findViewById(R.id.backIcon);
    }

    void initialize() {
        Log.d(TAG, "initialize: ");


    }

    void setListener() {
        Log.d(TAG, "setListener: ");
        backIcon.setOnClickListener(view -> onBackPressed());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }
}