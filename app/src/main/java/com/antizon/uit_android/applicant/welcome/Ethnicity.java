package com.antizon.uit_android.applicant.welcome;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.antizon.uit_android.R;
import com.antizon.uit_android.company.utility.BaseActivity;

public class Ethnicity extends BaseActivity {
    private static final String TAG = Ethnicity.class.getSimpleName();
    ImageView backIcon;
    TextView yes, skip;

    String ethnicityValue="", reminderValue="",passwordEditTextValue="",  selectDobValue=""
            ,  phoneValue = "", codeValue = "", emailAddressEditTextValue = "", applicantNameValue,
            encodedImageData = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ethnicity);
        setIds();
        initialize();
        setListener();
        getIntentData();
    }


    void setIds() {
        Log.d(TAG, "setIds: ");

        backIcon = findViewById(R.id.backIcon);
        yes = findViewById(R.id.yes);
        skip = findViewById(R.id.skip);
    }
    void initialize(){
        Log.d(TAG, "initialize: ");



    }
    void getIntentData() {

        encodedImageData = getIntent().getStringExtra("profilePic");
        applicantNameValue = getIntent().getStringExtra("applicantName");
        emailAddressEditTextValue = getIntent().getStringExtra("emailAddress");
        codeValue = getIntent().getStringExtra("verificationCode");
        phoneValue = getIntent().getStringExtra("phoneNumber");
        selectDobValue = getIntent().getStringExtra("dob");
        passwordEditTextValue = getIntent().getStringExtra("password");
        reminderValue = getIntent().getStringExtra("reminder");
    }
    void setListener()
    {
        Log.d(TAG, "setListener: ");

        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
                overridePendingTransition(R.anim.right_in, R.anim.right_out);
            }
        });

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNextScreen();
            }
        });
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNextScreen();
//                Intent intent = new Intent(Ethnicity.this,Race.class);
//                startActivity(intent);
//                overridePendingTransition(R.anim.left_out,R.anim.left_out);
            }
        });
    }
    void openNextScreen() {

        Intent intent = new Intent(Ethnicity.this, ActivityApplicantSelectRace.class);

        intent.putExtra("profilePic", encodedImageData);
        intent.putExtra("applicantName", applicantNameValue);
        intent.putExtra("emailAddress", emailAddressEditTextValue);
        intent.putExtra("verificationCode", codeValue);
        intent.putExtra("phoneNumber", phoneValue);
        intent.putExtra("dob", selectDobValue);
        intent.putExtra("password", passwordEditTextValue);
        intent.putExtra("reminder", reminderValue);
        intent.putExtra("ethnicity", ethnicityValue);
        startActivity(intent);
    }
}