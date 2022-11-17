package com.antizon.uit_android.utilities;

import android.app.Activity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.antizon.uit_android.R;
import org.aviran.cookiebar2.CookieBar;

import retrofit2.Response;

public class CustomCookieToast {
    public static final int DURATION = 3000;

    public static void showSuccessToast(Activity context, String msg) {

        CookieBar.build(context).
                setCustomView(R.layout.custom_banner)
                .setCustomViewInitializer(view -> {
                    LinearLayout cookie = view.findViewById(R.id.ll_cookie);
                    ImageView ivIcon = view.findViewById(R.id.iv_icon);
                    TextView tvTitle = view.findViewById(R.id.tv_title);
                    TextView tvMessage = view.findViewById(R.id.tv_message);
                    tvTitle.setText("Success!");
                    tvMessage.setText(msg);
                    ivIcon.setImageResource(R.drawable.ic_happiness);
                    cookie.setBackgroundResource(R.drawable.app_color_10dp_rounded);
                })
                .setCookiePosition(CookieBar.TOP)
                .setDuration(DURATION)
                .setEnableAutoDismiss(true) // Cookie will stay on display until manually dismissed
                .setSwipeToDismiss(true)    // Deny dismiss by swiping off the view
                .show();
    }





    public static void showFailureToast(Activity context, String msg) {

        CookieBar.build(context).
                setCustomView(R.layout.custom_banner)
                .setCustomViewInitializer(view -> {
                    LinearLayout cookie = view.findViewById(R.id.ll_cookie);
                    ImageView ivIcon = view.findViewById(R.id.iv_icon);
                    TextView tvTitle = view.findViewById(R.id.tv_title);
                    TextView tvMessage = view.findViewById(R.id.tv_message);
                    tvTitle.setText("Failed!");
                    tvMessage.setText(msg);
                    ivIcon.setImageResource(R.drawable.ic_warning_sign);
                    cookie.setBackgroundResource(R.drawable.app_color_10dp_rounded);
                })
                .setCookiePosition(CookieBar.TOP)
                .setDuration(DURATION)
                .setEnableAutoDismiss(true) // Cookie will stay on display until manually dismissed
                .setSwipeToDismiss(true)    // Deny dismiss by swiping off the view
                .show();
    }

    public static void showSuccessToast(Activity context, String title, String msg) {

        CookieBar.build(context).
                setCustomView(R.layout.custom_banner)
                .setCustomViewInitializer(view -> {

                    LinearLayout cookie = view.findViewById(R.id.ll_cookie);
                    ImageView ivIcon = view.findViewById(R.id.iv_icon);
                    TextView tvTitle = view.findViewById(R.id.tv_title);
                    TextView tvMessage = view.findViewById(R.id.tv_message);

                    tvTitle.setText(title);
                    tvMessage.setText(msg);
                    ivIcon.setImageResource(R.drawable.ic_happiness);
                    cookie.setBackgroundResource(R.drawable.app_color_10dp_rounded);
                })
                .setCookiePosition(CookieBar.TOP)
                .setDuration(DURATION)
                .setEnableAutoDismiss(true) // Cookie will stay on display until manually dismissed
                .setSwipeToDismiss(true)    // Deny dismiss by swiping off the view
                .show();
    }

    public static void showFailureToast(Activity context, String title, String msg) {

        CookieBar.build(context).
                setCustomView(R.layout.custom_banner)
                .setCustomViewInitializer(view -> {

                    LinearLayout cookie = view.findViewById(R.id.ll_cookie);
                    ImageView ivIcon = view.findViewById(R.id.iv_icon);
                    TextView tvTitle = view.findViewById(R.id.tv_title);
                    TextView tvMessage = view.findViewById(R.id.tv_message);

                    tvTitle.setText(title);
                    tvMessage.setText(msg);
                    ivIcon.setImageResource(R.drawable.ic_warning_sign);
                    cookie.setBackgroundResource(R.drawable.app_color_10dp_rounded);
                })
                .setCookiePosition(CookieBar.TOP)
                .setDuration(DURATION)
                .setEnableAutoDismiss(true) // Cookie will stay on display until manually dismissed
                .setSwipeToDismiss(true)    // Deny dismiss by swiping off the view
                .show();

    }

    public static void showRequiredToast(Activity context, String pleaseEnter) {

        CookieBar.build(context).
                setCustomView(R.layout.custom_banner)
                .setCustomViewInitializer(view -> {

                    LinearLayout cookie = view.findViewById(R.id.ll_cookie);
                    ImageView ivIcon = view.findViewById(R.id.iv_icon);
                    TextView tvTitle = view.findViewById(R.id.tv_title);
                    TextView tvMessage = view.findViewById(R.id.tv_message);

                    tvTitle.setText("Required!");
                    tvMessage.setText(pleaseEnter);
                    ivIcon.setImageResource(R.drawable.ic_warning_sign);
                    cookie.setBackgroundResource(R.drawable.app_color_10dp_rounded);
                })
                .setCookiePosition(CookieBar.TOP)
                .setDuration(DURATION)
                .setEnableAutoDismiss(true) // Cookie will stay on display until manually dismissed
                .setSwipeToDismiss(true)    // Deny dismiss by swiping off the view
                .show();
    }

