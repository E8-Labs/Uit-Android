package com.antizon.uit_android.generic.activities;

import static android.text.Html.FROM_HTML_MODE_LEGACY;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;
import com.antizon.uit_android.R;
import com.antizon.uit_android.company.utility.BaseActivity;


public class SignUpActivity extends BaseActivity {

    TextView createAccount, orSignIn, byTapping;

    String agreeText = "By tapping creating an account, you agree with our <b><font color='#D6B226'>Terms and Condition </b> .  Learn how we process your data in our  <b><font color='#D6B226'>Privacy Policy</b> and  <b><font color='#D6B226'>Cookies Policy</b>.";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        setIds();
        setListener();

    }


    void setIds() {
        createAccount = findViewById(R.id.createAccount);
        orSignIn = findViewById(R.id.orSignIn);
        byTapping = findViewById(R.id.byTapping);
    }

    void setListener() {

        createAccount.setOnClickListener(v -> moveToNextScreen(AccountTypeActivity.class));

        orSignIn.setOnClickListener(v -> {
            moveToNextScreen(SignInActivity.class);
            finish();
        });

        SpannableString ss = new SpannableString(Html.fromHtml(agreeText, FROM_HTML_MODE_LEGACY));

        ClickableSpan termsSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                goToWebViewActivity("Terms and Condition", "https://www.usintechnology.com/platform-terms");
            }

            @Override
            public void updateDrawState(final TextPaint textPaint) {
                textPaint.setColor(getColor(R.color.yellow));
            }
        };

        ClickableSpan privacyPolicySpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                goToWebViewActivity("Privacy Policy", "https://www.usintechnology.com/privacy-policy");
            }

            @Override
            public void updateDrawState(final TextPaint textPaint) {
                textPaint.setColor(getColor(R.color.yellow));
            }
        };

        ClickableSpan cookiesSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                goToWebViewActivity("Cookies Policy", "https://www.usintechnology.com/cookie-policy");
            }

            @Override
            public void updateDrawState(final TextPaint textPaint) {
                textPaint.setColor(getColor(R.color.yellow));
            }
        };


        ss.setSpan(termsSpan, 50, 71, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(privacyPolicySpan, 110, 124, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(cookiesSpan, 129, 143, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        byTapping.setText(ss);
        byTapping.setMovementMethod(LinkMovementMethod.getInstance());
        byTapping.setHighlightColor(Color.TRANSPARENT);
    }


    private void goToWebViewActivity(String title, String pageUrl){
        Intent intent = new Intent(SignUpActivity.this, WebViewActivity.class);
        intent.putExtra("pageTitle", title);
        intent.putExtra("pageUrl", pageUrl);
        startActivity(intent);
        overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
    }

    private void moveToNextScreen(Class<?> className){
        Intent intent = new Intent(SignUpActivity.this, className);
        startActivity(intent);
        overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
    }
}