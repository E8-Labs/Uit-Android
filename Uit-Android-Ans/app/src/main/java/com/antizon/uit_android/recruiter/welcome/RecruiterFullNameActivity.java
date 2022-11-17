package com.antizon.uit_android.recruiter.welcome;

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

public class RecruiterFullNameActivity extends BaseActivity {
    ImageView backIcon,menYellow;
    TextView next;
    EditText fullNameEditText;
    String fullNameEditTextValue = "", encodedImageData = "", companyId = "";

    boolean done;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recruiter_full_name);
        Utilities.setWhiteBars(RecruiterFullNameActivity.this);
        initViews();

        setUserNameTextWatcher();
    }


    private void initViews() {
        encodedImageData = getIntent().getStringExtra("profilePic");
        companyId = getIntent().getStringExtra("companyId");
        backIcon = findViewById(R.id.backIcon);
        next = findViewById(R.id.next);
        fullNameEditText = findViewById(R.id.fullNameEditText);
        menYellow = findViewById(R.id.menYellow);

        Utilities.loadCircleImage(RecruiterFullNameActivity.this, encodedImageData, menYellow);

        backIcon.setOnClickListener(v -> onBackPressed());

        next.setOnClickListener(v -> {
            fullNameEditTextValue = fullNameEditText.getText().toString().trim();
            if (fullNameEditTextValue.isEmpty()) {
                CustomCookieToast.showRequiredToast(RecruiterFullNameActivity.this, "Please enter your full name");
            } else {
                hideSoftKeyboard(RecruiterFullNameActivity.this, fullNameEditText);
                openNextScreen();
            }
        });
    }



    private void openNextScreen() {
        Intent intent = new Intent(RecruiterFullNameActivity.this, RecruiterSelectJobActivity.class);
        intent.putExtra("profilePic", encodedImageData);
        intent.putExtra("companyId", companyId);
        intent.putExtra("fullName", fullNameEditTextValue);
        intent.putExtra("from", "add");
        startActivity(intent);
        overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
    }


    private void setUserNameTextWatcher() {
        fullNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!done) {
                    String validUsername = getValidUserName(s.toString());
                    fullNameEditText.setText(validUsername);
                    fullNameEditText.setSelection(validUsername.length());
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }
}