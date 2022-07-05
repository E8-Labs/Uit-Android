package com.antizon.uit_android.company.welcome;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.antizon.uit_android.R;
import com.antizon.uit_android.company.utility.BaseActivity;
import com.antizon.uit_android.utilities.CustomCookieToast;
import com.antizon.uit_android.utilities.Utilities;

public class CompanyNameActivity extends BaseActivity {

    private static final String TAG = CompanyNameActivity.class.getSimpleName();
    EditText companyNameHint;
    ImageView backIcon,redNoah2;
    String companyNameHintValue = "", encodedImageData = "";
    TextView next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_name);
        Utilities.setWhiteBars(CompanyNameActivity.this);

        setIds();
        getIntentData();
        initialize();

        setListener();
    }


    void setIds() {
        Log.d(TAG, "setIds: ");
        redNoah2 = findViewById(R.id.redNoah2);
        backIcon = findViewById(R.id.backIcon);
        next = findViewById(R.id.next);
        companyNameHint = findViewById(R.id.companyNameHint);
    }
    void getIntentData() {
        if (getIntent() != null) {
            encodedImageData = getIntent().getStringExtra("profilePic");
            Log.d(TAG, "getIntentData: image: " + encodedImageData);
        }
    }
    void initialize() {
        Log.d(TAG, "initialize: ");
        loadProfile(CompanyNameActivity.this, encodedImageData, redNoah2);
    }



    void setListener() {
        Log.d(TAG, "setListener: ");

        backIcon.setOnClickListener(v -> onBackPressed());

        next.setOnClickListener(v -> {
            hideSoftKeyboard(CompanyNameActivity.this, companyNameHint);
            companyNameHintValue = companyNameHint.getText().toString().trim();
            if (companyNameHintValue.isEmpty()) {
                CustomCookieToast.showRequiredToast(CompanyNameActivity.this, "Please enter company name");
            }else if (companyNameHintValue.matches(".*[@#$%^&*()_+/.:;|,{}?]+.*") || companyNameHintValue.matches(".*[0-9]+.*")) {
                CustomCookieToast.showRequiredToast(CompanyNameActivity.this, "Please enter company name");
            }else {
                openNextScreen();
            }
        });
    }

    void openNextScreen() {

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
}