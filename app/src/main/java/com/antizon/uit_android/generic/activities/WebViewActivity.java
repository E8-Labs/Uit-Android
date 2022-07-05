package com.antizon.uit_android.generic.activities;

import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.antizon.uit_android.R;
import com.antizon.uit_android.utilities.Utilities;

public class WebViewActivity extends AppCompatActivity {

    String pageTitle;
    RelativeLayout btnBack;
    TextView text_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        Utilities.setWhiteBars(WebViewActivity.this);
        pageTitle = getIntent().getStringExtra("pageTitle");

        btnBack = findViewById(R.id.btnBack);
        text_title = findViewById(R.id.text_title);

        btnBack.setOnClickListener(view -> onBackPressed());

        text_title.setText(pageTitle);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }
}