package com.antizon.uit_android.utilities;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.antizon.uit_android.R;
import com.shasin.notificationbanner.Banner;


public class CustomBannerToast {
    public static final int DURATION = 3000;

    public static void showRequiredToast(View view, Activity context, String pleaseEnter) {
        Banner.make(view, context, Banner.TOP, R.layout.custom_banner);

        View bannerView =  Banner.getInstance().getBannerView();
        LinearLayout ll_cookie = bannerView.findViewById(R.id.ll_cookie);
        TextView tv_message = bannerView.findViewById(R.id.tv_message);
        TextView tv_title =  bannerView.findViewById(R.id.tv_title);
        ImageView ivIcon = bannerView.findViewById(R.id.iv_icon);


        tv_message.setText(pleaseEnter);
        tv_title.setText("Required!");
        ivIcon.setImageResource(R.drawable.ic_warning_sign);

        Banner.getInstance().setCustomAnimationStyle(R.style.topAnimation);
        Banner.getInstance().setDuration(DURATION);
        Banner.getInstance().show();

        ll_cookie.setOnClickListener(view1 -> Banner.getInstance().dismissBanner());
    }

    public static void showSuccessToast(View view, Activity context, String pleaseEnter) {
        Banner.make(view, context, Banner.TOP, R.layout.custom_banner);

        View bannerView =  Banner.getInstance().getBannerView();
        LinearLayout ll_cookie = bannerView.findViewById(R.id.ll_cookie);
        TextView tv_message = bannerView.findViewById(R.id.tv_message);
        TextView tv_title =  bannerView.findViewById(R.id.tv_title);
        ImageView ivIcon = bannerView.findViewById(R.id.iv_icon);


        ivIcon.setImageResource(R.drawable.ic_happiness);
        tv_message.setText(pleaseEnter);
        tv_title.setText("Success!");

        Banner.getInstance().setCustomAnimationStyle(R.style.topAnimation);
        Banner.getInstance().setDuration(DURATION);
        Banner.getInstance().show();

        ll_cookie.setOnClickListener(view1 -> Banner.getInstance().dismissBanner());
    }


    public static void showSuccessToastWithTitle(View view, Activity context, String title, String pleaseEnter) {
        Banner.make(view, context, Banner.TOP, R.layout.custom_banner);

        View bannerView =  Banner.getInstance().getBannerView();
        LinearLayout ll_cookie = bannerView.findViewById(R.id.ll_cookie);
        TextView tv_message = bannerView.findViewById(R.id.tv_message);
        TextView tv_title =  bannerView.findViewById(R.id.tv_title);
        ImageView ivIcon = bannerView.findViewById(R.id.iv_icon);

        ivIcon.setImageResource(R.drawable.ic_happiness);
        tv_message.setText(pleaseEnter);
        tv_title.setText(title);

        Banner.getInstance().setCustomAnimationStyle(R.style.topAnimation);
        Banner.getInstance().setDuration(DURATION);
        Banner.getInstance().show();

        ll_cookie.setOnClickListener(view1 -> Banner.getInstance().dismissBanner());
    }


    public static void showErrorToast(View view, Activity context, String title, String pleaseEnter) {

        Banner.make(view, context, Banner.TOP, R.layout.custom_banner);

        View bannerView =  Banner.getInstance().getBannerView();
        LinearLayout ll_cookie = bannerView.findViewById(R.id.ll_cookie);
        TextView tv_message = bannerView.findViewById(R.id.tv_message);
        TextView tv_title =  bannerView.findViewById(R.id.tv_title);
        ImageView ivIcon = bannerView.findViewById(R.id.iv_icon);

        ivIcon.setImageResource(R.drawable.ic_warning_sign);
        tv_message.setText(pleaseEnter);
        tv_title.setText(title);

        Banner.getInstance().setCustomAnimationStyle(R.style.topAnimation);
        Banner.getInstance().setDuration(DURATION);
        Banner.getInstance().show();

        ll_cookie.setOnClickListener(view1 -> Banner.getInstance().dismissBanner());
    }

    public static void showFailureToast(View view, Activity context, String title, String pleaseEnter) {
        Banner.make(view, context, Banner.TOP, R.layout.custom_banner);

        View bannerView =  Banner.getInstance().getBannerView();
        LinearLayout ll_cookie = bannerView.findViewById(R.id.ll_cookie);
        TextView tv_message = bannerView.findViewById(R.id.tv_message);
        TextView tv_title =  bannerView.findViewById(R.id.tv_title);
        ImageView ivIcon = bannerView.findViewById(R.id.iv_icon);

        ivIcon.setImageResource(R.drawable.ic_warning_sign);
        tv_message.setText(pleaseEnter);
        tv_title.setText(title);

        Banner.getInstance().setCustomAnimationStyle(R.style.topAnimation);
        Banner.getInstance().setDuration(DURATION);
        Banner.getInstance().show();

        ll_cookie.setOnClickListener(view1 -> Banner.getInstance().dismissBanner());
    }
}
