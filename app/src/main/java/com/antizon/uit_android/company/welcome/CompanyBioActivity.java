package com.antizon.uit_android.company.welcome;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.antizon.uit_android.R;
import com.antizon.uit_android.company.utility.BaseActivity;
import com.antizon.uit_android.utilities.CustomCookieToast;
import com.antizon.uit_android.utilities.Utilities;

public class CompanyBioActivity extends BaseActivity {

    ImageView backIcon,redNoah2;
    TextView next;
    EditText typeHere;
    String typeHereValue = "", companyNameHintValue = "", encodedImageData = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_bio);
        Utilities.setWhiteBars(CompanyBioActivity.this);
        initViews();

    }


    private void initViews() {
        backIcon = findViewById(R.id.backIcon);
        next = findViewById(R.id.next);
        redNoah2 = findViewById(R.id.redNoah2);
        typeHere = findViewById(R.id.typeHere);
        if (getIntent() != null) {
            encodedImageData = getIntent().getStringExtra("profilePic");
            companyNameHintValue = getIntent().getStringExtra("companyName");
            loadProfile(CompanyBioActivity.this, encodedImageData, redNoah2);
        }

        backIcon.setOnClickListener(v -> onBackPressed());

        next.setOnClickListener(v -> {
            hideSoftKeyboard(CompanyBioActivity.this, typeHere);
            typeHereValue = typeHere.getText().toString().trim();
            if (typeHereValue.isEmpty()) {
                CustomCookieToast.showRequiredToast(CompanyBioActivity.this, "Please enter company bio");
            }else {
                openNextScreen();
            }
        });
    }

    void openNextScreen() {
        Intent intent = new Intent(CompanyBioActivity.this, CompanyIndustryActivity.class);
        intent.putExtra("profilePic", encodedImageData);
        intent.putExtra("companyName", companyNameHintValue);
        intent.putExtra("bio", typeHereValue);
        startActivity(intent);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
    }
}