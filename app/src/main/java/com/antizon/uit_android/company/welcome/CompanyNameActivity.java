package com.antizon.uit_android.company.welcome;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.antizon.uit_android.R;
import com.antizon.uit_android.company.utility.BaseActivity;
import com.antizon.uit_android.utilities.CustomCookieToast;
import com.antizon.uit_android.utilities.Utilities;

public class CompanyNameActivity extends BaseActivity {

    EditText companyNameHint;
    ImageView backIcon,redNoah2;
    String companyNameHintValue = "", encodedImageData = "";
    TextView next;

    boolean done;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_name);
        Utilities.setWhiteBars(CompanyNameActivity.this);

        initViews();

        setUserNameTextWatcher();
    }


    void initViews() {
        redNoah2 = findViewById(R.id.redNoah2);
        backIcon = findViewById(R.id.backIcon);
        next = findViewById(R.id.next);
        companyNameHint = findViewById(R.id.companyNameHint);
        if (getIntent() != null) {
            encodedImageData = getIntent().getStringExtra("profilePic");
            loadProfile(CompanyNameActivity.this, encodedImageData, redNoah2);
        }

        backIcon.setOnClickListener(v -> onBackPressed());

        next.setOnClickListener(v -> {
            hideSoftKeyboard(CompanyNameActivity.this, companyNameHint);
            companyNameHintValue = companyNameHint.getText().toString().trim();
            if (companyNameHintValue.isEmpty()) {
                CustomCookieToast.showRequiredToast(CompanyNameActivity.this, "Please enter company name");
            }else {
                openNextScreen();
            }
        });
    }


    private void openNextScreen() {
        Intent intent = new Intent(CompanyNameActivity.this, CompanyBioActivity.class);
        intent.putExtra("profilePic", encodedImageData);
        intent.putExtra("companyName", companyNameHintValue);
        startActivity(intent);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }

    private void setUserNameTextWatcher() {
        companyNameHint.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!done) {
                    String validUsername = getValidUserName(s.toString());
                    companyNameHint.setText(validUsername);
                    companyNameHint.setSelection(validUsername.length());
                } else {
                    done = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private String getValidUserName(String name) {
        done = true;
        for (int i = 0; i < name.length(); i++) {
            char ch = name.charAt(i);
            if ((ch < 'a' || ch > 'z') && (ch < 'A' || ch > 'Z') && (ch < '0' || ch > '9') && ch != '_' && ch != '.' && ch != ' ') {
                name = name.substring(0, i) + name.substring(i + 1);
            }
        }
        return name;
    }
}