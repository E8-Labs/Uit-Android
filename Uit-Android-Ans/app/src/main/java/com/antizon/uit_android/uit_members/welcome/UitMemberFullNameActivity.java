package com.antizon.uit_android.uit_members.welcome;


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

public class UitMemberFullNameActivity extends BaseActivity {

    ImageView menYellow, backIcon;
    TextView next;
    EditText fullNameEditText;
    String fullNameEditTextValue = "", encodedImageData = "";
    boolean done = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uit_member_full_name);
        Utilities.setWhiteBars(UitMemberFullNameActivity.this);

        initViews();

        setUserNameTextWatcher();
    }


    private void initViews() {
        if (getIntent() != null) {
            encodedImageData = getIntent().getStringExtra("profilePic");
        }
        backIcon = findViewById(R.id.backIcon);
        next = findViewById(R.id.next);
        menYellow = findViewById(R.id.menYellow);
        fullNameEditText = findViewById(R.id.fullNameEditText);

        loadProfile(UitMemberFullNameActivity.this, encodedImageData, menYellow);


        backIcon.setOnClickListener(v -> onBackPressed());

        next.setOnClickListener(v -> {
            hideSoftKeyboard(UitMemberFullNameActivity.this, fullNameEditText);
            fullNameEditTextValue = fullNameEditText.getText().toString().trim();
            if (fullNameEditTextValue.isEmpty()) {
                CustomCookieToast.showRequiredToast(UitMemberFullNameActivity.this, "Please enter your full name");
            }else if (fullNameEditTextValue.matches(".*[@#$%^&*()_+/.:;|,{}?]+.*") || fullNameEditTextValue.matches(".*[0-9]+.*")) {
                CustomCookieToast.showRequiredToast(UitMemberFullNameActivity.this, "Please enter valid name");
            } else {
                openNextScreen();
            }
        });
    }

    private void openNextScreen() {
        Intent intent = new Intent(UitMemberFullNameActivity.this, UitMemberEmailAddressActivity.class);
        intent.putExtra("profilePic", encodedImageData);
        intent.putExtra("fullName", fullNameEditTextValue);
        startActivity(intent);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
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