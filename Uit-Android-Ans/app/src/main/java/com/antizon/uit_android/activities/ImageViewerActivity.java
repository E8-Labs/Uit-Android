package com.antizon.uit_android.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.antizon.uit_android.R;
import com.antizon.uit_android.utilities.Utilities;
import com.jsibbold.zoomage.ZoomageView;
import com.wang.avi.AVLoadingIndicatorView;

public class ImageViewerActivity extends AppCompatActivity {
    Context context;

    ZoomageView imageView;
    AVLoadingIndicatorView loading;
    RelativeLayout btnBack;
    String image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);
        Utilities.hideNavAndStatusBar(ImageViewerActivity.this);
        context = ImageViewerActivity.this;
        supportPostponeEnterTransition();

        Bundle extras = getIntent().getExtras();
        String imageTransitionName = extras.getString("animationName");

        imageView = findViewById(R.id.iv_image);

        btnBack = findViewById(R.id.btnBack);
        loading = findViewById(R.id.loading_profile_image);

        imageView.setTransitionName(imageTransitionName);

        image = getIntent().getStringExtra("IMAGE");

        Utilities.loadImage(context, image, imageView, loading);

        btnBack.setOnClickListener(v -> back());
        supportStartPostponedEnterTransition();
    }

    private void back() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