    public static void showSelectToast(Activity context, String title, String pleaseSelect) {

        CookieBar.build(context).
                setCustomView(R.layout.custom_banner)
                .setCustomViewInitializer(view -> {

                    LinearLayout cookie = view.findViewById(R.id.ll_cookie);
                    ImageView ivIcon = view.findViewById(R.id.iv_icon);
                    TextView tvTitle = view.findViewById(R.id.tv_title);
                    TextView tvMessage = view.findViewById(R.id.tv_message);

                    tvTitle.setText(title);
                    tvMessage.setText( pleaseSelect);
                    ivIcon.setImageResource(R.drawable.ic_warning_sign);
                    cookie.setBackgroundResource(R.drawable.app_color_10dp_rounded);
                })
                .setCookiePosition(CookieBar.TOP)
                .setDuration(DURATION)
                .setEnableAutoDismiss(true) // Cookie will stay on display until manually dismissed
                .setSwipeToDismiss(true)    // Deny dismiss by swiping off the view
                .show();
    }


    public static void showExceptionToast(Activity context, Throwable t) {

        CookieBar.build(context).
                setCustomView(R.layout.custom_banner)
                .setCustomViewInitializer(view -> {

                    LinearLayout cookie = view.findViewById(R.id.ll_cookie);
                    ImageView ivIcon = view.findViewById(R.id.iv_icon);
                    TextView tvTitle = view.findViewById(R.id.tv_title);
                    TextView tvMessage = view.findViewById(R.id.tv_message);

                    tvTitle.setText("Exception!");
                    tvMessage.setText(t.getMessage());
                    ivIcon.setImageResource(R.drawable.ic_warning_sign);
                    cookie.setBackgroundResource(R.drawable.app_color_10dp_rounded);
                })
                .setCookiePosition(CookieBar.TOP)
                .setDuration(DURATION + 1000)
                .setEnableAutoDismiss(true) // Cookie will stay on display until manually dismissed
                .setSwipeToDismiss(true)    // Deny dismiss by swiping off the view
                .show();
        t.printStackTrace();
    }

    public static void showNoResponseToast(Activity context, Response response) {
        int code = response.code();
        String message = "The server cannot process the request at the moment.";
        String title = "No Response!";

        switch (code) {

            case 401:
            case 403:
                title = "Forbidden!";
                message = "You do not have the permission to perform this action.";
                break;

            case 404:
                title = "Not Found!";
                message = "The requested resource could not be found.";
                break;

            case 405:
                title = "Method Not Allowed!";
                message = "The request method is not supported for the requested resource.";
                break;

            case 408:
                title = "Request Timeout!";
                message = "The server timed out waiting for the request.";
                break;

            case 409:
                title = "Conflict!";
                message = "The request could not be processed because of conflict in the current state of the resource.";
                break;

            case 411:
                title = "Length Required!";
                message = "The request did not specify the length of its content, which is required by the requested resource.";
                break;

            case 413:
                title = "Payload Too Large!";
                message = "The request payload is too large to process.";
                break;

            case 414:
                title = "URI Too Long!";
                message = "The request URI is too long to process.";
                break;

            case 415:
                title = "Unsupported Media Type!";
                message = "The media type provided is not supported by the server.";
                break;

            case 429:
                title = "Too Many Requests!";
                message = "You have sent astronomically too many requests that a normal human being should not send.";
                break;

            case 500:
                title = "Internal Server Error!";
                break;

            case 503:
                title = "Service Unavailable!";
                message = "The server cannot process the request right now.";
                break;


        }

        String finalMessage = message;
        String finalTitle = title;
        CookieBar.build(context).
                setCustomView(R.layout.custom_banner)
                .setCustomViewInitializer(view -> {

                    LinearLayout cookie = view.findViewById(R.id.ll_cookie);
                    ImageView ivIcon = view.findViewById(R.id.iv_icon);
                    TextView tvTitle = view.findViewById(R.id.tv_title);
                    TextView tvMessage = view.findViewById(R.id.tv_message);

                    tvTitle.setText(finalTitle + " Error " + code);
                    tvMessage.setText(finalMessage);
                    ivIcon.setImageResource(R.drawable.ic_warning_sign);
                    cookie.setBackgroundResource(R.drawable.app_color_10dp_rounded);
                })
                .setCookiePosition(CookieBar.TOP)
                .setDuration(DURATION)
                .setEnableAutoDismiss(true) // Cookie will stay on display until manually dismissed
                .setSwipeToDismiss(true)    // Deny dismiss by swiping off the view
                .show();
    }
}
