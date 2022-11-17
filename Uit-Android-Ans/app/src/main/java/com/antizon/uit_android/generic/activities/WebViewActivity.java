package com.antizon.uit_android.generic.activities;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.antizon.uit_android.R;
import com.antizon.uit_android.utilities.Utilities;
public class WebViewActivity extends AppCompatActivity {
    Context context;

    WebView webView;
    RelativeLayout btnBack, layoutLoader;
    TextView text_title;
    String pageTitle, pageUrl;
    ProgressBar progressBar;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        Utilities.setWhiteBars(WebViewActivity.this);
        context = WebViewActivity.this;

        pageTitle = getIntent().getStringExtra( "pageTitle");
        pageUrl = getIntent().getStringExtra( "pageUrl");

        webView = findViewById(R.id.webView);
        text_title = findViewById(R.id.text_title);
        btnBack = findViewById(R.id.btnBack);
        layoutLoader = findViewById(R.id.layoutLoader);
        progressBar = findViewById(R.id.progressBar);

        btnBack.setOnClickListener(view -> onBackPressed());
        text_title.setText(pageTitle);


        WebSettings webSettings = webView.getSettings();

        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setUseWideViewPort(false);
        webSettings.setGeolocationEnabled(true);
        webSettings.setSupportMultipleWindows(true); // This forces ChromeClient enabled.

        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                setProgressAnimate(progressBar, progress);
                if (progress == 100) {
                    setProgressAnimate(progressBar, 100);
                    new Handler(Looper.myLooper()).postDelayed(() -> layoutLoader.setVisibility(View.GONE), 700);
                }
            }
        });

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;
            }
        });

        webView.loadUrl(pageUrl);
    }

    @Override
    public void onBackPressed(){
        if (webView.canGoBack()){
            webView.goBack();
        }else {
            super.onBackPressed();
            overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
        }
    }


    private void setProgressAnimate(ProgressBar pb, int progressTo) {
        ObjectAnimator animation = ObjectAnimator.ofInt(pb, "progress", pb.getProgress(), progressTo);
        animation.setDuration(500);
        animation.setInterpolator(new DecelerateInterpolator());
        animation.start();
    }
}