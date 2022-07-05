package com.antizon.uit_android.recruiter.welcome;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.antizon.uit_android.R;
import com.antizon.uit_android.company.utility.BaseActivity;
import com.antizon.uit_android.generic.model.ModelApplicantJobs;
import com.hbb20.CountryCodePicker;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MemberPhone extends BaseActivity {
    private static final String TAG = MemberPhone.class.getSimpleName();

    ImageView backIcon,menYellow;
    TextView next,countryCode;
    EditText phone;

    String encodedImageData = "", fullNameEditTextValue = "", jobTitleValue = "", emailValue = "", codeValue="",
            phoneValue="",selected_country_code;

    CountryCodePicker selectCountryCode;
    private List<ModelApplicantJobs> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_phone);
        setIds();
        getIntentData();
        initialize();
        setListener();

    }


    void setIds() {
        Log.d(TAG, "setIds: ");

        backIcon = findViewById(R.id.backIcon);
        next = findViewById(R.id.next);
        phone = findViewById(R.id.phone);
        menYellow = findViewById(R.id.menYellow);
        countryCode = findViewById(R.id.countryCode);
        selectCountryCode = findViewById(R.id.selectCountryCode);

    }

    void getIntentData() {

        if (getIntent() != null) {

            encodedImageData = getIntent().getStringExtra("profilePic");
            fullNameEditTextValue = getIntent().getStringExtra("fullName");
            emailValue = getIntent().getStringExtra("emailAddress");
            codeValue = getIntent().getStringExtra("verification");

            Bundle b = getIntent().getExtras();
            list = b.getParcelableArrayList("jobs");

        }
    }
    void initialize() {
        Log.d(TAG, "initialize: ");
        loadProfile(MemberPhone.this, encodedImageData, menYellow);
        list = new ArrayList<>();

    }


    void setListener() {
        Log.d(TAG, "setListener: ");

        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
                overridePendingTransition(R.anim.right_in, R.anim.right_out);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard(MemberPhone.this, phone);
                if (!validate()) {

                } else {
                    openNextScreen();
                    Log.d(TAG, "onClick: enterPhoneNumber");
                }
            }
        });
        selectCountryCode.setOnCountryChangeListener(
                new CountryCodePicker.OnCountryChangeListener() {
                    @Override
                    public void onCountrySelected() {
                        selected_country_code = selectCountryCode.getSelectedCountryCodeWithPlus();
                        countryCode.setText(selected_country_code);
                    }
                });
    }

    public boolean validate() {
        Log.d(TAG, "validate: ");
        phoneValue = phone.getText().toString().trim();

        boolean valid = true;
        if (phoneValue.isEmpty()) {
            phone.setError("Please enter your phone number");
            valid = false;
        } else {
            phoneValue = countryCode + " " + phoneValue;
        }
        return valid;
    }

    void openNextScreen() {

        Bundle bundle = new Bundle();
        bundle.putSerializable("Job", (Serializable) list);

        Intent intent = new Intent(MemberPhone.this, Password.class);
        intent.putExtra("profilePic", encodedImageData);
        intent.putExtra("fullName", fullNameEditTextValue);
        intent.putExtra("emailAddress", emailValue);
        intent.putExtra("job", jobTitleValue);
        intent.putExtra("verification", codeValue);
        intent.putExtra("memberPhone", phoneValue);

        startActivity(intent);
    }
}