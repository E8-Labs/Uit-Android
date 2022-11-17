package com.antizon.uit_android.applicant.welcome;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.antizon.uit_android.R;
import com.antizon.uit_android.company.utility.BaseActivity;
import com.antizon.uit_android.utilities.CustomCookieToast;
import com.antizon.uit_android.utilities.Utilities;

public class ApplicantFullNameActivity extends BaseActivity {
    private static final String TAG = ApplicantFullNameActivity.class.getSimpleName();

    ImageView backIcon,menYellow;
    TextView next;
    EditText applicantName;
    String applicantNameValue="", encodedImageData = "";
    boolean done = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applicant_full_name);
        Utilities.setWhiteBars(ApplicantFullNameActivity.this);

        setIds();
        getIntentData();
        initialize();
        setListener();

    }


    void setIds() {
        backIcon = findViewById(R.id.backIcon);
        next = findViewById(R.id.next);
        applicantName = findViewById(R.id.applicantName);
        menYellow = findViewById(R.id.menYellow);


        setUserNameTextWatcher();

    }

    void getIntentData() {
        if (getIntent() != null) {
            encodedImageData = getIntent().getStringExtra("profilePic");
            Log.d(TAG, "getIntentData: image: " + encodedImageData);
        }
    }

    void initialize(){
        loadProfile(ApplicantFullNameActivity.this, encodedImageData, menYellow);
    }

    void setListener()
    {
        backIcon.setOnClickListener(v -> onBackPressed());

        next.setOnClickListener(v -> {
            hideSoftKeyboard(ApplicantFullNameActivity.this, applicantName);
            applicantNameValue = applicantName.getText().toString().trim();
            if (applicantNameValue.isEmpty()) {
                CustomCookieToast.showRequiredToast(ApplicantFullNameActivity.this, "Please enter your full name");
            }else {
                openNextScreen();
            }
        });
    }

    private void setUserNameTextWatcher() {
        applicantName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!done) {
                    String validUsername = getValidUserName(s.toString());
                    applicantName.setText(validUsername);
                    applicantName.setSelection(validUsername.length());
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


    void openNextScreen() {
        Intent intent = new Intent(ApplicantFullNameActivity.this, ApplicantEmailAddressActivity.class);
        intent.putExtra("from", "add");
        intent.putExtra("profilePic", encodedImageData);
        intent.putExtra("applicantName", applicantNameValue);
        startActivity(intent);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }
}