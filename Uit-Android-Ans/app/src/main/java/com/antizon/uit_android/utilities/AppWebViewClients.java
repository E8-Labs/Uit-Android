package com.antizon.uit_android.utilities;

import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import com.wang.avi.AVLoadingIndicatorView;

public class AppWebViewClients extends WebViewClient {
    AVLoadingIndicatorView avLoadingIndicatorView;
    String url;

    public AppWebViewClients(AVLoadingIndicatorView avLoadingIndicatorView, String url) {
        this.avLoadingIndicatorView=avLoadingIndicatorView;
        this.url=url;
        avLoadingIndicatorView.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        view.loadUrl(url);
        return true;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        avLoadingIndicatorView.setVisibility(View.GONE);
    }
}

